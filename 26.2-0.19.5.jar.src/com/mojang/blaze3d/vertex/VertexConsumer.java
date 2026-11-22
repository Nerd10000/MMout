/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import net.minecraft.client.model.geom.builders.UVPair;
/*     */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*     */ import org.joml.Matrix3x2fc;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector2f;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public interface VertexConsumer {
/*     */   VertexConsumer addVertex(float paramFloat1, float paramFloat2, float paramFloat3);
/*     */   
/*     */   VertexConsumer setColor(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */   
/*     */   VertexConsumer setColor(int paramInt);
/*     */   
/*     */   VertexConsumer setUv(float paramFloat1, float paramFloat2);
/*     */   
/*     */   VertexConsumer setUv1(int paramInt1, int paramInt2);
/*     */   
/*     */   VertexConsumer setUv2(int paramInt1, int paramInt2);
/*     */   
/*     */   VertexConsumer setNormal(float paramFloat1, float paramFloat2, float paramFloat3);
/*     */   
/*     */   VertexConsumer setLineWidth(float paramFloat);
/*     */   
/*     */   default void addVertex(float x, float y, float z, int color, float u, float v, int overlayCoords, int lightCoords, float nx, float ny, float nz) {
/*  30 */     addVertex(x, y, z);
/*  31 */     setColor(color);
/*  32 */     setUv(u, v);
/*  33 */     setOverlay(overlayCoords);
/*  34 */     setLight(lightCoords);
/*  35 */     setNormal(nx, ny, nz);
/*     */   }
/*     */   
/*     */   default VertexConsumer setColor(float r, float g, float b, float a) {
/*  39 */     return setColor((int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F), (int)(a * 255.0F));
/*     */   }
/*     */   
/*     */   default VertexConsumer setLight(int packedLightCoords) {
/*  43 */     return setUv2(packedLightCoords & 0xFFFF, packedLightCoords >> 16 & 0xFFFF);
/*     */   }
/*     */   
/*     */   default VertexConsumer setOverlay(int packedOverlayCoords) {
/*  47 */     return setUv1(packedOverlayCoords & 0xFFFF, packedOverlayCoords >> 16 & 0xFFFF);
/*     */   }
/*     */   
/*     */   default void putBlockBakedQuad(float x, float y, float z, BakedQuad quad, QuadInstance instance) {
/*  51 */     Vector3fc normal = quad.direction().getUnitVec3f();
/*  52 */     int lightEmission = quad.materialInfo().lightEmission();
/*  54 */     for (int vertex = 0; vertex < 4; vertex++) {
/*  55 */       Vector3fc pos = quad.position(vertex);
/*  56 */       long packedUv = quad.packedUV(vertex);
/*  58 */       int vertexColor = instance.getColor(vertex);
/*  59 */       int light = instance.getLightCoordsWithEmission(vertex, lightEmission);
/*  61 */       float u = UVPair.unpackU(packedUv);
/*  62 */       float v = UVPair.unpackV(packedUv);
/*  63 */       addVertex(pos.x() + x, pos.y() + y, pos.z() + z, vertexColor, u, v, instance.overlayCoords(), light, normal.x(), normal.y(), normal.z());
/*     */     } 
/*     */   }
/*     */   
/*     */   default void putBakedQuad(PoseStack.Pose pose, BakedQuad quad, QuadInstance instance) {
/*  68 */     Vector3fc normalVec = quad.direction().getUnitVec3f();
/*  70 */     Matrix4f matrix = pose.pose();
/*  71 */     Vector3f normal = pose.transformNormal(normalVec, new Vector3f());
/*  73 */     int lightEmission = quad.materialInfo().lightEmission();
/*  75 */     for (int vertex = 0; vertex < 4; vertex++) {
/*  76 */       Vector3fc position = quad.position(vertex);
/*  77 */       long packedUv = quad.packedUV(vertex);
/*  79 */       int vertexColor = instance.getColor(vertex);
/*  80 */       int light = instance.getLightCoordsWithEmission(vertex, lightEmission);
/*  82 */       Vector3f pos = matrix.transformPosition(position, new Vector3f());
/*  83 */       float u = UVPair.unpackU(packedUv);
/*  84 */       float v = UVPair.unpackV(packedUv);
/*  85 */       addVertex(pos.x(), pos.y(), pos.z(), vertexColor, u, v, instance.overlayCoords(), light, normal.x(), normal.y(), normal.z());
/*     */     } 
/*     */   }
/*     */   
/*     */   default VertexConsumer addVertex(Vector3fc position) {
/*  90 */     return addVertex(position.x(), position.y(), position.z());
/*     */   }
/*     */   
/*     */   default VertexConsumer addVertex(PoseStack.Pose pose, Vector3fc position) {
/*  94 */     return addVertex(pose, position.x(), position.y(), position.z());
/*     */   }
/*     */   
/*     */   default VertexConsumer addVertex(PoseStack.Pose pose, float x, float y, float z) {
/*  98 */     return addVertex((Matrix4fc)pose.pose(), x, y, z);
/*     */   }
/*     */   
/*     */   default VertexConsumer addVertex(Matrix4fc pose, float x, float y, float z) {
/* 102 */     Vector3f pos = pose.transformPosition(x, y, z, new Vector3f());
/* 103 */     return addVertex(pos.x(), pos.y(), pos.z());
/*     */   }
/*     */   
/*     */   default VertexConsumer addVertexWith2DPose(Matrix3x2fc pose, float x, float y) {
/* 107 */     Vector2f pos = pose.transformPosition(x, y, new Vector2f());
/* 108 */     return addVertex(pos.x(), pos.y(), 0.0F);
/*     */   }
/*     */   
/*     */   default VertexConsumer setNormal(PoseStack.Pose pose, float x, float y, float z) {
/* 112 */     Vector3f normal = pose.transformNormal(x, y, z, new Vector3f());
/* 113 */     return setNormal(normal.x(), normal.y(), normal.z());
/*     */   }
/*     */   
/*     */   default VertexConsumer setNormal(PoseStack.Pose pose, Vector3fc normal) {
/* 117 */     return setNormal(pose, normal.x(), normal.y(), normal.z());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\VertexConsumer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */