package alluysl.quilloforigin;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuillOfOrigin implements ModInitializer {

	public static final String MODID = "quilloforigin";
	public static final Logger LOGGER = LogManager.getLogger(QuillOfOrigin.class);

	@Override
	public void onInitialize() {

		LOGGER.info("Quill of Origins initialized, ready to write down your stories!");

	}
}
