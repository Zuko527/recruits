package com.talhanation.recruits.entities;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.*;

import javax.annotation.Nullable;
import java.util.Map;

public class BoatNodeEvaluator extends NodeEvaluator {

    public BoatNodeEvaluator() {

    }

    public Node getStart() {
        return super.getNode(Mth.floor(this.mob.getBoundingBox().minX), Mth.floor(this.mob.getBoundingBox().minY + 0.5D), Mth.floor(this.mob.getBoundingBox().minZ));
    }

    public Target getGoal(double p_224768_1_, double p_224768_3_, double p_224768_5_) {
        return new Target(super.getNode(Mth.floor(p_224768_1_ - (double)(this.mob.getBbWidth())), Mth.floor(p_224768_3_ + 0.5D), Mth.floor(p_224768_5_ - (double)(this.mob.getBbWidth()))));
    }

    public int getNeighbors(Node[] p_77483_, Node p_77484_) {
        int i = 0;
        Map<Direction, Node> map = Maps.newEnumMap(Direction.class);

        for(Direction direction : Direction.values()) {
            Node node = this.getNode(p_77484_.x + direction.getStepX(), p_77484_.y + direction.getStepY(), p_77484_.z + direction.getStepZ());
            map.put(direction, node);
            if (this.isNodeValid(node)) {
                p_77483_[i++] = node;
            }
        }

        for(Direction direction1 : Direction.Plane.HORIZONTAL) {
            Direction direction2 = direction1.getClockWise();
            Node node1 = this.getNode(p_77484_.x + direction1.getStepX() + direction2.getStepX(), p_77484_.y, p_77484_.z + direction1.getStepZ() + direction2.getStepZ());
            if (this.isDiagonalNodeValid(node1, map.get(direction1), map.get(direction2))) {
                p_77483_[i++] = node1;
            }
        }

        return i;
    }

    public BlockPathTypes getBlockPathType(BlockGetter blockGetter, int x, int y, int z) {
        return this.getBlockPathType(blockGetter, x, y, z, this.mob, this.entityWidth, this.entityHeight, this.entityDepth, this.canOpenDoors(), this.canPassDoors());
    }

    public BlockPathTypes getBlockPathType(BlockGetter blockGetter, int x, int y, int z, Mob mob, int entityWidth, int entityHeight, int entityDepth, boolean canOpenDoors, boolean canPassDoors) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int i = x; i < x + entityWidth; ++i) {
            for(int j = y; j < y + entityHeight; ++j) {
                for(int k = z; k < z + entityDepth; ++k) {
                    FluidState fluidstate = blockGetter.getFluidState(blockpos$mutableblockpos.set(i, j, k));
                    BlockState blockstate = blockGetter.getBlockState(blockpos$mutableblockpos.set(i, j, k));
                    if (fluidstate.isEmpty() && blockstate.isPathfindable(blockGetter, blockpos$mutableblockpos.below(), PathComputationType.WATER) && blockstate.isAir()) {
                        return BlockPathTypes.BREACH;
                    }

                    if (!fluidstate.is(FluidTags.WATER)) {
                        return BlockPathTypes.BLOCKED;
                    }
                }
            }
        }

        BlockState blockstate1 = blockGetter.getBlockState(blockpos$mutableblockpos);
        return blockstate1.isPathfindable(blockGetter, blockpos$mutableblockpos, PathComputationType.WATER) ? BlockPathTypes.WATER : BlockPathTypes.BLOCKED;
    }


    protected boolean isNodeValid(@Nullable Node p_192962_) {
        return p_192962_ != null && !p_192962_.closed;
    }

    protected boolean isDiagonalNodeValid(@Nullable Node p_192964_, @Nullable Node p_192965_, @Nullable Node p_192966_) {
        return this.isNodeValid(p_192964_) && p_192965_ != null && p_192965_.costMalus >= 0.0F && p_192966_ != null && p_192966_.costMalus >= 0.0F;
    }
}