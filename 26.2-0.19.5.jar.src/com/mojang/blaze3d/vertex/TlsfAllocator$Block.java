/*    */ package com.mojang.blaze3d.vertex;
/*    */ 
/*    */ class Block {
/* 48 */   private long size = 0L;
/*    */   
/*    */   public final TlsfAllocator.Heap heap;
/*    */   
/*    */   public long offsetFromHeap;
/*    */   
/*    */   public Block nextFreeBlock;
/*    */   
/*    */   public Block previousFreeBlock;
/*    */   
/*    */   public Block nextPhysicalBlock;
/*    */   
/*    */   public Block previousPhysicalBlock;
/*    */   
/*    */   private static final int BLOCK_HEADER_FREE_BIT = 1;
/*    */   
/*    */   private Block(long size, TlsfAllocator.Heap heap, long offsetFromHeap, Block nextFreeBlock, Block previousFreeBlock, Block nextPhysicalBlock, Block previousPhysicalBlock) {
/* 64 */     this.heap = heap;
/* 65 */     this.offsetFromHeap = offsetFromHeap;
/* 66 */     this.nextFreeBlock = nextFreeBlock;
/* 67 */     this.previousFreeBlock = previousFreeBlock;
/* 68 */     this.nextPhysicalBlock = nextPhysicalBlock;
/* 69 */     this.previousPhysicalBlock = previousPhysicalBlock;
/* 70 */     setSize(size);
/*    */   }
/*    */   
/*    */   private boolean isFree() {
/* 74 */     return ((this.size & 0x1L) == 1L);
/*    */   }
/*    */   
/*    */   private void setFree() {
/* 78 */     this.size |= 0x1L;
/*    */   }
/*    */   
/*    */   private void setUsed() {
/* 82 */     this.size &= 0xFFFFFFFFFFFFFFFEL;
/*    */   }
/*    */   
/*    */   private long getSize() {
/* 86 */     return this.size & 0xFFFFFFFFFFFFFFFEL;
/*    */   }
/*    */   
/*    */   private void setSize(long size) {
/* 90 */     long oldSize = this.size;
/* 91 */     this.size = size | oldSize & 0x1L;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\TlsfAllocator$Block.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */