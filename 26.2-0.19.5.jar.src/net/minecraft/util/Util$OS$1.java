/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.net.URI;
/*     */ 
/*     */ enum null {
/*     */   protected String[] getOpenUriArguments(URI uri) {
/* 504 */     return new String[] { "rundll32", "url.dll,FileProtocolHandler", uri.toString() };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Util$OS$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */