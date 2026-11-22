/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.FloatProvider;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Column;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LargeDripstoneFeature extends Feature<LargeDripstoneConfiguration> {
/*     */   public LargeDripstoneFeature(Codec<LargeDripstoneConfiguration> codec) {
/*  27 */     super(codec);
/*     */   }
/*     */   
/*     */   public boolean place(FeaturePlaceContext<LargeDripstoneConfiguration> context) {
/*     */     Column.Range columnRange;
/*     */     WindOffsetter wind;
/*  32 */     WorldGenLevel level = context.level();
/*  33 */     BlockPos origin = context.origin();
/*  34 */     LargeDripstoneConfiguration config = context.config();
/*  35 */     RandomSource random = context.random();
/*  37 */     if (!SpeleothemUtils.isEmptyOrWater((LevelAccessor)level, origin))
/*  38 */       return false; 
/*  42 */     Optional<Column> column = Column.scan((LevelSimulatedReader)level, origin, config.floorToCeilingSearchRange, SpeleothemUtils::isEmptyOrWater, state -> SpeleothemUtils.isBaseOrLava(state, Blocks.DRIPSTONE_BLOCK, config.replaceableBlocks));
/*  43 */     if (!column.isEmpty()) {
/*  43 */       Column.Range range = (Column.Range)column.get();
/*  43 */       if (range instanceof Column.Range) {
/*  43 */         columnRange = range;
/*     */       } else {
/*  47 */         return false;
/*     */       } 
/*     */     } else {
/*  47 */       return false;
/*     */     } 
/*  50 */     if (columnRange.height() < 4)
/*  52 */       return false; 
/*  56 */     int maxColumnRadiusBasedOnColumnHeight = (int)(columnRange.height() * config.maxColumnRadiusToCaveHeightRatio);
/*  57 */     int maxColumnRadius = Mth.clamp(maxColumnRadiusBasedOnColumnHeight, config.columnRadius.minInclusive(), config.columnRadius.maxInclusive());
/*  58 */     int radius = Mth.randomBetweenInclusive(random, config.columnRadius.minInclusive(), maxColumnRadius);
/*  60 */     LargeDripstone stalactite = makeDripstone(origin.atY(columnRange.ceiling() - 1), false, random, radius, config.stalactiteBluntness, config.heightScale);
/*  61 */     LargeDripstone stalagmite = makeDripstone(origin.atY(columnRange.floor() + 1), true, random, radius, config.stalagmiteBluntness, config.heightScale);
/*  64 */     if (stalactite.isSuitableForWind(config) && stalagmite.isSuitableForWind(config)) {
/*  65 */       wind = new WindOffsetter(origin.getY(), random, config.windSpeed, 16 - radius);
/*     */     } else {
/*  67 */       wind = WindOffsetter.noWind();
/*     */     } 
/*  70 */     boolean stalactiteBaseEmbeddedInStone = stalactite.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(level, wind);
/*  71 */     boolean stalagmiteBaseEmbeddedInStone = stalagmite.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(level, wind);
/*  73 */     if (stalactiteBaseEmbeddedInStone)
/*  74 */       stalactite.placeBlocks(level, random, wind); 
/*  77 */     if (stalagmiteBaseEmbeddedInStone)
/*  78 */       stalagmite.placeBlocks(level, random, wind); 
/*  81 */     if (SharedConstants.DEBUG_LARGE_DRIPSTONE)
/*  82 */       placeDebugMarkers(level, origin, columnRange, wind); 
/*  85 */     return true;
/*     */   }
/*     */   
/*     */   private static LargeDripstone makeDripstone(BlockPos root, boolean pointingUp, RandomSource random, int radius, FloatProvider bluntness, FloatProvider heightScale) {
/*  89 */     return new LargeDripstone(root, pointingUp, radius, bluntness.sample(random), heightScale.sample(random));
/*     */   }
/*     */   
/*     */   private void placeDebugMarkers(WorldGenLevel level, BlockPos origin, Column.Range range, WindOffsetter wind) {
/*  93 */     level.setBlock(wind.offset(origin.atY(range.ceiling() - 1)), Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
/*  94 */     level.setBlock(wind.offset(origin.atY(range.floor() + 1)), Blocks.GOLD_BLOCK.defaultBlockState(), 2);
/*  95 */     BlockPos.MutableBlockPos pos = origin.atY(range.floor() + 2).mutable();
/*  96 */     while (pos.getY() < range.ceiling() - 1) {
/*  97 */       BlockPos windAdjustedPos = wind.offset((BlockPos)pos);
/*  98 */       if (SpeleothemUtils.isEmptyOrWater((LevelAccessor)level, windAdjustedPos) || level.getBlockState(windAdjustedPos).is(Blocks.DRIPSTONE_BLOCK))
/*  99 */         level.setBlock(windAdjustedPos, Blocks.CREEPER_HEAD.defaultBlockState(), 2); 
/* 101 */       pos.move(Direction.UP);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class LargeDripstone {
/*     */     private BlockPos root;
/*     */     
/*     */     private final boolean pointingUp;
/*     */     
/*     */     private int radius;
/*     */     
/*     */     private final double bluntness;
/*     */     
/*     */     private final double scale;
/*     */     
/*     */     private LargeDripstone(BlockPos root, boolean pointingUp, int radius, double bluntness, double scale) {
/* 113 */       this.root = root;
/* 114 */       this.pointingUp = pointingUp;
/* 115 */       this.radius = radius;
/* 116 */       this.bluntness = bluntness;
/* 117 */       this.scale = scale;
/*     */     }
/*     */     
/*     */     private int getHeight() {
/* 121 */       return getHeightAtRadius(0.0F);
/*     */     }
/*     */     
/*     */     private int getMinY() {
/* 125 */       if (this.pointingUp)
/* 126 */         return this.root.getY(); 
/* 128 */       return this.root.getY() - getHeight();
/*     */     }
/*     */     
/*     */     private int getMaxY() {
/* 133 */       if (!this.pointingUp)
/* 134 */         return this.root.getY(); 
/* 136 */       return this.root.getY() + getHeight();
/*     */     }
/*     */     
/*     */     private boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(WorldGenLevel level, LargeDripstoneFeature.WindOffsetter wind) {
/* 141 */       while (this.radius > 1) {
/* 142 */         BlockPos.MutableBlockPos newRoot = this.root.mutable();
/* 143 */         int maxTries = Math.min(10, getHeight());
/* 144 */         for (int i = 0; i < maxTries; i++) {
/* 145 */           if (level.getBlockState((BlockPos)newRoot).is(Blocks.LAVA))
/* 146 */             return false; 
/* 148 */           if (SpeleothemUtils.isCircleMostlyEmbeddedInStone(level, wind.offset((BlockPos)newRoot), this.radius)) {
/* 149 */             this.root = (BlockPos)newRoot;
/* 150 */             return true;
/*     */           } 
/* 152 */           newRoot.move(this.pointingUp ? Direction.DOWN : Direction.UP);
/*     */         } 
/* 154 */         this.radius /= 2;
/*     */       } 
/* 156 */       return false;
/*     */     }
/*     */     
/*     */     private int getHeightAtRadius(float checkRadius) {
/* 160 */       return (int)SpeleothemUtils.getSpeleothemHeight(checkRadius, this.radius, this.scale, this.bluntness);
/*     */     }
/*     */     
/*     */     private void placeBlocks(WorldGenLevel level, RandomSource random, LargeDripstoneFeature.WindOffsetter wind) {
/* 164 */       for (int dx = -this.radius; dx <= this.radius; dx++) {
/* 165 */         for (int dz = -this.radius; dz <= this.radius; dz++) {
/* 166 */           float currentRadius = Mth.sqrt((dx * dx + dz * dz));
/* 167 */           if (currentRadius <= this.radius) {
/* 171 */             int height = getHeightAtRadius(currentRadius);
/* 172 */             if (height > 0) {
/* 175 */               if (random.nextFloat() < 0.2D)
/* 177 */                 height = (int)(height * Mth.randomBetween(random, 0.8F, 1.0F)); 
/* 180 */               BlockPos.MutableBlockPos pos = this.root.offset(dx, 0, dz).mutable();
/* 181 */               boolean hasBeenOutOfStone = false;
/* 183 */               int maxY = this.pointingUp ? level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ()) : Integer.MAX_VALUE;
/* 184 */               for (int i = 0; i < height && 
/* 185 */                 pos.getY() < maxY; i++) {
/* 188 */                 BlockPos windAdjustedPos = wind.offset((BlockPos)pos);
/* 189 */                 if (SpeleothemUtils.isEmptyOrWaterOrLava((LevelAccessor)level, windAdjustedPos)) {
/* 190 */                   hasBeenOutOfStone = true;
/* 191 */                   Block block = SharedConstants.DEBUG_LARGE_DRIPSTONE ? Blocks.GLASS : Blocks.DRIPSTONE_BLOCK;
/* 192 */                   level.setBlock(windAdjustedPos, block.defaultBlockState(), 2);
/* 193 */                 } else if (hasBeenOutOfStone && level.getBlockState(windAdjustedPos).is(BlockTags.BASE_STONE_OVERWORLD)) {
/*     */                   break;
/*     */                 } 
/* 196 */                 pos.move(this.pointingUp ? Direction.UP : Direction.DOWN);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean isSuitableForWind(LargeDripstoneConfiguration config) {
/* 206 */       return (this.radius >= config.minRadiusForWind && this.bluntness >= config.minBluntnessForWind);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class WindOffsetter {
/*     */     private final int originY;
/*     */     
/*     */     private final Vec3 windSpeed;
/*     */     
/*     */     private final int maxOffset;
/*     */     
/*     */     private WindOffsetter(int originY, RandomSource random, FloatProvider windSpeedRange, int maxOffset) {
/* 217 */       this.originY = originY;
/* 218 */       this.maxOffset = maxOffset;
/* 220 */       float speed = windSpeedRange.sample(random);
/* 222 */       float direction = Mth.randomBetween(random, 0.0F, 3.1415927F);
/* 223 */       this.windSpeed = new Vec3((Mth.cos(direction) * speed), 0.0D, (Mth.sin(direction) * speed));
/*     */     }
/*     */     
/*     */     private WindOffsetter() {
/* 227 */       this.originY = 0;
/* 228 */       this.windSpeed = null;
/* 229 */       this.maxOffset = 0;
/*     */     }
/*     */     
/*     */     private static WindOffsetter noWind() {
/* 233 */       return new WindOffsetter();
/*     */     }
/*     */     
/*     */     private BlockPos offset(BlockPos pos) {
/* 237 */       if (this.windSpeed == null)
/* 238 */         return pos; 
/* 240 */       int dy = this.originY - pos.getY();
/* 241 */       Vec3 totalWindAdjust = this.windSpeed.scale(dy);
/* 242 */       int dx = Mth.clamp(Mth.floor(totalWindAdjust.x), -this.maxOffset, this.maxOffset);
/* 243 */       int dz = Mth.clamp(Mth.floor(totalWindAdjust.z), -this.maxOffset, this.maxOffset);
/* 244 */       return pos.offset(dx, 0, dz);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\LargeDripstoneFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */