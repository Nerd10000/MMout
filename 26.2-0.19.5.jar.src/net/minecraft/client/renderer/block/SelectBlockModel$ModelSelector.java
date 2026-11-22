package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.block.model.BlockModel;

@FunctionalInterface
public interface ModelSelector<T> {
  BlockModel get(T paramT);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\SelectBlockModel$ModelSelector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */