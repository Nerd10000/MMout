/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.BoatRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*    */ import org.joml.Quaternionf;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public abstract class AbstractBoatRenderer extends EntityRenderer<AbstractBoat, BoatRenderState> {
/*    */   protected final Identifier texture;
/*    */   
/*    */   public AbstractBoatRenderer(EntityRendererProvider.Context context, Identifier texture) {
/* 19 */     super(context);
/* 20 */     this.texture = texture;
/* 21 */     this.shadowRadius = 0.8F;
/*    */   }
/*    */   
/*    */   public void submit(BoatRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 26 */     poseStack.pushPose();
/* 28 */     poseStack.translate(0.0F, 0.375F, 0.0F);
/* 29 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F - state.yRot));
/* 30 */     float hurt = state.hurtTime;
/* 31 */     if (hurt > 0.0F)
/* 32 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(Mth.sin(hurt) * hurt * state.damageTime / 10.0F * state.hurtDir)); 
/* 35 */     if (!state.isUnderWater && !Mth.equal(state.bubbleAngle, 0.0F))
/* 37 */       poseStack.mulPose((Quaternionfc)(new Quaternionf()).setAngleAxis(state.bubbleAngle * 0.017453292F, 1.0F, 0.0F, 1.0F)); 
/* 40 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/* 41 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(90.0F));
/* 43 */     submitNodeCollector.submitModel((Model)model(), state, poseStack, this.texture, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/* 45 */     submitTypeAdditions(state, poseStack, submitNodeCollector, state.lightCoords);
/* 47 */     poseStack.popPose();
/* 49 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   
/*    */   protected void submitTypeAdditions(BoatRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {}
/*    */   
/*    */   public BoatRenderState createRenderState() {
/* 59 */     return new BoatRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(AbstractBoat entity, BoatRenderState state, float partialTicks) {
/* 64 */     super.extractRenderState(entity, state, partialTicks);
/* 65 */     state.yRot = entity.getYRot(partialTicks);
/* 66 */     state.hurtTime = entity.getHurtTime() - partialTicks;
/* 67 */     state.hurtDir = entity.getHurtDir();
/* 68 */     state.damageTime = Math.max(entity.getDamage() - partialTicks, 0.0F);
/* 69 */     state.bubbleAngle = entity.getBubbleAngle(partialTicks);
/* 70 */     state.isUnderWater = entity.isUnderWater();
/* 71 */     state.rowingTimeLeft = entity.getRowingTime(0, partialTicks);
/* 72 */     state.rowingTimeRight = entity.getRowingTime(1, partialTicks);
/*    */   }
/*    */   
/*    */   protected abstract EntityModel<BoatRenderState> model();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\AbstractBoatRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */