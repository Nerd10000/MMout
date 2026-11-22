/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.ExclusionStrategy;
/*    */ import com.google.gson.FieldAttributes;
/*    */ 
/*    */ class null implements ExclusionStrategy {
/*    */   public boolean shouldSkipClass(Class<?> clazz) {
/* 14 */     return false;
/*    */   }
/*    */   
/*    */   public boolean shouldSkipField(FieldAttributes field) {
/* 19 */     return (field.getAnnotation(Exclude.class) != null);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\GuardedSerializer$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */