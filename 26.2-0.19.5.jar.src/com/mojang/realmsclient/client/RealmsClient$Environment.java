/*    */ package com.mojang.realmsclient.client;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public enum Environment {
/* 61 */   PRODUCTION("pc.realms.minecraft.net", "java.frontendlegacy.realms.minecraft-services.net", "https"),
/* 62 */   STAGE("pc-stage.realms.minecraft.net", "java.frontendlegacy.stage-c2a40e62.realms.minecraft-services.net", "https"),
/* 63 */   LOCAL("localhost:8080", "localhost:8080", "http");
/*    */   
/*    */   public final String baseUrl;
/*    */   
/*    */   public final String alternativeUrl;
/*    */   
/*    */   public final String protocol;
/*    */   
/*    */   Environment(String baseUrl, String alternativeUrl, String protocol) {
/* 70 */     this.baseUrl = baseUrl;
/* 71 */     this.alternativeUrl = alternativeUrl;
/* 72 */     this.protocol = protocol;
/*    */   }
/*    */   
/*    */   public static Optional<Environment> byName(String name) {
/* 76 */     switch (name.toLowerCase(Locale.ROOT)) {
/*    */       case "production":
/*    */       
/*    */       case "local":
/*    */       
/*    */       case "stage":
/*    */       case "staging":
/*    */       
/*    */     } 
/* 76 */     return 
/*    */       
/* 80 */       Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\RealmsClient$Environment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */