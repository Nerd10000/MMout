package net.minecraft.client.renderer.block.dispatch;

import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.world.level.block.state.BlockState;

public interface UnbakedRoot extends ResolvableModel {
  BlockStateModel bake(BlockState paramBlockState, ModelBaker paramModelBaker);
  
  Object visualEqualityGroup(BlockState paramBlockState);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\BlockStateModel$UnbakedRoot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */