/*     */ package net.minecraft.client.model.geom;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.Direction;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ public class Cube {
/*     */   public final ModelPart.Polygon[] polygons;
/*     */   
/*     */   public final float minX;
/*     */   
/*     */   public final float minY;
/*     */   
/*     */   public final float minZ;
/*     */   
/*     */   public final float maxX;
/*     */   
/*     */   public final float maxY;
/*     */   
/*     */   public final float maxZ;
/*     */   
/*     */   public Cube(int xTexOffs, int yTexOffs, float minX, float minY, float minZ, float width, float height, float depth, float growX, float growY, float growZ, boolean mirror, float xTexSize, float yTexSize, Set<Direction> visibleFaces) {
/* 255 */     this.minX = minX;
/* 256 */     this.minY = minY;
/* 257 */     this.minZ = minZ;
/* 258 */     this.maxX = minX + width;
/* 259 */     this.maxY = minY + height;
/* 260 */     this.maxZ = minZ + depth;
/* 261 */     this.polygons = new ModelPart.Polygon[visibleFaces.size()];
/* 263 */     float maxX = minX + width;
/* 264 */     float maxY = minY + height;
/* 265 */     float maxZ = minZ + depth;
/* 267 */     minX -= growX;
/* 268 */     minY -= growY;
/* 269 */     minZ -= growZ;
/* 270 */     maxX += growX;
/* 271 */     maxY += growY;
/* 272 */     maxZ += growZ;
/* 274 */     if (mirror) {
/* 275 */       float tmp = maxX;
/* 276 */       maxX = minX;
/* 277 */       minX = tmp;
/*     */     } 
/* 280 */     ModelPart.Vertex t0 = new ModelPart.Vertex(minX, minY, minZ, 0.0F, 0.0F);
/* 281 */     ModelPart.Vertex t1 = new ModelPart.Vertex(maxX, minY, minZ, 0.0F, 8.0F);
/* 282 */     ModelPart.Vertex t2 = new ModelPart.Vertex(maxX, maxY, minZ, 8.0F, 8.0F);
/* 283 */     ModelPart.Vertex t3 = new ModelPart.Vertex(minX, maxY, minZ, 8.0F, 0.0F);
/* 285 */     ModelPart.Vertex l0 = new ModelPart.Vertex(minX, minY, maxZ, 0.0F, 0.0F);
/* 286 */     ModelPart.Vertex l1 = new ModelPart.Vertex(maxX, minY, maxZ, 0.0F, 8.0F);
/* 287 */     ModelPart.Vertex l2 = new ModelPart.Vertex(maxX, maxY, maxZ, 8.0F, 8.0F);
/* 288 */     ModelPart.Vertex l3 = new ModelPart.Vertex(minX, maxY, maxZ, 8.0F, 0.0F);
/* 290 */     float u0 = xTexOffs;
/* 291 */     float u1 = xTexOffs + depth;
/* 292 */     float u2 = xTexOffs + depth + width;
/* 293 */     float u22 = xTexOffs + depth + width + width;
/* 294 */     float u3 = xTexOffs + depth + width + depth;
/* 295 */     float u4 = xTexOffs + depth + width + depth + width;
/* 297 */     float v0 = yTexOffs;
/* 298 */     float v1 = yTexOffs + depth;
/* 299 */     float v2 = yTexOffs + depth + height;
/* 301 */     int pos = 0;
/* 302 */     if (visibleFaces.contains(Direction.DOWN))
/* 303 */       this.polygons[pos++] = new ModelPart.Polygon(new ModelPart.Vertex[] { l1, l0, t0, t1 }, u1, v0, u2, v1, xTexSize, yTexSize, mirror, Direction.DOWN); 
/* 305 */     if (visibleFaces.contains(Direction.UP))
/* 306 */       this.polygons[pos++] = new ModelPart.Polygon(new ModelPart.Vertex[] { t2, t3, l3, l2 }, u2, v1, u22, v0, xTexSize, yTexSize, mirror, Direction.UP); 
/* 309 */     if (visibleFaces.contains(Direction.WEST))
/* 310 */       this.polygons[pos++] = new ModelPart.Polygon(new ModelPart.Vertex[] { t0, l0, l3, t3 }, u0, v1, u1, v2, xTexSize, yTexSize, mirror, Direction.WEST); 
/* 312 */     if (visibleFaces.contains(Direction.NORTH))
/* 313 */       this.polygons[pos++] = new ModelPart.Polygon(new ModelPart.Vertex[] { t1, t0, t3, t2 }, u1, v1, u2, v2, xTexSize, yTexSize, mirror, Direction.NORTH); 
/* 315 */     if (visibleFaces.contains(Direction.EAST))
/* 316 */       this.polygons[pos++] = new ModelPart.Polygon(new ModelPart.Vertex[] { l1, t1, t2, l2 }, u2, v1, u3, v2, xTexSize, yTexSize, mirror, Direction.EAST); 
/* 318 */     if (visibleFaces.contains(Direction.SOUTH))
/* 319 */       this.polygons[pos] = new ModelPart.Polygon(new ModelPart.Vertex[] { l0, l1, l2, l3 }, u3, v1, u4, v2, xTexSize, yTexSize, mirror, Direction.SOUTH); 
/*     */   }
/*     */   
/*     */   public void compile(PoseStack.Pose pose, VertexConsumer builder, int lightCoords, int overlayCoords, int color) {
/* 324 */     Matrix4f matrix = pose.pose();
/* 325 */     Vector3f scratchVector = new Vector3f();
/* 326 */     for (ModelPart.Polygon polygon : this.polygons) {
/* 327 */       Vector3f normal = pose.transformNormal(polygon.normal, scratchVector);
/* 328 */       float nx = normal.x();
/* 329 */       float ny = normal.y();
/* 330 */       float nz = normal.z();
/* 332 */       for (ModelPart.Vertex vertex : polygon.vertices) {
/* 333 */         float x = vertex.worldX();
/* 334 */         float y = vertex.worldY();
/* 335 */         float z = vertex.worldZ();
/* 336 */         Vector3f pos = matrix.transformPosition(x, y, z, scratchVector);
/* 337 */         builder.addVertex(pos.x(), pos.y(), pos.z(), color, vertex.u, vertex.v, overlayCoords, lightCoords, nx, ny, nz);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\geom\ModelPart$Cube.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */