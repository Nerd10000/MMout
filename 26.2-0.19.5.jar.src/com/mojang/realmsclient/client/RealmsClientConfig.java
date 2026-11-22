/*    */ package com.mojang.realmsclient.client;
/*    */ 
/*    */ import java.net.Proxy;
/*    */ 
/*    */ public class RealmsClientConfig {
/*    */   private static Proxy proxy;
/*    */   
/*    */   public static Proxy getProxy() {
/* 11 */     return proxy;
/*    */   }
/*    */   
/*    */   public static void setProxy(Proxy proxy) {
/* 15 */     if (RealmsClientConfig.proxy == null)
/* 16 */       RealmsClientConfig.proxy = proxy; 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\RealmsClientConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */