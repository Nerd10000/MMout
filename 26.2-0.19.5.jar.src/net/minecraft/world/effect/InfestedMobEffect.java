/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Silverfish;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ class InfestedMobEffect extends MobEffect {
/*    */   private final float chanceToSpawn;
/*    */   
/*    */   private final ToIntFunction<RandomSource> spawnedCount;
/*    */   
/*    */   protected InfestedMobEffect(MobEffectCategory category, int color, float chanceToSpawn, ToIntFunction<RandomSource> spawnedCount) {
/* 25 */     super(category, color, (ParticleOptions)ParticleTypes.INFESTED);
/* 26 */     this.chanceToSpawn = chanceToSpawn;
/* 27 */     this.spawnedCount = spawnedCount;
/*    */   }
/*    */   
/*    */   public void onMobHurt(ServerLevel level, LivingEntity mob, int amplifier, DamageSource source, float damage) {
/* 32 */     if (mob.getRandom().nextFloat() <= this.chanceToSpawn) {
/* 33 */       int count = this.spawnedCount.applyAsInt(mob.getRandom());
/* 34 */       for (int i = 0; i < count; i++)
/* 35 */         spawnSilverfish(level, mob, mob.getX(), mob.getY() + mob.getBbHeight() / 2.0D, mob.getZ()); 
/*    */     } 
/*    */   }
/*    */   
/*    */   private void spawnSilverfish(ServerLevel level, LivingEntity mob, double x, double y, double z) {
/* 41 */     Silverfish silverfish = (Silverfish)EntityTypes.SILVERFISH.create((Level)level, EntitySpawnReason.TRIGGERED);
/* 43 */     if (silverfish == null)
/*    */       return; 
/* 47 */     RandomSource random = mob.getRandom();
/* 48 */     float angle = 1.5707964F;
/* 49 */     float randomAngle = Mth.randomBetween(random, -1.5707964F, 1.5707964F);
/* 50 */     Vector3f viewDirection = mob.getLookAngle().toVector3f().mul(0.3F).mul(1.0F, 1.5F, 1.0F).rotateY(randomAngle);
/* 52 */     silverfish.snapTo(x, y, z, level.getRandom().nextFloat() * 360.0F, 0.0F);
/* 53 */     silverfish.setDeltaMovement(new Vec3((Vector3fc)viewDirection));
/* 54 */     level.addFreshEntity((Entity)silverfish);
/* 55 */     silverfish.playSound(SoundEvents.SILVERFISH_HURT);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\InfestedMobEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */