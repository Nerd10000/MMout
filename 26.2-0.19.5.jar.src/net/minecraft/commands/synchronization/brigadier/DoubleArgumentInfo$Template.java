/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*    */ 
/*    */ public final class Template implements ArgumentTypeInfo.Template<DoubleArgumentType> {
/*    */   private final double min;
/*    */   
/*    */   private final double max;
/*    */   
/*    */   private Template(double min, double max) {
/* 19 */     this.min = min;
/* 20 */     this.max = max;
/*    */   }
/*    */   
/*    */   public DoubleArgumentType instantiate(CommandBuildContext context) {
/* 25 */     return DoubleArgumentType.doubleArg(this.min, this.max);
/*    */   }
/*    */   
/*    */   public ArgumentTypeInfo<DoubleArgumentType, ?> type() {
/* 30 */     return DoubleArgumentInfo.this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\synchronization\brigadier\DoubleArgumentInfo$Template.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */