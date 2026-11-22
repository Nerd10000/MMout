/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.color.block.BlockColors;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*     */ import net.minecraft.client.renderer.block.BlockModelSet;
/*     */ import net.minecraft.client.renderer.block.BlockStateModelSet;
/*     */ import net.minecraft.client.renderer.block.BuiltInBlockModels;
/*     */ import net.minecraft.client.renderer.block.FluidModel;
/*     */ import net.minecraft.client.renderer.block.FluidStateModelSet;
/*     */ import net.minecraft.client.renderer.block.LoadedBlockModels;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*     */ import net.minecraft.client.renderer.block.model.BlockModel;
/*     */ import net.minecraft.client.renderer.item.ClientItem;
/*     */ import net.minecraft.client.renderer.item.ItemModel;
/*     */ import net.minecraft.client.renderer.texture.SpriteLoader;
/*     */ import net.minecraft.client.resources.model.cuboid.CuboidModel;
/*     */ import net.minecraft.client.resources.model.cuboid.ItemModelGenerator;
/*     */ import net.minecraft.client.resources.model.cuboid.MissingCuboidModel;
/*     */ import net.minecraft.client.resources.model.sprite.AtlasManager;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ import net.minecraft.client.resources.model.sprite.MaterialBaker;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.data.AtlasIds;
/*     */ import net.minecraft.resources.FileToIdConverter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ModelManager implements PreparableReloadListener {
/*  58 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  60 */   private static final FileToIdConverter MODEL_LISTER = FileToIdConverter.json("models");
/*     */   
/*  62 */   private Map<Identifier, ItemModel> bakedItemStackModels = Map.of();
/*     */   
/*  63 */   private Map<Identifier, ClientItem.Properties> itemProperties = Map.of();
/*     */   
/*     */   private final AtlasManager atlasManager;
/*     */   
/*     */   private final PlayerSkinRenderCache playerSkinRenderCache;
/*     */   
/*     */   private final BlockColors blockColors;
/*     */   
/*  67 */   private EntityModelSet entityModelSet = EntityModelSet.EMPTY;
/*     */   
/*     */   private ModelBakery.MissingModels missingModels;
/*     */   
/*     */   private BlockStateModelSet blockStateModelSet;
/*     */   
/*     */   private BlockModelSet blockModelSet;
/*     */   
/*     */   private FluidStateModelSet fluidStateModelSet;
/*     */   
/*  72 */   private Object2IntMap<BlockState> modelGroups = Object2IntMaps.emptyMap();
/*     */   
/*     */   public ModelManager(BlockColors blockColors, AtlasManager atlasManager, PlayerSkinRenderCache playerSkinRenderCache) {
/*  75 */     this.blockColors = blockColors;
/*  76 */     this.atlasManager = atlasManager;
/*  77 */     this.playerSkinRenderCache = playerSkinRenderCache;
/*     */   }
/*     */   
/*     */   public ItemModel getItemModel(Identifier id) {
/*  81 */     return (ItemModel)this.bakedItemStackModels.getOrDefault(id, this.missingModels.item());
/*     */   }
/*     */   
/*     */   public ClientItem.Properties getItemProperties(Identifier id) {
/*  85 */     return this.itemProperties.getOrDefault(id, ClientItem.Properties.DEFAULT);
/*     */   }
/*     */   
/*     */   public BlockStateModelSet getBlockStateModelSet() {
/*  89 */     return Objects.<BlockStateModelSet>requireNonNull(this.blockStateModelSet, "Block models not yet initialized");
/*     */   }
/*     */   
/*     */   public BlockModelSet getBlockModelSet() {
/*  93 */     return Objects.<BlockModelSet>requireNonNull(this.blockModelSet, "Block models not yet initialized");
/*     */   }
/*     */   
/*     */   public FluidStateModelSet getFluidStateModelSet() {
/*  97 */     return Objects.<FluidStateModelSet>requireNonNull(this.fluidStateModelSet, "Fluid models not yet initialized");
/*     */   }
/*     */   
/*     */   public final CompletableFuture<Void> reload(PreparableReloadListener.SharedState currentReload, Executor taskExecutor, PreparableReloadListener.PreparationBarrier preparationBarrier, Executor reloadExecutor) {
/* 102 */     ResourceManager manager = currentReload.resourceManager();
/* 103 */     CompletableFuture<EntityModelSet> entityModelSet = CompletableFuture.supplyAsync(EntityModelSet::vanilla, taskExecutor);
/* 105 */     CompletableFuture<Map<Identifier, UnbakedModel>> modelCache = loadBlockModels(manager, taskExecutor);
/* 106 */     CompletableFuture<BlockStateModelLoader.LoadedModels> blockStateModels = BlockStateModelLoader.loadBlockStates(manager, taskExecutor);
/* 107 */     CompletableFuture<Map<BlockState, BlockModel.Unbaked>> blockModelContents = CompletableFuture.supplyAsync(() -> BuiltInBlockModels.createBlockModels(this.blockColors), taskExecutor);
/* 108 */     CompletableFuture<ClientItemInfoLoader.LoadedClientInfos> itemStackModels = ClientItemInfoLoader.scheduleLoad(manager, taskExecutor);
/* 110 */     CompletableFuture<ResolvedModels> modelDiscovery = CompletableFuture.allOf((CompletableFuture<?>[])new CompletableFuture[] { modelCache, blockStateModels, itemStackModels }).thenApplyAsync(paramVoid -> discoverModelDependencies(modelCache.join(), blockStateModels.join(), itemStackModels.join()), taskExecutor);
/* 112 */     CompletableFuture<Object2IntMap<BlockState>> modelGroups = blockStateModels.thenApplyAsync(models -> buildModelGroups(this.blockColors, models), taskExecutor);
/* 114 */     AtlasManager.PendingStitchResults pendingStitches = (AtlasManager.PendingStitchResults)currentReload.get(AtlasManager.PENDING_STITCH);
/* 115 */     CompletableFuture<SpriteLoader.Preparations> pendingBlockAtlasSprites = pendingStitches.get(AtlasIds.BLOCKS);
/* 116 */     CompletableFuture<SpriteLoader.Preparations> pendingItemAtlasSprites = pendingStitches.get(AtlasIds.ITEMS);
/* 118 */     CompletableFuture<LoadedBlockModels> blockModels = CompletableFuture.allOf((CompletableFuture<?>[])new CompletableFuture[] { blockModelContents, entityModelSet }).thenApply(paramVoid -> new LoadedBlockModels(blockModelContents.join(), entityModelSet.join(), (SpriteGetter)this.atlasManager, this.playerSkinRenderCache));
/* 145 */     Objects.requireNonNull(preparationBarrier);
/*     */     return CompletableFuture.allOf((CompletableFuture<?>[])new CompletableFuture[] { pendingBlockAtlasSprites, pendingItemAtlasSprites, modelDiscovery, modelGroups, blockStateModels, itemStackModels, entityModelSet, blockModels, modelCache }).thenComposeAsync(paramVoid -> {
/*     */           SpriteLoader.Preparations blockAtlasSprites = pendingBlockAtlasSprites.join();
/*     */           SpriteLoader.Preparations itemAtlasSprites = pendingItemAtlasSprites.join();
/*     */           ResolvedModels resolvedModels = modelDiscovery.join();
/*     */           Object2IntMap<BlockState> groups = modelGroups.join();
/*     */           Sets.SetView setView = Sets.difference(((Map)modelCache.join()).keySet(), resolvedModels.models.keySet());
/*     */           if (!setView.isEmpty())
/*     */             LOGGER.debug("Unreferenced models: \n{}", setView.stream().sorted().map(()).collect(Collectors.joining())); 
/*     */           ModelBakery bakery = new ModelBakery(entityModelSet.join(), (SpriteGetter)this.atlasManager, this.playerSkinRenderCache, ((BlockStateModelLoader.LoadedModels)blockStateModels.join()).models(), ((ClientItemInfoLoader.LoadedClientInfos)itemStackModels.join()).contents(), resolvedModels.models(), resolvedModels.missing());
/*     */           return loadModels(blockAtlasSprites, itemAtlasSprites, bakery, blockModels.join(), groups, entityModelSet.join(), taskExecutor);
/* 145 */         }taskExecutor).thenCompose(preparationBarrier::wait)
/* 146 */       .thenAcceptAsync(this::apply, reloadExecutor);
/*     */   }
/*     */   
/*     */   private static CompletableFuture<Map<Identifier, UnbakedModel>> loadBlockModels(ResourceManager manager, Executor executor) {
/* 150 */     return CompletableFuture.supplyAsync(() -> MODEL_LISTER.listMatchingResources(manager), executor)
/* 151 */       .thenCompose(resources -> {
/*     */           List<CompletableFuture<Pair<Identifier, CuboidModel>>> result = new ArrayList<>(resources.size());
/*     */           for (Map.Entry<Identifier, Resource> resource : (Iterable<Map.Entry<Identifier, Resource>>)resources.entrySet())
/*     */             result.add(CompletableFuture.supplyAsync((), executor)); 
/*     */           return Util.sequence(result).thenApply(());
/*     */         });
/*     */   }
/*     */   
/*     */   private static ResolvedModels discoverModelDependencies(Map<Identifier, UnbakedModel> allModels, BlockStateModelLoader.LoadedModels blockStateModels, ClientItemInfoLoader.LoadedClientInfos itemInfos) {
/* 169 */     Zone ignored = Profiler.get().zone("dependencies");
/*     */     try {
/* 170 */       ModelDiscovery result = new ModelDiscovery(allModels, MissingCuboidModel.missingModel());
/* 171 */       result.addSpecialModel(ItemModelGenerator.GENERATED_ITEM_MODEL_ID, (UnbakedModel)new ItemModelGenerator());
/* 172 */       Objects.requireNonNull(result);
/* 172 */       blockStateModels.models().values().forEach(result::addRoot);
/* 173 */       itemInfos.contents().values().forEach(info -> result.addRoot((ResolvableModel)info.model()));
/* 174 */       ResolvedModels resolvedModels = new ResolvedModels(result.missingModel(), result.resolve());
/* 175 */       if (ignored != null)
/* 175 */         ignored.close(); 
/*     */       return resolvedModels;
/*     */     } catch (Throwable throwable) {
/*     */       if (ignored != null)
/*     */         try {
/*     */           ignored.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static CompletableFuture<ReloadState> loadModels(SpriteLoader.Preparations blockAtlas, SpriteLoader.Preparations itemAtlas, ModelBakery bakery, LoadedBlockModels blockModels, Object2IntMap<BlockState> modelGroups, EntityModelSet entityModelSet, Executor taskExecutor) {
/* 179 */     MaterialBaker blockItemMaterialBaker = new CombinedBlockItemMaterialBaker(blockAtlas, itemAtlas);
/* 180 */     MaterialBaker blockOnlyMaterialBaker = new BlockOnlyMaterialBaker(blockAtlas);
/* 182 */     CompletableFuture<ModelBakery.BakingResult> bakedStateResults = bakery.bakeModels(blockItemMaterialBaker, taskExecutor);
/* 184 */     CompletableFuture<Map<BlockState, BlockModel>> bakedModelsFuture = bakedStateResults.thenCompose(bakingResult -> {
/*     */           Objects.requireNonNull(bakingResult);
/*     */           return blockModels.bake(bakingResult::getBlockStateModel, bakingResult.missingModels().block(), taskExecutor);
/*     */         });
/* 187 */     return bakedStateResults
/* 188 */       .thenCombine(bakedModelsFuture, (bakingResult, bakedModels) -> {
/*     */           blockItemMaterialBaker.logMissingTextures();
/*     */           Map<Fluid, FluidModel> fluidModels = FluidStateModelSet.bake(blockOnlyMaterialBaker);
/*     */           blockOnlyMaterialBaker.logMissingTextures();
/*     */           Map<BlockState, BlockStateModel> modelByStateCache = createBlockStateToModelDispatch(bakingResult.blockStateModels(), bakingResult.missingModels().block());
/*     */           return new ReloadState(bakingResult, modelGroups, modelByStateCache, bakedModels, fluidModels, entityModelSet);
/*     */         });
/*     */   }
/*     */   
/*     */   private static Map<BlockState, BlockStateModel> createBlockStateToModelDispatch(Map<BlockState, BlockStateModel> bakedModels, BlockStateModel missingModel) {
/* 201 */     Zone ignored = Profiler.get().zone("block state dispatch");
/*     */     try {
/* 202 */       Map<BlockState, BlockStateModel> modelByStateCache = new IdentityHashMap<>(bakedModels);
/* 204 */       for (Block block : BuiltInRegistries.BLOCK) {
/* 205 */         block.getStateDefinition().getPossibleStates().forEach(state -> {
/*     */               if (bakedModels.putIfAbsent(state, missingModel) == null)
/*     */                 LOGGER.warn("Missing model for variant: '{}'", state); 
/*     */             });
/*     */       } 
/* 211 */       Map<BlockState, BlockStateModel> map1 = modelByStateCache;
/* 212 */       if (ignored != null)
/* 212 */         ignored.close(); 
/*     */       return map1;
/*     */     } catch (Throwable throwable) {
/*     */       if (ignored != null)
/*     */         try {
/*     */           ignored.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Object2IntMap<BlockState> buildModelGroups(BlockColors blockColors, BlockStateModelLoader.LoadedModels blockStateModels) {
/* 216 */     Zone ignored = Profiler.get().zone("block groups");
/*     */     try {
/* 217 */       Object2IntMap<BlockState> object2IntMap = ModelGroupCollector.build(blockColors, blockStateModels);
/* 218 */       if (ignored != null)
/* 218 */         ignored.close(); 
/*     */       return object2IntMap;
/*     */     } catch (Throwable throwable) {
/*     */       if (ignored != null)
/*     */         try {
/*     */           ignored.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void apply(ReloadState preparations) {
/* 222 */     ModelBakery.BakingResult bakedModels = preparations.bakedModels;
/* 223 */     this.bakedItemStackModels = bakedModels.itemStackModels();
/* 224 */     this.itemProperties = bakedModels.itemProperties();
/* 225 */     this.modelGroups = preparations.modelGroups;
/* 226 */     this.missingModels = bakedModels.missingModels();
/* 227 */     this.blockStateModelSet = new BlockStateModelSet(preparations.blockStateModels, this.missingModels.block());
/* 228 */     this.blockModelSet = new BlockModelSet(this.blockStateModelSet, preparations.blockModels, this.blockColors);
/* 229 */     this.fluidStateModelSet = new FluidStateModelSet(preparations.fluidModels, this.missingModels.fluid());
/* 230 */     this.entityModelSet = preparations.entityModelSet;
/*     */   }
/*     */   
/*     */   public boolean requiresRender(BlockState oldState, BlockState newState) {
/* 234 */     if (oldState == newState)
/* 235 */       return false; 
/* 237 */     int oldModelGroup = this.modelGroups.getInt(oldState);
/* 238 */     if (oldModelGroup != -1) {
/* 239 */       int newModelGroup = this.modelGroups.getInt(newState);
/* 240 */       if (oldModelGroup == newModelGroup) {
/* 241 */         FluidState oldFluidState = oldState.getFluidState();
/* 242 */         FluidState newFluidState = newState.getFluidState();
/* 243 */         return (oldFluidState != newFluidState);
/*     */       } 
/*     */     } 
/* 247 */     return true;
/*     */   }
/*     */   
/*     */   public Supplier<EntityModelSet> entityModels() {
/* 251 */     return () -> this.entityModelSet;
/*     */   }
/*     */   
/*     */   private static final class ResolvedModels extends Record {
/*     */     private final ResolvedModel missing;
/*     */     
/*     */     private final Map<Identifier, ResolvedModel> models;
/*     */     
/*     */     private ResolvedModels(ResolvedModel missing, Map<Identifier, ResolvedModel> models) {
/* 254 */       this.missing = missing;
/* 254 */       this.models = models;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #254	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #254	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #254	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ResolvedModel missing() {
/* 254 */       return this.missing;
/*     */     }
/*     */     
/*     */     public Map<Identifier, ResolvedModel> models() {
/* 254 */       return this.models;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ReloadState extends Record {
/*     */     private final ModelBakery.BakingResult bakedModels;
/*     */     
/*     */     private final Object2IntMap<BlockState> modelGroups;
/*     */     
/*     */     private final Map<BlockState, BlockStateModel> blockStateModels;
/*     */     
/*     */     private final Map<BlockState, BlockModel> blockModels;
/*     */     
/*     */     private final Map<Fluid, FluidModel> fluidModels;
/*     */     
/*     */     private final EntityModelSet entityModelSet;
/*     */     
/*     */     private ReloadState(ModelBakery.BakingResult bakedModels, Object2IntMap<BlockState> modelGroups, Map<BlockState, BlockStateModel> blockStateModels, Map<BlockState, BlockModel> blockModels, Map<Fluid, FluidModel> fluidModels, EntityModelSet entityModelSet) {
/* 259 */       this.bakedModels = bakedModels;
/* 259 */       this.modelGroups = modelGroups;
/* 259 */       this.blockStateModels = blockStateModels;
/* 259 */       this.blockModels = blockModels;
/* 259 */       this.fluidModels = fluidModels;
/* 259 */       this.entityModelSet = entityModelSet;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ModelManager$ReloadState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #259	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelManager$ReloadState;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ModelManager$ReloadState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #259	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelManager$ReloadState;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ModelManager$ReloadState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #259	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelManager$ReloadState;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ModelBakery.BakingResult bakedModels() {
/* 259 */       return this.bakedModels;
/*     */     }
/*     */     
/*     */     public Object2IntMap<BlockState> modelGroups() {
/* 259 */       return this.modelGroups;
/*     */     }
/*     */     
/*     */     public Map<BlockState, BlockStateModel> blockStateModels() {
/* 259 */       return this.blockStateModels;
/*     */     }
/*     */     
/*     */     public Map<BlockState, BlockModel> blockModels() {
/* 259 */       return this.blockModels;
/*     */     }
/*     */     
/*     */     public Map<Fluid, FluidModel> fluidModels() {
/* 259 */       return this.fluidModels;
/*     */     }
/*     */     
/*     */     public EntityModelSet entityModelSet() {
/* 259 */       return this.entityModelSet;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class BlockOnlyMaterialBaker extends MaterialBaker {
/*     */     private final SpriteLoader.Preparations blockAtlas;
/*     */     
/*     */     public BlockOnlyMaterialBaker(SpriteLoader.Preparations blockAtlas) {
/* 272 */       super(blockAtlas.missing());
/* 273 */       this.blockAtlas = blockAtlas;
/*     */     }
/*     */     
/*     */     protected Material.Baked bake(Material material) {
/* 278 */       return bakeForAtlas(material, this.blockAtlas);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CombinedBlockItemMaterialBaker extends MaterialBaker {
/*     */     private final SpriteLoader.Preparations blockAtlas;
/*     */     
/*     */     private final SpriteLoader.Preparations itemAtlas;
/*     */     
/*     */     public CombinedBlockItemMaterialBaker(SpriteLoader.Preparations blockAtlas, SpriteLoader.Preparations itemAtlas) {
/* 287 */       super(blockAtlas.missing());
/* 288 */       this.blockAtlas = blockAtlas;
/* 289 */       this.itemAtlas = itemAtlas;
/*     */     }
/*     */     
/*     */     protected Material.Baked bake(Material material) {
/* 294 */       Material.Baked itemMaterial = bakeForAtlas(material, this.itemAtlas);
/* 295 */       if (itemMaterial != null)
/* 296 */         return itemMaterial; 
/* 298 */       return bakeForAtlas(material, this.blockAtlas);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ModelManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */