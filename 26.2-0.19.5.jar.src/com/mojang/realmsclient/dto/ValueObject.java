/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.annotations.SerializedName;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Modifier;
/*    */ 
/*    */ public abstract class ValueObject {
/*    */   public String toString() {
/* 11 */     StringBuilder sb = new StringBuilder("{");
/* 12 */     for (Field f : getClass().getFields()) {
/* 13 */       if (!isStatic(f))
/*    */         try {
/* 15 */           sb.append(getName(f)).append("=").append(f.get(this)).append(" ");
/* 16 */         } catch (IllegalAccessException illegalAccessException) {} 
/*    */     } 
/* 20 */     sb.deleteCharAt(sb.length() - 1);
/* 21 */     sb.append('}');
/* 22 */     return sb.toString();
/*    */   }
/*    */   
/*    */   private static String getName(Field f) {
/* 26 */     SerializedName override = f.<SerializedName>getAnnotation(SerializedName.class);
/* 27 */     return (override != null) ? override.value() : f.getName();
/*    */   }
/*    */   
/*    */   private static boolean isStatic(Field f) {
/* 31 */     return Modifier.isStatic(f.getModifiers());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\ValueObject.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */