/*     */ package net.minecraft.client.renderer.rendertype;
/*     */ 
/*     */ import com.mojang.blaze3d.PrimitiveTopology;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.systems.ScissorState;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import org.joml.Matrix4f;
/*     */ 
/*     */ public class RenderType {
/*     */   private static final int MEGABYTE = 1048576;
/*     */   
/*     */   public static final int BIG_BUFFER_SIZE = 4194304;
/*     */   
/*     */   public static final int SMALL_BUFFER_SIZE = 786432;
/*     */   
/*     */   public static final int TRANSIENT_BUFFER_SIZE = 1536;
/*     */   
/*     */   private final RenderSetup state;
/*     */   
/*     */   private final Optional<RenderType> outline;
/*     */   
/*     */   protected final String name;
/*     */   
/*     */   private RenderType(String name, RenderSetup state) {
/*  31 */     this.name = name;
/*  32 */     this.state = state;
/*  33 */     this.outline = (state.outlineProperty == RenderSetup.OutlineProperty.AFFECTS_OUTLINE) ? state.textures.values().stream().findFirst().map(texture -> (RenderType)RenderTypes.OUTLINE.apply(texture.location(), Boolean.valueOf(state.pipeline.isCull()))) : Optional.<RenderType>empty();
/*     */   }
/*     */   
/*     */   static RenderType create(String name, RenderSetup state) {
/*  38 */     return new RenderType(name, state);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  43 */     return "RenderType[" + this.name + ":" + String.valueOf(this.state) + "]";
/*     */   }
/*     */   
/*     */   public boolean hasBlending() {
/*  47 */     return this.state.pipeline.getColorTargetState().blendFunction().isPresent();
/*     */   }
/*     */   
/*     */   public OutputTarget outputTarget() {
/*  51 */     return this.state.outputTarget;
/*     */   }
/*     */   
/*     */   public PreparedRenderType prepare() {
/*  56 */     Minecraft minecraft = Minecraft.getInstance();
/*  57 */     List<PreparedRenderType.Texture> textures = this.state.prepareTextures(minecraft
/*  58 */         .getTextureManager(), 
/*  59 */         RenderSystem.getSamplerCache(), minecraft.gameRenderer
/*  60 */         .overlayTexture().getTextureView(), minecraft.gameRenderer
/*  61 */         .lightmap());
/*  63 */     return new PreparedRenderType(this.state.pipeline, this.state.outputTarget, 
/*     */         
/*  66 */         writeDynamicTransforms(RenderSystem.getModelViewMatrixCopy()), new ScissorState(
/*  67 */           RenderSystem.getScissorStateForRenderTypeDraws()), textures);
/*     */   }
/*     */   
/*     */   private GpuBufferSlice writeDynamicTransforms(Matrix4f modelViewMatrix) {
/*  73 */     Consumer<Matrix4f> modelViewModifier = this.state.layeringTransform.getModifier();
/*  74 */     if (modelViewModifier != null)
/*  75 */       modelViewModifier.accept(modelViewMatrix); 
/*  77 */     return RenderSystem.getDynamicUniforms().writeTransform(modelViewMatrix, this.state.textureTransform.createMatrix());
/*     */   }
/*     */   
/*     */   public VertexFormat format() {
/*  81 */     return this.state.pipeline.getVertexFormatBinding(0);
/*     */   }
/*     */   
/*     */   public PrimitiveTopology primitiveTopology() {
/*  85 */     return this.state.pipeline.getPrimitiveTopology();
/*     */   }
/*     */   
/*     */   public Optional<RenderType> outline() {
/*  89 */     return this.outline;
/*     */   }
/*     */   
/*     */   public boolean isOutline() {
/*  93 */     return (this.state.outlineProperty == RenderSetup.OutlineProperty.IS_OUTLINE);
/*     */   }
/*     */   
/*     */   public RenderPipeline pipeline() {
/*  97 */     return this.state.pipeline;
/*     */   }
/*     */   
/*     */   public boolean affectsCrumbling() {
/* 101 */     return this.state.affectsCrumbling;
/*     */   }
/*     */   
/*     */   public boolean canConsolidateConsecutiveGeometry() {
/* 105 */     return !(primitiveTopology()).connectedPrimitives;
/*     */   }
/*     */   
/*     */   public boolean sortOnUpload() {
/* 109 */     return this.state.sortOnUpload;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\rendertype\RenderType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */