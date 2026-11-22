/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.monster.hoglin.BabyHoglinModel;
/*    */ import net.minecraft.client.model.monster.hoglin.HoglinModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HoglinRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.monster.hoglin.HoglinBase;
/*    */ 
/*    */ public abstract class AbstractHoglinRenderer<T extends Mob & HoglinBase> extends AgeableMobRenderer<T, HoglinRenderState, HoglinModel> {
/*    */   public AbstractHoglinRenderer(EntityRendererProvider.Context context, ModelLayerLocation adultLayer, ModelLayerLocation babyLayer, float shadow) {
/* 12 */     super(context, new HoglinModel(context.bakeLayer(adultLayer)), new BabyHoglinModel(context.bakeLayer(babyLayer)), shadow);
/*    */   }
/*    */   
/*    */   public HoglinRenderState createRenderState() {
/* 17 */     return new HoglinRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(T entity, HoglinRenderState state, float partialTicks) {
/* 22 */     super.extractRenderState(entity, state, partialTicks);
/* 23 */     state.attackAnimationRemainingTicks = ((HoglinBase)entity).getAttackAnimationRemainingTicks();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\AbstractHoglinRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */