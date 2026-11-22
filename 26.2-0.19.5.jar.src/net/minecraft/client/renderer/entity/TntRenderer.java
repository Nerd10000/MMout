/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.block.BlockModelResolver;
/*    */ import net.minecraft.client.renderer.block.model.BlockDisplayContext;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.TntRenderState;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.item.PrimedTnt;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class TntRenderer extends EntityRenderer<PrimedTnt, TntRenderState> {
/* 14 */   public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
/*    */   
/*    */   private final BlockModelResolver blockModelResolver;
/*    */   
/*    */   public TntRenderer(EntityRendererProvider.Context context) {
/* 19 */     super(context);
/* 20 */     this.shadowRadius = 0.5F;
/* 21 */     this.blockModelResolver = context.getBlockModelResolver();
/*    */   }
/*    */   
/*    */   public void submit(TntRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 26 */     poseStack.pushPose();
/* 27 */     poseStack.translate(0.0F, 0.5F, 0.0F);
/* 28 */     float fuse = state.fuseRemainingInTicks;
/* 29 */     if (fuse < 10.0F) {
/* 30 */       float scale = 1.0F + getSwellAmount(fuse);
/* 31 */       poseStack.scale(scale, scale, scale);
/*    */     } 
/* 34 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-90.0F));
/* 35 */     poseStack.translate(-0.5F, -0.5F, 0.5F);
/* 36 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(90.0F));
/* 38 */     if (!state.blockState.isEmpty())
/* 39 */       TntMinecartRenderer.submitWhiteSolidBlock(state.blockState, poseStack, submitNodeCollector, state.lightCoords, isLit(fuse), state.outlineColor); 
/* 42 */     poseStack.popPose();
/* 44 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   
/*    */   public static float getSwellAmount(float fuse) {
/* 48 */     float g = 1.0F - fuse / 10.0F;
/* 49 */     g = Mth.clamp(g, 0.0F, 1.0F);
/* 50 */     g *= g;
/* 51 */     g *= g;
/* 52 */     return g * 0.3F;
/*    */   }
/*    */   
/*    */   public static boolean isLit(float fuse) {
/* 56 */     if (fuse < 0.0F)
/* 57 */       return false; 
/* 59 */     return ((int)(fuse / 5.0F) % 2 == 0);
/*    */   }
/*    */   
/*    */   public TntRenderState createRenderState() {
/* 64 */     return new TntRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(PrimedTnt entity, TntRenderState state, float partialTicks) {
/* 69 */     super.extractRenderState(entity, state, partialTicks);
/* 72 */     state.fuseRemainingInTicks = entity.getFuse() - partialTicks + 1.0F;
/* 73 */     this.blockModelResolver.update(state.blockState, entity.getBlockState(), BLOCK_DISPLAY_CONTEXT);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\TntRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */