/*     */ package net.minecraft.client.renderer.feature;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.QuadInstance;
/*     */ import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.math.MatrixUtil;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
/*     */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.OutputTarget;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ public class ItemFeatureRenderer extends RenderTypeFeatureRenderer<ItemFeatureRenderer.Submit> {
/*  24 */   public static final FeatureRendererType<Submit> TYPE = FeatureRendererType.create("Item");
/*     */   
/*  26 */   public static final Identifier ENCHANTED_GLINT_ARMOR = Identifier.withDefaultNamespace("textures/misc/enchanted_glint_armor.png");
/*     */   
/*  27 */   public static final Identifier ENCHANTED_GLINT_ITEM = Identifier.withDefaultNamespace("textures/misc/enchanted_glint_item.png");
/*     */   
/*     */   private static final float SPECIAL_FOIL_UI_SCALE = 0.5F;
/*     */   
/*     */   private static final float SPECIAL_FOIL_FIRST_PERSON_SCALE = 0.75F;
/*     */   
/*     */   private static final float SPECIAL_FOIL_TEXTURE_SCALE = 0.0078125F;
/*     */   
/*     */   public static final int NO_TINT = -1;
/*     */   
/*  40 */   private final QuadInstance quadInstance = new QuadInstance();
/*     */   
/*     */   protected void buildGroup(FeatureFrameContext context, List<Submit> submits) {
/*  44 */     for (Submit submit : submits)
/*  45 */       prepareSubmit(submit, false); 
/*  47 */     for (Submit submit : submits)
/*  48 */       prepareSubmit(submit, true); 
/*     */   }
/*     */   
/*     */   private void prepareSubmit(Submit submit, boolean foil) {
/*  53 */     if (foil) {
/*  54 */       prepareFoilSubmit(submit);
/*  55 */     } else if (submit.outlineColor() != 0) {
/*  56 */       prepareOutlineSubmit(submit);
/*     */     } else {
/*  58 */       prepareMainSubmit(submit);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void prepareMainSubmit(Submit submit) {
/*  63 */     this.quadInstance.setLightCoords(submit.lightCoords());
/*  64 */     this.quadInstance.setOverlayCoords(submit.overlayCoords());
/*  66 */     for (BakedQuad quad : submit.quads()) {
/*  67 */       BakedQuad.MaterialInfo material = quad.materialInfo();
/*  68 */       RenderType renderType = material.itemRenderType();
/*  69 */       this.quadInstance.setColor(getLayerColorSafe(submit.tintLayers(), material));
/*  71 */       getVertexBuilder(renderType).putBakedQuad(submit.pose(), quad, this.quadInstance);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void prepareOutlineSubmit(Submit submit) {
/*  76 */     for (BakedQuad quad : submit.quads()) {
/*  77 */       BakedQuad.MaterialInfo material = quad.materialInfo();
/*  78 */       RenderType renderType = material.itemRenderType().outline().orElse(null);
/*  79 */       if (renderType == null)
/*     */         continue; 
/*  82 */       this.quadInstance.setColor(submit.outlineColor());
/*  83 */       getVertexBuilder(renderType).putBakedQuad(submit.pose(), quad, this.quadInstance);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void prepareFoilSubmit(Submit submit) {
/*  88 */     ItemStackRenderState.FoilType foilType = submit.foilType();
/*  89 */     if (foilType == ItemStackRenderState.FoilType.NONE)
/*     */       return; 
/*  92 */     PoseStack.Pose foilDecalPose = (foilType == ItemStackRenderState.FoilType.SPECIAL) ? computeFoilDecalPose(submit.displayContext(), submit.pose()) : null;
/*  93 */     for (BakedQuad quad : submit.quads()) {
/*  94 */       VertexConsumer foilBuffer = getFoilBuffer(quad.materialInfo().itemRenderType(), foilDecalPose);
/*  95 */       foilBuffer.putBakedQuad(submit.pose(), quad, this.quadInstance);
/*     */     } 
/*     */   }
/*     */   
/*     */   private VertexConsumer getFoilBuffer(RenderType renderType, PoseStack.Pose foilDecalPose) {
/*     */     SheetedDecalTextureGenerator sheetedDecalTextureGenerator;
/* 100 */     RenderType foilRenderType = useTransparentGlint(renderType) ? RenderTypes.glintTranslucent() : RenderTypes.glint();
/* 101 */     VertexConsumer foilBuffer = getVertexBuilder(foilRenderType);
/* 102 */     if (foilDecalPose != null)
/* 103 */       sheetedDecalTextureGenerator = new SheetedDecalTextureGenerator(foilBuffer, foilDecalPose, 0.0078125F); 
/* 105 */     return (VertexConsumer)sheetedDecalTextureGenerator;
/*     */   }
/*     */   
/*     */   private static PoseStack.Pose computeFoilDecalPose(ItemDisplayContext type, PoseStack.Pose pose) {
/* 109 */     PoseStack.Pose foilDecalPose = pose.copy();
/* 111 */     if (type == ItemDisplayContext.GUI) {
/* 112 */       MatrixUtil.mulComponentWise(foilDecalPose.pose(), 0.5F);
/* 113 */     } else if (type.firstPerson()) {
/* 114 */       MatrixUtil.mulComponentWise(foilDecalPose.pose(), 0.75F);
/*     */     } 
/* 116 */     return foilDecalPose;
/*     */   }
/*     */   
/*     */   private static boolean useTransparentGlint(RenderType renderType) {
/* 120 */     return ((Minecraft.getInstance()).gameRenderer.gameRenderState().useShaderTransparency() && renderType.outputTarget() == OutputTarget.ITEM_ENTITY_TARGET);
/*     */   }
/*     */   
/*     */   private static int getLayerColorSafe(int[] layers, int layer) {
/* 124 */     if (layer < 0 || layer >= layers.length)
/* 125 */       return -1; 
/* 127 */     return layers[layer];
/*     */   }
/*     */   
/*     */   private static int getLayerColorSafe(int[] tintLayers, BakedQuad.MaterialInfo material) {
/* 131 */     if (material.isTinted())
/* 132 */       return getLayerColorSafe(tintLayers, material.tintIndex()); 
/* 134 */     return -1;
/*     */   }
/*     */   
/*     */   public static final class Submit extends Record implements TranslucentSubmit {
/*     */     private final PoseStack.Pose pose;
/*     */     
/*     */     private final ItemDisplayContext displayContext;
/*     */     
/*     */     private final int lightCoords;
/*     */     
/*     */     private final int overlayCoords;
/*     */     
/*     */     private final int outlineColor;
/*     */     
/*     */     private final int[] tintLayers;
/*     */     
/*     */     private final List<BakedQuad> quads;
/*     */     
/*     */     private final ItemStackRenderState.FoilType foilType;
/*     */     
/*     */     public Submit(PoseStack.Pose pose, ItemDisplayContext displayContext, int lightCoords, int overlayCoords, int outlineColor, int[] tintLayers, List<BakedQuad> quads, ItemStackRenderState.FoilType foilType) {
/* 137 */       this.pose = pose;
/* 137 */       this.displayContext = displayContext;
/* 137 */       this.lightCoords = lightCoords;
/* 137 */       this.overlayCoords = overlayCoords;
/* 137 */       this.outlineColor = outlineColor;
/* 137 */       this.tintLayers = tintLayers;
/* 137 */       this.quads = quads;
/* 137 */       this.foilType = foilType;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer$Submit;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #137	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer$Submit;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer$Submit;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #137	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer$Submit;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #137	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer$Submit;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public PoseStack.Pose pose() {
/* 137 */       return this.pose;
/*     */     }
/*     */     
/*     */     public ItemDisplayContext displayContext() {
/* 137 */       return this.displayContext;
/*     */     }
/*     */     
/*     */     public int lightCoords() {
/* 137 */       return this.lightCoords;
/*     */     }
/*     */     
/*     */     public int overlayCoords() {
/* 137 */       return this.overlayCoords;
/*     */     }
/*     */     
/*     */     public int outlineColor() {
/* 137 */       return this.outlineColor;
/*     */     }
/*     */     
/*     */     public int[] tintLayers() {
/* 137 */       return this.tintLayers;
/*     */     }
/*     */     
/*     */     public List<BakedQuad> quads() {
/* 137 */       return this.quads;
/*     */     }
/*     */     
/*     */     public ItemStackRenderState.FoilType foilType() {
/* 137 */       return this.foilType;
/*     */     }
/*     */     
/*     */     public boolean hasTranslucency() {
/* 149 */       for (BakedQuad quad : quads()) {
/* 150 */         if (quad.materialInfo().itemRenderType().hasBlending())
/* 151 */           return true; 
/*     */       } 
/* 154 */       return false;
/*     */     }
/*     */     
/*     */     public float distanceToCameraSq() {
/* 159 */       return TranslucentSubmit.computeDistanceToCameraSq((Matrix4fc)this.pose.pose());
/*     */     }
/*     */     
/*     */     public FeatureRendererType<Submit> featureType() {
/* 164 */       return ItemFeatureRenderer.TYPE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\ItemFeatureRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */