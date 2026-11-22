/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.NeutralMob;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.entity.ai.goal.Goal;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class ResetUniversalAngerTargetGoal<T extends Mob & NeutralMob> extends Goal {
/*    */   private static final int ALERT_RANGE_Y = 10;
/*    */   
/*    */   private final T mob;
/*    */   
/*    */   private final boolean alertOthersOfSameType;
/*    */   
/*    */   private int lastHurtByPlayerTimestamp;
/*    */   
/*    */   public ResetUniversalAngerTargetGoal(T mob, boolean alertOthersOfSameType) {
/* 28 */     this.mob = mob;
/* 29 */     this.alertOthersOfSameType = alertOthersOfSameType;
/*    */   }
/*    */   
/*    */   public boolean canUse() {
/* 34 */     return (((Boolean)getServerLevel((Entity)this.mob).getGameRules().get(GameRules.UNIVERSAL_ANGER)).booleanValue() && wasHurtByPlayer());
/*    */   }
/*    */   
/*    */   private boolean wasHurtByPlayer() {
/* 38 */     return (this.mob.getLastHurtByMob() != null && this.mob
/* 39 */       .getLastHurtByMob().is(EntityTypes.PLAYER) && this.mob
/* 40 */       .getLastHurtByMobTimestamp() > this.lastHurtByPlayerTimestamp);
/*    */   }
/*    */   
/*    */   public void start() {
/* 45 */     this.lastHurtByPlayerTimestamp = this.mob.getLastHurtByMobTimestamp();
/* 46 */     ((NeutralMob)this.mob).forgetCurrentTargetAndRefreshUniversalAnger();
/* 47 */     if (this.alertOthersOfSameType)
/* 48 */       getNearbyMobsOfSameType().stream()
/* 49 */         .filter(otherMob -> (otherMob != this.mob))
/* 50 */         .map(otherMob -> (NeutralMob)otherMob)
/* 51 */         .forEach(NeutralMob::forgetCurrentTargetAndRefreshUniversalAnger); 
/* 53 */     super.start();
/*    */   }
/*    */   
/*    */   private List<? extends Mob> getNearbyMobsOfSameType() {
/* 57 */     double within = this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
/* 58 */     AABB searchAabb = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(within, 10.0D, within);
/* 59 */     return this.mob.level().getEntitiesOfClass(this.mob.getClass(), searchAabb, EntitySelector.NO_SPECTATORS);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\target\ResetUniversalAngerTargetGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */