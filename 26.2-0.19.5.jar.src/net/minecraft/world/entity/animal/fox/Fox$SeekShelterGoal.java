/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.ai.goal.FleeSunGoal;
/*      */ 
/*      */ class SeekShelterGoal extends FleeSunGoal {
/*      */   private int interval;
/*      */   
/*      */   public SeekShelterGoal(double speedModifier) {
/* 1037 */     super((PathfinderMob)paramFox, speedModifier);
/* 1038 */     this.interval = reducedTickDelay(100);
/*      */   }
/*      */   
/*      */   public boolean canUse() {
/* 1043 */     if (Fox.this.isSleeping() || this.mob.getTarget() != null)
/* 1044 */       return false; 
/* 1046 */     if (Fox.this.level().isThundering() && Fox.this.level().canSeeSky(this.mob.blockPosition()))
/* 1047 */       return setWantedPos(); 
/* 1049 */     if (this.interval > 0) {
/* 1050 */       this.interval--;
/* 1051 */       return false;
/*      */     } 
/* 1053 */     this.interval = 100;
/* 1055 */     BlockPos pos = this.mob.blockPosition();
/* 1057 */     return (Fox.this.level().isBrightOutside() && Fox.this
/* 1058 */       .level().canSeeSky(pos) && 
/* 1059 */       !((ServerLevel)Fox.this.level()).isVillage(pos) && 
/* 1060 */       setWantedPos());
/*      */   }
/*      */   
/*      */   public void start() {
/* 1065 */     Fox.this.clearStates();
/* 1066 */     super.start();
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$SeekShelterGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */