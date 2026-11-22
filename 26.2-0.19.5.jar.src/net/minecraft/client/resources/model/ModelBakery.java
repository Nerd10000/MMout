/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import com.google.common.collect.Interner;
/*     */ import com.google.common.collect.Interners;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.block.FluidModel;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
/*     */ import net.minecraft.client.renderer.block.dispatch.ModelState;
/*     */ import net.minecraft.client.renderer.block.dispatch.SingleVariant;
/*     */ import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
/*     */ import net.minecraft.client.renderer.item.ClientItem;
/*     */ import net.minecraft.client.renderer.item.ItemModel;
/*     */ import net.minecraft.client.renderer.item.MissingItemModel;
/*     */ import net.minecraft.client.renderer.item.ModelRenderProperties;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.resources.model.cuboid.ItemTransforms;
/*     */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*     */ import net.minecraft.client.resources.model.geometry.QuadCollection;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ import net.minecraft.client.resources.model.sprite.MaterialBaker;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteId;
/*     */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.thread.ParallelMapTransform;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector3fc;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ModelBakery {
/*  49 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  51 */   public static final SpriteId FIRE_0 = Sheets.BLOCKS_MAPPER.defaultNamespaceApply("fire_0");
/*     */   
/*  52 */   public static final SpriteId FIRE_1 = Sheets.BLOCKS_MAPPER.defaultNamespaceApply("fire_1");
/*     */   
/*     */   public static final int DESTROY_STAGE_COUNT = 10;
/*     */   
/*     */   public static final List<Identifier> DESTROY_STAGES;
/*     */   
/*     */   public static final List<Identifier> BREAKING_LOCATIONS;
/*     */   
/*     */   static {
/*  55 */     DESTROY_STAGES = (List<Identifier>)IntStream.range(0, 10).mapToObj(i -> Identifier.withDefaultNamespace("block/destroy_stage_" + i)).collect(Collectors.toList());
/*  59 */     BREAKING_LOCATIONS = (List<Identifier>)DESTROY_STAGES.stream().map(location -> location.withPath(())).collect(Collectors.toList());
/*     */   }
/*     */   
/*  61 */   public static final List<RenderType> DESTROY_TYPES = (List<RenderType>)BREAKING_LOCATIONS.stream().map(RenderTypes::crumbling).collect(Collectors.toList());
/*     */   
/*  63 */   private static final Matrix4fc IDENTITY = (Matrix4fc)new Matrix4f();
/*     */   
/*     */   private final EntityModelSet entityModelSet;
/*     */   
/*     */   private final SpriteGetter sprites;
/*     */   
/*     */   private final PlayerSkinRenderCache playerSkinRenderCache;
/*     */   
/*     */   private final Map<BlockState, BlockStateModel.UnbakedRoot> unbakedBlockStateModels;
/*     */   
/*     */   private final Map<Identifier, ClientItem> clientInfos;
/*     */   
/*     */   private final Map<Identifier, ResolvedModel> resolvedModels;
/*     */   
/*     */   private final ResolvedModel missingModel;
/*     */   
/*     */   public ModelBakery(EntityModelSet entityModelSet, SpriteGetter sprites, PlayerSkinRenderCache playerSkinRenderCache, Map<BlockState, BlockStateModel.UnbakedRoot> unbakedBlockStateModels, Map<Identifier, ClientItem> clientInfos, Map<Identifier, ResolvedModel> resolvedModels, ResolvedModel missingModel) {
/*  76 */     this.entityModelSet = entityModelSet;
/*  77 */     this.sprites = sprites;
/*  78 */     this.playerSkinRenderCache = playerSkinRenderCache;
/*  79 */     this.unbakedBlockStateModels = unbakedBlockStateModels;
/*  80 */     this.clientInfos = clientInfos;
/*  81 */     this.resolvedModels = resolvedModels;
/*  82 */     this.missingModel = missingModel;
/*     */   }
/*     */   
/*     */   public static final class MissingModels extends Record {
/*     */     private final BlockStateModelPart blockPart;
/*     */     
/*     */     private final BlockStateModel block;
/*     */     
/*     */     private final MissingItemModel item;
/*     */     
/*     */     private final FluidModel fluid;
/*     */     
/*     */     public MissingModels(BlockStateModelPart blockPart, BlockStateModel block, MissingItemModel item, FluidModel fluid) {
/*  85 */       this.blockPart = blockPart;
/*  85 */       this.block = block;
/*  85 */       this.item = item;
/*  85 */       this.fluid = fluid;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ModelBakery$MissingModels;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelBakery$MissingModels;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ModelBakery$MissingModels;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelBakery$MissingModels;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ModelBakery$MissingModels;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelBakery$MissingModels;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockStateModelPart blockPart() {
/*  85 */       return this.blockPart;
/*     */     }
/*     */     
/*     */     public BlockStateModel block() {
/*  85 */       return this.block;
/*     */     }
/*     */     
/*     */     public MissingItemModel item() {
/*  85 */       return this.item;
/*     */     }
/*     */     
/*     */     public FluidModel fluid() {
/*  85 */       return this.fluid;
/*     */     }
/*     */     
/*     */     public static MissingModels bake(ResolvedModel unbaked, final MaterialBaker materials, final ModelBaker.Interner interner) {
/*  92 */       ModelBaker missingModelBakery = new ModelBaker() {
/*     */           public ResolvedModel getModel(Identifier location) {
/*  95 */             throw new IllegalStateException("Missing model can't have dependencies, but asked for " + String.valueOf(location));
/*     */           }
/*     */           
/*     */           public BlockStateModelPart missingBlockModelPart() {
/* 101 */             throw new IllegalStateException();
/*     */           }
/*     */           
/*     */           public <T> T compute(ModelBaker.SharedOperationKey<T> key) {
/* 107 */             return key.compute(this);
/*     */           }
/*     */           
/*     */           public MaterialBaker materials() {
/* 112 */             return materials;
/*     */           }
/*     */           
/*     */           public ModelBaker.Interner interner() {
/* 117 */             return interner;
/*     */           }
/*     */         };
/* 121 */       TextureSlots textureSlots = unbaked.getTopTextureSlots();
/* 122 */       boolean hasAmbientOcclusion = unbaked.getTopAmbientOcclusion();
/* 123 */       boolean usesBlockLight = unbaked.getTopGuiLight().lightLikeBlock();
/* 124 */       ItemTransforms transforms = unbaked.getTopTransforms();
/* 125 */       QuadCollection geometry = unbaked.bakeTopGeometry(textureSlots, missingModelBakery, (ModelState)BlockModelRotation.IDENTITY);
/* 127 */       Multimap<Identifier, Identifier> forbiddenSprites = SimpleModelWrapper.findNonBlockSprites(geometry);
/* 128 */       if (forbiddenSprites != null)
/* 130 */         throw new IllegalStateException("Missing block contains sprites from outside of block atlas: " + String.valueOf(forbiddenSprites)); 
/* 133 */       Material.Baked particleMaterial = unbaked.resolveParticleMaterial(textureSlots, missingModelBakery);
/* 135 */       SimpleModelWrapper missingModelPart = new SimpleModelWrapper(geometry, hasAmbientOcclusion, particleMaterial);
/* 140 */       SingleVariant singleVariant = new SingleVariant(missingModelPart);
/* 143 */       MissingItemModel bakedItemModel = new MissingItemModel(geometry.getAll(), new ModelRenderProperties(usesBlockLight, particleMaterial, transforms));
/* 151 */       FluidModel bakedFluidModel = new FluidModel(ChunkSectionLayer.SOLID, particleMaterial, particleMaterial, null, null);
/* 159 */       return new MissingModels(missingModelPart, (BlockStateModel)singleVariant, bakedItemModel, bakedFluidModel);
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements ModelBaker {
/*     */     public ResolvedModel getModel(Identifier location) {
/*     */       throw new IllegalStateException("Missing model can't have dependencies, but asked for " + String.valueOf(location));
/*     */     }
/*     */     
/*     */     public BlockStateModelPart missingBlockModelPart() {
/*     */       throw new IllegalStateException();
/*     */     }
/*     */     
/*     */     public <T> T compute(ModelBaker.SharedOperationKey<T> key) {
/*     */       return key.compute(this);
/*     */     }
/*     */     
/*     */     public MaterialBaker materials() {
/*     */       return materials;
/*     */     }
/*     */     
/*     */     public ModelBaker.Interner interner() {
/*     */       return interner;
/*     */     }
/*     */   }
/*     */   
/*     */   public CompletableFuture<BakingResult> bakeModels(MaterialBaker materials, Executor taskExecutor) {
/* 164 */     InternerImpl interner = new InternerImpl();
/* 165 */     MissingModels missingModels = MissingModels.bake(this.missingModel, materials, interner);
/* 167 */     ModelBakerImpl baker = new ModelBakerImpl(materials, interner, missingModels);
/* 168 */     CompletableFuture<Map<BlockState, BlockStateModel>> bakedBlockStateModelFuture = ParallelMapTransform.schedule(this.unbakedBlockStateModels, (blockState, model) -> {
/*     */           try {
/*     */             return model.bake(blockState, baker);
/* 173 */           } catch (Exception e) {
/*     */             LOGGER.warn("Unable to bake model: '{}': {}", blockState, e);
/*     */             return null;
/*     */           } 
/*     */         }taskExecutor);
/* 181 */     CompletableFuture<Map<Identifier, ItemModel>> bakedItemStackModelFuture = ParallelMapTransform.schedule(this.clientInfos, (location, clientInfo) -> {
/*     */           try {
/*     */             return clientInfo.model().bake(new ItemModel.BakingContext(baker, this.entityModelSet, this.sprites, this.playerSkinRenderCache, missingModels.item, clientInfo.registrySwapper()), IDENTITY);
/* 186 */           } catch (Exception e) {
/*     */             LOGGER.warn("Unable to bake item model: '{}'", location, e);
/*     */             return null;
/*     */           } 
/*     */         }taskExecutor);
/* 194 */     Map<Identifier, ClientItem.Properties> itemStackModelProperties = new HashMap<>(this.clientInfos.size());
/* 195 */     this.clientInfos.forEach((id, clientInfo) -> {
/*     */           ClientItem.Properties properties = clientInfo.properties();
/*     */           if (!properties.equals(ClientItem.Properties.DEFAULT))
/*     */             itemStackModelProperties.put(id, properties); 
/*     */         });
/* 203 */     return bakedBlockStateModelFuture.thenCombine(bakedItemStackModelFuture, (bakedBlockStateModels, bakedItemStateModels) -> new BakingResult(missingModels, bakedBlockStateModels, bakedItemStateModels, itemStackModelProperties));
/*     */   }
/*     */   
/*     */   private class ModelBakerImpl implements ModelBaker {
/*     */     private final MaterialBaker materials;
/*     */     
/*     */     private final ModelBaker.Interner interner;
/*     */     
/*     */     private final ModelBakery.MissingModels missingModels;
/*     */     
/* 213 */     private final Map<ModelBaker.SharedOperationKey<Object>, Object> operationCache = new ConcurrentHashMap<>();
/*     */     
/*     */     private final Function<ModelBaker.SharedOperationKey<Object>, Object> cacheComputeFunction;
/*     */     
/*     */     private ModelBakerImpl(MaterialBaker materials, ModelBaker.Interner interner, ModelBakery.MissingModels missingModels) {
/* 214 */       this.cacheComputeFunction = (k -> k.compute(this));
/* 217 */       this.materials = materials;
/* 218 */       this.interner = interner;
/* 219 */       this.missingModels = missingModels;
/*     */     }
/*     */     
/*     */     public BlockStateModelPart missingBlockModelPart() {
/* 224 */       return this.missingModels.blockPart;
/*     */     }
/*     */     
/*     */     public MaterialBaker materials() {
/* 229 */       return this.materials;
/*     */     }
/*     */     
/*     */     public ModelBaker.Interner interner() {
/* 234 */       return this.interner;
/*     */     }
/*     */     
/*     */     public ResolvedModel getModel(Identifier location) {
/* 239 */       ResolvedModel result = ModelBakery.this.resolvedModels.get(location);
/* 240 */       if (result == null) {
/* 241 */         ModelBakery.LOGGER.warn("Requested a model that was not discovered previously: {}", location);
/* 242 */         return ModelBakery.this.missingModel;
/*     */       } 
/* 244 */       return result;
/*     */     }
/*     */     
/*     */     public <T> T compute(ModelBaker.SharedOperationKey<T> key) {
/* 250 */       return (T)this.operationCache.computeIfAbsent(key, this.cacheComputeFunction);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class BakingResult extends Record {
/*     */     private final ModelBakery.MissingModels missingModels;
/*     */     
/*     */     private final Map<BlockState, BlockStateModel> blockStateModels;
/*     */     
/*     */     private final Map<Identifier, ItemModel> itemStackModels;
/*     */     
/*     */     private final Map<Identifier, ClientItem.Properties> itemProperties;
/*     */     
/*     */     public BakingResult(ModelBakery.MissingModels missingModels, Map<BlockState, BlockStateModel> blockStateModels, Map<Identifier, ItemModel> itemStackModels, Map<Identifier, ClientItem.Properties> itemProperties) {
/* 254 */       this.missingModels = missingModels;
/* 254 */       this.blockStateModels = blockStateModels;
/* 254 */       this.itemStackModels = itemStackModels;
/* 254 */       this.itemProperties = itemProperties;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ModelBakery$BakingResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #254	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelBakery$BakingResult;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ModelBakery$BakingResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #254	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelBakery$BakingResult;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ModelBakery$BakingResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #254	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelBakery$BakingResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ModelBakery.MissingModels missingModels() {
/* 254 */       return this.missingModels;
/*     */     }
/*     */     
/*     */     public Map<BlockState, BlockStateModel> blockStateModels() {
/* 254 */       return this.blockStateModels;
/*     */     }
/*     */     
/*     */     public Map<Identifier, ItemModel> itemStackModels() {
/* 254 */       return this.itemStackModels;
/*     */     }
/*     */     
/*     */     public Map<Identifier, ClientItem.Properties> itemProperties() {
/* 254 */       return this.itemProperties;
/*     */     }
/*     */     
/*     */     public BlockStateModel getBlockStateModel(BlockState blockState) {
/* 262 */       return this.blockStateModels.getOrDefault(blockState, this.missingModels.block);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class InternerImpl implements ModelBaker.Interner {
/* 267 */     private final Interner<Vector3fc> vectors = Interners.newStrongInterner();
/*     */     
/* 268 */     private final Interner<BakedQuad.MaterialInfo> materialInfos = Interners.newStrongInterner();
/*     */     
/*     */     public Vector3fc vector(Vector3fc v) {
/* 272 */       return (Vector3fc)this.vectors.intern(v);
/*     */     }
/*     */     
/*     */     public BakedQuad.MaterialInfo materialInfo(BakedQuad.MaterialInfo material) {
/* 277 */       return (BakedQuad.MaterialInfo)this.materialInfos.intern(material);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ModelBakery.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */