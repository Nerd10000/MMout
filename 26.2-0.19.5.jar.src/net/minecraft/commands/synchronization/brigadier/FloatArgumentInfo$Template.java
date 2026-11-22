/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*    */ 
/*    */ public final class Template implements ArgumentTypeInfo.Template<FloatArgumentType> {
/*    */   private final float min;
/*    */   
/*    */   private final float max;
/*    */   
/*    */   private Template(float min, float max) {
/* 19 */     this.min = min;
/* 20 */     this.max = max;
/*    */   }
/*    */   
/*    */   public FloatArgumentType instantiate(CommandBuildContext context) {
/* 25 */     return FloatArgumentType.floatArg(this.min, this.max);
/*    */   }
/*    */   
/*    */   public ArgumentTypeInfo<FloatArgumentType, ?> type() {
/* 30 */     return FloatArgumentInfo.this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\synchronization\brigadier\FloatArgumentInfo$Template.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */