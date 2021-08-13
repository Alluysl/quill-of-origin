package alluysl.quilloforigin.mixin;

import alluysl.quilloforigin.QuillOfOrigin;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow public abstract GameRules getGameRules();

    @Inject(method = "tick", at = @At("HEAD"))
    public void getModGameRules(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
        QuillOfOrigin.updateModGameRules(getGameRules());
    }
}
