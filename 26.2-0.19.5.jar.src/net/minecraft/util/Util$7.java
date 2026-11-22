/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.function.BooleanSupplier;
/*     */ 
/*     */ class null implements BooleanSupplier {
/*     */   public boolean getAsBoolean() {
/* 883 */     return !Files.exists(target, new java.nio.file.LinkOption[0]);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 888 */     return "verify that " + String.valueOf(target) + " is deleted";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Util$7.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */