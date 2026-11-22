/*    */ package net.minecraft.client.renderer.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.state.gui.GuiRenderState;
/*    */ import net.minecraft.client.renderer.state.level.LevelRenderState;
/*    */ 
/*    */ public class GameRenderState {
/*  7 */   public final LevelRenderState levelRenderState = new LevelRenderState();
/*    */   
/*  9 */   public final LightmapRenderState lightmapRenderState = new LightmapRenderState();
/*    */   
/* 10 */   public final GuiRenderState guiRenderState = new GuiRenderState();
/*    */   
/* 11 */   public final OptionsRenderState optionsRenderState = new OptionsRenderState();
/*    */   
/* 12 */   public final WindowRenderState windowRenderState = new WindowRenderState();
/*    */   
/*    */   public int framerateLimit;
/*    */   
/*    */   public boolean useShaderTransparency() {
/* 17 */     return (!this.levelRenderState.cameraRenderState.isPanoramicMode && this.optionsRenderState.improvedTransparency);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\GameRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */