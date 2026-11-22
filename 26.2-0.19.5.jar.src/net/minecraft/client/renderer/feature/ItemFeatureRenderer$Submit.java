/*     */ package net.minecraft.client.renderer.feature;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
/*     */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*     */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ public final class Submit extends Record implements TranslucentSubmit {
/*     */   private final PoseStack.Pose pose;
/*     */   
/*     */   private final ItemDisplayContext displayContext;
/*     */   
/*     */   private final int lightCoords;
/*     */   
/*     */   private final int overlayCoords;
/*     */   
/*     */   private final int outlineColor;
/*     */   
/*     */   private final int[] tintLayers;
/*     */   
/*     */   private final List<BakedQuad> quads;
/*     */   
/*     */   private final ItemStackRenderState.FoilType foilType;
/*     */   
/*     */   public Submit(PoseStack.Pose pose, ItemDisplayContext displayContext, int lightCoords, int overlayCoords, int outlineColor, int[] tintLayers, List<BakedQuad> quads, ItemStackRenderState.FoilType foilType) {
/* 137 */     this.pose = pose;
/* 137 */     this.displayContext = displayContext;
/* 137 */     this.lightCoords = lightCoords;
/* 137 */     this.overlayCoords = overlayCoords;
/* 137 */     this.outlineColor = outlineColor;
/* 137 */     this.tintLayers = tintLayers;
/* 137 */     this.quads = quads;
/* 137 */     this.foilType = foilType;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer$Submit;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #137	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer$Submit;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer$Submit;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #137	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer$Submit;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #137	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer$Submit;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public PoseStack.Pose pose() {
/* 137 */     return this.pose;
/*     */   }
/*     */   
/*     */   public ItemDisplayContext displayContext() {
/* 137 */     return this.displayContext;
/*     */   }
/*     */   
/*     */   public int lightCoords() {
/* 137 */     return this.lightCoords;
/*     */   }
/*     */   
/*     */   public int overlayCoords() {
/* 137 */     return this.overlayCoords;
/*     */   }
/*     */   
/*     */   public int outlineColor() {
/* 137 */     return this.outlineColor;
/*     */   }
/*     */   
/*     */   public int[] tintLayers() {
/* 137 */     return this.tintLayers;
/*     */   }
/*     */   
/*     */   public List<BakedQuad> quads() {
/* 137 */     return this.quads;
/*     */   }
/*     */   
/*     */   public ItemStackRenderState.FoilType foilType() {
/* 137 */     return this.foilType;
/*     */   }
/*     */   
/*     */   public boolean hasTranslucency() {
/* 149 */     for (BakedQuad quad : quads()) {
/* 150 */       if (quad.materialInfo().itemRenderType().hasBlending())
/* 151 */         return true; 
/*     */     } 
/* 154 */     return false;
/*     */   }
/*     */   
/*     */   public float distanceToCameraSq() {
/* 159 */     return TranslucentSubmit.computeDistanceToCameraSq((Matrix4fc)this.pose.pose());
/*     */   }
/*     */   
/*     */   public FeatureRendererType<Submit> featureType() {
/* 164 */     return ItemFeatureRenderer.TYPE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\ItemFeatureRenderer$Submit.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */