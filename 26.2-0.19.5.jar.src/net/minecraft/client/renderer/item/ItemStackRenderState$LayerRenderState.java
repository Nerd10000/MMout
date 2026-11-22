/*     */ package net.minecraft.client.renderer.item;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*     */ import net.minecraft.client.resources.model.cuboid.ItemTransform;
/*     */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class LayerRenderState {
/* 153 */   private static final Vector3fc[] NO_EXTENTS = new Vector3fc[0];
/*     */   
/*     */   public static final Supplier<Vector3fc[]> NO_EXTENTS_SUPPLIER = () -> NO_EXTENTS;
/*     */   
/* 155 */   public static final int[] EMPTY_TINTS = new int[0];
/*     */   
/* 157 */   private final List<BakedQuad> quads = new ArrayList<>();
/*     */   
/*     */   private boolean usesBlockLight;
/*     */   
/*     */   private Material.Baked particleMaterial;
/*     */   
/* 160 */   private ItemTransform itemTransform = ItemTransform.NO_TRANSFORM;
/*     */   
/* 161 */   private final Matrix4f localTransform = new Matrix4f();
/*     */   
/* 163 */   private ItemStackRenderState.FoilType foilType = ItemStackRenderState.FoilType.NONE;
/*     */   
/*     */   private IntList tintLayers;
/*     */   
/*     */   private SpecialModelRenderer<Object> specialRenderer;
/*     */   
/*     */   private Object argumentForSpecialRendering;
/*     */   
/* 170 */   private Supplier<Vector3fc[]> extents = NO_EXTENTS_SUPPLIER;
/*     */   
/*     */   public void clear() {
/* 173 */     this.quads.clear();
/* 174 */     this.foilType = ItemStackRenderState.FoilType.NONE;
/* 175 */     this.specialRenderer = null;
/* 176 */     this.argumentForSpecialRendering = null;
/* 177 */     if (this.tintLayers != null)
/* 178 */       this.tintLayers.clear(); 
/* 180 */     this.usesBlockLight = false;
/* 181 */     this.particleMaterial = null;
/* 182 */     this.itemTransform = ItemTransform.NO_TRANSFORM;
/* 183 */     this.localTransform.identity();
/* 184 */     this.extents = NO_EXTENTS_SUPPLIER;
/*     */   }
/*     */   
/*     */   public List<BakedQuad> prepareQuadList() {
/* 188 */     return this.quads;
/*     */   }
/*     */   
/*     */   public void setUsesBlockLight(boolean usesBlockLight) {
/* 192 */     this.usesBlockLight = usesBlockLight;
/*     */   }
/*     */   
/*     */   public void setExtents(Supplier<Vector3fc[]> extents) {
/* 196 */     this.extents = extents;
/*     */   }
/*     */   
/*     */   public void setParticleMaterial(Material.Baked particleMaterial) {
/* 200 */     this.particleMaterial = particleMaterial;
/*     */   }
/*     */   
/*     */   public void setItemTransform(ItemTransform transform) {
/* 204 */     this.itemTransform = transform;
/*     */   }
/*     */   
/*     */   public void setLocalTransform(Matrix4fc transform) {
/* 208 */     this.localTransform.set(transform);
/*     */   }
/*     */   
/*     */   public <T> void setupSpecialModel(SpecialModelRenderer<T> renderer, T argument) {
/* 212 */     this.specialRenderer = eraseSpecialRenderer(renderer);
/* 213 */     this.argumentForSpecialRendering = argument;
/*     */   }
/*     */   
/*     */   private static SpecialModelRenderer<Object> eraseSpecialRenderer(SpecialModelRenderer<?> renderer) {
/* 219 */     return (SpecialModelRenderer)renderer;
/*     */   }
/*     */   
/*     */   public void setFoilType(ItemStackRenderState.FoilType foilType) {
/* 223 */     this.foilType = foilType;
/*     */   }
/*     */   
/*     */   public IntList tintLayers() {
/* 227 */     if (this.tintLayers == null)
/* 228 */       this.tintLayers = (IntList)new IntArrayList(); 
/* 230 */     return this.tintLayers;
/*     */   }
/*     */   
/*     */   private void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor) {
/* 234 */     poseStack.pushPose();
/* 235 */     applyTransform(poseStack.last());
/* 237 */     if (this.specialRenderer != null) {
/* 238 */       this.specialRenderer.submit(this.argumentForSpecialRendering, poseStack, submitNodeCollector, lightCoords, overlayCoords, (this.foilType != ItemStackRenderState.FoilType.NONE), outlineColor);
/*     */     } else {
/* 240 */       int[] tints = (this.tintLayers != null) ? this.tintLayers.toArray(EMPTY_TINTS) : EMPTY_TINTS;
/* 241 */       submitNodeCollector.submitItem(poseStack, ItemStackRenderState.this.displayContext, lightCoords, overlayCoords, outlineColor, tints, this.quads, this.foilType);
/*     */     } 
/* 243 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private void applyTransform(PoseStack.Pose localPose) {
/* 247 */     this.itemTransform.apply(ItemStackRenderState.this.displayContext.leftHand(), localPose);
/* 248 */     localPose.mulPose((Matrix4fc)this.localTransform);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\ItemStackRenderState$LayerRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */