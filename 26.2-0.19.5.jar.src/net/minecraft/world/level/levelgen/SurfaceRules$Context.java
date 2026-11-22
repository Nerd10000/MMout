/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.DoubleSupplier;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ public final class Context {
/*     */   private static final int HOW_FAR_BELOW_PRELIMINARY_SURFACE_LEVEL_TO_BUILD_SURFACE = 8;
/*     */   
/*     */   private static final int SURFACE_CELL_BITS = 4;
/*     */   
/*     */   private static final int SURFACE_CELL_SIZE = 16;
/*     */   
/*     */   private static final int SURFACE_CELL_MASK = 15;
/*     */   
/*     */   private final SurfaceSystem system;
/*     */   
/*  51 */   private final SurfaceRules.Condition temperature = new TemperatureHelperCondition(this);
/*     */   
/*  52 */   private final SurfaceRules.Condition steep = new SteepMaterialCondition(this);
/*     */   
/*  53 */   private final SurfaceRules.Condition hole = new HoleCondition(this);
/*     */   
/*  54 */   private final SurfaceRules.Condition abovePreliminarySurface = new AbovePreliminarySurfaceCondition();
/*     */   
/*     */   private final RandomState randomState;
/*     */   
/*     */   private final ChunkAccess chunk;
/*     */   
/*     */   private final NoiseChunk noiseChunk;
/*     */   
/*     */   private final Function<BlockPos, Holder<Biome>> biomeGetter;
/*     */   
/*     */   private final WorldGenerationContext context;
/*     */   
/*     */   private final Set<Holder<Biome>> possibleBiomes;
/*     */   
/*  63 */   private long lastPreliminarySurfaceCellOrigin = Long.MAX_VALUE;
/*     */   
/*  64 */   private final int[] preliminarySurfaceCache = new int[4];
/*     */   
/*  66 */   private final Map<ResourceKey<NormalNoise.NoiseParameters>, DoubleSupplier> noiseSamplers2d = new IdentityHashMap<>();
/*     */   
/*  67 */   private final Map<ResourceKey<NormalNoise.NoiseParameters>, DoubleSupplier> noiseSamplers3d = new IdentityHashMap<>();
/*     */   
/*  70 */   private long lastUpdateXZ = -9223372036854775807L;
/*     */   
/*     */   private int blockX;
/*     */   
/*     */   private int blockZ;
/*     */   
/*     */   private int surfaceDepth;
/*     */   
/*  76 */   private long lastSurfaceDepth2Update = this.lastUpdateXZ - 1L;
/*     */   
/*     */   private double surfaceSecondary;
/*     */   
/*  79 */   private long lastMinSurfaceLevelUpdate = this.lastUpdateXZ - 1L;
/*     */   
/*     */   private int minSurfaceLevel;
/*     */   
/*  83 */   private long lastUpdateY = -9223372036854775807L;
/*     */   
/*  84 */   private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/*     */   
/*     */   private Holder<Biome> biome;
/*     */   
/*     */   private int blockY;
/*     */   
/*     */   private int waterHeight;
/*     */   
/*     */   private int stoneDepthBelow;
/*     */   
/*     */   private int stoneDepthAbove;
/*     */   
/*     */   protected Context(SurfaceSystem system, RandomState randomState, ChunkAccess chunk, NoiseChunk noiseChunk, Function<BlockPos, Holder<Biome>> biomeGetter, WorldGenerationContext context, Set<Holder<Biome>> possibleBiomes) {
/*  92 */     this.system = system;
/*  93 */     this.randomState = randomState;
/*  94 */     this.chunk = chunk;
/*  95 */     this.noiseChunk = noiseChunk;
/*  96 */     this.biomeGetter = biomeGetter;
/*  97 */     this.context = context;
/*  98 */     this.possibleBiomes = possibleBiomes;
/*     */   }
/*     */   
/*     */   protected void updateXZ(int blockX, int blockZ) {
/* 102 */     this.lastUpdateXZ++;
/* 103 */     this.lastUpdateY++;
/* 104 */     this.blockX = blockX;
/* 105 */     this.blockZ = blockZ;
/* 106 */     this.surfaceDepth = this.system.getSurfaceDepth(blockX, blockZ);
/*     */   }
/*     */   
/*     */   protected void updateY(int stoneDepthAbove, int stoneDepthBelow, int waterHeight, int blockY) {
/* 110 */     this.lastUpdateY++;
/* 111 */     this.biome = null;
/* 112 */     this.blockY = blockY;
/* 113 */     this.waterHeight = waterHeight;
/* 114 */     this.stoneDepthBelow = stoneDepthBelow;
/* 115 */     this.stoneDepthAbove = stoneDepthAbove;
/*     */   }
/*     */   
/*     */   protected double getSurfaceSecondary() {
/* 119 */     if (this.lastSurfaceDepth2Update != this.lastUpdateXZ) {
/* 120 */       this.lastSurfaceDepth2Update = this.lastUpdateXZ;
/* 121 */       this.surfaceSecondary = this.system.getSurfaceSecondary(this.blockX, this.blockZ);
/*     */     } 
/* 123 */     return this.surfaceSecondary;
/*     */   }
/*     */   
/*     */   protected Holder<Biome> getBiome() {
/* 127 */     if (this.biome == null)
/* 128 */       this.biome = this.biomeGetter.apply(this.pos.set(this.blockX, this.blockY, this.blockZ)); 
/* 130 */     return this.biome;
/*     */   }
/*     */   
/*     */   public int getSeaLevel() {
/* 134 */     return this.system.getSeaLevel();
/*     */   }
/*     */   
/*     */   private static int blockCoordToSurfaceCell(int blockCoord) {
/* 138 */     return blockCoord >> 4;
/*     */   }
/*     */   
/*     */   private static int surfaceCellToBlockCoord(int cellCoord) {
/* 142 */     return cellCoord << 4;
/*     */   }
/*     */   
/*     */   protected int getMinSurfaceLevel() {
/* 147 */     if (this.lastMinSurfaceLevelUpdate != this.lastUpdateXZ) {
/* 148 */       this.lastMinSurfaceLevelUpdate = this.lastUpdateXZ;
/* 149 */       int cornerCellX = blockCoordToSurfaceCell(this.blockX);
/* 150 */       int cornerCellZ = blockCoordToSurfaceCell(this.blockZ);
/* 152 */       long preliminarySurfaceCellOrigin = ChunkPos.pack(cornerCellX, cornerCellZ);
/* 153 */       if (this.lastPreliminarySurfaceCellOrigin != preliminarySurfaceCellOrigin) {
/* 154 */         this.lastPreliminarySurfaceCellOrigin = preliminarySurfaceCellOrigin;
/* 156 */         this.preliminarySurfaceCache[0] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX), surfaceCellToBlockCoord(cornerCellZ));
/* 157 */         this.preliminarySurfaceCache[1] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX + 1), surfaceCellToBlockCoord(cornerCellZ));
/* 158 */         this.preliminarySurfaceCache[2] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX), surfaceCellToBlockCoord(cornerCellZ + 1));
/* 159 */         this.preliminarySurfaceCache[3] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX + 1), surfaceCellToBlockCoord(cornerCellZ + 1));
/*     */       } 
/* 161 */       int preliminarySurfaceLevel = Mth.floor(Mth.lerp2(((this.blockX & 0xF) / 16.0F), ((this.blockZ & 0xF) / 16.0F), this.preliminarySurfaceCache[0], this.preliminarySurfaceCache[1], this.preliminarySurfaceCache[2], this.preliminarySurfaceCache[3]));
/* 169 */       this.minSurfaceLevel = preliminarySurfaceLevel + this.surfaceDepth - 8;
/*     */     } 
/* 171 */     return this.minSurfaceLevel;
/*     */   }
/*     */   
/*     */   protected DoubleSupplier getNoiseSampler(ResourceKey<NormalNoise.NoiseParameters> noiseId, boolean is3d) {
/* 175 */     if (is3d)
/* 176 */       return this.noiseSamplers3d.computeIfAbsent(noiseId, this::createNoiseSampler3d); 
/* 178 */     return this.noiseSamplers2d.computeIfAbsent(noiseId, this::createNoiseSampler2d);
/*     */   }
/*     */   
/*     */   private DoubleSupplier createNoiseSampler2d(ResourceKey<NormalNoise.NoiseParameters> noiseId) {
/* 183 */     final NormalNoise noise = this.randomState.getOrCreateNoise(noiseId);
/* 184 */     return new DoubleSupplier() {
/* 185 */         private long lastUpdateXZ = SurfaceRules.Context.this.lastUpdateXZ - 1L;
/*     */         
/*     */         private double lastNoise;
/*     */         
/*     */         public double getAsDouble() {
/* 190 */           if (this.lastUpdateXZ != SurfaceRules.Context.this.lastUpdateXZ) {
/* 191 */             this.lastNoise = noise.getValue(SurfaceRules.Context.this.blockX, 0.0D, SurfaceRules.Context.this.blockZ);
/* 192 */             this.lastUpdateXZ = SurfaceRules.Context.this.lastUpdateXZ;
/*     */           } 
/* 194 */           return this.lastNoise;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private DoubleSupplier createNoiseSampler3d(ResourceKey<NormalNoise.NoiseParameters> noiseId) {
/* 200 */     final NormalNoise noise = this.randomState.getOrCreateNoise(noiseId);
/* 201 */     return new DoubleSupplier() {
/* 202 */         private long lastUpdateY = SurfaceRules.Context.this.lastUpdateY - 1L;
/*     */         
/*     */         private double lastNoise;
/*     */         
/*     */         public double getAsDouble() {
/* 207 */           if (this.lastUpdateY != SurfaceRules.Context.this.lastUpdateY) {
/* 208 */             this.lastNoise = noise.getValue(SurfaceRules.Context.this.blockX, SurfaceRules.Context.this.blockY, SurfaceRules.Context.this.blockZ);
/* 209 */             this.lastUpdateY = SurfaceRules.Context.this.lastUpdateY;
/*     */           } 
/* 211 */           return this.lastNoise;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static final class HoleCondition extends SurfaceRules.LazyXZCondition {
/*     */     private HoleCondition(SurfaceRules.Context context) {
/* 218 */       super(context);
/*     */     }
/*     */     
/*     */     protected boolean compute() {
/* 223 */       return (this.context.surfaceDepth <= 0);
/*     */     }
/*     */   }
/*     */   
/*     */   private final class AbovePreliminarySurfaceCondition implements SurfaceRules.Condition {
/*     */     public boolean test() {
/* 230 */       return (SurfaceRules.Context.this.blockY >= SurfaceRules.Context.this.getMinSurfaceLevel());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TemperatureHelperCondition extends SurfaceRules.LazyYCondition {
/*     */     private TemperatureHelperCondition(SurfaceRules.Context context) {
/* 236 */       super(context);
/*     */     }
/*     */     
/*     */     protected boolean compute() {
/* 241 */       return ((Biome)this.context.getBiome().value()).coldEnoughToSnow((BlockPos)this.context.pos.set(this.context.blockX, this.context.blockY, this.context.blockZ), this.context.getSeaLevel());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SteepMaterialCondition extends SurfaceRules.LazyXZCondition {
/*     */     private SteepMaterialCondition(SurfaceRules.Context context) {
/* 247 */       super(context);
/*     */     }
/*     */     
/*     */     protected boolean compute() {
/* 252 */       int chunkBlockX = this.context.blockX & 0xF;
/* 253 */       int chunkBlockZ = this.context.blockZ & 0xF;
/* 255 */       int zNorth = Math.max(chunkBlockZ - 1, 0);
/* 256 */       int zSouth = Math.min(chunkBlockZ + 1, 15);
/* 258 */       ChunkAccess chunk = this.context.chunk;
/* 259 */       int heightNorth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zNorth);
/* 260 */       int heightSouth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zSouth);
/* 262 */       if (heightSouth >= heightNorth + 4)
/* 263 */         return true; 
/* 266 */       int xWest = Math.max(chunkBlockX - 1, 0);
/* 267 */       int xEast = Math.min(chunkBlockX + 1, 15);
/* 268 */       int heightWest = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xWest, chunkBlockZ);
/* 269 */       int heightEast = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xEast, chunkBlockZ);
/* 271 */       return (heightWest >= heightEast + 4);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$Context.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */