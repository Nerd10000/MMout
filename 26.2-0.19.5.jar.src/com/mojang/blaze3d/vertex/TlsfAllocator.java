/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TlsfAllocator {
/*     */   private static final int SECOND_LEVEL_BIN_LOG2 = 3;
/*     */   
/*     */   private static final int SECOND_LEVEL_BIN_COUNT = 8;
/*     */   
/*     */   private static final int FIRST_LEVEL_INDEX_SHIFT = 8;
/*     */   
/*     */   private static final int FIRST_LEVEL_BIN_COUNT = 32;
/*     */   
/*     */   private static final int SMALL_BLOCK_SIZE = 256;
/*     */   
/*     */   private static final long MAX_ALLOCATION_SIZE = 549755813888L;
/*     */   
/*     */   private static final int ALIGN_SIZE = 32;
/*     */   
/*     */   public static class Allocation {
/*     */     private final TlsfAllocator.Block block;
/*     */     
/*     */     private final long offsetFromHeap;
/*     */     
/*     */     private boolean freed = false;
/*     */     
/*     */     private Allocation(TlsfAllocator.Block block, long offsetFromHeap) {
/*  27 */       this.block = block;
/*  28 */       this.offsetFromHeap = offsetFromHeap;
/*     */     }
/*     */     
/*     */     public long getSize() {
/*  32 */       return this.block.getSize();
/*     */     }
/*     */     
/*     */     public TlsfAllocator.Heap getHeap() {
/*  36 */       return this.block.heap;
/*     */     }
/*     */     
/*     */     public long getOffsetFromHeap() {
/*  40 */       return this.offsetFromHeap;
/*     */     }
/*     */     
/*     */     public boolean isFreed() {
/*  44 */       return this.freed;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Block {
/*  48 */     private long size = 0L;
/*     */     
/*     */     public final TlsfAllocator.Heap heap;
/*     */     
/*     */     public long offsetFromHeap;
/*     */     
/*     */     public Block nextFreeBlock;
/*     */     
/*     */     public Block previousFreeBlock;
/*     */     
/*     */     public Block nextPhysicalBlock;
/*     */     
/*     */     public Block previousPhysicalBlock;
/*     */     
/*     */     private static final int BLOCK_HEADER_FREE_BIT = 1;
/*     */     
/*     */     private Block(long size, TlsfAllocator.Heap heap, long offsetFromHeap, Block nextFreeBlock, Block previousFreeBlock, Block nextPhysicalBlock, Block previousPhysicalBlock) {
/*  64 */       this.heap = heap;
/*  65 */       this.offsetFromHeap = offsetFromHeap;
/*  66 */       this.nextFreeBlock = nextFreeBlock;
/*  67 */       this.previousFreeBlock = previousFreeBlock;
/*  68 */       this.nextPhysicalBlock = nextPhysicalBlock;
/*  69 */       this.previousPhysicalBlock = previousPhysicalBlock;
/*  70 */       setSize(size);
/*     */     }
/*     */     
/*     */     private boolean isFree() {
/*  74 */       return ((this.size & 0x1L) == 1L);
/*     */     }
/*     */     
/*     */     private void setFree() {
/*  78 */       this.size |= 0x1L;
/*     */     }
/*     */     
/*     */     private void setUsed() {
/*  82 */       this.size &= 0xFFFFFFFFFFFFFFFEL;
/*     */     }
/*     */     
/*     */     private long getSize() {
/*  86 */       return this.size & 0xFFFFFFFFFFFFFFFEL;
/*     */     }
/*     */     
/*     */     private void setSize(long size) {
/*  90 */       long oldSize = this.size;
/*  91 */       this.size = size | oldSize & 0x1L;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Heap {
/*     */     private final long size;
/*     */     
/*     */     public Heap(long size) {
/*  99 */       this.size = size;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class IndexPair extends Record {
/*     */     private final int firstLevelIndex;
/*     */     
/*     */     private final int secondLevelIndex;
/*     */     
/*     */     private IndexPair(int firstLevelIndex, int secondLevelIndex) {
/* 103 */       this.firstLevelIndex = firstLevelIndex;
/* 103 */       this.secondLevelIndex = secondLevelIndex;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vertex/TlsfAllocator$IndexPair;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #103	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vertex/TlsfAllocator$IndexPair;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vertex/TlsfAllocator$IndexPair;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #103	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vertex/TlsfAllocator$IndexPair;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vertex/TlsfAllocator$IndexPair;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #103	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/vertex/TlsfAllocator$IndexPair;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int firstLevelIndex() {
/* 103 */       return this.firstLevelIndex;
/*     */     }
/*     */     
/*     */     public int secondLevelIndex() {
/* 103 */       return this.secondLevelIndex;
/*     */     }
/*     */   }
/*     */   
/* 106 */   private int firstLevelBitmap = 0;
/*     */   
/* 107 */   private final int[] secondLevelBitmap = new int[32];
/*     */   
/* 108 */   private final Block[] freeLists = new Block[256];
/*     */   
/*     */   private final long totalMemorySize;
/*     */   
/* 110 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public TlsfAllocator(Heap heap) {
/* 113 */     long alignedHeapSize = alignDown(heap.size, 32L);
/* 114 */     Block freeBlock = new Block(alignedHeapSize, heap, 0L, null, null, null, null);
/* 115 */     freeBlock.setFree();
/* 116 */     insertFreeBlock(freeBlock);
/* 118 */     long remainingHeapSize = heap.size - alignedHeapSize;
/* 119 */     Block sentinelBlock = new Block(remainingHeapSize, heap, alignedHeapSize, null, null, null, freeBlock);
/* 120 */     sentinelBlock.setUsed();
/* 121 */     freeBlock.nextPhysicalBlock = sentinelBlock;
/* 122 */     this.totalMemorySize = alignedHeapSize;
/*     */   }
/*     */   
/*     */   private Block getBlockFromFreeList(int firstLevelIndex, int secondLevelIndex) {
/* 126 */     return this.freeLists[firstLevelIndex * 8 + secondLevelIndex];
/*     */   }
/*     */   
/*     */   private void setBlockFreeList(int firstLevelIndex, int secondLevelIndex, Block block) {
/* 130 */     this.freeLists[firstLevelIndex * 8 + secondLevelIndex] = block;
/*     */   }
/*     */   
/*     */   private static long alignUp(long x, long align) {
/* 134 */     return x + align - 1L & (align - 1L ^ 0xFFFFFFFFFFFFFFFFL);
/*     */   }
/*     */   
/*     */   private static long alignDown(long x, long align) {
/* 138 */     return x - (x & align - 1L);
/*     */   }
/*     */   
/*     */   private static int findLastSignificantBit(long x) {
/* 142 */     return 63 - Long.numberOfLeadingZeros(x);
/*     */   }
/*     */   
/*     */   private static int findFirstSignificantBit(int x) {
/* 146 */     return Integer.numberOfTrailingZeros(x);
/*     */   }
/*     */   
/*     */   private IndexPair getLevelIndex(long size) {
/* 150 */     if (Long.compareUnsigned(size, 256L) < 0) {
/* 151 */       int i = 0;
/* 152 */       int j = (int)Long.divideUnsigned(size, 32L);
/* 153 */       return new IndexPair(0, j);
/*     */     } 
/* 155 */     int firstLevelIndex = findLastSignificantBit(size);
/* 156 */     int secondLevelIndex = (int)(size >>> firstLevelIndex - 3) ^ 0x8;
/* 157 */     firstLevelIndex -= 7;
/* 158 */     return new IndexPair(firstLevelIndex, secondLevelIndex);
/*     */   }
/*     */   
/*     */   private IndexPair mappingSearch(long size) {
/* 163 */     long roundedSize = size;
/* 164 */     if (Long.compareUnsigned(size, 256L) >= 0) {
/* 165 */       long round = (1L << findLastSignificantBit(size) - 3) - 1L;
/* 166 */       roundedSize += round;
/*     */     } 
/* 169 */     return getLevelIndex(roundedSize);
/*     */   }
/*     */   
/*     */   private void insertFreeBlock(Block block) {
/* 173 */     IndexPair levelIndex = getLevelIndex(block.getSize());
/* 175 */     Block currentBlock = getBlockFromFreeList(levelIndex.firstLevelIndex, levelIndex.secondLevelIndex);
/* 176 */     if (currentBlock != null)
/* 177 */       currentBlock.previousFreeBlock = block; 
/* 179 */     block.nextFreeBlock = currentBlock;
/* 181 */     this.firstLevelBitmap |= 1 << levelIndex.firstLevelIndex;
/* 182 */     this.secondLevelBitmap[levelIndex.firstLevelIndex] = this.secondLevelBitmap[levelIndex.firstLevelIndex] | 1 << levelIndex.secondLevelIndex;
/* 183 */     setBlockFreeList(levelIndex.firstLevelIndex, levelIndex.secondLevelIndex, block);
/*     */   }
/*     */   
/*     */   private void removeFreeBlock(Block block, int firstLevel, int secondLevel) {
/* 187 */     Block next = block.nextFreeBlock;
/* 188 */     Block previous = block.previousFreeBlock;
/* 190 */     if (previous != null)
/* 191 */       previous.nextFreeBlock = next; 
/* 193 */     if (next != null)
/* 194 */       next.previousFreeBlock = previous; 
/* 197 */     if (getBlockFromFreeList(firstLevel, secondLevel) == block) {
/* 198 */       setBlockFreeList(firstLevel, secondLevel, next);
/* 199 */       if (next == null) {
/* 201 */         this.secondLevelBitmap[firstLevel] = this.secondLevelBitmap[firstLevel] & (1 << secondLevel ^ 0xFFFFFFFF);
/* 202 */         if (this.secondLevelBitmap[firstLevel] == 0)
/* 203 */           this.firstLevelBitmap &= 1 << firstLevel ^ 0xFFFFFFFF; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void trimBlock(Block block, long size) {
/* 210 */     if (Long.compareUnsigned(block.getSize(), size + 256L) > 0) {
/* 211 */       long remaining = block.getSize() - size;
/* 212 */       Block remainingBlock = new Block(remaining, block.heap, block.offsetFromHeap + size, null, null, block.nextPhysicalBlock, block);
/* 213 */       remainingBlock.setFree();
/* 215 */       Block next = block.nextPhysicalBlock;
/* 216 */       if (next != null) {
/* 217 */         assert next.previousPhysicalBlock == block;
/* 218 */         next.previousPhysicalBlock = remainingBlock;
/*     */       } 
/* 221 */       block.nextPhysicalBlock = remainingBlock;
/* 222 */       block.setSize(size);
/* 224 */       mergeBlockWithNext(remainingBlock);
/* 225 */       insertFreeBlock(remainingBlock);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Allocation allocate(long size, int align) {
/* 231 */     boolean isPowerOfTwo = Mth.isPowerOfTwo(align);
/* 234 */     int sizePadding = (!isPowerOfTwo || align > 32) ? align : 0;
/* 236 */     long alignedSize = alignUp(size + sizePadding, 32L);
/* 237 */     assert alignedSize <= 549755813888L;
/* 239 */     IndexPair levelIndex = mappingSearch(alignedSize);
/* 240 */     int firstLevelIndex = levelIndex.firstLevelIndex;
/* 241 */     int secondLevelIndex = levelIndex.secondLevelIndex;
/* 245 */     long firstLevelSize = 1L << firstLevelIndex + 8 - 1;
/* 246 */     long secondLevelInterval = firstLevelSize / 8L;
/* 247 */     long slotSize = firstLevelSize + secondLevelIndex * secondLevelInterval;
/* 249 */     if (firstLevelIndex < 32) {
/* 250 */       int slBitmap = this.secondLevelBitmap[firstLevelIndex] & -1 << secondLevelIndex;
/* 251 */       if (slBitmap == 0) {
/* 254 */         int flBitmap = this.firstLevelBitmap & -1 << firstLevelIndex + 1;
/* 255 */         if (flBitmap == 0)
/* 257 */           return null; 
/* 260 */         firstLevelIndex = findFirstSignificantBit(flBitmap);
/* 261 */         slBitmap = this.secondLevelBitmap[firstLevelIndex];
/*     */       } 
/* 264 */       secondLevelIndex = findFirstSignificantBit(slBitmap);
/* 265 */       Block block = getBlockFromFreeList(firstLevelIndex, secondLevelIndex);
/* 266 */       assert block != null && block.getSize() >= alignedSize;
/* 268 */       removeFreeBlock(block, firstLevelIndex, secondLevelIndex);
/* 269 */       trimBlock(block, slotSize);
/* 270 */       block.setUsed();
/* 272 */       long gap = Long.remainderUnsigned(block.offsetFromHeap, align);
/* 273 */       long alignmentOffset = (gap == 0L) ? 0L : (align - gap);
/* 274 */       long allocationOffset = block.offsetFromHeap + alignmentOffset;
/* 275 */       return new Allocation(block, allocationOffset);
/*     */     } 
/* 278 */     return null;
/*     */   }
/*     */   
/*     */   private void mergeBlockWithPrevious(Block block) {
/* 282 */     Block previous = block.previousPhysicalBlock;
/* 283 */     if (previous != null && previous.isFree()) {
/* 284 */       assert previous.offsetFromHeap + previous.getSize() == block.offsetFromHeap;
/* 285 */       assert previous.nextPhysicalBlock == block;
/* 287 */       IndexPair levelIndex = getLevelIndex(previous.getSize());
/* 288 */       removeFreeBlock(previous, levelIndex.firstLevelIndex, levelIndex.secondLevelIndex);
/* 290 */       Block prevprev = previous.previousPhysicalBlock;
/* 291 */       if (prevprev != null) {
/* 292 */         assert prevprev.nextPhysicalBlock == previous;
/* 293 */         prevprev.nextPhysicalBlock = block;
/*     */       } 
/* 296 */       block.previousPhysicalBlock = prevprev;
/* 297 */       block.setSize(block.getSize() + previous.getSize());
/* 298 */       block.offsetFromHeap = previous.offsetFromHeap;
/* 300 */       previous.previousPhysicalBlock = null;
/* 301 */       previous.nextPhysicalBlock = null;
/* 302 */       previous.nextFreeBlock = null;
/* 303 */       previous.previousFreeBlock = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void mergeBlockWithNext(Block block) {
/* 308 */     Block next = block.nextPhysicalBlock;
/* 309 */     if (next != null && next.isFree()) {
/* 310 */       assert next.offsetFromHeap - block.getSize() == block.offsetFromHeap;
/* 311 */       assert next.previousPhysicalBlock == block;
/* 313 */       IndexPair levelIndex = getLevelIndex(next.getSize());
/* 314 */       removeFreeBlock(next, levelIndex.firstLevelIndex, levelIndex.secondLevelIndex);
/* 316 */       Block nextnext = next.nextPhysicalBlock;
/* 317 */       if (nextnext != null) {
/* 318 */         assert nextnext.previousPhysicalBlock == next;
/* 319 */         nextnext.previousPhysicalBlock = block;
/*     */       } 
/* 322 */       block.nextPhysicalBlock = nextnext;
/* 323 */       block.setSize(block.getSize() + next.getSize());
/* 325 */       next.previousPhysicalBlock = null;
/* 326 */       next.nextPhysicalBlock = null;
/* 327 */       next.nextFreeBlock = null;
/* 328 */       next.previousFreeBlock = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void free(Allocation allocation) {
/* 333 */     if (!allocation.freed) {
/* 334 */       Block block = allocation.block;
/* 335 */       block.setFree();
/* 337 */       mergeBlockWithPrevious(block);
/* 338 */       mergeBlockWithNext(block);
/* 340 */       insertFreeBlock(block);
/* 341 */       allocation.freed = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isCompletelyFree() {
/* 349 */     if (Long.bitCount(this.firstLevelBitmap) == 1) {
/* 350 */       int firstLevelIndex = findFirstSignificantBit(this.firstLevelBitmap);
/* 351 */       int slBitmap = this.secondLevelBitmap[firstLevelIndex];
/* 352 */       if (Long.bitCount(slBitmap) == 1) {
/* 353 */         int secondLevelIndex = findFirstSignificantBit(slBitmap);
/* 354 */         Block freeBlock = getBlockFromFreeList(firstLevelIndex, secondLevelIndex);
/* 355 */         return (freeBlock != null && freeBlock.getSize() == this.totalMemorySize);
/*     */       } 
/*     */     } 
/* 359 */     return false;
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public void printAllocatorStatistics(String name) {
/* 365 */     int freeBlockCount = 0;
/* 366 */     long freeMemorySize = 0L;
/* 367 */     int levelFreeBlockCount = 0;
/* 368 */     for (int i = 0; i < 32; i++) {
/* 369 */       for (int j = 0; j < 8; j++) {
/* 370 */         levelFreeBlockCount = 0;
/* 371 */         Block block = getBlockFromFreeList(i, j);
/* 372 */         while (block != null) {
/* 373 */           levelFreeBlockCount++;
/* 374 */           freeMemorySize += block.getSize();
/* 375 */           block = block.nextFreeBlock;
/*     */         } 
/* 378 */         freeBlockCount += levelFreeBlockCount;
/*     */       } 
/*     */     } 
/* 382 */     double unusedPercent = freeMemorySize / this.totalMemorySize * 100.0D;
/* 384 */     LOGGER.debug("Uber buffer {}, size: {} -- free-listed memory: {} -- free-block count:{}", new Object[] { name, Long.valueOf(this.totalMemorySize), Double.valueOf(unusedPercent), Integer.valueOf(freeBlockCount) });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\TlsfAllocator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */