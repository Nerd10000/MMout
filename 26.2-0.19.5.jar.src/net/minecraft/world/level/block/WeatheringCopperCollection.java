/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.data.BlockFamily;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import org.apache.commons.lang3.function.TriFunction;
/*     */ 
/*     */ public final class WeatheringCopperCollection<T> extends Record {
/*     */   private final ByState<T> weathering;
/*     */   
/*     */   private final ByState<T> waxed;
/*     */   
/*     */   public WeatheringCopperCollection(ByState<T> weathering, ByState<T> waxed) {
/*  15 */     this.weathering = weathering;
/*  15 */     this.waxed = waxed;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/WeatheringCopperCollection;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/WeatheringCopperCollection;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/WeatheringCopperCollection;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection<TT;>;
/*     */   }
/*     */   
/*     */   public ByState<T> weathering() {
/*  15 */     return this.weathering;
/*     */   }
/*     */   
/*     */   public ByState<T> waxed() {
/*  15 */     return this.waxed;
/*     */   }
/*     */   
/*  19 */   public static final ByState<WeatheringCopper.WeatherState> STATES = new ByState<>(WeatheringCopper.WeatherState.UNAFFECTED, WeatheringCopper.WeatherState.EXPOSED, WeatheringCopper.WeatherState.WEATHERED, WeatheringCopper.WeatherState.OXIDIZED);
/*     */   
/*  26 */   public static final WeatheringCopperCollection<String> PREFIXES = new WeatheringCopperCollection(new ByState<>((T)"", (T)"exposed_", (T)"weathered_", (T)"oxidized_"), new ByState<>((T)"waxed_", (T)"waxed_exposed_", (T)"waxed_weathered_", (T)"waxed_oxidized_"));
/*     */   
/*     */   public static WeatheringCopperCollection<String> prefixWithState(WeatheringCopperCollection<String> ids) {
/*  42 */     return zipMap(PREFIXES, ids, (state, id) -> state + state);
/*     */   }
/*     */   
/*     */   public static WeatheringCopperCollection<String> create(String name) {
/*  46 */     return same(ByState.create(name));
/*     */   }
/*     */   
/*     */   public static WeatheringCopperCollection<String> same(ByState<String> byState) {
/*  50 */     return new WeatheringCopperCollection<>(byState, byState);
/*     */   }
/*     */   
/*     */   public static <WaxedBlock extends Block, WeatheringBlock extends Block & WeatheringCopper, Id> WeatheringCopperCollection<Block> registerBlocks(WeatheringCopperCollection<Id> ids, TriFunction<Id, Function<BlockBehaviour.Properties, Block>, BlockBehaviour.Properties, Block> register, BiFunction<WeatheringCopper.WeatherState, BlockBehaviour.Properties, WaxedBlock> waxedBlockFactory, BiFunction<WeatheringCopper.WeatherState, BlockBehaviour.Properties, WeatheringBlock> weatheringFactory, Function<WeatheringCopper.WeatherState, BlockBehaviour.Properties> propertiesSupplier) {
/*  60 */     return ids.apply(weatheringIds -> zipMap(STATES, weatheringIds, ()), waxedIds -> zipMap(STATES, waxedIds, ()));
/*     */   }
/*     */   
/*     */   public static <Id> WeatheringCopperCollection<Item> registerItems(WeatheringCopperCollection<Id> ids, WeatheringCopperCollection<Block> blocks, BiFunction<Id, Block, Item> itemFactory) {
/*  71 */     return zipMap(ids, blocks, itemFactory);
/*     */   }
/*     */   
/*     */   public static WeatheringCopperCollection<BlockFamily> createFamily(BiFunction<String, WeatheringCopper.WeatherState, BlockFamily> waxedProvider, BiFunction<String, WeatheringCopper.WeatherState, BlockFamily> weatheringProvider) {
/*  75 */     return PREFIXES.apply(weatheringPrefixes -> zipMap(weatheringPrefixes, STATES, weatheringProvider), waxedPrefixes -> zipMap(waxedPrefixes, STATES, waxedProvider));
/*     */   }
/*     */   
/*     */   public List<T> asList() {
/*  82 */     ImmutableList.Builder<T> builder = ImmutableList.builderWithExpectedSize(8);
/*  83 */     Objects.requireNonNull(builder);
/*  83 */     forEach(builder::add);
/*  84 */     return (List<T>)builder.build();
/*     */   }
/*     */   
/*     */   public void forEach(Consumer<T> consumer) {
/*  88 */     this.weathering.forEach(consumer);
/*  89 */     this.waxed.forEach(consumer);
/*     */   }
/*     */   
/*     */   public <U> WeatheringCopperCollection<U> map(Function<T, U> mapper) {
/*  93 */     return new WeatheringCopperCollection(this.weathering
/*  94 */         .map(mapper), this.waxed
/*  95 */         .map(mapper));
/*     */   }
/*     */   
/*     */   public <U> WeatheringCopperCollection<U> apply(Function<ByState<T>, ByState<U>> mapper) {
/* 100 */     return apply(mapper, mapper);
/*     */   }
/*     */   
/*     */   public <U> WeatheringCopperCollection<U> apply(Function<ByState<T>, ByState<U>> weatheringMapper, Function<ByState<T>, ByState<U>> waxedMapper) {
/* 104 */     return new WeatheringCopperCollection(weatheringMapper
/* 105 */         .apply(this.weathering), waxedMapper
/* 106 */         .apply(this.waxed));
/*     */   }
/*     */   
/*     */   public static <T, U> void zipApply(WeatheringCopperCollection<T> first, WeatheringCopperCollection<U> second, BiConsumer<T, U> consumer) {
/* 111 */     zipApply(first.weathering, second.weathering, consumer);
/* 112 */     zipApply(first.waxed, second.waxed, consumer);
/*     */   }
/*     */   
/*     */   public static <T, U, R> WeatheringCopperCollection<R> zipMap(WeatheringCopperCollection<T> first, WeatheringCopperCollection<U> second, BiFunction<T, U, R> operation) {
/* 116 */     return new WeatheringCopperCollection<>(
/* 117 */         zipMap(first.weathering, second.weathering, operation), 
/* 118 */         zipMap(first.waxed, second.waxed, operation));
/*     */   }
/*     */   
/*     */   public void zipUnwaxedWaxed(BiConsumer<T, T> consumer) {
/* 123 */     zipApply(this.weathering, this.waxed, consumer);
/*     */   }
/*     */   
/*     */   public static <T, U> void zipApply(ByState<T> first, ByState<U> second, BiConsumer<T, U> consumer) {
/* 127 */     consumer.accept(first.unaffected, (U)second.unaffected);
/* 128 */     consumer.accept(first.exposed, (U)second.exposed);
/* 129 */     consumer.accept(first.weathered, (U)second.weathered);
/* 130 */     consumer.accept(first.oxidized, (U)second.oxidized);
/*     */   }
/*     */   
/*     */   public static <T, U, R> ByState<R> zipMap(ByState<T> first, ByState<U> second, BiFunction<T, U, R> operation) {
/* 134 */     return new ByState<>(operation
/* 135 */         .apply(first.unaffected, (U)second.unaffected), operation
/* 136 */         .apply(first.exposed, (U)second.exposed), operation
/* 137 */         .apply(first.weathered, (U)second.weathered), operation
/* 138 */         .apply(first.oxidized, (U)second.oxidized));
/*     */   }
/*     */   
/*     */   public static final class ByState<T> extends Record {
/*     */     private final T unaffected;
/*     */     
/*     */     private final T exposed;
/*     */     
/*     */     private final T weathered;
/*     */     
/*     */     private final T oxidized;
/*     */     
/*     */     public ByState(T unaffected, T exposed, T weathered, T oxidized) {
/* 142 */       this.unaffected = unaffected;
/* 142 */       this.exposed = exposed;
/* 142 */       this.weathered = weathered;
/* 142 */       this.oxidized = oxidized;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState<TT;>;
/*     */     }
/*     */     
/*     */     public T unaffected() {
/* 142 */       return this.unaffected;
/*     */     }
/*     */     
/*     */     public T exposed() {
/* 142 */       return this.exposed;
/*     */     }
/*     */     
/*     */     public T weathered() {
/* 142 */       return this.weathered;
/*     */     }
/*     */     
/*     */     public T oxidized() {
/* 142 */       return this.oxidized;
/*     */     }
/*     */     
/*     */     public static <T> ByState<T> create(T value) {
/* 149 */       return new ByState<>(value, value, value, value);
/*     */     }
/*     */     
/*     */     public <U> ByState<U> map(Function<T, U> mapper) {
/* 153 */       return new ByState((T)mapper
/* 154 */           .apply(this.unaffected), (T)mapper
/* 155 */           .apply(this.exposed), (T)mapper
/* 156 */           .apply(this.weathered), (T)mapper
/* 157 */           .apply(this.oxidized));
/*     */     }
/*     */     
/*     */     public T pick(WeatheringCopper.WeatherState state) {
/*     */       // Byte code:
/*     */       //   0: getstatic net/minecraft/world/level/block/WeatheringCopperCollection$1.$SwitchMap$net$minecraft$world$level$block$WeatheringCopper$WeatherState : [I
/*     */       //   3: aload_1
/*     */       //   4: invokevirtual ordinal : ()I
/*     */       //   7: iaload
/*     */       //   8: tableswitch default -> 40, 1 -> 50, 2 -> 57, 3 -> 64, 4 -> 71
/*     */       //   40: new java/lang/MatchException
/*     */       //   43: dup
/*     */       //   44: aconst_null
/*     */       //   45: aconst_null
/*     */       //   46: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   49: athrow
/*     */       //   50: aload_0
/*     */       //   51: getfield unaffected : Ljava/lang/Object;
/*     */       //   54: goto -> 75
/*     */       //   57: aload_0
/*     */       //   58: getfield exposed : Ljava/lang/Object;
/*     */       //   61: goto -> 75
/*     */       //   64: aload_0
/*     */       //   65: getfield weathered : Ljava/lang/Object;
/*     */       //   68: goto -> 75
/*     */       //   71: aload_0
/*     */       //   72: getfield oxidized : Ljava/lang/Object;
/*     */       //   75: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #162	-> 0
/*     */       //   #163	-> 50
/*     */       //   #164	-> 57
/*     */       //   #165	-> 64
/*     */       //   #166	-> 71
/*     */       //   #162	-> 75
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	76	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState;
/*     */       //   0	76	1	state	Lnet/minecraft/world/level/block/WeatheringCopper$WeatherState;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	76	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState<TT;>;
/*     */     }
/*     */     
/*     */     public void forEach(Consumer<T> consumer) {
/* 171 */       consumer.accept(this.unaffected);
/* 172 */       consumer.accept(this.exposed);
/* 173 */       consumer.accept(this.weathered);
/* 174 */       consumer.accept(this.oxidized);
/*     */     }
/*     */     
/*     */     public void progressMapping(BiConsumer<T, T> consumer) {
/* 178 */       consumer.accept(this.unaffected, this.exposed);
/* 179 */       consumer.accept(this.exposed, this.weathered);
/* 180 */       consumer.accept(this.weathered, this.oxidized);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\WeatheringCopperCollection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */