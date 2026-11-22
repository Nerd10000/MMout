/*     */ package net.minecraft.world.level.border;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ class MovingBorderExtent implements WorldBorder.BorderExtent {
/*     */   private final double from;
/*     */   
/*     */   private final double to;
/*     */   
/*     */   private final long lerpEnd;
/*     */   
/*     */   private final long lerpBegin;
/*     */   
/*     */   private final double lerpDuration;
/*     */   
/*     */   private long lerpProgress;
/*     */   
/*     */   private double size;
/*     */   
/*     */   private double previousSize;
/*     */   
/*     */   private MovingBorderExtent(double from, double to, long duration, long gameTime) {
/*  71 */     this.from = from;
/*  72 */     this.to = to;
/*  74 */     this.lerpDuration = duration;
/*  75 */     this.lerpProgress = duration;
/*  76 */     this.lerpBegin = gameTime;
/*  77 */     this.lerpEnd = this.lerpBegin + duration;
/*  78 */     double size = calculateSize();
/*  79 */     this.size = size;
/*  80 */     this.previousSize = size;
/*     */   }
/*     */   
/*     */   public double getMinX(float deltaPartialTick) {
/*  85 */     return Mth.clamp(WorldBorder.this.getCenterX() - Mth.lerp(deltaPartialTick, getPreviousSize(), getSize()) / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/*     */   }
/*     */   
/*     */   public double getMinZ(float deltaPartialTick) {
/*  90 */     return Mth.clamp(WorldBorder.this.getCenterZ() - Mth.lerp(deltaPartialTick, getPreviousSize(), getSize()) / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/*     */   }
/*     */   
/*     */   public double getMaxX(float deltaPartialTick) {
/*  95 */     return Mth.clamp(WorldBorder.this.getCenterX() + Mth.lerp(deltaPartialTick, getPreviousSize(), getSize()) / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/*     */   }
/*     */   
/*     */   public double getMaxZ(float deltaPartialTick) {
/* 100 */     return Mth.clamp(WorldBorder.this.getCenterZ() + Mth.lerp(deltaPartialTick, getPreviousSize(), getSize()) / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/*     */   }
/*     */   
/*     */   public double getSize() {
/* 105 */     return this.size;
/*     */   }
/*     */   
/*     */   public double getPreviousSize() {
/* 109 */     return this.previousSize;
/*     */   }
/*     */   
/*     */   private double calculateSize() {
/* 113 */     double progress = (this.lerpDuration - this.lerpProgress) / this.lerpDuration;
/* 114 */     return (progress < 1.0D) ? Mth.lerp(progress, this.from, this.to) : this.to;
/*     */   }
/*     */   
/*     */   public double getLerpSpeed() {
/* 119 */     return Math.abs(this.from - this.to) / (this.lerpEnd - this.lerpBegin);
/*     */   }
/*     */   
/*     */   public long getLerpTime() {
/* 124 */     return this.lerpProgress;
/*     */   }
/*     */   
/*     */   public double getLerpTarget() {
/* 129 */     return this.to;
/*     */   }
/*     */   
/*     */   public BorderStatus getStatus() {
/* 134 */     return (this.to < this.from) ? BorderStatus.SHRINKING : BorderStatus.GROWING;
/*     */   }
/*     */   
/*     */   public void onCenterChange() {}
/*     */   
/*     */   public void onAbsoluteMaxSizeChange() {}
/*     */   
/*     */   public WorldBorder.BorderExtent update() {
/* 147 */     this.lerpProgress--;
/* 148 */     this.previousSize = this.size;
/* 149 */     this.size = calculateSize();
/* 150 */     WorldBorder.this.setDirty();
/* 151 */     if (this.lerpProgress <= 0L)
/* 152 */       return new WorldBorder.StaticBorderExtent(WorldBorder.this, this.to); 
/* 155 */     return this;
/*     */   }
/*     */   
/*     */   public VoxelShape getCollisionShape() {
/* 160 */     return Shapes.join(Shapes.INFINITY, Shapes.box(
/* 161 */           Math.floor(getMinX(0.0F)), Double.NEGATIVE_INFINITY, Math.floor(getMinZ(0.0F)), 
/* 162 */           Math.ceil(getMaxX(0.0F)), Double.POSITIVE_INFINITY, Math.ceil(getMaxZ(0.0F))), BooleanOp.ONLY_FIRST);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\border\WorldBorder$MovingBorderExtent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */