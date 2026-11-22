/*     */ package net.minecraft.client.renderer.block.dispatch;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.renderer.block.dispatch.multipart.MultiPartModel;
/*     */ import net.minecraft.client.renderer.block.dispatch.multipart.Selector;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class BlockStateModelDispatcher extends Record {
/*     */   private final Optional<SimpleModelSelectors> simpleModels;
/*     */   
/*     */   private final Optional<MultiPartDefinition> multiPart;
/*     */   
/*     */   public BlockStateModelDispatcher(Optional<SimpleModelSelectors> simpleModels, Optional<MultiPartDefinition> multiPart) {
/*  25 */     this.simpleModels = simpleModels;
/*  25 */     this.multiPart = multiPart;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #25	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #25	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #25	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<SimpleModelSelectors> simpleModels() {
/*  25 */     return this.simpleModels;
/*     */   }
/*     */   
/*     */   public Optional<MultiPartDefinition> multiPart() {
/*  25 */     return this.multiPart;
/*     */   }
/*     */   
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final Codec<BlockStateModelDispatcher> CODEC;
/*     */   
/*     */   static {
/*  35 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)SimpleModelSelectors.CODEC.optionalFieldOf("variants").forGetter(BlockStateModelDispatcher::simpleModels), (App)MultiPartDefinition.CODEC.optionalFieldOf("multipart").forGetter(BlockStateModelDispatcher::multiPart)).apply((Applicative)i, BlockStateModelDispatcher::new)).validate(o -> 
/*  36 */         (o.simpleModels().isEmpty() && o.multiPart().isEmpty()) ? DataResult.error(()) : DataResult.success(o));
/*     */   }
/*     */   
/*     */   public Map<BlockState, BlockStateModel.UnbakedRoot> instantiate(StateDefinition<Block, BlockState> stateDefinition, Supplier<String> source) {
/*  43 */     Map<BlockState, BlockStateModel.UnbakedRoot> matchedStates = new IdentityHashMap<>();
/*  45 */     this.simpleModels.ifPresent(s -> s.instantiate(stateDefinition, source, ()));
/*  56 */     this.multiPart.ifPresent(m -> {
/*     */           ImmutableList immutableList = stateDefinition.getPossibleStates();
/*     */           MultiPartModel.Unbaked unbaked = m.instantiate(stateDefinition);
/*     */           for (BlockState state : immutableList)
/*     */             matchedStates.putIfAbsent(state, unbaked); 
/*     */         });
/*  65 */     return matchedStates;
/*     */   }
/*     */   
/*     */   public static final class SimpleModelSelectors extends Record {
/*     */     private final Map<String, BlockStateModel.Unbaked> models;
/*     */     
/*     */     public SimpleModelSelectors(Map<String, BlockStateModel.Unbaked> models) {
/*  68 */       this.models = models;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher$SimpleModelSelectors;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #68	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher$SimpleModelSelectors;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher$SimpleModelSelectors;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #68	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher$SimpleModelSelectors;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher$SimpleModelSelectors;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #68	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher$SimpleModelSelectors;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Map<String, BlockStateModel.Unbaked> models() {
/*  68 */       return this.models;
/*     */     }
/*     */     
/*  71 */     public static final Codec<SimpleModelSelectors> CODEC = ExtraCodecs.nonEmptyMap((Codec)Codec.unboundedMap((Codec)Codec.STRING, BlockStateModel.Unbaked.CODEC))
/*  72 */       .xmap(SimpleModelSelectors::new, SimpleModelSelectors::models);
/*     */     
/*     */     public void instantiate(StateDefinition<Block, BlockState> stateDefinition, Supplier<String> source, BiConsumer<BlockState, BlockStateModel.UnbakedRoot> output) {
/*  75 */       this.models.forEach((selectorString, model) -> {
/*     */             try {
/*     */               Predicate<StateHolder<Block, BlockState>> selector = VariantSelector.predicate(stateDefinition, selectorString);
/*     */               BlockStateModel.UnbakedRoot wrapper = model.asRoot();
/*     */               UnmodifiableIterator<BlockState> unmodifiableIterator = stateDefinition.getPossibleStates().iterator();
/*     */               while (unmodifiableIterator.hasNext()) {
/*     */                 BlockState state = unmodifiableIterator.next();
/*     */                 if (selector.test(state))
/*     */                   output.accept(state, wrapper); 
/*     */               } 
/*  85 */             } catch (Exception e) {
/*     */               BlockStateModelDispatcher.LOGGER.warn("Exception loading blockstate definition: '{}' for variant: '{}': {}", new Object[] { source.get(), selectorString, e.getMessage() });
/*     */             } 
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class MultiPartDefinition extends Record {
/*     */     private final List<Selector> selectors;
/*     */     
/*     */     public MultiPartDefinition(List<Selector> selectors) {
/*  92 */       this.selectors = selectors;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher$MultiPartDefinition;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher$MultiPartDefinition;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher$MultiPartDefinition;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher$MultiPartDefinition;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher$MultiPartDefinition;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelDispatcher$MultiPartDefinition;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public List<Selector> selectors() {
/*  92 */       return this.selectors;
/*     */     }
/*     */     
/*  95 */     public static final Codec<MultiPartDefinition> CODEC = ExtraCodecs.nonEmptyList(Selector.CODEC.listOf())
/*  96 */       .xmap(MultiPartDefinition::new, MultiPartDefinition::selectors);
/*     */     
/*     */     public MultiPartModel.Unbaked instantiate(StateDefinition<Block, BlockState> stateDefinition) {
/*  99 */       ImmutableList.Builder<MultiPartModel.Selector<BlockStateModel.Unbaked>> instantiatedSelectors = ImmutableList.builderWithExpectedSize(this.selectors.size());
/* 100 */       for (Selector selector : this.selectors)
/* 101 */         instantiatedSelectors.add(new MultiPartModel.Selector(selector
/* 102 */               .instantiate(stateDefinition), selector
/* 103 */               .variant())); 
/* 106 */       return new MultiPartModel.Unbaked((List)instantiatedSelectors.build());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\BlockStateModelDispatcher.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */