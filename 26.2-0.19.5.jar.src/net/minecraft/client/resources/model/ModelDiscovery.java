/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectFunction;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
/*     */ import net.minecraft.client.renderer.block.dispatch.ModelState;
/*     */ import net.minecraft.client.resources.model.cuboid.ItemTransforms;
/*     */ import net.minecraft.client.resources.model.cuboid.MissingCuboidModel;
/*     */ import net.minecraft.client.resources.model.geometry.QuadCollection;
/*     */ import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ModelDiscovery {
/*  33 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  35 */   private final Object2ObjectMap<Identifier, ModelWrapper> modelWrappers = (Object2ObjectMap<Identifier, ModelWrapper>)new Object2ObjectOpenHashMap();
/*     */   
/*     */   private final ModelWrapper missingModel;
/*     */   
/*     */   private final Object2ObjectFunction<Identifier, ModelWrapper> uncachedResolver;
/*     */   
/*     */   private final ResolvableModel.Resolver resolver;
/*     */   
/*  41 */   private final Queue<ModelWrapper> parentDiscoveryQueue = new ArrayDeque<>();
/*     */   
/*     */   public ModelDiscovery(Map<Identifier, UnbakedModel> unbakedModels, UnbakedModel missingUnbakedModel) {
/*  44 */     this.missingModel = new ModelWrapper(MissingCuboidModel.LOCATION, missingUnbakedModel, true);
/*  45 */     this.modelWrappers.put(MissingCuboidModel.LOCATION, this.missingModel);
/*  47 */     this.uncachedResolver = (rawId -> {
/*     */         Identifier id = (Identifier)rawId;
/*     */         UnbakedModel rawModel = (UnbakedModel)unbakedModels.get(id);
/*     */         if (rawModel == null) {
/*     */           LOGGER.warn("Missing block model: {}", id);
/*     */           return this.missingModel;
/*     */         } 
/*     */         return createAndQueueWrapper(id, rawModel);
/*     */       });
/*  58 */     this.resolver = this::getOrCreateModel;
/*     */   }
/*     */   
/*     */   private static boolean isRoot(UnbakedModel model) {
/*  62 */     return (model.parent() == null);
/*     */   }
/*     */   
/*     */   private ModelWrapper getOrCreateModel(Identifier id) {
/*  66 */     return (ModelWrapper)this.modelWrappers.computeIfAbsent(id, this.uncachedResolver);
/*     */   }
/*     */   
/*     */   private ModelWrapper createAndQueueWrapper(Identifier id, UnbakedModel rawModel) {
/*  70 */     boolean isRoot = isRoot(rawModel);
/*  71 */     ModelWrapper result = new ModelWrapper(id, rawModel, isRoot);
/*  72 */     if (!isRoot)
/*  73 */       this.parentDiscoveryQueue.add(result); 
/*  75 */     return result;
/*     */   }
/*     */   
/*     */   public void addRoot(ResolvableModel model) {
/*  79 */     model.resolveDependencies(this.resolver);
/*     */   }
/*     */   
/*     */   public void addSpecialModel(Identifier id, UnbakedModel model) {
/*  83 */     if (!isRoot(model)) {
/*  85 */       LOGGER.warn("Trying to add non-root special model {}, ignoring", id);
/*     */       return;
/*     */     } 
/*  88 */     ModelWrapper previous = (ModelWrapper)this.modelWrappers.put(id, createAndQueueWrapper(id, model));
/*  89 */     if (previous != null)
/*  90 */       LOGGER.warn("Duplicate special model {}", id); 
/*     */   }
/*     */   
/*     */   public ResolvedModel missingModel() {
/*  95 */     return this.missingModel;
/*     */   }
/*     */   
/*     */   public Map<Identifier, ResolvedModel> resolve() {
/*  99 */     List<ModelWrapper> toValidate = new ArrayList<>();
/* 101 */     discoverDependencies(toValidate);
/* 104 */     propagateValidity(toValidate);
/* 106 */     ImmutableMap.Builder<Identifier, ResolvedModel> result = ImmutableMap.builder();
/* 107 */     this.modelWrappers.forEach((location, model) -> {
/*     */           if (model.valid) {
/*     */             result.put(location, model);
/*     */           } else {
/*     */             LOGGER.warn("Model {} ignored due to cyclic dependency", location);
/*     */           } 
/*     */         });
/* 114 */     return (Map<Identifier, ResolvedModel>)result.build();
/*     */   }
/*     */   
/*     */   private void discoverDependencies(List<ModelWrapper> toValidate) {
/*     */     ModelWrapper current;
/* 119 */     while ((current = this.parentDiscoveryQueue.poll()) != null) {
/* 121 */       Identifier parentLocation = Objects.<Identifier>requireNonNull(current.wrapped.parent());
/* 122 */       ModelWrapper parent = getOrCreateModel(parentLocation);
/* 123 */       current.parent = parent;
/* 124 */       if (parent.valid) {
/* 126 */         current.valid = true;
/*     */         continue;
/*     */       } 
/* 128 */       toValidate.add(current);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void propagateValidity(List<ModelWrapper> toValidate) {
/* 134 */     boolean progressed = true;
/* 135 */     while (progressed) {
/* 136 */       progressed = false;
/* 137 */       Iterator<ModelWrapper> iterator = toValidate.iterator();
/* 138 */       while (iterator.hasNext()) {
/* 139 */         ModelWrapper model = iterator.next();
/* 141 */         if (((ModelWrapper)Objects.requireNonNull((T)model.parent)).valid) {
/* 142 */           model.valid = true;
/* 143 */           iterator.remove();
/* 144 */           progressed = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class Slot<T> extends Record {
/*     */     private final int index;
/*     */     
/*     */     private Slot(int index) {
/* 150 */       this.index = index;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ModelDiscovery$Slot;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #150	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelDiscovery$Slot;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelDiscovery$Slot<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ModelDiscovery$Slot;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #150	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelDiscovery$Slot;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelDiscovery$Slot<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ModelDiscovery$Slot;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #150	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelDiscovery$Slot;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelDiscovery$Slot<TT;>;
/*     */     }
/*     */     
/*     */     public int index() {
/* 150 */       return this.index;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ModelWrapper implements ResolvedModel {
/* 154 */     private static final ModelDiscovery.Slot<Boolean> KEY_AMBIENT_OCCLUSION = slot(0);
/*     */     
/* 155 */     private static final ModelDiscovery.Slot<UnbakedModel.GuiLight> KEY_GUI_LIGHT = slot(1);
/*     */     
/* 156 */     private static final ModelDiscovery.Slot<UnbakedGeometry> KEY_GEOMETRY = slot(2);
/*     */     
/* 157 */     private static final ModelDiscovery.Slot<ItemTransforms> KEY_TRANSFORMS = slot(3);
/*     */     
/* 158 */     private static final ModelDiscovery.Slot<TextureSlots> KEY_TEXTURE_SLOTS = slot(4);
/*     */     
/* 161 */     private static final ModelDiscovery.Slot<Material.Baked> KEY_PARTICLE_SPRITE = slot(5);
/*     */     
/* 162 */     private static final ModelDiscovery.Slot<QuadCollection> KEY_DEFAULT_GEOMETRY = slot(6);
/*     */     
/*     */     private static final int SLOT_COUNT = 7;
/*     */     
/*     */     private final Identifier id;
/*     */     
/*     */     private boolean valid;
/*     */     
/*     */     private ModelWrapper parent;
/*     */     
/*     */     private final UnbakedModel wrapped;
/*     */     
/*     */     private static <T> ModelDiscovery.Slot<T> slot(int index) {
/* 172 */       Objects.checkIndex(index, 7);
/* 173 */       return new ModelDiscovery.Slot<>(index);
/*     */     }
/*     */     
/* 176 */     private final AtomicReferenceArray<Object> fixedSlots = new AtomicReferenceArray(7);
/*     */     
/* 178 */     private final Map<ModelState, QuadCollection> modelBakeCache = new ConcurrentHashMap<>();
/*     */     
/*     */     private ModelWrapper(Identifier id, UnbakedModel wrapped, boolean valid) {
/* 181 */       this.id = id;
/* 182 */       this.wrapped = wrapped;
/* 183 */       this.valid = valid;
/*     */     }
/*     */     
/*     */     public UnbakedModel wrapped() {
/* 188 */       return this.wrapped;
/*     */     }
/*     */     
/*     */     public ResolvedModel parent() {
/* 193 */       return this.parent;
/*     */     }
/*     */     
/*     */     public String debugName() {
/* 198 */       return this.id.toString();
/*     */     }
/*     */     
/*     */     private <T> T getSlot(ModelDiscovery.Slot<T> key) {
/* 203 */       return (T)this.fixedSlots.get(key.index);
/*     */     }
/*     */     
/*     */     private <T> T updateSlot(ModelDiscovery.Slot<T> key, T value) {
/* 208 */       T currentValue = (T)this.fixedSlots.compareAndExchange(key.index, null, value);
/* 209 */       if (currentValue == null)
/* 211 */         return value; 
/* 214 */       return currentValue;
/*     */     }
/*     */     
/*     */     private <T> T getSimpleProperty(ModelDiscovery.Slot<T> key, Function<ResolvedModel, T> getter) {
/* 218 */       T result = getSlot(key);
/* 219 */       if (result != null)
/* 220 */         return result; 
/* 222 */       return updateSlot(key, getter.apply(this));
/*     */     }
/*     */     
/*     */     public boolean getTopAmbientOcclusion() {
/* 227 */       return ((Boolean)getSimpleProperty(KEY_AMBIENT_OCCLUSION, ResolvedModel::findTopAmbientOcclusion)).booleanValue();
/*     */     }
/*     */     
/*     */     public UnbakedModel.GuiLight getTopGuiLight() {
/* 232 */       return getSimpleProperty(KEY_GUI_LIGHT, ResolvedModel::findTopGuiLight);
/*     */     }
/*     */     
/*     */     public ItemTransforms getTopTransforms() {
/* 237 */       return getSimpleProperty(KEY_TRANSFORMS, ResolvedModel::findTopTransforms);
/*     */     }
/*     */     
/*     */     public UnbakedGeometry getTopGeometry() {
/* 242 */       return getSimpleProperty(KEY_GEOMETRY, ResolvedModel::findTopGeometry);
/*     */     }
/*     */     
/*     */     public TextureSlots getTopTextureSlots() {
/* 247 */       return getSimpleProperty(KEY_TEXTURE_SLOTS, ResolvedModel::findTopTextureSlots);
/*     */     }
/*     */     
/*     */     public Material.Baked resolveParticleMaterial(TextureSlots textureSlots, ModelBaker baker) {
/* 253 */       Material.Baked result = getSlot(KEY_PARTICLE_SPRITE);
/* 254 */       if (result != null)
/* 255 */         return result; 
/* 257 */       return updateSlot(KEY_PARTICLE_SPRITE, ResolvedModel.resolveParticleMaterial(textureSlots, baker, this));
/*     */     }
/*     */     
/*     */     private QuadCollection bakeDefaultState(TextureSlots textureSlots, ModelBaker baker, ModelState state) {
/* 262 */       QuadCollection result = getSlot(KEY_DEFAULT_GEOMETRY);
/* 263 */       if (result != null)
/* 264 */         return result; 
/* 266 */       return updateSlot(KEY_DEFAULT_GEOMETRY, getTopGeometry().bake(textureSlots, baker, state, this));
/*     */     }
/*     */     
/*     */     public QuadCollection bakeTopGeometry(TextureSlots textureSlots, ModelBaker baker, ModelState state) {
/* 271 */       if (state == BlockModelRotation.IDENTITY)
/* 272 */         return bakeDefaultState(textureSlots, baker, state); 
/* 276 */       return this.modelBakeCache.computeIfAbsent(state, s -> {
/*     */             UnbakedGeometry topGeometry = getTopGeometry();
/*     */             return topGeometry.bake(textureSlots, baker, s, this);
/*     */           });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ModelDiscovery.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */