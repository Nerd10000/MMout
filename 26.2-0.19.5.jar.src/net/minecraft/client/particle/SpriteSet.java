package net.minecraft.client.particle;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.RandomSource;

public interface SpriteSet {
  TextureAtlasSprite get(int paramInt1, int paramInt2);
  
  TextureAtlasSprite get(RandomSource paramRandomSource);
  
  TextureAtlasSprite first();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SpriteSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */