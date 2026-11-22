package net.minecraft.client.renderer.feature.submit;

import net.minecraft.client.renderer.feature.FeatureRendererType;

public interface BatchableSubmit extends SubmitNode {
  Object batchKey();
  
  FeatureRendererType<? extends BatchableSubmit> featureType();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\submit\BatchableSubmit.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */