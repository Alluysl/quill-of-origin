package alluysl.quilloforigin;

import alluysl.quilloforigin.power.factory.PowerFactories;
import alluysl.quilloforigin.registry.ChatMessageConditions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuillOfOrigin implements ModInitializer {

	public static final String MODID = "quilloforigin";
	public static final Logger LOGGER = LogManager.getLogger(QuillOfOrigin.class);

	public static final Item CHAT_BOOK = new ChatBookItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(16));
	public static GameRules.Key<GameRules.BooleanRule> COPIES_STAY_CHAT_BOOKS = null;

	public static boolean copiesStayChatBooks;

	public static void updateModGameRules(GameRules gameRules){
		if (COPIES_STAY_CHAT_BOOKS != null)
			copiesStayChatBooks = gameRules.getBoolean(COPIES_STAY_CHAT_BOOKS);
	}

	@Override
	public void onInitialize() {

		LOGGER.info("Quill of Origin initialized, ready to write down your stories!");

		Registry.register(Registry.ITEM, new Identifier(MODID, "chat_book"), CHAT_BOOK);
		ChatMessageConditions.register();
		PowerFactories.register();

		if (!GameRuleRegistry.hasRegistration("copiesStayChatBooks"))
			COPIES_STAY_CHAT_BOOKS = GameRuleRegistry.register("copiesStayChatBooks", GameRules.Category.PLAYER, GameRules.BooleanRule.create(copiesStayChatBooks));

		ServerTickEvents.START_SERVER_TICK.register(server -> updateModGameRules(server.getGameRules()));
	}

	public static Identifier identifier(String path) {
		return new Identifier(MODID, path);
	}

}
