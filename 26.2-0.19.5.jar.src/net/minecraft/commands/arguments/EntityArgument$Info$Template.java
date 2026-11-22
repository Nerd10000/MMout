/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ 
/*     */ public final class Template implements ArgumentTypeInfo.Template<EntityArgument> {
/*     */   private final boolean single;
/*     */   
/*     */   private final boolean playersOnly;
/*     */   
/*     */   private Template(boolean single, boolean playersOnly) {
/* 160 */     this.single = single;
/* 161 */     this.playersOnly = playersOnly;
/*     */   }
/*     */   
/*     */   public EntityArgument instantiate(CommandBuildContext context) {
/* 166 */     return new EntityArgument(this.single, this.playersOnly);
/*     */   }
/*     */   
/*     */   public ArgumentTypeInfo<EntityArgument, ?> type() {
/* 171 */     return EntityArgument.Info.this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\EntityArgument$Info$Template.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */