package net.minecraft.client.resources.model;

import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.MaterialBaker;
import net.minecraft.resources.Identifier;
import org.joml.Vector3fc;

public interface ModelBaker {
  ResolvedModel getModel(Identifier paramIdentifier);
  
  BlockStateModelPart missingBlockModelPart();
  
  MaterialBaker materials();
  
  Interner interner();
  
  <T> T compute(SharedOperationKey<T> paramSharedOperationKey);
  
  public static interface Interner {
    Vector3fc vector(Vector3fc param1Vector3fc);
    
    BakedQuad.MaterialInfo materialInfo(BakedQuad.MaterialInfo param1MaterialInfo);
  }
  
  @FunctionalInterface
  public static interface SharedOperationKey<T> {
    T compute(ModelBaker param1ModelBaker);
  }
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ModelBaker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */