/*    */ package net.minecraft.world.entity.monster.illager;
/*    */ 
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class AbstractIllager extends Raider {
/*    */   public enum IllagerArmPose {
/* 14 */     CROSSED, ATTACKING, SPELLCASTING, BOW_AND_ARROW, CROSSBOW_HOLD, CROSSBOW_CHARGE, CELEBRATING, NEUTRAL;
/*    */   }
/*    */   
/*    */   protected AbstractIllager(EntityType<? extends AbstractIllager> type, Level level) {
/* 26 */     super(type, level);
/*    */   }
/*    */   
/*    */   protected void registerGoals() {
/* 31 */     super.registerGoals();
/*    */   }
/*    */   
/*    */   public IllagerArmPose getArmPose() {
/* 35 */     return IllagerArmPose.CROSSED;
/*    */   }
/*    */   
/*    */   public boolean canAttack(LivingEntity target) {
/* 42 */     if (target instanceof net.minecraft.world.entity.npc.villager.AbstractVillager && target.isBaby())
/* 43 */       return false; 
/* 45 */     return super.canAttack(target);
/*    */   }
/*    */   
/*    */   protected class RaiderOpenDoorGoal extends OpenDoorGoal {
/*    */     public RaiderOpenDoorGoal(Raider raider) {
/* 50 */       super((Mob)raider, false);
/*    */     }
/*    */     
/*    */     public boolean canUse() {
/* 55 */       return (super.canUse() && AbstractIllager.this.hasActiveRaid());
/*    */     }
/*    */   }
/*    */   
/*    */   protected boolean considersEntityAsAlly(Entity other) {
/* 61 */     if (super.considersEntityAsAlly(other))
/* 62 */       return true; 
/* 64 */     if (other.is(EntityTypeTags.ILLAGER_FRIENDS))
/* 66 */       return (getTeam() == null && other.getTeam() == null); 
/* 68 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\illager\AbstractIllager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */