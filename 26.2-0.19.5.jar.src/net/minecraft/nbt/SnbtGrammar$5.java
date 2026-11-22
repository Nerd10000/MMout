/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.chars.CharList;
/*     */ import net.minecraft.util.parsing.packrat.commands.StringReaderTerms;
/*     */ 
/*     */ class null extends StringReaderTerms.TerminalCharacters {
/*     */   null(CharList values) {
/* 313 */     super(values);
/*     */   }
/*     */   
/*     */   protected boolean isAccepted(char c) {
/* 316 */     return SnbtGrammar.canStartNumber(c);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\SnbtGrammar$5.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */