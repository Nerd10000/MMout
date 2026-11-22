/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.ProjectionType;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.Std140Builder;
/*     */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*     */ import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
/*     */ import com.mojang.blaze3d.framegraph.FramePass;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.resource.ResourceHandle;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.systems.SamplerCache;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalDouble;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ 
/*     */ public class PostPass implements AutoCloseable {
/*  31 */   private static final int UBO_SIZE_PER_SAMPLER = (new Std140SizeCalculator()).putVec2().get();
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final RenderPipeline pipeline;
/*     */   
/*     */   private final Identifier outputTargetId;
/*     */   
/*  35 */   private final Map<String, GpuBuffer> customUniforms = new HashMap<>();
/*     */   
/*     */   private final MappableRingBuffer infoUbo;
/*     */   
/*     */   private final List<Input> inputs;
/*     */   
/*     */   public PostPass(RenderPipeline pipeline, Identifier outputTargetId, Map<String, List<UniformValue>> uniformGroups, List<Input> inputs) {
/*  40 */     this.pipeline = pipeline;
/*  41 */     this.name = pipeline.getLocation().toString();
/*  42 */     this.outputTargetId = outputTargetId;
/*  43 */     this.inputs = inputs;
/*  45 */     for (Map.Entry<String, List<UniformValue>> uniformGroup : uniformGroups.entrySet()) {
/*  46 */       List<UniformValue> uniforms = uniformGroup.getValue();
/*  47 */       if (uniforms.isEmpty())
/*     */         continue; 
/*  50 */       Std140SizeCalculator calculator = new Std140SizeCalculator();
/*  51 */       for (UniformValue uniform : uniforms)
/*  52 */         uniform.addSize(calculator); 
/*  54 */       int size = calculator.get();
/*  55 */       MemoryStack stack = MemoryStack.stackPush();
/*     */       try {
/*  56 */         Std140Builder builder = Std140Builder.onStack(stack, size);
/*  57 */         for (UniformValue uniform : uniforms)
/*  58 */           uniform.writeTo(builder); 
/*  60 */         this.customUniforms.put(uniformGroup.getKey(), RenderSystem.getDevice().createBuffer(() -> this.name + " / " + this.name, 128, builder.get()));
/*  61 */         if (stack != null)
/*  61 */           stack.close(); 
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
/*  64 */     this.infoUbo = new MappableRingBuffer(() -> this.name + " SamplerInfo", 130, (inputs.size() + 1) * UBO_SIZE_PER_SAMPLER);
/*     */   }
/*     */   
/*     */   public void addToFrame(FrameGraphBuilder frame, Map<Identifier, ResourceHandle<RenderTarget>> targets, GpuBufferSlice shaderOrthoMatrix) {
/*  68 */     FramePass pass = frame.addPass(this.name);
/*  70 */     for (Input input : this.inputs)
/*  71 */       input.addToPass(pass, targets); 
/*  74 */     ResourceHandle<RenderTarget> outputHandle = targets.computeIfPresent(this.outputTargetId, (id, handle) -> pass.readsAndWrites(handle));
/*  75 */     if (outputHandle == null)
/*  76 */       throw new IllegalStateException("Missing handle for target " + String.valueOf(this.outputTargetId)); 
/*  79 */     pass.executes(() -> {
/*     */           RenderTarget outputTarget = (RenderTarget)outputHandle.get();
/*     */           RenderSystem.backupProjectionMatrix();
/*     */           RenderSystem.setProjectionMatrix(shaderOrthoMatrix, ProjectionType.ORTHOGRAPHIC);
/*     */           CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
/*     */           SamplerCache samplerCache = RenderSystem.getSamplerCache();
/*     */           List<InputTexture> inputTextures = this.inputs.stream().map(()).toList();
/*     */           GpuBufferSlice.MappedView view = this.infoUbo.currentBuffer().map(false, true);
/*     */           try {
/*     */             Std140Builder builder = Std140Builder.intoBuffer(view.data());
/*     */             builder.putVec2(outputTarget.width, outputTarget.height);
/*     */             for (InputTexture input : inputTextures)
/*     */               builder.putVec2(input.view.getWidth(0), input.view.getHeight(0)); 
/*     */             if (view != null)
/*     */               view.close(); 
/*  88 */           } catch (Throwable t$) {
/*     */             if (view != null)
/*     */               try {
/*     */                 view.close();
/*  88 */               } catch (Throwable x2) {
/*     */                 t$.addSuppressed(x2);
/*     */               }  
/*     */             throw t$;
/*     */           } 
/*     */           RenderPass renderPass = commandEncoder.createRenderPass((), outputTarget.getColorTextureView(), Optional.empty(), outputTarget.useDepth ? outputTarget.getDepthTextureView() : null, OptionalDouble.empty());
/*     */           try {
/*     */             renderPass.setPipeline(this.pipeline);
/*     */             RenderSystem.bindDefaultUniforms(renderPass);
/*     */             renderPass.setUniform("SamplerInfo", this.infoUbo.currentBuffer());
/*     */             for (Map.Entry<String, GpuBuffer> entry : this.customUniforms.entrySet())
/*     */               renderPass.setUniform(entry.getKey(), entry.getValue()); 
/*     */             for (InputTexture input : inputTextures)
/*     */               renderPass.bindTexture(input.samplerName() + "Sampler", input.view(), input.sampler()); 
/*     */             renderPass.draw(3, 1, 0, 0);
/*     */             if (renderPass != null)
/*     */               renderPass.close(); 
/*  97 */           } catch (Throwable t$) {
/*     */             if (renderPass != null)
/*     */               try {
/*     */                 renderPass.close();
/*  97 */               } catch (Throwable x2) {
/*     */                 t$.addSuppressed(x2);
/*     */               }  
/*     */             throw t$;
/*     */           } 
/*     */           this.infoUbo.rotate();
/*     */           RenderSystem.restoreProjectionMatrix();
/*     */           for (Input input : this.inputs)
/*     */             input.cleanup(targets); 
/*     */         });
/*     */   }
/*     */   
/*     */   public void close() {
/* 121 */     for (GpuBuffer buffer : this.customUniforms.values())
/* 122 */       buffer.close(); 
/* 124 */     this.infoUbo.close();
/*     */   }
/*     */   
/*     */   public static interface Input {
/*     */     void addToPass(FramePass param1FramePass, Map<Identifier, ResourceHandle<RenderTarget>> param1Map);
/*     */     
/*     */     default void cleanup(Map<Identifier, ResourceHandle<RenderTarget>> targets) {}
/*     */     
/*     */     GpuTextureView texture(Map<Identifier, ResourceHandle<RenderTarget>> param1Map);
/*     */     
/*     */     String samplerName();
/*     */     
/*     */     boolean bilinear();
/*     */   }
/*     */   
/*     */   public static final class TextureInput extends Record implements Input {
/*     */     private final String samplerName;
/*     */     
/*     */     private final AbstractTexture texture;
/*     */     
/*     */     private final int width;
/*     */     
/*     */     private final int height;
/*     */     
/*     */     private final boolean bilinear;
/*     */     
/*     */     public TextureInput(String samplerName, AbstractTexture texture, int width, int height, boolean bilinear) {
/* 140 */       this.samplerName = samplerName;
/* 140 */       this.texture = texture;
/* 140 */       this.width = width;
/* 140 */       this.height = height;
/* 140 */       this.bilinear = bilinear;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostPass$TextureInput;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #140	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostPass$TextureInput;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostPass$TextureInput;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #140	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostPass$TextureInput;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostPass$TextureInput;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #140	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/PostPass$TextureInput;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String samplerName() {
/* 140 */       return this.samplerName;
/*     */     }
/*     */     
/*     */     public AbstractTexture texture() {
/* 140 */       return this.texture;
/*     */     }
/*     */     
/*     */     public int width() {
/* 140 */       return this.width;
/*     */     }
/*     */     
/*     */     public int height() {
/* 140 */       return this.height;
/*     */     }
/*     */     
/*     */     public void addToPass(FramePass pass, Map<Identifier, ResourceHandle<RenderTarget>> targets) {}
/*     */     
/*     */     public GpuTextureView texture(Map<Identifier, ResourceHandle<RenderTarget>> targets) {
/* 148 */       return this.texture.getTextureView();
/*     */     }
/*     */     
/*     */     public boolean bilinear() {
/* 153 */       return this.bilinear;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class TargetInput extends Record implements Input {
/*     */     private final String samplerName;
/*     */     
/*     */     private final Identifier targetId;
/*     */     
/*     */     private final boolean depthBuffer;
/*     */     
/*     */     private final boolean bilinear;
/*     */     
/*     */     public TargetInput(String samplerName, Identifier targetId, boolean depthBuffer, boolean bilinear) {
/* 157 */       this.samplerName = samplerName;
/* 157 */       this.targetId = targetId;
/* 157 */       this.depthBuffer = depthBuffer;
/* 157 */       this.bilinear = bilinear;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostPass$TargetInput;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #157	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostPass$TargetInput;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostPass$TargetInput;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #157	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostPass$TargetInput;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostPass$TargetInput;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #157	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/PostPass$TargetInput;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String samplerName() {
/* 157 */       return this.samplerName;
/*     */     }
/*     */     
/*     */     public Identifier targetId() {
/* 157 */       return this.targetId;
/*     */     }
/*     */     
/*     */     public boolean depthBuffer() {
/* 157 */       return this.depthBuffer;
/*     */     }
/*     */     
/*     */     public boolean bilinear() {
/* 157 */       return this.bilinear;
/*     */     }
/*     */     
/*     */     private ResourceHandle<RenderTarget> getHandle(Map<Identifier, ResourceHandle<RenderTarget>> targets) {
/* 159 */       ResourceHandle<RenderTarget> handle = targets.get(this.targetId);
/* 160 */       if (handle == null)
/* 161 */         throw new IllegalStateException("Missing handle for target " + String.valueOf(this.targetId)); 
/* 163 */       return handle;
/*     */     }
/*     */     
/*     */     public void addToPass(FramePass pass, Map<Identifier, ResourceHandle<RenderTarget>> targets) {
/* 168 */       pass.reads(getHandle(targets));
/*     */     }
/*     */     
/*     */     public GpuTextureView texture(Map<Identifier, ResourceHandle<RenderTarget>> targets) {
/* 173 */       ResourceHandle<RenderTarget> handle = getHandle(targets);
/* 174 */       RenderTarget target = (RenderTarget)handle.get();
/* 175 */       GpuTextureView textureView = this.depthBuffer ? target.getDepthTextureView() : target.getColorTextureView();
/* 176 */       if (textureView == null)
/* 177 */         throw new IllegalStateException("Missing " + (this.depthBuffer ? "depth" : "color") + "texture for target " + String.valueOf(this.targetId)); 
/* 179 */       return textureView;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class InputTexture extends Record {
/*     */     private final String samplerName;
/*     */     
/*     */     private final GpuTextureView view;
/*     */     
/*     */     private final GpuSampler sampler;
/*     */     
/*     */     private InputTexture(String samplerName, GpuTextureView view, GpuSampler sampler) {
/* 183 */       this.samplerName = samplerName;
/* 183 */       this.view = view;
/* 183 */       this.sampler = sampler;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostPass$InputTexture;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #183	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostPass$InputTexture;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostPass$InputTexture;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #183	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostPass$InputTexture;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostPass$InputTexture;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #183	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/PostPass$InputTexture;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String samplerName() {
/* 183 */       return this.samplerName;
/*     */     }
/*     */     
/*     */     public GpuTextureView view() {
/* 183 */       return this.view;
/*     */     }
/*     */     
/*     */     public GpuSampler sampler() {
/* 183 */       return this.sampler;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PostPass.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */