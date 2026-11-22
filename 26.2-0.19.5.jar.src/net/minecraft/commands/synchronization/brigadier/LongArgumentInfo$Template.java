/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.LongArgumentType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*    */ 
/*    */ public final class Template implements ArgumentTypeInfo.Template<LongArgumentType> {
/*    */   private final long min;
/*    */   
/*    */   private final long max;
/*    */   
/*    */   private Template(long min, long max) {
/* 19 */     this.min = min;
/* 20 */     this.max = max;
/*    */   }
/*    */   
/*    */   public LongArgumentType instantiate(CommandBuildContext context) {
/* 25 */     return LongArgumentType.longArg(this.min, this.max);
/*    */   }
/*    */   
/*    */   public ArgumentTypeInfo<LongArgumentType, ?> type() {
/* 30 */     return LongArgumentInfo.this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\synchronization\brigadier\LongArgumentInfo$Template.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */