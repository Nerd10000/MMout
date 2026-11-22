/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ 
/*    */ import net.minecraft.core.particles.ItemParticleOption;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class Snowball extends ThrowableItemProjectile {
/*    */   public Snowball(EntityType<? extends Snowball> type, Level level) {
/* 22 */     super((EntityType)type, level);
/*    */   }
/*    */   
/*    */   public Snowball(Level level, LivingEntity mob, ItemStack itemStack) {
/* 26 */     super(EntityTypes.SNOWBALL, mob, level, itemStack);
/*    */   }
/*    */   
/*    */   public Snowball(Level level, double x, double y, double z, ItemStack itemStack) {
/* 30 */     super(EntityTypes.SNOWBALL, x, y, z, level, itemStack);
/*    */   }
/*    */   
/*    */   protected Item getDefaultItem() {
/* 35 */     return Items.SNOWBALL;
/*    */   }
/*    */   
/*    */   private ParticleOptions getParticle() {
/* 39 */     ItemStack item = getItem();
/* 40 */     return item.isEmpty() ? (ParticleOptions)ParticleTypes.ITEM_SNOWBALL : (ParticleOptions)new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(item));
/*    */   }
/*    */   
/*    */   public void handleEntityEvent(byte id) {
/* 45 */     if (id == 3) {
/* 46 */       ParticleOptions particle = getParticle();
/* 47 */       for (int i = 0; i < 8; i++)
/* 48 */         level().addParticle(particle, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D); 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void onHitEntity(EntityHitResult hitResult) {
/* 55 */     super.onHitEntity(hitResult);
/* 56 */     Entity entity = hitResult.getEntity();
/* 57 */     int damage = (entity instanceof net.minecraft.world.entity.monster.Blaze) ? 3 : 0;
/* 59 */     entity.hurt(damageSources().thrown((Entity)this, getOwner()), damage);
/*    */   }
/*    */   
/*    */   protected void onHit(HitResult hitResult) {
/* 64 */     super.onHit(hitResult);
/* 66 */     if (!level().isClientSide()) {
/* 67 */       level().broadcastEntityEvent((Entity)this, (byte)3);
/* 68 */       discard();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\Snowball.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */