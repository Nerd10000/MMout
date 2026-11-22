/*     */ package net.minecraft.client.renderer.culling;
/*     */ 
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.joml.FrustumIntersection;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector4f;
/*     */ 
/*     */ public class Frustum {
/*     */   public static final int OFFSET_STEP = 4;
/*     */   
/*  12 */   private final FrustumIntersection intersection = new FrustumIntersection();
/*     */   
/*  13 */   private final Matrix4f matrix = new Matrix4f();
/*     */   
/*     */   private Vector4f viewVector;
/*     */   
/*     */   private double camX;
/*     */   
/*     */   private double camY;
/*     */   
/*     */   private double camZ;
/*     */   
/*     */   public Frustum(Matrix4fc modelView, Matrix4f projection) {
/*  20 */     calculateFrustum(modelView, projection);
/*     */   }
/*     */   
/*     */   public Frustum(Frustum frustum) {
/*  24 */     set(frustum);
/*     */   }
/*     */   
/*     */   public void set(Frustum frustum) {
/*  28 */     this.intersection.set((Matrix4fc)frustum.matrix);
/*  29 */     this.matrix.set((Matrix4fc)frustum.matrix);
/*  30 */     this.camX = frustum.camX;
/*  31 */     this.camY = frustum.camY;
/*  32 */     this.camZ = frustum.camZ;
/*  33 */     this.viewVector = frustum.viewVector;
/*     */   }
/*     */   
/*     */   public Frustum offset(float offset) {
/*  37 */     this.camX += (this.viewVector.x * offset);
/*  38 */     this.camY += (this.viewVector.y * offset);
/*  39 */     this.camZ += (this.viewVector.z * offset);
/*  40 */     return this;
/*     */   }
/*     */   
/*     */   public Frustum offsetToFullyIncludeCameraCube(int cubeSize) {
/*  44 */     double camX1 = Math.floor(this.camX / cubeSize) * cubeSize;
/*  45 */     double camY1 = Math.floor(this.camY / cubeSize) * cubeSize;
/*  46 */     double camZ1 = Math.floor(this.camZ / cubeSize) * cubeSize;
/*  47 */     double camX2 = Math.ceil(this.camX / cubeSize) * cubeSize;
/*  48 */     double camY2 = Math.ceil(this.camY / cubeSize) * cubeSize;
/*  49 */     double camZ2 = Math.ceil(this.camZ / cubeSize) * cubeSize;
/*  50 */     while (this.intersection.intersectAab((float)(camX1 - this.camX), (float)(camY1 - this.camY), (float)(camZ1 - this.camZ), (float)(camX2 - this.camX), (float)(camY2 - this.camY), (float)(camZ2 - this.camZ)) != -2) {
/*  58 */       this.camX -= (this.viewVector.x() * 4.0F);
/*  59 */       this.camY -= (this.viewVector.y() * 4.0F);
/*  60 */       this.camZ -= (this.viewVector.z() * 4.0F);
/*     */     } 
/*  62 */     return this;
/*     */   }
/*     */   
/*     */   public void prepare(double camX, double camY, double camZ) {
/*  66 */     this.camX = camX;
/*  67 */     this.camY = camY;
/*  68 */     this.camZ = camZ;
/*     */   }
/*     */   
/*     */   private void calculateFrustum(Matrix4fc modelView, Matrix4f projection) {
/*  72 */     projection.mul(modelView, this.matrix);
/*  74 */     this.intersection.set((Matrix4fc)this.matrix);
/*  75 */     this.viewVector = this.matrix.transformTranspose(new Vector4f(0.0F, 0.0F, 1.0F, 0.0F));
/*     */   }
/*     */   
/*     */   public boolean isVisible(AABB bb) {
/*  79 */     int intersectionResult = cubeInFrustum(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
/*  80 */     return (intersectionResult == -2 || intersectionResult == -1);
/*     */   }
/*     */   
/*     */   public int cubeInFrustum(BoundingBox bb) {
/*  84 */     return cubeInFrustum(bb.minX(), bb.minY(), bb.minZ(), (bb.maxX() + 1), (bb.maxY() + 1), (bb.maxZ() + 1));
/*     */   }
/*     */   
/*     */   private int cubeInFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
/*  88 */     float x1 = (float)(minX - this.camX);
/*  89 */     float y1 = (float)(minY - this.camY);
/*  90 */     float z1 = (float)(minZ - this.camZ);
/*  91 */     float x2 = (float)(maxX - this.camX);
/*  92 */     float y2 = (float)(maxY - this.camY);
/*  93 */     float z2 = (float)(maxZ - this.camZ);
/*  94 */     return this.intersection.intersectAab(x1, y1, z1, x2, y2, z2);
/*     */   }
/*     */   
/*     */   public boolean pointInFrustum(double x, double y, double z) {
/*  98 */     return this.intersection.testPoint((float)(x - this.camX), (float)(y - this.camY), (float)(z - this.camZ));
/*     */   }
/*     */   
/*     */   public Vector4f[] getFrustumPoints() {
/* 102 */     Vector4f[] frustumPoints = new Vector4f[8];
/* 105 */     frustumPoints[0] = new Vector4f(-1.0F, -1.0F, -1.0F, 1.0F);
/* 106 */     frustumPoints[1] = new Vector4f(1.0F, -1.0F, -1.0F, 1.0F);
/* 107 */     frustumPoints[2] = new Vector4f(1.0F, 1.0F, -1.0F, 1.0F);
/* 108 */     frustumPoints[3] = new Vector4f(-1.0F, 1.0F, -1.0F, 1.0F);
/* 111 */     frustumPoints[4] = new Vector4f(-1.0F, -1.0F, 1.0F, 1.0F);
/* 112 */     frustumPoints[5] = new Vector4f(1.0F, -1.0F, 1.0F, 1.0F);
/* 113 */     frustumPoints[6] = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 114 */     frustumPoints[7] = new Vector4f(-1.0F, 1.0F, 1.0F, 1.0F);
/* 116 */     Matrix4f clipToWorldMatrix = this.matrix.invert(new Matrix4f());
/* 117 */     for (int i = 0; i < 8; i++) {
/* 118 */       clipToWorldMatrix.transform(frustumPoints[i]);
/* 119 */       frustumPoints[i].div(frustumPoints[i].w());
/*     */     } 
/* 122 */     return frustumPoints;
/*     */   }
/*     */   
/*     */   public double getCamX() {
/* 126 */     return this.camX;
/*     */   }
/*     */   
/*     */   public double getCamY() {
/* 130 */     return this.camY;
/*     */   }
/*     */   
/*     */   public double getCamZ() {
/* 134 */     return this.camZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\culling\Frustum.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */