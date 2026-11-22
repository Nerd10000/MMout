/*     */ package net.minecraft.client.renderer.block;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.MatrixUtil;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.util.LightCoordsUtil;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ public class BlockModelRenderState {
/*  23 */   public static final int[] EMPTY_TINTS = new int[0];
/*     */   
/*     */   private List<BlockStateModelPart> modelParts;
/*     */   
/*     */   private Matrix4fc transformation;
/*     */   
/*     */   private RenderType renderType;
/*     */   
/*     */   private SpecialModelRenderer<?> specialRenderer;
/*     */   
/*     */   private Matrix4fc specialRendererTransformation;
/*     */   
/*     */   private IntList tintLayers;
/*     */   
/*     */   public int blockLightCoords;
/*     */   
/*     */   private RandomSource randomSource;
/*     */   
/*     */   public void clear() {
/*  35 */     this.modelParts = null;
/*  36 */     this.transformation = null;
/*  37 */     this.renderType = null;
/*  38 */     this.specialRenderer = null;
/*  39 */     this.specialRendererTransformation = null;
/*  40 */     this.blockLightCoords = 0;
/*  41 */     if (this.tintLayers != null)
/*  42 */       this.tintLayers.clear(); 
/*     */   }
/*     */   
/*     */   public IntList tintLayers() {
/*  47 */     if (this.tintLayers == null)
/*  48 */       this.tintLayers = (IntList)new IntArrayList(); 
/*  50 */     return this.tintLayers;
/*     */   }
/*     */   
/*     */   public <T> void setupSpecialModel(SpecialModelRenderer<T> renderer, Matrix4fc transformation) {
/*  54 */     this.specialRenderer = renderer;
/*  55 */     this.specialRendererTransformation = identityToNull(transformation);
/*     */   }
/*     */   
/*     */   public List<BlockStateModelPart> setupModel(Matrix4fc transformation, boolean hasTranslucency) {
/*  59 */     this.transformation = identityToNull(transformation);
/*  61 */     this.renderType = hasTranslucency ? Sheets.translucentBlockItemSheet() : Sheets.cutoutBlockItemSheet();
/*  62 */     if (this.modelParts == null) {
/*  63 */       this.modelParts = (List<BlockStateModelPart>)new ObjectArrayList();
/*     */     } else {
/*  65 */       this.modelParts.clear();
/*     */     } 
/*  67 */     return this.modelParts;
/*     */   }
/*     */   
/*     */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int externalLightCoords, int overlayCoords, int outlineColor) {
/*  71 */     submitModel(this.renderType, poseStack, submitNodeCollector, externalLightCoords, overlayCoords, outlineColor);
/*  73 */     if (this.specialRenderer != null) {
/*  74 */       int lightCoords = LightCoordsUtil.max(externalLightCoords, this.blockLightCoords);
/*  75 */       if (this.specialRendererTransformation != null) {
/*  76 */         poseStack.pushPose();
/*  77 */         poseStack.mulPose(this.specialRendererTransformation);
/*  78 */         submitSpecialRenderer(this.specialRenderer, poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
/*  79 */         poseStack.popPose();
/*     */       } else {
/*  81 */         submitSpecialRenderer(this.specialRenderer, poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Matrix4fc identityToNull(Matrix4fc transformation) {
/*  87 */     if (MatrixUtil.checkPropertyRaw(transformation, 4))
/*  88 */       return null; 
/*  90 */     return transformation;
/*     */   }
/*     */   
/*     */   private void submitModel(RenderType renderType, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int externalLightCoords, int overlayCoords, int outlineColor) {
/*  94 */     if (this.modelParts != null && !this.modelParts.isEmpty()) {
/*  95 */       ObjectArrayList objectArrayList = new ObjectArrayList(this.modelParts);
/*  96 */       int[] tints = (this.tintLayers != null) ? this.tintLayers.toArray(EMPTY_TINTS) : EMPTY_TINTS;
/*  97 */       int lightCoords = LightCoordsUtil.max(externalLightCoords, this.blockLightCoords);
/*  99 */       if (this.transformation != null) {
/* 100 */         poseStack.pushPose();
/* 101 */         poseStack.mulPose(this.transformation);
/* 102 */         submitNodeCollector.submitBlockModel(poseStack, renderType, (List)objectArrayList, tints, lightCoords, overlayCoords, outlineColor);
/* 103 */         poseStack.popPose();
/*     */       } else {
/* 105 */         submitNodeCollector.submitBlockModel(poseStack, renderType, (List)objectArrayList, tints, lightCoords, overlayCoords, outlineColor);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void submitSpecialRenderer(SpecialModelRenderer<?> renderer, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor) {
/* 111 */     renderer.submit(null, poseStack, submitNodeCollector, lightCoords, overlayCoords, false, outlineColor);
/*     */   }
/*     */   
/*     */   public void submitOnlyOutline(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor) {
/* 116 */     submitModel(RenderTypes.outline(TextureAtlas.LOCATION_BLOCKS), poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
/*     */   }
/*     */   
/*     */   public void submitWithZOffset(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor) {
/* 122 */     submitModel(RenderTypes.entitySolidZOffsetForward(TextureAtlas.LOCATION_BLOCKS), poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 127 */     return (this.modelParts == null && this.specialRenderer == null);
/*     */   }
/*     */   
/*     */   public RandomSource scratchRandomSource(long seed) {
/* 131 */     if (this.randomSource == null) {
/* 132 */       this.randomSource = RandomSource.create(seed);
/*     */     } else {
/* 134 */       this.randomSource.setSeed(seed);
/*     */     } 
/* 136 */     return this.randomSource;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\BlockModelRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */