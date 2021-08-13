package alluysl.quilloforigin.util;

import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.util.ClassUtil;
import io.github.apace100.origins.util.SerializableDataType;

public class DataTypes {
    //     public static final SerializableDataType<ConditionFactory<Pair<DamageSource, Float>>.Instance> DAMAGE_CONDITION =
    //        SerializableDataType.condition(ClassUtil.castClass(ConditionFactory.Instance.class), ConditionTypes.DAMAGE);

    public static final SerializableDataType<ConditionFactory<ChatMessage>.Instance> CHAT_MESSAGE_CONDITION =
        SerializableDataType.condition(ClassUtil.castClass(ConditionFactory.Instance.class), ConditionTypes.CHAT_MESSAGE_CONDITION_TYPE);
}
