/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.block.BlockModelRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.MinecartRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.MinecartTntRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*    */ import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
/*    */ 
/*    */ public class TntMinecartRenderer extends AbstractMinecartRenderer<MinecartTNT, MinecartTntRenderState> {
/*    */   public TntMinecartRenderer(EntityRendererProvider.Context context) {
/* 14 */     super(context, ModelLayers.TNT_MINECART);
/*    */   }
/*    */   
/*    */   protected void submitMinecartContents(MinecartTntRenderState state, BlockModelRenderState blockModel, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
/* 19 */     float fuse = state.fuseRemainingInTicks;
/* 21 */     if (fuse > -1.0F && fuse < 10.0F) {
/* 22 */       float swell = TntRenderer.getSwellAmount(fuse);
/* 24 */       poseStack.translate(-swell * 0.5D, 0.0D, -swell * 0.5D);
/* 25 */       float scale = 1.0F + swell;
/* 26 */       poseStack.scale(scale, scale, scale);
/*    */     } 
/* 29 */     submitWhiteSolidBlock(blockModel, poseStack, submitNodeCollector, lightCoords, TntRenderer.isLit(fuse), state.outlineColor);
/*    */   }
/*    */   
/*    */   public static void submitWhiteSolidBlock(BlockModelRenderState blockModel, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, boolean white, int outlineColor) {
/*    */     int overlayCoords;
/* 35 */     if (white) {
/* 36 */       overlayCoords = OverlayTexture.pack(OverlayTexture.u(1.0F), 10);
/*    */     } else {
/* 38 */       overlayCoords = OverlayTexture.NO_OVERLAY;
/*    */     } 
/* 41 */     blockModel.submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
/*    */   }
/*    */   
/*    */   public MinecartTntRenderState createRenderState() {
/* 46 */     return new MinecartTntRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(MinecartTNT entity, MinecartTntRenderState state, float partialTicks) {
/* 51 */     super.extractRenderState(entity, state, partialTicks);
/* 54 */     state.fuseRemainingInTicks = (entity.getFuse() > -1) ? (entity.getFuse() - partialTicks + 1.0F) : -1.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\TntMinecartRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */