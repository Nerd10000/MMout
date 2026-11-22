/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.projectile.TridentModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class ThrownTridentRenderer extends EntityRenderer<ThrownTrident, ThrownTridentRenderState> {
/* 18 */   public static final Identifier TRIDENT_LOCATION = Identifier.withDefaultNamespace("textures/entity/trident/trident.png");
/*    */   
/*    */   private final TridentModel model;
/*    */   
/*    */   public ThrownTridentRenderer(EntityRendererProvider.Context context) {
/* 23 */     super(context);
/* 24 */     this.model = new TridentModel(context.bakeLayer(ModelLayers.TRIDENT));
/*    */   }
/*    */   
/*    */   public void submit(ThrownTridentRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 29 */     poseStack.pushPose();
/* 31 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(state.yRot - 90.0F));
/* 32 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(state.xRot + 90.0F));
/* 34 */     submitNodeCollector.order(0).submitModel((Model)this.model, Unit.INSTANCE, poseStack, TRIDENT_LOCATION, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/* 35 */     if (state.isFoil)
/* 36 */       submitNodeCollector.order(1).submitModel((Model)this.model, Unit.INSTANCE, poseStack, RenderTypes.entityGlint(), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null); 
/* 39 */     poseStack.popPose();
/* 41 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   
/*    */   protected AABB getBoundingBoxForCulling(ThrownTrident entity) {
/* 48 */     return super.getBoundingBoxForCulling(entity).inflate(1.5D);
/*    */   }
/*    */   
/*    */   public ThrownTridentRenderState createRenderState() {
/* 53 */     return new ThrownTridentRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(ThrownTrident entity, ThrownTridentRenderState state, float partialTicks) {
/* 58 */     super.extractRenderState(entity, state, partialTicks);
/* 59 */     state.yRot = entity.getYRot(partialTicks);
/* 60 */     state.xRot = entity.getXRot(partialTicks);
/* 61 */     state.isFoil = entity.isFoil();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\ThrownTridentRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */