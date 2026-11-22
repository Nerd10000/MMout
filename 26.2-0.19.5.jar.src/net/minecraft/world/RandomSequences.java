/*     */ package net.minecraft.world;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.levelgen.PositionalRandomFactory;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ 
/*     */ public class RandomSequences extends SavedData {
/*     */   public static final Codec<RandomSequences> CODEC;
/*     */   
/*     */   static {
/*  19 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("salt").forGetter(RandomSequences::salt), (App)Codec.BOOL.optionalFieldOf("include_world_seed", Boolean.valueOf(true)).forGetter(RandomSequences::includeWorldSeed), (App)Codec.BOOL.optionalFieldOf("include_sequence_id", Boolean.valueOf(true)).forGetter(RandomSequences::includeSequenceId), (App)Codec.unboundedMap(Identifier.CODEC, RandomSequence.CODEC).fieldOf("sequences").forGetter(())).apply((Applicative)i, RandomSequences::new));
/*     */   }
/*     */   
/*  26 */   public static final SavedDataType<RandomSequences> TYPE = new SavedDataType(
/*  27 */       Identifier.withDefaultNamespace("random_sequences"), RandomSequences::new, CODEC, DataFixTypes.SAVED_DATA_RANDOM_SEQUENCES);
/*     */   
/*     */   private int salt;
/*     */   
/*     */   private boolean includeWorldSeed = true;
/*     */   
/*     */   private boolean includeSequenceId = true;
/*     */   
/*  36 */   private final Map<Identifier, RandomSequence> sequences = (Map<Identifier, RandomSequence>)new Object2ObjectOpenHashMap();
/*     */   
/*     */   private RandomSequences(int salt, boolean includeWorldSeed, boolean includeSequenceId, Map<Identifier, RandomSequence> sequences) {
/*  42 */     this.salt = salt;
/*  43 */     this.includeWorldSeed = includeWorldSeed;
/*  44 */     this.includeSequenceId = includeSequenceId;
/*  45 */     this.sequences.putAll(sequences);
/*     */   }
/*     */   
/*     */   private class DirtyMarkingRandomSource implements RandomSource {
/*     */     private final RandomSource random;
/*     */     
/*     */     private DirtyMarkingRandomSource(RandomSource random) {
/*  52 */       this.random = random;
/*     */     }
/*     */     
/*     */     public RandomSource fork() {
/*  57 */       RandomSequences.this.setDirty();
/*  58 */       return this.random.fork();
/*     */     }
/*     */     
/*     */     public PositionalRandomFactory forkPositional() {
/*  63 */       RandomSequences.this.setDirty();
/*  64 */       return this.random.forkPositional();
/*     */     }
/*     */     
/*     */     public void setSeed(long seed) {
/*  69 */       RandomSequences.this.setDirty();
/*  70 */       this.random.setSeed(seed);
/*     */     }
/*     */     
/*     */     public int nextInt() {
/*  75 */       RandomSequences.this.setDirty();
/*  76 */       return this.random.nextInt();
/*     */     }
/*     */     
/*     */     public int nextInt(int bound) {
/*  81 */       RandomSequences.this.setDirty();
/*  82 */       return this.random.nextInt(bound);
/*     */     }
/*     */     
/*     */     public long nextLong() {
/*  87 */       RandomSequences.this.setDirty();
/*  88 */       return this.random.nextLong();
/*     */     }
/*     */     
/*     */     public boolean nextBoolean() {
/*  93 */       RandomSequences.this.setDirty();
/*  94 */       return this.random.nextBoolean();
/*     */     }
/*     */     
/*     */     public float nextFloat() {
/*  99 */       RandomSequences.this.setDirty();
/* 100 */       return this.random.nextFloat();
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 105 */       RandomSequences.this.setDirty();
/* 106 */       return this.random.nextDouble();
/*     */     }
/*     */     
/*     */     public double nextGaussian() {
/* 111 */       RandomSequences.this.setDirty();
/* 112 */       return this.random.nextGaussian();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj) {
/* 117 */       if (this == obj)
/* 118 */         return true; 
/* 120 */       if (obj instanceof DirtyMarkingRandomSource) {
/* 120 */         DirtyMarkingRandomSource other = (DirtyMarkingRandomSource)obj;
/* 121 */         return this.random.equals(other.random);
/*     */       } 
/* 123 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 128 */       return Objects.hashCode(this.random);
/*     */     }
/*     */   }
/*     */   
/*     */   public RandomSource get(Identifier key, long worldSeed) {
/* 133 */     RandomSource random = ((RandomSequence)this.sequences.computeIfAbsent(key, rl -> createSequence(rl, worldSeed))).random();
/* 134 */     return new DirtyMarkingRandomSource(random);
/*     */   }
/*     */   
/*     */   private RandomSequence createSequence(Identifier key, long worldSeed) {
/* 138 */     return createSequence(key, worldSeed, this.salt, this.includeWorldSeed, this.includeSequenceId);
/*     */   }
/*     */   
/*     */   private RandomSequence createSequence(Identifier key, long worldSeed, int salt, boolean includeWorldSeed, boolean includeSequenceId) {
/* 142 */     long seed = (includeWorldSeed ? worldSeed : 0L) ^ salt;
/* 143 */     return new RandomSequence(seed, includeSequenceId ? Optional.<Identifier>of(key) : Optional.<Identifier>empty());
/*     */   }
/*     */   
/*     */   public void forAllSequences(BiConsumer<Identifier, RandomSequence> consumer) {
/* 147 */     this.sequences.forEach(consumer);
/*     */   }
/*     */   
/*     */   public void setSeedDefaults(int salt, boolean includeWorldSeed, boolean includeSequenceId) {
/* 151 */     this.salt = salt;
/* 152 */     this.includeWorldSeed = includeWorldSeed;
/* 153 */     this.includeSequenceId = includeSequenceId;
/*     */   }
/*     */   
/*     */   public int clear() {
/* 157 */     int count = this.sequences.size();
/* 158 */     this.sequences.clear();
/* 159 */     return count;
/*     */   }
/*     */   
/*     */   public void reset(Identifier id, long worldSeed) {
/* 163 */     this.sequences.put(id, createSequence(id, worldSeed));
/*     */   }
/*     */   
/*     */   public void reset(Identifier id, long worldSeed, int salt, boolean includeWorldSeed, boolean includeSequenceId) {
/* 167 */     this.sequences.put(id, createSequence(id, worldSeed, salt, includeWorldSeed, includeSequenceId));
/*     */   }
/*     */   
/*     */   private int salt() {
/* 171 */     return this.salt;
/*     */   }
/*     */   
/*     */   private boolean includeWorldSeed() {
/* 175 */     return this.includeWorldSeed;
/*     */   }
/*     */   
/*     */   private boolean includeSequenceId() {
/* 179 */     return this.includeSequenceId;
/*     */   }
/*     */   
/*     */   public RandomSequences() {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\RandomSequences.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */