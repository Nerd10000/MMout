/*    */ package net.minecraft.client.renderer.gizmos;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class Group extends Record {
/*    */   private final boolean opaque;
/*    */   
/*    */   private final List<DrawableGizmoPrimitives.Line> lines;
/*    */   
/*    */   private final List<DrawableGizmoPrimitives.Quad> quads;
/*    */   
/*    */   private final List<DrawableGizmoPrimitives.TriangleFan> triangleFans;
/*    */   
/*    */   private final List<DrawableGizmoPrimitives.Text> texts;
/*    */   
/*    */   private final List<DrawableGizmoPrimitives.Point> points;
/*    */   
/*    */   public List<DrawableGizmoPrimitives.Point> points() {
/* 74 */     return this.points;
/*    */   }
/*    */   
/*    */   public List<DrawableGizmoPrimitives.Text> texts() {
/* 74 */     return this.texts;
/*    */   }
/*    */   
/*    */   public List<DrawableGizmoPrimitives.TriangleFan> triangleFans() {
/* 74 */     return this.triangleFans;
/*    */   }
/*    */   
/*    */   public List<DrawableGizmoPrimitives.Quad> quads() {
/* 74 */     return this.quads;
/*    */   }
/*    */   
/*    */   public List<DrawableGizmoPrimitives.Line> lines() {
/* 74 */     return this.lines;
/*    */   }
/*    */   
/*    */   public boolean opaque() {
/* 74 */     return this.opaque;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #74	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #74	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #74	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group;
/*    */   }
/*    */   
/*    */   public Group(boolean opaque, List<DrawableGizmoPrimitives.Line> lines, List<DrawableGizmoPrimitives.Quad> quads, List<DrawableGizmoPrimitives.TriangleFan> triangleFans, List<DrawableGizmoPrimitives.Text> texts, List<DrawableGizmoPrimitives.Point> points) {
/* 74 */     this.opaque = opaque;
/* 74 */     this.lines = lines;
/* 74 */     this.quads = quads;
/* 74 */     this.triangleFans = triangleFans;
/* 74 */     this.texts = texts;
/* 74 */     this.points = points;
/*    */   }
/*    */   
/*    */   private Group(boolean opaque) {
/* 83 */     this(opaque, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\gizmos\DrawableGizmoPrimitives$Group.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */