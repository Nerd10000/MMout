/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public interface NoDataSpecialModelRenderer extends SpecialModelRenderer<Void> {
/*    */   default Void extractArgument(ItemStack stack) {
/* 12 */     return null;
/*    */   }
/*    */   
/*    */   default void submit(Void argument, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 17 */     submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, hasFoil, outlineColor);
/*    */   }
/*    */   
/*    */   void submit(PoseStack paramPoseStack, SubmitNodeCollector paramSubmitNodeCollector, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3);
/*    */   
/*    */   public static interface Unbaked extends SpecialModelRenderer.Unbaked<Void> {
/*    */     MapCodec<? extends Unbaked> type();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\NoDataSpecialModelRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */