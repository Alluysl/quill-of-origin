package alluysl.quilloforigin.mixin;

import alluysl.quilloforigin.QuillOfOrigin;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LecternBlockEntity.class)
public abstract class LecternBlockEntityMixin extends BlockEntity { // extending to get world field

    public LecternBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state){
        super(type, pos, state);
    }

    @Shadow
    protected abstract ServerCommandSource getCommandSource(@Nullable PlayerEntity player);

    @Inject(method = "resolveBook", at = @At("HEAD"))
    public void resolveChatBook(ItemStack book, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir){
        if (world instanceof ServerWorld && book.getItem() == QuillOfOrigin.CHAT_BOOK) {
            WrittenBookItem.resolve(book, getCommandSource(player), player);
        }
    }

    @Shadow
    ItemStack book; // using this rather than getBook because it's probably faster and more future-proof

    @Inject(method = "hasBook", at = @At("HEAD"), cancellable = true)
    public void hasBookIfChatBook(CallbackInfoReturnable<Boolean> cir){
        if (this.book.isOf(QuillOfOrigin.CHAT_BOOK))
            cir.setReturnValue(true);
    }
}
