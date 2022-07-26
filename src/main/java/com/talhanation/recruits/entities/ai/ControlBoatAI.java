package com.talhanation.recruits.entities.ai;

import com.talhanation.recruits.entities.AbstractRecruitEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.vehicle.Boat;

import java.util.List;

public class ControlBoatAI extends Goal {

    private final AbstractRecruitEntity recruit;

    public ControlBoatAI(AbstractRecruitEntity recruit) {
        this.recruit = recruit;
    }


    @Override
    public boolean canUse() {
        return this.recruit.getVehicle() == null; //&& mount ship
    }

    public boolean canContinueToUse() {
        return true;
    }

    public boolean isInterruptable() {
        return true;
    }

    public void start(){
        this.recruit.getNavigation().stop();
    }

    public void stop(){
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        findAndMountBoat();
        updateBoatControl();
    }

    private void findAndMountBoat(){
        List<Boat> list = recruit.level.getEntitiesOfClass(Boat.class, recruit.getBoundingBox().inflate(16D));
        for(Boat boat : list){
            if (boat.canAddPassenger(this.recruit)){
                recruit.getNavigation().moveTo(list.get(0), 1.15F);
                if(recruit.distanceTo(boat) < 2D) recruit.startRiding(boat);
            }
        }
    }

    private void updateBoatControl() {
        if(this.recruit.getVehicle() instanceof Boat boat) {
            BlockPos target = this.recruit.getNavigation().getPath().getTarget();

            double dx = target.getX() - this.recruit.getX();
            double dz = target.getZ() - this.recruit.getZ();

            float angle = Mth.wrapDegrees((float) (Mth.atan2(dz, dx) * 180.0D / 3.14D) - 90.0F);
            float drot = angle - Mth.wrapDegrees(boat.getYRot());

            boolean inputLeft = (drot < 0.0F && Math.abs(drot) >= 5.0F);
            boolean inputRight = (drot > 0.0F && Math.abs(drot) >= 5.0F);
            boolean inputUp = (Math.abs(drot) < 20.0F);

            float deltaRotation = boat.getYRot();
            float f = 0.0F;

            if (inputLeft) {
                --deltaRotation;
            }

            if (inputRight) {
                ++deltaRotation;
            }

            if (inputRight != inputLeft && !inputUp) {
                f += 0.005F;
            }
            boat.setYRot(boat.getYRot() + boat.getYRot());
            if (inputUp) {
                f += 0.04F;
            }

            boat.setDeltaMovement(boat.getDeltaMovement().add((Mth.sin(-boat.getYRot() * 0.0175F) * f), boat.getDeltaMovement().y, (Mth.cos(boat.getYRot() * 0.0175F) * f)));
            boat.setPaddleState(inputRight || inputUp, inputLeft || inputUp);


            boat.move(MoverType.SELF, boat.getDeltaMovement());
        }
    }
}

