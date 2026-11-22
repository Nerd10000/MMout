/*     */ package net.minecraft.world.entity.animal.happyghast;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.monster.Ghast;
/*     */ 
/*     */ class HappyGhastLookControl extends LookControl {
/*     */   private HappyGhastLookControl() {
/* 613 */     super((Mob)paramHappyGhast);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 618 */     if (HappyGhast.this.isOnStillTimeout()) {
/* 619 */       float closeAngle = Mth.wrapDegrees90(HappyGhast.this.getYRot());
/* 620 */       HappyGhast.this.setYRot(HappyGhast.this.getYRot() - closeAngle);
/* 621 */       HappyGhast.this.setYHeadRot(HappyGhast.this.getYRot());
/*     */       return;
/*     */     } 
/* 624 */     if (this.lookAtCooldown > 0) {
/* 625 */       this.lookAtCooldown--;
/* 626 */       double xdd = this.wantedX - HappyGhast.this.getX();
/* 627 */       double zdd = this.wantedZ - HappyGhast.this.getZ();
/* 628 */       HappyGhast.this.setYRot(-((float)Mth.atan2(xdd, zdd)) * 57.295776F);
/* 629 */       HappyGhast.this.yBodyRot = HappyGhast.this.getYRot();
/* 630 */       HappyGhast.this.yHeadRot = HappyGhast.this.yBodyRot;
/*     */       return;
/*     */     } 
/* 633 */     Ghast.faceMovementDirection(this.mob);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\happyghast\HappyGhast$HappyGhastLookControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */