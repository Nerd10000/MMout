/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ 
/*     */ public final class Template implements ArgumentTypeInfo.Template<TimeArgument> {
/*     */   private final int min;
/*     */   
/*     */   private Template(int min) {
/*  92 */     this.min = min;
/*     */   }
/*     */   
/*     */   public TimeArgument instantiate(CommandBuildContext context) {
/*  97 */     return TimeArgument.time(this.min);
/*     */   }
/*     */   
/*     */   public ArgumentTypeInfo<TimeArgument, ?> type() {
/* 102 */     return TimeArgument.Info.this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\TimeArgument$Info$Template.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */