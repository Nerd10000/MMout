/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.animal.squid.SquidModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SquidRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.squid.Squid;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class SquidRenderer<T extends Squid> extends AgeableMobRenderer<T, SquidRenderState, SquidModel> {
/* 12 */   private static final Identifier SQUID_LOCATION = Identifier.withDefaultNamespace("textures/entity/squid/squid.png");
/*    */   
/* 13 */   private static final Identifier SQUID_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/squid/squid_baby.png");
/*    */   
/*    */   public SquidRenderer(EntityRendererProvider.Context context, SquidModel model, SquidModel babyModel) {
/* 16 */     super(context, model, babyModel, 0.7F);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(SquidRenderState state) {
/* 21 */     return state.isBaby ? SQUID_BABY_LOCATION : SQUID_LOCATION;
/*    */   }
/*    */   
/*    */   public SquidRenderState createRenderState() {
/* 26 */     return new SquidRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(T entity, SquidRenderState state, float partialTicks) {
/* 31 */     super.extractRenderState(entity, state, partialTicks);
/* 32 */     state.tentacleAngle = Mth.lerp(partialTicks, ((Squid)entity).oldTentacleAngle, ((Squid)entity).tentacleAngle);
/* 33 */     state.xBodyRot = Mth.lerp(partialTicks, ((Squid)entity).xBodyRotO, ((Squid)entity).xBodyRot);
/* 34 */     state.zBodyRot = Mth.lerp(partialTicks, ((Squid)entity).zBodyRotO, ((Squid)entity).zBodyRot);
/*    */   }
/*    */   
/*    */   protected void setupRotations(SquidRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 39 */     poseStack.translate(0.0F, state.isBaby ? 0.25F : 0.5F, 0.0F);
/* 40 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F - bodyRot));
/* 41 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(state.xBodyRot));
/* 42 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(state.zBodyRot));
/* 43 */     poseStack.translate(0.0F, state.isBaby ? -0.6F : -1.2F, 0.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\SquidRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */