/*   */ package net.minecraft.world.entity.ai.goal;
/*   */ 
/*   */ import java.util.EnumSet;
/*   */ 
/*   */ public abstract class JumpGoal extends Goal {
/*   */   public JumpGoal() {
/* 7 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\JumpGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */