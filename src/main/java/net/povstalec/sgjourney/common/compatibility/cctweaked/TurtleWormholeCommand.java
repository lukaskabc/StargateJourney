// based on TurtleMoveCommand by Daniel Ratcliffe

package net.povstalec.sgjourney.common.compatibility.cctweaked;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.shared.turtle.core.TurtlePlayer;
import dan200.computercraft.shared.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

/**
 * Teleports turtle to specified location and sets a direction
 */
public class TurtleWormholeCommand implements TurtleCommand {

    private final ServerLevel level;
    private final BlockPos blockPos;
    private final Direction direction;

    public TurtleWormholeCommand(ServerLevel level, BlockPos blockPos, Direction direction) {
        this.level = level;
        this.blockPos = blockPos;
        this.direction = direction;
    }

    @Override
    public TurtleCommandResult execute(ITurtleAccess turtle) {
        // Check if we can move
        var oldWorld = (ServerLevel) turtle.getLevel();
        var oldPosition = turtle.getPosition();

        var turtlePlayer = TurtlePlayer.getWithPosition(turtle, oldPosition, Direction.EAST);
        if (level.isOutsideBuildHeight(blockPos)) {
            return TurtleCommandResult.failure(blockPos.getY() < 0 ? "Too low to move" : "Too high to move");
        }
        if (!level.isInWorldBounds(blockPos)) return TurtleCommandResult.failure("Cannot leave the world");

        // Check spawn protection
        if (turtlePlayer.isBlockProtected(level, blockPos)) {
            return TurtleCommandResult.failure("Cannot enter protected area");
        }

        // Check existing block is air or replaceable
        var state = oldWorld.getBlockState(blockPos);
        if (!oldWorld.isEmptyBlock(blockPos) &&
                !WorldUtil.isLiquidBlock(oldWorld, blockPos) &&
                !state.getMaterial().isReplaceable()) {
            return TurtleCommandResult.failure("Movement obstructed");
        }

        // Move
        if (!turtle.teleportTo(level, blockPos)) return TurtleCommandResult.failure("Movement failed");
        turtle.setDirection(direction);

        return TurtleCommandResult.success();
    }
}
