package alluysl.quilloforigin.power;

import alluysl.quilloforigin.ChatBookItem;
import alluysl.quilloforigin.QuillOfOrigin;
import alluysl.quilloforigin.mixin.PlayerEntityAccessor;
import alluysl.quilloforigin.mixin.PlayerManagerAccessor;
import alluysl.quilloforigin.util.ChatMessage;
import alluysl.quilloforigin.util.ChatMessageEvent;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ScribePower extends Power {

    private final boolean mainhand;
    private final boolean offhand;
    private final boolean hotbar;
    private final boolean inventory;

    private final Consumer<Entity> entityAction;
    private final Consumer<ItemStack> itemAction;

    private final Predicate<ChatMessage> messageCondition;

    public ScribePower(PowerType<?> type, LivingEntity entity,
                       boolean mainhand, boolean offhand, boolean hotbar, boolean inventory,
                       Consumer<Entity> entityAction, Consumer<ItemStack> itemAction,
                       Predicate<ChatMessage> messageCondition){
        super(type, entity);
        this.mainhand = mainhand;
        this.offhand = offhand;
        this.hotbar = hotbar;
        this.inventory = inventory;
        this.entityAction = entityAction;
        this.itemAction = itemAction;
        this.messageCondition = messageCondition;
    }

    public static void handleBroadcastMessage(PlayerManager playerManager, ChatMessageEvent event, Text message, MessageType type, UUID senderUuid){
        // For each scribe power on each player the message is being broadcasted to, log the message and execute the actions if any
        ((PlayerManagerAccessor)playerManager).getPlayers().stream()
            .filter(player -> PowerHolderComponent.hasPower(player, ScribePower.class)).forEach(player ->
                PowerHolderComponent.getPowers(player, ScribePower.class).forEach(scribePower -> scribePower.tryLogMessage(
                    new ChatMessage(player.getServerWorld(), event, message, type, senderUuid)
                )));
    }

    public static void captureBroadcastMessage(PlayerManager playerManager, ChatMessageEvent event, Text message, MessageType type, UUID senderUuid){
        handleBroadcastMessage(playerManager, event, message, type, senderUuid);
        playerManager.broadcastChatMessage(message, type, senderUuid);
    }

    public static void captureBroadcast(PlayerManager playerManager, ChatMessageEvent event, Text message, Function<ServerPlayerEntity, Text> messageFactory, MessageType type, UUID senderUuid){
        handleBroadcastMessage(playerManager, event, message, type, senderUuid);
        playerManager.broadcast(message, messageFactory, type, senderUuid);
    }

    public static void captureSystemMessage(ServerPlayerEntity player, ChatMessageEvent event, Text message, UUID senderUuid){
        // For each scribe power on the player the message is being sent to, log the message and execute the actions if any
        PowerHolderComponent.getPowers(player, ScribePower.class).forEach(scribePower -> scribePower.tryLogMessage(
            new ChatMessage(player.getServerWorld(), event, message, MessageType.SYSTEM, senderUuid)
        ));
        player.sendSystemMessage(message, senderUuid);
    }

    public void tryLogMessage(ChatMessage message){
        if (messageCondition == null || messageCondition.test(message)){
            Set<ItemStack> stacks = new HashSet<>(); // using a set to avoid duplicates (hotbar-mainhand)
            if (mainhand)
                stacks.add(entity.getEquippedStack(EquipmentSlot.MAINHAND));
            if (offhand)
                stacks.add(entity.getEquippedStack(EquipmentSlot.OFFHAND));
            if (entity instanceof PlayerEntityAccessor player){
                if (hotbar)
                    for (int i = 0; i < 9; ++i)
                        stacks.add(player.getInventory().getStack(i));
                if (inventory)
                    for (int i = 9; i < 36; ++i) // only the main 27 slots, not the armor slots
                        stacks.add(player.getInventory().getStack(i));
            }
            AtomicBoolean hasBeenLogged = new AtomicBoolean(false);
            stacks.forEach(stack -> {
                if (stack.getItem() == QuillOfOrigin.CHAT_BOOK){
                    ChatBookItem.appendText(stack, message.getMessage());
                    hasBeenLogged.set(true);
                    if (itemAction != null)
                        itemAction.accept(stack);
                }
            });

            if (entityAction != null && hasBeenLogged.get())
                entityAction.accept(entity);
        }
    }
}
