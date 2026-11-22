/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.ProjectionType;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ public class Projection {
/*   9 */   private ProjectionType projectionType = ProjectionType.PERSPECTIVE;
/*     */   
/*     */   private float zNear;
/*     */   
/*     */   private float zFar;
/*     */   
/*     */   private float perspectiveFov;
/*     */   
/*     */   private float width;
/*     */   
/*     */   private float height;
/*     */   
/*     */   private boolean orthoInvertY;
/*     */   
/*     */   private boolean isMatrixDirty;
/*     */   
/*  18 */   private final Matrix4f matrix = new Matrix4f();
/*     */   
/*  20 */   private long matrixVersion = -1L;
/*     */   
/*     */   public void setupPerspective(float zNear, float zFar, float fov, float width, float height) {
/*  23 */     if (this.projectionType == ProjectionType.PERSPECTIVE && this.zNear == zNear && this.zFar == zFar && this.perspectiveFov == fov && this.width == width && this.height == height)
/*     */       return; 
/*  27 */     this.isMatrixDirty = true;
/*  28 */     this.projectionType = ProjectionType.PERSPECTIVE;
/*  29 */     this.zNear = zNear;
/*  30 */     this.zFar = zFar;
/*  31 */     this.perspectiveFov = fov;
/*  32 */     this.width = width;
/*  33 */     this.height = height;
/*     */   }
/*     */   
/*     */   public void setupOrtho(float zNear, float zFar, float width, float height, boolean invertY) {
/*  37 */     if (this.projectionType == ProjectionType.ORTHOGRAPHIC && this.zNear == zNear && this.zFar == zFar && this.width == width && this.height == height && this.orthoInvertY == invertY)
/*     */       return; 
/*  41 */     this.isMatrixDirty = true;
/*  42 */     this.projectionType = ProjectionType.ORTHOGRAPHIC;
/*  43 */     this.zNear = zNear;
/*  44 */     this.zFar = zFar;
/*  45 */     this.perspectiveFov = 0.0F;
/*  46 */     this.width = width;
/*  47 */     this.height = height;
/*  48 */     this.orthoInvertY = invertY;
/*     */   }
/*     */   
/*     */   public void setSize(float width, float height) {
/*  52 */     this.isMatrixDirty = true;
/*  53 */     this.width = width;
/*  54 */     this.height = height;
/*     */   }
/*     */   
/*     */   public Matrix4f getMatrix(Matrix4f dest) {
/*  58 */     if (!this.isMatrixDirty)
/*  59 */       return dest.set((Matrix4fc)this.matrix); 
/*  62 */     this.isMatrixDirty = false;
/*  63 */     this.matrixVersion++;
/*  66 */     float near = this.zFar;
/*  67 */     float far = this.zNear;
/*  68 */     boolean zZeroToOne = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
/*  69 */     if (this.projectionType == ProjectionType.PERSPECTIVE)
/*  70 */       return dest.set((Matrix4fc)this.matrix.setPerspective(this.perspectiveFov * 0.017453292F, this.width / this.height, near, far, zZeroToOne)); 
/*  72 */     return dest.set((Matrix4fc)this.matrix.setOrtho(0.0F, this.width, this.orthoInvertY ? this.height : 0.0F, this.orthoInvertY ? 0.0F : this.height, near, far, zZeroToOne));
/*     */   }
/*     */   
/*     */   public long getMatrixVersion() {
/*  78 */     return this.isMatrixDirty ? (this.matrixVersion + 1L) : this.matrixVersion;
/*     */   }
/*     */   
/*     */   public float zNear() {
/*  82 */     return this.zNear;
/*     */   }
/*     */   
/*     */   public float zFar() {
/*  86 */     return this.zFar;
/*     */   }
/*     */   
/*     */   public float width() {
/*  90 */     return this.width;
/*     */   }
/*     */   
/*     */   public float height() {
/*  94 */     return this.height;
/*     */   }
/*     */   
/*     */   public float fov() {
/*  98 */     return this.perspectiveFov;
/*     */   }
/*     */   
/*     */   public boolean invertY() {
/* 102 */     return this.orthoInvertY;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\Projection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */