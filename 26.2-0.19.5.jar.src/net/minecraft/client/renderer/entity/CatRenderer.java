/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.animal.feline.AbstractFelineModel;
/*    */ import net.minecraft.client.model.animal.feline.AdultCatModel;
/*    */ import net.minecraft.client.model.animal.feline.BabyCatModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.CatRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.feline.Cat;
/*    */ import net.minecraft.world.entity.animal.feline.CatVariant;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class CatRenderer extends AgeableMobRenderer<Cat, CatRenderState, AbstractFelineModel<CatRenderState>> {
/*    */   public CatRenderer(EntityRendererProvider.Context context) {
/* 17 */     super(context, new AdultCatModel(context.bakeLayer(ModelLayers.CAT)), new BabyCatModel(context.bakeLayer(ModelLayers.CAT_BABY)), 0.4F);
/* 19 */     addLayer((RenderLayer<CatRenderState, AbstractFelineModel<CatRenderState>>)new CatCollarLayer(this, context.getModelSet()));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(CatRenderState state) {
/* 24 */     return state.texture;
/*    */   }
/*    */   
/*    */   public CatRenderState createRenderState() {
/* 29 */     return new CatRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Cat entity, CatRenderState state, float partialTicks) {
/* 34 */     super.extractRenderState(entity, state, partialTicks);
/* 35 */     state.texture = ((CatVariant)entity.getVariant().value()).assetInfo(state.isBaby).texturePath();
/* 36 */     state.isCrouching = entity.isCrouching();
/* 37 */     state.isSprinting = entity.isSprinting();
/* 38 */     state.isSitting = entity.isInSittingPose();
/* 39 */     state.lieDownAmount = entity.getLieDownAmount(partialTicks);
/* 40 */     state.lieDownAmountTail = entity.getLieDownAmountTail(partialTicks);
/* 41 */     state.relaxStateOneAmount = entity.getRelaxStateOneAmount(partialTicks);
/* 42 */     state.isLyingOnTopOfSleepingPlayer = entity.isLyingOnTopOfSleepingPlayer();
/* 43 */     state.collarColor = entity.isTame() ? entity.getCollarColor() : null;
/*    */   }
/*    */   
/*    */   protected void setupRotations(CatRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 48 */     super.setupRotations(state, poseStack, bodyRot, entityScale);
/* 50 */     float lieDownAmount = state.lieDownAmount;
/* 51 */     if (lieDownAmount > 0.0F) {
/* 52 */       poseStack.translate(0.4F * lieDownAmount, 0.15F * lieDownAmount, 0.1F * lieDownAmount);
/* 53 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(Mth.rotLerp(lieDownAmount, 0.0F, 90.0F)));
/* 56 */       if (state.isLyingOnTopOfSleepingPlayer)
/* 57 */         poseStack.translate(0.15F * lieDownAmount, 0.0F, 0.0F); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\CatRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */