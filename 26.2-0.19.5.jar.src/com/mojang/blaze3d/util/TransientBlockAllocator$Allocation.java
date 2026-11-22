/*    */ package com.mojang.blaze3d.util;
/*    */ 
/*    */ public final class Allocation<T> extends Record {
/*    */   private final T block;
/*    */   
/*    */   private final long offset;
/*    */   
/*    */   private final long size;
/*    */   
/*    */   public Allocation(T block, long offset, long size) {
/* 95 */     this.block = block;
/* 95 */     this.offset = offset;
/* 95 */     this.size = size;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #95	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #95	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #95	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation<TT;>;
/*    */   }
/*    */   
/*    */   public T block() {
/* 95 */     return this.block;
/*    */   }
/*    */   
/*    */   public long offset() {
/* 95 */     return this.offset;
/*    */   }
/*    */   
/*    */   public long size() {
/* 95 */     return this.size;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3\\util\TransientBlockAllocator$Allocation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */