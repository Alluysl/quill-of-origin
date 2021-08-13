package alluysl.quilloforigin.registry;

import alluysl.quilloforigin.QuillOfOrigin;
import alluysl.quilloforigin.util.ChatMessage;
import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.util.ClassUtil;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;

public class ModRegistries {

    public static final Registry<ConditionFactory<ChatMessage>> CHAT_MESSAGE_CONDITION;

    static {
        CHAT_MESSAGE_CONDITION =
            FabricRegistryBuilder.createSimple(ClassUtil.<ConditionFactory<ChatMessage>>castClass(ConditionFactory.class),
                QuillOfOrigin.identifier("chat_message_condition")).buildAndRegister();
    }

}
