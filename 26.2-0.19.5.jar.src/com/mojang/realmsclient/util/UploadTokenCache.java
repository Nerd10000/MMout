/*    */ package com.mojang.realmsclient.util;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ 
/*    */ public class UploadTokenCache {
/*  8 */   private static final Long2ObjectMap<String> TOKEN_CACHE = (Long2ObjectMap<String>)new Long2ObjectOpenHashMap();
/*    */   
/*    */   public static String get(long realmId) {
/* 11 */     return (String)TOKEN_CACHE.get(realmId);
/*    */   }
/*    */   
/*    */   public static void invalidate(long realmId) {
/* 15 */     TOKEN_CACHE.remove(realmId);
/*    */   }
/*    */   
/*    */   public static void put(long realmId, String token) {
/* 19 */     TOKEN_CACHE.put(realmId, token);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclien\\util\UploadTokenCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */