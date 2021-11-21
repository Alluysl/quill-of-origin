package alluysl.quilloforigin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ChatBookItem extends WrittenBookItem {

    public ChatBookItem(Settings settings){
        super(settings);
    }

    /**
     * Whether the text would fit in the page.
     * Assumes the worst case scenario by considering all characters to take the largest possible width.
     * Considers the fact that words will only be cut if they started the line and can't even fit in it.
     */
    private static boolean fitsIn(Text text){
        int remainingLines = 14;
        for (String textLine : text.getString().split("\\R", -1)){ // for each text line
            if (textLine.length() > 16){ // if the text line doesn't fit in a book line, fit words as best as possible
                int len = 0; // current book line cumulated length
                for (String word : textLine.split("\\s", -1)){ // for each word
                    if (word.length() + len > 16){ // goes to new line
                        // 16 or less characters would have consumed (16 + 15) / 16 = 1 line (the old line)
                        // 17 to 32 consume 2 lines in total (up to 16 on each line), etc
                        int consumed = Math.max((word.length() + len + 15) / 16, 1);
                        remainingLines -= consumed;
                        len = Math.max(word.length() - 16 * consumed, 0); // remaining characters on last line
                    } else // can fit in the current line
                        len += word.length() + 1;
                }
            } else
                --remainingLines;
        }
        return remainingLines >= 0;
    }

    private static void appendTextToListTag(NbtList listTag, Text text){
        if (listTag.size() <= 100) // I saw somewhere in the code (don't remember where) a check that pages shouldn't go above 100 so don't add if hit the max
            listTag.add(NbtString.of(Text.Serializer.toJson(text)));
    }

    /**
     * Adds text to the chat book(s) in the stack.
     * If the text to add doesn't fit at the end of the last page, puts it on a new page (which might overflow for e.g. tellraw messages from command blocks or functions).
     * Otherwise appends it to the last page.
     */
    public static void appendText(ItemStack stack, Text text){

        MutableText textln = new LiteralText("");
        textln.append(text);
        textln.append(Text.of("\n"));

        NbtCompound tag = stack.getOrCreateNbt();

        if (!tag.contains("title", 8))
            tag.putString("title", "Chat Remnants");
        if (!tag.contains("author", 8))
        tag.putString("author", "The Community");

        if (!tag.contains("pages", 9))
            tag.put("pages", new NbtList());

        NbtList listTag = tag.getList("pages", 8);

        int lastId = listTag.size() - 1;
        if (lastId < 0) // no pages yet
            appendTextToListTag(listTag, textln);
        else {
            MutableText lastPage = Text.Serializer.fromJson(listTag.get(lastId).asString());
            if (lastPage != null){
                String candidate = Text.Serializer.toJson(lastPage.append(textln));
                if (candidate.length() < 32768 && fitsIn(lastPage)) // make sure book will be valid
                    listTag.set(lastId, NbtString.of(candidate));
                else // new string too long
                    appendTextToListTag(listTag, textln);
            } else // error reading
                appendTextToListTag(listTag, textln);
        }

        tag.putBoolean("resolved", false);
    }

    private static void makePlayerOpenEditBookScreen(PlayerEntity player, ItemStack book, Hand hand) {
        if (player instanceof ServerPlayerEntity serverPlayer){
            if (resolve(book, serverPlayer.getCommandSource(), serverPlayer))
                serverPlayer.currentScreenHandler.sendContentUpdates();
            serverPlayer.networkHandler.sendPacket(new OpenWrittenBookS2CPacket(hand));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        makePlayerOpenEditBookScreen(user, itemStack, hand);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }
}
