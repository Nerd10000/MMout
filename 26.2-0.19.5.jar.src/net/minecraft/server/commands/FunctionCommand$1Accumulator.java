/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ class Accumulator {
/*     */   private boolean anyResult;
/*     */   
/*     */   private int sum;
/*     */   
/*     */   public void add(int result) {
/* 250 */     this.anyResult = true;
/* 251 */     this.sum += result;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\FunctionCommand$1Accumulator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */