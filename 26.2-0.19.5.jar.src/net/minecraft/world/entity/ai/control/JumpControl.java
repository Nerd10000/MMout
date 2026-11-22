/*    */ package net.minecraft.world.entity.ai.control;
/*    */ 
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class JumpControl implements Control {
/*    */   private final Mob mob;
/*    */   
/*    */   protected boolean jump;
/*    */   
/*    */   public JumpControl(Mob mob) {
/* 10 */     this.mob = mob;
/*    */   }
/*    */   
/*    */   public void jump() {
/* 14 */     this.jump = true;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 18 */     this.mob.setJumping(this.jump);
/* 19 */     this.jump = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\control\JumpControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */