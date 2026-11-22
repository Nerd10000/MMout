/*    */ package net.minecraft.client.renderer.state.level;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.WeatherEffectRenderer;
/*    */ 
/*    */ public class WeatherRenderState {
/*  9 */   public final List<WeatherEffectRenderer.ColumnInstance> rainColumns = new ArrayList<>();
/*    */   
/* 10 */   public final List<WeatherEffectRenderer.ColumnInstance> snowColumns = new ArrayList<>();
/*    */   
/*    */   public float intensity;
/*    */   
/*    */   public int radius;
/*    */   
/*    */   public void reset() {
/* 15 */     this.rainColumns.clear();
/* 16 */     this.snowColumns.clear();
/* 17 */     this.intensity = 0.0F;
/* 18 */     this.radius = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\level\WeatherRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */