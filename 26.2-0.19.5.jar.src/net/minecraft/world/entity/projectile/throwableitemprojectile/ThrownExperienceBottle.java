/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.ExperienceOrb;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ThrownExperienceBottle extends ThrowableItemProjectile {
/*    */   public ThrownExperienceBottle(EntityType<? extends ThrownExperienceBottle> type, Level level) {
/* 20 */     super((EntityType)type, level);
/*    */   }
/*    */   
/*    */   public ThrownExperienceBottle(Level level, LivingEntity mob, ItemStack itemStack) {
/* 24 */     super(EntityTypes.EXPERIENCE_BOTTLE, mob, level, itemStack);
/*    */   }
/*    */   
/*    */   public ThrownExperienceBottle(Level level, double x, double y, double z, ItemStack itemStack) {
/* 28 */     super(EntityTypes.EXPERIENCE_BOTTLE, x, y, z, level, itemStack);
/*    */   }
/*    */   
/*    */   protected Item getDefaultItem() {
/* 33 */     return Items.EXPERIENCE_BOTTLE;
/*    */   }
/*    */   
/*    */   protected double getDefaultGravity() {
/* 38 */     return 0.07D;
/*    */   }
/*    */   
/*    */   protected void onHit(HitResult hitResult) {
/* 43 */     super.onHit(hitResult);
/* 45 */     Level level = level();
/* 45 */     if (level instanceof ServerLevel) {
/* 45 */       ServerLevel serverLevel = (ServerLevel)level;
/* 46 */       serverLevel.levelEvent(2002, blockPosition(), -13083194);
/* 48 */       int xpCount = 3 + this.random.nextInt(5) + this.random.nextInt(5);
/* 49 */       if (hitResult instanceof BlockHitResult) {
/* 49 */         BlockHitResult blockHitResult = (BlockHitResult)hitResult;
/* 50 */         Vec3 blockNormalHit = blockHitResult.getDirection().getUnitVec3();
/* 51 */         ExperienceOrb.awardWithDirection(serverLevel, hitResult.getLocation(), blockNormalHit, xpCount);
/*    */       } else {
/* 53 */         ExperienceOrb.awardWithDirection(serverLevel, hitResult.getLocation(), getDeltaMovement().scale(-1.0D), xpCount);
/*    */       } 
/* 55 */       discard();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\ThrownExperienceBottle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */