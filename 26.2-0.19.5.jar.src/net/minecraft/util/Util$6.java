/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.function.BooleanSupplier;
/*     */ 
/*     */ class null implements BooleanSupplier {
/*     */   public boolean getAsBoolean() {
/*     */     try {
/* 864 */       Files.deleteIfExists(target);
/* 865 */       return true;
/* 866 */     } catch (IOException e) {
/* 867 */       Util.LOGGER.warn("Failed to delete", e);
/* 868 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 874 */     return "delete old " + String.valueOf(target);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Util$6.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */