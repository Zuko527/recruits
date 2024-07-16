package com.talhanation.recruits.network;

import com.talhanation.recruits.CommandEvents;
import com.talhanation.recruits.client.gui.group.RecruitsGroup;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import de.maxhenkel.corelib.net.Message;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MessageMove implements Message<MessageMove> {

    private UUID player;
    private int groupId;

    public MessageMove(){
    }

    public MessageMove(UUID player, List<RecruitsGroup> groups) {
        this.player = player;
        this.groupId = groupId;
    }

    public Dist getExecutingSide() {
        return Dist.DEDICATED_SERVER;
    }

    public void executeServerSide(NetworkEvent.Context context) {
        ServerPlayer serverPlayer = context.getSender();
        List<AbstractRecruitEntity> list = Objects.requireNonNull(context.getSender()).getCommandSenderWorld().getEntitiesOfClass(AbstractRecruitEntity.class, context.getSender().getBoundingBox().inflate(100));
        for (AbstractRecruitEntity recruits : list) {
                CommandEvents.onMoveCommand(serverPlayer, this.player, recruits, groupId);
        }
    }
    public MessageMove fromBytes(FriendlyByteBuf buf) {
        this.player = buf.readUUID();
        this.groupId = buf.readInt();
        return this;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.player);
        buf.writeInt(this.groupId);
    }

}