package net.minecraft.client.renderer.state.level;

import net.minecraft.client.renderer.SubmitNodeCollector;

public interface ParticleGroupRenderState {
  void submit(SubmitNodeCollector paramSubmitNodeCollector, CameraRenderState paramCameraRenderState);
  
  default void clear() {}
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\level\ParticleGroupRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */