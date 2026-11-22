/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.ai.goal.JumpGoal;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ public class FoxPounceGoal extends JumpGoal {
/*      */   public boolean canUse() {
/* 1403 */     if (!Fox.this.isFullyCrouched())
/* 1404 */       return false; 
/* 1407 */     LivingEntity target = Fox.this.getTarget();
/* 1409 */     if (target == null || !target.isAlive())
/* 1410 */       return false; 
/* 1413 */     if (target.getMotionDirection() != target.getDirection())
/* 1414 */       return false; 
/* 1417 */     boolean hasClearPath = Fox.isPathClear(Fox.this, target);
/* 1418 */     if (!hasClearPath) {
/* 1419 */       Fox.this.getNavigation().createPath((Entity)target, 0);
/* 1420 */       Fox.this.setIsCrouching(false);
/* 1421 */       Fox.this.setIsInterested(false);
/*      */     } 
/* 1424 */     return hasClearPath;
/*      */   }
/*      */   
/*      */   public boolean canContinueToUse() {
/* 1429 */     LivingEntity target = Fox.this.getTarget();
/* 1431 */     if (target == null || !target.isAlive())
/* 1432 */       return false; 
/* 1435 */     double yd = (Fox.this.getDeltaMovement()).y;
/* 1436 */     return ((yd * yd >= 0.05000000074505806D || Math.abs(Fox.this.getXRot()) >= 15.0F || !Fox.this.onGround()) && !Fox.this.isFaceplanted());
/*      */   }
/*      */   
/*      */   public boolean isInterruptable() {
/* 1441 */     return false;
/*      */   }
/*      */   
/*      */   public void start() {
/* 1446 */     Fox.this.setJumping(true);
/* 1447 */     Fox.this.setIsPouncing(true);
/* 1448 */     Fox.this.setIsInterested(false);
/* 1450 */     LivingEntity target = Fox.this.getTarget();
/* 1451 */     if (target != null) {
/* 1452 */       Fox.this.getLookControl().setLookAt((Entity)target, 60.0F, 30.0F);
/* 1454 */       Vec3 uv = (new Vec3(target.getX() - Fox.this.getX(), target.getY() - Fox.this.getY(), target.getZ() - Fox.this.getZ())).normalize();
/* 1455 */       Fox.this.setDeltaMovement(Fox.this.getDeltaMovement().add(uv.x * 0.8D, 0.9D, uv.z * 0.8D));
/*      */     } 
/* 1462 */     Fox.this.getNavigation().stop();
/*      */   }
/*      */   
/*      */   public void stop() {
/* 1467 */     Fox.this.setIsCrouching(false);
/* 1468 */     Fox.this.crouchAmount = 0.0F;
/* 1469 */     Fox.this.crouchAmountO = 0.0F;
/* 1470 */     Fox.this.setIsInterested(false);
/* 1471 */     Fox.this.setIsPouncing(false);
/*      */   }
/*      */   
/*      */   public void tick() {
/* 1476 */     LivingEntity target = Fox.this.getTarget();
/* 1478 */     if (target != null)
/* 1479 */       Fox.this.getLookControl().setLookAt((Entity)target, 60.0F, 30.0F); 
/* 1482 */     if (!Fox.this.isFaceplanted()) {
/* 1483 */       Vec3 movement = Fox.this.getDeltaMovement();
/* 1484 */       if (movement.y * movement.y < 0.029999999329447746D && Fox.this.getXRot() != 0.0F) {
/* 1485 */         Fox.this.setXRot(Mth.rotLerp(0.2F, Fox.this.getXRot(), 0.0F));
/*      */       } else {
/* 1487 */         double direction = movement.horizontalDistance();
/* 1488 */         float upwardsBias = (Fox.access$400(Fox.this) && movement.y > 0.0D) ? 6.5F : 1.0F;
/* 1489 */         double biasedY = movement.y * upwardsBias;
/* 1490 */         double len = Math.sqrt(direction * direction + biasedY * biasedY);
/* 1491 */         if (len > 9.999999747378752E-6D) {
/* 1492 */           double rotation = Math.signum(-biasedY) * Math.acos(direction / len) * 57.2957763671875D;
/* 1493 */           Fox.this.setXRot((float)rotation);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1499 */     if (target != null && Fox.this.distanceTo((Entity)target) <= 2.0F) {
/* 1500 */       Fox.this.doHurtTarget(getServerLevel(Fox.this.level()), (Entity)target);
/* 1502 */     } else if (Fox.this.getXRot() > 0.0F && Fox.this.onGround() && (float)(Fox.this.getDeltaMovement()).y != 0.0F && 
/* 1503 */       Fox.this.level().getBlockState(Fox.this.blockPosition()).is(Blocks.SNOW)) {
/* 1504 */       Fox.this.setXRot(60.0F);
/* 1505 */       Fox.this.setTarget((LivingEntity)null);
/* 1506 */       Fox.this.setFaceplanted(true);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$FoxPounceGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */