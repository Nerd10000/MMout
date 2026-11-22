/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.function.BooleanSupplier;
/*     */ 
/*     */ class null implements BooleanSupplier {
/*     */   public boolean getAsBoolean() {
/*     */     try {
/* 844 */       Files.move(from, to, options);
/* 845 */       return true;
/* 846 */     } catch (IOException e) {
/* 847 */       Util.LOGGER.error("Failed to rename", e);
/* 848 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 854 */     return "rename " + String.valueOf(from) + " to " + String.valueOf(to);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Util$5.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */