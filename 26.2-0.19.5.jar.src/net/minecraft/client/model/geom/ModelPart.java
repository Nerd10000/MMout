/*     */ package net.minecraft.client.model.geom;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import org.joml.Matrix3f;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public final class ModelPart {
/*     */   public static final float DEFAULT_SCALE = 1.0F;
/*     */   
/*     */   public float x;
/*     */   
/*     */   public float y;
/*     */   
/*     */   public float z;
/*     */   
/*     */   public float xRot;
/*     */   
/*     */   public float yRot;
/*     */   
/*     */   public float zRot;
/*     */   
/*  35 */   public float xScale = 1.0F;
/*     */   
/*  36 */   public float yScale = 1.0F;
/*     */   
/*  37 */   public float zScale = 1.0F;
/*     */   
/*     */   public boolean visible = true;
/*     */   
/*     */   public boolean skipDraw;
/*     */   
/*     */   private final List<Cube> cubes;
/*     */   
/*     */   private final Map<String, ModelPart> children;
/*     */   
/*  44 */   private PartPose initialPose = PartPose.ZERO;
/*     */   
/*     */   public ModelPart(List<Cube> cubes, Map<String, ModelPart> children) {
/*  47 */     this.cubes = cubes;
/*  48 */     this.children = children;
/*     */   }
/*     */   
/*     */   public PartPose storePose() {
/*  52 */     return PartPose.offsetAndRotation(this.x, this.y, this.z, this.xRot, this.yRot, this.zRot);
/*     */   }
/*     */   
/*     */   public PartPose getInitialPose() {
/*  56 */     return this.initialPose;
/*     */   }
/*     */   
/*     */   public void setInitialPose(PartPose initialPose) {
/*  60 */     this.initialPose = initialPose;
/*     */   }
/*     */   
/*     */   public void resetPose() {
/*  64 */     loadPose(this.initialPose);
/*     */   }
/*     */   
/*     */   public void loadPose(PartPose pose) {
/*  68 */     this.x = pose.x();
/*  69 */     this.y = pose.y();
/*  70 */     this.z = pose.z();
/*  71 */     this.xRot = pose.xRot();
/*  72 */     this.yRot = pose.yRot();
/*  73 */     this.zRot = pose.zRot();
/*  74 */     this.xScale = pose.xScale();
/*  75 */     this.yScale = pose.yScale();
/*  76 */     this.zScale = pose.zScale();
/*     */   }
/*     */   
/*     */   public boolean hasChild(String name) {
/*  80 */     return this.children.containsKey(name);
/*     */   }
/*     */   
/*     */   public ModelPart getChild(String name) {
/*  84 */     ModelPart result = this.children.get(name);
/*  85 */     if (result == null)
/*  86 */       throw new NoSuchElementException("Can't find part " + name); 
/*  88 */     return result;
/*     */   }
/*     */   
/*     */   public void setPos(float x, float y, float z) {
/*  92 */     this.x = x;
/*  93 */     this.y = y;
/*  94 */     this.z = z;
/*     */   }
/*     */   
/*     */   public void setRotation(float xRot, float yRot, float zRot) {
/*  98 */     this.xRot = xRot;
/*  99 */     this.yRot = yRot;
/* 100 */     this.zRot = zRot;
/*     */   }
/*     */   
/*     */   public void render(PoseStack poseStack, VertexConsumer buffer, int lightCoords, int overlayCoords) {
/* 104 */     render(poseStack, buffer, lightCoords, overlayCoords, -1);
/*     */   }
/*     */   
/*     */   public void render(PoseStack poseStack, VertexConsumer buffer, int lightCoords, int overlayCoords, int color) {
/* 108 */     if (!this.visible)
/*     */       return; 
/* 111 */     if (this.cubes.isEmpty() && this.children.isEmpty())
/*     */       return; 
/* 115 */     poseStack.pushPose();
/* 116 */     translateAndRotate(poseStack);
/* 118 */     if (!this.skipDraw)
/* 119 */       compile(poseStack.last(), buffer, lightCoords, overlayCoords, color); 
/* 122 */     for (ModelPart child : this.children.values())
/* 123 */       child.render(poseStack, buffer, lightCoords, overlayCoords, color); 
/* 126 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public void rotateBy(Quaternionf rotation) {
/* 130 */     Matrix3f oldRotation = (new Matrix3f()).rotationZYX(this.zRot, this.yRot, this.xRot);
/* 131 */     Matrix3f newRotation = oldRotation.rotate((Quaternionfc)rotation);
/* 132 */     Vector3f newAngles = newRotation.getEulerAnglesZYX(new Vector3f());
/* 133 */     setRotation(newAngles.x, newAngles.y, newAngles.z);
/*     */   }
/*     */   
/*     */   public void getExtentsForGui(PoseStack poseStack, Consumer<Vector3fc> output) {
/* 142 */     visit(poseStack, (pose, partPath, cubeIndex, cube) -> {
/*     */           for (Polygon polygon : cube.polygons) {
/*     */             for (Vertex vertex : polygon.vertices()) {
/*     */               float x = vertex.worldX();
/*     */               float y = vertex.worldY();
/*     */               float z = vertex.worldZ();
/*     */               Vector3f pos = pose.pose().transformPosition(x, y, z, new Vector3f());
/*     */               output.accept(pos);
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public void visit(PoseStack poseStack, Visitor visitor) {
/* 156 */     visit(poseStack, visitor, "");
/*     */   }
/*     */   
/*     */   private void visit(PoseStack poseStack, Visitor visitor, String path) {
/* 160 */     if (this.cubes.isEmpty() && this.children.isEmpty())
/*     */       return; 
/* 164 */     poseStack.pushPose();
/* 165 */     translateAndRotate(poseStack);
/* 167 */     PoseStack.Pose pose = poseStack.last();
/* 168 */     for (int i = 0; i < this.cubes.size(); i++)
/* 169 */       visitor.visit(pose, path, i, this.cubes.get(i)); 
/* 172 */     String childPath = path + "/";
/* 173 */     this.children.forEach((name, child) -> child.visit(poseStack, visitor, childPath + childPath));
/* 177 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public void translateAndRotate(PoseStack poseStack) {
/* 181 */     poseStack.translate(this.x / 16.0F, this.y / 16.0F, this.z / 16.0F);
/* 182 */     if (this.xRot != 0.0F || this.yRot != 0.0F || this.zRot != 0.0F)
/* 183 */       poseStack.mulPose((Quaternionfc)(new Quaternionf()).rotationZYX(this.zRot, this.yRot, this.xRot)); 
/* 185 */     if (this.xScale != 1.0F || this.yScale != 1.0F || this.zScale != 1.0F)
/* 186 */       poseStack.scale(this.xScale, this.yScale, this.zScale); 
/*     */   }
/*     */   
/*     */   private void compile(PoseStack.Pose pose, VertexConsumer builder, int lightCoords, int overlayCoords, int color) {
/* 191 */     for (Cube cube : this.cubes)
/* 192 */       cube.compile(pose, builder, lightCoords, overlayCoords, color); 
/*     */   }
/*     */   
/*     */   public Cube getRandomCube(RandomSource random) {
/* 197 */     return (Cube)Util.getRandom(this.cubes, random);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 201 */     return this.cubes.isEmpty();
/*     */   }
/*     */   
/*     */   public void offsetPos(Vector3f offset) {
/* 205 */     this.x += offset.x();
/* 206 */     this.y += offset.y();
/* 207 */     this.z += offset.z();
/*     */   }
/*     */   
/*     */   public void offsetRotation(Vector3f offset) {
/* 211 */     this.xRot += offset.x();
/* 212 */     this.yRot += offset.y();
/* 213 */     this.zRot += offset.z();
/*     */   }
/*     */   
/*     */   public void offsetScale(Vector3f offset) {
/* 217 */     this.xScale += offset.x();
/* 218 */     this.yScale += offset.y();
/* 219 */     this.zScale += offset.z();
/*     */   }
/*     */   
/*     */   public List<ModelPart> getAllParts() {
/* 223 */     List<ModelPart> allParts = new ArrayList<>();
/* 224 */     allParts.add(this);
/* 225 */     addAllChildren((name, part) -> allParts.add(part));
/* 226 */     return List.copyOf(allParts);
/*     */   }
/*     */   
/*     */   public Function<String, ModelPart> createPartLookup() {
/* 230 */     Map<String, ModelPart> parts = new HashMap<>();
/* 231 */     parts.put("root", this);
/* 232 */     Objects.requireNonNull(parts);
/* 232 */     addAllChildren(parts::putIfAbsent);
/* 233 */     Objects.requireNonNull(parts);
/* 233 */     return parts::get;
/*     */   }
/*     */   
/*     */   private void addAllChildren(BiConsumer<String, ModelPart> output) {
/* 237 */     for (Map.Entry<String, ModelPart> entry : this.children.entrySet())
/* 238 */       output.accept(entry.getKey(), entry.getValue()); 
/* 240 */     for (ModelPart part : this.children.values())
/* 241 */       part.addAllChildren(output); 
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Visitor {
/*     */     void visit(PoseStack.Pose param1Pose, String param1String, int param1Int, ModelPart.Cube param1Cube);
/*     */   }
/*     */   
/*     */   public static class Cube {
/*     */     public final ModelPart.Polygon[] polygons;
/*     */     
/*     */     public final float minX;
/*     */     
/*     */     public final float minY;
/*     */     
/*     */     public final float minZ;
/*     */     
/*     */     public final float maxX;
/*     */     
/*     */     public final float maxY;
/*     */     
/*     */     public final float maxZ;
/*     */     
/*     */     public Cube(int xTexOffs, int yTexOffs, float minX, float minY, float minZ, float width, float height, float depth, float growX, float growY, float growZ, boolean mirror, float xTexSize, float yTexSize, Set<Direction> visibleFaces) {
/* 255 */       this.minX = minX;
/* 256 */       this.minY = minY;
/* 257 */       this.minZ = minZ;
/* 258 */       this.maxX = minX + width;
/* 259 */       this.maxY = minY + height;
/* 260 */       this.maxZ = minZ + depth;
/* 261 */       this.polygons = new ModelPart.Polygon[visibleFaces.size()];
/* 263 */       float maxX = minX + width;
/* 264 */       float maxY = minY + height;
/* 265 */       float maxZ = minZ + depth;
/* 267 */       minX -= growX;
/* 268 */       minY -= growY;
/* 269 */       minZ -= growZ;
/* 270 */       maxX += growX;
/* 271 */       maxY += growY;
/* 272 */       maxZ += growZ;
/* 274 */       if (mirror) {
/* 275 */         float tmp = maxX;
/* 276 */         maxX = minX;
/* 277 */         minX = tmp;
/*     */       } 
/* 280 */       ModelPart.Vertex t0 = new ModelPart.Vertex(minX, minY, minZ, 0.0F, 0.0F);
/* 281 */       ModelPart.Vertex t1 = new ModelPart.Vertex(maxX, minY, minZ, 0.0F, 8.0F);
/* 282 */       ModelPart.Vertex t2 = new ModelPart.Vertex(maxX, maxY, minZ, 8.0F, 8.0F);
/* 283 */       ModelPart.Vertex t3 = new ModelPart.Vertex(minX, maxY, minZ, 8.0F, 0.0F);
/* 285 */       ModelPart.Vertex l0 = new ModelPart.Vertex(minX, minY, maxZ, 0.0F, 0.0F);
/* 286 */       ModelPart.Vertex l1 = new ModelPart.Vertex(maxX, minY, maxZ, 0.0F, 8.0F);
/* 287 */       ModelPart.Vertex l2 = new ModelPart.Vertex(maxX, maxY, maxZ, 8.0F, 8.0F);
/* 288 */       ModelPart.Vertex l3 = new ModelPart.Vertex(minX, maxY, maxZ, 8.0F, 0.0F);
/* 290 */       float u0 = xTexOffs;
/* 291 */       float u1 = xTexOffs + depth;
/* 292 */       float u2 = xTexOffs + depth + width;
/* 293 */       float u22 = xTexOffs + depth + width + width;
/* 294 */       float u3 = xTexOffs + depth + width + depth;
/* 295 */       float u4 = xTexOffs + depth + width + depth + width;
/* 297 */       float v0 = yTexOffs;
/* 298 */       float v1 = yTexOffs + depth;
/* 299 */       float v2 = yTexOffs + depth + height;
/* 301 */       int pos = 0;
/* 302 */       if (visibleFaces.contains(Direction.DOWN))
/* 303 */         this.polygons[pos++] = new ModelPart.Polygon(new ModelPart.Vertex[] { l1, l0, t0, t1 }, u1, v0, u2, v1, xTexSize, yTexSize, mirror, Direction.DOWN); 
/* 305 */       if (visibleFaces.contains(Direction.UP))
/* 306 */         this.polygons[pos++] = new ModelPart.Polygon(new ModelPart.Vertex[] { t2, t3, l3, l2 }, u2, v1, u22, v0, xTexSize, yTexSize, mirror, Direction.UP); 
/* 309 */       if (visibleFaces.contains(Direction.WEST))
/* 310 */         this.polygons[pos++] = new ModelPart.Polygon(new ModelPart.Vertex[] { t0, l0, l3, t3 }, u0, v1, u1, v2, xTexSize, yTexSize, mirror, Direction.WEST); 
/* 312 */       if (visibleFaces.contains(Direction.NORTH))
/* 313 */         this.polygons[pos++] = new ModelPart.Polygon(new ModelPart.Vertex[] { t1, t0, t3, t2 }, u1, v1, u2, v2, xTexSize, yTexSize, mirror, Direction.NORTH); 
/* 315 */       if (visibleFaces.contains(Direction.EAST))
/* 316 */         this.polygons[pos++] = new ModelPart.Polygon(new ModelPart.Vertex[] { l1, t1, t2, l2 }, u2, v1, u3, v2, xTexSize, yTexSize, mirror, Direction.EAST); 
/* 318 */       if (visibleFaces.contains(Direction.SOUTH))
/* 319 */         this.polygons[pos] = new ModelPart.Polygon(new ModelPart.Vertex[] { l0, l1, l2, l3 }, u3, v1, u4, v2, xTexSize, yTexSize, mirror, Direction.SOUTH); 
/*     */     }
/*     */     
/*     */     public void compile(PoseStack.Pose pose, VertexConsumer builder, int lightCoords, int overlayCoords, int color) {
/* 324 */       Matrix4f matrix = pose.pose();
/* 325 */       Vector3f scratchVector = new Vector3f();
/* 326 */       for (ModelPart.Polygon polygon : this.polygons) {
/* 327 */         Vector3f normal = pose.transformNormal(polygon.normal, scratchVector);
/* 328 */         float nx = normal.x();
/* 329 */         float ny = normal.y();
/* 330 */         float nz = normal.z();
/* 332 */         for (ModelPart.Vertex vertex : polygon.vertices) {
/* 333 */           float x = vertex.worldX();
/* 334 */           float y = vertex.worldY();
/* 335 */           float z = vertex.worldZ();
/* 336 */           Vector3f pos = matrix.transformPosition(x, y, z, scratchVector);
/* 337 */           builder.addVertex(pos.x(), pos.y(), pos.z(), color, vertex.u, vertex.v, overlayCoords, lightCoords, nx, ny, nz);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Polygon extends Record {
/*     */     private final ModelPart.Vertex[] vertices;
/*     */     
/*     */     private final Vector3fc normal;
/*     */     
/*     */     public Polygon(ModelPart.Vertex[] vertices, Vector3fc normal) {
/* 343 */       this.vertices = vertices;
/* 343 */       this.normal = normal;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/geom/ModelPart$Polygon;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #343	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/model/geom/ModelPart$Polygon;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/geom/ModelPart$Polygon;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #343	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/model/geom/ModelPart$Polygon;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/geom/ModelPart$Polygon;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #343	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/model/geom/ModelPart$Polygon;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ModelPart.Vertex[] vertices() {
/* 343 */       return this.vertices;
/*     */     }
/*     */     
/*     */     public Vector3fc normal() {
/* 343 */       return this.normal;
/*     */     }
/*     */     
/*     */     public Polygon(ModelPart.Vertex[] vertices, float u0, float v0, float u1, float v1, float xTexSize, float yTexSize, boolean mirror, Direction facing) {
/* 345 */       this(vertices, (mirror ? mirrorFacing(facing) : facing).getUnitVec3f());
/* 347 */       float us = 0.0F / xTexSize;
/* 348 */       float vs = 0.0F / yTexSize;
/* 349 */       vertices[0] = vertices[0].remap(u1 / xTexSize - us, v0 / yTexSize + vs);
/* 350 */       vertices[1] = vertices[1].remap(u0 / xTexSize + us, v0 / yTexSize + vs);
/* 351 */       vertices[2] = vertices[2].remap(u0 / xTexSize + us, v1 / yTexSize - vs);
/* 352 */       vertices[3] = vertices[3].remap(u1 / xTexSize - us, v1 / yTexSize - vs);
/* 354 */       if (mirror) {
/* 355 */         int length = vertices.length;
/* 356 */         for (int i = 0; i < length / 2; i++) {
/* 357 */           ModelPart.Vertex tmp = vertices[i];
/* 358 */           vertices[i] = vertices[length - 1 - i];
/* 359 */           vertices[length - 1 - i] = tmp;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private static Direction mirrorFacing(Direction facing) {
/* 365 */       return (facing.getAxis() == Direction.Axis.X) ? facing.getOpposite() : facing;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Vertex extends Record {
/*     */     private final float x;
/*     */     
/*     */     private final float y;
/*     */     
/*     */     private final float z;
/*     */     
/*     */     private final float u;
/*     */     
/*     */     private final float v;
/*     */     
/*     */     public static final float SCALE_FACTOR = 16.0F;
/*     */     
/*     */     public Vertex(float x, float y, float z, float u, float v) {
/* 369 */       this.x = x;
/* 369 */       this.y = y;
/* 369 */       this.z = z;
/* 369 */       this.u = u;
/* 369 */       this.v = v;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/geom/ModelPart$Vertex;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #369	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/model/geom/ModelPart$Vertex;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/geom/ModelPart$Vertex;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #369	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/model/geom/ModelPart$Vertex;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/geom/ModelPart$Vertex;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #369	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/model/geom/ModelPart$Vertex;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public float x() {
/* 369 */       return this.x;
/*     */     }
/*     */     
/*     */     public float y() {
/* 369 */       return this.y;
/*     */     }
/*     */     
/*     */     public float z() {
/* 369 */       return this.z;
/*     */     }
/*     */     
/*     */     public float u() {
/* 369 */       return this.u;
/*     */     }
/*     */     
/*     */     public float v() {
/* 369 */       return this.v;
/*     */     }
/*     */     
/*     */     public Vertex remap(float u, float v) {
/* 373 */       return new Vertex(this.x, this.y, this.z, u, v);
/*     */     }
/*     */     
/*     */     public float worldX() {
/* 377 */       return this.x / 16.0F;
/*     */     }
/*     */     
/*     */     public float worldY() {
/* 381 */       return this.y / 16.0F;
/*     */     }
/*     */     
/*     */     public float worldZ() {
/* 385 */       return this.z / 16.0F;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\geom\ModelPart.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */