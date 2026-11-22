package net.minecraft.client.renderer.special;

import com.mojang.serialization.MapCodec;

public interface Unbaked extends SpecialModelRenderer.Unbaked<Void> {
  MapCodec<? extends Unbaked> type();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\NoDataSpecialModelRenderer$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */