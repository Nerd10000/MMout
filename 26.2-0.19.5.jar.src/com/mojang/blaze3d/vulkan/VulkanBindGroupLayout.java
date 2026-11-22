/*    */ package com.mojang.blaze3d.vulkan;
/*    */ 
/*    */ import com.mojang.blaze3d.GpuFormat;
/*    */ import java.nio.LongBuffer;
/*    */ import java.util.List;
/*    */ import org.lwjgl.system.MemoryStack;
/*    */ import org.lwjgl.system.Struct;
/*    */ import org.lwjgl.vulkan.VK12;
/*    */ import org.lwjgl.vulkan.VkDescriptorSetLayoutBinding;
/*    */ import org.lwjgl.vulkan.VkDescriptorSetLayoutCreateInfo;
/*    */ 
/*    */ public final class VulkanBindGroupLayout extends Record {
/*    */   private final long handle;
/*    */   
/*    */   private final List<Entry> entries;
/*    */   
/*    */   public VulkanBindGroupLayout(long handle, List<Entry> entries) {
/* 14 */     this.handle = handle;
/* 14 */     this.entries = entries;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public long handle() {
/* 14 */     return this.handle;
/*    */   }
/*    */   
/*    */   public List<Entry> entries() {
/* 14 */     return this.entries;
/*    */   }
/*    */   
/* 15 */   public static final VulkanBindGroupLayout INVALID_LAYOUT = new VulkanBindGroupLayout(0L, List.of());
/*    */   
/*    */   public enum VulkanBindGroupEntryType {
/* 18 */     UNIFORM_BUFFER, SAMPLED_IMAGE, TEXEL_BUFFER;
/*    */   }
/*    */   
/*    */   public static final class Entry extends Record {
/*    */     private final VulkanBindGroupLayout.VulkanBindGroupEntryType type;
/*    */     
/*    */     private final String name;
/*    */     
/*    */     private final GpuFormat texelBufferFormat;
/*    */     
/*    */     public Entry(VulkanBindGroupLayout.VulkanBindGroupEntryType type, String name, GpuFormat texelBufferFormat) {
/* 23 */       this.type = type;
/* 23 */       this.name = name;
/* 23 */       this.texelBufferFormat = texelBufferFormat;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout$Entry;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout$Entry;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout$Entry;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public VulkanBindGroupLayout.VulkanBindGroupEntryType type() {
/* 23 */       return this.type;
/*    */     }
/*    */     
/*    */     public String name() {
/* 23 */       return this.name;
/*    */     }
/*    */     
/*    */     public GpuFormat texelBufferFormat() {
/* 23 */       return this.texelBufferFormat;
/*    */     }
/*    */   }
/*    */   
/*    */   public static VulkanBindGroupLayout create(VulkanDevice device, List<Entry> entries, String name) {
/*    */     long layoutHandle;
/* 27 */     MemoryStack stack = MemoryStack.stackPush();
/*    */     try {
/* 29 */       VkDescriptorSetLayoutBinding.Buffer bindings = VkDescriptorSetLayoutBinding.calloc(entries.size(), stack);
/* 30 */       for (int i = 0; i < entries.size(); i++) {
/* 32 */         switch (((Entry)entries
/* 33 */           .get(i)).type().ordinal()) {
/*    */           default:
/* 33 */             throw new MatchException(null, null);
/*    */           case 0:
/*    */           
/*    */           case 1:
/*    */           
/*    */           case 2:
/*    */             break;
/*    */         } 
/* 40 */         VkDescriptorSetLayoutBinding binding = VkDescriptorSetLayoutBinding.calloc(stack).descriptorType(4).descriptorCount(1).binding(i).stageFlags(17);
/* 41 */         bindings.put((Struct)binding);
/*    */       } 
/* 43 */       bindings.flip();
/* 48 */       VkDescriptorSetLayoutCreateInfo setCreateInfo = VkDescriptorSetLayoutCreateInfo.calloc(stack).sType$Default().flags(1).pBindings(bindings);
/* 49 */       LongBuffer pointer = stack.callocLong(1);
/* 50 */       VulkanUtils.crashIfFailure(device, VK12.vkCreateDescriptorSetLayout(device.vkDevice(), setCreateInfo, null, pointer), "Can't set layout for " + name);
/* 51 */       layoutHandle = pointer.get(0);
/* 52 */       if (stack != null)
/* 52 */         stack.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (stack != null)
/*    */         try {
/*    */           stack.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/* 53 */     return new VulkanBindGroupLayout(layoutHandle, entries);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanBindGroupLayout.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */