package net.minecraft.client.renderer.feature;

import java.util.List;

public interface FeatureRenderer<Submit extends net.minecraft.client.renderer.feature.submit.SubmitNode> extends AutoCloseable {
  default void beginPrepare(FeatureFrameContext context) {}
  
  void prepareGroup(FeatureFrameContext paramFeatureFrameContext, List<Submit> paramList, boolean paramBoolean);
  
  default void finishPrepare(FeatureFrameContext context) {}
  
  void executeGroup(FeatureFrameContext paramFeatureFrameContext, int paramInt, List<Submit> paramList, boolean paramBoolean);
  
  default void finishExecute(FeatureFrameContext context) {}
  
  default void close() {}
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\FeatureRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */