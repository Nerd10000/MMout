/*    */ package net.minecraft.world.entity.monster.illager;
/*    */ 
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ 
/*    */ public class RaiderOpenDoorGoal extends OpenDoorGoal {
/*    */   public RaiderOpenDoorGoal(Raider raider) {
/* 50 */     super((Mob)raider, false);
/*    */   }
/*    */   
/*    */   public boolean canUse() {
/* 55 */     return (super.canUse() && AbstractIllager.this.hasActiveRaid());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\illager\AbstractIllager$RaiderOpenDoorGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */