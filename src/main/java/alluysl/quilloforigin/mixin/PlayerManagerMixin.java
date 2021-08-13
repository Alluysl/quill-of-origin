package alluysl.quilloforigin.mixin;

import alluysl.quilloforigin.power.ScribePower;
import alluysl.quilloforigin.util.ChatMessageEvent;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Redirect(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcastChatMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"))
    public void captureConnectionMessage(PlayerManager playerManager, Text message, MessageType type, UUID senderUuid){
        ScribePower.captureBroadcastMessage(playerManager, ChatMessageEvent.CONNECT, message, type, senderUuid);
    }

    @Redirect(method = "sendToTeam", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendSystemMessage(Lnet/minecraft/text/Text;Ljava/util/UUID;)V"))
    public void captureTeamMessage(ServerPlayerEntity serverPlayerEntity, Text message, UUID senderUuid){
        ScribePower.captureSystemMessage(serverPlayerEntity, ChatMessageEvent.TEAM_MESSAGE, message, senderUuid);
    }

    @Redirect(method = "sendToOtherTeams", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendSystemMessage(Lnet/minecraft/text/Text;Ljava/util/UUID;)V"))
    public void captureOtherTeamsMessage(ServerPlayerEntity serverPlayerEntity, Text message, UUID senderUuid){
        ScribePower.captureSystemMessage(serverPlayerEntity, ChatMessageEvent.OTHER_TEAM_MESSAGE, message, senderUuid);
    }
}
