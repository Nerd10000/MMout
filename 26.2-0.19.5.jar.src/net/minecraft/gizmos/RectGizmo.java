/*    */ package net.minecraft.gizmos;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class RectGizmo extends Record implements Gizmo {
/*    */   private final Vec3 a;
/*    */   
/*    */   private final Vec3 b;
/*    */   
/*    */   private final Vec3 c;
/*    */   
/*    */   private final Vec3 d;
/*    */   
/*    */   private final GizmoStyle style;
/*    */   
/*    */   public RectGizmo(Vec3 a, Vec3 b, Vec3 c, Vec3 d, GizmoStyle style) {
/*  6 */     this.a = a;
/*  6 */     this.b = b;
/*  6 */     this.c = c;
/*  6 */     this.d = d;
/*  6 */     this.style = style;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/RectGizmo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/RectGizmo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/RectGizmo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/RectGizmo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/RectGizmo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gizmos/RectGizmo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3 a() {
/*  6 */     return this.a;
/*    */   }
/*    */   
/*    */   public Vec3 b() {
/*  6 */     return this.b;
/*    */   }
/*    */   
/*    */   public Vec3 c() {
/*  6 */     return this.c;
/*    */   }
/*    */   
/*    */   public Vec3 d() {
/*  6 */     return this.d;
/*    */   }
/*    */   
/*    */   public GizmoStyle style() {
/*  6 */     return this.style;
/*    */   }
/*    */   
/*    */   public static RectGizmo fromCuboidFace(Vec3 cuboidCornerA, Vec3 cuboidCornerB, Direction face, GizmoStyle style) {
/*  8 */     switch (face) {
/*    */       default:
/*  8 */         throw new MatchException(null, null);
/*    */       case DOWN:
/*    */       
/*    */       case UP:
/*    */       
/*    */       case NORTH:
/*    */       
/*    */       case SOUTH:
/*    */       
/*    */       case WEST:
/*    */       
/*    */       case EAST:
/*    */         break;
/*    */     } 
/*  8 */     return 
/*    */       
/* 44 */       new RectGizmo(new Vec3(cuboidCornerB.x, cuboidCornerA.y, cuboidCornerA.z), new Vec3(cuboidCornerB.x, cuboidCornerB.y, cuboidCornerA.z), new Vec3(cuboidCornerB.x, cuboidCornerB.y, cuboidCornerB.z), new Vec3(cuboidCornerB.x, cuboidCornerA.y, cuboidCornerB.z), style);
/*    */   }
/*    */   
/*    */   public void emit(GizmoPrimitives primitives, float alphaMultiplier) {
/* 56 */     if (this.style.hasFill()) {
/* 57 */       int color = this.style.multipliedFill(alphaMultiplier);
/* 58 */       primitives.addQuad(this.a, this.b, this.c, this.d, color);
/*    */     } 
/* 60 */     if (this.style.hasStroke()) {
/* 61 */       int color = this.style.multipliedStroke(alphaMultiplier);
/* 62 */       primitives.addLine(this.a, this.b, color, this.style.strokeWidth());
/* 63 */       primitives.addLine(this.b, this.c, color, this.style.strokeWidth());
/* 64 */       primitives.addLine(this.c, this.d, color, this.style.strokeWidth());
/* 65 */       primitives.addLine(this.d, this.a, color, this.style.strokeWidth());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gizmos\RectGizmo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */