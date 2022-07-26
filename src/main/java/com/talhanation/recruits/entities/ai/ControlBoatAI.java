package com.talhanation.recruits.entities.ai;

import com.talhanation.recruits.entities.AbstractRecruitEntity;
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
            }
        }
    }

    private void updateBoatControl(){
        if(this.recruit.getVehicle() instanceof Boat boat) {
            //MoveControl movement = this.recruit.getMoveControl();
            //PathNavigation nav = this.recruit.getNavigation();
/*
            double x0 = movement.getWantedX();
            double y0 = movement.getWantedY();
            double z0 = movement.getWantedZ();
            //Main.LOGGER.debug("MoveControl:" + " x: " + x0 + " y: " + y0 + " z: " + z0 + " ");
            //Path path = nav.createPath(x0, y0, z0, 1);
            double x = boat.getDeltaMovement().x;
            double y = boat.getDeltaMovement().y;
            double z = boat.getDeltaMovement().z;
*/

            float deltaRotation = recruit.getYRot();

            boat.setYRot(boat.getYRot() + deltaRotation * 0.1F);
            boat.setDeltaMovement(recruit.getForward().x, boat.getDeltaMovement().y, recruit.getForward().z);
            boat.setPaddleState(true, true);
        }
    }
}
