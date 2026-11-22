/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.animal.fish.CodModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.animal.fish.Cod;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class CodRenderer extends MobRenderer<Cod, LivingEntityRenderState, CodModel> {
/* 13 */   private static final Identifier COD_LOCATION = Identifier.withDefaultNamespace("textures/entity/fish/cod.png");
/*    */   
/*    */   public CodRenderer(EntityRendererProvider.Context context) {
/* 16 */     super(context, new CodModel(context.bakeLayer(ModelLayers.COD)), 0.3F);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(LivingEntityRenderState state) {
/* 21 */     return COD_LOCATION;
/*    */   }
/*    */   
/*    */   public LivingEntityRenderState createRenderState() {
/* 26 */     return new LivingEntityRenderState();
/*    */   }
/*    */   
/*    */   protected void setupRotations(LivingEntityRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 31 */     super.setupRotations(state, poseStack, bodyRot, entityScale);
/* 33 */     float bodyZRot = 4.3F * Mth.sin((0.6F * state.ageInTicks));
/* 34 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(bodyZRot));
/* 36 */     if (!state.isInWater) {
/* 37 */       poseStack.translate(0.1F, 0.1F, -0.1F);
/* 38 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(90.0F));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\CodRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */