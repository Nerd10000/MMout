/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.monster.zombie.ZombieModel;
/*    */ import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieRenderState;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*    */ import net.minecraft.world.item.SwingAnimationType;
/*    */ import net.minecraft.world.item.component.SwingAnimation;
/*    */ 
/*    */ public abstract class AbstractZombieRenderer<T extends Zombie, S extends ZombieRenderState, M extends ZombieModel<S>> extends HumanoidMobRenderer<T, S, M> {
/* 15 */   private static final Identifier ZOMBIE_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/zombie.png");
/*    */   
/* 16 */   private static final Identifier BABY_ZOMBIE_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/zombie_baby.png");
/*    */   
/*    */   protected AbstractZombieRenderer(EntityRendererProvider.Context context, M model, M babyModel, ArmorModelSet<M> armorSet, ArmorModelSet<M> babyArmorSet) {
/* 19 */     super(context, model, babyModel, 0.5F);
/* 21 */     addLayer((RenderLayer<S, M>)new HumanoidArmorLayer(this, armorSet, babyArmorSet, context.getEquipmentRenderer()));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(S state) {
/* 26 */     return ((ZombieRenderState)state).isBaby ? BABY_ZOMBIE_LOCATION : ZOMBIE_LOCATION;
/*    */   }
/*    */   
/*    */   public void extractRenderState(T entity, S state, float partialTicks) {
/* 31 */     super.extractRenderState(entity, state, partialTicks);
/* 32 */     ((ZombieRenderState)state).isAggressive = entity.isAggressive();
/* 33 */     ((ZombieRenderState)state).isConverting = entity.isUnderWaterConverting();
/*    */   }
/*    */   
/*    */   protected boolean isShaking(S state) {
/* 38 */     return (super.isShaking(state) || ((ZombieRenderState)state).isConverting);
/*    */   }
/*    */   
/*    */   protected HumanoidModel.ArmPose getArmPose(T mob, HumanoidArm arm) {
/* 43 */     SwingAnimation otherAnim = (SwingAnimation)mob.getItemHeldByArm(arm.getOpposite()).get(DataComponents.SWING_ANIMATION);
/* 44 */     if (otherAnim != null && otherAnim.type() == SwingAnimationType.STAB)
/* 46 */       return HumanoidModel.ArmPose.SPEAR; 
/* 48 */     return super.getArmPose(mob, arm);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\AbstractZombieRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */