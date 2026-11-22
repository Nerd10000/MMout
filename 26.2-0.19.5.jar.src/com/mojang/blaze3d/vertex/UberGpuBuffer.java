/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ 
/*     */ public class UberGpuBuffer<T> implements AutoCloseable {
/*     */   private final int bufferUsage;
/*     */   
/*     */   private final int heapSize;
/*     */   
/*     */   private final int alignSize;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private static final class StagedAllocationEntry<T> extends Record implements AutoCloseable {
/*     */     private final StagingBuffer.BufferHandle buffer;
/*     */     
/*     */     private final UberGpuBuffer.UploadCallback<T> callback;
/*     */     
/*     */     private StagedAllocationEntry(StagingBuffer.BufferHandle buffer, UberGpuBuffer.UploadCallback<T> callback) {
/*  31 */       this.buffer = buffer;
/*  31 */       this.callback = callback;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry<TT;>;
/*     */     }
/*     */     
/*     */     public StagingBuffer.BufferHandle buffer() {
/*  31 */       return this.buffer;
/*     */     }
/*     */     
/*     */     public UberGpuBuffer.UploadCallback<T> callback() {
/*  31 */       return this.callback;
/*     */     }
/*     */     
/*     */     public void close() {
/*  37 */       this.buffer.close();
/*     */     }
/*     */   }
/*     */   
/*  47 */   private final List<Pair<TlsfAllocator, UberGpuBufferHeap>> nodes = new ArrayList<>();
/*     */   
/*     */   private final StagingBuffer stagingBuffer;
/*     */   
/*  51 */   private final Object2ObjectOpenHashMap<T, StagedAllocationEntry<? extends T>> stagedAllocations = new Object2ObjectOpenHashMap(32);
/*     */   
/*  52 */   private final ObjectOpenHashSet<T> skippedStagedAllocations = new ObjectOpenHashSet(32);
/*     */   
/*  53 */   private final Map<T, TlsfAllocator.Allocation> allocationMap = new HashMap<>(256);
/*     */   
/*     */   public static interface UploadCallback<T> {
/*     */     void bufferHasBeenUploaded(T param1T);
/*     */   }
/*     */   
/*     */   public static class UberGpuBufferHeap extends TlsfAllocator.Heap {
/*     */     private final GpuBuffer gpuBuffer;
/*     */     
/*     */     public UberGpuBufferHeap(long size, GpuDevice gpuDevice, int usage, String name) {
/*  60 */       super(size);
/*  61 */       this.gpuBuffer = gpuDevice.createBuffer(() -> name, usage | 0x8 | 0x10, size);
/*     */     }
/*     */   }
/*     */   
/*     */   public UberGpuBuffer(String name, int bufferUsage, int heapSize, int alignSize, StagingBuffer stagingBuffer) {
/*  66 */     this.name = "UberBuffer " + name;
/*  67 */     this.bufferUsage = bufferUsage;
/*  68 */     this.heapSize = heapSize;
/*  69 */     this.alignSize = alignSize;
/*  70 */     this.stagingBuffer = stagingBuffer;
/*     */   }
/*     */   
/*     */   public <U extends T> boolean addAllocation(U allocationKey, UploadCallback<U> callback, ByteBuffer buffer) {
/*  77 */     StagingBuffer.BufferHandle handle = this.stagingBuffer.tryAppend(buffer);
/*  78 */     if (handle == null)
/*  79 */       return false; 
/*  81 */     StagedAllocationEntry<U> entry = new StagedAllocationEntry<>(handle, callback);
/*  82 */     StagedAllocationEntry<? extends T> oldEntry = (StagedAllocationEntry<? extends T>)this.stagedAllocations.put(allocationKey, entry);
/*  83 */     if (oldEntry != null)
/*  84 */       oldEntry.close(); 
/*  86 */     return true;
/*     */   }
/*     */   
/*     */   public boolean uploadStagedAllocations(GpuDevice gpuDevice, StagingBuffer.Uploader uploader) {
/*  94 */     uploader.checkValidFor(this.stagingBuffer);
/*  96 */     for (ObjectIterator<T> objectIterator = this.stagedAllocations.keySet().iterator(); objectIterator.hasNext(); ) {
/*  96 */       T key = objectIterator.next();
/*  97 */       freeAllocation(key);
/*     */     } 
/* 100 */     boolean newHeapCreatedOrDestroyed = false;
/* 101 */     Zone zone = Profiler.get().zone("uploadStagedAllocations");
/*     */     try {
/* 102 */       for (ObjectIterator<Map.Entry<T, StagedAllocationEntry<? extends T>>> objectIterator1 = this.stagedAllocations.entrySet().iterator(); objectIterator1.hasNext(); ) {
/* 102 */         Map.Entry<T, StagedAllocationEntry<? extends T>> entry = objectIterator1.next();
/* 103 */         StagedAllocationEntry<? extends T> staged = entry.getValue();
/*     */         try {
/* 104 */           long allocationSize = staged.buffer.size();
/* 105 */           if (this.skippedStagedAllocations.contains(entry.getKey())) {
/* 137 */             if (staged != null)
/* 137 */               staged.close(); 
/*     */             continue;
/*     */           } 
/*     */           TlsfAllocator.Allocation allocation = null;
/*     */           for (Pair<TlsfAllocator, UberGpuBufferHeap> node : this.nodes) {
/*     */             allocation = ((TlsfAllocator)node.getFirst()).allocate(allocationSize, this.alignSize);
/*     */             if (allocation != null)
/*     */               break; 
/*     */           } 
/*     */           if (allocation == null) {
/*     */             Zone zone1 = Profiler.get().zone("createNewHeap");
/*     */             try {
/*     */               assert allocationSize <= this.heapSize;
/*     */               String heapName = String.format(Locale.ROOT, "%s %d", new Object[] { this.name, Integer.valueOf(this.nodes.size()) });
/*     */               UberGpuBufferHeap newHeap = new UberGpuBufferHeap(this.heapSize, gpuDevice, this.bufferUsage, heapName);
/*     */               TlsfAllocator newTlsfAllocator = new TlsfAllocator(newHeap);
/*     */               this.nodes.add(new Pair(newTlsfAllocator, newHeap));
/*     */               allocation = newTlsfAllocator.allocate(allocationSize, this.alignSize);
/*     */               newHeapCreatedOrDestroyed = true;
/*     */               if (zone1 != null)
/*     */                 zone1.close(); 
/*     */             } catch (Throwable throwable) {
/*     */               if (zone1 != null)
/*     */                 try {
/*     */                   zone1.close();
/*     */                 } catch (Throwable throwable1) {
/*     */                   throwable.addSuppressed(throwable1);
/*     */                 }  
/*     */               throw throwable;
/*     */             } 
/*     */           } 
/*     */           if (allocation == null) {
/* 137 */             if (staged != null)
/* 137 */               staged.close(); 
/*     */             continue;
/*     */           } 
/*     */           TlsfAllocator.Heap allocationHeap = allocation.getHeap();
/*     */           GpuBuffer allocationDestBuffer = ((UberGpuBufferHeap)allocationHeap).gpuBuffer;
/*     */           uploader.copyTo(staged.buffer, allocationDestBuffer, allocation.getOffsetFromHeap());
/*     */           this.allocationMap.put(entry.getKey(), allocation);
/*     */           runCallbackUnchecked(entry.getKey(), entry.getValue());
/* 137 */           if (staged != null)
/* 137 */             staged.close(); 
/*     */         } catch (Throwable throwable) {
/*     */           if (staged != null)
/*     */             try {
/*     */               staged.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/*     */       } 
/* 140 */       this.stagedAllocations.clear();
/* 141 */       this.skippedStagedAllocations.clear();
/* 142 */       if (zone != null)
/* 142 */         zone.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (zone != null)
/*     */         try {
/*     */           zone.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 144 */     Iterator<Pair<TlsfAllocator, UberGpuBufferHeap>> iterator = this.nodes.iterator();
/* 145 */     while (iterator.hasNext()) {
/* 146 */       Pair<TlsfAllocator, UberGpuBufferHeap> node = iterator.next();
/* 147 */       if (((TlsfAllocator)node.getFirst()).isCompletelyFree()) {
/* 148 */         ((UberGpuBufferHeap)node.getSecond()).gpuBuffer.close();
/* 149 */         iterator.remove();
/* 152 */         newHeapCreatedOrDestroyed = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 157 */     return newHeapCreatedOrDestroyed;
/*     */   }
/*     */   
/*     */   private static <T, U extends T> void runCallbackUnchecked(T key, StagedAllocationEntry<U> value) {
/* 162 */     if (value.callback != null)
/* 163 */       value.callback.bufferHasBeenUploaded((U)key); 
/*     */   }
/*     */   
/*     */   public TlsfAllocator.Allocation getAllocation(T allocationKey) {
/* 168 */     return this.allocationMap.get(allocationKey);
/*     */   }
/*     */   
/*     */   public void removeAllocation(T allocationKey) {
/* 172 */     this.skippedStagedAllocations.add(allocationKey);
/* 173 */     freeAllocation(allocationKey);
/*     */   }
/*     */   
/*     */   private void freeAllocation(T allocationKey) {
/* 177 */     TlsfAllocator.Allocation allocation = this.allocationMap.remove(allocationKey);
/* 178 */     if (allocation != null)
/* 179 */       for (Pair<TlsfAllocator, UberGpuBufferHeap> node : this.nodes) {
/* 180 */         if (node.getSecond() == allocation.getHeap()) {
/* 181 */           ((TlsfAllocator)node.getFirst()).free(allocation);
/*     */           break;
/*     */         } 
/*     */       }  
/*     */   }
/*     */   
/*     */   public GpuBuffer getGpuBuffer(TlsfAllocator.Allocation allocation) {
/* 189 */     return ((UberGpuBufferHeap)allocation.getHeap()).gpuBuffer;
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public void printStatistics() {
/* 195 */     for (int i = 0; i < this.nodes.size(); i++) {
/* 196 */       Pair<TlsfAllocator, UberGpuBufferHeap> node = this.nodes.get(i);
/* 197 */       String heapName = String.format(Locale.ROOT, "%s %d", new Object[] { this.name, Integer.valueOf(i) });
/* 198 */       ((TlsfAllocator)node.getFirst()).printAllocatorStatistics(heapName);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/* 205 */     this.stagedAllocations.values().forEach(StagedAllocationEntry::close);
/* 206 */     this.stagedAllocations.clear();
/* 207 */     this.allocationMap.clear();
/* 209 */     for (Pair<TlsfAllocator, UberGpuBufferHeap> node : this.nodes)
/* 210 */       ((UberGpuBufferHeap)node.getSecond()).gpuBuffer.close(); 
/* 212 */     this.nodes.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\UberGpuBuffer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */