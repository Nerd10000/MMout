/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import net.minecraft.util.parsing.packrat.DelayedException;
/*     */ import net.minecraft.util.parsing.packrat.commands.GreedyPredicateParseRule;
/*     */ 
/*     */ class null extends GreedyPredicateParseRule {
/*     */   null(int minSize, DelayedException<CommandSyntaxException> error) {
/* 292 */     super(minSize, error);
/*     */   }
/*     */   
/*     */   protected boolean isAccepted(char c) {
/* 295 */     switch (c) {
/*     */       case '"':
/*     */       case '\'':
/*     */       case '\\':
/*     */       
/*     */     } 
/* 295 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\SnbtGrammar$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */