/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.systems.TransientMemory;
/*     */ import com.mojang.blaze3d.util.TransientBlockAllocator;
/*     */ import it.unimi.dsi.fastutil.Pair;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntComparator;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceReferenceImmutablePair;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.lwjgl.util.vma.Vma;
/*     */ import org.lwjgl.util.vma.VmaAllocationCreateInfo;
/*     */ import org.lwjgl.vulkan.VK12;
/*     */ import org.lwjgl.vulkan.VkBufferCopy;
/*     */ import org.lwjgl.vulkan.VkBufferCreateInfo;
/*     */ import org.lwjgl.vulkan.VkCommandBuffer;
/*     */ import org.lwjgl.vulkan.VkDevice;
/*     */ import org.lwjgl.vulkan.VkMemoryHeap;
/*     */ import org.lwjgl.vulkan.VkMemoryType;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceMemoryProperties;
/*     */ 
/*     */ public class VulkanTransientMemory implements TransientMemory, Destroyable {
/*     */   private static final long BLOCK_SIZE = 524288L;
/*     */   
/*     */   private static final long MAX_CPU_ALIGNMENT = 16L;
/*     */   
/*  41 */   private static final long MAX_GPU_ALIGNMENT = Long.highestOneBit(Long.MAX_VALUE);
/*     */   
/*     */   private static final int BUFFER_USAGE_BITS = 471;
/*     */   
/*     */   private final VulkanDevice device;
/*     */   
/*     */   private final VulkanCommandEncoder encoder;
/*     */   
/*     */   private final boolean useDeviceMemoryForMappedGpuStaging;
/*     */   
/*     */   private static final class VulkanAllocation extends Record {
/*     */     private final long vkBuffer;
/*     */     
/*     */     private final long vmaAllocation;
/*     */     
/*     */     private final long hostPtr;
/*     */     
/*     */     private final long size;
/*     */     
/*     */     private VulkanAllocation(long vkBuffer, long vmaAllocation, long hostPtr, long size) {
/*  48 */       this.vkBuffer = vkBuffer;
/*  48 */       this.vmaAllocation = vmaAllocation;
/*  48 */       this.hostPtr = hostPtr;
/*  48 */       this.size = size;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/VulkanTransientMemory$VulkanAllocation;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanTransientMemory$VulkanAllocation;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/VulkanTransientMemory$VulkanAllocation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanTransientMemory$VulkanAllocation;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/VulkanTransientMemory$VulkanAllocation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/VulkanTransientMemory$VulkanAllocation;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public long vkBuffer() {
/*  48 */       return this.vkBuffer;
/*     */     }
/*     */     
/*     */     public long vmaAllocation() {
/*  48 */       return this.vmaAllocation;
/*     */     }
/*     */     
/*     */     public long hostPtr() {
/*  48 */       return this.hostPtr;
/*     */     }
/*     */     
/*     */     public long size() {
/*  48 */       return this.size;
/*     */     }
/*     */   }
/*     */   
/*  55 */   private final TransientBlockAllocator<Long> cpuBlockAllocator = new TransientBlockAllocator(524288L, 16L, TransientBlockAllocator.Allocator.create(MemoryUtil::nmemAlloc, MemoryUtil::nmemFree));
/*     */   
/*     */   private final TransientBlockAllocator<VulkanAllocation> stagingBlockAllocator;
/*     */   
/*     */   private final TransientBlockAllocator<VulkanAllocation> gpuBlockAllocator;
/*     */   
/*     */   private final TransientBlockAllocator<Pair<VulkanAllocation, VulkanAllocation>> gpuMappedBlockAllocator;
/*     */   
/*  60 */   private long submitIndex = 0L;
/*     */   
/*     */   private boolean anyCommandRecorded = false;
/*     */   
/*     */   private VkCommandBuffer commandBuffer;
/*     */   
/*     */   VulkanTransientMemory(VulkanDevice device, VulkanCommandEncoder encoder) {
/*  66 */     this.device = device;
/*  67 */     this.encoder = encoder;
/*  68 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/*  69 */       VkDevice vkDevice = device.vkDevice();
/*  70 */       VkPhysicalDeviceMemoryProperties memoryProperties = VkPhysicalDeviceMemoryProperties.calloc(stack);
/*  71 */       VK12.vkGetPhysicalDeviceMemoryProperties(vkDevice.getPhysicalDevice(), memoryProperties);
/*  72 */       int heapCount = memoryProperties.memoryHeapCount();
/*  73 */       int typeCount = memoryProperties.memoryTypeCount();
/*  75 */       int largestDeviceLocalHeapIndex = -1;
/*  76 */       long largestDeviceLocalHeapSize = -1L;
/*  77 */       for (int i = 0; i < heapCount; i++) {
/*  78 */         VkMemoryHeap heapProperties = memoryProperties.memoryHeaps(i);
/*  79 */         if (VulkanUtils.hasAnyBit(heapProperties.flags(), 1))
/*  82 */           if (heapProperties.size() >= largestDeviceLocalHeapSize) {
/*  85 */             largestDeviceLocalHeapIndex = i;
/*  86 */             largestDeviceLocalHeapSize = heapProperties.size();
/*     */           }  
/*     */       } 
/*  89 */       assert largestDeviceLocalHeapIndex != -1;
/*  91 */       boolean largestHeapIsHostVisibleAndCoherent = false;
/*  92 */       for (int j = 0; j < typeCount; j++) {
/*  93 */         VkMemoryType typeProperties = memoryProperties.memoryTypes(j);
/*  94 */         if (typeProperties.heapIndex() == largestDeviceLocalHeapIndex)
/*  97 */           if (VulkanUtils.hasAllBits(typeProperties.propertyFlags(), 6)) {
/* 100 */             largestHeapIsHostVisibleAndCoherent = true;
/*     */             break;
/*     */           }  
/*     */       } 
/* 103 */       this.useDeviceMemoryForMappedGpuStaging = largestHeapIsHostVisibleAndCoherent;
/* 104 */       if (stack != null)
/* 104 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 105 */     this.stagingBlockAllocator = new TransientBlockAllocator(524288L, MAX_GPU_ALIGNMENT, TransientBlockAllocator.Allocator.create(size -> allocateVulkanBlock(size, true), this::freeVulkanBlock));
/* 106 */     this.gpuBlockAllocator = new TransientBlockAllocator(524288L, MAX_GPU_ALIGNMENT, TransientBlockAllocator.Allocator.create(size -> allocateVulkanBlock(size, false), this::queueFreeVulkanBlock));
/* 107 */     this.gpuMappedBlockAllocator = new TransientBlockAllocator(524288L, MAX_GPU_ALIGNMENT, TransientBlockAllocator.Allocator.create(this::allocateGpuMappedVulkanBlock, this::freeGpuMappedVulkanBlock), this::recordGpuMappedCopy);
/*     */   }
/*     */   
/*     */   public void destroy() {
/* 112 */     this.cpuBlockAllocator.close();
/* 113 */     this.stagingBlockAllocator.close();
/* 114 */     this.gpuBlockAllocator.close();
/* 115 */     this.gpuMappedBlockAllocator.close();
/*     */   }
/*     */   
/*     */   public void beginSubmit() {
/* 119 */     assert this.commandBuffer == null;
/* 120 */     this.commandBuffer = this.encoder.allocateAndBeginTransientCommandBuffer();
/* 121 */     this.encoder.execute(this.commandBuffer);
/* 122 */     this.anyCommandRecorded = false;
/*     */   }
/*     */   
/*     */   public void endSubmit() {
/* 126 */     this.cpuBlockAllocator.rotate().run();
/* 127 */     Objects.requireNonNull(this.stagingBlockAllocator.rotate());
/* 127 */     this.encoder.queueForDestroy(this.stagingBlockAllocator.rotate()::run);
/* 128 */     if (this.useDeviceMemoryForMappedGpuStaging) {
/* 129 */       Objects.requireNonNull(this.gpuBlockAllocator.rotate());
/* 129 */       this.encoder.queueForDestroy(this.gpuBlockAllocator.rotate()::run);
/*     */     } else {
/* 132 */       this.gpuBlockAllocator.rotate().run();
/*     */     } 
/* 136 */     this.gpuMappedBlockAllocator.rotate();
/* 138 */     assert this.commandBuffer != null;
/* 139 */     if (this.anyCommandRecorded) {
/* 140 */       MemoryStack stack = MemoryStack.stackPush();
/*     */       try {
/* 141 */         VulkanCommandEncoder.memoryBarrier(this.commandBuffer, stack);
/* 142 */         if (stack != null)
/* 142 */           stack.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (stack != null)
/*     */           try {
/*     */             stack.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } 
/* 144 */     VK12.vkEndCommandBuffer(this.commandBuffer);
/* 145 */     this.commandBuffer = null;
/* 147 */     this.submitIndex++;
/*     */   }
/*     */   
/*     */   private void recordGpuMappedCopy(Pair<VulkanAllocation, VulkanAllocation> block) {
/* 151 */     if (block.first() == block.second())
/*     */       return; 
/* 156 */     assert ((VulkanAllocation)block.first()).size == ((VulkanAllocation)block.second()).size;
/* 158 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 159 */       VkBufferCopy.Buffer region = VkBufferCopy.calloc(1, stack);
/* 160 */       region.srcOffset(0L);
/* 161 */       region.dstOffset(0L);
/* 162 */       region.size(((VulkanAllocation)block.first()).size);
/* 163 */       assert this.commandBuffer != null;
/* 164 */       VK12.vkCmdCopyBuffer(this.commandBuffer, ((VulkanAllocation)block.first()).vkBuffer, ((VulkanAllocation)block.second()).vkBuffer, region);
/* 165 */       this.anyCommandRecorded = true;
/* 166 */       if (stack != null)
/* 166 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private VulkanAllocation allocateVulkanBlock(long size, boolean staging) {
/* 170 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 171 */       VkBufferCreateInfo bufferCreateInfo = VkBufferCreateInfo.calloc(stack).sType$Default();
/* 172 */       bufferCreateInfo.size(size);
/* 173 */       bufferCreateInfo.usage(471);
/* 174 */       bufferCreateInfo.sharingMode(0);
/* 175 */       bufferCreateInfo.pQueueFamilyIndices(null);
/* 176 */       VmaAllocationCreateInfo allocCreateInfo = VmaAllocationCreateInfo.calloc(stack);
/* 178 */       if (staging) {
/* 179 */         allocCreateInfo.usage(9);
/*     */       } else {
/* 181 */         allocCreateInfo.usage(8);
/*     */       } 
/* 183 */       if (this.useDeviceMemoryForMappedGpuStaging || staging) {
/* 184 */         allocCreateInfo.requiredFlags(6);
/* 185 */         allocCreateInfo.flags(1024);
/*     */       } 
/* 188 */       LongBuffer bufferPtr = stack.callocLong(1);
/* 189 */       PointerBuffer allocPtr = stack.callocPointer(1);
/* 190 */       int result = Vma.vmaCreateBuffer(this.device.vma(), bufferCreateInfo, allocCreateInfo, bufferPtr, allocPtr, null);
/* 191 */       VulkanUtils.crashIfFailure(this.device, result, "Failed to allocate VkBuffer");
/* 193 */       PointerBuffer hostPtrPtr = stack.callocPointer(1);
/* 194 */       if (staging || this.useDeviceMemoryForMappedGpuStaging)
/* 195 */         VulkanUtils.crashIfFailure(this.device, Vma.vmaMapMemory(this.device.vma(), allocPtr.get(0), hostPtrPtr), "Failed to map buffer"); 
/* 198 */       this.device.instance().debug().setObjectName(this.device.vkDevice(), 9, bufferPtr.get(0), "Vulkan Transient Memory Buffer");
/* 200 */       VulkanAllocation vulkanAllocation = new VulkanAllocation(bufferPtr.get(0), allocPtr.get(0), hostPtrPtr.get(0), size);
/* 201 */       if (stack != null)
/* 201 */         stack.close(); 
/*     */       return vulkanAllocation;
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void queueFreeVulkanBlock(VulkanAllocation allocation) {
/* 205 */     this.encoder.queueForDestroy(() -> freeVulkanBlock(allocation));
/*     */   }
/*     */   
/*     */   private void freeVulkanBlock(VulkanAllocation allocation) {
/* 209 */     Vma.vmaDestroyBuffer(this.device.vma(), allocation.vkBuffer, allocation.vmaAllocation);
/*     */   }
/*     */   
/*     */   private Pair<VulkanAllocation, VulkanAllocation> allocateGpuMappedVulkanBlock(long size) {
/* 213 */     assert size >= 524288L;
/* 214 */     assert size >= this.gpuBlockAllocator.blockSize();
/* 216 */     if (this.useDeviceMemoryForMappedGpuStaging) {
/* 218 */       TransientBlockAllocator.Allocation<VulkanAllocation> block = this.gpuBlockAllocator.allocate(size, 16L, size, 1L);
/* 219 */       assert block.offset() == 0L;
/* 220 */       return (Pair<VulkanAllocation, VulkanAllocation>)new ReferenceReferenceImmutablePair(block.block(), block.block());
/*     */     } 
/* 223 */     assert size >= this.stagingBlockAllocator.blockSize();
/* 224 */     TransientBlockAllocator.Allocation<VulkanAllocation> stagingBlock = this.stagingBlockAllocator.allocate(size, 16L, size, 1L);
/* 225 */     TransientBlockAllocator.Allocation<VulkanAllocation> gpuBlock = this.gpuBlockAllocator.allocate(size, 16L, size, 1L);
/* 226 */     return (Pair<VulkanAllocation, VulkanAllocation>)new ReferenceReferenceImmutablePair(stagingBlock.block(), gpuBlock.block());
/*     */   }
/*     */   
/*     */   private void freeGpuMappedVulkanBlock(Pair<VulkanAllocation, VulkanAllocation> allocations) {}
/*     */   
/*     */   public ByteBuffer allocateCpu(long size, long alignment, long minimumAllocation, long elementSize) {
/* 235 */     assert size <= 2147483647L;
/* 236 */     TransientBlockAllocator.Allocation<Long> alloc = this.cpuBlockAllocator.allocate(size, alignment, minimumAllocation, elementSize);
/* 237 */     return MemoryUtil.memByteBuffer(((Long)alloc.block()).longValue() + alloc.offset(), (int)alloc.size());
/*     */   }
/*     */   
/*     */   public GpuBufferSlice.MappedView allocateStaging(long size, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 242 */     assert size <= 2147483647L;
/* 243 */     TransientBlockAllocator.Allocation<VulkanAllocation> alloc = this.stagingBlockAllocator.allocate(size, alignment, minimumAllocation, elementSize);
/* 244 */     TransientGpuBuffer apiBuffer = new TransientGpuBuffer(((VulkanAllocation)alloc.block()).vkBuffer, usage, (int)((VulkanAllocation)alloc.block()).size, this.submitIndex);
/* 245 */     ByteBuffer cpuBuffer = MemoryUtil.memByteBuffer(((VulkanAllocation)alloc.block()).hostPtr + alloc.offset(), (int)alloc.size());
/* 246 */     return new GpuBufferSlice.MappedView(new GpuBufferSlice(apiBuffer, alloc.offset(), alloc.size()), cpuBuffer, () -> {
/*     */         
/*     */         });
/*     */   }
/*     */   
/*     */   public GpuBufferSlice allocateGpu(long size, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 251 */     assert size <= 2147483647L;
/* 252 */     TransientBlockAllocator.Allocation<VulkanAllocation> alloc = this.gpuBlockAllocator.allocate(size, alignment, minimumAllocation, elementSize);
/* 253 */     TransientGpuBuffer apiBuffer = new TransientGpuBuffer(((VulkanAllocation)alloc.block()).vkBuffer, usage, (int)((VulkanAllocation)alloc.block()).size, this.submitIndex);
/* 254 */     return new GpuBufferSlice(apiBuffer, alloc.offset(), alloc.size());
/*     */   }
/*     */   
/*     */   public GpuBufferSlice.MappedView allocateGpuMapped(long size, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 259 */     assert size <= 2147483647L;
/* 260 */     TransientBlockAllocator.Allocation<Pair<VulkanAllocation, VulkanAllocation>> alloc = this.gpuMappedBlockAllocator.allocate(size, alignment, minimumAllocation, elementSize);
/* 261 */     TransientGpuBuffer apiBuffer = new TransientGpuBuffer(((VulkanAllocation)((Pair)alloc.block()).second()).vkBuffer, usage, (int)((VulkanAllocation)((Pair)alloc.block()).first()).size, this.submitIndex);
/* 262 */     ByteBuffer cpuBuffer = MemoryUtil.memByteBuffer(((VulkanAllocation)((Pair)alloc.block()).first()).hostPtr + alloc.offset(), (int)alloc.size());
/* 263 */     return new GpuBufferSlice.MappedView(new GpuBufferSlice(apiBuffer, alloc.offset(), alloc.size()), cpuBuffer, () -> {
/*     */         
/*     */         });
/*     */   }
/*     */   
/*     */   public GpuBufferSlice uploadStaging(List<ByteBuffer> data, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 268 */     return upload(data, alignment, usage, minimumAllocation, elementSize, true);
/*     */   }
/*     */   
/*     */   public GpuBufferSlice uploadGpu(List<ByteBuffer> data, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 273 */     return upload(data, alignment, usage, minimumAllocation, elementSize, false);
/*     */   }
/*     */   
/*     */   public GpuBufferSlice upload(List<ByteBuffer> data, long alignment, int usage, long minimumAllocation, long elementSize, boolean staging) {
/* 277 */     long totalSize = 0L;
/* 278 */     for (ByteBuffer buffer : data) {
/* 279 */       totalSize += buffer.remaining();
/* 280 */       totalSize = Mth.roundToward(totalSize, alignment);
/*     */     } 
/* 286 */     GpuBufferSlice.MappedView mapped = staging ? allocateStaging(totalSize, alignment, usage, minimumAllocation, elementSize) : allocateGpuMapped(totalSize, alignment, usage, minimumAllocation, elementSize);
/*     */     try {
/* 288 */       long mappedPtr = MemoryUtil.memAddress(mapped.data());
/* 289 */       long offset = 0L;
/* 290 */       for (ByteBuffer buffer : data) {
/* 291 */         MemoryUtil.memCopy(MemoryUtil.memAddress(buffer), mappedPtr + offset, Math.min(mapped.slice().length() - offset, buffer.remaining()));
/* 292 */         offset += buffer.remaining();
/* 293 */         offset = Mth.roundToward(offset, alignment);
/* 294 */         if (offset >= mapped.slice().length())
/*     */           break; 
/*     */       } 
/* 298 */       GpuBufferSlice gpuBufferSlice = mapped.slice();
/* 299 */       if (mapped != null)
/* 299 */         mapped.close(); 
/*     */       return gpuBufferSlice;
/*     */     } catch (Throwable throwable) {
/*     */       if (mapped != null)
/*     */         try {
/*     */           mapped.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<GpuBufferSlice> multiUploadStaging(List<ByteBuffer> data, long alignment, int usage) {
/* 304 */     return multiUpload(data, alignment, usage, true);
/*     */   }
/*     */   
/*     */   public List<GpuBufferSlice> multiUploadGpu(List<ByteBuffer> data, long alignment, int usage) {
/* 309 */     return multiUpload(data, alignment, usage, false);
/*     */   }
/*     */   
/*     */   public List<GpuBufferSlice> multiUpload(List<ByteBuffer> data, long alignment, int usage, boolean staging) {
/* 313 */     ReferenceArrayList<GpuBufferSlice> uploadedBuffers = new ReferenceArrayList();
/* 314 */     uploadedBuffers.size(data.size());
/* 316 */     TransientBlockAllocator<?> allocatorInUse = staging ? this.stagingBlockAllocator : this.gpuMappedBlockAllocator;
/* 318 */     IntArrayList sortedDataIndices = IntArrayList.toList(IntStream.range(0, data.size()));
/* 319 */     sortedDataIndices.sort(IntComparator.comparing(index -> Integer.valueOf(((ByteBuffer)data.get(index)).remaining())));
/* 320 */     while (!sortedDataIndices.isEmpty()) {
/* 322 */       boolean allocatedAnything = false;
/* 324 */       for (int i = sortedDataIndices.size() - 1; i >= 0; ) {
/* 325 */         int j = sortedDataIndices.getInt(i);
/* 326 */         ByteBuffer byteBuffer = data.get(j);
/* 327 */         if (!allocatorInUse.canAllocateInCurrentBlock(byteBuffer.remaining(), alignment)) {
/*     */           i--;
/*     */           continue;
/*     */         } 
/* 330 */         sortedDataIndices.removeInt(i);
/* 335 */         GpuBufferSlice.MappedView mappedView = staging ? allocateStaging(byteBuffer.remaining(), alignment, usage) : allocateGpuMapped(byteBuffer.remaining(), alignment, usage);
/*     */         try {
/* 337 */           MemoryUtil.memCopy(byteBuffer, mappedView.data());
/* 338 */           uploadedBuffers.set(j, mappedView.slice());
/* 339 */           if (mappedView != null)
/* 339 */             mappedView.close(); 
/*     */         } catch (Throwable throwable) {
/*     */           if (mappedView != null)
/*     */             try {
/*     */               mappedView.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/* 340 */         allocatedAnything = true;
/*     */       } 
/* 343 */       if (allocatedAnything)
/*     */         continue; 
/* 348 */       int bufferIndex = sortedDataIndices.popInt();
/* 349 */       ByteBuffer currentBuffer = data.get(bufferIndex);
/* 350 */       GpuBufferSlice.MappedView view = allocateGpuMapped(currentBuffer.remaining(), alignment, usage);
/*     */       try {
/* 351 */         MemoryUtil.memCopy(currentBuffer, view.data());
/* 352 */         uploadedBuffers.set(bufferIndex, view.slice());
/* 353 */         if (view != null)
/* 353 */           view.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (view != null)
/*     */           try {
/*     */             view.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } 
/* 355 */     return (List<GpuBufferSlice>)uploadedBuffers;
/*     */   }
/*     */   
/*     */   private class TransientGpuBuffer extends VulkanGpuBuffer {
/*     */     private boolean closed = false;
/*     */     
/*     */     private final long bufferSubmitIndex;
/*     */     
/*     */     public TransientGpuBuffer(long vkBuffer, int usage, int size, long bufferSubmitIndex) {
/* 363 */       super(vkBuffer, usage, size);
/* 364 */       this.bufferSubmitIndex = bufferSubmitIndex;
/*     */     }
/*     */     
/*     */     public void destroy() {}
/*     */     
/*     */     public GpuBufferSlice.MappedView map(long offset, long length, boolean read, boolean write) {
/* 373 */       throw new IllegalStateException("Cannot map transient buffer");
/*     */     }
/*     */     
/*     */     public boolean isClosed() {
/* 378 */       if (this.closed)
/* 379 */         return true; 
/* 381 */       this.closed = (this.bufferSubmitIndex < VulkanTransientMemory.this.submitIndex);
/* 382 */       return this.closed;
/*     */     }
/*     */     
/*     */     public void close() {
/* 387 */       this.closed = true;
/*     */     }
/*     */     
/*     */     public GpuBufferSlice slice(long offset, long length) {
/* 392 */       throw new IllegalStateException("Cannot slice transient buffer");
/*     */     }
/*     */     
/*     */     public GpuBufferSlice slice() {
/* 397 */       throw new IllegalStateException("Cannot slice transient buffer");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanTransientMemory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */