package alluysl.quilloforigin.mixin;

import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerCommandSource.class)
public interface ServerCommandSourceAccessor {

    @Accessor
    CommandOutput getOutput();

    @Accessor
    boolean getSilent();

    @Invoker
    void callSendToOps(Text message);
}
