package alluysl.quilloforigin.mixin;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PlayerManager.class)
public interface PlayerManagerAccessor {
    @Accessor
    List<ServerPlayerEntity> getPlayers();
}
