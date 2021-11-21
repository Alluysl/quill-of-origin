package alluysl.quilloforigin.util;

import alluysl.quilloforigin.registry.ModRegistries;
import io.github.apace100.apoli.power.factory.condition.ConditionType;

public class ConditionTypes {

    public static ConditionType<ChatMessage> CHAT_MESSAGE_CONDITION_TYPE =
            new ConditionType<>("ChatMessageCondition", ModRegistries.CHAT_MESSAGE_CONDITION);
}
