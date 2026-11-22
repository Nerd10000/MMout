package net.minecraft.client.renderer.block.model.properties.select;

import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.world.level.block.state.BlockState;

public interface SelectBlockModelProperty<T> {
  T get(BlockState paramBlockState, BlockDisplayContext paramBlockDisplayContext);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\model\properties\select\SelectBlockModelProperty.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */