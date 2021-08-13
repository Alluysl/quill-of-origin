package alluysl.quilloforigin.power.factory;

import alluysl.quilloforigin.QuillOfOrigin;
import alluysl.quilloforigin.power.ScribePower;
import alluysl.quilloforigin.util.ChatMessage;
import alluysl.quilloforigin.util.DataTypes;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import net.minecraft.util.registry.Registry;

public class PowerFactories {

    @SuppressWarnings("unchecked")
    public static void register(){

        register(new PowerFactory<>(QuillOfOrigin.identifier("scribe"),
            new SerializableData()
                .add("mainhand", SerializableDataType.BOOLEAN, true)
                .add("offhand", SerializableDataType.BOOLEAN, true)
                .add("hotbar", SerializableDataType.BOOLEAN, false)
                .add("inventory", SerializableDataType.BOOLEAN, false)
                .add("message_condition", DataTypes.CHAT_MESSAGE_CONDITION, null),
            data ->
                (type, player) -> new ScribePower(type, player,
                    data.getBoolean("mainhand"), data.getBoolean("offhand"),
                    data.getBoolean("hotbar"), data.getBoolean("inventory"),
                    (ConditionFactory<ChatMessage>.Instance)data.get("message_condition"))
        ).allowCondition());
    }

    private static void register(PowerFactory<?> factory){
        Registry.register(ModRegistries.POWER_FACTORY, factory.getSerializerId(), factory);
    }
}
