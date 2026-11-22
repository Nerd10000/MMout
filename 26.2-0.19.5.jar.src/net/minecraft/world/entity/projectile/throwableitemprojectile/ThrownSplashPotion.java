/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class ThrownSplashPotion extends AbstractThrownPotion {
/*    */   public ThrownSplashPotion(EntityType<? extends ThrownSplashPotion> type, Level level) {
/* 26 */     super((EntityType)type, level);
/*    */   }
/*    */   
/*    */   public ThrownSplashPotion(Level level, LivingEntity owner, ItemStack itemStack) {
/* 30 */     super(EntityTypes.SPLASH_POTION, level, owner, itemStack);
/*    */   }
/*    */   
/*    */   public ThrownSplashPotion(Level level, double x, double y, double z, ItemStack itemStack) {
/* 34 */     super(EntityTypes.SPLASH_POTION, level, x, y, z, itemStack);
/*    */   }
/*    */   
/*    */   protected Item getDefaultItem() {
/* 39 */     return Items.SPLASH_POTION;
/*    */   }
/*    */   
/*    */   public void onHitAsPotion(ServerLevel level, ItemStack potionItem, HitResult hitResult) {
/* 44 */     PotionContents contents = (PotionContents)potionItem.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/* 45 */     float durationScale = ((Float)potionItem.getOrDefault(DataComponents.POTION_DURATION_SCALE, Float.valueOf(1.0F))).floatValue();
/* 47 */     Iterable<MobEffectInstance> mobEffects = contents.getAllEffects();
/* 48 */     AABB potionAabb = getBoundingBox().move(hitResult.getLocation().subtract(position()));
/* 49 */     AABB effectAabb = potionAabb.inflate(4.0D, 2.0D, 4.0D);
/* 50 */     List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, effectAabb);
/* 52 */     float margin = ProjectileUtil.computeMargin((Entity)this);
/* 54 */     if (!entities.isEmpty()) {
/* 55 */       Entity effectSource = getEffectSource();
/* 56 */       for (LivingEntity entity : entities) {
/* 57 */         if (!entity.isAffectedByPotions())
/*    */           continue; 
/* 61 */         double dist = potionAabb.distanceToSqr(entity.getBoundingBox().inflate(margin));
/* 62 */         if (dist < 16.0D) {
/* 63 */           double scale = 1.0D - Math.sqrt(dist) / 4.0D;
/* 64 */           for (MobEffectInstance effectInstance : mobEffects) {
/* 65 */             Holder<MobEffect> effect = effectInstance.getEffect();
/* 66 */             if (((MobEffect)effect.value()).isInstantaneous()) {
/* 67 */               ((MobEffect)effect.value()).applyInstantaneousEffect(level, (Entity)this, getOwner(), entity, effectInstance.getAmplifier(), scale);
/*    */               continue;
/*    */             } 
/* 69 */             int duration = effectInstance.mapDuration(d -> (int)(scale * d * durationScale + 0.5D));
/* 70 */             MobEffectInstance newEffect = new MobEffectInstance(effect, duration, effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible());
/* 71 */             if (!newEffect.endsWithin(20))
/* 72 */               entity.addEffect(newEffect, effectSource); 
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\ThrownSplashPotion.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */