package com.talhanation.recruits.entities.ai.navigation;

import com.talhanation.recruits.entities.AbstractRecruitEntity;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class RecruitPathNavigation extends GroundPathNavigation {

    public RecruitPathNavigation(AbstractRecruitEntity recruit, Level world) {
        super(recruit, world);
    }

    protected PathFinder createPathFinder(int range) {
        this.nodeEvaluator = new WalkNodeEvaluator();
        this.nodeEvaluator.setCanOpenDoors(true);
        this.nodeEvaluator.setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, range);
    }
}