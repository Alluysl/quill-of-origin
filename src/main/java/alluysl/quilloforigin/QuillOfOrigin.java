package alluysl.quilloforigin;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuillOfOrigin implements ModInitializer {

	public static final String MODID = "quilloforigin";
	public static final Logger LOGGER = LogManager.getLogger(QuillOfOrigin.class);

	public static final Item CHAT_BOOK = new ChatBookItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));

	@Override
	public void onInitialize() {

		LOGGER.info("Quill of Origin initialized, ready to write down your stories!");

		Registry.register(Registry.ITEM, new Identifier(MODID, "chat_book"), CHAT_BOOK);
	}
}
