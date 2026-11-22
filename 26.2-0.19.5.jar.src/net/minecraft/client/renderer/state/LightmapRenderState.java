/*    */ package net.minecraft.client.renderer.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.LightmapRenderStateExtractor;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class LightmapRenderState {
/*    */   public boolean needsUpdate = false;
/*    */   
/*    */   public float blockFactor;
/*    */   
/*  9 */   public Vector3fc blockLightTint = LightmapRenderStateExtractor.WHITE;
/*    */   
/*    */   public float skyFactor;
/*    */   
/* 11 */   public Vector3fc skyLightColor = LightmapRenderStateExtractor.WHITE;
/*    */   
/* 12 */   public Vector3fc ambientColor = LightmapRenderStateExtractor.WHITE;
/*    */   
/*    */   public float brightness;
/*    */   
/*    */   public float darknessEffectScale;
/*    */   
/*    */   public float nightVisionEffectIntensity;
/*    */   
/* 16 */   public Vector3fc nightVisionColor = LightmapRenderStateExtractor.WHITE;
/*    */   
/*    */   public float bossOverlayWorldDarkening;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\LightmapRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */