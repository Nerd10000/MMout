package net.minecraft.client.resources.model;

import net.minecraft.client.resources.model.geometry.BakedQuad;
import org.joml.Vector3fc;

public interface Interner {
  Vector3fc vector(Vector3fc paramVector3fc);
  
  BakedQuad.MaterialInfo materialInfo(BakedQuad.MaterialInfo paramMaterialInfo);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ModelBaker$Interner.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */