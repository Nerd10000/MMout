/*     */ package net.minecraft.client.data.models;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonElement;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
/*     */ import net.minecraft.client.data.models.model.ItemModelUtils;
/*     */ import net.minecraft.client.data.models.model.ModelInstance;
/*     */ import net.minecraft.client.data.models.model.ModelLocationUtils;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
/*     */ import net.minecraft.client.renderer.item.ClientItem;
/*     */ import net.minecraft.client.renderer.item.ItemModel;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ public class ModelProvider implements DataProvider {
/*     */   private final PackOutput.PathProvider blockStatePathProvider;
/*     */   
/*     */   private final PackOutput.PathProvider itemInfoPathProvider;
/*     */   
/*     */   private final PackOutput.PathProvider modelPathProvider;
/*     */   
/*     */   public ModelProvider(PackOutput output) {
/*  37 */     this.blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
/*  38 */     this.itemInfoPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
/*  39 */     this.modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
/*     */   }
/*     */   
/*     */   private static class SimpleModelCollector implements BiConsumer<Identifier, ModelInstance> {
/*  43 */     private final Map<Identifier, ModelInstance> models = new HashMap<>();
/*     */     
/*     */     public void accept(Identifier id, ModelInstance contents) {
/*  47 */       Supplier<JsonElement> prev = (Supplier<JsonElement>)this.models.put(id, contents);
/*  48 */       if (prev != null)
/*  49 */         throw new IllegalStateException("Duplicate model definition for " + String.valueOf(id)); 
/*     */     }
/*     */     
/*     */     public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
/*  54 */       Objects.requireNonNull(pathProvider);
/*  54 */       return DataProvider.saveAll(cache, Supplier::get, pathProvider::json, this.models);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class BlockStateGeneratorCollector implements Consumer<BlockModelDefinitionGenerator> {
/*  59 */     private final Map<Block, BlockModelDefinitionGenerator> generators = new HashMap<>();
/*     */     
/*     */     public void accept(BlockModelDefinitionGenerator generator) {
/*  63 */       Block block = generator.block();
/*  64 */       BlockModelDefinitionGenerator prev = this.generators.put(block, generator);
/*  65 */       if (prev != null)
/*  66 */         throw new IllegalStateException("Duplicate blockstate definition for " + String.valueOf(block)); 
/*     */     }
/*     */     
/*     */     public void validate() {
/*  74 */       List<Identifier> missingDefinitions = BuiltInRegistries.BLOCK.listElements().filter(e -> !this.generators.containsKey(e.value())).map(e -> e.key().identifier()).toList();
/*  76 */       if (!missingDefinitions.isEmpty())
/*  77 */         throw new IllegalStateException("Missing blockstate definitions for: " + String.valueOf(missingDefinitions)); 
/*     */     }
/*     */     
/*     */     public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
/*  82 */       Map<Block, BlockStateModelDispatcher> definitions = Maps.transformValues(this.generators, BlockModelDefinitionGenerator::create);
/*  83 */       Function<Block, Path> pathGetter = block -> pathProvider.json(block.builtInRegistryHolder().key().identifier());
/*  84 */       return DataProvider.saveAll(cache, BlockStateModelDispatcher.CODEC, pathGetter, definitions);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ItemInfoCollector implements ItemModelOutput {
/*  89 */     private final Map<Item, ClientItem> itemInfos = new HashMap<>();
/*     */     
/*  90 */     private final Map<Item, Item> copies = new HashMap<>();
/*     */     
/*     */     public void accept(Item item, ItemModel.Unbaked model, ClientItem.Properties properties) {
/*  94 */       register(item, new ClientItem(model, properties));
/*     */     }
/*     */     
/*     */     private void register(Item item, ClientItem itemInfo) {
/*  98 */       ClientItem prev = this.itemInfos.put(item, itemInfo);
/*  99 */       if (prev != null)
/* 100 */         throw new IllegalStateException("Duplicate item model definition for " + String.valueOf(item)); 
/*     */     }
/*     */     
/*     */     public void copy(Item donor, Item acceptor) {
/* 106 */       this.copies.put(acceptor, donor);
/*     */     }
/*     */     
/*     */     public void finalizeAndValidate() {
/* 112 */       BuiltInRegistries.ITEM.forEach(item -> {
/*     */             if (this.copies.containsKey(item))
/*     */               return; 
/*     */             if (item instanceof BlockItem) {
/*     */               BlockItem blockItem = (BlockItem)item;
/*     */               if (!this.itemInfos.containsKey(blockItem)) {
/*     */                 Identifier targetModel = ModelLocationUtils.getModelLocation(blockItem.getBlock());
/*     */                 accept((Item)blockItem, ItemModelUtils.plainModel(targetModel));
/*     */               } 
/*     */             } 
/*     */           });
/* 123 */       this.copies.forEach((acceptor, donor) -> {
/*     */             ClientItem donorInfo = this.itemInfos.get(donor);
/*     */             if (donorInfo == null)
/*     */               throw new IllegalStateException("Missing donor: " + String.valueOf(donor) + " -> " + String.valueOf(acceptor)); 
/*     */             register(acceptor, donorInfo);
/*     */           });
/* 134 */       List<Identifier> missingDefinitions = BuiltInRegistries.ITEM.listElements().filter(e -> !this.itemInfos.containsKey(e.value())).map(e -> e.key().identifier()).toList();
/* 136 */       if (!missingDefinitions.isEmpty())
/* 137 */         throw new IllegalStateException("Missing item model definitions for: " + String.valueOf(missingDefinitions)); 
/*     */     }
/*     */     
/*     */     public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
/* 142 */       return DataProvider.saveAll(cache, ClientItem.CODEC, item -> pathProvider.json(item.builtInRegistryHolder().key().identifier()), this.itemInfos);
/*     */     }
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> run(CachedOutput cache) {
/* 148 */     ItemInfoCollector itemModels = new ItemInfoCollector();
/* 149 */     BlockStateGeneratorCollector blockStateGenerators = new BlockStateGeneratorCollector();
/* 150 */     SimpleModelCollector simpleModels = new SimpleModelCollector();
/* 152 */     (new BlockModelGenerators(blockStateGenerators, itemModels, simpleModels)).run();
/* 153 */     (new ItemModelGenerators(itemModels, simpleModels)).run();
/* 155 */     blockStateGenerators.validate();
/* 156 */     itemModels.finalizeAndValidate();
/* 158 */     return CompletableFuture.allOf((CompletableFuture<?>[])new CompletableFuture[] { blockStateGenerators
/* 159 */           .save(cache, this.blockStatePathProvider), simpleModels
/* 160 */           .save(cache, this.modelPathProvider), itemModels
/* 161 */           .save(cache, this.itemInfoPathProvider) });
/*     */   }
/*     */   
/*     */   public final String getName() {
/* 167 */     return "Model Definitions";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\ModelProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */