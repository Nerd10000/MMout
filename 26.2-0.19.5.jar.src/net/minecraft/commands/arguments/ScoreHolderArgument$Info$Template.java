/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ 
/*     */ public final class Template implements ArgumentTypeInfo.Template<ScoreHolderArgument> {
/*     */   private final boolean multiple;
/*     */   
/*     */   private Template(boolean multiple) {
/* 209 */     this.multiple = multiple;
/*     */   }
/*     */   
/*     */   public ScoreHolderArgument instantiate(CommandBuildContext context) {
/* 214 */     return new ScoreHolderArgument(this.multiple);
/*     */   }
/*     */   
/*     */   public ArgumentTypeInfo<ScoreHolderArgument, ?> type() {
/* 219 */     return ScoreHolderArgument.Info.this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ScoreHolderArgument$Info$Template.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */