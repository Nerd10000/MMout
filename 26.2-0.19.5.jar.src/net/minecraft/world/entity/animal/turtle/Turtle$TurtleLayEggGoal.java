/*     */ package net.minecraft.world.entity.animal.turtle;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.TurtleEggBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ 
/*     */ class TurtleLayEggGoal extends MoveToBlockGoal {
/*     */   private final Turtle turtle;
/*     */   
/*     */   public TurtleLayEggGoal(Turtle turtle, double speedModifier) {
/* 541 */     super((PathfinderMob)turtle, speedModifier, 16);
/* 542 */     this.turtle = turtle;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 547 */     if (this.turtle.hasEgg() && this.turtle.homePos.closerToCenterThan((Position)this.turtle.position(), 9.0D))
/* 548 */       return super.canUse(); 
/* 550 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 555 */     return (super.canContinueToUse() && this.turtle.hasEgg() && this.turtle.homePos.closerToCenterThan((Position)this.turtle.position(), 9.0D));
/*     */   }
/*     */   
/*     */   public void tick() {
/* 560 */     super.tick();
/* 562 */     BlockPos turtlePos = this.turtle.blockPosition();
/* 563 */     if (!this.turtle.isInWater() && isReachedTarget()) {
/* 564 */       if (this.turtle.layEggCounter < 1) {
/* 565 */         this.turtle.setLayingEgg(true);
/* 566 */       } else if (this.turtle.layEggCounter > adjustedTickDelay(200)) {
/* 567 */         Level level = this.turtle.level();
/* 568 */         level.playSound(null, turtlePos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + level.getRandom().nextFloat() * 0.2F);
/* 569 */         BlockPos eggPos = this.blockPos.above();
/* 570 */         BlockState eggState = (BlockState)Blocks.TURTLE_EGG.defaultBlockState().setValue((Property)TurtleEggBlock.EGGS, Integer.valueOf(Turtle.access$100(this.turtle).nextInt(4) + 1));
/* 571 */         level.setBlock(eggPos, eggState, 3);
/* 572 */         level.gameEvent((Holder)GameEvent.BLOCK_PLACE, eggPos, GameEvent.Context.of((Entity)this.turtle, eggState));
/* 573 */         this.turtle.setHasEgg(false);
/* 574 */         this.turtle.setLayingEgg(false);
/* 575 */         this.turtle.setInLoveTime(600);
/*     */       } 
/* 577 */       if (this.turtle.isLayingEgg())
/* 578 */         this.turtle.layEggCounter++; 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 585 */     if (!level.isEmptyBlock(pos.above()))
/* 586 */       return false; 
/* 589 */     return TurtleEggBlock.isSand((BlockGetter)level, pos);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\turtle\Turtle$TurtleLayEggGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */