package alluysl.quilloforigin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ChatBookItem extends WrittenBookItem {

    public ChatBookItem(Settings settings){
        super(settings);
    }

    private static void appendTextToListTag(ListTag listTag, Text text){
        if (listTag.size() <= 100) // I saw somewhere in the code (don't remember where) a check that pages shouldn't go above 100 so don't add if hit the max
            listTag.add(StringTag.of(Text.Serializer.toJson(text)));
    }

    public static void appendText(ItemStack stack, Text text){
        CompoundTag tag = stack.getOrCreateTag();

        if (!tag.contains("title", 8))
            tag.putString("title", "Chat Remnants");
        if (!tag.contains("author", 8))
        tag.putString("author", "The Community");

        if (!tag.contains("pages", 9))
            tag.put("pages", new ListTag());

        ListTag listTag = tag.getList("pages", 8);

        int lastId = listTag.size() - 1;
        if (lastId < 0) // no pages yet
            appendTextToListTag(listTag, text);
        else {
            MutableText lastPage = Text.Serializer.fromJson(listTag.get(lastId).asString());
            if (lastPage != null){
                String candidate = Text.Serializer.toJson(lastPage.append(text));
                if (candidate.length() < 32768) // make sure book will be valid
                    listTag.set(lastId, StringTag.of(candidate));
                else // new string too long
                    appendTextToListTag(listTag, text);
            } else // error reading
                appendTextToListTag(listTag, text);
        }
    }

    private static void makePlayerOpenEditBookScreen(PlayerEntity player, ItemStack book, Hand hand) {
        if (player instanceof ServerPlayerEntity){
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
            if (resolve(book, serverPlayer.getCommandSource(), serverPlayer))
                serverPlayer.currentScreenHandler.sendContentUpdates();
            serverPlayer.networkHandler.sendPacket(new OpenWrittenBookS2CPacket(hand));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        makePlayerOpenEditBookScreen(user, itemStack, hand);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }
}
