/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.bell.BellModel;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BellRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteId;
/*    */ import net.minecraft.world.level.block.entity.BellBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BellRenderer implements BlockEntityRenderer<BellBlockEntity, BellRenderState> {
/* 20 */   public static final SpriteId BELL_TEXTURE = Sheets.BLOCK_ENTITIES_MAPPER.defaultNamespaceApply("bell/bell_body");
/*    */   
/*    */   private final SpriteGetter sprites;
/*    */   
/*    */   private final BellModel model;
/*    */   
/*    */   public BellRenderer(BlockEntityRendererProvider.Context context) {
/* 26 */     this.sprites = context.sprites();
/* 27 */     this.model = new BellModel(context.bakeLayer(ModelLayers.BELL));
/*    */   }
/*    */   
/*    */   public BellRenderState createRenderState() {
/* 32 */     return new BellRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(BellBlockEntity blockEntity, BellRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 37 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 38 */     state.ticks = blockEntity.ticks + partialTicks;
/* 39 */     state.shakeDirection = blockEntity.shaking ? blockEntity.clickDirection : null;
/*    */   }
/*    */   
/*    */   public void submit(BellRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 44 */     BellModel.State modelState = new BellModel.State(state.ticks, state.shakeDirection);
/* 45 */     this.model.setupAnim(modelState);
/* 46 */     submitNodeCollector.submitModel((Model)this.model, modelState, poseStack, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, BELL_TEXTURE, this.sprites, 0, state.breakProgress);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\BellRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */