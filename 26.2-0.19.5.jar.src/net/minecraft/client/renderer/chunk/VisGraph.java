/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
/*     */ import java.util.BitSet;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class VisGraph {
/*     */   private static final int SIZE_IN_BITS = 4;
/*     */   
/*     */   private static final int LEN = 16;
/*     */   
/*     */   private static final int MASK = 15;
/*     */   
/*     */   private static final int SIZE = 4096;
/*     */   
/*     */   private static final int X_SHIFT = 0;
/*     */   
/*     */   private static final int Z_SHIFT = 4;
/*     */   
/*     */   private static final int Y_SHIFT = 8;
/*     */   
/*  22 */   private static final int DX = (int)Math.pow(16.0D, 0.0D);
/*     */   
/*  23 */   private static final int DZ = (int)Math.pow(16.0D, 1.0D);
/*     */   
/*  24 */   private static final int DY = (int)Math.pow(16.0D, 2.0D);
/*     */   
/*     */   private static final int INVALID_INDEX = -1;
/*     */   
/*  26 */   private static final Direction[] DIRECTIONS = Direction.values();
/*     */   
/*  28 */   private final BitSet bitSet = new BitSet(4096);
/*     */   
/*     */   private static final int[] INDEX_OF_EDGES;
/*     */   
/*     */   static {
/*  30 */     INDEX_OF_EDGES = (int[])Util.make(new int[1352], map -> {
/*     */           int min = 0;
/*     */           int max = 15;
/*     */           int index = 0;
/*     */           for (int x = 0; x < 16; x++) {
/*     */             for (int y = 0; y < 16; y++) {
/*     */               for (int z = 0; z < 16; z++) {
/*     */                 if (x == 0 || x == 15 || y == 0 || y == 15 || z == 0 || z == 15)
/*     */                   map[index++] = getIndex(x, y, z); 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*  46 */   private int empty = 4096;
/*     */   
/*     */   public void setOpaque(BlockPos pos) {
/*  49 */     this.bitSet.set(getIndex(pos), true);
/*  50 */     this.empty--;
/*     */   }
/*     */   
/*     */   private static int getIndex(BlockPos pos) {
/*  54 */     return getIndex(pos.getX() & 0xF, pos.getY() & 0xF, pos.getZ() & 0xF);
/*     */   }
/*     */   
/*     */   private static int getIndex(int x, int y, int z) {
/*  58 */     return x << 0 | y << 8 | z << 4;
/*     */   }
/*     */   
/*     */   public VisibilitySet resolve() {
/*  62 */     VisibilitySet visibilitySet = new VisibilitySet();
/*  64 */     if (4096 - this.empty < 256) {
/*  65 */       visibilitySet.setAll(true);
/*  66 */     } else if (this.empty == 0) {
/*  67 */       visibilitySet.setAll(false);
/*     */     } else {
/*  69 */       for (int i : INDEX_OF_EDGES) {
/*  70 */         if (!this.bitSet.get(i))
/*  71 */           visibilitySet.add(floodFill(i)); 
/*     */       } 
/*     */     } 
/*  76 */     return visibilitySet;
/*     */   }
/*     */   
/*     */   private Set<Direction> floodFill(int startIndex) {
/*  80 */     Set<Direction> edges = EnumSet.noneOf(Direction.class);
/*  82 */     IntArrayFIFOQueue intArrayFIFOQueue = new IntArrayFIFOQueue();
/*  83 */     intArrayFIFOQueue.enqueue(startIndex);
/*  84 */     this.bitSet.set(startIndex, true);
/*  86 */     while (!intArrayFIFOQueue.isEmpty()) {
/*  87 */       int index = intArrayFIFOQueue.dequeueInt();
/*  88 */       addEdges(index, edges);
/*  90 */       for (Direction direction : DIRECTIONS) {
/*  91 */         int neighborIndex = getNeighborIndexAtFace(index, direction);
/*  92 */         if (neighborIndex >= 0 && !this.bitSet.get(neighborIndex)) {
/*  93 */           this.bitSet.set(neighborIndex, true);
/*  94 */           intArrayFIFOQueue.enqueue(neighborIndex);
/*     */         } 
/*     */       } 
/*     */     } 
/*  98 */     return edges;
/*     */   }
/*     */   
/*     */   private void addEdges(int index, Set<Direction> edges) {
/* 102 */     int x = index >> 0 & 0xF;
/* 103 */     if (x == 0) {
/* 104 */       edges.add(Direction.WEST);
/* 105 */     } else if (x == 15) {
/* 106 */       edges.add(Direction.EAST);
/*     */     } 
/* 109 */     int y = index >> 8 & 0xF;
/* 110 */     if (y == 0) {
/* 111 */       edges.add(Direction.DOWN);
/* 112 */     } else if (y == 15) {
/* 113 */       edges.add(Direction.UP);
/*     */     } 
/* 116 */     int z = index >> 4 & 0xF;
/* 117 */     if (z == 0) {
/* 118 */       edges.add(Direction.NORTH);
/* 119 */     } else if (z == 15) {
/* 120 */       edges.add(Direction.SOUTH);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getNeighborIndexAtFace(int index, Direction direction) {
/* 125 */     switch (direction) {
/*     */       default:
/* 125 */         throw new MatchException(null, null);
/*     */       case DOWN:
/* 125 */         return 
/*     */           
/* 127 */           ((index >> 8 & 0xF) == 0) ? 
/* 128 */           -1 : (
/*     */           
/* 130 */           index - DY);
/*     */       case UP:
/* 133 */         return ((index >> 8 & 0xF) == 15) ? 
/* 134 */           -1 : (
/*     */           
/* 136 */           index + DY);
/*     */       case NORTH:
/* 139 */         return ((index >> 4 & 0xF) == 0) ? 
/* 140 */           -1 : (
/*     */           
/* 142 */           index - DZ);
/*     */       case SOUTH:
/* 145 */         return ((index >> 4 & 0xF) == 15) ? 
/* 146 */           -1 : (
/*     */           
/* 148 */           index + DZ);
/*     */       case WEST:
/* 151 */         return ((index >> 0 & 0xF) == 0) ? 
/* 152 */           -1 : (
/*     */           
/* 154 */           index - DX);
/*     */       case EAST:
/*     */         break;
/*     */     } 
/* 157 */     return ((index >> 0 & 0xF) == 15) ? 
/* 158 */       -1 : (
/*     */       
/* 160 */       index + DX);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\VisGraph.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */