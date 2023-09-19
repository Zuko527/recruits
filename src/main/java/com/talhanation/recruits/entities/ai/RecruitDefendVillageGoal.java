package com.talhanation.recruits.entities.ai;

import com.talhanation.recruits.entities.AbstractRecruitEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class RecruitDefendVillageGoal extends TargetGoal {
    private final AbstractRecruitEntity recruit;
    @Nullable
    private LivingEntity potentialTarget;
    private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0D);

    public RecruitDefendVillageGoal(AbstractRecruitEntity p_26029_) {
        super(p_26029_, false, true);
        this.recruit = p_26029_;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        AABB aabb = this.recruit.getBoundingBox().inflate(10.0D, 8.0D, 10.0D);
        List<? extends LivingEntity> list = this.recruit.getCommandSenderWorld().getNearbyEntities(Villager.class, this.attackTargeting, this.recruit, aabb);
        List<Player> list1 = this.recruit.getCommandSenderWorld().getNearbyPlayers(this.attackTargeting, this.recruit, aabb);

        for(LivingEntity livingentity : list) {
            Villager villager = (Villager)livingentity;

            for(Player player : list1) {
                int i = villager.getPlayerReputation(player);
                if (i <= -100) {
                    this.potentialTarget = player;
                }
            }
        }

        if (this.potentialTarget == null) {
            return false;
        } else {
            return !(this.potentialTarget instanceof Player) || !this.potentialTarget.isSpectator() && !((Player)this.potentialTarget).isCreative();
        }
    }

    public void start() {
        this.recruit.setTarget(this.potentialTarget);
        super.start();
    }
}