/*      */ package net.minecraft.world.entity.animal.bee;
/*      */ 
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.BonemealableBlock;
/*      */ import net.minecraft.world.level.block.CropBlock;
/*      */ import net.minecraft.world.level.block.StemBlock;
/*      */ import net.minecraft.world.level.block.SweetBerryBushBlock;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ 
/*      */ class BeeGrowCropGoal extends Bee.BaseBeeGoal {
/*      */   private static final int GROW_CHANCE = 30;
/*      */   
/*      */   public boolean canBeeUse() {
/* 1365 */     if (Bee.this.getCropsGrownSincePollination() >= 10)
/* 1366 */       return false; 
/* 1369 */     if (Bee.access$2700(Bee.this).nextFloat() < 0.3F)
/* 1370 */       return false; 
/* 1374 */     return (Bee.this.hasNectar() && Bee.this.isHiveValid());
/*      */   }
/*      */   
/*      */   public boolean canBeeContinueToUse() {
/* 1379 */     return canBeeUse();
/*      */   }
/*      */   
/*      */   public void tick() {
/* 1384 */     if (Bee.access$2800(Bee.this).nextInt(adjustedTickDelay(30)) != 0)
/*      */       return; 
/* 1388 */     for (int i = 1; i <= 2; i++) {
/* 1389 */       BlockPos belowPos = Bee.this.blockPosition().below(i);
/* 1390 */       BlockState belowState = Bee.this.level().getBlockState(belowPos);
/* 1391 */       Block belowBlock = belowState.getBlock();
/* 1392 */       BlockState growState = null;
/* 1393 */       if (belowState.is(BlockTags.BEE_GROWABLES)) {
/* 1394 */         if (belowBlock instanceof CropBlock) {
/* 1394 */           CropBlock cropBlockBelow = (CropBlock)belowBlock;
/* 1395 */           if (!cropBlockBelow.isMaxAge(belowState))
/* 1396 */             growState = cropBlockBelow.getStateForAge(cropBlockBelow.getAge(belowState) + 1); 
/* 1398 */         } else if (belowBlock instanceof StemBlock) {
/* 1399 */           int age = ((Integer)belowState.getValue((Property)StemBlock.AGE)).intValue();
/* 1400 */           if (age < 7)
/* 1401 */             growState = (BlockState)belowState.setValue((Property)StemBlock.AGE, Integer.valueOf(age + 1)); 
/* 1403 */         } else if (belowState.is(Blocks.SWEET_BERRY_BUSH)) {
/* 1404 */           int age = ((Integer)belowState.getValue((Property)SweetBerryBushBlock.AGE)).intValue();
/* 1405 */           if (age < 3)
/* 1406 */             growState = (BlockState)belowState.setValue((Property)SweetBerryBushBlock.AGE, Integer.valueOf(age + 1)); 
/* 1408 */         } else if (belowState.is(Blocks.CAVE_VINES) || belowState.is(Blocks.CAVE_VINES_PLANT)) {
/* 1409 */           BonemealableBlock bonemealableBlock = (BonemealableBlock)belowState.getBlock();
/* 1410 */           if (bonemealableBlock.isValidBonemealTarget((LevelReader)Bee.this.level(), belowPos, belowState)) {
/* 1411 */             bonemealableBlock.performBonemeal((ServerLevel)Bee.this.level(), Bee.access$2900(Bee.this), belowPos, belowState);
/* 1412 */             growState = Bee.this.level().getBlockState(belowPos);
/*      */           } 
/*      */         } 
/* 1416 */         if (growState != null) {
/* 1417 */           Bee.this.level().levelEvent(2011, belowPos, 15);
/* 1418 */           Bee.this.level().setBlockAndUpdate(belowPos, growState);
/* 1419 */           Bee.this.incrementNumCropsGrownSincePollination();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\bee\Bee$BeeGrowCropGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */