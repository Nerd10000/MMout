/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
/*     */ 
/*     */ final class LargeDripstone {
/*     */   private BlockPos root;
/*     */   
/*     */   private final boolean pointingUp;
/*     */   
/*     */   private int radius;
/*     */   
/*     */   private final double bluntness;
/*     */   
/*     */   private final double scale;
/*     */   
/*     */   private LargeDripstone(BlockPos root, boolean pointingUp, int radius, double bluntness, double scale) {
/* 113 */     this.root = root;
/* 114 */     this.pointingUp = pointingUp;
/* 115 */     this.radius = radius;
/* 116 */     this.bluntness = bluntness;
/* 117 */     this.scale = scale;
/*     */   }
/*     */   
/*     */   private int getHeight() {
/* 121 */     return getHeightAtRadius(0.0F);
/*     */   }
/*     */   
/*     */   private int getMinY() {
/* 125 */     if (this.pointingUp)
/* 126 */       return this.root.getY(); 
/* 128 */     return this.root.getY() - getHeight();
/*     */   }
/*     */   
/*     */   private int getMaxY() {
/* 133 */     if (!this.pointingUp)
/* 134 */       return this.root.getY(); 
/* 136 */     return this.root.getY() + getHeight();
/*     */   }
/*     */   
/*     */   private boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(WorldGenLevel level, LargeDripstoneFeature.WindOffsetter wind) {
/* 141 */     while (this.radius > 1) {
/* 142 */       BlockPos.MutableBlockPos newRoot = this.root.mutable();
/* 143 */       int maxTries = Math.min(10, getHeight());
/* 144 */       for (int i = 0; i < maxTries; i++) {
/* 145 */         if (level.getBlockState((BlockPos)newRoot).is(Blocks.LAVA))
/* 146 */           return false; 
/* 148 */         if (SpeleothemUtils.isCircleMostlyEmbeddedInStone(level, wind.offset((BlockPos)newRoot), this.radius)) {
/* 149 */           this.root = (BlockPos)newRoot;
/* 150 */           return true;
/*     */         } 
/* 152 */         newRoot.move(this.pointingUp ? Direction.DOWN : Direction.UP);
/*     */       } 
/* 154 */       this.radius /= 2;
/*     */     } 
/* 156 */     return false;
/*     */   }
/*     */   
/*     */   private int getHeightAtRadius(float checkRadius) {
/* 160 */     return (int)SpeleothemUtils.getSpeleothemHeight(checkRadius, this.radius, this.scale, this.bluntness);
/*     */   }
/*     */   
/*     */   private void placeBlocks(WorldGenLevel level, RandomSource random, LargeDripstoneFeature.WindOffsetter wind) {
/* 164 */     for (int dx = -this.radius; dx <= this.radius; dx++) {
/* 165 */       for (int dz = -this.radius; dz <= this.radius; dz++) {
/* 166 */         float currentRadius = Mth.sqrt((dx * dx + dz * dz));
/* 167 */         if (currentRadius <= this.radius) {
/* 171 */           int height = getHeightAtRadius(currentRadius);
/* 172 */           if (height > 0) {
/* 175 */             if (random.nextFloat() < 0.2D)
/* 177 */               height = (int)(height * Mth.randomBetween(random, 0.8F, 1.0F)); 
/* 180 */             BlockPos.MutableBlockPos pos = this.root.offset(dx, 0, dz).mutable();
/* 181 */             boolean hasBeenOutOfStone = false;
/* 183 */             int maxY = this.pointingUp ? level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ()) : Integer.MAX_VALUE;
/* 184 */             for (int i = 0; i < height && 
/* 185 */               pos.getY() < maxY; i++) {
/* 188 */               BlockPos windAdjustedPos = wind.offset((BlockPos)pos);
/* 189 */               if (SpeleothemUtils.isEmptyOrWaterOrLava((LevelAccessor)level, windAdjustedPos)) {
/* 190 */                 hasBeenOutOfStone = true;
/* 191 */                 Block block = SharedConstants.DEBUG_LARGE_DRIPSTONE ? Blocks.GLASS : Blocks.DRIPSTONE_BLOCK;
/* 192 */                 level.setBlock(windAdjustedPos, block.defaultBlockState(), 2);
/* 193 */               } else if (hasBeenOutOfStone && level.getBlockState(windAdjustedPos).is(BlockTags.BASE_STONE_OVERWORLD)) {
/*     */                 break;
/*     */               } 
/* 196 */               pos.move(this.pointingUp ? Direction.UP : Direction.DOWN);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isSuitableForWind(LargeDripstoneConfiguration config) {
/* 206 */     return (this.radius >= config.minRadiusForWind && this.bluntness >= config.minBluntnessForWind);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\LargeDripstoneFeature$LargeDripstone.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */