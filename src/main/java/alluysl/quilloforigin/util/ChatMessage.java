package alluysl.quilloforigin.util;

import net.minecraft.entity.Entity;
import net.minecraft.network.MessageType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.util.UUID;

public class ChatMessage {

    private final ServerWorld world;
    private final ChatMessageEvent event;
    private final Text message;
    private final MessageType type;
    private final UUID senderUuid;

    public ChatMessage(ServerWorld world, ChatMessageEvent event, Text text, MessageType type, UUID senderUuid){
        this.world = world;
        this.event = event;
        this.message = text;
        this.type = type;
        this.senderUuid = senderUuid;
    }

    public Entity getEntity(){
        if (senderUuid == Util.NIL_UUID)
            return null;
        return world.getEntity(senderUuid);
    }

    public ChatMessageEvent getEvent(){ return event; }

    public Text getMessage(){ return message; }

    public MessageType getType(){ return type; }
}
