/*     */ package net.minecraft.world.phys.shapes;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import net.minecraft.core.Direction;
/*     */ 
/*     */ public final class BitSetDiscreteVoxelShape extends DiscreteVoxelShape {
/*     */   private final BitSet storage;
/*     */   
/*     */   private int xMin;
/*     */   
/*     */   private int yMin;
/*     */   
/*     */   private int zMin;
/*     */   
/*     */   private int xMax;
/*     */   
/*     */   private int yMax;
/*     */   
/*     */   private int zMax;
/*     */   
/*     */   public BitSetDiscreteVoxelShape(int xSize, int ySize, int zSize) {
/*  18 */     super(xSize, ySize, zSize);
/*  19 */     this.storage = new BitSet(xSize * ySize * zSize);
/*  20 */     this.xMin = xSize;
/*  21 */     this.yMin = ySize;
/*  22 */     this.zMin = zSize;
/*     */   }
/*     */   
/*     */   public static BitSetDiscreteVoxelShape withFilledBounds(int xSize, int ySize, int zSize, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax) {
/*  26 */     BitSetDiscreteVoxelShape shape = new BitSetDiscreteVoxelShape(xSize, ySize, zSize);
/*  28 */     shape.xMin = xMin;
/*  29 */     shape.yMin = yMin;
/*  30 */     shape.zMin = zMin;
/*  31 */     shape.xMax = xMax;
/*  32 */     shape.yMax = yMax;
/*  33 */     shape.zMax = zMax;
/*  35 */     for (int x = xMin; x < xMax; x++) {
/*  36 */       for (int y = yMin; y < yMax; y++) {
/*  37 */         for (int z = zMin; z < zMax; z++)
/*  38 */           shape.fillUpdateBounds(x, y, z, false); 
/*     */       } 
/*     */     } 
/*  43 */     return shape;
/*     */   }
/*     */   
/*     */   public BitSetDiscreteVoxelShape(DiscreteVoxelShape voxelShape) {
/*  47 */     super(voxelShape.xSize, voxelShape.ySize, voxelShape.zSize);
/*  48 */     if (voxelShape instanceof BitSetDiscreteVoxelShape) {
/*  48 */       BitSetDiscreteVoxelShape bitSetDiscreteVoxelShape = (BitSetDiscreteVoxelShape)voxelShape;
/*  49 */       this.storage = (BitSet)bitSetDiscreteVoxelShape.storage.clone();
/*     */     } else {
/*  51 */       this.storage = new BitSet(this.xSize * this.ySize * this.zSize);
/*  52 */       for (int x = 0; x < this.xSize; x++) {
/*  53 */         for (int y = 0; y < this.ySize; y++) {
/*  54 */           for (int z = 0; z < this.zSize; z++) {
/*  55 */             if (voxelShape.isFull(x, y, z))
/*  56 */               this.storage.set(getIndex(x, y, z)); 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  63 */     this.xMin = voxelShape.firstFull(Direction.Axis.X);
/*  64 */     this.yMin = voxelShape.firstFull(Direction.Axis.Y);
/*  65 */     this.zMin = voxelShape.firstFull(Direction.Axis.Z);
/*  67 */     this.xMax = voxelShape.lastFull(Direction.Axis.X);
/*  68 */     this.yMax = voxelShape.lastFull(Direction.Axis.Y);
/*  69 */     this.zMax = voxelShape.lastFull(Direction.Axis.Z);
/*     */   }
/*     */   
/*     */   private int getIndex(int x, int y, int z) {
/*  73 */     return (x * this.ySize + y) * this.zSize + z;
/*     */   }
/*     */   
/*     */   public boolean isFull(int x, int y, int z) {
/*  78 */     return this.storage.get(getIndex(x, y, z));
/*     */   }
/*     */   
/*     */   private void fillUpdateBounds(int x, int y, int z, boolean updateBounds) {
/*  82 */     this.storage.set(getIndex(x, y, z));
/*  84 */     if (updateBounds) {
/*  85 */       this.xMin = Math.min(this.xMin, x);
/*  86 */       this.yMin = Math.min(this.yMin, y);
/*  87 */       this.zMin = Math.min(this.zMin, z);
/*  89 */       this.xMax = Math.max(this.xMax, x + 1);
/*  90 */       this.yMax = Math.max(this.yMax, y + 1);
/*  91 */       this.zMax = Math.max(this.zMax, z + 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void fill(int x, int y, int z) {
/*  97 */     fillUpdateBounds(x, y, z, true);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 102 */     return this.storage.isEmpty();
/*     */   }
/*     */   
/*     */   public int firstFull(Direction.Axis axis) {
/* 107 */     return axis.choose(this.xMin, this.yMin, this.zMin);
/*     */   }
/*     */   
/*     */   public int lastFull(Direction.Axis axis) {
/* 112 */     return axis.choose(this.xMax, this.yMax, this.zMax);
/*     */   }
/*     */   
/*     */   public static BitSetDiscreteVoxelShape join(DiscreteVoxelShape first, DiscreteVoxelShape second, IndexMerger xMerger, IndexMerger yMerger, IndexMerger zMerger, BooleanOp op) {
/* 116 */     BitSetDiscreteVoxelShape shape = new BitSetDiscreteVoxelShape(xMerger.size() - 1, yMerger.size() - 1, zMerger.size() - 1);
/* 117 */     int[] bounds = { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE };
/* 126 */     xMerger.forMergedIndexes((x1, x2, xr) -> {
/*     */           boolean[] updatedSlice = { false };
/*     */           yMerger.forMergedIndexes(());
/*     */           if (updatedSlice[0]) {
/*     */             bounds[0] = Math.min(bounds[0], xr);
/*     */             bounds[3] = Math.max(bounds[3], xr);
/*     */           } 
/*     */           return true;
/*     */         });
/* 152 */     shape.xMin = bounds[0];
/* 153 */     shape.yMin = bounds[1];
/* 154 */     shape.zMin = bounds[2];
/* 155 */     shape.xMax = bounds[3] + 1;
/* 156 */     shape.yMax = bounds[4] + 1;
/* 157 */     shape.zMax = bounds[5] + 1;
/* 158 */     return shape;
/*     */   }
/*     */   
/*     */   static void forAllBoxes(DiscreteVoxelShape voxelShape, DiscreteVoxelShape.IntLineConsumer consumer, boolean mergeNeighbors) {
/* 165 */     BitSetDiscreteVoxelShape shape = new BitSetDiscreteVoxelShape(voxelShape);
/* 166 */     for (int y = 0; y < shape.ySize; y++) {
/* 167 */       for (int x = 0; x < shape.xSize; x++) {
/* 168 */         int lastStartZ = -1;
/* 169 */         for (int z = 0; z <= shape.zSize; z++) {
/* 170 */           if (shape.isFullWide(x, y, z)) {
/* 171 */             if (mergeNeighbors) {
/* 173 */               if (lastStartZ == -1)
/* 174 */                 lastStartZ = z; 
/*     */             } else {
/* 177 */               consumer.consume(x, y, z, x + 1, y + 1, z + 1);
/*     */             } 
/* 179 */           } else if (lastStartZ != -1) {
/* 182 */             int endX = x;
/* 183 */             int endY = y;
/* 186 */             shape.clearZStrip(lastStartZ, z, x, y);
/* 189 */             while (shape.isZStripFull(lastStartZ, z, endX + 1, y)) {
/* 190 */               shape.clearZStrip(lastStartZ, z, endX + 1, y);
/* 191 */               endX++;
/*     */             } 
/* 195 */             while (shape.isXZRectangleFull(x, endX + 1, lastStartZ, z, endY + 1)) {
/* 196 */               for (int cx = x; cx <= endX; cx++)
/* 197 */                 shape.clearZStrip(lastStartZ, z, cx, endY + 1); 
/* 199 */               endY++;
/*     */             } 
/* 202 */             consumer.consume(x, y, lastStartZ, endX + 1, endY + 1, z);
/* 203 */             lastStartZ = -1;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isZStripFull(int startZ, int endZ, int x, int y) {
/* 212 */     if (x >= this.xSize || y >= this.ySize)
/* 213 */       return false; 
/* 215 */     return (this.storage.nextClearBit(getIndex(x, y, startZ)) >= getIndex(x, y, endZ));
/*     */   }
/*     */   
/*     */   private boolean isXZRectangleFull(int startX, int endX, int startZ, int endZ, int y) {
/* 219 */     for (int x = startX; x < endX; x++) {
/* 220 */       if (!isZStripFull(startZ, endZ, x, y))
/* 221 */         return false; 
/*     */     } 
/* 224 */     return true;
/*     */   }
/*     */   
/*     */   private void clearZStrip(int startZ, int endZ, int x, int y) {
/* 229 */     this.storage.clear(getIndex(x, y, startZ), getIndex(x, y, endZ));
/*     */   }
/*     */   
/*     */   public boolean isInterior(int x, int y, int z) {
/* 234 */     boolean isInterior = (x > 0 && x < this.xSize - 1 && y > 0 && y < this.ySize - 1 && z > 0 && z < this.zSize - 1);
/* 236 */     return (isInterior && isFull(x, y, z) && 
/* 237 */       isFull(x - 1, y, z) && 
/* 238 */       isFull(x + 1, y, z) && 
/* 239 */       isFull(x, y - 1, z) && 
/* 240 */       isFull(x, y + 1, z) && 
/* 241 */       isFull(x, y, z - 1) && 
/* 242 */       isFull(x, y, z + 1));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\shapes\BitSetDiscreteVoxelShape.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */