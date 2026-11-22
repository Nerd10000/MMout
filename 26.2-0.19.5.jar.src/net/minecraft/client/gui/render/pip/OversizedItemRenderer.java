/*    */ package net.minecraft.client.gui.render.pip;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Lighting;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
/*    */ import net.minecraft.client.renderer.state.gui.GuiItemRenderState;
/*    */ import net.minecraft.client.renderer.state.gui.GuiRenderState;
/*    */ import net.minecraft.client.renderer.state.gui.pip.OversizedItemRenderState;
/*    */ import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ 
/*    */ public class OversizedItemRenderer extends PictureInPictureRenderer<OversizedItemRenderState> {
/*    */   private boolean usedOnThisFrame;
/*    */   
/*    */   private Object modelOnTextureIdentity;
/*    */   
/*    */   public boolean usedOnThisFrame() {
/* 25 */     return this.usedOnThisFrame;
/*    */   }
/*    */   
/*    */   public void resetUsedOnThisFrame() {
/* 29 */     this.usedOnThisFrame = false;
/*    */   }
/*    */   
/*    */   public void invalidateTexture() {
/* 33 */     this.modelOnTextureIdentity = null;
/*    */   }
/*    */   
/*    */   public Class<OversizedItemRenderState> getRenderStateClass() {
/* 38 */     return OversizedItemRenderState.class;
/*    */   }
/*    */   
/*    */   protected void renderToTexture(OversizedItemRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
/* 43 */     poseStack.scale(1.0F, -1.0F, -1.0F);
/* 44 */     GuiItemRenderState guiItemRenderState = renderState.guiItemRenderState();
/* 45 */     ScreenRectangle itemBounds = guiItemRenderState.oversizedItemBounds();
/* 46 */     Objects.requireNonNull(itemBounds);
/* 47 */     float itemBoundsCenterX = (itemBounds.left() + itemBounds.right()) / 2.0F;
/* 48 */     float itemBoundsCenterY = (itemBounds.top() + itemBounds.bottom()) / 2.0F;
/* 49 */     float slotCenterX = guiItemRenderState.x() + 8.0F;
/* 50 */     float slotCenterY = guiItemRenderState.y() + 8.0F;
/* 51 */     poseStack.translate((slotCenterX - itemBoundsCenterX) / 16.0F, (itemBoundsCenterY - slotCenterY) / 16.0F, 0.0F);
/* 52 */     TrackingItemStackRenderState itemStackRenderState = guiItemRenderState.itemStackRenderState();
/* 53 */     boolean flat = !itemStackRenderState.usesBlockLight();
/* 54 */     if (flat) {
/* 55 */       (Minecraft.getInstance()).gameRenderer.lighting().setupFor(Lighting.Entry.ITEMS_FLAT);
/*    */     } else {
/* 57 */       (Minecraft.getInstance()).gameRenderer.lighting().setupFor(Lighting.Entry.ITEMS_3D);
/*    */     } 
/* 59 */     itemStackRenderState.submit(poseStack, submitNodeCollector, 15728880, OverlayTexture.NO_OVERLAY, 0);
/* 60 */     this.modelOnTextureIdentity = itemStackRenderState.getModelIdentity();
/*    */   }
/*    */   
/*    */   public void blitTexture(OversizedItemRenderState renderState, GuiRenderState guiRenderState) {
/* 65 */     super.blitTexture(renderState, guiRenderState);
/* 66 */     this.usedOnThisFrame = true;
/*    */   }
/*    */   
/*    */   public boolean textureIsReadyToBlit(OversizedItemRenderState renderState) {
/* 71 */     TrackingItemStackRenderState itemStackRenderState = renderState.guiItemRenderState().itemStackRenderState();
/* 72 */     return (!itemStackRenderState.isAnimated() && itemStackRenderState.getModelIdentity().equals(this.modelOnTextureIdentity));
/*    */   }
/*    */   
/*    */   protected float getTranslateY(int height, int guiScale) {
/* 77 */     return height / 2.0F;
/*    */   }
/*    */   
/*    */   protected String getTextureLabel() {
/* 82 */     return "oversized_item";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\render\pip\OversizedItemRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */