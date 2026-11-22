/*     */ package net.minecraft.client.renderer.item;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*     */ import net.minecraft.client.resources.model.cuboid.ItemTransform;
/*     */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class ItemStackRenderState {
/*  29 */   ItemDisplayContext displayContext = ItemDisplayContext.NONE;
/*     */   
/*     */   private int activeLayerCount;
/*     */   
/*     */   private boolean animated;
/*     */   
/*     */   private boolean oversizedInGui;
/*     */   
/*     */   private AABB cachedModelBoundingBox;
/*     */   
/*  37 */   private LayerRenderState[] layers = new LayerRenderState[] { new LayerRenderState() };
/*     */   
/*     */   public void ensureCapacity(int requestedCount) {
/*  44 */     int currentCapacity = this.layers.length;
/*  45 */     int requiredNewCapacity = this.activeLayerCount + requestedCount;
/*  46 */     if (requiredNewCapacity > currentCapacity) {
/*  47 */       this.layers = Arrays.<LayerRenderState>copyOf(this.layers, requiredNewCapacity);
/*  48 */       for (int i = currentCapacity; i < requiredNewCapacity; i++)
/*  49 */         this.layers[i] = new LayerRenderState(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public LayerRenderState newLayer() {
/*  55 */     ensureCapacity(1);
/*  56 */     return this.layers[this.activeLayerCount++];
/*     */   }
/*     */   
/*     */   public void clear() {
/*  60 */     this.displayContext = ItemDisplayContext.NONE;
/*  63 */     for (int i = 0; i < this.activeLayerCount; i++)
/*  64 */       this.layers[i].clear(); 
/*  66 */     this.activeLayerCount = 0;
/*  67 */     this.animated = false;
/*  68 */     this.oversizedInGui = false;
/*  69 */     this.cachedModelBoundingBox = null;
/*     */   }
/*     */   
/*     */   public void setAnimated() {
/*  73 */     this.animated = true;
/*     */   }
/*     */   
/*     */   public boolean isAnimated() {
/*  77 */     return this.animated;
/*     */   }
/*     */   
/*     */   public void appendModelIdentityElement(Object element) {}
/*     */   
/*     */   private LayerRenderState firstLayer() {
/*  87 */     return this.layers[0];
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  91 */     return (this.activeLayerCount == 0);
/*     */   }
/*     */   
/*     */   public boolean usesBlockLight() {
/*  96 */     return (firstLayer()).usesBlockLight;
/*     */   }
/*     */   
/*     */   public Material.Baked pickParticleMaterial(RandomSource randomSource) {
/* 100 */     if (this.activeLayerCount == 0)
/* 101 */       return null; 
/* 103 */     return (this.layers[randomSource.nextInt(this.activeLayerCount)]).particleMaterial;
/*     */   }
/*     */   
/*     */   public void visitExtents(Consumer<Vector3fc> output) {
/* 107 */     Vector3f scratch = new Vector3f();
/* 108 */     PoseStack.Pose pose = new PoseStack.Pose();
/* 109 */     for (int i = 0; i < this.activeLayerCount; i++) {
/* 110 */       LayerRenderState layer = this.layers[i];
/* 111 */       layer.applyTransform(pose);
/* 112 */       Matrix4f poseTransform = pose.pose();
/* 113 */       Vector3fc[] layerExtents = layer.extents.get();
/* 114 */       for (Vector3fc extent : layerExtents)
/* 115 */         output.accept(scratch.set(extent).mulPosition((Matrix4fc)poseTransform)); 
/* 117 */       pose.setIdentity();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor) {
/* 122 */     for (int i = 0; i < this.activeLayerCount; i++)
/* 123 */       this.layers[i].submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor); 
/*     */   }
/*     */   
/*     */   public AABB getModelBoundingBox() {
/* 128 */     if (this.cachedModelBoundingBox != null)
/* 129 */       return this.cachedModelBoundingBox; 
/* 131 */     AABB.Builder collector = new AABB.Builder();
/* 132 */     Objects.requireNonNull(collector);
/* 132 */     visitExtents(collector::include);
/* 133 */     AABB aabb = collector.isDefined() ? collector.build() : AABB.ofSize(Vec3.ZERO, 0.0D, 0.0D, 0.0D);
/* 134 */     this.cachedModelBoundingBox = aabb;
/* 135 */     return aabb;
/*     */   }
/*     */   
/*     */   public void setOversizedInGui(boolean oversizedInGui) {
/* 139 */     this.oversizedInGui = oversizedInGui;
/*     */   }
/*     */   
/*     */   public boolean isOversizedInGui() {
/* 143 */     return this.oversizedInGui;
/*     */   }
/*     */   
/*     */   public enum FoilType {
/* 147 */     NONE, STANDARD, SPECIAL;
/*     */   }
/*     */   
/*     */   public class LayerRenderState {
/* 153 */     private static final Vector3fc[] NO_EXTENTS = new Vector3fc[0];
/*     */     
/*     */     public static final Supplier<Vector3fc[]> NO_EXTENTS_SUPPLIER = () -> NO_EXTENTS;
/*     */     
/* 155 */     public static final int[] EMPTY_TINTS = new int[0];
/*     */     
/* 157 */     private final List<BakedQuad> quads = new ArrayList<>();
/*     */     
/*     */     private boolean usesBlockLight;
/*     */     
/*     */     private Material.Baked particleMaterial;
/*     */     
/* 160 */     private ItemTransform itemTransform = ItemTransform.NO_TRANSFORM;
/*     */     
/* 161 */     private final Matrix4f localTransform = new Matrix4f();
/*     */     
/* 163 */     private ItemStackRenderState.FoilType foilType = ItemStackRenderState.FoilType.NONE;
/*     */     
/*     */     private IntList tintLayers;
/*     */     
/*     */     private SpecialModelRenderer<Object> specialRenderer;
/*     */     
/*     */     private Object argumentForSpecialRendering;
/*     */     
/* 170 */     private Supplier<Vector3fc[]> extents = NO_EXTENTS_SUPPLIER;
/*     */     
/*     */     public void clear() {
/* 173 */       this.quads.clear();
/* 174 */       this.foilType = ItemStackRenderState.FoilType.NONE;
/* 175 */       this.specialRenderer = null;
/* 176 */       this.argumentForSpecialRendering = null;
/* 177 */       if (this.tintLayers != null)
/* 178 */         this.tintLayers.clear(); 
/* 180 */       this.usesBlockLight = false;
/* 181 */       this.particleMaterial = null;
/* 182 */       this.itemTransform = ItemTransform.NO_TRANSFORM;
/* 183 */       this.localTransform.identity();
/* 184 */       this.extents = NO_EXTENTS_SUPPLIER;
/*     */     }
/*     */     
/*     */     public List<BakedQuad> prepareQuadList() {
/* 188 */       return this.quads;
/*     */     }
/*     */     
/*     */     public void setUsesBlockLight(boolean usesBlockLight) {
/* 192 */       this.usesBlockLight = usesBlockLight;
/*     */     }
/*     */     
/*     */     public void setExtents(Supplier<Vector3fc[]> extents) {
/* 196 */       this.extents = extents;
/*     */     }
/*     */     
/*     */     public void setParticleMaterial(Material.Baked particleMaterial) {
/* 200 */       this.particleMaterial = particleMaterial;
/*     */     }
/*     */     
/*     */     public void setItemTransform(ItemTransform transform) {
/* 204 */       this.itemTransform = transform;
/*     */     }
/*     */     
/*     */     public void setLocalTransform(Matrix4fc transform) {
/* 208 */       this.localTransform.set(transform);
/*     */     }
/*     */     
/*     */     public <T> void setupSpecialModel(SpecialModelRenderer<T> renderer, T argument) {
/* 212 */       this.specialRenderer = eraseSpecialRenderer(renderer);
/* 213 */       this.argumentForSpecialRendering = argument;
/*     */     }
/*     */     
/*     */     private static SpecialModelRenderer<Object> eraseSpecialRenderer(SpecialModelRenderer<?> renderer) {
/* 219 */       return (SpecialModelRenderer)renderer;
/*     */     }
/*     */     
/*     */     public void setFoilType(ItemStackRenderState.FoilType foilType) {
/* 223 */       this.foilType = foilType;
/*     */     }
/*     */     
/*     */     public IntList tintLayers() {
/* 227 */       if (this.tintLayers == null)
/* 228 */         this.tintLayers = (IntList)new IntArrayList(); 
/* 230 */       return this.tintLayers;
/*     */     }
/*     */     
/*     */     private void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor) {
/* 234 */       poseStack.pushPose();
/* 235 */       applyTransform(poseStack.last());
/* 237 */       if (this.specialRenderer != null) {
/* 238 */         this.specialRenderer.submit(this.argumentForSpecialRendering, poseStack, submitNodeCollector, lightCoords, overlayCoords, (this.foilType != ItemStackRenderState.FoilType.NONE), outlineColor);
/*     */       } else {
/* 240 */         int[] tints = (this.tintLayers != null) ? this.tintLayers.toArray(EMPTY_TINTS) : EMPTY_TINTS;
/* 241 */         submitNodeCollector.submitItem(poseStack, ItemStackRenderState.this.displayContext, lightCoords, overlayCoords, outlineColor, tints, this.quads, this.foilType);
/*     */       } 
/* 243 */       poseStack.popPose();
/*     */     }
/*     */     
/*     */     private void applyTransform(PoseStack.Pose localPose) {
/* 247 */       this.itemTransform.apply(ItemStackRenderState.this.displayContext.leftHand(), localPose);
/* 248 */       localPose.mulPose((Matrix4fc)this.localTransform);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\ItemStackRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */