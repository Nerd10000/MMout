/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public final class SingleAngle {
/*    */   private final float angle;
/*    */   
/*    */   private final boolean isRelative;
/*    */   
/*    */   private SingleAngle(float angle, boolean isRelative) {
/* 53 */     this.angle = angle;
/* 54 */     this.isRelative = isRelative;
/*    */   }
/*    */   
/*    */   public float getAngle(CommandSourceStack sender) {
/* 58 */     return Mth.wrapDegrees(this.isRelative ? (this.angle + (sender.getRotation()).y) : this.angle);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\AngleArgument$SingleAngle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */