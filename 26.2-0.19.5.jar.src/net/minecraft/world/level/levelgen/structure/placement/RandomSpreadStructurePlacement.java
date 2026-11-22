/*    */ package net.minecraft.world.level.levelgen.structure.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function8;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ 
/*    */ public class RandomSpreadStructurePlacement extends StructurePlacement {
/*    */   public static final MapCodec<RandomSpreadStructurePlacement> CODEC;
/*    */   
/*    */   private final int spacing;
/*    */   
/*    */   private final int separation;
/*    */   
/*    */   private final RandomSpreadType spreadType;
/*    */   
/*    */   static {
/* 25 */     CODEC = RecordCodecBuilder.mapCodec(i -> placementCodec(i).and(i.group((App)Codec.intRange(0, 4096).fieldOf("spacing").forGetter(RandomSpreadStructurePlacement::spacing), (App)Codec.intRange(0, 4096).fieldOf("separation").forGetter(RandomSpreadStructurePlacement::separation), (App)RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(RandomSpreadStructurePlacement::spreadType))).apply((Applicative)i, RandomSpreadStructurePlacement::new)).validate(RandomSpreadStructurePlacement::validate);
/*    */   }
/*    */   
/*    */   private static DataResult<RandomSpreadStructurePlacement> validate(RandomSpreadStructurePlacement c) {
/* 28 */     if (c.spacing <= c.separation)
/* 29 */       return DataResult.error(() -> "Spacing has to be larger than separation"); 
/* 31 */     return DataResult.success(c);
/*    */   }
/*    */   
/*    */   public RandomSpreadStructurePlacement(Vec3i locateOffset, StructurePlacement.FrequencyReductionMethod frequencyReductionMethod, float frequency, int salt, Optional<StructurePlacement.ExclusionZone> exclusionZone, int spacing, int separation, RandomSpreadType spreadType) {
/* 39 */     super(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone);
/* 40 */     this.spacing = spacing;
/* 41 */     this.separation = separation;
/* 42 */     this.spreadType = spreadType;
/*    */   }
/*    */   
/*    */   public RandomSpreadStructurePlacement(int spacing, int separation, RandomSpreadType spreadType, int salt) {
/* 46 */     this(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.DEFAULT, 1.0F, salt, Optional.empty(), spacing, separation, spreadType);
/*    */   }
/*    */   
/*    */   public int spacing() {
/* 50 */     return this.spacing;
/*    */   }
/*    */   
/*    */   public int separation() {
/* 54 */     return this.separation;
/*    */   }
/*    */   
/*    */   public RandomSpreadType spreadType() {
/* 58 */     return this.spreadType;
/*    */   }
/*    */   
/*    */   public ChunkPos getPotentialStructureChunk(long seed, int sourceX, int sourceZ) {
/* 62 */     int spacedGridX = Math.floorDiv(sourceX, this.spacing);
/* 63 */     int spacedGridZ = Math.floorDiv(sourceZ, this.spacing);
/* 65 */     WorldgenRandom random = new WorldgenRandom((RandomSource)new LegacyRandomSource(0L));
/* 66 */     random.setLargeFeatureWithSalt(seed, spacedGridX, spacedGridZ, salt());
/* 68 */     int limit = this.spacing - this.separation;
/* 69 */     int spreadX = this.spreadType.evaluate((RandomSource)random, limit);
/* 70 */     int spreadZ = this.spreadType.evaluate((RandomSource)random, limit);
/* 72 */     return new ChunkPos(spacedGridX * this.spacing + spreadX, spacedGridZ * this.spacing + spreadZ);
/*    */   }
/*    */   
/*    */   protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int sourceX, int sourceZ) {
/* 80 */     ChunkPos chunkPos = getPotentialStructureChunk(state.getLevelSeed(), sourceX, sourceZ);
/* 81 */     return (chunkPos.x() == sourceX && chunkPos.z() == sourceZ);
/*    */   }
/*    */   
/*    */   public StructurePlacementType<?> type() {
/* 86 */     return StructurePlacementType.RANDOM_SPREAD;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\placement\RandomSpreadStructurePlacement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */