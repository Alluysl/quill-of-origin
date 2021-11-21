package alluysl.quilloforigin.power.factory;

import alluysl.quilloforigin.QuillOfOrigin;
import alluysl.quilloforigin.power.ScribePower;
import alluysl.quilloforigin.util.ChatMessage;
import alluysl.quilloforigin.util.DataTypes;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

public class PowerFactories {

    @SuppressWarnings("unchecked")
    public static void register(){

        register(new PowerFactory<>(QuillOfOrigin.identifier("scribe"),
            new SerializableData()
                .add("mainhand", SerializableDataTypes.BOOLEAN, true)
                .add("offhand", SerializableDataTypes.BOOLEAN, true)
                .add("hotbar", SerializableDataTypes.BOOLEAN, false)
                .add("inventory", SerializableDataTypes.BOOLEAN, false)
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_action", ApoliDataTypes.ITEM_ACTION, null)
                .add("message_condition", DataTypes.CHAT_MESSAGE_CONDITION, null),
            data ->
                (type, entity) -> new ScribePower(type, entity,
                    data.getBoolean("mainhand"), data.getBoolean("offhand"),
                    data.getBoolean("hotbar"), data.getBoolean("inventory"),
                    (ActionFactory<Entity>.Instance)data.get("entity_action"),
                    (ActionFactory<ItemStack>.Instance)data.get("item_action"),
                    (ConditionFactory<ChatMessage>.Instance)data.get("message_condition"))
        ).allowCondition());
    }

    private static void register(PowerFactory<?> factory){
        Registry.register(ApoliRegistries.POWER_FACTORY, factory.getSerializerId(), factory);
    }
}
