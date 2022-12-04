package com.talhanation.recruits.entities.ai;

import com.talhanation.recruits.entities.AbstractRecruitEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class BoatPathAI extends Goal {
    private final AbstractRecruitEntity recruitEntity;
    private boolean stuck;

    public BoatPathAI(AbstractRecruitEntity rec) {
        this.recruitEntity = rec;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean isInterruptable() {
        return false;
    }

    public boolean canUse() {
        return  recruitEntity.isPassenger() & recruitEntity.getVehicle() instanceof Boat && recruitEntity.getHoldPos() != null;
    }

    public boolean canContinueToUse() {
        BlockPos blockpos = this.recruitEntity.getHoldPos(); //später mit target pos uas nav
        return !(new BlockPos((double)blockpos.getX(), this.recruitEntity.getY(), (double)blockpos.getZ())).closerToCenterThan(this.recruitEntity.position(), 5.0D) && !this.stuck;
    }

    public void start() {
        if (this.recruitEntity.level instanceof ServerLevel) {
            this.stuck = false;
            this.recruitEntity.getNavigation().stop();
        }
    }

    public void tick() {
        Level level = this.recruitEntity.level;
        if (this.closeToNextPos() || this.recruitEntity.getNavigation().isDone()) {
            Vec3 vec3 = Vec3.atCenterOf(this.recruitEntity.getHoldPos()); //später target pos aus navigation
            Vec3 vec31 = DefaultRandomPos.getPosTowards(this.recruitEntity, 16, 1, vec3, (double)((float)Math.PI / 8F));
            if (vec31 == null) {
                vec31 = DefaultRandomPos.getPosTowards(this.recruitEntity, 8, 4, vec3, (double)((float)Math.PI / 2F));
            }

            if (vec31 != null) {
                BlockPos blockpos = new BlockPos(vec31);
                if (!level.getFluidState(blockpos).is(FluidTags.WATER) || !level.getBlockState(blockpos).isPathfindable(level, blockpos, PathComputationType.WATER)) {
                    vec31 = DefaultRandomPos.getPosTowards(this.recruitEntity, 8, 5, vec3, (double)((float)Math.PI / 2F));
                }
            }

            if (vec31 == null) {
                this.stuck = true;
                return;
            }

            this.recruitEntity.getLookControl().setLookAt(vec31.x, vec31.y, vec31.z, (float)(this.recruitEntity.getMaxHeadYRot() + 20), (float)this.recruitEntity.getMaxHeadXRot());
            this.recruitEntity.getNavigation().moveTo(vec31.x, vec31.y, vec31.z, 1.3D);
            if (level.random.nextInt(this.adjustedTickDelay(80)) == 0) {
                level.broadcastEntityEvent(this.recruitEntity, (byte)38);
            }
        }
    }

    private boolean closeToNextPos() {
        BlockPos blockpos = recruitEntity.getNavigation().getTargetPos();
        return blockpos != null ? blockpos.closerToCenterThan(recruitEntity.position(), 12.0D) : false;
    }
}