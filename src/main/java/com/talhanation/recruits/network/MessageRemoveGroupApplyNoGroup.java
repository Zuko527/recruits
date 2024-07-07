package com.talhanation.recruits.network;

import com.talhanation.recruits.CommandEvents;
import com.talhanation.recruits.Main;
import com.talhanation.recruits.entities.AbstractLeaderEntity;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import de.maxhenkel.corelib.net.Message;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.enginehub.piston.Command;

import java.util.*;

public class MessageRemoveGroupApplyNoGroup implements Message<MessageRemoveGroupApplyNoGroup> {

    private UUID owner;
    private int groupID;

    public MessageRemoveGroupApplyNoGroup(){
    }

    public MessageRemoveGroupApplyNoGroup(UUID owner, int groupID) {
        this.owner = owner;
        this.groupID = groupID;
    }

    public Dist getExecutingSide() {
        return Dist.DEDICATED_SERVER;
    }

    public void executeServerSide(NetworkEvent.Context context) {
        List<AbstractRecruitEntity> recruitList = new ArrayList<>();
        if(context.getSender().getCommandSenderWorld() instanceof ServerLevel serverLevel){
            for(Entity entity : serverLevel.getEntities().getAll()){
                if(entity instanceof AbstractRecruitEntity recruit && recruit.isEffectedByCommand(owner, groupID))
                    recruitList.add(recruit);
            }
        }

        for(AbstractRecruitEntity recruit : recruitList){
            recruit.setGroup(0);
        }
    }
    public MessageRemoveGroupApplyNoGroup fromBytes(FriendlyByteBuf buf) {
        this.owner = buf.readUUID();
        this.groupID = buf.readInt();
        return this;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.owner);
        buf.writeInt(this.groupID);
    }

}