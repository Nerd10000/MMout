/*    */ package com.mojang.blaze3d.vulkan;
/*    */ 
/*    */ import com.mojang.blaze3d.GpuFormat;
/*    */ 
/*    */ public final class Entry extends Record {
/*    */   private final VulkanBindGroupLayout.VulkanBindGroupEntryType type;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final GpuFormat texelBufferFormat;
/*    */   
/*    */   public Entry(VulkanBindGroupLayout.VulkanBindGroupEntryType type, String name, GpuFormat texelBufferFormat) {
/* 23 */     this.type = type;
/* 23 */     this.name = name;
/* 23 */     this.texelBufferFormat = texelBufferFormat;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout$Entry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout$Entry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/VulkanBindGroupLayout$Entry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public VulkanBindGroupLayout.VulkanBindGroupEntryType type() {
/* 23 */     return this.type;
/*    */   }
/*    */   
/*    */   public String name() {
/* 23 */     return this.name;
/*    */   }
/*    */   
/*    */   public GpuFormat texelBufferFormat() {
/* 23 */     return this.texelBufferFormat;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanBindGroupLayout$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */