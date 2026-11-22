/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ import java.util.OptionalInt;
/*    */ 
/*    */ public final class DisplayData extends Record {
/*    */   private final int width;
/*    */   
/*    */   private final int height;
/*    */   
/*    */   private final OptionalInt fullscreenWidth;
/*    */   
/*    */   private final OptionalInt fullscreenHeight;
/*    */   
/*    */   private final boolean isFullscreen;
/*    */   
/*    */   public DisplayData(int width, int height, OptionalInt fullscreenWidth, OptionalInt fullscreenHeight, boolean isFullscreen) {
/*  5 */     this.width = width;
/*  5 */     this.height = height;
/*  5 */     this.fullscreenWidth = fullscreenWidth;
/*  5 */     this.fullscreenHeight = fullscreenHeight;
/*  5 */     this.isFullscreen = isFullscreen;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/platform/DisplayData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/platform/DisplayData;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/platform/DisplayData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/platform/DisplayData;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/platform/DisplayData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/platform/DisplayData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int width() {
/*  5 */     return this.width;
/*    */   }
/*    */   
/*    */   public int height() {
/*  5 */     return this.height;
/*    */   }
/*    */   
/*    */   public OptionalInt fullscreenWidth() {
/*  5 */     return this.fullscreenWidth;
/*    */   }
/*    */   
/*    */   public OptionalInt fullscreenHeight() {
/*  5 */     return this.fullscreenHeight;
/*    */   }
/*    */   
/*    */   public boolean isFullscreen() {
/*  5 */     return this.isFullscreen;
/*    */   }
/*    */   
/*    */   public DisplayData withSize(int width, int height) {
/* 13 */     return new DisplayData(width, height, this.fullscreenWidth, this.fullscreenHeight, this.isFullscreen);
/*    */   }
/*    */   
/*    */   public DisplayData withFullscreen(boolean isFullscreen) {
/* 17 */     return new DisplayData(this.width, this.height, this.fullscreenWidth, this.fullscreenHeight, isFullscreen);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\DisplayData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */