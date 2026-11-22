/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.EndGatewayRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.EndPortalRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class TheEndGatewayRenderer extends AbstractEndPortalRenderer<TheEndGatewayBlockEntity, EndGatewayRenderState> {
/* 18 */   private static final Identifier BEAM_LOCATION = Identifier.withDefaultNamespace("textures/entity/end_portal/end_gateway_beam.png");
/*    */   
/*    */   public EndGatewayRenderState createRenderState() {
/* 22 */     return new EndGatewayRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(TheEndGatewayBlockEntity blockEntity, EndGatewayRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 27 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 28 */     Level level = blockEntity.getLevel();
/* 29 */     if (blockEntity.isSpawning() || (blockEntity.isCoolingDown() && level != null)) {
/* 30 */       state.scale = blockEntity.isSpawning() ? blockEntity.getSpawnPercent(partialTicks) : blockEntity.getCooldownPercent(partialTicks);
/* 31 */       double beamDistance = blockEntity.isSpawning() ? blockEntity.getLevel().getMaxY() : 50.0D;
/* 32 */       state.scale = Mth.sin((state.scale * 3.1415927F));
/* 33 */       state.height = Mth.floor(state.scale * beamDistance);
/* 34 */       state.color = blockEntity.isSpawning() ? DyeColor.MAGENTA.getTextureDiffuseColor() : DyeColor.PURPLE.getTextureDiffuseColor();
/* 35 */       state.animationTime = (blockEntity.getLevel() != null) ? (Math.floorMod(blockEntity.getLevel().getGameTime(), 40) + partialTicks) : 0.0F;
/*    */     } else {
/* 37 */       state.height = 0;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void submit(EndGatewayRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 43 */     if (state.height > 0)
/* 44 */       BeaconRenderer.submitBeaconBeam(poseStack, submitNodeCollector, BEAM_LOCATION, state.scale, state.animationTime, -state.height, state.height * 2, state.color, 0.15F, 0.175F); 
/* 46 */     submitCube(state.facesToShow, RenderTypes.endGateway(), poseStack, submitNodeCollector);
/*    */   }
/*    */   
/*    */   public int getViewDistance() {
/* 51 */     return 256;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\TheEndGatewayRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */