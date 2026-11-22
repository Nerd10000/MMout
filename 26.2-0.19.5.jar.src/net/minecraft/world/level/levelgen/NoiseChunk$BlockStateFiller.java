package net.minecraft.world.level.levelgen;

import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface BlockStateFiller {
  BlockState calculate(DensityFunction.FunctionContext paramFunctionContext);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseChunk$BlockStateFiller.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */