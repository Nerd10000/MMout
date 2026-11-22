package net.minecraft.client.renderer.block.dispatch;

import java.util.List;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;

public interface BlockStateModelPart {
  List<BakedQuad> getQuads(Direction paramDirection);
  
  boolean useAmbientOcclusion();
  
  Material.Baked particleMaterial();
  
  int materialFlags();
  
  public static interface Unbaked extends ResolvableModel {
    BlockStateModelPart bake(ModelBaker param1ModelBaker);
  }
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\BlockStateModelPart.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */