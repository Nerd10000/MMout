/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.hurtingprojectile.DragonFireball;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class DragonFireballRenderer extends EntityRenderer<DragonFireball, EntityRenderState> {
/* 18 */   private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon_fireball.png");
/*    */   
/* 19 */   private static final RenderType RENDER_TYPE = RenderTypes.entityCutout(TEXTURE_LOCATION);
/*    */   
/*    */   public DragonFireballRenderer(EntityRendererProvider.Context context) {
/* 22 */     super(context);
/*    */   }
/*    */   
/*    */   protected int getBlockLightLevel(DragonFireball entity, BlockPos blockPos) {
/* 27 */     return 15;
/*    */   }
/*    */   
/*    */   public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 32 */     poseStack.pushPose();
/* 33 */     poseStack.scale(2.0F, 2.0F, 2.0F);
/* 35 */     poseStack.mulPose((Quaternionfc)camera.orientation);
/* 37 */     submitNodeCollector.submitCustomGeometry(poseStack, RENDER_TYPE, (pose, buffer) -> buildQuad(state, pose, buffer, -1));
/* 39 */     if (state.outlineColor != 0 && RENDER_TYPE.outline().isPresent())
/* 40 */       submitNodeCollector.submitCustomGeometry(poseStack, RENDER_TYPE.outline().get(), (pose, buffer) -> buildQuad(state, pose, buffer, state.outlineColor)); 
/* 43 */     poseStack.popPose();
/* 45 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   
/*    */   private static void buildQuad(EntityRenderState state, PoseStack.Pose pose, VertexConsumer buffer, int color) {
/* 49 */     vertex(buffer, pose, state.lightCoords, 0.0F, 0, 0, 1, color);
/* 50 */     vertex(buffer, pose, state.lightCoords, 1.0F, 0, 1, 1, color);
/* 51 */     vertex(buffer, pose, state.lightCoords, 1.0F, 1, 1, 0, color);
/* 52 */     vertex(buffer, pose, state.lightCoords, 0.0F, 1, 0, 0, color);
/*    */   }
/*    */   
/*    */   private static void vertex(VertexConsumer builder, PoseStack.Pose pose, int lightCoords, float x, int y, int u, int v, int color) {
/* 56 */     builder.addVertex(pose, x - 0.5F, y - 0.25F, 0.0F).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0F, 1.0F, 0.0F);
/*    */   }
/*    */   
/*    */   public EntityRenderState createRenderState() {
/* 61 */     return new EntityRenderState();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\DragonFireballRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */