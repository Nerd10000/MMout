/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ public final class Transparency extends Record {
/*    */   private final boolean hasTransparent;
/*    */   
/*    */   private final boolean hasTranslucent;
/*    */   
/*    */   public Transparency(boolean hasTransparent, boolean hasTranslucent) {
/*  5 */     this.hasTransparent = hasTransparent;
/*  5 */     this.hasTranslucent = hasTranslucent;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/platform/Transparency;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/platform/Transparency;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/platform/Transparency;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/platform/Transparency;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/platform/Transparency;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/platform/Transparency;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean hasTransparent() {
/*  5 */     return this.hasTransparent;
/*    */   }
/*    */   
/*    */   public boolean hasTranslucent() {
/*  5 */     return this.hasTranslucent;
/*    */   }
/*    */   
/*  9 */   public static final Transparency NONE = new Transparency(false, false);
/*    */   
/* 10 */   public static final Transparency TRANSPARENT = new Transparency(true, false);
/*    */   
/* 11 */   public static final Transparency TRANSLUCENT = new Transparency(false, true);
/*    */   
/* 12 */   public static final Transparency TRANSPARENT_AND_TRANSLUCENT = new Transparency(true, true);
/*    */   
/*    */   public static Transparency of(boolean hasTransparent, boolean hasTranslucent) {
/* 15 */     if (hasTransparent && hasTranslucent)
/* 16 */       return TRANSPARENT_AND_TRANSLUCENT; 
/* 17 */     if (hasTransparent)
/* 18 */       return TRANSPARENT; 
/* 19 */     if (hasTranslucent)
/* 20 */       return TRANSLUCENT; 
/* 22 */     return NONE;
/*    */   }
/*    */   
/*    */   public Transparency or(Transparency other) {
/* 27 */     return of((this.hasTransparent || other.hasTransparent), (this.hasTranslucent || other.hasTranslucent));
/*    */   }
/*    */   
/*    */   public boolean isOpaque() {
/* 31 */     return (!this.hasTransparent && !this.hasTranslucent);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\Transparency.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */