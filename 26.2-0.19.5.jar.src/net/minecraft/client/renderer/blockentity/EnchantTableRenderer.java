/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.book.BookModel;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.EnchantTableRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteId;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class EnchantTableRenderer implements BlockEntityRenderer<EnchantingTableBlockEntity, EnchantTableRenderState> {
/* 23 */   public static final SpriteId BOOK_TEXTURE = Sheets.BLOCK_ENTITIES_MAPPER.defaultNamespaceApply("enchantment/enchanting_table_book");
/*    */   
/*    */   private final SpriteGetter sprites;
/*    */   
/*    */   private final BookModel bookModel;
/*    */   
/*    */   public EnchantTableRenderer(BlockEntityRendererProvider.Context context) {
/* 29 */     this.sprites = context.sprites();
/* 30 */     this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
/*    */   }
/*    */   
/*    */   public EnchantTableRenderState createRenderState() {
/* 35 */     return new EnchantTableRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(EnchantingTableBlockEntity blockEntity, EnchantTableRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 40 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 41 */     state.flip = Mth.lerp(partialTicks, blockEntity.oFlip, blockEntity.flip);
/* 42 */     state.open = Mth.lerp(partialTicks, blockEntity.oOpen, blockEntity.open);
/* 43 */     state.time = blockEntity.time + partialTicks;
/* 45 */     float or = blockEntity.rot - blockEntity.oRot;
/* 47 */     while (or >= 3.1415927F)
/* 48 */       or -= 6.2831855F; 
/* 50 */     while (or < -3.1415927F)
/* 51 */       or += 6.2831855F; 
/* 54 */     state.yRot = blockEntity.oRot + or * partialTicks;
/*    */   }
/*    */   
/*    */   public void submit(EnchantTableRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 59 */     poseStack.pushPose();
/* 60 */     poseStack.translate(0.5F, 0.75F, 0.5F);
/* 62 */     poseStack.translate(0.0F, 0.1F + Mth.sin((state.time * 0.1F)) * 0.01F, 0.0F);
/* 64 */     float yRot = state.yRot;
/* 66 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotation(-yRot));
/* 67 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(80.0F));
/* 69 */     float ff1 = Mth.frac(state.flip + 0.25F) * 1.6F - 0.3F;
/* 70 */     float ff2 = Mth.frac(state.flip + 0.75F) * 1.6F - 0.3F;
/* 72 */     BookModel.State bookState = BookModel.State.forAnimation(state.time, Mth.clamp(ff1, 0.0F, 1.0F), Mth.clamp(ff2, 0.0F, 1.0F), state.open);
/* 73 */     submitNodeCollector.submitModel((Model)this.bookModel, bookState, poseStack, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, BOOK_TEXTURE, this.sprites, 0, state.breakProgress);
/* 74 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\EnchantTableRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */