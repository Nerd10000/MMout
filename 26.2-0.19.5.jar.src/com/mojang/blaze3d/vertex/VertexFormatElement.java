/*    */ package com.mojang.blaze3d.vertex;
/*    */ 
/*    */ import com.mojang.blaze3d.GpuFormat;
/*    */ import java.util.Locale;
/*    */ 
/*    */ public final class VertexFormatElement extends Record {
/*    */   private final String name;
/*    */   
/*    */   private final int offset;
/*    */   
/*    */   private final GpuFormat format;
/*    */   
/*    */   public VertexFormatElement(String name, int offset, GpuFormat format) {
/*  7 */     this.name = name;
/*  7 */     this.offset = offset;
/*  7 */     this.format = format;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vertex/VertexFormatElement;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vertex/VertexFormatElement;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vertex/VertexFormatElement;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/vertex/VertexFormatElement;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String name() {
/*  7 */     return this.name;
/*    */   }
/*    */   
/*    */   public int offset() {
/*  7 */     return this.offset;
/*    */   }
/*    */   
/*    */   public GpuFormat format() {
/*  7 */     return this.format;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 10 */     return String.format(Locale.ROOT, "%s %s offset:%d", new Object[] { this.name, this.format, Integer.valueOf(this.offset) });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\VertexFormatElement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */