/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SlimeRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
/*    */ 
/*    */ public abstract class AbstractCubeMobRenderer<T extends AbstractCubeMob, S extends SlimeRenderState, M extends EntityModel<? super S>> extends MobRenderer<T, S, M> {
/*    */   public AbstractCubeMobRenderer(EntityRendererProvider.Context context, M model) {
/* 12 */     super(context, model, 0.25F);
/*    */   }
/*    */   
/*    */   protected float getShadowRadius(SlimeRenderState state) {
/* 17 */     return state.size * 0.25F;
/*    */   }
/*    */   
/*    */   protected void scale(S state, PoseStack poseStack) {
/* 22 */     super.scale(state, poseStack);
/* 23 */     applySizeAndSquish(state, poseStack);
/*    */   }
/*    */   
/*    */   protected void downscaleSlightly(PoseStack poseStack) {
/* 28 */     float s = 0.999F;
/* 29 */     poseStack.scale(0.999F, 0.999F, 0.999F);
/* 30 */     poseStack.translate(0.0F, 0.001F, 0.0F);
/*    */   }
/*    */   
/*    */   protected void applySizeAndSquish(S state, PoseStack poseStack) {
/* 34 */     float size = ((SlimeRenderState)state).size;
/* 35 */     float ss = ((SlimeRenderState)state).squish / (size * 0.5F + 1.0F);
/* 36 */     float w = 1.0F / (ss + 1.0F);
/* 37 */     poseStack.scale(w * size, 1.0F / w * size, w * size);
/*    */   }
/*    */   
/*    */   public void extractRenderState(T entity, S state, float partialTicks) {
/* 42 */     super.extractRenderState(entity, state, partialTicks);
/* 43 */     ((SlimeRenderState)state).squish = Mth.lerp(partialTicks, ((AbstractCubeMob)entity).oSquish, ((AbstractCubeMob)entity).squish);
/* 44 */     ((SlimeRenderState)state).size = entity.getSize();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\AbstractCubeMobRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */