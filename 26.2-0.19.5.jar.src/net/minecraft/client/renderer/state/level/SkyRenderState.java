/*    */ package net.minecraft.client.renderer.state.level;
/*    */ 
/*    */ import net.minecraft.world.level.MoonPhase;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ 
/*    */ public class SkyRenderState {
/*  7 */   public DimensionType.Skybox skybox = DimensionType.Skybox.NONE;
/*    */   
/*    */   public boolean shouldRenderDarkDisc;
/*    */   
/*    */   public float sunAngle;
/*    */   
/*    */   public float moonAngle;
/*    */   
/*    */   public float starAngle;
/*    */   
/*    */   public float rainBrightness;
/*    */   
/*    */   public float starBrightness;
/*    */   
/*    */   public int sunriseAndSunsetColor;
/*    */   
/* 15 */   public MoonPhase moonPhase = MoonPhase.FULL_MOON;
/*    */   
/*    */   public int skyColor;
/*    */   
/*    */   public float endFlashIntensity;
/*    */   
/*    */   public float endFlashXAngle;
/*    */   
/*    */   public float endFlashYAngle;
/*    */   
/*    */   public void reset() {
/* 22 */     this.skybox = DimensionType.Skybox.NONE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\level\SkyRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */