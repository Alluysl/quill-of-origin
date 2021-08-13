package alluysl.quilloforigin.mixin;

import alluysl.quilloforigin.power.ScribePower;
import alluysl.quilloforigin.util.ChatMessageEvent;
import net.minecraft.server.command.MessageCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(MessageCommand.class)
public class MessageCommandMixin {

    @Redirect(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendSystemMessage(Lnet/minecraft/text/Text;Ljava/util/UUID;)V"))
    private static void captureIncomingTellMessage(ServerPlayerEntity serverPlayerEntity, Text message, UUID senderUuid){
        ScribePower.captureSystemMessage(serverPlayerEntity, ChatMessageEvent.TELL_INCOMING, message, senderUuid);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method = "method_31164", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendSystemMessage(Lnet/minecraft/text/Text;Ljava/util/UUID;)V"))
    private static void captureOutgoingTellMessage(ServerPlayerEntity serverPlayerEntity, Text message, UUID senderUuid){
        ScribePower.captureSystemMessage(serverPlayerEntity, ChatMessageEvent.TELL_OUTGOING, message, senderUuid);
    }
}
