/*    */ package com.mojang.blaze3d.pipeline;
/*    */ 
/*    */ import com.mojang.blaze3d.GpuFormat;
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public final class ColorTargetState extends Record {
/*    */   private final Optional<BlendFunction> blendFunction;
/*    */   
/*    */   private final GpuFormat format;
/*    */   
/*    */   private final int writeMask;
/*    */   
/*    */   public static final int WRITE_RED = 1;
/*    */   
/*    */   public static final int WRITE_GREEN = 2;
/*    */   
/*    */   public static final int WRITE_BLUE = 4;
/*    */   
/*    */   public static final int WRITE_ALPHA = 8;
/*    */   
/*    */   public static final int WRITE_COLOR = 7;
/*    */   
/*    */   public static final int WRITE_ALL = 15;
/*    */   
/*    */   public static final int WRITE_NONE = 0;
/*    */   
/*    */   public ColorTargetState(Optional<BlendFunction> blendFunction, GpuFormat format, int writeMask) {
/* 12 */     this.blendFunction = blendFunction;
/* 12 */     this.format = format;
/* 12 */     this.writeMask = writeMask;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/pipeline/ColorTargetState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/ColorTargetState;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/pipeline/ColorTargetState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/ColorTargetState;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/pipeline/ColorTargetState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/pipeline/ColorTargetState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<BlendFunction> blendFunction() {
/* 12 */     return this.blendFunction;
/*    */   }
/*    */   
/*    */   public GpuFormat format() {
/* 12 */     return this.format;
/*    */   }
/*    */   
/*    */   public int writeMask() {
/* 12 */     return this.writeMask;
/*    */   }
/*    */   
/* 20 */   public static final ColorTargetState DEFAULT = new ColorTargetState(Optional.empty(), GpuFormat.RGBA8_UNORM, 15);
/*    */   
/*    */   public static final int MAX_COLOR_TARGETS = 8;
/*    */   
/*    */   public ColorTargetState(BlendFunction blendFunction) {
/* 25 */     this(Optional.of(blendFunction), GpuFormat.RGBA8_UNORM, 15);
/*    */   }
/*    */   
/*    */   public boolean writeRed() {
/* 29 */     return ((this.writeMask & 0x1) != 0);
/*    */   }
/*    */   
/*    */   public boolean writeGreen() {
/* 33 */     return ((this.writeMask & 0x2) != 0);
/*    */   }
/*    */   
/*    */   public boolean writeBlue() {
/* 37 */     return ((this.writeMask & 0x4) != 0);
/*    */   }
/*    */   
/*    */   public boolean writeAlpha() {
/* 41 */     return ((this.writeMask & 0x8) != 0);
/*    */   }
/*    */   
/*    */   @Retention(RetentionPolicy.CLASS)
/*    */   @Target({ElementType.TYPE_USE})
/*    */   public static @interface WriteMask {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\pipeline\ColorTargetState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */