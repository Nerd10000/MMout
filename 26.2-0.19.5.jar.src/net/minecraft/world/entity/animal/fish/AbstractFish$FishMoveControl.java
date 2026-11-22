/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ 
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ 
/*     */ class FishMoveControl<T extends AbstractFish> extends MoveControl<T> {
/*     */   public FishMoveControl(T fish) {
/* 178 */     super((Mob)fish);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 183 */     if (((AbstractFish)this.mob).isEyeInFluid(FluidTags.WATER))
/* 185 */       ((AbstractFish)this.mob).setDeltaMovement(((AbstractFish)this.mob).getDeltaMovement().add(0.0D, 0.005D, 0.0D)); 
/* 188 */     if (this.operation != MoveControl.Operation.MOVE_TO || ((AbstractFish)this.mob).getNavigation().isDone()) {
/* 189 */       ((AbstractFish)this.mob).setSpeed(0.0F);
/*     */       return;
/*     */     } 
/* 193 */     float targetSpeed = (float)(this.speedModifier * ((AbstractFish)this.mob).getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 194 */     ((AbstractFish)this.mob).setSpeed(Mth.lerp(0.125F, ((AbstractFish)this.mob).getSpeed(), targetSpeed));
/* 196 */     double xd = this.wantedX - ((AbstractFish)this.mob).getX();
/* 197 */     double yd = this.wantedY - ((AbstractFish)this.mob).getY();
/* 198 */     double zd = this.wantedZ - ((AbstractFish)this.mob).getZ();
/* 200 */     if (yd != 0.0D) {
/* 201 */       double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
/* 203 */       ((AbstractFish)this.mob).setDeltaMovement(((AbstractFish)this.mob).getDeltaMovement().add(0.0D, ((AbstractFish)this.mob).getSpeed() * yd / dd * 0.1D, 0.0D));
/*     */     } 
/* 206 */     if (xd != 0.0D || zd != 0.0D) {
/* 207 */       float yRotD = (float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F;
/* 209 */       ((AbstractFish)this.mob).setYRot(rotlerp(((AbstractFish)this.mob).getYRot(), yRotD, 90.0F));
/* 210 */       ((AbstractFish)this.mob).yBodyRot = ((AbstractFish)this.mob).getYRot();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fish\AbstractFish$FishMoveControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */