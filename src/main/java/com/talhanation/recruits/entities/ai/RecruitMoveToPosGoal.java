package com.talhanation.recruits.entities.ai;

import com.talhanation.recruits.entities.AbstractRecruitEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RecruitMoveToPosGoal extends Goal {
    private final AbstractRecruitEntity recruit;
    private final double speedModifier;

    public RecruitMoveToPosGoal(AbstractRecruitEntity recruit, double v) {
        this.recruit = recruit;
        this.speedModifier = v;
    }
    public boolean canUse() {
        return recruit.getShouldMovePos() && !recruit.needsToGetFood() && !recruit.getShouldMount();
    }

    public boolean canContinueToUse() {
        return this.canUse();
    }

    //maybe?? start(){
    public void tick() {
        BlockPos blockpos = this.recruit.getMovePos();
        if (blockpos != null) {
            double distance = recruit.distanceToSqr(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            if(distance >= 6) {
                this.recruit.getNavigation().moveTo(blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.speedModifier);
                if (recruit.horizontalCollision || recruit.minorHorizontalCollision) {
                    this.recruit.getJumpControl().jump();
                }
            }
            else {
                recruit.setShouldMovePos(false);
                recruit.clearMovePos();
            }
        }
    }
}

