/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ enum Sign {
/* 106 */   PLUS, MINUS;
/*     */   
/*     */   public void append(StringBuilder output) {
/* 111 */     if (this == MINUS)
/* 112 */       output.append("-"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\SnbtGrammar$Sign.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */