package net.povstalec.sgjourney.mixin.cctweaked;

import dan200.computercraft.shared.turtle.core.TurtleBrain;
import dan200.computercraft.shared.turtle.core.TurtleCommandQueueEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.povstalec.sgjourney.common.block_entities.stargate.AbstractStargateEntity;
import net.povstalec.sgjourney.common.blocks.stargate.AbstractStargateBlock;
import net.povstalec.sgjourney.common.blockstates.Orientation;
import net.povstalec.sgjourney.common.compatibility.cctweaked.TurtleWormholeCommand;
import net.povstalec.sgjourney.common.data.StargateNetwork;
import net.povstalec.sgjourney.common.misc.CoordinateHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

@Mixin(TurtleBrain.class)
public abstract class TurtleBrainWormholeCompatibility {

    /**
     * True if the turtle is being transported by stargate
     */
    @Unique
    private boolean inTransit = false;

    @Final
    @Shadow(remap = false)
    private Queue<TurtleCommandQueueEntry> commandQueue;

    @Inject(at = @At(value = "HEAD"), method = "teleportTo", remap = false, cancellable = true)
    protected void teleportToAfter(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir)
    {
        if(level.isClientSide() || inTransit /*|| originalPos == null*/) return;

        TurtleBrain turtle = (TurtleBrain) (Object) this;

        if(!turtle.getLevel().equals(level)) return;

        var moveVector = turtle.getPosition().getCenter().subtract(pos.getCenter());
        if (moveVector.length() > 1) return; // not a normal move
        moveVector = moveVector.normalize();

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
            var state = level.getBlockState(coord);
            if (state == null || !(state.getBlock() instanceof AbstractStargateBlock)) continue;

            var gateEntity = ((AbstractStargateBlock) state.getBlock()).getStargate(level, coord, state);

            if (!gateEntity.isConnected() || gateEntity.getWormhole() == null) continue;
            if (gateEntity.getCenterPos().distToCenterSqr(pos.getCenter()) > 8) continue;

            if (gateEntity.getOrientation() == Orientation.DOWNWARD || gateEntity.getOrientation() == Orientation.UPWARD)
            {
                if (coord.getY() == gateEntity.getCenterPos().getY())
                {
                    if ((gateEntity.getOrientation() == Orientation.DOWNWARD && moveVector.y < 0) ||
                            (gateEntity.getOrientation() == Orientation.UPWARD && moveVector.y > 0))
                    {
                        wormholeTurtle(level, pos, turtle, gateEntity, cir);
                    } // otherwise, entering a gate from a bad direction
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
                    wormholeTurtle(level, pos, turtle, gateEntity, cir);
                }
                return;
            }
        }
    }

    @Unique
    private void wormholeTurtle(Level level, BlockPos posInGate, TurtleBrain turtle, AbstractStargateEntity gateEntity, CallbackInfoReturnable<Boolean> cir)
    {
        if (gateEntity.getConnectionID() == null)
            return;

        var connection = StargateNetwork.get(level).getConnection(gateEntity.getConnectionID());
        if (connection.isEmpty())
            return;

        var destinationGate = connection.get().getDialedStargate();
        if(destinationGate.getLevel() == null) return;

        inTransit = true;
        try {
            if (!gateEntity.getConnectionState().isDialingOut()) {
                turtle.getLevel().removeBlock(turtle.getPosition(), false);
                return;
            }

            var sourceVec = gateEntity.getCenter().vectorTo(posInGate.getCenter());
            var translatedVec = CoordinateHelper.Relative.preserveRelative(gateEntity.getDirection(), gateEntity.getOrientation(), destinationGate.getDirection(), destinationGate.getOrientation(), sourceVec);
            var targetVec = destinationGate.getCenter().add(translatedVec);
            var targetPos = new BlockPos(targetVec);

            var yRot = CoordinateHelper.Relative.preserveYRot(gateEntity.getDirection(), destinationGate.getDirection(), turtle.getDirection().toYRot());

            // as a very next command, enqueue the wormhole travel
            ((ArrayDeque) commandQueue).offerFirst(new TurtleCommandQueueEntry(-1, new TurtleWormholeCommand((ServerLevel) level, targetPos, Direction.fromYRot(yRot))));
        } finally {
            inTransit = false;
        }

    }
}
