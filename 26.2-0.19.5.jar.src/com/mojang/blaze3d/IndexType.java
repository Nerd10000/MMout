/*    */ package com.mojang.blaze3d;
/*    */ 
/*    */ public enum IndexType {
/*  4 */   SHORT(2),
/*  5 */   INT(4);
/*    */   
/*    */   public final int bytes;
/*    */   
/*    */   IndexType(int bytes) {
/* 10 */     this.bytes = bytes;
/*    */   }
/*    */   
/*    */   public static IndexType least(int length) {
/* 14 */     if ((length & 0xFFFF0000) != 0)
/* 15 */       return INT; 
/* 17 */     return SHORT;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\IndexType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */