package alluysl.quilloforigin.registry;

import alluysl.quilloforigin.QuillOfOrigin;
import alluysl.quilloforigin.util.ChatMessage;
import alluysl.quilloforigin.util.ChatMessageEvent;
import alluysl.quilloforigin.util.DataTypes;
import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.power.factory.condition.ConditionType;
import io.github.apace100.origins.util.ClassUtil;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.MessageType;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class ChatMessageConditions {

    @SuppressWarnings("unchecked")
    public static void register() {
        register(new ConditionFactory<>(QuillOfOrigin.identifier("constant"), new SerializableData()
            .add("value", SerializableDataType.BOOLEAN),
            (data, message) -> data.getBoolean("value")));
        register(new ConditionFactory<>(QuillOfOrigin.identifier("and"), new SerializableData()
            .add("conditions", DataTypes.CHAT_MESSAGE_CONDITION),
            (data, message) -> ((List<ConditionFactory<ChatMessage>.Instance>)data.get("conditions")).stream().allMatch(
                condition -> condition.test(message)
            )));
        register(new ConditionFactory<>(QuillOfOrigin.identifier("or"), new SerializableData()
            .add("conditions", DataTypes.CHAT_MESSAGE_CONDITION),
            (data, message) -> ((List<ConditionFactory<ChatMessage>.Instance>)data.get("conditions")).stream().anyMatch(
                condition -> condition.test(message)
            )));
        register(new ConditionFactory<>(QuillOfOrigin.identifier("sent_by_entity"), new SerializableData()
            .add("entity_condition", SerializableDataType.ENTITY_CONDITION, null),
            (data, message) -> {
                Entity entity = message.getEntity();
                return entity != null &&
                     (!data.isPresent("entity_condition") ||
                         entity instanceof LivingEntity &&
                             ((ConditionFactory<LivingEntity>.Instance)data.get("entity_condition")).test((LivingEntity)entity));
            }));
        register(new ConditionFactory<>(QuillOfOrigin.identifier("sent_by_server"), new SerializableData(), (data, message) -> message.getEntity() == null));
        register(new ConditionFactory<>(QuillOfOrigin.identifier("message_event"), new SerializableData()
            .add("message_event", SerializableDataType.STRING, null)
            .add("message_events", SerializableDataType.list(SerializableDataType.STRING), null),
            (data, message) ->
                 data.isPresent("message_event")
                        && ChatMessageEvent.valueOf(data.getString("message_event").toUpperCase()) == message.getEvent()
                    || data.isPresent("message_events")
                        && ((List<String>)data.get("message_events")).stream().anyMatch(source ->
                            ChatMessageEvent.valueOf(source.toUpperCase()) == message.getEvent())
            ));
        register(new ConditionFactory<>(QuillOfOrigin.identifier("message_type"), new SerializableData()
            .add("message_type", SerializableDataType.STRING, null)
            .add("message_types", SerializableDataType.list(SerializableDataType.STRING), null),
            (data, message) ->
                 data.isPresent("message_type")
                        && MessageType.valueOf(data.getString("message_type").toUpperCase()) == message.getType()
                    || data.isPresent("message_types")
                        && ((List<String>)data.get("message_types")).stream().anyMatch(source ->
                            MessageType.valueOf(source.toUpperCase()) == message.getType())
            ));
    }

    private static void register(ConditionFactory<ChatMessage> conditionFactory) {
        Registry.register(ModRegistries.CHAT_MESSAGE_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
