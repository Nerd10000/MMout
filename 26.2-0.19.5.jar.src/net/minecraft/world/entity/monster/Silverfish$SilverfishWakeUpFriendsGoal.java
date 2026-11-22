/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.InfestedBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ class SilverfishWakeUpFriendsGoal extends Goal {
/*     */   private final Silverfish silverfish;
/*     */   
/*     */   private int lookForFriends;
/*     */   
/*     */   public SilverfishWakeUpFriendsGoal(Silverfish silverfish) {
/* 142 */     this.silverfish = silverfish;
/*     */   }
/*     */   
/*     */   public void notifyHurt() {
/* 146 */     if (this.lookForFriends == 0)
/* 147 */       this.lookForFriends = adjustedTickDelay(20); 
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 153 */     return (this.lookForFriends > 0);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 158 */     this.lookForFriends--;
/* 159 */     if (this.lookForFriends <= 0) {
/* 160 */       Level level = this.silverfish.level();
/* 161 */       RandomSource random = this.silverfish.getRandom();
/* 164 */       BlockPos basePos = this.silverfish.blockPosition();
/*     */       int yOff;
/* 167 */       for (yOff = 0; yOff <= 5 && yOff >= -5; yOff = ((yOff <= 0) ? 1 : 0) - yOff) {
/*     */         int xOff;
/* 168 */         for (xOff = 0; xOff <= 10 && xOff >= -10; xOff = ((xOff <= 0) ? 1 : 0) - xOff) {
/*     */           int zOff;
/* 169 */           for (zOff = 0; zOff <= 10 && zOff >= -10; zOff = ((zOff <= 0) ? 1 : 0) - zOff) {
/* 170 */             BlockPos testPos = basePos.offset(xOff, yOff, zOff);
/* 171 */             BlockState blockState = level.getBlockState(testPos);
/* 173 */             Block block = blockState.getBlock();
/* 174 */             if (block instanceof InfestedBlock) {
/* 174 */               InfestedBlock infestedBlock = (InfestedBlock)block;
/* 175 */               if (((Boolean)getServerLevel(level).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 176 */                 level.destroyBlock(testPos, true, (Entity)this.silverfish);
/*     */               } else {
/* 178 */                 level.setBlock(testPos, infestedBlock.hostStateByInfested(level.getBlockState(testPos)), 3);
/*     */               } 
/* 180 */               if (random.nextBoolean())
/*     */                 // Byte code: goto -> 255 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Silverfish$SilverfishWakeUpFriendsGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */