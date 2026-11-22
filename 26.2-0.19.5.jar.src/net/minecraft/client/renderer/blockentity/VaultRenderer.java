/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.VaultRenderState;
/*    */ import net.minecraft.client.renderer.entity.ItemEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.vault.VaultClientData;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class VaultRenderer implements BlockEntityRenderer<VaultBlockEntity, VaultRenderState> {
/*    */   private final ItemModelResolver itemModelResolver;
/*    */   
/* 23 */   private final RandomSource random = RandomSource.create();
/*    */   
/*    */   public VaultRenderer(BlockEntityRendererProvider.Context context) {
/* 26 */     this.itemModelResolver = context.itemModelResolver();
/*    */   }
/*    */   
/*    */   public VaultRenderState createRenderState() {
/* 31 */     return new VaultRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(VaultBlockEntity blockEntity, VaultRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 36 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 37 */     ItemStack displayItem = blockEntity.getSharedData().getDisplayItem();
/* 38 */     if (!VaultBlockEntity.Client.shouldDisplayActiveEffects(blockEntity.getSharedData()) || displayItem.isEmpty() || blockEntity.getLevel() == null)
/*    */       return; 
/* 41 */     state.displayItem = new ItemClusterRenderState();
/* 42 */     this.itemModelResolver.updateForTopItem(state.displayItem.item, displayItem, ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
/* 43 */     state.displayItem.count = ItemClusterRenderState.getRenderedAmount(displayItem.getCount());
/* 44 */     state.displayItem.seed = ItemClusterRenderState.getSeedForItemStack(displayItem);
/* 45 */     VaultClientData clientData = blockEntity.getClientData();
/* 46 */     state.spin = Mth.rotLerp(partialTicks, clientData.previousSpin(), clientData.currentSpin());
/*    */   }
/*    */   
/*    */   public void submit(VaultRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 51 */     if (state.displayItem == null)
/*    */       return; 
/* 55 */     poseStack.pushPose();
/* 56 */     poseStack.translate(0.5F, 0.4F, 0.5F);
/* 57 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(state.spin));
/* 58 */     ItemEntityRenderer.renderMultipleFromCount(poseStack, submitNodeCollector, state.lightCoords, state.displayItem, this.random);
/* 59 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\VaultRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */