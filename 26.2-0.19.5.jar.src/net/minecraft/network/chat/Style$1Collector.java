/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ class Collector {
/*     */   private boolean isNotFirst;
/*     */   
/*     */   Collector(Style this$0) {}
/*     */   
/*     */   private void prependSeparator() {
/* 366 */     if (this.isNotFirst)
/* 367 */       result.append(','); 
/* 369 */     this.isNotFirst = true;
/*     */   }
/*     */   
/*     */   private void addFlagString(String name, Boolean value) {
/* 373 */     if (value != null) {
/* 374 */       prependSeparator();
/* 375 */       if (!value.booleanValue())
/* 376 */         result.append('!'); 
/* 378 */       result.append(name);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addValueString(String name, Object value) {
/* 383 */     if (value != null) {
/* 384 */       prependSeparator();
/* 385 */       result.append(name);
/* 386 */       result.append('=');
/* 387 */       result.append(value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\Style$1Collector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */