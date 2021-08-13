package alluysl.quilloforigin.mixin;

import alluysl.quilloforigin.QuillOfOrigin;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LecternBlockEntity.class)
public abstract class LecternBlockEntityMixin extends BlockEntity { // extending to get world field

    public LecternBlockEntityMixin(BlockEntityType<?> type){
        super(type);
    }

    @Shadow
    protected abstract ServerCommandSource getCommandSource(@Nullable PlayerEntity player);

    @Inject(method = "resolveBook", at = @At("HEAD"))
    public void makeResolveChatBook(ItemStack book, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir){
        if (world instanceof ServerWorld && book.getItem() == Items.WRITTEN_BOOK) {
            WrittenBookItem.resolve(book, getCommandSource(player), player);
        }
    }

    @Inject(method = "hasBook", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void hasBookIfChatBook(CallbackInfoReturnable<Boolean> cir, Item item){
        if (item == QuillOfOrigin.CHAT_BOOK)
            cir.setReturnValue(true);
    }
}
