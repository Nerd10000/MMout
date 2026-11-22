/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ public class GlUtil {
/*    */   public static int selectBufferBindTarget(int usage) {
/* 10 */     if ((usage & 0x20) != 0)
/* 11 */       return 34962; 
/* 12 */     if ((usage & 0x40) != 0)
/* 13 */       return 34963; 
/* 14 */     if ((usage & 0x80) != 0)
/* 15 */       return 35345; 
/* 17 */     return 36663;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlUtil.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */