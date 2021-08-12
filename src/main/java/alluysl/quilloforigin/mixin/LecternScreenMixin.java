package alluysl.quilloforigin.mixin;

import alluysl.quilloforigin.QuillOfOrigin;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.screen.ingame.LecternScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LecternScreen.class)
public abstract class LecternScreenMixin {

    @Redirect(method = "updatePageProvider", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/BookScreen$Contents;create(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/gui/screen/ingame/BookScreen$Contents;"))
    static BookScreen.Contents create(ItemStack stack) {
        Item item = stack.getItem();
        if (item == Items.WRITTEN_BOOK || item == QuillOfOrigin.CHAT_BOOK) {
            return new BookScreen.WrittenBookContents(stack);
        } else {
            return item == Items.WRITABLE_BOOK ? new BookScreen.WritableBookContents(stack) : BookScreen.EMPTY_PROVIDER;
        }
    }
}
