/*      */ package net.minecraft.world.entity.animal.bee;
/*      */ 
/*      */ import net.minecraft.util.Mth;
/*      */ 
/*      */ class ValidateHiveGoal extends Bee.BaseBeeGoal {
/*      */   private final int VALIDATE_HIVE_COOLDOWN;
/*      */   
/*      */   private long lastValidateTick;
/*      */   
/*      */   private ValidateHiveGoal() {
/* 1503 */     this.VALIDATE_HIVE_COOLDOWN = Mth.nextInt(Bee.access$3100(Bee.this), 20, 40);
/* 1505 */     this.lastValidateTick = -1L;
/*      */   }
/*      */   
/*      */   public void start() {
/* 1509 */     if (Bee.this.hivePos != null && Bee.this.level().isLoaded(Bee.this.hivePos) && !Bee.this.isHiveValid())
/* 1510 */       Bee.this.dropHive(); 
/* 1512 */     this.lastValidateTick = Bee.this.level().getGameTime();
/*      */   }
/*      */   
/*      */   public boolean canBeeUse() {
/* 1517 */     return (Bee.this.level().getGameTime() > this.lastValidateTick + this.VALIDATE_HIVE_COOLDOWN);
/*      */   }
/*      */   
/*      */   public boolean canBeeContinueToUse() {
/* 1522 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\bee\Bee$ValidateHiveGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */