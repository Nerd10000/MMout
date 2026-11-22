package net.minecraft.client.renderer.block;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
interface ModelFactory {
  BlockModel.Unbaked create(BlockColors paramBlockColors, BlockState paramBlockState);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\BuiltInBlockModels$ModelFactory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */