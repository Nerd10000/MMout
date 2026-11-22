/*     */ package net.minecraft.client.renderer.rendertype;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.systems.SamplerCache;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public final class RenderSetup {
/*     */   final RenderPipeline pipeline;
/*     */   
/*     */   final Map<String, TextureBinding> textures;
/*     */   
/*     */   final TextureTransform textureTransform;
/*     */   
/*     */   final OutputTarget outputTarget;
/*     */   
/*     */   final OutlineProperty outlineProperty;
/*     */   
/*     */   final boolean useLightmap;
/*     */   
/*     */   final boolean useOverlay;
/*     */   
/*     */   final boolean affectsCrumbling;
/*     */   
/*     */   final boolean sortOnUpload;
/*     */   
/*     */   final LayeringTransform layeringTransform;
/*     */   
/*     */   private RenderSetup(RenderPipeline pipeline, Map<String, TextureBinding> textures, boolean useLightmap, boolean useOverlay, LayeringTransform layeringTransform, OutputTarget outputTarget, TextureTransform textureTransform, OutlineProperty outlineProperty, boolean affectsCrumbling, boolean sortOnUpload) {
/*  48 */     this.pipeline = pipeline;
/*  49 */     this.textures = textures;
/*  50 */     this.outputTarget = outputTarget;
/*  51 */     this.textureTransform = textureTransform;
/*  52 */     this.useLightmap = useLightmap;
/*  53 */     this.useOverlay = useOverlay;
/*  54 */     this.outlineProperty = outlineProperty;
/*  55 */     this.layeringTransform = layeringTransform;
/*  56 */     this.affectsCrumbling = affectsCrumbling;
/*  57 */     this.sortOnUpload = sortOnUpload;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  62 */     return "RenderSetup[layeringTransform=" + String.valueOf(this.layeringTransform) + ", textureTransform=" + String.valueOf(this.textureTransform) + ", textures=" + String.valueOf(this.textures) + ", outlineProperty=" + String.valueOf(this.outlineProperty) + ", useLightmap=" + this.useLightmap + ", useOverlay=" + this.useOverlay + "]";
/*     */   }
/*     */   
/*     */   public static RenderSetupBuilder builder(RenderPipeline pipeline) {
/*  66 */     return new RenderSetupBuilder(pipeline);
/*     */   }
/*     */   
/*     */   public List<PreparedRenderType.Texture> prepareTextures(TextureManager textureManager, SamplerCache samplerCache, GpuTextureView overlayTexture, GpuTextureView lightmapTexture) {
/*  70 */     if (this.textures.isEmpty() && !this.useOverlay && !this.useLightmap)
/*  71 */       return List.of(); 
/*  73 */     ImmutableList.Builder<PreparedRenderType.Texture> textures = ImmutableList.builderWithExpectedSize(this.textures.size() + 2);
/*  74 */     if (this.useOverlay)
/*  75 */       textures.add(new PreparedRenderType.Texture("Sampler1", overlayTexture, samplerCache.getClampToEdge(FilterMode.LINEAR))); 
/*  77 */     if (this.useLightmap)
/*  78 */       textures.add(new PreparedRenderType.Texture("Sampler2", lightmapTexture, samplerCache.getClampToEdge(FilterMode.LINEAR))); 
/*  80 */     for (Map.Entry<String, TextureBinding> entry : this.textures.entrySet()) {
/*  81 */       AbstractTexture texture = textureManager.getTexture(((TextureBinding)entry.getValue()).location);
/*  82 */       GpuSampler samplerOverride = ((TextureBinding)entry.getValue()).sampler().get();
/*  83 */       textures.add(new PreparedRenderType.Texture(entry.getKey(), texture.getTextureView(), (samplerOverride != null) ? samplerOverride : texture.getSampler()));
/*     */     } 
/*  85 */     return (List<PreparedRenderType.Texture>)textures.build();
/*     */   }
/*     */   
/*     */   public enum OutlineProperty {
/*  89 */     NONE("none"),
/*  90 */     IS_OUTLINE("is_outline"),
/*  91 */     AFFECTS_OUTLINE("affects_outline");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     OutlineProperty(String name) {
/*  97 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 102 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RenderSetupBuilder {
/*     */     private final RenderPipeline pipeline;
/*     */     
/*     */     private boolean useLightmap = false;
/*     */     
/*     */     private boolean useOverlay = false;
/*     */     
/* 110 */     private LayeringTransform layeringTransform = LayeringTransform.NO_LAYERING;
/*     */     
/* 111 */     private OutputTarget outputTarget = OutputTarget.MAIN_TARGET;
/*     */     
/* 112 */     private TextureTransform textureTransform = TextureTransform.DEFAULT_TEXTURING;
/*     */     
/*     */     private boolean affectsCrumbling = false;
/*     */     
/*     */     private boolean sortOnUpload = false;
/*     */     
/* 115 */     private RenderSetup.OutlineProperty outlineProperty = RenderSetup.OutlineProperty.NONE;
/*     */     
/* 116 */     private final Map<String, RenderSetup.TextureBinding> textures = new HashMap<>();
/*     */     
/*     */     private RenderSetupBuilder(RenderPipeline pipeline) {
/* 119 */       this.pipeline = pipeline;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder withTexture(String name, Identifier texture) {
/* 123 */       this.textures.put(name, new RenderSetup.TextureBinding(texture, () -> null));
/* 124 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder withTexture(String name, Identifier texture, Supplier<GpuSampler> sampler) {
/* 128 */       this.textures.put(name, new RenderSetup.TextureBinding(texture, (Supplier<GpuSampler>)Suppliers.memoize(() -> (sampler == null) ? null : sampler.get())));
/* 129 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder useLightmap() {
/* 133 */       this.useLightmap = true;
/* 134 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder useOverlay() {
/* 138 */       this.useOverlay = true;
/* 139 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder affectsCrumbling() {
/* 143 */       this.affectsCrumbling = true;
/* 144 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder sortOnUpload() {
/* 148 */       this.sortOnUpload = true;
/* 149 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder setLayeringTransform(LayeringTransform layeringTransform) {
/* 153 */       this.layeringTransform = layeringTransform;
/* 154 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder setOutputTarget(OutputTarget outputTarget) {
/* 158 */       this.outputTarget = outputTarget;
/* 159 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder setTextureTransform(TextureTransform textureTransform) {
/* 163 */       this.textureTransform = textureTransform;
/* 164 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder setOutline(RenderSetup.OutlineProperty outlineProperty) {
/* 168 */       this.outlineProperty = outlineProperty;
/* 169 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetup createRenderSetup() {
/* 173 */       return new RenderSetup(this.pipeline, this.textures, this.useLightmap, this.useOverlay, this.layeringTransform, this.outputTarget, this.textureTransform, this.outlineProperty, this.affectsCrumbling, this.sortOnUpload);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class TextureBinding extends Record {
/*     */     private final Identifier location;
/*     */     
/*     */     private final Supplier<GpuSampler> sampler;
/*     */     
/*     */     public Supplier<GpuSampler> sampler() {
/* 177 */       return this.sampler;
/*     */     }
/*     */     
/*     */     public Identifier location() {
/* 177 */       return this.location;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #177	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #177	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #177	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding;
/*     */     }
/*     */     
/*     */     TextureBinding(Identifier location, Supplier<GpuSampler> sampler) {
/* 178 */       this.location = location;
/* 178 */       this.sampler = sampler;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\rendertype\RenderSetup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */