/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ public class AgeableMobGroupData implements SpawnGroupData {
/*     */   private int groupSize;
/*     */   
/*     */   private final boolean shouldSpawnBaby;
/*     */   
/*     */   private final float babySpawnChance;
/*     */   
/*     */   public AgeableMobGroupData(boolean shouldSpawnBaby, float babySpawnChance) {
/* 279 */     this.shouldSpawnBaby = shouldSpawnBaby;
/* 280 */     this.babySpawnChance = babySpawnChance;
/*     */   }
/*     */   
/*     */   public AgeableMobGroupData(boolean shouldSpawnBaby) {
/* 284 */     this(shouldSpawnBaby, 0.05F);
/*     */   }
/*     */   
/*     */   public AgeableMobGroupData(float babySpawnChance) {
/* 288 */     this(true, babySpawnChance);
/*     */   }
/*     */   
/*     */   public int getGroupSize() {
/* 292 */     return this.groupSize;
/*     */   }
/*     */   
/*     */   public void increaseGroupSizeByOne() {
/* 296 */     this.groupSize++;
/*     */   }
/*     */   
/*     */   public boolean isShouldSpawnBaby() {
/* 300 */     return this.shouldSpawnBaby;
/*     */   }
/*     */   
/*     */   public float getBabySpawnChance() {
/* 304 */     return this.babySpawnChance;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\AgeableMob$AgeableMobGroupData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */