/*    */ package net.minecraft.world.entity.projectile.hurtingprojectile.windcharge;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.monster.breeze.Breeze;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BreezeWindCharge extends AbstractWindCharge {
/*    */   private static final float RADIUS = 3.0F;
/*    */   
/*    */   public BreezeWindCharge(EntityType<? extends AbstractWindCharge> type, Level level) {
/* 17 */     super(type, level);
/*    */   }
/*    */   
/*    */   public BreezeWindCharge(Breeze breeze, Level level) {
/* 21 */     super(EntityTypes.BREEZE_WIND_CHARGE, level, (Entity)breeze, breeze.getX(), breeze.getFiringYPosition(), breeze.getZ());
/*    */   }
/*    */   
/*    */   protected void explode(Vec3 position) {
/* 26 */     level().explode((Entity)this, null, EXPLOSION_DAMAGE_CALCULATOR, position.x(), position.y(), position.z(), 3.0F, false, Level.ExplosionInteraction.TRIGGER, (ParticleOptions)ParticleTypes.GUST_EMITTER_SMALL, (ParticleOptions)ParticleTypes.GUST_EMITTER_LARGE, WeightedList.of(), (Holder)SoundEvents.BREEZE_WIND_CHARGE_BURST);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\windcharge\BreezeWindCharge.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */