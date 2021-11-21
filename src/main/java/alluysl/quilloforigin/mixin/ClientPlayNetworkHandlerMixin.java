package alluysl.quilloforigin.mixin;

import alluysl.quilloforigin.QuillOfOrigin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Shadow
    private MinecraftClient client;

    @Inject(method = "onOpenWrittenBook", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void openChatBookScreen(OpenWrittenBookS2CPacket packet, CallbackInfo ci, ItemStack itemStack){
        if (itemStack.getItem() == QuillOfOrigin.CHAT_BOOK)
            this.client.setScreen(new BookScreen(new BookScreen.WrittenBookContents(itemStack)));
    }
}
