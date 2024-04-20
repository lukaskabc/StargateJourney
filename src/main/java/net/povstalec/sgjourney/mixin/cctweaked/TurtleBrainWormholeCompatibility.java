package net.povstalec.sgjourney.mixin.cctweaked;

import dan200.computercraft.shared.turtle.core.TurtleBrain;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.povstalec.sgjourney.common.block_entities.stargate.AbstractStargateEntity;
import net.povstalec.sgjourney.common.blocks.stargate.AbstractStargateBlock;
import net.povstalec.sgjourney.common.blockstates.Orientation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(TurtleBrain.class)
public abstract class TurtleBrainWormholeCompatibility {

    @Inject(at = @At(value = "HEAD"), method = "teleportTo", remap = false)
    protected void teleportTo(Level world, BlockPos pos, CallbackInfoReturnable<Boolean> cir)
    {
        if(world.isClientSide()) return;

        TurtleBrain turtle = (TurtleBrain) (Object) this;

        if(!turtle.getLevel().equals(world)) return;

        var moveVector = turtle.getPosition().getCenter().subtract(pos.getCenter()).normalize();
        if (moveVector.length() > 1) return; // not a normal move

        // let's find out possible coords where stargate block could be
        // assuming that turtle can move only up, down, forward and back
        var candidates = List.of(
                // up & down for a vertical gate
                pos.below(1), pos.below(2), pos.below(3),
                pos.above(1), pos.above(2),
                // left and right for a horizontal gate
                pos.relative(turtle.getDirection().getClockWise(), 1), pos.relative(turtle.getDirection().getClockWise(), 2), pos.relative(turtle.getDirection().getClockWise(), 3),
                pos.relative(turtle.getDirection().getCounterClockWise(), 1), pos.relative(turtle.getDirection().getCounterClockWise(), 2)
        );

        for (var coord : candidates)
        {
            var state = world.getBlockState(coord);
            if (state == null || !(state.getBlock() instanceof AbstractStargateBlock)) continue;

            var gateEntity = ((AbstractStargateBlock) state.getBlock()).getStargate(world, coord, state);

            if (!gateEntity.isConnected() || gateEntity.getWormhole() == null) continue;
            if (gateEntity.getCenterPos().distToCenterSqr(coord.getCenter()) > 8) continue;

            if (gateEntity.getOrientation() == Orientation.DOWNWARD || gateEntity.getOrientation() == Orientation.UPWARD)
            {
                if (coord.getY() == gateEntity.getCenterPos().getY())
                {
                    if ((gateEntity.getOrientation() == Orientation.DOWNWARD && moveVector.y < 0) ||
                            (gateEntity.getOrientation() == Orientation.UPWARD && moveVector.y > 0))
                    {
                        wormholeTurtle(world, pos, turtle, gateEntity, cir);
                    } // otherwise entering a gate from bad direction
                    return;
                }
                continue;
            }

            // okey so stargate is standing (vertical)
            if(gateEntity.getDirection() == turtle.getDirection() || gateEntity.getDirection().getOpposite() == turtle.getDirection())
            {
                if ((gateEntity.getDirection() == Direction.NORTH && moveVector.z < 0) ||
                        (gateEntity.getDirection() == Direction.SOUTH && moveVector.z > 0) ||
                        (gateEntity.getDirection() == Direction.WEST && moveVector.x < 0) ||
                        (gateEntity.getDirection() == Direction.EAST && moveVector.x > 0))
                {
                    wormholeTurtle(world, pos, turtle, gateEntity, cir);
                }
                return;
            }
        }
    }

    private void wormholeTurtle(Level world, BlockPos posInGate, TurtleBrain turtle, AbstractStargateEntity gateEntity, CallbackInfoReturnable<Boolean> cir)
    {
        if (!gateEntity.getConnectionState().isDialingOut()) {
            turtle.getLevel().removeBlock(turtle.getPosition(), false);
            return;
        }
//        var targetGate = gateEntity.getConnectionAddress()
        
        var sourceVec = gateEntity.getCenter().subtract(posInGate.getCenter());
//        var targetVec =

    }
}
