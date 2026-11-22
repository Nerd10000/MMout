/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.cubemob.Slime;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.entity.EntityTypeTest;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ class OozingMobEffect extends MobEffect {
/*    */   private static final int RADIUS_TO_CHECK_SLIMES = 2;
/*    */   
/*    */   public static final int SLIME_SIZE = 2;
/*    */   
/*    */   private final ToIntFunction<RandomSource> spawnedCount;
/*    */   
/*    */   protected OozingMobEffect(MobEffectCategory category, int color, ToIntFunction<RandomSource> spawnedCount) {
/* 28 */     super(category, color, (ParticleOptions)ParticleTypes.ITEM_SLIME);
/* 29 */     this.spawnedCount = spawnedCount;
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   protected static int numberOfSlimesToSpawn(int maxEntityCramming, NearbySlimes nearbySlimes, int numberRequested) {
/* 34 */     if (maxEntityCramming < 1)
/* 35 */       return numberRequested; 
/* 39 */     return Mth.clamp(0, maxEntityCramming - nearbySlimes.count(maxEntityCramming), numberRequested);
/*    */   }
/*    */   
/*    */   public void onMobRemoved(ServerLevel level, LivingEntity mob, int amplifier, Entity.RemovalReason reason) {
/* 44 */     if (reason != Entity.RemovalReason.KILLED)
/*    */       return; 
/* 48 */     int requestedSlimesToSpawn = this.spawnedCount.applyAsInt(mob.getRandom());
/* 49 */     int maxEntityCramming = ((Integer)level.getGameRules().get(GameRules.MAX_ENTITY_CRAMMING)).intValue();
/* 50 */     int numberOfSlimesToSpawn = numberOfSlimesToSpawn(maxEntityCramming, NearbySlimes.closeTo(mob), requestedSlimesToSpawn);
/* 52 */     for (int i = 0; i < numberOfSlimesToSpawn; i++)
/* 53 */       spawnSlimeOffspring(mob.level(), mob.getX(), mob.getY() + 0.5D, mob.getZ()); 
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   protected static interface NearbySlimes {
/*    */     int count(int param1Int);
/*    */     
/*    */     private static NearbySlimes closeTo(LivingEntity mob) {
/* 62 */       return maxResults -> {
/*    */           List<Slime> slimesNearby = new ArrayList<>();
/*    */           mob.level().getEntities((EntityTypeTest)EntityTypes.SLIME, mob.getBoundingBox().inflate(2.0D), (), slimesNearby, maxResults);
/*    */           return slimesNearby.size();
/*    */         };
/*    */     }
/*    */   }
/*    */   
/*    */   private void spawnSlimeOffspring(Level level, double x, double y, double z) {
/* 71 */     Slime slime = (Slime)EntityTypes.SLIME.create(level, EntitySpawnReason.TRIGGERED);
/* 73 */     if (slime == null)
/*    */       return; 
/* 77 */     slime.setSize(2, true);
/* 78 */     slime.snapTo(x, y, z, level.getRandom().nextFloat() * 360.0F, 0.0F);
/* 79 */     level.addFreshEntity((Entity)slime);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\OozingMobEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */