package net.minecraft.client.renderer.special;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.resources.model.sprite.SpriteGetter;

public interface BakingContext {
  EntityModelSet entityModelSet();
  
  SpriteGetter sprites();
  
  PlayerSkinRenderCache playerSkinRenderCache();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\SpecialModelRenderer$BakingContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */