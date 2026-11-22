/*    */ package net.minecraft.client.data.models;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.nio.file.Path;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ class BlockStateGeneratorCollector implements Consumer<BlockModelDefinitionGenerator> {
/* 59 */   private final Map<Block, BlockModelDefinitionGenerator> generators = new HashMap<>();
/*    */   
/*    */   public void accept(BlockModelDefinitionGenerator generator) {
/* 63 */     Block block = generator.block();
/* 64 */     BlockModelDefinitionGenerator prev = this.generators.put(block, generator);
/* 65 */     if (prev != null)
/* 66 */       throw new IllegalStateException("Duplicate blockstate definition for " + String.valueOf(block)); 
/*    */   }
/*    */   
/*    */   public void validate() {
/* 74 */     List<Identifier> missingDefinitions = BuiltInRegistries.BLOCK.listElements().filter(e -> !this.generators.containsKey(e.value())).map(e -> e.key().identifier()).toList();
/* 76 */     if (!missingDefinitions.isEmpty())
/* 77 */       throw new IllegalStateException("Missing blockstate definitions for: " + String.valueOf(missingDefinitions)); 
/*    */   }
/*    */   
/*    */   public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
/* 82 */     Map<Block, BlockStateModelDispatcher> definitions = Maps.transformValues(this.generators, BlockModelDefinitionGenerator::create);
/* 83 */     Function<Block, Path> pathGetter = block -> pathProvider.json(block.builtInRegistryHolder().key().identifier());
/* 84 */     return DataProvider.saveAll(cache, BlockStateModelDispatcher.CODEC, pathGetter, definitions);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\ModelProvider$BlockStateGeneratorCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */