/*    */ package net.minecraft.client.gui.render.pip;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Lighting;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*    */ import net.minecraft.client.renderer.state.gui.pip.GuiEntityRenderState;
/*    */ import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import org.joml.Quaternionf;
/*    */ import org.joml.Quaternionfc;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class GuiEntityRenderer extends PictureInPictureRenderer<GuiEntityRenderState> {
/*    */   private final EntityRenderDispatcher entityRenderDispatcher;
/*    */   
/*    */   public GuiEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
/* 19 */     this.entityRenderDispatcher = entityRenderDispatcher;
/*    */   }
/*    */   
/*    */   public Class<GuiEntityRenderState> getRenderStateClass() {
/* 24 */     return GuiEntityRenderState.class;
/*    */   }
/*    */   
/*    */   protected void renderToTexture(GuiEntityRenderState entityState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
/* 29 */     (Minecraft.getInstance()).gameRenderer.lighting().setupFor(Lighting.Entry.ENTITY_IN_UI);
/* 30 */     Vector3fc translation = entityState.translation();
/* 31 */     poseStack.translate(translation.x(), translation.y(), translation.z());
/* 32 */     poseStack.mulPose(entityState.rotation());
/* 34 */     Quaternionfc overriddenCameraAngle = entityState.overrideCameraAngle();
/* 35 */     CameraRenderState cameraRenderState = new CameraRenderState();
/* 36 */     if (overriddenCameraAngle != null)
/* 37 */       cameraRenderState.orientation = overriddenCameraAngle.conjugate(new Quaternionf()).rotateY(3.1415927F); 
/* 39 */     this.entityRenderDispatcher.submit(entityState.renderState(), cameraRenderState, 0.0D, 0.0D, 0.0D, poseStack, submitNodeCollector);
/*    */   }
/*    */   
/*    */   protected float getTranslateY(int height, int guiScale) {
/* 44 */     return height / 2.0F;
/*    */   }
/*    */   
/*    */   protected String getTextureLabel() {
/* 49 */     return "entity";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\render\pip\GuiEntityRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */