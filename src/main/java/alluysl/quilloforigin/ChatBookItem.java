package alluysl.quilloforigin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

public class ChatBookItem extends WrittenBookItem {

    public ChatBookItem(Settings settings){
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }
}
