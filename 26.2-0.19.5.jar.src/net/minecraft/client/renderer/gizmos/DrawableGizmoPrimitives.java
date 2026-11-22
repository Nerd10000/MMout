/*    */ package net.minecraft.client.renderer.gizmos;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.gizmos.GizmoPrimitives;
/*    */ import net.minecraft.gizmos.TextGizmo;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DrawableGizmoPrimitives implements GizmoPrimitives {
/* 14 */   private final Group opaque = new Group(true);
/*    */   
/* 15 */   private final Group translucent = new Group(false);
/*    */   
/*    */   private boolean isEmpty = true;
/*    */   
/*    */   private Group getGroup(int color) {
/* 19 */     if (ARGB.alpha(color) < 255)
/* 20 */       return this.translucent; 
/* 22 */     return this.opaque;
/*    */   }
/*    */   
/*    */   public void addPoint(Vec3 pos, int color, float size) {
/* 28 */     (getGroup(color)).points.add(new Point(pos, color, size));
/* 29 */     this.isEmpty = false;
/*    */   }
/*    */   
/*    */   public void addLine(Vec3 start, Vec3 end, int color, float width) {
/* 34 */     (getGroup(color)).lines.add(new Line(start, end, color, width));
/* 35 */     this.isEmpty = false;
/*    */   }
/*    */   
/*    */   public void addTriangleFan(Vec3[] points, int color) {
/* 40 */     (getGroup(color)).triangleFans.add(new TriangleFan(points, color));
/* 41 */     this.isEmpty = false;
/*    */   }
/*    */   
/*    */   public void addQuad(Vec3 a, Vec3 b, Vec3 c, Vec3 d, int color) {
/* 46 */     (getGroup(color)).quads.add(new Quad(a, b, c, d, color));
/* 47 */     this.isEmpty = false;
/*    */   }
/*    */   
/*    */   public void addText(Vec3 pos, String text, TextGizmo.Style style) {
/* 52 */     (getGroup(style.color())).texts.add(new Text(pos, text, style));
/* 53 */     this.isEmpty = false;
/*    */   }
/*    */   
/*    */   public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, boolean onTop) {
/* 57 */     if (this.isEmpty)
/*    */       return; 
/* 60 */     submitNodeCollector.submitGizmoPrimitives(this.opaque, cameraRenderState, onTop);
/* 61 */     submitNodeCollector.submitGizmoPrimitives(this.translucent, cameraRenderState, onTop);
/*    */   }
/*    */   
/*    */   public static final class Line extends Record {
/*    */     private final Vec3 start;
/*    */     
/*    */     private final Vec3 end;
/*    */     
/*    */     private final int color;
/*    */     
/*    */     private final float width;
/*    */     
/*    */     public Line(Vec3 start, Vec3 end, int color, float width) {
/* 64 */       this.start = start;
/* 64 */       this.end = end;
/* 64 */       this.color = color;
/* 64 */       this.width = width;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #64	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #64	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #64	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Vec3 start() {
/* 64 */       return this.start;
/*    */     }
/*    */     
/*    */     public Vec3 end() {
/* 64 */       return this.end;
/*    */     }
/*    */     
/*    */     public int color() {
/* 64 */       return this.color;
/*    */     }
/*    */     
/*    */     public float width() {
/* 64 */       return this.width;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class TriangleFan extends Record {
/*    */     private final Vec3[] points;
/*    */     
/*    */     private final int color;
/*    */     
/*    */     public TriangleFan(Vec3[] points, int color) {
/* 66 */       this.points = points;
/* 66 */       this.color = color;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$TriangleFan;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$TriangleFan;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$TriangleFan;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$TriangleFan;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$TriangleFan;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$TriangleFan;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Vec3[] points() {
/* 66 */       return this.points;
/*    */     }
/*    */     
/*    */     public int color() {
/* 66 */       return this.color;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Quad extends Record {
/*    */     private final Vec3 a;
/*    */     
/*    */     private final Vec3 b;
/*    */     
/*    */     private final Vec3 c;
/*    */     
/*    */     private final Vec3 d;
/*    */     
/*    */     private final int color;
/*    */     
/*    */     public Quad(Vec3 a, Vec3 b, Vec3 c, Vec3 d, int color) {
/* 68 */       this.a = a;
/* 68 */       this.b = b;
/* 68 */       this.c = c;
/* 68 */       this.d = d;
/* 68 */       this.color = color;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Vec3 a() {
/* 68 */       return this.a;
/*    */     }
/*    */     
/*    */     public Vec3 b() {
/* 68 */       return this.b;
/*    */     }
/*    */     
/*    */     public Vec3 c() {
/* 68 */       return this.c;
/*    */     }
/*    */     
/*    */     public Vec3 d() {
/* 68 */       return this.d;
/*    */     }
/*    */     
/*    */     public int color() {
/* 68 */       return this.color;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Text extends Record {
/*    */     private final Vec3 pos;
/*    */     
/*    */     private final String text;
/*    */     
/*    */     private final TextGizmo.Style style;
/*    */     
/*    */     public Text(Vec3 pos, String text, TextGizmo.Style style) {
/* 70 */       this.pos = pos;
/* 70 */       this.text = text;
/* 70 */       this.style = style;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #70	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #70	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #70	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Vec3 pos() {
/* 70 */       return this.pos;
/*    */     }
/*    */     
/*    */     public String text() {
/* 70 */       return this.text;
/*    */     }
/*    */     
/*    */     public TextGizmo.Style style() {
/* 70 */       return this.style;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Point extends Record {
/*    */     private final Vec3 pos;
/*    */     
/*    */     private final int color;
/*    */     
/*    */     private final float size;
/*    */     
/*    */     public Point(Vec3 pos, int color, float size) {
/* 72 */       this.pos = pos;
/* 72 */       this.color = color;
/* 72 */       this.size = size;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #72	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #72	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #72	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Vec3 pos() {
/* 72 */       return this.pos;
/*    */     }
/*    */     
/*    */     public int color() {
/* 72 */       return this.color;
/*    */     }
/*    */     
/*    */     public float size() {
/* 72 */       return this.size;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Group extends Record {
/*    */     private final boolean opaque;
/*    */     
/*    */     private final List<DrawableGizmoPrimitives.Line> lines;
/*    */     
/*    */     private final List<DrawableGizmoPrimitives.Quad> quads;
/*    */     
/*    */     private final List<DrawableGizmoPrimitives.TriangleFan> triangleFans;
/*    */     
/*    */     private final List<DrawableGizmoPrimitives.Text> texts;
/*    */     
/*    */     private final List<DrawableGizmoPrimitives.Point> points;
/*    */     
/*    */     public List<DrawableGizmoPrimitives.Point> points() {
/* 74 */       return this.points;
/*    */     }
/*    */     
/*    */     public List<DrawableGizmoPrimitives.Text> texts() {
/* 74 */       return this.texts;
/*    */     }
/*    */     
/*    */     public List<DrawableGizmoPrimitives.TriangleFan> triangleFans() {
/* 74 */       return this.triangleFans;
/*    */     }
/*    */     
/*    */     public List<DrawableGizmoPrimitives.Quad> quads() {
/* 74 */       return this.quads;
/*    */     }
/*    */     
/*    */     public List<DrawableGizmoPrimitives.Line> lines() {
/* 74 */       return this.lines;
/*    */     }
/*    */     
/*    */     public boolean opaque() {
/* 74 */       return this.opaque;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Group;
/*    */     }
/*    */     
/*    */     public Group(boolean opaque, List<DrawableGizmoPrimitives.Line> lines, List<DrawableGizmoPrimitives.Quad> quads, List<DrawableGizmoPrimitives.TriangleFan> triangleFans, List<DrawableGizmoPrimitives.Text> texts, List<DrawableGizmoPrimitives.Point> points) {
/* 74 */       this.opaque = opaque;
/* 74 */       this.lines = lines;
/* 74 */       this.quads = quads;
/* 74 */       this.triangleFans = triangleFans;
/* 74 */       this.texts = texts;
/* 74 */       this.points = points;
/*    */     }
/*    */     
/*    */     private Group(boolean opaque) {
/* 83 */       this(opaque, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\gizmos\DrawableGizmoPrimitives.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */