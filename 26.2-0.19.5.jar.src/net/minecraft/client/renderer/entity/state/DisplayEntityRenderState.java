package net.minecraft.client.renderer.entity.state;

import net.minecraft.world.entity.Display;

public abstract class DisplayEntityRenderState extends EntityRenderState {
  public Display.RenderState renderState;
  
  public float interpolationProgress;
  
  public float entityYRot;
  
  public float entityXRot;
  
  public float cameraYRot;
  
  public float cameraXRot;
  
  public abstract boolean hasSubState();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\DisplayEntityRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */