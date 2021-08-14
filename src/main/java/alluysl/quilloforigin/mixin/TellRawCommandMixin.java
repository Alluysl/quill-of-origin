package alluysl.quilloforigin.mixin;

import alluysl.quilloforigin.power.ScribePower;
import alluysl.quilloforigin.util.ChatMessageEvent;
import net.minecraft.server.command.TellRawCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(TellRawCommand.class)
public class TellRawCommandMixin {

    @Dynamic
    @Redirect(method = "method_13777", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendSystemMessage(Lnet/minecraft/text/Text;Ljava/util/UUID;)V"))
    private static void captureTellRawMessage(ServerPlayerEntity serverPlayerEntity, Text message, UUID senderUuid){
        ScribePower.captureSystemMessage(serverPlayerEntity, ChatMessageEvent.TELLRAW, message, senderUuid);
    }
}
