/*    */ package net.minecraft.client.renderer.state;
/*    */ 
/*    */ import net.minecraft.client.CameraType;
/*    */ import net.minecraft.client.CloudStatus;
/*    */ import net.minecraft.client.PrioritizeChunkUpdates;
/*    */ import net.minecraft.client.TextureFilteringMethod;
/*    */ 
/*    */ public class OptionsRenderState {
/*    */   public int cloudRange;
/*    */   
/*    */   public boolean cutoutLeaves;
/*    */   
/*    */   public boolean improvedTransparency;
/*    */   
/*    */   public boolean ambientOcclusion;
/*    */   
/*    */   public int menuBackgroundBlurriness;
/*    */   
/*    */   public double panoramaSpeed;
/*    */   
/*    */   public int maxAnisotropyValue;
/*    */   
/* 16 */   public TextureFilteringMethod textureFiltering = TextureFilteringMethod.NONE;
/*    */   
/*    */   public boolean bobView;
/*    */   
/*    */   public float screenEffectScale;
/*    */   
/*    */   public double glintSpeed;
/*    */   
/*    */   public double glintStrength;
/*    */   
/*    */   public double damageTiltStrength;
/*    */   
/*    */   public boolean backgroundForChatOnly;
/*    */   
/*    */   public float textBackgroundOpacity;
/*    */   
/* 24 */   public CloudStatus cloudStatus = CloudStatus.OFF;
/*    */   
/* 25 */   public CameraType cameraType = CameraType.FIRST_PERSON;
/*    */   
/*    */   public int renderDistance;
/*    */   
/*    */   public double chunkSectionFadeInTime;
/*    */   
/*    */   public PrioritizeChunkUpdates prioritizeChunkUpdates;
/*    */   
/*    */   public int fov;
/*    */   
/*    */   public float getBackgroundOpacity(float defaultOpacity) {
/* 32 */     return this.backgroundForChatOnly ? defaultOpacity : this.textBackgroundOpacity;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\OptionsRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */