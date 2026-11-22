package net.minecraft.client.renderer.special;

import com.mojang.serialization.MapCodec;

public interface Unbaked<T> {
  SpecialModelRenderer<T> bake(SpecialModelRenderer.BakingContext paramBakingContext);
  
  MapCodec<? extends Unbaked<T>> type();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\SpecialModelRenderer$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */