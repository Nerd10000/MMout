/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.io.Reader;
/*    */ import java.util.ArrayList;
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
/*    */ import net.minecraft.resources.FileToIdConverter;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.Resource;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.util.StrictJsonParser;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class BlockStateModelLoader {
/* 31 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 32 */   private static final FileToIdConverter BLOCKSTATE_LISTER = FileToIdConverter.json("blockstates");
/*    */   
/*    */   public static CompletableFuture<LoadedModels> loadBlockStates(ResourceManager manager, Executor executor) {
/* 35 */     Function<Identifier, StateDefinition<Block, BlockState>> definitionToBlockState = BlockStateDefinitions.definitionLocationToBlockStateMapper();
/* 37 */     return CompletableFuture.supplyAsync(() -> BLOCKSTATE_LISTER.listMatchingResourceStacks(manager), executor)
/* 38 */       .thenCompose(resources -> {
/*    */           List<CompletableFuture<LoadedModels>> result = new ArrayList<>(resources.size());
/*    */           for (Map.Entry<Identifier, List<Resource>> resourceStack : (Iterable<Map.Entry<Identifier, List<Resource>>>)resources.entrySet())
/*    */             result.add(CompletableFuture.supplyAsync((), executor)); 
/*    */           return Util.sequence(result).thenApply(());
/*    */         });
/*    */   }
/*    */   
/*    */   private static LoadedModels loadBlockStateDefinitionStack(Identifier stateDefinitionId, StateDefinition<Block, BlockState> stateDefinition, List<LoadedBlockStateModelDispatcher> definitionStack) {
/* 83 */     Map<BlockState, BlockStateModel.UnbakedRoot> result = new IdentityHashMap<>();
/* 85 */     for (LoadedBlockStateModelDispatcher definition : definitionStack)
/* 86 */       result.putAll(definition.contents.instantiate(stateDefinition, () -> String.valueOf(stateDefinitionId) + "/" + String.valueOf(stateDefinitionId))); 
/* 89 */     return new LoadedModels(result);
/*    */   }
/*    */   
/*    */   private static final class LoadedBlockStateModelDispatcher extends Record {
/*    */     private final String source;
/*    */     
/*    */     private final BlockStateModelDispatcher contents;
/*    */     
/*    */     private LoadedBlockStateModelDispatcher(String source, BlockStateModelDispatcher contents) {
/* 92 */       this.source = source;
/* 92 */       this.contents = contents;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedBlockStateModelDispatcher;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #92	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedBlockStateModelDispatcher;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedBlockStateModelDispatcher;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #92	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedBlockStateModelDispatcher;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedBlockStateModelDispatcher;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #92	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedBlockStateModelDispatcher;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public String source() {
/* 92 */       return this.source;
/*    */     }
/*    */     
/*    */     public BlockStateModelDispatcher contents() {
/* 92 */       return this.contents;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class LoadedModels extends Record {
/*    */     private final Map<BlockState, BlockStateModel.UnbakedRoot> models;
/*    */     
/*    */     public LoadedModels(Map<BlockState, BlockStateModel.UnbakedRoot> models) {
/* 94 */       this.models = models;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedModels;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #94	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedModels;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedModels;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #94	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedModels;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedModels;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #94	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedModels;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Map<BlockState, BlockStateModel.UnbakedRoot> models() {
/* 94 */       return this.models;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\BlockStateModelLoader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */