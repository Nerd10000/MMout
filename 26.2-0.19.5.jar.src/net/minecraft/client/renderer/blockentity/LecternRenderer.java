/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.book.BookModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.LecternRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.LecternBlock;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.LecternBlockEntity;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class LecternRenderer implements BlockEntityRenderer<LecternBlockEntity, LecternRenderState> {
/*    */   private final SpriteGetter sprites;
/*    */   
/*    */   private final BookModel bookModel;
/*    */   
/* 23 */   private static final BookModel.State BOOK_STATE = BookModel.State.forAnimation(0.0F, 0.1F, 0.9F, 1.2F);
/*    */   
/*    */   public LecternRenderer(BlockEntityRendererProvider.Context context) {
/* 26 */     this.sprites = context.sprites();
/* 27 */     this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
/*    */   }
/*    */   
/*    */   public LecternRenderState createRenderState() {
/* 32 */     return new LecternRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(LecternBlockEntity blockEntity, LecternRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 37 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 38 */     state.hasBook = ((Boolean)blockEntity.getBlockState().getValue((Property)LecternBlock.HAS_BOOK)).booleanValue();
/* 39 */     state.yRot = ((Direction)blockEntity.getBlockState().getValue((Property)LecternBlock.FACING)).getClockWise().toYRot();
/*    */   }
/*    */   
/*    */   public void submit(LecternRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 44 */     if (!state.hasBook)
/*    */       return; 
/* 48 */     poseStack.pushPose();
/* 49 */     poseStack.translate(0.5F, 1.0625F, 0.5F);
/* 51 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-state.yRot));
/* 52 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(67.5F));
/* 54 */     poseStack.translate(0.0F, -0.125F, 0.0F);
/* 56 */     submitNodeCollector.submitModel((Model)this.bookModel, BOOK_STATE, poseStack, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, EnchantTableRenderer.BOOK_TEXTURE, this.sprites, 0, state.breakProgress);
/* 58 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\LecternRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */