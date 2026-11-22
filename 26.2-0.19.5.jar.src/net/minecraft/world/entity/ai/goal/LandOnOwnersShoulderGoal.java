/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.parrot.ShoulderRidingEntity;
/*    */ 
/*    */ public class LandOnOwnersShoulderGoal extends Goal {
/*    */   private final ShoulderRidingEntity entity;
/*    */   
/*    */   private boolean isSittingOnShoulder;
/*    */   
/*    */   public LandOnOwnersShoulderGoal(ShoulderRidingEntity entity) {
/* 12 */     this.entity = entity;
/*    */   }
/*    */   
/*    */   public boolean canUse() {
/* 17 */     LivingEntity livingEntity = this.entity.getOwner();
/* 17 */     if (livingEntity instanceof ServerPlayer) {
/* 17 */       ServerPlayer owner = (ServerPlayer)livingEntity;
/* 18 */       boolean ownerThatCanBeSatOn = (!owner.isSpectator() && !(owner.getAbilities()).flying && !owner.isInWater() && !owner.isInPowderSnow);
/* 19 */       return (!this.entity.isOrderedToSit() && ownerThatCanBeSatOn && this.entity.canSitOnShoulder());
/*    */     } 
/* 21 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isInterruptable() {
/* 26 */     return !this.isSittingOnShoulder;
/*    */   }
/*    */   
/*    */   public void start() {
/* 31 */     this.isSittingOnShoulder = false;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 36 */     if (this.isSittingOnShoulder || this.entity.isInSittingPose() || this.entity.isLeashed())
/*    */       return; 
/* 40 */     LivingEntity livingEntity = this.entity.getOwner();
/* 40 */     if (livingEntity instanceof ServerPlayer) {
/* 40 */       ServerPlayer owner = (ServerPlayer)livingEntity;
/* 41 */       if (this.entity.getBoundingBox().intersects(owner.getBoundingBox()))
/* 42 */         this.isSittingOnShoulder = this.entity.setEntityOnShoulder(owner); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\LandOnOwnersShoulderGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */