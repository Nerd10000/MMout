/*    */ package net.minecraft.world.entity.animal.fish;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.pathfinder.PathType;
/*    */ 
/*    */ public abstract class WaterAnimal extends PathfinderMob {
/*    */   public static final int AMBIENT_SOUND_INTERVAL = 120;
/*    */   
/*    */   protected WaterAnimal(EntityType<? extends WaterAnimal> type, Level level) {
/* 23 */     super(type, level);
/* 25 */     setPathfindingMalus(PathType.WATER, 0.0F);
/*    */   }
/*    */   
/*    */   public boolean checkSpawnObstruction(LevelReader level) {
/* 30 */     return level.isUnobstructed((Entity)this);
/*    */   }
/*    */   
/*    */   public int getAmbientSoundInterval() {
/* 35 */     return 120;
/*    */   }
/*    */   
/*    */   protected int getBaseExperienceReward(ServerLevel level) {
/* 40 */     return 1 + this.random.nextInt(3);
/*    */   }
/*    */   
/*    */   protected void handleAirSupply(ServerLevel level, int preTickAirSupply) {
/* 44 */     if (isAlive() && !isInWater()) {
/* 45 */       setAirSupply(preTickAirSupply - 1);
/* 46 */       if (shouldTakeDrowningDamage()) {
/* 47 */         setAirSupply(0);
/* 48 */         hurtServer(level, damageSources().drown(), 2.0F);
/*    */       } 
/*    */     } else {
/* 51 */       setAirSupply(300);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void baseTick() {
/* 57 */     int airSupply = getAirSupply();
/* 58 */     super.baseTick();
/* 59 */     Level level = level();
/* 59 */     if (level instanceof ServerLevel) {
/* 59 */       ServerLevel serverLevel = (ServerLevel)level;
/* 60 */       handleAirSupply(serverLevel, airSupply);
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isPushedByFluid() {
/* 67 */     return false;
/*    */   }
/*    */   
/*    */   public boolean canBeLeashed() {
/* 72 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<? extends WaterAnimal> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 76 */     int seaLevel = level.getSeaLevel();
/* 77 */     int minSpawnLevel = seaLevel - 13;
/* 78 */     return (pos.getY() >= minSpawnLevel && pos
/* 79 */       .getY() <= seaLevel && level
/* 80 */       .getFluidState(pos.below()).is(FluidTags.WATER) && level
/* 81 */       .getBlockState(pos.above()).is(Blocks.WATER));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fish\WaterAnimal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */