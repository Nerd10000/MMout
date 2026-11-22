/*     */ package net.minecraft.world.level.border;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ class StaticBorderExtent implements WorldBorder.BorderExtent {
/*     */   private final double size;
/*     */   
/*     */   private double minX;
/*     */   
/*     */   private double minZ;
/*     */   
/*     */   private double maxX;
/*     */   
/*     */   private double maxZ;
/*     */   
/*     */   private VoxelShape shape;
/*     */   
/*     */   public StaticBorderExtent(double size) {
/* 178 */     this.size = size;
/* 179 */     updateBox();
/*     */   }
/*     */   
/*     */   public double getMinX(float deltaPartialTick) {
/* 184 */     return this.minX;
/*     */   }
/*     */   
/*     */   public double getMaxX(float deltaPartialTick) {
/* 189 */     return this.maxX;
/*     */   }
/*     */   
/*     */   public double getMinZ(float deltaPartialTick) {
/* 194 */     return this.minZ;
/*     */   }
/*     */   
/*     */   public double getMaxZ(float deltaPartialTick) {
/* 199 */     return this.maxZ;
/*     */   }
/*     */   
/*     */   public double getSize() {
/* 204 */     return this.size;
/*     */   }
/*     */   
/*     */   public BorderStatus getStatus() {
/* 209 */     return BorderStatus.STATIONARY;
/*     */   }
/*     */   
/*     */   public double getLerpSpeed() {
/* 214 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public long getLerpTime() {
/* 219 */     return 0L;
/*     */   }
/*     */   
/*     */   public double getLerpTarget() {
/* 224 */     return this.size;
/*     */   }
/*     */   
/*     */   private void updateBox() {
/* 228 */     this.minX = Mth.clamp(WorldBorder.this.getCenterX() - this.size / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/* 229 */     this.minZ = Mth.clamp(WorldBorder.this.getCenterZ() - this.size / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/* 230 */     this.maxX = Mth.clamp(WorldBorder.this.getCenterX() + this.size / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/* 231 */     this.maxZ = Mth.clamp(WorldBorder.this.getCenterZ() + this.size / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/* 233 */     this.shape = Shapes.join(Shapes.INFINITY, Shapes.box(
/* 234 */           Math.floor(getMinX(0.0F)), Double.NEGATIVE_INFINITY, Math.floor(getMinZ(0.0F)), 
/* 235 */           Math.ceil(getMaxX(0.0F)), Double.POSITIVE_INFINITY, Math.ceil(getMaxZ(0.0F))), BooleanOp.ONLY_FIRST);
/*     */   }
/*     */   
/*     */   public void onAbsoluteMaxSizeChange() {
/* 241 */     updateBox();
/*     */   }
/*     */   
/*     */   public void onCenterChange() {
/* 246 */     updateBox();
/*     */   }
/*     */   
/*     */   public WorldBorder.BorderExtent update() {
/* 251 */     return this;
/*     */   }
/*     */   
/*     */   public VoxelShape getCollisionShape() {
/* 256 */     return this.shape;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\border\WorldBorder$StaticBorderExtent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */