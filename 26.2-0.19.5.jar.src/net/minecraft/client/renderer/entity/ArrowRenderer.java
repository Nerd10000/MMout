/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.projectile.ArrowModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.ArrowRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public abstract class ArrowRenderer<T extends AbstractArrow, S extends ArrowRenderState> extends EntityRenderer<T, S> {
/*    */   private final ArrowModel model;
/*    */   
/*    */   public ArrowRenderer(EntityRendererProvider.Context context) {
/* 18 */     super(context);
/* 19 */     this.model = new ArrowModel(context.bakeLayer(ModelLayers.ARROW));
/*    */   }
/*    */   
/*    */   public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 24 */     poseStack.pushPose();
/* 26 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(((ArrowRenderState)state).yRot - 90.0F));
/* 27 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(((ArrowRenderState)state).xRot));
/* 29 */     submitNodeCollector.submitModel((Model)this.model, state, poseStack, getTextureLocation(state), ((ArrowRenderState)state).lightCoords, OverlayTexture.NO_OVERLAY, ((ArrowRenderState)state).outlineColor, null);
/* 31 */     poseStack.popPose();
/* 33 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   
/*    */   public void extractRenderState(T entity, S state, float partialTicks) {
/* 40 */     super.extractRenderState(entity, state, partialTicks);
/* 41 */     ((ArrowRenderState)state).xRot = entity.getXRot(partialTicks);
/* 42 */     ((ArrowRenderState)state).yRot = entity.getYRot(partialTicks);
/* 43 */     ((ArrowRenderState)state).shake = ((AbstractArrow)entity).shakeTime - partialTicks;
/*    */   }
/*    */   
/*    */   protected abstract Identifier getTextureLocation(S paramS);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\ArrowRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */