/*    */ package net.minecraft.gizmos;
/*    */ 
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Quaternionf;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public final class ArrowGizmo extends Record implements Gizmo {
/*    */   private final Vec3 start;
/*    */   
/*    */   private final Vec3 end;
/*    */   
/*    */   private final int color;
/*    */   
/*    */   private final float width;
/*    */   
/*    */   public static final float DEFAULT_WIDTH = 2.5F;
/*    */   
/*    */   public ArrowGizmo(Vec3 start, Vec3 end, int color, float width) {
/*  9 */     this.start = start;
/*  9 */     this.end = end;
/*  9 */     this.color = color;
/*  9 */     this.width = width;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/ArrowGizmo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/ArrowGizmo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/ArrowGizmo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/ArrowGizmo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/ArrowGizmo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gizmos/ArrowGizmo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3 start() {
/*  9 */     return this.start;
/*    */   }
/*    */   
/*    */   public Vec3 end() {
/*  9 */     return this.end;
/*    */   }
/*    */   
/*    */   public int color() {
/*  9 */     return this.color;
/*    */   }
/*    */   
/*    */   public float width() {
/*  9 */     return this.width;
/*    */   }
/*    */   
/*    */   public void emit(GizmoPrimitives primitives, float alphaMultiplier) {
/* 14 */     int color = ARGB.multiplyAlpha(this.color, alphaMultiplier);
/* 16 */     primitives.addLine(this.start, this.end, color, this.width);
/* 19 */     Quaternionf rotation = (new Quaternionf()).rotationTo((Vector3fc)new Vector3f(1.0F, 0.0F, 0.0F), (Vector3fc)this.end.subtract(this.start).toVector3f().normalize());
/* 20 */     float len = (float)Mth.clamp(this.end.distanceTo(this.start) * 0.10000000149011612D, 0.10000000149011612D, 1.0D);
/* 25 */     Vector3f[] tips = { rotation.transform(-len, len, 0.0F, new Vector3f()), rotation.transform(-len, 0.0F, len, new Vector3f()), rotation.transform(-len, -len, 0.0F, new Vector3f()), rotation.transform(-len, 0.0F, -len, new Vector3f()) };
/* 27 */     for (Vector3f tip : tips)
/* 28 */       primitives.addLine(this.end.add(tip.x, tip.y, tip.z), this.end, color, this.width); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gizmos\ArrowGizmo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */