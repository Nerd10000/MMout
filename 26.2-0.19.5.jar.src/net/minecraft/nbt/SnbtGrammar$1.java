/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import net.minecraft.util.parsing.packrat.DelayedException;
/*     */ import net.minecraft.util.parsing.packrat.commands.NumberRunParseRule;
/*     */ 
/*     */ class null extends NumberRunParseRule {
/*     */   null(DelayedException<CommandSyntaxException> noValueError, DelayedException<CommandSyntaxException> underscoreNotAllowedError) {
/* 243 */     super(noValueError, underscoreNotAllowedError);
/*     */   }
/*     */   
/*     */   protected boolean isAccepted(char c) {
/* 246 */     switch (c) {
/*     */       case '0':
/*     */       case '1':
/*     */       case '_':
/*     */       
/*     */     } 
/* 246 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\SnbtGrammar$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */