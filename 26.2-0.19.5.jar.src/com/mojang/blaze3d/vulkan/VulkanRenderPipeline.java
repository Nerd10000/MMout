/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.pipeline.BlendFunction;
/*     */ import com.mojang.blaze3d.pipeline.ColorTargetState;
/*     */ import com.mojang.blaze3d.pipeline.CompiledRenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.blaze3d.vertex.VertexFormatElement;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.system.Struct;
/*     */ import org.lwjgl.vulkan.VK12;
/*     */ import org.lwjgl.vulkan.VkGraphicsPipelineCreateInfo;
/*     */ import org.lwjgl.vulkan.VkPipelineColorBlendAttachmentState;
/*     */ import org.lwjgl.vulkan.VkPipelineColorBlendStateCreateInfo;
/*     */ import org.lwjgl.vulkan.VkPipelineDepthStencilStateCreateInfo;
/*     */ import org.lwjgl.vulkan.VkPipelineDynamicStateCreateInfo;
/*     */ import org.lwjgl.vulkan.VkPipelineInputAssemblyStateCreateInfo;
/*     */ import org.lwjgl.vulkan.VkPipelineLayoutCreateInfo;
/*     */ import org.lwjgl.vulkan.VkPipelineMultisampleStateCreateInfo;
/*     */ import org.lwjgl.vulkan.VkPipelineRasterizationStateCreateInfo;
/*     */ import org.lwjgl.vulkan.VkPipelineRenderingCreateInfoKHR;
/*     */ import org.lwjgl.vulkan.VkPipelineShaderStageCreateInfo;
/*     */ import org.lwjgl.vulkan.VkPipelineVertexInputDivisorStateCreateInfoEXT;
/*     */ import org.lwjgl.vulkan.VkPipelineVertexInputStateCreateInfo;
/*     */ import org.lwjgl.vulkan.VkPipelineViewportStateCreateInfo;
/*     */ import org.lwjgl.vulkan.VkVertexInputAttributeDescription;
/*     */ import org.lwjgl.vulkan.VkVertexInputBindingDescription;
/*     */ import org.lwjgl.vulkan.VkVertexInputBindingDivisorDescription;
/*     */ import org.lwjgl.vulkan.VkVertexInputBindingDivisorDescriptionEXT;
/*     */ 
/*     */ public final class VulkanRenderPipeline extends Record implements CompiledRenderPipeline, Destroyable {
/*     */   private final RenderPipeline info;
/*     */   
/*     */   private final VulkanDevice device;
/*     */   
/*     */   private final long withDepthPipeline;
/*     */   
/*     */   private final long withoutDepthPipeline;
/*     */   
/*     */   private final long pipelineLayout;
/*     */   
/*     */   private final VulkanBindGroupLayout layout;
/*     */   
/*     */   private final long vertexModule;
/*     */   
/*     */   private final long fragmentModule;
/*     */   
/*     */   public static final long INVALID_PIPELINE = 0L;
/*     */   
/*     */   public VulkanRenderPipeline(RenderPipeline info, VulkanDevice device, long withDepthPipeline, long withoutDepthPipeline, long pipelineLayout, VulkanBindGroupLayout layout, long vertexModule, long fragmentModule) {
/*  34 */     this.info = info;
/*  34 */     this.device = device;
/*  34 */     this.withDepthPipeline = withDepthPipeline;
/*  34 */     this.withoutDepthPipeline = withoutDepthPipeline;
/*  34 */     this.pipelineLayout = pipelineLayout;
/*  34 */     this.layout = layout;
/*  34 */     this.vertexModule = vertexModule;
/*  34 */     this.fragmentModule = fragmentModule;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/VulkanRenderPipeline;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanRenderPipeline;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/VulkanRenderPipeline;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanRenderPipeline;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/VulkanRenderPipeline;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/VulkanRenderPipeline;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public RenderPipeline info() {
/*  34 */     return this.info;
/*     */   }
/*     */   
/*     */   public VulkanDevice device() {
/*  34 */     return this.device;
/*     */   }
/*     */   
/*     */   public long withDepthPipeline() {
/*  34 */     return this.withDepthPipeline;
/*     */   }
/*     */   
/*     */   public long withoutDepthPipeline() {
/*  34 */     return this.withoutDepthPipeline;
/*     */   }
/*     */   
/*     */   public long pipelineLayout() {
/*  34 */     return this.pipelineLayout;
/*     */   }
/*     */   
/*     */   public VulkanBindGroupLayout layout() {
/*  34 */     return this.layout;
/*     */   }
/*     */   
/*     */   public long vertexModule() {
/*  34 */     return this.vertexModule;
/*     */   }
/*     */   
/*     */   public long fragmentModule() {
/*  34 */     return this.fragmentModule;
/*     */   }
/*     */   
/*     */   public boolean isValid() {
/*  39 */     return (this.withDepthPipeline != 0L);
/*     */   }
/*     */   
/*     */   public static VulkanRenderPipeline compile(VulkanDevice device, VulkanBindGroupLayout layout, RenderPipeline pipeline, long vertexModule, long fragmentModule) {
/*     */     long pipelineLayout;
/*  44 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/*  48 */       VkPipelineLayoutCreateInfo createInfo = VkPipelineLayoutCreateInfo.calloc(stack).sType$Default().pSetLayouts(stack.longs(layout.handle()));
/*  49 */       LongBuffer pointer = stack.callocLong(1);
/*  50 */       VulkanUtils.crashIfFailure(device, VK12.vkCreatePipelineLayout(device.vkDevice(), createInfo, null, pointer), "Can't create pipeline for " + String.valueOf(pipeline.getLocation()));
/*  51 */       pipelineLayout = pointer.get(0);
/*  52 */       device.instance().debug().setObjectName(device.vkDevice(), 17, pipelineLayout, () -> "Pipeline layout for " + String.valueOf(pipeline.getLocation()));
/*  53 */       if (stack != null)
/*  53 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*  55 */     stack = MemoryStack.stackPush();
/*     */     try {
/*     */       long withoutDepthPipeline;
/*  56 */       VkPipelineShaderStageCreateInfo.Buffer shaderStages = VkPipelineShaderStageCreateInfo.calloc(2, stack);
/*  57 */       ByteBuffer nameMain = stack.UTF8("main");
/*  63 */       VkPipelineShaderStageCreateInfo vertexStage = VkPipelineShaderStageCreateInfo.calloc(stack).sType$Default().stage(1).module(vertexModule).pName(nameMain);
/*  70 */       VkPipelineShaderStageCreateInfo fragmentStage = VkPipelineShaderStageCreateInfo.calloc(stack).sType$Default().stage(16).module(fragmentModule).pName(nameMain);
/*  71 */       ((VkPipelineShaderStageCreateInfo.Buffer)((VkPipelineShaderStageCreateInfo.Buffer)shaderStages.put((Struct)vertexStage)).put((Struct)fragmentStage)).flip();
/*  73 */       VertexFormat[] vertexBindings = pipeline.getVertexFormatBindings();
/*  75 */       VkVertexInputAttributeDescription.Buffer vertexAttributeDescriptions = VkVertexInputAttributeDescription.calloc(vertexBindings.length, stack);
/*  76 */       VkVertexInputBindingDescription.Buffer vertexBindingDescriptions = VkVertexInputBindingDescription.calloc(vertexBindings.length, stack);
/*  77 */       VkVertexInputBindingDivisorDescriptionEXT.Buffer vertexBindingDivisorDescriptions = VkVertexInputBindingDivisorDescriptionEXT.calloc(vertexBindings.length, stack);
/*  78 */       int attribLocation = 0;
/*  79 */       for (int i = 0; i < vertexBindings.length; i++) {
/*  80 */         VertexFormat bindings = vertexBindings[i];
/*  81 */         if (bindings != null) {
/*  89 */           VkVertexInputBindingDescription bindingDescription = VkVertexInputBindingDescription.calloc(stack).binding(i).stride(bindings.getVertexSize()).inputRate((bindings.getStepRate() > 0) ? 1 : 0);
/*  90 */           vertexBindingDescriptions.put((Struct)bindingDescription);
/*  92 */           if (bindings.getStepRate() > 0) {
/*  96 */             VkVertexInputBindingDivisorDescriptionEXT divisorBinding = VkVertexInputBindingDivisorDescriptionEXT.calloc(stack).binding(i).divisor(bindings.getStepRate());
/*  98 */             vertexBindingDivisorDescriptions.put((Struct)divisorBinding);
/*     */           } 
/* 101 */           for (VertexFormatElement element : bindings.getElements()) {
/* 107 */             VkVertexInputAttributeDescription attributeDescription = VkVertexInputAttributeDescription.calloc(stack).location(attribLocation).binding(i).offset(element.offset()).format(VulkanConst.toVk(element.format()));
/* 108 */             vertexAttributeDescriptions.put((Struct)attributeDescription);
/* 109 */             attribLocation++;
/*     */           } 
/*     */         } 
/*     */       } 
/* 112 */       vertexAttributeDescriptions.flip();
/* 113 */       vertexBindingDescriptions.flip();
/* 114 */       vertexBindingDivisorDescriptions.flip();
/* 119 */       VkPipelineVertexInputDivisorStateCreateInfoEXT vertexInputDivisorState = VkPipelineVertexInputDivisorStateCreateInfoEXT.calloc(stack).sType$Default().pVertexBindingDivisors((VkVertexInputBindingDivisorDescription.Buffer)vertexBindingDivisorDescriptions);
/* 125 */       VkPipelineVertexInputStateCreateInfo vertexInputState = VkPipelineVertexInputStateCreateInfo.calloc(stack).sType$Default().pVertexAttributeDescriptions(vertexAttributeDescriptions).pVertexBindingDescriptions(vertexBindingDescriptions);
/* 127 */       if (vertexInputDivisorState.vertexBindingDivisorCount() > 0)
/* 128 */         vertexInputState.pNext(vertexInputDivisorState); 
/* 134 */       VkPipelineInputAssemblyStateCreateInfo inputAssemblyState = VkPipelineInputAssemblyStateCreateInfo.calloc(stack).sType$Default().topology(VulkanConst.toVk(pipeline.getPrimitiveTopology()));
/* 142 */       VkPipelineRasterizationStateCreateInfo rasterizationState = VkPipelineRasterizationStateCreateInfo.calloc(stack).sType$Default().polygonMode(VulkanConst.toVk(pipeline.getPolygonMode())).cullMode(pipeline.isCull() ? 2 : 0).frontFace(1).lineWidth(1.0F);
/* 146 */       VkPipelineDepthStencilStateCreateInfo depthStencilState = VkPipelineDepthStencilStateCreateInfo.calloc(stack).sType$Default();
/* 148 */       if (pipeline.getDepthStencilState() != null) {
/* 149 */         rasterizationState.depthBiasEnable((pipeline.getDepthStencilState().depthBiasConstant() != 0.0F && pipeline.getDepthStencilState().depthBiasScaleFactor() != 0.0F));
/* 150 */         rasterizationState.depthBiasConstantFactor(pipeline.getDepthStencilState().depthBiasConstant());
/* 151 */         rasterizationState.depthBiasSlopeFactor(pipeline.getDepthStencilState().depthBiasScaleFactor());
/* 153 */         depthStencilState.depthTestEnable(true);
/* 154 */         depthStencilState.depthWriteEnable(pipeline.getDepthStencilState().writeDepth());
/* 155 */         depthStencilState.depthCompareOp(VulkanConst.toVk(pipeline.getDepthStencilState().depthTest()));
/*     */       } 
/* 158 */       ColorTargetState[] colorTargetStates = pipeline.getColorTargetStates();
/* 160 */       VkPipelineColorBlendAttachmentState.Buffer blendAttachments = VkPipelineColorBlendAttachmentState.calloc(colorTargetStates.length, stack);
/* 161 */       for (ColorTargetState colorTargetState : colorTargetStates) {
/* 162 */         blendAttachments.colorWriteMask((colorTargetState != null) ? VulkanConst.toVk(colorTargetState) : 0);
/* 163 */         if (colorTargetState != null && colorTargetState.blendFunction().isPresent())
/* 164 */           applyBlendInformation(blendAttachments, colorTargetState.blendFunction().get()); 
/* 166 */         blendAttachments.position(blendAttachments.position() + 1);
/*     */       } 
/* 168 */       blendAttachments.position(0);
/* 173 */       VkPipelineColorBlendStateCreateInfo colorBlendState = VkPipelineColorBlendStateCreateInfo.calloc(stack).sType$Default().pAttachments(blendAttachments);
/* 179 */       VkPipelineViewportStateCreateInfo viewportState = VkPipelineViewportStateCreateInfo.calloc(stack).sType$Default().scissorCount(1).viewportCount(1);
/* 185 */       VkPipelineMultisampleStateCreateInfo multisampleState = VkPipelineMultisampleStateCreateInfo.calloc(stack).sType$Default().rasterizationSamples(1).sampleShadingEnable(false);
/* 190 */       VkPipelineDynamicStateCreateInfo dynamicStateInfo = VkPipelineDynamicStateCreateInfo.calloc(stack).sType$Default().pDynamicStates(stack.ints(1, 0));
/* 195 */       VkPipelineRenderingCreateInfoKHR renderingInfo = VkPipelineRenderingCreateInfoKHR.calloc(stack).sType$Default();
/* 196 */       IntBuffer colorAttachmentFormats = stack.mallocInt(colorTargetStates.length);
/* 197 */       for (int j = 0; j < colorTargetStates.length; j++) {
/* 198 */         ColorTargetState colorTargetState = colorTargetStates[j];
/* 199 */         colorAttachmentFormats.put(j, (colorTargetState != null) ? VulkanConst.toVk(colorTargetState.format()) : 0);
/*     */       } 
/* 201 */       renderingInfo.pColorAttachmentFormats(colorAttachmentFormats);
/* 202 */       renderingInfo.depthAttachmentFormat(126);
/* 218 */       VkGraphicsPipelineCreateInfo.Buffer createInfo = VkGraphicsPipelineCreateInfo.calloc(1, stack).sType$Default().flags(0).pStages(shaderStages).pVertexInputState(vertexInputState).pInputAssemblyState(inputAssemblyState).pRasterizationState(rasterizationState).pDepthStencilState(depthStencilState).pColorBlendState(colorBlendState).pViewportState(viewportState).pMultisampleState(multisampleState).pDynamicState(dynamicStateInfo).layout(pipelineLayout).pNext(renderingInfo);
/* 220 */       LongBuffer pointer = stack.callocLong(1);
/* 221 */       VulkanUtils.crashIfFailure(device, VK12.vkCreateGraphicsPipelines(device.vkDevice(), 0L, createInfo, null, pointer), "Can't compile pipeline " + String.valueOf(pipeline.getLocation()));
/* 222 */       long withDepthPipeline = pointer.get(0);
/* 223 */       device.instance().debug().setObjectName(device.vkDevice(), 19, withDepthPipeline, () -> "Pipeline " + String.valueOf(pipeline.getLocation()));
/* 226 */       if (pipeline.getDepthStencilState() == null) {
/* 227 */         renderingInfo.depthAttachmentFormat(0);
/* 228 */         VulkanUtils.crashIfFailure(device, VK12.vkCreateGraphicsPipelines(device.vkDevice(), 0L, createInfo, null, pointer), "Can't compile pipeline " + String.valueOf(pipeline.getLocation()));
/* 229 */         withoutDepthPipeline = pointer.get(0);
/* 230 */         device.instance().debug().setObjectName(device.vkDevice(), 19, withoutDepthPipeline, () -> "Pipeline " + String.valueOf(pipeline.getLocation()));
/*     */       } else {
/* 232 */         withoutDepthPipeline = 0L;
/*     */       } 
/* 235 */       VulkanRenderPipeline vulkanRenderPipeline = new VulkanRenderPipeline(pipeline, device, withDepthPipeline, withoutDepthPipeline, pipelineLayout, layout, vertexModule, fragmentModule);
/* 236 */       if (stack != null)
/* 236 */         stack.close(); 
/*     */       return vulkanRenderPipeline;
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
/*     */   public void destroy() {
/* 241 */     if (this.withDepthPipeline == 0L)
/*     */       return; 
/* 244 */     VK12.vkDestroyPipeline(this.device.vkDevice(), this.withoutDepthPipeline, null);
/* 245 */     VK12.vkDestroyPipeline(this.device.vkDevice(), this.withDepthPipeline, null);
/* 246 */     VK12.vkDestroyPipelineLayout(this.device.vkDevice(), this.pipelineLayout, null);
/* 247 */     VK12.vkDestroyDescriptorSetLayout(this.device.vkDevice(), this.layout.handle(), null);
/* 248 */     VK12.vkDestroyShaderModule(this.device.vkDevice(), this.vertexModule, null);
/* 249 */     VK12.vkDestroyShaderModule(this.device.vkDevice(), this.fragmentModule, null);
/*     */   }
/*     */   
/*     */   private static void applyBlendInformation(VkPipelineColorBlendAttachmentState.Buffer blendAttachments, BlendFunction blendFunction) {
/* 253 */     blendAttachments
/* 254 */       .blendEnable(true)
/* 255 */       .colorBlendOp(VulkanConst.toVk(blendFunction.color().op()))
/* 256 */       .alphaBlendOp(VulkanConst.toVk(blendFunction.alpha().op()))
/* 257 */       .dstAlphaBlendFactor(VulkanConst.toVk(blendFunction.alpha().destFactor()))
/* 258 */       .dstColorBlendFactor(VulkanConst.toVk(blendFunction.color().destFactor()))
/* 259 */       .srcAlphaBlendFactor(VulkanConst.toVk(blendFunction.alpha().sourceFactor()))
/* 260 */       .srcColorBlendFactor(VulkanConst.toVk(blendFunction.color().sourceFactor()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanRenderPipeline.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */