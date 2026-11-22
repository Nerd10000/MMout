/*     */ package net.minecraft.client.renderer.rendertype;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class RenderSetupBuilder {
/*     */   private final RenderPipeline pipeline;
/*     */   
/*     */   private boolean useLightmap = false;
/*     */   
/*     */   private boolean useOverlay = false;
/*     */   
/* 110 */   private LayeringTransform layeringTransform = LayeringTransform.NO_LAYERING;
/*     */   
/* 111 */   private OutputTarget outputTarget = OutputTarget.MAIN_TARGET;
/*     */   
/* 112 */   private TextureTransform textureTransform = TextureTransform.DEFAULT_TEXTURING;
/*     */   
/*     */   private boolean affectsCrumbling = false;
/*     */   
/*     */   private boolean sortOnUpload = false;
/*     */   
/* 115 */   private RenderSetup.OutlineProperty outlineProperty = RenderSetup.OutlineProperty.NONE;
/*     */   
/* 116 */   private final Map<String, RenderSetup.TextureBinding> textures = new HashMap<>();
/*     */   
/*     */   private RenderSetupBuilder(RenderPipeline pipeline) {
/* 119 */     this.pipeline = pipeline;
/*     */   }
/*     */   
/*     */   public RenderSetupBuilder withTexture(String name, Identifier texture) {
/* 123 */     this.textures.put(name, new RenderSetup.TextureBinding(texture, () -> null));
/* 124 */     return this;
/*     */   }
/*     */   
/*     */   public RenderSetupBuilder withTexture(String name, Identifier texture, Supplier<GpuSampler> sampler) {
/* 128 */     this.textures.put(name, new RenderSetup.TextureBinding(texture, (Supplier<GpuSampler>)Suppliers.memoize(() -> (sampler == null) ? null : sampler.get())));
/* 129 */     return this;
/*     */   }
/*     */   
/*     */   public RenderSetupBuilder useLightmap() {
/* 133 */     this.useLightmap = true;
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public RenderSetupBuilder useOverlay() {
/* 138 */     this.useOverlay = true;
/* 139 */     return this;
/*     */   }
/*     */   
/*     */   public RenderSetupBuilder affectsCrumbling() {
/* 143 */     this.affectsCrumbling = true;
/* 144 */     return this;
/*     */   }
/*     */   
/*     */   public RenderSetupBuilder sortOnUpload() {
/* 148 */     this.sortOnUpload = true;
/* 149 */     return this;
/*     */   }
/*     */   
/*     */   public RenderSetupBuilder setLayeringTransform(LayeringTransform layeringTransform) {
/* 153 */     this.layeringTransform = layeringTransform;
/* 154 */     return this;
/*     */   }
/*     */   
/*     */   public RenderSetupBuilder setOutputTarget(OutputTarget outputTarget) {
/* 158 */     this.outputTarget = outputTarget;
/* 159 */     return this;
/*     */   }
/*     */   
/*     */   public RenderSetupBuilder setTextureTransform(TextureTransform textureTransform) {
/* 163 */     this.textureTransform = textureTransform;
/* 164 */     return this;
/*     */   }
/*     */   
/*     */   public RenderSetupBuilder setOutline(RenderSetup.OutlineProperty outlineProperty) {
/* 168 */     this.outlineProperty = outlineProperty;
/* 169 */     return this;
/*     */   }
/*     */   
/*     */   public RenderSetup createRenderSetup() {
/* 173 */     return new RenderSetup(this.pipeline, this.textures, this.useLightmap, this.useOverlay, this.layeringTransform, this.outputTarget, this.textureTransform, this.outlineProperty, this.affectsCrumbling, this.sortOnUpload);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\rendertype\RenderSetup$RenderSetupBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */