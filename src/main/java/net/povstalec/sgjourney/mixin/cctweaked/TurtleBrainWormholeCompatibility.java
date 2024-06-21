package net.povstalec.sgjourney.mixin.cctweaked;

import dan200.computercraft.shared.turtle.core.TurtleBrain;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
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
        // lets find out possible coords where stargate base block could be


    }
}
