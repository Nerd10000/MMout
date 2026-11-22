/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.security.SecureRandom;
/*     */ 
/*     */ public class SaltSupplier {
/* 208 */   private static final SecureRandom secureRandom = new SecureRandom();
/*     */   
/*     */   public static long getLong() {
/* 211 */     return secureRandom.nextLong();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Crypt$SaltSupplier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */