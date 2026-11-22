/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*    */ 
/*    */ public final class Template implements ArgumentTypeInfo.Template<IntegerArgumentType> {
/*    */   private final int min;
/*    */   
/*    */   private final int max;
/*    */   
/*    */   private Template(int min, int max) {
/* 19 */     this.min = min;
/* 20 */     this.max = max;
/*    */   }
/*    */   
/*    */   public IntegerArgumentType instantiate(CommandBuildContext context) {
/* 25 */     return IntegerArgumentType.integer(this.min, this.max);
/*    */   }
/*    */   
/*    */   public ArgumentTypeInfo<IntegerArgumentType, ?> type() {
/* 30 */     return IntegerArgumentInfo.this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\synchronization\brigadier\IntegerArgumentInfo$Template.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */