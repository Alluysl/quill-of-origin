package alluysl.quilloforigin.mixin;

import alluysl.quilloforigin.power.ScribePower;
import alluysl.quilloforigin.util.ChatMessageEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(TameableEntity.class)
public class TameableEntityMixin {

    @Redirect(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;sendSystemMessage(Lnet/minecraft/text/Text;Ljava/util/UUID;)V"))
    public void captureTamedEntityDeathMessage(LivingEntity entity, Text message, UUID senderUuid){
        if (entity instanceof ServerPlayerEntity)
            ScribePower.captureSystemMessage((ServerPlayerEntity)entity, ChatMessageEvent.PET_DEATH, message, senderUuid);
        else // mere future-proofing: onDeath never calls this with a non-ServerPlayerEntity argument in 1.16/1.17
            entity.sendSystemMessage(message, senderUuid);
    }
}
