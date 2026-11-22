/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class OpenDoorGoal extends DoorInteractGoal {
/*    */   private final boolean closeDoor;
/*    */   
/*    */   private int forgetTime;
/*    */   
/*    */   public OpenDoorGoal(Mob mob, boolean closeDoorAfter) {
/* 10 */     super(mob);
/* 11 */     this.mob = mob;
/* 12 */     this.closeDoor = closeDoorAfter;
/*    */   }
/*    */   
/*    */   public boolean canContinueToUse() {
/* 17 */     return (this.closeDoor && this.forgetTime > 0 && super.canContinueToUse());
/*    */   }
/*    */   
/*    */   public void start() {
/* 22 */     this.forgetTime = 20;
/* 23 */     setOpen(true);
/*    */   }
/*    */   
/*    */   public void stop() {
/* 28 */     setOpen(false);
/*    */   }
/*    */   
/*    */   public void tick() {
/* 33 */     this.forgetTime--;
/* 34 */     super.tick();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\OpenDoorGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */