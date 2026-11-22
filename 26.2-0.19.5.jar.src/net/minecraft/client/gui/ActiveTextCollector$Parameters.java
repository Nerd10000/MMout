/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import org.joml.Matrix3x2f;
/*     */ import org.joml.Matrix3x2fc;
/*     */ 
/*     */ public final class Parameters extends Record {
/*     */   private final Matrix3x2fc pose;
/*     */   
/*     */   private final float opacity;
/*     */   
/*     */   private final ScreenRectangle scissor;
/*     */   
/*     */   public Parameters(Matrix3x2fc pose, float opacity, ScreenRectangle scissor) {
/*  83 */     this.pose = pose;
/*  83 */     this.opacity = opacity;
/*  83 */     this.scissor = scissor;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #83	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #83	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #83	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Matrix3x2fc pose() {
/*  83 */     return this.pose;
/*     */   }
/*     */   
/*     */   public float opacity() {
/*  83 */     return this.opacity;
/*     */   }
/*     */   
/*     */   public ScreenRectangle scissor() {
/*  83 */     return this.scissor;
/*     */   }
/*     */   
/*     */   public Parameters(Matrix3x2fc pose) {
/*  89 */     this(pose, 1.0F, null);
/*     */   }
/*     */   
/*     */   public Parameters withPose(Matrix3x2fc pose) {
/*  93 */     return new Parameters(pose, this.opacity, this.scissor);
/*     */   }
/*     */   
/*     */   public Parameters withScale(float scale) {
/*  98 */     return withPose((Matrix3x2fc)this.pose.scale(scale, scale, new Matrix3x2f()));
/*     */   }
/*     */   
/*     */   public Parameters withOpacity(float opacity) {
/* 102 */     if (this.opacity == opacity)
/* 103 */       return this; 
/* 105 */     return new Parameters(this.pose, opacity, this.scissor);
/*     */   }
/*     */   
/*     */   public Parameters withScissor(ScreenRectangle scissor) {
/* 109 */     if (scissor.equals(this.scissor))
/* 110 */       return this; 
/* 112 */     return new Parameters(this.pose, this.opacity, scissor);
/*     */   }
/*     */   
/*     */   public Parameters withScissor(int left, int right, int top, int bottom) {
/* 116 */     ScreenRectangle newScissor = (new ScreenRectangle(left, top, right - left, bottom - top)).transformAxisAligned(this.pose);
/* 117 */     if (this.scissor != null)
/* 118 */       newScissor = Objects.<ScreenRectangle>requireNonNullElse(this.scissor.intersection(newScissor), ScreenRectangle.empty()); 
/* 120 */     return withScissor(newScissor);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\ActiveTextCollector$Parameters.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */