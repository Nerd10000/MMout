package net.minecraft.client.data.models.blockstates;

import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.minecraft.world.level.block.Block;

public interface BlockModelDefinitionGenerator {
  Block block();
  
  BlockStateModelDispatcher create();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\blockstates\BlockModelDefinitionGenerator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */