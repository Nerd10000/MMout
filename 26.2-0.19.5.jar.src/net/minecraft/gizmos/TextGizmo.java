/*    */ package net.minecraft.gizmos;
/*    */ 
/*    */ import java.util.OptionalDouble;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class TextGizmo extends Record implements Gizmo {
/*    */   private final Vec3 pos;
/*    */   
/*    */   private final String text;
/*    */   
/*    */   private final Style style;
/*    */   
/*    */   public TextGizmo(Vec3 pos, String text, Style style) {
/*  9 */     this.pos = pos;
/*  9 */     this.text = text;
/*  9 */     this.style = style;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/TextGizmo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/TextGizmo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/TextGizmo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/TextGizmo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/TextGizmo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gizmos/TextGizmo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3 pos() {
/*  9 */     return this.pos;
/*    */   }
/*    */   
/*    */   public String text() {
/*  9 */     return this.text;
/*    */   }
/*    */   
/*    */   public Style style() {
/*  9 */     return this.style;
/*    */   }
/*    */   
/*    */   public void emit(GizmoPrimitives primitives, float alphaMultiplier) {
/*    */     Style newStyle;
/* 13 */     if (alphaMultiplier < 1.0F) {
/* 14 */       newStyle = new Style(ARGB.multiplyAlpha(this.style.color, alphaMultiplier), this.style.scale, this.style.adjustLeft);
/*    */     } else {
/* 16 */       newStyle = this.style;
/*    */     } 
/* 18 */     primitives.addText(this.pos, this.text, newStyle);
/*    */   }
/*    */   
/*    */   public static final class Style extends Record {
/*    */     private final int color;
/*    */     
/*    */     private final float scale;
/*    */     
/*    */     private final OptionalDouble adjustLeft;
/*    */     
/*    */     public static final float DEFAULT_SCALE = 0.32F;
/*    */     
/*    */     public Style(int color, float scale, OptionalDouble adjustLeft) {
/* 21 */       this.color = color;
/* 21 */       this.scale = scale;
/* 21 */       this.adjustLeft = adjustLeft;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/TextGizmo$Style;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/gizmos/TextGizmo$Style;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/TextGizmo$Style;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/gizmos/TextGizmo$Style;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/TextGizmo$Style;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/gizmos/TextGizmo$Style;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public int color() {
/* 21 */       return this.color;
/*    */     }
/*    */     
/*    */     public float scale() {
/* 21 */       return this.scale;
/*    */     }
/*    */     
/*    */     public OptionalDouble adjustLeft() {
/* 21 */       return this.adjustLeft;
/*    */     }
/*    */     
/*    */     public static Style whiteAndCentered() {
/* 25 */       return new Style(-1, 0.32F, OptionalDouble.empty());
/*    */     }
/*    */     
/*    */     public static Style forColorAndCentered(int argb) {
/* 29 */       return new Style(argb, 0.32F, OptionalDouble.empty());
/*    */     }
/*    */     
/*    */     public static Style forColor(int argb) {
/* 33 */       return new Style(argb, 0.32F, OptionalDouble.of(0.0D));
/*    */     }
/*    */     
/*    */     public Style withScale(float scale) {
/* 37 */       return new Style(this.color, scale, this.adjustLeft);
/*    */     }
/*    */     
/*    */     public Style withLeftAlignment(float adjustLeft) {
/* 41 */       return new Style(this.color, this.scale, OptionalDouble.of(adjustLeft));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gizmos\TextGizmo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */