/*    */ package com.mojang.blaze3d.vertex;
/*    */ 
/*    */ public class Allocation {
/*    */   private final TlsfAllocator.Block block;
/*    */   
/*    */   private final long offsetFromHeap;
/*    */   
/*    */   private boolean freed = false;
/*    */   
/*    */   private Allocation(TlsfAllocator.Block block, long offsetFromHeap) {
/* 27 */     this.block = block;
/* 28 */     this.offsetFromHeap = offsetFromHeap;
/*    */   }
/*    */   
/*    */   public long getSize() {
/* 32 */     return this.block.getSize();
/*    */   }
/*    */   
/*    */   public TlsfAllocator.Heap getHeap() {
/* 36 */     return this.block.heap;
/*    */   }
/*    */   
/*    */   public long getOffsetFromHeap() {
/* 40 */     return this.offsetFromHeap;
/*    */   }
/*    */   
/*    */   public boolean isFreed() {
/* 44 */     return this.freed;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\TlsfAllocator$Allocation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */