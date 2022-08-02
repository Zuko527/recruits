package com.talhanation.recruits.entities.ai;

import com.talhanation.recruits.entities.AbstractRecruitEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import java.util.List;

public class ControlBoatAI extends Goal {

    private final AbstractRecruitEntity recruit;
    private int stuck;

    public ControlBoatAI(AbstractRecruitEntity recruit) {
        this.recruit = recruit;
    }

    @Override
    public boolean canUse() {
        return true; //&& mount ship
    }

    public boolean canContinueToUse() {
        return true;
    }

    public boolean isInterruptable() {
        return true;
    }

    public void start(){
        this.recruit.setPathfindingMalus(BlockPathTypes.WATER, 8.0F);
        this.recruit.setPathfindingMalus(BlockPathTypes.BREACH, -1.0F);
        this.recruit.setPathfindingMalus(BlockPathTypes.WALKABLE, -1.0F);
    }

    public void stop(){
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        if(!(this.recruit.getVehicle() instanceof Boat)) findAndMountBoat();
        double posX = 0, posZ = 0;


        if (recruit.getShouldFollow() && recruit.getOwner() != null && recruit.getOwner().distanceTo(recruit) >= 5D){
             posX = recruit.getOwner().getX();
             posZ = recruit.getOwner().getZ();
        }
        else if (recruit.getShouldHoldPos() && recruit.getHoldPos() != null && recruit.getHoldPos().distToCenterSqr(recruit.position()) >= 5D){
            posX = recruit.getHoldPos().getX();
            posZ = recruit.getHoldPos().getZ();

        }
        if (recruit.getTarget() != null && recruit.getTarget().distanceTo(recruit) >= 5D){
            posX = recruit.getTarget().getX();
            posZ = recruit.getTarget().getZ();
        }
        updateBoatControl(posX, posZ);

        if (this.recruit.getVehicle() instanceof Boat && recruit.getNavigation().isStuck()){
            stuck++;
        }
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

    private void updateBoatControl(double posX, double posZ) {
        if(this.recruit.getVehicle() instanceof Boat boat && boat.getPassengers().get(0).equals(this.recruit)) {
            double dx = posX - this.recruit.getX();
            double dz = posZ - this.recruit.getZ();

            float angle = Mth.wrapDegrees((float) (Mth.atan2(dz, dx) * 180.0D / 3.14D) - 90.0F);
            float drot = angle - Mth.wrapDegrees(boat.getYRot());

            boolean inputLeft = (drot < 0.0F && Math.abs(drot) >= 5.0F);
            boolean inputRight = (drot > 0.0F && Math.abs(drot) >= 5.0F);
            boolean inputUp = (Math.abs(drot) < 20.0F);

            float f = 0.0F;
            if(stuck >= 100) {
                boat.setYRot(boat.getYRot() - 25F);
                f -= 0.08F;
                stuck = 50;
            }
            else {
                if (inputLeft) {
                    boat.setYRot(boat.getYRot() - 3F);
                }

                if (inputRight) {
                    boat.setYRot(boat.getYRot() + 3F);
                }


                if (inputRight != inputLeft && !inputUp) {
                    f += 0.005F;
                }

                if (inputUp) {
                    f += 0.04F;
                }
            }
            boat.setDeltaMovement(boat.getDeltaMovement().add((double)(Mth.sin(-boat.getYRot() * ((float)Math.PI / 180F)) * f), 0.0D, (double)(Mth.cos(boat.getYRot() * ((float)Math.PI / 180F)) * f)));
            boat.setPaddleState(inputRight || inputUp, inputLeft || inputUp);
        }
    }
}

