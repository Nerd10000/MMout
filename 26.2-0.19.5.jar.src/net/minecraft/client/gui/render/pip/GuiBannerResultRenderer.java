/*    */ package net.minecraft.client.gui.render.pip;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Lighting;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.BannerRenderer;
/*    */ import net.minecraft.client.renderer.state.gui.pip.GuiBannerResultRenderState;
/*    */ import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*    */ 
/*    */ public class GuiBannerResultRenderer extends PictureInPictureRenderer<GuiBannerResultRenderState> {
/*    */   private final SpriteGetter sprites;
/*    */   
/*    */   public GuiBannerResultRenderer(SpriteGetter sprites) {
/* 21 */     this.sprites = sprites;
/*    */   }
/*    */   
/*    */   public Class<GuiBannerResultRenderState> getRenderStateClass() {
/* 26 */     return GuiBannerResultRenderState.class;
/*    */   }
/*    */   
/*    */   protected void renderToTexture(GuiBannerResultRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
/* 31 */     (Minecraft.getInstance()).gameRenderer.lighting().setupFor(Lighting.Entry.ITEMS_FLAT);
/* 32 */     poseStack.translate(0.0F, 0.25F, 0.0F);
/* 34 */     submitNodeCollector.submitModel((Model)renderState.flag(), Float.valueOf(0.0F), poseStack, 15728880, OverlayTexture.NO_OVERLAY, -1, Sheets.BANNER_BASE, this.sprites, 0, null);
/* 35 */     BannerRenderer.submitPatterns(this.sprites, poseStack, submitNodeCollector, 15728880, OverlayTexture.NO_OVERLAY, (Model)renderState.flag(), Float.valueOf(0.0F), true, renderState.baseColor(), renderState.resultBannerPatterns(), null);
/*    */   }
/*    */   
/*    */   protected String getTextureLabel() {
/* 40 */     return "banner result";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\render\pip\GuiBannerResultRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */