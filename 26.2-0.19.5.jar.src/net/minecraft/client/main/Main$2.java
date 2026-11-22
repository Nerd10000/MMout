/*     */ package net.minecraft.client.main;
/*     */ 
/*     */ import java.net.Authenticator;
/*     */ import java.net.PasswordAuthentication;
/*     */ 
/*     */ class null extends Authenticator {
/*     */   protected PasswordAuthentication getPasswordAuthentication() {
/* 207 */     return new PasswordAuthentication(proxyUser, proxyPass.toCharArray());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\main\Main$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */