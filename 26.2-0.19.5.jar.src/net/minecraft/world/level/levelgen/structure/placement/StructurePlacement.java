/*     */ package net.minecraft.world.level.levelgen.structure.placement;
/*     */ 
/*     */ import com.mojang.datafixers.Products;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
/*     */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ 
/*     */ public abstract class StructurePlacement {
/*  23 */   public static final Codec<StructurePlacement> CODEC = BuiltInRegistries.STRUCTURE_PLACEMENT.byNameCodec().dispatch(StructurePlacement::type, StructurePlacementType::codec);
/*     */   
/*     */   private static final int HIGHLY_ARBITRARY_RANDOM_SALT = 10387320;
/*     */   
/*     */   private final Vec3i locateOffset;
/*     */   
/*     */   private final FrequencyReductionMethod frequencyReductionMethod;
/*     */   
/*     */   private final float frequency;
/*     */   
/*     */   private final int salt;
/*     */   
/*     */   private final Optional<ExclusionZone> exclusionZone;
/*     */   
/*     */   protected static <S extends StructurePlacement> Products.P5<RecordCodecBuilder.Mu<S>, Vec3i, FrequencyReductionMethod, Float, Integer, Optional<ExclusionZone>> placementCodec(RecordCodecBuilder.Instance<S> i) {
/*  27 */     return i.group(
/*  28 */         (App)Vec3i.offsetCodec(16).optionalFieldOf("locate_offset", Vec3i.ZERO).forGetter(StructurePlacement::locateOffset), (App)FrequencyReductionMethod.CODEC
/*  29 */         .optionalFieldOf("frequency_reduction_method", FrequencyReductionMethod.DEFAULT).forGetter(StructurePlacement::frequencyReductionMethod), 
/*  30 */         (App)Codec.floatRange(0.0F, 1.0F).optionalFieldOf("frequency", Float.valueOf(1.0F)).forGetter(StructurePlacement::frequency), (App)ExtraCodecs.NON_NEGATIVE_INT
/*  31 */         .fieldOf("salt").forGetter(StructurePlacement::salt), (App)ExclusionZone.CODEC
/*  32 */         .optionalFieldOf("exclusion_zone").forGetter(StructurePlacement::exclusionZone));
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface FrequencyReducer {
/*     */     boolean shouldGenerate(long param1Long, int param1Int1, int param1Int2, int param1Int3, float param1Float);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static final class ExclusionZone extends Record {
/*     */     private final Holder<StructureSet> otherSet;
/*     */     
/*     */     private final int chunkCount;
/*     */     
/*     */     public static final Codec<ExclusionZone> CODEC;
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #37	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #37	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #37	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Holder<StructureSet> otherSet() {
/*  37 */       return this.otherSet;
/*     */     }
/*     */     
/*     */     public int chunkCount() {
/*  37 */       return this.chunkCount;
/*     */     }
/*     */     
/*     */     public ExclusionZone(Holder<StructureSet> otherSet, int chunkCount) {
/*  38 */       this.otherSet = otherSet;
/*  38 */       this.chunkCount = chunkCount;
/*     */     }
/*     */     
/*     */     static {
/*  39 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryFileCodec.create(Registries.STRUCTURE_SET, StructureSet.DIRECT_CODEC, false).fieldOf("other_set").forGetter(ExclusionZone::otherSet), (App)Codec.intRange(1, 16).fieldOf("chunk_count").forGetter(ExclusionZone::chunkCount)).apply((Applicative)i, ExclusionZone::new));
/*     */     }
/*     */     
/*     */     private boolean isPlacementForbidden(ChunkGeneratorStructureState state, int sourceX, int sourceZ) {
/*  46 */       return state.hasStructureChunkInRange(this.otherSet, sourceX, sourceZ, this.chunkCount);
/*     */     }
/*     */   }
/*     */   
/*     */   protected StructurePlacement(Vec3i locateOffset, FrequencyReductionMethod frequencyReductionMethod, float frequency, int salt, Optional<ExclusionZone> exclusionZone) {
/*  57 */     this.locateOffset = locateOffset;
/*  58 */     this.frequencyReductionMethod = frequencyReductionMethod;
/*  59 */     this.frequency = frequency;
/*  60 */     this.salt = salt;
/*  61 */     this.exclusionZone = exclusionZone;
/*     */   }
/*     */   
/*     */   protected Vec3i locateOffset() {
/*  65 */     return this.locateOffset;
/*     */   }
/*     */   
/*     */   protected FrequencyReductionMethod frequencyReductionMethod() {
/*  69 */     return this.frequencyReductionMethod;
/*     */   }
/*     */   
/*     */   protected float frequency() {
/*  73 */     return this.frequency;
/*     */   }
/*     */   
/*     */   protected int salt() {
/*  77 */     return this.salt;
/*     */   }
/*     */   
/*     */   protected Optional<ExclusionZone> exclusionZone() {
/*  81 */     return this.exclusionZone;
/*     */   }
/*     */   
/*     */   public boolean isStructureChunk(ChunkGeneratorStructureState state, int sourceX, int sourceZ) {
/*  85 */     return (isPlacementChunk(state, sourceX, sourceZ) && 
/*  86 */       applyAdditionalChunkRestrictions(sourceX, sourceZ, state.getLevelSeed()) && 
/*  87 */       applyInteractionsWithOtherStructures(state, sourceX, sourceZ));
/*     */   }
/*     */   
/*     */   public boolean applyAdditionalChunkRestrictions(int sourceX, int sourceZ, long levelSeed) {
/*  91 */     if (this.frequency < 1.0F && !this.frequencyReductionMethod.shouldGenerate(levelSeed, this.salt, sourceX, sourceZ, this.frequency))
/*  92 */       return false; 
/*  95 */     return true;
/*     */   }
/*     */   
/*     */   public boolean applyInteractionsWithOtherStructures(ChunkGeneratorStructureState state, int sourceX, int sourceZ) {
/*  99 */     if (this.exclusionZone.isPresent() && ((ExclusionZone)this.exclusionZone.get()).isPlacementForbidden(state, sourceX, sourceZ))
/* 100 */       return false; 
/* 103 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract boolean isPlacementChunk(ChunkGeneratorStructureState paramChunkGeneratorStructureState, int paramInt1, int paramInt2);
/*     */   
/*     */   public BlockPos getLocatePos(ChunkPos chunkPos) {
/* 109 */     return (new BlockPos(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ())).offset(locateOffset());
/*     */   }
/*     */   
/*     */   public abstract StructurePlacementType<?> type();
/*     */   
/*     */   private static boolean probabilityReducer(long seed, int salt, int sourceX, int sourceZ, float probability) {
/* 120 */     WorldgenRandom random = new WorldgenRandom((RandomSource)new LegacyRandomSource(0L));
/* 121 */     random.setLargeFeatureWithSalt(seed, salt, sourceX, sourceZ);
/* 122 */     return (random.nextFloat() < probability);
/*     */   }
/*     */   
/*     */   private static boolean legacyProbabilityReducerWithDouble(long seed, int salt, int sourceX, int sourceZ, float probability) {
/* 126 */     WorldgenRandom random = new WorldgenRandom((RandomSource)new LegacyRandomSource(0L));
/* 127 */     random.setLargeFeatureSeed(seed, sourceX, sourceZ);
/* 128 */     return (random.nextDouble() < probability);
/*     */   }
/*     */   
/*     */   private static boolean legacyArbitrarySaltProbabilityReducer(long seed, int salt, int sourceX, int sourceZ, float probability) {
/* 133 */     WorldgenRandom random = new WorldgenRandom((RandomSource)new LegacyRandomSource(0L));
/* 134 */     random.setLargeFeatureWithSalt(seed, sourceX, sourceZ, 10387320);
/* 135 */     return (random.nextFloat() < probability);
/*     */   }
/*     */   
/*     */   private static boolean legacyPillagerOutpostReducer(long seed, int salt, int sourceX, int sourceZ, float probability) {
/* 139 */     int cx = sourceX >> 4;
/* 140 */     int cz = sourceZ >> 4;
/* 143 */     WorldgenRandom random = new WorldgenRandom((RandomSource)new LegacyRandomSource(0L));
/* 144 */     random.setSeed((cx ^ cz << 4) ^ seed);
/* 145 */     random.nextInt();
/* 147 */     return (random.nextInt((int)(1.0F / probability)) == 0);
/*     */   }
/*     */   
/*     */   public enum FrequencyReductionMethod implements StringRepresentable {
/* 151 */     DEFAULT("default", StructurePlacement::probabilityReducer),
/* 152 */     LEGACY_TYPE_1("legacy_type_1", StructurePlacement::legacyPillagerOutpostReducer),
/* 153 */     LEGACY_TYPE_2("legacy_type_2", StructurePlacement::legacyArbitrarySaltProbabilityReducer),
/* 154 */     LEGACY_TYPE_3("legacy_type_3", StructurePlacement::legacyProbabilityReducerWithDouble);
/*     */     
/* 157 */     public static final Codec<FrequencyReductionMethod> CODEC = (Codec<FrequencyReductionMethod>)StringRepresentable.fromEnum(FrequencyReductionMethod::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final StructurePlacement.FrequencyReducer reducer;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     FrequencyReductionMethod(String name, StructurePlacement.FrequencyReducer reducer) {
/* 163 */       this.name = name;
/* 164 */       this.reducer = reducer;
/*     */     }
/*     */     
/*     */     public boolean shouldGenerate(long seed, int salt, int sourceX, int sourceZ, float probability) {
/* 168 */       return this.reducer.shouldGenerate(seed, salt, sourceX, sourceZ, probability);
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 173 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\placement\StructurePlacement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */