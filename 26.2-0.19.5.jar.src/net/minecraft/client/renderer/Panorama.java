/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.state.gui.PanoramaRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class Panorama {
/* 10 */   public static final Identifier PANORAMA_OVERLAY = Identifier.withDefaultNamespace("textures/gui/title/background/panorama_overlay.png");
/*    */   
/*    */   private float spin;
/*    */   
/*    */   private boolean shouldSpin = true;
/*    */   
/*    */   public void startSpin() {
/* 16 */     this.shouldSpin = true;
/*    */   }
/*    */   
/*    */   public void holdSpin() {
/* 20 */     this.shouldSpin = false;
/*    */   }
/*    */   
/*    */   public void extractRenderState(GuiGraphicsExtractor graphics, int width, int height) {
/* 24 */     Minecraft minecraft = Minecraft.getInstance();
/* 25 */     if (this.shouldSpin) {
/* 26 */       float a = minecraft.getDeltaTracker().getRealtimeDeltaTicks();
/* 27 */       float delta = (float)(a * (minecraft.gameRenderer.gameRenderState()).optionsRenderState.panoramaSpeed);
/* 28 */       this.spin = Mth.wrapDegrees(this.spin + delta * 0.1F);
/*    */     } 
/* 30 */     (minecraft.gameRenderer.gameRenderState()).guiRenderState.panoramaRenderState = new PanoramaRenderState(-this.spin);
/* 31 */     graphics.blit(RenderPipelines.GUI_TEXTURED, PANORAMA_OVERLAY, 0, 0, 0.0F, 0.0F, width, height, 16, 128, 16, 128);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\Panorama.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */