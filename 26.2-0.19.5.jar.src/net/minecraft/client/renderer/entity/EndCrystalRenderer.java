/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.crystal.EndCrystalModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EndCrystalRenderer extends EntityRenderer<EndCrystal, EndCrystalRenderState> {
/* 18 */   private static final Identifier END_CRYSTAL_LOCATION = Identifier.withDefaultNamespace("textures/entity/end_crystal/end_crystal.png");
/*    */   
/*    */   private final EndCrystalModel model;
/*    */   
/*    */   public EndCrystalRenderer(EntityRendererProvider.Context context) {
/* 23 */     super(context);
/* 24 */     this.shadowRadius = 0.5F;
/* 25 */     this.model = new EndCrystalModel(context.bakeLayer(ModelLayers.END_CRYSTAL));
/*    */   }
/*    */   
/*    */   public void submit(EndCrystalRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 30 */     poseStack.pushPose();
/* 31 */     poseStack.scale(2.0F, 2.0F, 2.0F);
/* 32 */     poseStack.translate(0.0F, -0.5F, 0.0F);
/* 34 */     submitNodeCollector.submitModel((Model)this.model, state, poseStack, END_CRYSTAL_LOCATION, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/* 36 */     poseStack.popPose();
/* 38 */     Vec3 beamOffset = state.beamOffset;
/* 39 */     if (beamOffset != null) {
/* 40 */       float crystalY = getY(state.ageInTicks);
/* 41 */       float deltaX = (float)beamOffset.x;
/* 42 */       float deltaY = (float)beamOffset.y;
/* 43 */       float deltaZ = (float)beamOffset.z;
/* 44 */       poseStack.translate(beamOffset);
/* 45 */       EnderDragonRenderer.submitCrystalBeams(-deltaX, -deltaY + crystalY, -deltaZ, state.ageInTicks, poseStack, submitNodeCollector, state.lightCoords);
/*    */     } 
/* 48 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   
/*    */   public static float getY(float timeInTicks) {
/* 52 */     float hh = Mth.sin((timeInTicks * 0.2F)) / 2.0F + 0.5F;
/* 53 */     hh = (hh * hh + hh) * 0.4F;
/* 54 */     return hh - 1.4F;
/*    */   }
/*    */   
/*    */   public EndCrystalRenderState createRenderState() {
/* 59 */     return new EndCrystalRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(EndCrystal entity, EndCrystalRenderState state, float partialTicks) {
/* 64 */     super.extractRenderState(entity, state, partialTicks);
/* 65 */     state.ageInTicks = entity.time + partialTicks;
/* 66 */     state.showsBottom = entity.showsBottom();
/* 67 */     BlockPos beamTarget = entity.getBeamTarget();
/* 68 */     if (beamTarget != null) {
/* 69 */       state.beamOffset = Vec3.atCenterOf((Vec3i)beamTarget).subtract(entity.getPosition(partialTicks));
/*    */     } else {
/* 71 */       state.beamOffset = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean shouldRender(EndCrystal entity, Frustum culler, double camX, double camY, double camZ) {
/* 77 */     return (super.shouldRender(entity, culler, camX, camY, camZ) || entity.getBeamTarget() != null);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\EndCrystalRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */