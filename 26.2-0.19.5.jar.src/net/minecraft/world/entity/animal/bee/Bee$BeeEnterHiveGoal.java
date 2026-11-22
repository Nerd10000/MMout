/*      */ package net.minecraft.world.entity.animal.bee;
/*      */ 
/*      */ import net.minecraft.core.Position;
/*      */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*      */ 
/*      */ class BeeEnterHiveGoal extends Bee.BaseBeeGoal {
/*      */   public boolean canBeeUse() {
/* 1446 */     if (Bee.this.hivePos != null && Bee.this.wantsToEnterHive() && Bee.this.hivePos.closerToCenterThan((Position)Bee.this.position(), 2.0D)) {
/* 1447 */       BeehiveBlockEntity beehiveBlockEntity = Bee.this.getBeehiveBlockEntity();
/* 1448 */       if (beehiveBlockEntity != null)
/* 1449 */         if (beehiveBlockEntity.isFull()) {
/* 1450 */           Bee.this.hivePos = null;
/*      */         } else {
/* 1452 */           return true;
/*      */         }  
/*      */     } 
/* 1456 */     return false;
/*      */   }
/*      */   
/*      */   public boolean canBeeContinueToUse() {
/* 1461 */     return false;
/*      */   }
/*      */   
/*      */   public void start() {
/* 1466 */     BeehiveBlockEntity beehiveBlockEntity = Bee.this.getBeehiveBlockEntity();
/* 1467 */     if (beehiveBlockEntity != null)
/* 1468 */       beehiveBlockEntity.addOccupant(Bee.this); 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\bee\Bee$BeeEnterHiveGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */