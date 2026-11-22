/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import net.minecraft.util.parsing.packrat.DelayedException;
/*     */ import net.minecraft.util.parsing.packrat.commands.NumberRunParseRule;
/*     */ 
/*     */ class null extends NumberRunParseRule {
/*     */   null(DelayedException<CommandSyntaxException> noValueError, DelayedException<CommandSyntaxException> underscoreNotAllowedError) {
/* 263 */     super(noValueError, underscoreNotAllowedError);
/*     */   }
/*     */   
/*     */   protected boolean isAccepted(char c) {
/* 266 */     switch (c) {
/*     */       case '0':
/*     */       case '1':
/*     */       case '2':
/*     */       case '3':
/*     */       case '4':
/*     */       case '5':
/*     */       case '6':
/*     */       case '7':
/*     */       case '8':
/*     */       case '9':
/*     */       case 'A':
/*     */       case 'B':
/*     */       case 'C':
/*     */       case 'D':
/*     */       case 'E':
/*     */       case 'F':
/*     */       case '_':
/*     */       case 'a':
/*     */       case 'b':
/*     */       case 'c':
/*     */       case 'd':
/*     */       case 'e':
/*     */       case 'f':
/*     */       
/*     */     } 
/* 266 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\SnbtGrammar$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */