/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ 
/*     */ public class ZombieGroupData implements SpawnGroupData {
/*     */   public final boolean isBaby;
/*     */   
/*     */   public final boolean canSpawnJockey;
/*     */   
/*     */   public ZombieGroupData(boolean baby, boolean canSpawnJockey) {
/* 589 */     this.isBaby = baby;
/* 590 */     this.canSpawnJockey = canSpawnJockey;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\zombie\Zombie$ZombieGroupData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */