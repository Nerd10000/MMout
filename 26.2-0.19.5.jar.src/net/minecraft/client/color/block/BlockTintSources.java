/*     */ package net.minecraft.client.color.block;
/*     */ 
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.renderer.BiomeColors;
/*     */ import net.minecraft.client.renderer.block.BlockAndTintGetter;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.world.level.GrassColor;
/*     */ import net.minecraft.world.level.block.DoublePlantBlock;
/*     */ import net.minecraft.world.level.block.RedStoneWireBlock;
/*     */ import net.minecraft.world.level.block.StemBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class BlockTintSources {
/*     */   public static BlockTintSource constant(int color) {
/*  22 */     return paramBlockState -> color;
/*     */   }
/*     */   
/*     */   public static BlockTintSource constant(final int colorInHand, final int colorInWorld) {
/*  26 */     return new BlockTintSource() {
/*     */         public int color(BlockState state) {
/*  29 */           return colorInHand;
/*     */         }
/*     */         
/*     */         public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/*  34 */           return colorInWorld;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static BlockTintSource doubleTallGrass() {
/*  40 */     return new BlockTintSource() {
/*     */         public int color(BlockState state) {
/*  43 */           return GrassColor.getDefaultColor();
/*     */         }
/*     */         
/*     */         public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/*  48 */           return BiomeColors.getAverageGrassColor(level, (state.getValue((Property)DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) ? pos.below() : pos);
/*     */         }
/*     */         
/*     */         public Set<Property<?>> relevantProperties() {
/*  53 */           return (Set)Set.of(DoublePlantBlock.HALF);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static BlockTintSource grass() {
/*  59 */     return new BlockTintSource() {
/*     */         public int color(BlockState state) {
/*  62 */           return GrassColor.getDefaultColor();
/*     */         }
/*     */         
/*     */         public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/*  67 */           return BiomeColors.getAverageGrassColor(level, pos);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static BlockTintSource grassBlock() {
/*  73 */     return new BlockTintSource() {
/*     */         public int color(BlockState state) {
/*  76 */           return GrassColor.getDefaultColor();
/*     */         }
/*     */         
/*     */         public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/*  81 */           return BiomeColors.getAverageGrassColor(level, pos);
/*     */         }
/*     */         
/*     */         public int colorAsTerrainParticle(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/*  87 */           return -1;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static BlockTintSource sugarCane() {
/*  93 */     return new BlockTintSource() {
/*     */         public int color(BlockState state) {
/*  97 */           return -1;
/*     */         }
/*     */         
/*     */         public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/* 102 */           return BiomeColors.getAverageGrassColor(level, pos);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static BlockTintSource foliage() {
/* 108 */     return new BlockTintSource() {
/*     */         public int color(BlockState state) {
/* 111 */           return -12012264;
/*     */         }
/*     */         
/*     */         public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/* 116 */           return BiomeColors.getAverageFoliageColor(level, pos);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static BlockTintSource dryFoliage() {
/* 122 */     return new BlockTintSource() {
/*     */         public int color(BlockState state) {
/* 125 */           return -10732494;
/*     */         }
/*     */         
/*     */         public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/* 130 */           return BiomeColors.getAverageDryFoliageColor(level, pos);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static BlockTintSource water() {
/* 136 */     return new BlockTintSource() {
/*     */         public int color(BlockState state) {
/* 140 */           return -1;
/*     */         }
/*     */         
/*     */         public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/* 145 */           return BiomeColors.getAverageWaterColor(level, pos);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static BlockTintSource waterParticles() {
/* 152 */     return new BlockTintSource() {
/*     */         public int color(BlockState state) {
/* 156 */           return -1;
/*     */         }
/*     */         
/*     */         public int colorAsTerrainParticle(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/* 161 */           return BiomeColors.getAverageWaterColor(level, pos);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static BlockTintSource redstone() {
/* 167 */     return new BlockTintSource() {
/*     */         public int color(BlockState state) {
/* 170 */           return RedStoneWireBlock.getColorForPower(((Integer)state.getValue((Property)RedStoneWireBlock.POWER)).intValue());
/*     */         }
/*     */         
/*     */         public Set<Property<?>> relevantProperties() {
/* 175 */           return (Set)Set.of(RedStoneWireBlock.POWER);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static BlockTintSource stem() {
/* 181 */     return new BlockTintSource() {
/*     */         public int color(BlockState state) {
/* 184 */           int age = ((Integer)state.getValue((Property)StemBlock.AGE)).intValue();
/* 185 */           return ARGB.color(age * 32, 255 - age * 8, age * 4);
/*     */         }
/*     */         
/*     */         public Set<Property<?>> relevantProperties() {
/* 190 */           return (Set)Set.of(StemBlock.AGE);
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\color\block\BlockTintSources.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */