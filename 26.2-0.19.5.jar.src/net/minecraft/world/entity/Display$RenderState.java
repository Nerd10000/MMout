/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.mojang.math.Transformation;
/*     */ 
/*     */ public final class RenderState extends Record {
/*     */   private final Display.GenericInterpolator<Transformation> transformation;
/*     */   
/*     */   private final Display.BillboardConstraints billboardConstraints;
/*     */   
/*     */   private final int brightnessOverride;
/*     */   
/*     */   private final Display.FloatInterpolator shadowRadius;
/*     */   
/*     */   private final Display.FloatInterpolator shadowStrength;
/*     */   
/*     */   private final int glowColorOverride;
/*     */   
/*     */   public RenderState(Display.GenericInterpolator<Transformation> transformation, Display.BillboardConstraints billboardConstraints, int brightnessOverride, Display.FloatInterpolator shadowRadius, Display.FloatInterpolator shadowStrength, int glowColorOverride) {
/* 497 */     this.transformation = transformation;
/* 497 */     this.billboardConstraints = billboardConstraints;
/* 497 */     this.brightnessOverride = brightnessOverride;
/* 497 */     this.shadowRadius = shadowRadius;
/* 497 */     this.shadowStrength = shadowStrength;
/* 497 */     this.glowColorOverride = glowColorOverride;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$RenderState;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #497	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Display$RenderState;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$RenderState;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #497	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Display$RenderState;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$RenderState;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #497	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/Display$RenderState;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Display.GenericInterpolator<Transformation> transformation() {
/* 497 */     return this.transformation;
/*     */   }
/*     */   
/*     */   public Display.BillboardConstraints billboardConstraints() {
/* 497 */     return this.billboardConstraints;
/*     */   }
/*     */   
/*     */   public int brightnessOverride() {
/* 497 */     return this.brightnessOverride;
/*     */   }
/*     */   
/*     */   public Display.FloatInterpolator shadowRadius() {
/* 497 */     return this.shadowRadius;
/*     */   }
/*     */   
/*     */   public Display.FloatInterpolator shadowStrength() {
/* 497 */     return this.shadowStrength;
/*     */   }
/*     */   
/*     */   public int glowColorOverride() {
/* 497 */     return this.glowColorOverride;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Display$RenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */