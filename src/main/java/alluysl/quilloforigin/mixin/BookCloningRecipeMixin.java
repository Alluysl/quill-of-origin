package alluysl.quilloforigin.mixin;

import alluysl.quilloforigin.QuillOfOrigin;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.BookCloningRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BookCloningRecipe.class)
public class BookCloningRecipeMixin {

    private boolean isCopyingChatBook;

    @Redirect(method = "matches", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    public Item checkSingleChatBookMatch(ItemStack itemStack){
        // Pretend the item is a written book for this check if it's a chat book, so they have the same behavior in this method
        // (which would have happened naturally if an instanceof WrittenBookItem check was used by Mojang instead of using getItem)
        return itemStack.getItem() == QuillOfOrigin.CHAT_BOOK ? Items.WRITTEN_BOOK : itemStack.getItem();
    }

    @Redirect(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    public Item checkSingleChatBookCraft(ItemStack itemStack){
        // Same thing as above except we also keep which type of book is being crafted in a buffer for use in the injection below
        if (itemStack.getItem() == QuillOfOrigin.CHAT_BOOK)
            isCopyingChatBook = true;
        else if (itemStack.getItem() == Items.WRITTEN_BOOK)
            isCopyingChatBook = false;
        return itemStack.getItem() == QuillOfOrigin.CHAT_BOOK ? Items.WRITTEN_BOOK : itemStack.getItem();
    }

    @ModifyArg(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/item/ItemConvertible;I)V"), index = 0)
    public ItemConvertible makeCopyAChatBook(ItemConvertible item){
        return isCopyingChatBook && QuillOfOrigin.copiesStayChatBooks ? QuillOfOrigin.CHAT_BOOK : item;
    }
}
