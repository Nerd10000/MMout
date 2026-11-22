/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public final class Preset extends Record {
/*    */   private final Identifier id;
/*    */   
/*    */   private final SourceProvider provider;
/*    */   
/*    */   public Preset(Identifier id, SourceProvider provider) {
/* 53 */     this.id = id;
/* 53 */     this.provider = provider;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterList$Preset;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #53	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterList$Preset;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterList$Preset;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #53	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterList$Preset;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterList$Preset;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #53	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterList$Preset;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/* 53 */     return this.id;
/*    */   }
/*    */   
/*    */   public SourceProvider provider() {
/* 53 */     return this.provider;
/*    */   }
/*    */   
/* 59 */   public static final Preset NETHER = new Preset(Identifier.withDefaultNamespace("nether"), new SourceProvider() {
/*    */         public <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> lookup) {
/* 62 */           return new Climate.ParameterList<>(List.of(
/* 63 */                 Pair.of(Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), lookup.apply(Biomes.NETHER_WASTES)), 
/* 64 */                 Pair.of(Climate.parameters(0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), lookup.apply(Biomes.SOUL_SAND_VALLEY)), 
/* 65 */                 Pair.of(Climate.parameters(0.4F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), lookup.apply(Biomes.CRIMSON_FOREST)), 
/* 66 */                 Pair.of(Climate.parameters(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.375F), lookup.apply(Biomes.WARPED_FOREST)), 
/* 67 */                 Pair.of(Climate.parameters(-0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.175F), lookup.apply(Biomes.BASALT_DELTAS))));
/*    */         }
/*    */       });
/*    */   
/*    */   @FunctionalInterface
/*    */   private static interface SourceProvider {
/*    */     <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> param2Function);
/*    */   }
/*    */   
/* 72 */   public static final Preset OVERWORLD = new Preset(Identifier.withDefaultNamespace("overworld"), new SourceProvider() {
/*    */         public <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> lookup) {
/* 75 */           return MultiNoiseBiomeSourceParameterList.Preset.generateOverworldBiomes(lookup);
/*    */         }
/*    */       });
/*    */   
/*    */   private static final Map<Identifier, Preset> BY_NAME;
/*    */   
/*    */   public static final Codec<Preset> CODEC;
/*    */   
/*    */   static {
/* 80 */     BY_NAME = (Map<Identifier, Preset>)Stream.<Preset>of(new Preset[] { NETHER, OVERWORLD }).collect(Collectors.toMap(Preset::id, p -> p));
/* 82 */     CODEC = Identifier.CODEC.flatXmap(name -> (DataResult)Optional.<Preset>ofNullable(BY_NAME.get(name)).map(DataResult::success).orElseGet(()), p -> DataResult.success(p.id));
/*    */   }
/*    */   
/*    */   private static <T> Climate.ParameterList<T> generateOverworldBiomes(Function<ResourceKey<Biome>, T> lookup) {
/* 88 */     ImmutableList.Builder<Pair<Climate.ParameterPoint, T>> builder = ImmutableList.builder();
/* 89 */     (new OverworldBiomeBuilder()).addBiomes(p -> builder.add(p.mapSecond(lookup)));
/* 90 */     return new Climate.ParameterList<>((List<Pair<Climate.ParameterPoint, T>>)builder.build());
/*    */   }
/*    */   
/*    */   public Stream<ResourceKey<Biome>> usedBiomes() {
/* 94 */     return this.provider.apply(e -> e).values().stream().map(Pair::getSecond).distinct();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\MultiNoiseBiomeSourceParameterList$Preset.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */