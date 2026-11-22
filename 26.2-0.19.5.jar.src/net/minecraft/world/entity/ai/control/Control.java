/*   */ package net.minecraft.world.entity.ai.control;
/*   */ 
/*   */ import net.minecraft.util.Mth;
/*   */ 
/*   */ public interface Control {
/*   */   default float rotateTowards(float fromAngle, float toAngle, float maxRot) {
/* 7 */     float diff = Mth.degreesDifference(fromAngle, toAngle);
/* 8 */     float diffClamped = Mth.clamp(diff, -maxRot, maxRot);
/* 9 */     return fromAngle + diffClamped;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\control\Control.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */