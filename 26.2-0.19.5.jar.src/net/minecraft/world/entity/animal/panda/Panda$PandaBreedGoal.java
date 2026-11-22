/*      */ package net.minecraft.world.entity.animal.panda;
/*      */ 
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ 
/*      */ class PandaBreedGoal extends BreedGoal {
/*      */   private final Panda panda;
/*      */   
/*      */   private int unhappyCooldown;
/*      */   
/*      */   public PandaBreedGoal(Panda panda, double speedModifier) {
/*  971 */     super(panda, speedModifier);
/*  972 */     this.panda = panda;
/*      */   }
/*      */   
/*      */   public boolean canUse() {
/*  977 */     if (super.canUse() && this.panda.getUnhappyCounter() == 0) {
/*  978 */       if (!canFindBamboo()) {
/*  979 */         if (this.unhappyCooldown <= this.panda.tickCount) {
/*  980 */           this.panda.setUnhappyCounter(32);
/*  981 */           this.unhappyCooldown = this.panda.tickCount + 600;
/*  982 */           if (this.panda.isEffectiveAi()) {
/*  983 */             Player player = this.level.getNearestPlayer(Panda.BREED_TARGETING, (LivingEntity)this.panda);
/*  984 */             this.panda.lookAtPlayerGoal.setTarget((LivingEntity)player);
/*      */           } 
/*      */         } 
/*  988 */         return false;
/*      */       } 
/*  991 */       return true;
/*      */     } 
/*  994 */     return false;
/*      */   }
/*      */   
/*      */   private boolean canFindBamboo() {
/*  998 */     BlockPos pandaPos = this.panda.blockPosition();
/*  999 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 1000 */     for (int yOff = 0; yOff < 3; yOff++) {
/* 1001 */       for (int r = 0; r < 8; r++) {
/*      */         int x;
/* 1002 */         for (x = 0; x <= r; x = (x > 0) ? -x : (1 - x)) {
/* 1003 */           int z = (x < r && x > -r) ? r : 0;
/* 1004 */           for (; z <= r; z = (z > 0) ? -z : (1 - z)) {
/* 1005 */             pos.setWithOffset((Vec3i)pandaPos, x, yOff, z);
/* 1006 */             if (this.level.getBlockState((BlockPos)pos).is(Blocks.BAMBOO))
/* 1007 */               return true; 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1013 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\panda\Panda$PandaBreedGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */