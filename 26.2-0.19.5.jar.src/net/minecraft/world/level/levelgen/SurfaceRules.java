/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.DoubleSupplier;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.levelgen.placement.CaveSurface;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ public class SurfaceRules {
/*     */   protected static final class Context {
/*     */     private static final int HOW_FAR_BELOW_PRELIMINARY_SURFACE_LEVEL_TO_BUILD_SURFACE = 8;
/*     */     
/*     */     private static final int SURFACE_CELL_BITS = 4;
/*     */     
/*     */     private static final int SURFACE_CELL_SIZE = 16;
/*     */     
/*     */     private static final int SURFACE_CELL_MASK = 15;
/*     */     
/*     */     private final SurfaceSystem system;
/*     */     
/*  51 */     private final SurfaceRules.Condition temperature = new TemperatureHelperCondition(this);
/*     */     
/*  52 */     private final SurfaceRules.Condition steep = new SteepMaterialCondition(this);
/*     */     
/*  53 */     private final SurfaceRules.Condition hole = new HoleCondition(this);
/*     */     
/*  54 */     private final SurfaceRules.Condition abovePreliminarySurface = new AbovePreliminarySurfaceCondition();
/*     */     
/*     */     private final RandomState randomState;
/*     */     
/*     */     private final ChunkAccess chunk;
/*     */     
/*     */     private final NoiseChunk noiseChunk;
/*     */     
/*     */     private final Function<BlockPos, Holder<Biome>> biomeGetter;
/*     */     
/*     */     private final WorldGenerationContext context;
/*     */     
/*     */     private final Set<Holder<Biome>> possibleBiomes;
/*     */     
/*  63 */     private long lastPreliminarySurfaceCellOrigin = Long.MAX_VALUE;
/*     */     
/*  64 */     private final int[] preliminarySurfaceCache = new int[4];
/*     */     
/*  66 */     private final Map<ResourceKey<NormalNoise.NoiseParameters>, DoubleSupplier> noiseSamplers2d = new IdentityHashMap<>();
/*     */     
/*  67 */     private final Map<ResourceKey<NormalNoise.NoiseParameters>, DoubleSupplier> noiseSamplers3d = new IdentityHashMap<>();
/*     */     
/*  70 */     private long lastUpdateXZ = -9223372036854775807L;
/*     */     
/*     */     private int blockX;
/*     */     
/*     */     private int blockZ;
/*     */     
/*     */     private int surfaceDepth;
/*     */     
/*  76 */     private long lastSurfaceDepth2Update = this.lastUpdateXZ - 1L;
/*     */     
/*     */     private double surfaceSecondary;
/*     */     
/*  79 */     private long lastMinSurfaceLevelUpdate = this.lastUpdateXZ - 1L;
/*     */     
/*     */     private int minSurfaceLevel;
/*     */     
/*  83 */     private long lastUpdateY = -9223372036854775807L;
/*     */     
/*  84 */     private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/*     */     
/*     */     private Holder<Biome> biome;
/*     */     
/*     */     private int blockY;
/*     */     
/*     */     private int waterHeight;
/*     */     
/*     */     private int stoneDepthBelow;
/*     */     
/*     */     private int stoneDepthAbove;
/*     */     
/*     */     protected Context(SurfaceSystem system, RandomState randomState, ChunkAccess chunk, NoiseChunk noiseChunk, Function<BlockPos, Holder<Biome>> biomeGetter, WorldGenerationContext context, Set<Holder<Biome>> possibleBiomes) {
/*  92 */       this.system = system;
/*  93 */       this.randomState = randomState;
/*  94 */       this.chunk = chunk;
/*  95 */       this.noiseChunk = noiseChunk;
/*  96 */       this.biomeGetter = biomeGetter;
/*  97 */       this.context = context;
/*  98 */       this.possibleBiomes = possibleBiomes;
/*     */     }
/*     */     
/*     */     protected void updateXZ(int blockX, int blockZ) {
/* 102 */       this.lastUpdateXZ++;
/* 103 */       this.lastUpdateY++;
/* 104 */       this.blockX = blockX;
/* 105 */       this.blockZ = blockZ;
/* 106 */       this.surfaceDepth = this.system.getSurfaceDepth(blockX, blockZ);
/*     */     }
/*     */     
/*     */     protected void updateY(int stoneDepthAbove, int stoneDepthBelow, int waterHeight, int blockY) {
/* 110 */       this.lastUpdateY++;
/* 111 */       this.biome = null;
/* 112 */       this.blockY = blockY;
/* 113 */       this.waterHeight = waterHeight;
/* 114 */       this.stoneDepthBelow = stoneDepthBelow;
/* 115 */       this.stoneDepthAbove = stoneDepthAbove;
/*     */     }
/*     */     
/*     */     protected double getSurfaceSecondary() {
/* 119 */       if (this.lastSurfaceDepth2Update != this.lastUpdateXZ) {
/* 120 */         this.lastSurfaceDepth2Update = this.lastUpdateXZ;
/* 121 */         this.surfaceSecondary = this.system.getSurfaceSecondary(this.blockX, this.blockZ);
/*     */       } 
/* 123 */       return this.surfaceSecondary;
/*     */     }
/*     */     
/*     */     protected Holder<Biome> getBiome() {
/* 127 */       if (this.biome == null)
/* 128 */         this.biome = this.biomeGetter.apply(this.pos.set(this.blockX, this.blockY, this.blockZ)); 
/* 130 */       return this.biome;
/*     */     }
/*     */     
/*     */     public int getSeaLevel() {
/* 134 */       return this.system.getSeaLevel();
/*     */     }
/*     */     
/*     */     private static int blockCoordToSurfaceCell(int blockCoord) {
/* 138 */       return blockCoord >> 4;
/*     */     }
/*     */     
/*     */     private static int surfaceCellToBlockCoord(int cellCoord) {
/* 142 */       return cellCoord << 4;
/*     */     }
/*     */     
/*     */     protected int getMinSurfaceLevel() {
/* 147 */       if (this.lastMinSurfaceLevelUpdate != this.lastUpdateXZ) {
/* 148 */         this.lastMinSurfaceLevelUpdate = this.lastUpdateXZ;
/* 149 */         int cornerCellX = blockCoordToSurfaceCell(this.blockX);
/* 150 */         int cornerCellZ = blockCoordToSurfaceCell(this.blockZ);
/* 152 */         long preliminarySurfaceCellOrigin = ChunkPos.pack(cornerCellX, cornerCellZ);
/* 153 */         if (this.lastPreliminarySurfaceCellOrigin != preliminarySurfaceCellOrigin) {
/* 154 */           this.lastPreliminarySurfaceCellOrigin = preliminarySurfaceCellOrigin;
/* 156 */           this.preliminarySurfaceCache[0] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX), surfaceCellToBlockCoord(cornerCellZ));
/* 157 */           this.preliminarySurfaceCache[1] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX + 1), surfaceCellToBlockCoord(cornerCellZ));
/* 158 */           this.preliminarySurfaceCache[2] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX), surfaceCellToBlockCoord(cornerCellZ + 1));
/* 159 */           this.preliminarySurfaceCache[3] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX + 1), surfaceCellToBlockCoord(cornerCellZ + 1));
/*     */         } 
/* 161 */         int preliminarySurfaceLevel = Mth.floor(Mth.lerp2(((this.blockX & 0xF) / 16.0F), ((this.blockZ & 0xF) / 16.0F), this.preliminarySurfaceCache[0], this.preliminarySurfaceCache[1], this.preliminarySurfaceCache[2], this.preliminarySurfaceCache[3]));
/* 169 */         this.minSurfaceLevel = preliminarySurfaceLevel + this.surfaceDepth - 8;
/*     */       } 
/* 171 */       return this.minSurfaceLevel;
/*     */     }
/*     */     
/*     */     protected DoubleSupplier getNoiseSampler(ResourceKey<NormalNoise.NoiseParameters> noiseId, boolean is3d) {
/* 175 */       if (is3d)
/* 176 */         return this.noiseSamplers3d.computeIfAbsent(noiseId, this::createNoiseSampler3d); 
/* 178 */       return this.noiseSamplers2d.computeIfAbsent(noiseId, this::createNoiseSampler2d);
/*     */     }
/*     */     
/*     */     private DoubleSupplier createNoiseSampler2d(ResourceKey<NormalNoise.NoiseParameters> noiseId) {
/* 183 */       final NormalNoise noise = this.randomState.getOrCreateNoise(noiseId);
/* 184 */       return new DoubleSupplier() {
/* 185 */           private long lastUpdateXZ = SurfaceRules.Context.this.lastUpdateXZ - 1L;
/*     */           
/*     */           private double lastNoise;
/*     */           
/*     */           public double getAsDouble() {
/* 190 */             if (this.lastUpdateXZ != SurfaceRules.Context.this.lastUpdateXZ) {
/* 191 */               this.lastNoise = noise.getValue(SurfaceRules.Context.this.blockX, 0.0D, SurfaceRules.Context.this.blockZ);
/* 192 */               this.lastUpdateXZ = SurfaceRules.Context.this.lastUpdateXZ;
/*     */             } 
/* 194 */             return this.lastNoise;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     private DoubleSupplier createNoiseSampler3d(ResourceKey<NormalNoise.NoiseParameters> noiseId) {
/* 200 */       final NormalNoise noise = this.randomState.getOrCreateNoise(noiseId);
/* 201 */       return new DoubleSupplier() {
/* 202 */           private long lastUpdateY = SurfaceRules.Context.this.lastUpdateY - 1L;
/*     */           
/*     */           private double lastNoise;
/*     */           
/*     */           public double getAsDouble() {
/* 207 */             if (this.lastUpdateY != SurfaceRules.Context.this.lastUpdateY) {
/* 208 */               this.lastNoise = noise.getValue(SurfaceRules.Context.this.blockX, SurfaceRules.Context.this.blockY, SurfaceRules.Context.this.blockZ);
/* 209 */               this.lastUpdateY = SurfaceRules.Context.this.lastUpdateY;
/*     */             } 
/* 211 */             return this.lastNoise;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     private static final class HoleCondition extends SurfaceRules.LazyXZCondition {
/*     */       private HoleCondition(SurfaceRules.Context context) {
/* 218 */         super(context);
/*     */       }
/*     */       
/*     */       protected boolean compute() {
/* 223 */         return (this.context.surfaceDepth <= 0);
/*     */       }
/*     */     }
/*     */     
/*     */     private final class AbovePreliminarySurfaceCondition implements SurfaceRules.Condition {
/*     */       public boolean test() {
/* 230 */         return (SurfaceRules.Context.this.blockY >= SurfaceRules.Context.this.getMinSurfaceLevel());
/*     */       }
/*     */     }
/*     */     
/*     */     private static class TemperatureHelperCondition extends SurfaceRules.LazyYCondition {
/*     */       private TemperatureHelperCondition(SurfaceRules.Context context) {
/* 236 */         super(context);
/*     */       }
/*     */       
/*     */       protected boolean compute() {
/* 241 */         return ((Biome)this.context.getBiome().value()).coldEnoughToSnow((BlockPos)this.context.pos.set(this.context.blockX, this.context.blockY, this.context.blockZ), this.context.getSeaLevel());
/*     */       }
/*     */     }
/*     */     
/*     */     private static class SteepMaterialCondition extends SurfaceRules.LazyXZCondition {
/*     */       private SteepMaterialCondition(SurfaceRules.Context context) {
/* 247 */         super(context);
/*     */       }
/*     */       
/*     */       protected boolean compute() {
/* 252 */         int chunkBlockX = this.context.blockX & 0xF;
/* 253 */         int chunkBlockZ = this.context.blockZ & 0xF;
/* 255 */         int zNorth = Math.max(chunkBlockZ - 1, 0);
/* 256 */         int zSouth = Math.min(chunkBlockZ + 1, 15);
/* 258 */         ChunkAccess chunk = this.context.chunk;
/* 259 */         int heightNorth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zNorth);
/* 260 */         int heightSouth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zSouth);
/* 262 */         if (heightSouth >= heightNorth + 4)
/* 263 */           return true; 
/* 266 */         int xWest = Math.max(chunkBlockX - 1, 0);
/* 267 */         int xEast = Math.min(chunkBlockX + 1, 15);
/* 268 */         int heightWest = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xWest, chunkBlockZ);
/* 269 */         int heightEast = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xEast, chunkBlockZ);
/* 271 */         return (heightWest >= heightEast + 4);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements DoubleSupplier {
/*     */     private long lastUpdateXZ = SurfaceRules.Context.this.lastUpdateXZ - 1L;
/*     */     
/*     */     private double lastNoise;
/*     */     
/*     */     public double getAsDouble() {
/*     */       if (this.lastUpdateXZ != SurfaceRules.Context.this.lastUpdateXZ) {
/*     */         this.lastNoise = noise.getValue(SurfaceRules.Context.this.blockX, 0.0D, SurfaceRules.Context.this.blockZ);
/*     */         this.lastUpdateXZ = SurfaceRules.Context.this.lastUpdateXZ;
/*     */       } 
/*     */       return this.lastNoise;
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements DoubleSupplier {
/*     */     private long lastUpdateY = SurfaceRules.Context.this.lastUpdateY - 1L;
/*     */     
/*     */     private double lastNoise;
/*     */     
/*     */     public double getAsDouble() {
/*     */       if (this.lastUpdateY != SurfaceRules.Context.this.lastUpdateY) {
/*     */         this.lastNoise = noise.getValue(SurfaceRules.Context.this.blockX, SurfaceRules.Context.this.blockY, SurfaceRules.Context.this.blockZ);
/*     */         this.lastUpdateY = SurfaceRules.Context.this.lastUpdateY;
/*     */       } 
/*     */       return this.lastNoise;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class HoleCondition extends LazyXZCondition {
/*     */     private HoleCondition(SurfaceRules.Context context) {
/*     */       super(context);
/*     */     }
/*     */     
/*     */     protected boolean compute() {
/*     */       return (this.context.surfaceDepth <= 0);
/*     */     }
/*     */   }
/*     */   
/*     */   private final class AbovePreliminarySurfaceCondition implements Condition {
/*     */     public boolean test() {
/*     */       return (this.this$0.blockY >= this.this$0.getMinSurfaceLevel());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TemperatureHelperCondition extends LazyYCondition {
/*     */     private TemperatureHelperCondition(SurfaceRules.Context context) {
/*     */       super(context);
/*     */     }
/*     */     
/*     */     protected boolean compute() {
/*     */       return ((Biome)this.context.getBiome().value()).coldEnoughToSnow((BlockPos)this.context.pos.set(this.context.blockX, this.context.blockY, this.context.blockZ), this.context.getSeaLevel());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SteepMaterialCondition extends LazyXZCondition {
/*     */     private SteepMaterialCondition(SurfaceRules.Context context) {
/*     */       super(context);
/*     */     }
/*     */     
/*     */     protected boolean compute() {
/*     */       int chunkBlockX = this.context.blockX & 0xF;
/*     */       int chunkBlockZ = this.context.blockZ & 0xF;
/*     */       int zNorth = Math.max(chunkBlockZ - 1, 0);
/*     */       int zSouth = Math.min(chunkBlockZ + 1, 15);
/*     */       ChunkAccess chunk = this.context.chunk;
/*     */       int heightNorth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zNorth);
/*     */       int heightSouth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zSouth);
/*     */       if (heightSouth >= heightNorth + 4)
/*     */         return true; 
/*     */       int xWest = Math.max(chunkBlockX - 1, 0);
/*     */       int xEast = Math.min(chunkBlockX + 1, 15);
/*     */       int heightWest = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xWest, chunkBlockZ);
/*     */       int heightEast = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xEast, chunkBlockZ);
/* 271 */       return (heightWest >= heightEast + 4);
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class LazyCondition implements Condition {
/*     */     protected final SurfaceRules.Context context;
/*     */     
/*     */     private long lastUpdate;
/*     */     
/*     */     private Boolean result;
/*     */     
/*     */     protected LazyCondition(SurfaceRules.Context context) {
/* 288 */       this.context = context;
/* 289 */       this.lastUpdate = getContextLastUpdate() - 1L;
/*     */     }
/*     */     
/*     */     public boolean test() {
/* 294 */       long lastContextUpdate = getContextLastUpdate();
/* 296 */       if (lastContextUpdate == this.lastUpdate) {
/* 297 */         if (this.result == null)
/* 298 */           throw new IllegalStateException("Update triggered but the result is null"); 
/* 300 */         return this.result.booleanValue();
/*     */       } 
/* 302 */       this.lastUpdate = lastContextUpdate;
/* 304 */       this.result = Boolean.valueOf(compute());
/* 306 */       return this.result.booleanValue();
/*     */     }
/*     */     
/*     */     protected abstract long getContextLastUpdate();
/*     */     
/*     */     protected abstract boolean compute();
/*     */   }
/*     */   
/*     */   private static abstract class LazyXZCondition extends LazyCondition {
/*     */     protected LazyXZCondition(SurfaceRules.Context context) {
/* 316 */       super(context);
/*     */     }
/*     */     
/*     */     protected long getContextLastUpdate() {
/* 321 */       return this.context.lastUpdateXZ;
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class LazyYCondition extends LazyCondition {
/*     */     protected LazyYCondition(SurfaceRules.Context context) {
/* 327 */       super(context);
/*     */     }
/*     */     
/*     */     protected long getContextLastUpdate() {
/* 332 */       return this.context.lastUpdateY;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class NotCondition extends Record implements Condition {
/*     */     private final SurfaceRules.Condition target;
/*     */     
/*     */     private NotCondition(SurfaceRules.Condition target) {
/* 336 */       this.target = target;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #336	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #336	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #336	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition target() {
/* 336 */       return this.target;
/*     */     }
/*     */     
/*     */     public boolean test() {
/* 339 */       return !this.target.test();
/*     */     }
/*     */   }
/*     */   
/* 343 */   public static final ConditionSource ON_FLOOR = stoneDepthCheck(0, false, CaveSurface.FLOOR);
/*     */   
/* 344 */   public static final ConditionSource UNDER_FLOOR = stoneDepthCheck(0, true, CaveSurface.FLOOR);
/*     */   
/* 345 */   public static final ConditionSource DEEP_UNDER_FLOOR = stoneDepthCheck(0, true, 6, CaveSurface.FLOOR);
/*     */   
/* 346 */   public static final ConditionSource VERY_DEEP_UNDER_FLOOR = stoneDepthCheck(0, true, 30, CaveSurface.FLOOR);
/*     */   
/* 348 */   public static final ConditionSource ON_CEILING = stoneDepthCheck(0, false, CaveSurface.CEILING);
/*     */   
/* 349 */   public static final ConditionSource UNDER_CEILING = stoneDepthCheck(0, true, CaveSurface.CEILING);
/*     */   
/*     */   public static ConditionSource stoneDepthCheck(int offset, boolean addSurfaceDepth1, CaveSurface surfaceType) {
/* 352 */     return new StoneDepthCheck(offset, addSurfaceDepth1, 0, surfaceType);
/*     */   }
/*     */   
/*     */   public static ConditionSource stoneDepthCheck(int offset, boolean addSurfaceDepth1, int secondaryDepthRange, CaveSurface surfaceType) {
/* 356 */     return new StoneDepthCheck(offset, addSurfaceDepth1, secondaryDepthRange, surfaceType);
/*     */   }
/*     */   
/*     */   public static ConditionSource not(ConditionSource target) {
/* 360 */     return new NotConditionSource(target);
/*     */   }
/*     */   
/*     */   public static ConditionSource yBlockCheck(VerticalAnchor anchor, int surfaceDepthMultiplier) {
/* 364 */     return new YConditionSource(anchor, surfaceDepthMultiplier, false);
/*     */   }
/*     */   
/*     */   public static ConditionSource yStartCheck(VerticalAnchor anchor, int surfaceDepthMultiplier) {
/* 369 */     return new YConditionSource(anchor, surfaceDepthMultiplier, true);
/*     */   }
/*     */   
/*     */   public static ConditionSource waterBlockCheck(int offset, int surfaceDepthMultiplier) {
/* 373 */     return new WaterConditionSource(offset, surfaceDepthMultiplier, false);
/*     */   }
/*     */   
/*     */   public static ConditionSource waterStartCheck(int offset, int surfaceDepthMultiplier) {
/* 377 */     return new WaterConditionSource(offset, surfaceDepthMultiplier, true);
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public static ConditionSource isBiome(HolderGetter<Biome> biomes, ResourceKey<Biome>... target) {
/* 382 */     Objects.requireNonNull(biomes);
/* 382 */     return new BiomeConditionSource((HolderSet<Biome>)HolderSet.direct(biomes::getOrThrow, (Object[])target));
/*     */   }
/*     */   
/*     */   public static ConditionSource noiseCondition2d(ResourceKey<NormalNoise.NoiseParameters> noise, double minRange) {
/* 386 */     return noiseCondition2d(noise, minRange, Double.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public static ConditionSource noiseCondition2d(ResourceKey<NormalNoise.NoiseParameters> noise, double minRange, double maxRange) {
/* 390 */     return new NoiseThresholdConditionSource(noise, minRange, maxRange, false);
/*     */   }
/*     */   
/*     */   public static ConditionSource noiseCondition3d(ResourceKey<NormalNoise.NoiseParameters> noise, double minRange) {
/* 394 */     return noiseCondition3d(noise, minRange, Double.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public static ConditionSource noiseCondition3d(ResourceKey<NormalNoise.NoiseParameters> noise, double minRange, double maxRange) {
/* 398 */     return new NoiseThresholdConditionSource(noise, minRange, maxRange, true);
/*     */   }
/*     */   
/*     */   public static ConditionSource verticalGradient(String randomName, VerticalAnchor trueAtAndBelow, VerticalAnchor falseAtAndAbove) {
/* 402 */     return new VerticalGradientConditionSource(Identifier.parse(randomName), trueAtAndBelow, falseAtAndAbove);
/*     */   }
/*     */   
/*     */   public static ConditionSource steep() {
/* 406 */     return Steep.INSTANCE;
/*     */   }
/*     */   
/*     */   public static ConditionSource hole() {
/* 410 */     return Hole.INSTANCE;
/*     */   }
/*     */   
/*     */   public static ConditionSource abovePreliminarySurface() {
/* 414 */     return AbovePreliminarySurface.INSTANCE;
/*     */   }
/*     */   
/*     */   public static ConditionSource temperature() {
/* 418 */     return Temperature.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class StateRule extends Record implements SurfaceRule {
/*     */     private final BlockState state;
/*     */     
/*     */     private StateRule(BlockState state) {
/* 425 */       this.state = state;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #425	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #425	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #425	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockState state() {
/* 425 */       return this.state;
/*     */     }
/*     */     
/*     */     public BlockState tryApply(int blockX, int blockY, int blockZ) {
/* 428 */       return this.state;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TestRule extends Record implements SurfaceRule {
/*     */     private final SurfaceRules.Condition condition;
/*     */     
/*     */     private final SurfaceRules.SurfaceRule followup;
/*     */     
/*     */     private TestRule(SurfaceRules.Condition condition, SurfaceRules.SurfaceRule followup) {
/* 432 */       this.condition = condition;
/* 432 */       this.followup = followup;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #432	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #432	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #432	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition condition() {
/* 432 */       return this.condition;
/*     */     }
/*     */     
/*     */     public SurfaceRules.SurfaceRule followup() {
/* 432 */       return this.followup;
/*     */     }
/*     */     
/*     */     public BlockState tryApply(int blockX, int blockY, int blockZ) {
/* 435 */       if (!this.condition.test())
/* 436 */         return null; 
/* 438 */       return this.followup.tryApply(blockX, blockY, blockZ);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SequenceRule extends Record implements SurfaceRule {
/*     */     private final List<SurfaceRules.SurfaceRule> rules;
/*     */     
/*     */     private SequenceRule(List<SurfaceRules.SurfaceRule> rules) {
/* 442 */       this.rules = rules;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #442	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #442	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #442	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public List<SurfaceRules.SurfaceRule> rules() {
/* 442 */       return this.rules;
/*     */     }
/*     */     
/*     */     public BlockState tryApply(int blockX, int blockY, int blockZ) {
/* 445 */       for (SurfaceRules.SurfaceRule rule : this.rules) {
/* 446 */         BlockState state = rule.tryApply(blockX, blockY, blockZ);
/* 447 */         if (state != null)
/* 448 */           return state; 
/*     */       } 
/* 451 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public static RuleSource ifTrue(ConditionSource condition, RuleSource next) {
/* 456 */     return new TestRuleSource(condition, next);
/*     */   }
/*     */   
/*     */   public static RuleSource sequence(RuleSource... rules) {
/* 460 */     if (rules.length == 0)
/* 461 */       throw new IllegalArgumentException("Need at least 1 rule for a sequence"); 
/* 463 */     return new SequenceRuleSource(Arrays.asList(rules));
/*     */   }
/*     */   
/*     */   public static RuleSource state(BlockState state) {
/* 467 */     return new BlockRuleSource(state);
/*     */   }
/*     */   
/*     */   public static RuleSource bandlands() {
/* 471 */     return Bandlands.INSTANCE;
/*     */   }
/*     */   
/*     */   private static <A> MapCodec<? extends A> register(Registry<MapCodec<? extends A>> registry, String name, MapCodec<? extends A> codec) {
/* 475 */     return (MapCodec<? extends A>)Registry.register(registry, name, codec);
/*     */   }
/*     */   
/*     */   public static interface ConditionSource extends Function<Context, Condition> {
/* 479 */     public static final Codec<ConditionSource> CODEC = BuiltInRegistries.MATERIAL_CONDITION.byNameCodec().dispatch(ConditionSource::codec, Function.identity());
/*     */     
/*     */     static MapCodec<? extends ConditionSource> bootstrap(Registry<MapCodec<? extends ConditionSource>> registry) {
/* 482 */       SurfaceRules.register(registry, "biome", (MapCodec)SurfaceRules.BiomeConditionSource.CODEC);
/* 483 */       SurfaceRules.register(registry, "noise_threshold", (MapCodec)SurfaceRules.NoiseThresholdConditionSource.CODEC);
/* 484 */       SurfaceRules.register(registry, "vertical_gradient", (MapCodec)SurfaceRules.VerticalGradientConditionSource.CODEC);
/* 485 */       SurfaceRules.register(registry, "y_above", (MapCodec)SurfaceRules.YConditionSource.CODEC);
/* 486 */       SurfaceRules.register(registry, "water", (MapCodec)SurfaceRules.WaterConditionSource.CODEC);
/* 487 */       SurfaceRules.register(registry, "temperature", (MapCodec)SurfaceRules.Temperature.CODEC);
/* 488 */       SurfaceRules.register(registry, "steep", (MapCodec)SurfaceRules.Steep.CODEC);
/* 489 */       SurfaceRules.register(registry, "not", (MapCodec)SurfaceRules.NotConditionSource.CODEC);
/* 490 */       SurfaceRules.register(registry, "hole", (MapCodec)SurfaceRules.Hole.CODEC);
/* 491 */       SurfaceRules.register(registry, "above_preliminary_surface", (MapCodec)SurfaceRules.AbovePreliminarySurface.CODEC);
/* 492 */       return SurfaceRules.register(registry, "stone_depth", (MapCodec)SurfaceRules.StoneDepthCheck.CODEC);
/*     */     }
/*     */     
/*     */     MapCodec<? extends ConditionSource> codec();
/*     */   }
/*     */   
/*     */   public static interface RuleSource extends Function<Context, SurfaceRule> {
/* 500 */     public static final Codec<RuleSource> CODEC = BuiltInRegistries.MATERIAL_RULE.byNameCodec().dispatch(RuleSource::codec, Function.identity());
/*     */     
/*     */     static MapCodec<? extends RuleSource> bootstrap(Registry<MapCodec<? extends RuleSource>> registry) {
/* 503 */       SurfaceRules.register(registry, "bandlands", (MapCodec)SurfaceRules.Bandlands.CODEC);
/* 504 */       SurfaceRules.register(registry, "block", (MapCodec)SurfaceRules.BlockRuleSource.CODEC);
/* 505 */       SurfaceRules.register(registry, "sequence", (MapCodec)SurfaceRules.SequenceRuleSource.CODEC);
/* 506 */       return SurfaceRules.register(registry, "condition", (MapCodec)SurfaceRules.TestRuleSource.CODEC);
/*     */     }
/*     */     
/*     */     MapCodec<? extends RuleSource> codec();
/*     */   }
/*     */   
/*     */   private static final class NotConditionSource extends Record implements ConditionSource {
/*     */     private final SurfaceRules.ConditionSource target;
/*     */     
/*     */     private NotConditionSource(SurfaceRules.ConditionSource target) {
/* 513 */       this.target = target;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #513	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #513	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #513	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public SurfaceRules.ConditionSource target() {
/* 513 */       return this.target;
/*     */     }
/*     */     
/* 514 */     private static final MapCodec<NotConditionSource> CODEC = SurfaceRules.ConditionSource.CODEC.xmap(NotConditionSource::new, NotConditionSource::target).fieldOf("invert");
/*     */     
/*     */     public MapCodec<NotConditionSource> codec() {
/* 518 */       return CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(SurfaceRules.Context context) {
/* 523 */       return new SurfaceRules.NotCondition(this.target.apply(context));
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class StoneDepthCheck extends Record implements ConditionSource {
/*     */     private final int offset;
/*     */     
/*     */     private final boolean addSurfaceDepth;
/*     */     
/*     */     private final int secondaryDepthRange;
/*     */     
/*     */     private final CaveSurface surfaceType;
/*     */     
/*     */     private static final MapCodec<StoneDepthCheck> CODEC;
/*     */     
/*     */     private StoneDepthCheck(int offset, boolean addSurfaceDepth, int secondaryDepthRange, CaveSurface surfaceType) {
/* 527 */       this.offset = offset;
/* 527 */       this.addSurfaceDepth = addSurfaceDepth;
/* 527 */       this.secondaryDepthRange = secondaryDepthRange;
/* 527 */       this.surfaceType = surfaceType;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #527	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #527	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #527	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int offset() {
/* 527 */       return this.offset;
/*     */     }
/*     */     
/*     */     public boolean addSurfaceDepth() {
/* 527 */       return this.addSurfaceDepth;
/*     */     }
/*     */     
/*     */     public int secondaryDepthRange() {
/* 527 */       return this.secondaryDepthRange;
/*     */     }
/*     */     
/*     */     public CaveSurface surfaceType() {
/* 527 */       return this.surfaceType;
/*     */     }
/*     */     
/*     */     static {
/* 528 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("offset").forGetter(StoneDepthCheck::offset), (App)Codec.BOOL.fieldOf("add_surface_depth").forGetter(StoneDepthCheck::addSurfaceDepth), (App)Codec.INT.fieldOf("secondary_depth_range").forGetter(StoneDepthCheck::secondaryDepthRange), (App)CaveSurface.CODEC.fieldOf("surface_type").forGetter(StoneDepthCheck::surfaceType)).apply((Applicative)i, StoneDepthCheck::new));
/*     */     }
/*     */     
/*     */     public MapCodec<StoneDepthCheck> codec() {
/* 538 */       return CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/* 543 */       final boolean ceiling = (this.surfaceType == CaveSurface.CEILING);
/*     */       class StoneDepthCondition extends SurfaceRules.LazyYCondition {
/*     */         private StoneDepthCondition() {
/* 547 */           super(param2Context);
/*     */         }
/*     */         
/*     */         protected boolean compute() {
/* 552 */           int stoneDepth = ceiling ? this.context.stoneDepthBelow : this.context.stoneDepthAbove;
/* 553 */           int surfaceDepth = SurfaceRules.StoneDepthCheck.this.addSurfaceDepth ? this.context.surfaceDepth : 0;
/* 554 */           int secondarySurfaceDepth = (SurfaceRules.StoneDepthCheck.this.secondaryDepthRange == 0) ? 0 : (int)Mth.map(this.context.getSurfaceSecondary(), -1.0D, 1.0D, 0.0D, SurfaceRules.StoneDepthCheck.this.secondaryDepthRange);
/* 556 */           return (stoneDepth <= 1 + SurfaceRules.StoneDepthCheck.this.offset + surfaceDepth + secondarySurfaceDepth);
/*     */         }
/*     */       };
/* 560 */       return new StoneDepthCondition();
/*     */     }
/*     */   }
/*     */   
/*     */   class StoneDepthCondition extends LazyYCondition {
/*     */     private StoneDepthCondition() {
/*     */       super(param1Context);
/*     */     }
/*     */     
/*     */     protected boolean compute() {
/*     */       int stoneDepth = ceiling ? this.context.stoneDepthBelow : this.context.stoneDepthAbove;
/*     */       int surfaceDepth = SurfaceRules.StoneDepthCheck.this.addSurfaceDepth ? this.context.surfaceDepth : 0;
/*     */       int secondarySurfaceDepth = (SurfaceRules.StoneDepthCheck.this.secondaryDepthRange == 0) ? 0 : (int)Mth.map(this.context.getSurfaceSecondary(), -1.0D, 1.0D, 0.0D, SurfaceRules.StoneDepthCheck.this.secondaryDepthRange);
/*     */       return (stoneDepth <= 1 + SurfaceRules.StoneDepthCheck.this.offset + surfaceDepth + secondarySurfaceDepth);
/*     */     }
/*     */   }
/*     */   
/*     */   private enum AbovePreliminarySurface implements ConditionSource {
/* 565 */     INSTANCE;
/*     */     
/* 566 */     private static final MapCodec<AbovePreliminarySurface> CODEC = MapCodec.unit(INSTANCE);
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     public MapCodec<AbovePreliminarySurface> codec() {
/* 570 */       return CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(SurfaceRules.Context context) {
/* 575 */       return context.abovePreliminarySurface;
/*     */     }
/*     */   }
/*     */   
/*     */   private enum Hole implements ConditionSource {
/* 580 */     INSTANCE;
/*     */     
/* 581 */     private static final MapCodec<Hole> CODEC = MapCodec.unit(INSTANCE);
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     public MapCodec<Hole> codec() {
/* 585 */       return CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(SurfaceRules.Context context) {
/* 590 */       return context.hole;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class YConditionSource extends Record implements ConditionSource {
/*     */     private final VerticalAnchor anchor;
/*     */     
/*     */     private final int surfaceDepthMultiplier;
/*     */     
/*     */     private final boolean addStoneDepth;
/*     */     
/*     */     private static final MapCodec<YConditionSource> CODEC;
/*     */     
/*     */     private YConditionSource(VerticalAnchor anchor, int surfaceDepthMultiplier, boolean addStoneDepth) {
/* 594 */       this.anchor = anchor;
/* 594 */       this.surfaceDepthMultiplier = surfaceDepthMultiplier;
/* 594 */       this.addStoneDepth = addStoneDepth;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #594	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #594	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #594	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public VerticalAnchor anchor() {
/* 594 */       return this.anchor;
/*     */     }
/*     */     
/*     */     public int surfaceDepthMultiplier() {
/* 594 */       return this.surfaceDepthMultiplier;
/*     */     }
/*     */     
/*     */     public boolean addStoneDepth() {
/* 594 */       return this.addStoneDepth;
/*     */     }
/*     */     
/*     */     static {
/* 595 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)VerticalAnchor.CODEC.fieldOf("anchor").forGetter(YConditionSource::anchor), (App)Codec.intRange(-20, 20).fieldOf("surface_depth_multiplier").forGetter(YConditionSource::surfaceDepthMultiplier), (App)Codec.BOOL.fieldOf("add_stone_depth").forGetter(YConditionSource::addStoneDepth)).apply((Applicative)i, YConditionSource::new));
/*     */     }
/*     */     
/*     */     public MapCodec<YConditionSource> codec() {
/* 603 */       return CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/*     */       class YCondition extends SurfaceRules.LazyYCondition {
/*     */         private YCondition() {
/* 610 */           super(param2Context);
/*     */         }
/*     */         
/*     */         protected boolean compute() {
/* 615 */           return (this.context.blockY + (SurfaceRules.YConditionSource.this.addStoneDepth ? this.context.stoneDepthAbove : 0) >= SurfaceRules.YConditionSource.this.anchor.resolveY(this.context.context) + this.context.surfaceDepth * SurfaceRules.YConditionSource.this.surfaceDepthMultiplier);
/*     */         }
/*     */       };
/* 619 */       return new YCondition();
/*     */     }
/*     */   }
/*     */   
/*     */   class YCondition extends LazyYCondition {
/*     */     private YCondition() {
/*     */       super(param1Context);
/*     */     }
/*     */     
/*     */     protected boolean compute() {
/*     */       return (this.context.blockY + (SurfaceRules.YConditionSource.this.addStoneDepth ? this.context.stoneDepthAbove : 0) >= SurfaceRules.YConditionSource.this.anchor.resolveY(this.context.context) + this.context.surfaceDepth * SurfaceRules.YConditionSource.this.surfaceDepthMultiplier);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class WaterConditionSource extends Record implements ConditionSource {
/*     */     private final int offset;
/*     */     
/*     */     private final int surfaceDepthMultiplier;
/*     */     
/*     */     private final boolean addStoneDepth;
/*     */     
/*     */     private static final MapCodec<WaterConditionSource> CODEC;
/*     */     
/*     */     private WaterConditionSource(int offset, int surfaceDepthMultiplier, boolean addStoneDepth) {
/* 623 */       this.offset = offset;
/* 623 */       this.surfaceDepthMultiplier = surfaceDepthMultiplier;
/* 623 */       this.addStoneDepth = addStoneDepth;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #623	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #623	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #623	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int offset() {
/* 623 */       return this.offset;
/*     */     }
/*     */     
/*     */     public int surfaceDepthMultiplier() {
/* 623 */       return this.surfaceDepthMultiplier;
/*     */     }
/*     */     
/*     */     public boolean addStoneDepth() {
/* 623 */       return this.addStoneDepth;
/*     */     }
/*     */     
/*     */     static {
/* 624 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("offset").forGetter(WaterConditionSource::offset), (App)Codec.intRange(-20, 20).fieldOf("surface_depth_multiplier").forGetter(WaterConditionSource::surfaceDepthMultiplier), (App)Codec.BOOL.fieldOf("add_stone_depth").forGetter(WaterConditionSource::addStoneDepth)).apply((Applicative)i, WaterConditionSource::new));
/*     */     }
/*     */     
/*     */     public MapCodec<WaterConditionSource> codec() {
/* 632 */       return CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/*     */       class WaterCondition extends SurfaceRules.LazyYCondition {
/*     */         private WaterCondition() {
/* 639 */           super(param2Context);
/*     */         }
/*     */         
/*     */         protected boolean compute() {
/* 644 */           return (this.context.waterHeight == Integer.MIN_VALUE || this.context.blockY + (SurfaceRules.WaterConditionSource.this.addStoneDepth ? this.context.stoneDepthAbove : 0) >= this.context.waterHeight + SurfaceRules.WaterConditionSource.this.offset + this.context.surfaceDepth * SurfaceRules.WaterConditionSource.this.surfaceDepthMultiplier);
/*     */         }
/*     */       };
/* 648 */       return new WaterCondition();
/*     */     }
/*     */   }
/*     */   
/*     */   class WaterCondition extends LazyYCondition {
/*     */     private WaterCondition() {
/*     */       super(param1Context);
/*     */     }
/*     */     
/*     */     protected boolean compute() {
/*     */       return (this.context.waterHeight == Integer.MIN_VALUE || this.context.blockY + (SurfaceRules.WaterConditionSource.this.addStoneDepth ? this.context.stoneDepthAbove : 0) >= this.context.waterHeight + SurfaceRules.WaterConditionSource.this.offset + this.context.surfaceDepth * SurfaceRules.WaterConditionSource.this.surfaceDepthMultiplier);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class BiomeConditionSource extends Record implements ConditionSource {
/*     */     private final HolderSet<Biome> biomes;
/*     */     
/*     */     private static final MapCodec<BiomeConditionSource> CODEC;
/*     */     
/*     */     private BiomeConditionSource(HolderSet<Biome> biomes) {
/* 652 */       this.biomes = biomes;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$BiomeConditionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #652	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$BiomeConditionSource;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$BiomeConditionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #652	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$BiomeConditionSource;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public HolderSet<Biome> biomes() {
/* 652 */       return this.biomes;
/*     */     }
/*     */     
/*     */     static {
/* 653 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biome_is").forGetter(BiomeConditionSource::biomes)).apply((Applicative)i, BiomeConditionSource::new));
/*     */     }
/*     */     
/*     */     public MapCodec<BiomeConditionSource> codec() {
/* 659 */       return CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/* 666 */       if (ruleContext.possibleBiomes != null) {
/* 667 */         if (canNeverMatch(ruleContext.possibleBiomes))
/* 668 */           return () -> false; 
/* 669 */         if (willAlwaysMatch(ruleContext.possibleBiomes))
/* 670 */           return () -> true; 
/*     */       } 
/*     */       class BiomeCondition extends SurfaceRules.LazyYCondition {
/*     */         private BiomeCondition() {
/* 676 */           super(param2Context);
/*     */         }
/*     */         
/*     */         protected boolean compute() {
/* 681 */           return SurfaceRules.BiomeConditionSource.this.biomes.contains(this.context.getBiome());
/*     */         }
/*     */       };
/* 685 */       return new BiomeCondition();
/*     */     }
/*     */     
/*     */     private boolean canNeverMatch(Set<Holder<Biome>> possibleBiomes) {
/* 689 */       for (Holder<Biome> biome : this.biomes) {
/* 690 */         if (possibleBiomes.contains(biome))
/* 691 */           return false; 
/*     */       } 
/* 694 */       return true;
/*     */     }
/*     */     
/*     */     private boolean willAlwaysMatch(Set<Holder<Biome>> possibleBiomes) {
/* 698 */       for (Holder<Biome> possibleBiome : possibleBiomes) {
/* 699 */         if (!this.biomes.contains(possibleBiome))
/* 700 */           return false; 
/*     */       } 
/* 703 */       return true;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 708 */       return "BiomeConditionSource[biomes=" + String.valueOf(this.biomes) + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   class BiomeCondition extends LazyYCondition {
/*     */     private BiomeCondition() {
/*     */       super(param1Context);
/*     */     }
/*     */     
/*     */     protected boolean compute() {
/*     */       return SurfaceRules.BiomeConditionSource.this.biomes.contains(this.context.getBiome());
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class NoiseThresholdConditionSource extends Record implements ConditionSource {
/*     */     private final ResourceKey<NormalNoise.NoiseParameters> noise;
/*     */     
/*     */     private final double minThreshold;
/*     */     
/*     */     private final double maxThreshold;
/*     */     
/*     */     private final boolean is3d;
/*     */     
/*     */     private static final MapCodec<NoiseThresholdConditionSource> CODEC;
/*     */     
/*     */     private NoiseThresholdConditionSource(ResourceKey<NormalNoise.NoiseParameters> noise, double minThreshold, double maxThreshold, boolean is3d) {
/* 712 */       this.noise = noise;
/* 712 */       this.minThreshold = minThreshold;
/* 712 */       this.maxThreshold = maxThreshold;
/* 712 */       this.is3d = is3d;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #712	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #712	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #712	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ResourceKey<NormalNoise.NoiseParameters> noise() {
/* 712 */       return this.noise;
/*     */     }
/*     */     
/*     */     public double minThreshold() {
/* 712 */       return this.minThreshold;
/*     */     }
/*     */     
/*     */     public double maxThreshold() {
/* 712 */       return this.maxThreshold;
/*     */     }
/*     */     
/*     */     public boolean is3d() {
/* 712 */       return this.is3d;
/*     */     }
/*     */     
/*     */     static {
/* 713 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.NOISE).fieldOf("noise").forGetter(NoiseThresholdConditionSource::noise), (App)Codec.DOUBLE.fieldOf("min_threshold").forGetter(NoiseThresholdConditionSource::minThreshold), (App)Codec.DOUBLE.fieldOf("max_threshold").forGetter(NoiseThresholdConditionSource::maxThreshold), (App)Codec.BOOL.optionalFieldOf("is_3d", Boolean.valueOf(false)).forGetter(NoiseThresholdConditionSource::is3d)).apply((Applicative)i, NoiseThresholdConditionSource::new));
/*     */     }
/*     */     
/*     */     public MapCodec<NoiseThresholdConditionSource> codec() {
/* 723 */       return CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(SurfaceRules.Context ruleContext) {
/* 728 */       final DoubleSupplier noise = ruleContext.getNoiseSampler(this.noise, this.is3d);
/*     */       class NoiseThresholdCondition implements SurfaceRules.Condition {
/*     */         public boolean test() {
/* 732 */           double value = noise.getAsDouble();
/* 733 */           return (value >= SurfaceRules.NoiseThresholdConditionSource.this.minThreshold && value <= SurfaceRules.NoiseThresholdConditionSource.this.maxThreshold);
/*     */         }
/*     */       };
/* 737 */       return new NoiseThresholdCondition();
/*     */     }
/*     */   }
/*     */   
/*     */   class NoiseThresholdCondition implements Condition {
/*     */     public boolean test() {
/*     */       double value = noise.getAsDouble();
/*     */       return (value >= SurfaceRules.NoiseThresholdConditionSource.this.minThreshold && value <= SurfaceRules.NoiseThresholdConditionSource.this.maxThreshold);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class VerticalGradientConditionSource extends Record implements ConditionSource {
/*     */     private final Identifier randomName;
/*     */     
/*     */     private final VerticalAnchor trueAtAndBelow;
/*     */     
/*     */     private final VerticalAnchor falseAtAndAbove;
/*     */     
/*     */     private static final MapCodec<VerticalGradientConditionSource> CODEC;
/*     */     
/*     */     private VerticalGradientConditionSource(Identifier randomName, VerticalAnchor trueAtAndBelow, VerticalAnchor falseAtAndAbove) {
/* 741 */       this.randomName = randomName;
/* 741 */       this.trueAtAndBelow = trueAtAndBelow;
/* 741 */       this.falseAtAndAbove = falseAtAndAbove;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #741	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #741	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #741	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Identifier randomName() {
/* 741 */       return this.randomName;
/*     */     }
/*     */     
/*     */     public VerticalAnchor trueAtAndBelow() {
/* 741 */       return this.trueAtAndBelow;
/*     */     }
/*     */     
/*     */     public VerticalAnchor falseAtAndAbove() {
/* 741 */       return this.falseAtAndAbove;
/*     */     }
/*     */     
/*     */     static {
/* 742 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("random_name").forGetter(VerticalGradientConditionSource::randomName), (App)VerticalAnchor.CODEC.fieldOf("true_at_and_below").forGetter(VerticalGradientConditionSource::trueAtAndBelow), (App)VerticalAnchor.CODEC.fieldOf("false_at_and_above").forGetter(VerticalGradientConditionSource::falseAtAndAbove)).apply((Applicative)i, VerticalGradientConditionSource::new));
/*     */     }
/*     */     
/*     */     public MapCodec<VerticalGradientConditionSource> codec() {
/* 750 */       return CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/* 755 */       final int trueAtAndBelow = trueAtAndBelow().resolveY(ruleContext.context);
/* 756 */       final int falseAtAndAbove = falseAtAndAbove().resolveY(ruleContext.context);
/* 757 */       final PositionalRandomFactory randomFactory = ruleContext.randomState.getOrCreateRandomFactory(randomName());
/*     */       class VerticalGradientCondition extends SurfaceRules.LazyYCondition {
/*     */         private VerticalGradientCondition(SurfaceRules.VerticalGradientConditionSource this$0) {
/* 761 */           super(param2Context);
/*     */         }
/*     */         
/*     */         protected boolean compute() {
/* 766 */           int blockY = this.context.blockY;
/* 767 */           if (blockY <= trueAtAndBelow)
/* 768 */             return true; 
/* 770 */           if (blockY >= falseAtAndAbove)
/* 771 */             return false; 
/* 773 */           double probability = Mth.map(blockY, trueAtAndBelow, falseAtAndAbove, 1.0D, 0.0D);
/* 774 */           RandomSource random = randomFactory.at(this.context.blockX, blockY, this.context.blockZ);
/* 775 */           return (random.nextFloat() < probability);
/*     */         }
/*     */       };
/* 778 */       return new VerticalGradientCondition(this);
/*     */     }
/*     */   }
/*     */   
/*     */   class VerticalGradientCondition extends LazyYCondition {
/*     */     private VerticalGradientCondition(SurfaceRules.VerticalGradientConditionSource this$0) {
/*     */       super(param1Context);
/*     */     }
/*     */     
/*     */     protected boolean compute() {
/*     */       int blockY = this.context.blockY;
/*     */       if (blockY <= trueAtAndBelow)
/*     */         return true; 
/*     */       if (blockY >= falseAtAndAbove)
/*     */         return false; 
/*     */       double probability = Mth.map(blockY, trueAtAndBelow, falseAtAndAbove, 1.0D, 0.0D);
/*     */       RandomSource random = randomFactory.at(this.context.blockX, blockY, this.context.blockZ);
/*     */       return (random.nextFloat() < probability);
/*     */     }
/*     */   }
/*     */   
/*     */   private enum Temperature implements ConditionSource {
/* 783 */     INSTANCE;
/*     */     
/* 784 */     private static final MapCodec<Temperature> CODEC = MapCodec.unit(INSTANCE);
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     public MapCodec<Temperature> codec() {
/* 788 */       return CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(SurfaceRules.Context context) {
/* 793 */       return context.temperature;
/*     */     }
/*     */   }
/*     */   
/*     */   private enum Steep implements ConditionSource {
/* 798 */     INSTANCE;
/*     */     
/* 799 */     private static final MapCodec<Steep> CODEC = MapCodec.unit(INSTANCE);
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     public MapCodec<Steep> codec() {
/* 803 */       return CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(SurfaceRules.Context context) {
/* 808 */       return context.steep;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class BlockRuleSource extends Record implements RuleSource {
/*     */     private final BlockState resultState;
/*     */     
/*     */     private final SurfaceRules.StateRule rule;
/*     */     
/*     */     private BlockRuleSource(BlockState resultState, SurfaceRules.StateRule rule) {
/* 812 */       this.resultState = resultState;
/* 812 */       this.rule = rule;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #812	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #812	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #812	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockState resultState() {
/* 812 */       return this.resultState;
/*     */     }
/*     */     
/*     */     public SurfaceRules.StateRule rule() {
/* 812 */       return this.rule;
/*     */     }
/*     */     
/* 813 */     private static final MapCodec<BlockRuleSource> CODEC = BlockState.CODEC.xmap(BlockRuleSource::new, BlockRuleSource::resultState).fieldOf("result_state");
/*     */     
/*     */     private BlockRuleSource(BlockState state) {
/* 816 */       this(state, new SurfaceRules.StateRule(state));
/*     */     }
/*     */     
/*     */     public MapCodec<BlockRuleSource> codec() {
/* 821 */       return CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
/* 826 */       return this.rule;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TestRuleSource extends Record implements RuleSource {
/*     */     private final SurfaceRules.ConditionSource ifTrue;
/*     */     
/*     */     private final SurfaceRules.RuleSource thenRun;
/*     */     
/*     */     private static final MapCodec<TestRuleSource> CODEC;
/*     */     
/*     */     private TestRuleSource(SurfaceRules.ConditionSource ifTrue, SurfaceRules.RuleSource thenRun) {
/* 830 */       this.ifTrue = ifTrue;
/* 830 */       this.thenRun = thenRun;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #830	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #830	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #830	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public SurfaceRules.ConditionSource ifTrue() {
/* 830 */       return this.ifTrue;
/*     */     }
/*     */     
/*     */     public SurfaceRules.RuleSource thenRun() {
/* 830 */       return this.thenRun;
/*     */     }
/*     */     
/*     */     static {
/* 831 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SurfaceRules.ConditionSource.CODEC.fieldOf("if_true").forGetter(TestRuleSource::ifTrue), (App)SurfaceRules.RuleSource.CODEC.fieldOf("then_run").forGetter(TestRuleSource::thenRun)).apply((Applicative)i, TestRuleSource::new));
/*     */     }
/*     */     
/*     */     public MapCodec<TestRuleSource> codec() {
/* 838 */       return CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
/* 843 */       return new SurfaceRules.TestRule(this.ifTrue.apply(context), this.thenRun.apply(context));
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SequenceRuleSource extends Record implements RuleSource {
/*     */     private final List<SurfaceRules.RuleSource> sequence;
/*     */     
/*     */     private SequenceRuleSource(List<SurfaceRules.RuleSource> sequence) {
/* 847 */       this.sequence = sequence;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #847	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #847	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #847	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public List<SurfaceRules.RuleSource> sequence() {
/* 847 */       return this.sequence;
/*     */     }
/*     */     
/* 848 */     private static final MapCodec<SequenceRuleSource> CODEC = SurfaceRules.RuleSource.CODEC.listOf().xmap(SequenceRuleSource::new, SequenceRuleSource::sequence).fieldOf("sequence");
/*     */     
/*     */     public MapCodec<SequenceRuleSource> codec() {
/* 852 */       return CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
/* 857 */       if (this.sequence.size() == 1)
/* 858 */         return ((SurfaceRules.RuleSource)this.sequence.get(0)).apply(context); 
/* 861 */       ImmutableList.Builder<SurfaceRules.SurfaceRule> builder = ImmutableList.builder();
/* 862 */       for (SurfaceRules.RuleSource rule : this.sequence)
/* 863 */         builder.add(rule.apply(context)); 
/* 865 */       return new SurfaceRules.SequenceRule((List<SurfaceRules.SurfaceRule>)builder.build());
/*     */     }
/*     */   }
/*     */   
/*     */   private enum Bandlands implements RuleSource {
/* 870 */     INSTANCE;
/*     */     
/* 871 */     private static final MapCodec<Bandlands> CODEC = MapCodec.unit(INSTANCE);
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     public MapCodec<Bandlands> codec() {
/* 875 */       return CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
/* 880 */       Objects.requireNonNull(context.system);
/* 880 */       return context.system::getBand;
/*     */     }
/*     */   }
/*     */   
/*     */   protected static interface SurfaceRule {
/*     */     BlockState tryApply(int param1Int1, int param1Int2, int param1Int3);
/*     */   }
/*     */   
/*     */   private static interface Condition {
/*     */     boolean test();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */