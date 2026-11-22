/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
/*    */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.ItemSupplier;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class ThrownItemRenderer<T extends Entity & ItemSupplier> extends EntityRenderer<T, ThrownItemRenderState> {
/*    */   private final ItemModelResolver itemModelResolver;
/*    */   
/*    */   private final float scale;
/*    */   
/*    */   private final boolean fullBright;
/*    */   
/*    */   public ThrownItemRenderer(EntityRendererProvider.Context context, float scale, boolean fullBright) {
/* 21 */     super(context);
/* 22 */     this.itemModelResolver = context.getItemModelResolver();
/* 23 */     this.scale = scale;
/* 24 */     this.fullBright = fullBright;
/*    */   }
/*    */   
/*    */   public ThrownItemRenderer(EntityRendererProvider.Context context) {
/* 28 */     this(context, 1.0F, false);
/*    */   }
/*    */   
/*    */   protected int getBlockLightLevel(T entity, BlockPos blockPos) {
/* 33 */     return this.fullBright ? 15 : super.getBlockLightLevel(entity, blockPos);
/*    */   }
/*    */   
/*    */   public void submit(ThrownItemRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 38 */     poseStack.pushPose();
/* 40 */     poseStack.scale(this.scale, this.scale, this.scale);
/* 42 */     poseStack.mulPose((Quaternionfc)camera.orientation);
/* 44 */     state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/* 46 */     poseStack.popPose();
/* 48 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   
/*    */   public ThrownItemRenderState createRenderState() {
/* 53 */     return new ThrownItemRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(T entity, ThrownItemRenderState state, float partialTicks) {
/* 58 */     super.extractRenderState(entity, state, partialTicks);
/* 59 */     this.itemModelResolver.updateForNonLiving(state.item, ((ItemSupplier)entity).getItem(), ItemDisplayContext.GROUND, (Entity)entity);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\ThrownItemRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */