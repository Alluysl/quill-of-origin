package alluysl.quilloforigin.mixin;

import alluysl.quilloforigin.power.ScribePower;
import alluysl.quilloforigin.util.ChatMessageEvent;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.MeCommand;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(MeCommand.class)
public class MeCommandMixin {

    @Dynamic
    @Redirect(method = "method_13238", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcastChatMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"))
    private static void captureMeMessage(PlayerManager playerManager, Text message, MessageType type, UUID senderUuid){
        ScribePower.captureBroadcastMessage(playerManager, ChatMessageEvent.ME, message, type, senderUuid);
    }
}
