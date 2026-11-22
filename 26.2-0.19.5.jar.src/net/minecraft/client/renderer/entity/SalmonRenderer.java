/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.animal.fish.SalmonModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SalmonRenderState;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.fish.Salmon;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class SalmonRenderer extends MobRenderer<Salmon, SalmonRenderState, SalmonModel> {
/* 15 */   private static final Identifier SALMON_LOCATION = Identifier.withDefaultNamespace("textures/entity/fish/salmon.png");
/*    */   
/*    */   private final SalmonModel smallSalmonModel;
/*    */   
/*    */   private final SalmonModel mediumSalmonModel;
/*    */   
/*    */   private final SalmonModel largeSalmonModel;
/*    */   
/*    */   public SalmonRenderer(EntityRendererProvider.Context context) {
/* 22 */     super(context, new SalmonModel(context.bakeLayer(ModelLayers.SALMON)), 0.4F);
/* 23 */     this.smallSalmonModel = new SalmonModel(context.bakeLayer(ModelLayers.SALMON_SMALL));
/* 24 */     this.mediumSalmonModel = new SalmonModel(context.bakeLayer(ModelLayers.SALMON));
/* 25 */     this.largeSalmonModel = new SalmonModel(context.bakeLayer(ModelLayers.SALMON_LARGE));
/*    */   }
/*    */   
/*    */   public void extractRenderState(Salmon entity, SalmonRenderState state, float partialTicks) {
/* 30 */     super.extractRenderState(entity, state, partialTicks);
/* 31 */     state.variant = entity.getVariant();
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(SalmonRenderState state) {
/* 36 */     return SALMON_LOCATION;
/*    */   }
/*    */   
/*    */   public SalmonRenderState createRenderState() {
/* 41 */     return new SalmonRenderState();
/*    */   }
/*    */   
/*    */   protected void setupRotations(SalmonRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 46 */     super.setupRotations(state, poseStack, bodyRot, entityScale);
/* 48 */     float amplitudeMultiplier = 1.0F;
/* 49 */     float angleMultiplier = 1.0F;
/* 50 */     if (!state.isInWater) {
/* 51 */       amplitudeMultiplier = 1.3F;
/* 52 */       angleMultiplier = 1.7F;
/*    */     } 
/* 55 */     float bodyZRot = amplitudeMultiplier * 4.3F * Mth.sin((angleMultiplier * 0.6F * state.ageInTicks));
/* 56 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(bodyZRot));
/* 58 */     if (!state.isInWater) {
/* 59 */       poseStack.translate(0.2F, 0.1F, 0.0F);
/* 60 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(90.0F));
/*    */     } 
/*    */   }
/*    */   
/*    */   public void submit(SalmonRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 66 */     switch (state.variant) {
/*    */       default:
/* 66 */         throw new MatchException(null, null);
/*    */       case SMALL:
/*    */       
/*    */       case MEDIUM:
/*    */       
/*    */       case LARGE:
/*    */         break;
/*    */     } 
/* 66 */     this.model = 
/*    */       
/* 69 */       this.largeSalmonModel;
/* 71 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\SalmonRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */