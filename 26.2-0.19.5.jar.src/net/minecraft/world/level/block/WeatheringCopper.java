/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.ImmutableBiMap;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public interface WeatheringCopper extends ChangeOverTimeBlock<WeatheringCopper.WeatherState> {
/*     */   public static final Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK;
/*     */   
/*     */   static {
/*  21 */     NEXT_BY_BLOCK = (Supplier<BiMap<Block, Block>>)Suppliers.memoize(() -> {
/*     */           ImmutableBiMap.Builder<Block, Block> builder = ImmutableBiMap.builder();
/*     */           Stream.<WeatheringCopperCollection>of(new WeatheringCopperCollection[] { 
/*     */                 Blocks.COPPER_BLOCK, Blocks.CUT_COPPER, Blocks.CHISELED_COPPER, Blocks.CUT_COPPER_SLAB, Blocks.CUT_COPPER_STAIRS, Blocks.COPPER_DOOR, Blocks.COPPER_TRAPDOOR, Blocks.COPPER_BARS, Blocks.COPPER_GRATE, Blocks.COPPER_BULB, 
/*     */                 Blocks.COPPER_LANTERN, Blocks.COPPER_CHEST, Blocks.COPPER_GOLEM_STATUE, Blocks.LIGHTNING_ROD, Blocks.COPPER_CHAIN }).forEach(());
/*     */           return (BiMap)builder.build();
/*     */         });
/*     */   }
/*     */   
/*  31 */   public static final Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = (Supplier<BiMap<Block, Block>>)Suppliers.memoize(() -> ((BiMap)NEXT_BY_BLOCK.get()).inverse());
/*     */   
/*     */   static Optional<Block> getPrevious(Block block) {
/*  34 */     return Optional.ofNullable((Block)((BiMap)PREVIOUS_BY_BLOCK.get()).get(block));
/*     */   }
/*     */   
/*     */   static Block getFirst(Block block) {
/*  38 */     Block candiate = block;
/*  39 */     Block previous = (Block)((BiMap)PREVIOUS_BY_BLOCK.get()).get(candiate);
/*  40 */     while (previous != null) {
/*  41 */       candiate = previous;
/*  42 */       previous = (Block)((BiMap)PREVIOUS_BY_BLOCK.get()).get(candiate);
/*     */     } 
/*  44 */     return candiate;
/*     */   }
/*     */   
/*     */   static Optional<BlockState> getPrevious(BlockState state) {
/*  48 */     return getPrevious(state.getBlock()).map(s -> s.withPropertiesOf(state));
/*     */   }
/*     */   
/*     */   static Optional<Block> getNext(Block block) {
/*  52 */     return Optional.ofNullable((Block)((BiMap)NEXT_BY_BLOCK.get()).get(block));
/*     */   }
/*     */   
/*     */   static BlockState getFirst(BlockState state) {
/*  56 */     return getFirst(state.getBlock()).withPropertiesOf(state);
/*     */   }
/*     */   
/*     */   default Optional<BlockState> getNext(BlockState state) {
/*  61 */     return getNext(state.getBlock()).map(s -> s.withPropertiesOf(state));
/*     */   }
/*     */   
/*     */   default float getChanceModifier() {
/*  66 */     if (getAge() == WeatherState.UNAFFECTED)
/*  67 */       return 0.75F; 
/*  69 */     return 1.0F;
/*     */   }
/*     */   
/*     */   public enum WeatherState implements StringRepresentable {
/*  75 */     UNAFFECTED("unaffected"),
/*  76 */     EXPOSED("exposed"),
/*  77 */     WEATHERED("weathered"),
/*  78 */     OXIDIZED("oxidized");
/*     */     
/*  80 */     public static final IntFunction<WeatherState> BY_ID = ByIdMap.continuous(Enum::ordinal, (Object[])values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/*     */     
/*  81 */     public static final Codec<WeatherState> CODEC = (Codec<WeatherState>)StringRepresentable.fromEnum(WeatherState::values);
/*     */     
/*  82 */     public static final StreamCodec<ByteBuf, WeatherState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     WeatherState(String name) {
/*  87 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/*  92 */       return this.name;
/*     */     }
/*     */     
/*     */     public static void forEach(Consumer<WeatherState> consumer) {
/*  96 */       for (WeatherState weatherState : values())
/*  97 */         consumer.accept(weatherState); 
/*     */     }
/*     */     
/*     */     public WeatherState next() {
/* 102 */       return BY_ID.apply(ordinal() + 1);
/*     */     }
/*     */     
/*     */     public WeatherState previous() {
/* 106 */       return BY_ID.apply(ordinal() - 1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\WeatheringCopper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */