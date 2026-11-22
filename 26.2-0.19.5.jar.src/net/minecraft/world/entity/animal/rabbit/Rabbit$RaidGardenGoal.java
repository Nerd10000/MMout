/*     */ package net.minecraft.world.entity.animal.rabbit;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CarrotBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ class RaidGardenGoal extends MoveToBlockGoal {
/*     */   private final Rabbit rabbit;
/*     */   
/*     */   private boolean wantsToRaid;
/*     */   
/*     */   private boolean canRaid;
/*     */   
/*     */   public RaidGardenGoal(Rabbit rabbit) {
/* 664 */     super((PathfinderMob)rabbit, 0.699999988079071D, 16);
/* 665 */     this.rabbit = rabbit;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 670 */     if (this.nextStartTick <= 0) {
/* 671 */       if (!((Boolean)getServerLevel((Entity)this.rabbit).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue())
/* 672 */         return false; 
/* 676 */       this.canRaid = false;
/* 677 */       this.wantsToRaid = this.rabbit.wantsMoreFood();
/*     */     } 
/* 680 */     return super.canUse();
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 685 */     return (this.canRaid && super.canContinueToUse());
/*     */   }
/*     */   
/*     */   public void tick() {
/* 690 */     super.tick();
/* 692 */     this.rabbit.getLookControl().setLookAt(this.blockPos.getX() + 0.5D, (this.blockPos.getY() + 1), this.blockPos.getZ() + 0.5D, 10.0F, this.rabbit.getMaxHeadXRot());
/* 694 */     if (isReachedTarget()) {
/* 695 */       Level level = this.rabbit.level();
/* 696 */       BlockPos cropsPos = this.blockPos.above();
/* 698 */       BlockState blockState = level.getBlockState(cropsPos);
/* 699 */       Block block = blockState.getBlock();
/* 701 */       if (this.canRaid && block instanceof CarrotBlock) {
/* 702 */         int carrotAge = ((Integer)blockState.getValue((Property)CarrotBlock.AGE)).intValue();
/* 703 */         if (carrotAge == 0) {
/* 704 */           level.setBlock(cropsPos, Blocks.AIR.defaultBlockState(), 2);
/* 705 */           level.destroyBlock(cropsPos, true, (Entity)this.rabbit);
/*     */         } else {
/* 707 */           level.setBlock(cropsPos, (BlockState)blockState.setValue((Property)CarrotBlock.AGE, Integer.valueOf(carrotAge - 1)), 2);
/* 708 */           level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, cropsPos, GameEvent.Context.of((Entity)this.rabbit));
/* 709 */           level.levelEvent(2001, cropsPos, Block.getId(blockState));
/*     */         } 
/* 711 */         this.rabbit.moreCarrotTicks = 40;
/*     */       } 
/* 714 */       this.canRaid = false;
/* 717 */       this.nextStartTick = 10;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 723 */     BlockState state = level.getBlockState(pos);
/* 725 */     if (state.is(BlockTags.SUPPORTS_CROPS) && this.wantsToRaid && !this.canRaid) {
/* 726 */       state = level.getBlockState(pos.above());
/* 728 */       Block block = state.getBlock();
/* 728 */       if (block instanceof CarrotBlock) {
/* 728 */         CarrotBlock carrotBlock = (CarrotBlock)block;
/* 728 */         if (carrotBlock.isMaxAge(state)) {
/* 729 */           this.canRaid = true;
/* 730 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 733 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\rabbit\Rabbit$RaidGardenGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */