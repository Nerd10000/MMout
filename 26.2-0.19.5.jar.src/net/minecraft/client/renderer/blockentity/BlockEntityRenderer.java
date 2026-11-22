/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface BlockEntityRenderer<T extends BlockEntity, S extends BlockEntityRenderState> {
/*    */   S createRenderState();
/*    */   
/*    */   default void extractRenderState(T blockEntity, S state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 16 */     BlockEntityRenderState.extractBase((BlockEntity)blockEntity, (BlockEntityRenderState)state, breakProgress);
/*    */   }
/*    */   
/*    */   void submit(S paramS, PoseStack paramPoseStack, SubmitNodeCollector paramSubmitNodeCollector, CameraRenderState paramCameraRenderState);
/*    */   
/*    */   default boolean shouldRenderOffScreen() {
/* 22 */     return false;
/*    */   }
/*    */   
/*    */   default int getViewDistance() {
/* 26 */     return 64;
/*    */   }
/*    */   
/*    */   default boolean shouldRender(T blockEntity, Vec3 cameraPosition) {
/* 30 */     return Vec3.atCenterOf((Vec3i)blockEntity.getBlockPos()).closerThan((Position)cameraPosition, getViewDistance());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\BlockEntityRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */