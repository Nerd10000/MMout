/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ public class RamCandidate {
/*     */   private final BlockPos startPosition;
/*     */   
/*     */   private final BlockPos targetPosition;
/*     */   
/*     */   private final LivingEntity target;
/*     */   
/*     */   public RamCandidate(BlockPos startPosition, BlockPos targetPosition, LivingEntity target) {
/* 182 */     this.startPosition = startPosition;
/* 183 */     this.targetPosition = targetPosition;
/* 184 */     this.target = target;
/*     */   }
/*     */   
/*     */   public BlockPos getStartPosition() {
/* 188 */     return this.startPosition;
/*     */   }
/*     */   
/*     */   public BlockPos getTargetPosition() {
/* 192 */     return this.targetPosition;
/*     */   }
/*     */   
/*     */   public LivingEntity getTarget() {
/* 196 */     return this.target;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\PrepareRamNearestTarget$RamCandidate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */