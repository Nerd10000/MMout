/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
/*     */ import net.minecraft.client.renderer.block.dispatch.ModelState;
/*     */ import net.minecraft.client.resources.model.cuboid.ItemTransforms;
/*     */ import net.minecraft.client.resources.model.geometry.QuadCollection;
/*     */ import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class ModelWrapper implements ResolvedModel {
/* 154 */   private static final ModelDiscovery.Slot<Boolean> KEY_AMBIENT_OCCLUSION = slot(0);
/*     */   
/* 155 */   private static final ModelDiscovery.Slot<UnbakedModel.GuiLight> KEY_GUI_LIGHT = slot(1);
/*     */   
/* 156 */   private static final ModelDiscovery.Slot<UnbakedGeometry> KEY_GEOMETRY = slot(2);
/*     */   
/* 157 */   private static final ModelDiscovery.Slot<ItemTransforms> KEY_TRANSFORMS = slot(3);
/*     */   
/* 158 */   private static final ModelDiscovery.Slot<TextureSlots> KEY_TEXTURE_SLOTS = slot(4);
/*     */   
/* 161 */   private static final ModelDiscovery.Slot<Material.Baked> KEY_PARTICLE_SPRITE = slot(5);
/*     */   
/* 162 */   private static final ModelDiscovery.Slot<QuadCollection> KEY_DEFAULT_GEOMETRY = slot(6);
/*     */   
/*     */   private static final int SLOT_COUNT = 7;
/*     */   
/*     */   private final Identifier id;
/*     */   
/*     */   private boolean valid;
/*     */   
/*     */   private ModelWrapper parent;
/*     */   
/*     */   private final UnbakedModel wrapped;
/*     */   
/*     */   private static <T> ModelDiscovery.Slot<T> slot(int index) {
/* 172 */     Objects.checkIndex(index, 7);
/* 173 */     return new ModelDiscovery.Slot<>(index);
/*     */   }
/*     */   
/* 176 */   private final AtomicReferenceArray<Object> fixedSlots = new AtomicReferenceArray(7);
/*     */   
/* 178 */   private final Map<ModelState, QuadCollection> modelBakeCache = new ConcurrentHashMap<>();
/*     */   
/*     */   private ModelWrapper(Identifier id, UnbakedModel wrapped, boolean valid) {
/* 181 */     this.id = id;
/* 182 */     this.wrapped = wrapped;
/* 183 */     this.valid = valid;
/*     */   }
/*     */   
/*     */   public UnbakedModel wrapped() {
/* 188 */     return this.wrapped;
/*     */   }
/*     */   
/*     */   public ResolvedModel parent() {
/* 193 */     return this.parent;
/*     */   }
/*     */   
/*     */   public String debugName() {
/* 198 */     return this.id.toString();
/*     */   }
/*     */   
/*     */   private <T> T getSlot(ModelDiscovery.Slot<T> key) {
/* 203 */     return (T)this.fixedSlots.get(key.index);
/*     */   }
/*     */   
/*     */   private <T> T updateSlot(ModelDiscovery.Slot<T> key, T value) {
/* 208 */     T currentValue = (T)this.fixedSlots.compareAndExchange(key.index, null, value);
/* 209 */     if (currentValue == null)
/* 211 */       return value; 
/* 214 */     return currentValue;
/*     */   }
/*     */   
/*     */   private <T> T getSimpleProperty(ModelDiscovery.Slot<T> key, Function<ResolvedModel, T> getter) {
/* 218 */     T result = getSlot(key);
/* 219 */     if (result != null)
/* 220 */       return result; 
/* 222 */     return updateSlot(key, getter.apply(this));
/*     */   }
/*     */   
/*     */   public boolean getTopAmbientOcclusion() {
/* 227 */     return ((Boolean)getSimpleProperty(KEY_AMBIENT_OCCLUSION, ResolvedModel::findTopAmbientOcclusion)).booleanValue();
/*     */   }
/*     */   
/*     */   public UnbakedModel.GuiLight getTopGuiLight() {
/* 232 */     return getSimpleProperty(KEY_GUI_LIGHT, ResolvedModel::findTopGuiLight);
/*     */   }
/*     */   
/*     */   public ItemTransforms getTopTransforms() {
/* 237 */     return getSimpleProperty(KEY_TRANSFORMS, ResolvedModel::findTopTransforms);
/*     */   }
/*     */   
/*     */   public UnbakedGeometry getTopGeometry() {
/* 242 */     return getSimpleProperty(KEY_GEOMETRY, ResolvedModel::findTopGeometry);
/*     */   }
/*     */   
/*     */   public TextureSlots getTopTextureSlots() {
/* 247 */     return getSimpleProperty(KEY_TEXTURE_SLOTS, ResolvedModel::findTopTextureSlots);
/*     */   }
/*     */   
/*     */   public Material.Baked resolveParticleMaterial(TextureSlots textureSlots, ModelBaker baker) {
/* 253 */     Material.Baked result = getSlot(KEY_PARTICLE_SPRITE);
/* 254 */     if (result != null)
/* 255 */       return result; 
/* 257 */     return updateSlot(KEY_PARTICLE_SPRITE, ResolvedModel.resolveParticleMaterial(textureSlots, baker, this));
/*     */   }
/*     */   
/*     */   private QuadCollection bakeDefaultState(TextureSlots textureSlots, ModelBaker baker, ModelState state) {
/* 262 */     QuadCollection result = getSlot(KEY_DEFAULT_GEOMETRY);
/* 263 */     if (result != null)
/* 264 */       return result; 
/* 266 */     return updateSlot(KEY_DEFAULT_GEOMETRY, getTopGeometry().bake(textureSlots, baker, state, this));
/*     */   }
/*     */   
/*     */   public QuadCollection bakeTopGeometry(TextureSlots textureSlots, ModelBaker baker, ModelState state) {
/* 271 */     if (state == BlockModelRotation.IDENTITY)
/* 272 */       return bakeDefaultState(textureSlots, baker, state); 
/* 276 */     return this.modelBakeCache.computeIfAbsent(state, s -> {
/*     */           UnbakedGeometry topGeometry = getTopGeometry();
/*     */           return topGeometry.bake(textureSlots, baker, s, this);
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ModelDiscovery$ModelWrapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */