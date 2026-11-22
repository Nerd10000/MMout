/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ public enum Token {
/* 301 */   FOLDED("<...>"),
/* 302 */   ELEMENT_SEPARATOR(","),
/* 303 */   LIST_CLOSE("]"),
/* 304 */   LIST_OPEN("["),
/* 305 */   LIST_TYPE_SEPARATOR(";"),
/* 306 */   STRUCT_CLOSE("}"),
/* 307 */   STRUCT_OPEN("{"),
/* 308 */   NAME_VALUE_SEPARATOR(":"),
/* 309 */   BYTE_SUFFIX("b"),
/* 310 */   BYTE_ARRAY_PREFIX("B"),
/* 311 */   SHORT_SUFFIX("s"),
/* 312 */   INT_ARRAY_PREFIX("I"),
/* 313 */   LONG_SUFFIX("L"),
/* 314 */   LONG_ARRAY_PREFIX("L"),
/* 315 */   FLOAT_SUFFIX("f"),
/* 316 */   DOUBLE_SUFFIX("d");
/*     */   
/*     */   public final String text;
/*     */   
/*     */   Token(String text) {
/* 321 */     this.text = text;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\TextComponentTagVisitor$Token.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */