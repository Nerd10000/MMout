/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.FeatureCountTracker;
/*    */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*    */ 
/*    */ public final class PlacedFeature extends Record {
/*    */   private final Holder<ConfiguredFeature<?, ?>> feature;
/*    */   
/*    */   private final List<PlacementModifier> placement;
/*    */   
/*    */   public static final Codec<PlacedFeature> DIRECT_CODEC;
/*    */   
/*    */   public PlacedFeature(Holder<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> placement) {
/* 23 */     this.feature = feature;
/* 23 */     this.placement = placement;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/placement/PlacedFeature;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/placement/PlacedFeature;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/placement/PlacedFeature;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/placement/PlacedFeature;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<ConfiguredFeature<?, ?>> feature() {
/* 23 */     return this.feature;
/*    */   }
/*    */   
/*    */   public List<PlacementModifier> placement() {
/* 23 */     return this.placement;
/*    */   }
/*    */   
/*    */   static {
/* 27 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)ConfiguredFeature.CODEC.fieldOf("feature").forGetter(()), (App)PlacementModifier.CODEC.listOf().fieldOf("placement").forGetter(())).apply((Applicative)i, PlacedFeature::new));
/*    */   }
/*    */   
/* 32 */   public static final Codec<Holder<PlacedFeature>> CODEC = (Codec<Holder<PlacedFeature>>)RegistryFileCodec.create(Registries.PLACED_FEATURE, DIRECT_CODEC);
/*    */   
/* 33 */   public static final Codec<HolderSet<PlacedFeature>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.PLACED_FEATURE, DIRECT_CODEC);
/*    */   
/* 35 */   public static final Codec<List<HolderSet<PlacedFeature>>> LIST_OF_LISTS_CODEC = RegistryCodecs.homogeneousList(Registries.PLACED_FEATURE, DIRECT_CODEC, true).listOf();
/*    */   
/*    */   public boolean place(WorldGenLevel level, ChunkGenerator generator, RandomSource random, BlockPos origin) {
/* 38 */     return placeWithContext(new PlacementContext(level, generator, Optional.empty()), random, origin);
/*    */   }
/*    */   
/*    */   public boolean placeWithBiomeCheck(WorldGenLevel level, ChunkGenerator generator, RandomSource random, BlockPos origin) {
/* 42 */     return placeWithContext(new PlacementContext(level, generator, Optional.of(this)), random, origin);
/*    */   }
/*    */   
/*    */   private boolean placeWithContext(PlacementContext context, RandomSource random, BlockPos origin) {
/* 46 */     Stream<BlockPos> placements = Stream.of(origin);
/* 47 */     for (PlacementModifier placementModifier : this.placement)
/* 48 */       placements = placements.flatMap(p -> placementModifier.getPositions(context, random, p)); 
/* 51 */     ConfiguredFeature<?, ?> feature = (ConfiguredFeature<?, ?>)this.feature.value();
/* 52 */     MutableBoolean placedAny = new MutableBoolean();
/* 53 */     placements.forEach(pos -> {
/*    */           if (feature.place(context.getLevel(), context.generator(), random, pos)) {
/*    */             placedAny.setTrue();
/*    */             if (SharedConstants.DEBUG_FEATURE_COUNT)
/*    */               FeatureCountTracker.featurePlaced(context.getLevel().getLevel(), feature, context.topFeature()); 
/*    */           } 
/*    */         });
/* 61 */     return placedAny.isTrue();
/*    */   }
/*    */   
/*    */   public Stream<Holder<ConfiguredFeature<?, ?>>> getFeatures() {
/* 65 */     return Stream.concat(
/* 66 */         Stream.of(this.feature), ((ConfiguredFeature)this.feature
/* 67 */         .value()).getSubFeatures());
/*    */   }
/*    */   
/*    */   public String toString() {
/* 73 */     return "Placed " + String.valueOf(this.feature);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\PlacedFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */