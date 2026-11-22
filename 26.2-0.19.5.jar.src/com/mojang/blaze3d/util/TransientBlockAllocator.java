/*     */ package com.mojang.blaze3d.util;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceList;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.LongFunction;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ 
/*     */ public class TransientBlockAllocator<T> implements AutoCloseable {
/*     */   private final long blockSize;
/*     */   
/*     */   private final long maxAlignment;
/*     */   
/*     */   private final Allocator<T> allocator;
/*     */   
/*     */   private final Consumer<T> onBlockUse;
/*     */   
/*  17 */   private final ReferenceArrayList<T> specialBlocks = new ReferenceArrayList();
/*     */   
/*  18 */   private final ReferenceArrayList<T> freeBlocks = new ReferenceArrayList();
/*     */   
/*  19 */   private final ReferenceArrayList<T> usedBlocks = new ReferenceArrayList();
/*     */   
/*     */   private T currentBlock;
/*     */   
/*  21 */   private long currentOffset = 0L;
/*     */   
/*     */   public TransientBlockAllocator(long blockSize, long maxAlignment, Allocator<T> allocator) {
/*  24 */     this(blockSize, maxAlignment, allocator, paramObject -> {
/*     */         
/*     */         });
/*     */   }
/*     */   
/*     */   public TransientBlockAllocator(long blockSize, long maxAlignment, Allocator<T> allocator, Consumer<T> onBlockUse) {
/*  28 */     this.blockSize = blockSize;
/*  29 */     this.maxAlignment = maxAlignment;
/*  30 */     this.allocator = allocator;
/*  31 */     this.onBlockUse = onBlockUse;
/*     */   }
/*     */   
/*     */   public void close() {
/*  37 */     rotate().run();
/*  38 */     rotate().run();
/*     */   }
/*     */   
/*     */   public long blockSize() {
/*  42 */     return this.blockSize;
/*     */   }
/*     */   
/*     */   public Runnable rotate() {
/*  46 */     this.currentBlock = null;
/*  47 */     this.currentOffset = this.blockSize;
/*  49 */     Objects.requireNonNull(this.allocator);
/*  49 */     this.freeBlocks.forEach(this.allocator::free);
/*  50 */     this.freeBlocks.clear();
/*  52 */     if (this.usedBlocks.isEmpty() && this.specialBlocks.isEmpty())
/*  53 */       return () -> {
/*     */         
/*     */         }; 
/*  55 */     ReferenceArrayList<T> blocksUsedThisRotation = this.usedBlocks.clone();
/*  56 */     this.usedBlocks.clear();
/*  57 */     ReferenceArrayList<T> specialBlocksUsedThisRotation = this.specialBlocks.clone();
/*  58 */     this.specialBlocks.clear();
/*  59 */     return () -> {
/*     */         if (!blocksUsedThisRotation.isEmpty())
/*     */           this.allocator.free((T)blocksUsedThisRotation.pop()); 
/*     */         this.freeBlocks.addAll((ReferenceList)blocksUsedThisRotation);
/*     */         Objects.requireNonNull(this.allocator);
/*     */         specialBlocksUsedThisRotation.forEach(this.allocator::free);
/*     */       };
/*     */   }
/*     */   
/*     */   @Contract(pure = true)
/*     */   public boolean canAllocateInBlock(long size, long alignment) {
/*  73 */     return (size <= this.blockSize && alignment <= this.maxAlignment);
/*     */   }
/*     */   
/*     */   @Contract(pure = true)
/*     */   public boolean canAllocateInCurrentBlock(long size, long alignment) {
/*  78 */     if (this.currentBlock == null && canAllocateInBlock(size, alignment))
/*  79 */       return true; 
/*  81 */     long alignedOffset = Mth.roundToward(this.currentOffset, alignment);
/*  82 */     return (size <= this.blockSize - alignedOffset && alignment <= this.maxAlignment);
/*     */   }
/*     */   
/*     */   private T allocateBlock() {
/*  86 */     if (this.freeBlocks.isEmpty())
/*  87 */       this.freeBlocks.add(this.allocator.alloc(this.blockSize)); 
/*  89 */     T block = (T)this.freeBlocks.pop();
/*  90 */     this.onBlockUse.accept(block);
/*  91 */     this.usedBlocks.add(block);
/*  92 */     return block;
/*     */   }
/*     */   
/*     */   public static final class Allocation<T> extends Record {
/*     */     private final T block;
/*     */     
/*     */     private final long offset;
/*     */     
/*     */     private final long size;
/*     */     
/*     */     public Allocation(T block, long offset, long size) {
/*  95 */       this.block = block;
/*  95 */       this.offset = offset;
/*  95 */       this.size = size;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #95	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #95	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #95	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/util/TransientBlockAllocator$Allocation<TT;>;
/*     */     }
/*     */     
/*     */     public T block() {
/*  95 */       return this.block;
/*     */     }
/*     */     
/*     */     public long offset() {
/*  95 */       return this.offset;
/*     */     }
/*     */     
/*     */     public long size() {
/*  95 */       return this.size;
/*     */     }
/*     */   }
/*     */   
/*     */   public Allocation<T> allocate(long size, long alignment, long minimumAllocation, long elementSize) {
/*  99 */     if (alignment > this.maxAlignment)
/* 100 */       throw new IllegalArgumentException("Alignment requirement over maximum supported alignment"); 
/* 102 */     if (size == this.blockSize)
/* 103 */       return new Allocation<>(allocateBlock(), 0L, this.blockSize); 
/* 105 */     if (!canAllocateInBlock(size, alignment)) {
/* 106 */       T specialBlock = this.allocator.alloc(size);
/* 107 */       this.onBlockUse.accept(specialBlock);
/* 108 */       this.specialBlocks.add(specialBlock);
/* 109 */       return new Allocation<>(specialBlock, 0L, size);
/*     */     } 
/* 111 */     if (this.currentBlock == null) {
/* 112 */       this.currentBlock = allocateBlock();
/* 113 */       this.currentOffset = 0L;
/*     */     } 
/* 115 */     if (canAllocateInCurrentBlock(size, alignment)) {
/* 116 */       assert this.currentBlock != null;
/* 117 */       long alignedOffset = Mth.roundToward(this.currentOffset, alignment);
/* 118 */       this.currentOffset = alignedOffset + size;
/* 119 */       T block = this.currentBlock;
/* 120 */       return new Allocation<>(block, alignedOffset, size);
/*     */     } 
/* 122 */     if (canAllocateInCurrentBlock(minimumAllocation, alignment)) {
/* 123 */       assert this.currentBlock != null;
/* 124 */       long alignedOffset = Mth.roundToward(this.currentOffset, alignment);
/* 125 */       long allocatedSize = (this.blockSize - alignedOffset) / elementSize * elementSize;
/* 127 */       this.currentOffset = alignedOffset + allocatedSize;
/* 128 */       T block = this.currentBlock;
/* 129 */       return new Allocation<>(block, alignedOffset, allocatedSize);
/*     */     } 
/* 131 */     T newBlock = allocateBlock();
/* 133 */     if (this.currentOffset > size) {
/* 134 */       this.currentBlock = newBlock;
/* 135 */       this.currentOffset = size;
/*     */     } 
/* 137 */     return new Allocation<>(newBlock, 0L, size);
/*     */   }
/*     */   
/*     */   public static interface Allocator<T> {
/*     */     T alloc(long param1Long);
/*     */     
/*     */     void free(T param1T);
/*     */     
/*     */     static <T> Allocator<T> create(final LongFunction<T> alloc, final Consumer<T> free) {
/* 146 */       return new Allocator<T>() {
/*     */           public T alloc(long size) {
/* 149 */             return alloc.apply(size);
/*     */           }
/*     */           
/*     */           public void free(T t) {
/* 154 */             free.accept(t);
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements Allocator<T> {
/*     */     public T alloc(long size) {
/*     */       return alloc.apply(size);
/*     */     }
/*     */     
/*     */     public void free(T t) {
/* 154 */       free.accept(t);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3\\util\TransientBlockAllocator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */