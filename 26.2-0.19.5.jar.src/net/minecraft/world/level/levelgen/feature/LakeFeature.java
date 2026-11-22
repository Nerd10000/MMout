/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ 
/*     */ @Deprecated
/*     */ public class LakeFeature extends Feature<LakeFeature.Configuration> {
/*     */   public static final class Configuration extends Record implements FeatureConfiguration {
/*     */     private final BlockStateProvider fluid;
/*     */     
/*     */     private final BlockStateProvider barrier;
/*     */     
/*     */     private final BlockPredicate canPlaceFeature;
/*     */     
/*     */     private final BlockPredicate canReplaceWithAirOrFluid;
/*     */     
/*     */     private final BlockPredicate canReplaceWithBarrier;
/*     */     
/*     */     public static final Codec<Configuration> CODEC;
/*     */     
/*     */     public Configuration(BlockStateProvider fluid, BlockStateProvider barrier, BlockPredicate canPlaceFeature, BlockPredicate canReplaceWithAirOrFluid, BlockPredicate canReplaceWithBarrier) {
/*  19 */       this.fluid = fluid;
/*  19 */       this.barrier = barrier;
/*  19 */       this.canPlaceFeature = canPlaceFeature;
/*  19 */       this.canReplaceWithAirOrFluid = canReplaceWithAirOrFluid;
/*  19 */       this.canReplaceWithBarrier = canReplaceWithBarrier;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #19	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #19	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #19	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockStateProvider fluid() {
/*  19 */       return this.fluid;
/*     */     }
/*     */     
/*     */     public BlockStateProvider barrier() {
/*  19 */       return this.barrier;
/*     */     }
/*     */     
/*     */     public BlockPredicate canPlaceFeature() {
/*  19 */       return this.canPlaceFeature;
/*     */     }
/*     */     
/*     */     public BlockPredicate canReplaceWithAirOrFluid() {
/*  19 */       return this.canReplaceWithAirOrFluid;
/*     */     }
/*     */     
/*     */     public BlockPredicate canReplaceWithBarrier() {
/*  19 */       return this.canReplaceWithBarrier;
/*     */     }
/*     */     
/*     */     static {
/*  20 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockStateProvider.CODEC.fieldOf("fluid").forGetter(Configuration::fluid), (App)BlockStateProvider.CODEC.fieldOf("barrier").forGetter(Configuration::barrier), (App)BlockPredicate.CODEC.fieldOf("can_place_feature").forGetter(Configuration::canPlaceFeature), (App)BlockPredicate.CODEC.fieldOf("can_replace_with_air_or_fluid").forGetter(Configuration::canReplaceWithAirOrFluid), (App)BlockPredicate.CODEC.fieldOf("can_replace_with_barrier").forGetter(Configuration::canReplaceWithBarrier)).apply((Applicative)i, Configuration::new));
/*     */     }
/*     */   }
/*     */   
/*  29 */   private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();
/*     */   
/*     */   public LakeFeature(Codec<Configuration> codec) {
/*  32 */     super(codec);
/*     */   }
/*     */   
/*     */   public boolean place(FeaturePlaceContext<Configuration> context) {
/*  37 */     BlockPos origin = context.origin();
/*  38 */     WorldGenLevel level = context.level();
/*  39 */     RandomSource random = context.random();
/*  40 */     Configuration config = context.config();
/*  42 */     if (origin.getY() <= level.getMinY() + 4)
/*  43 */       return false; 
/*  46 */     origin = origin.offset(-8, -4, -8);
/*  48 */     boolean[] grid = new boolean[2048];
/*  50 */     int spots = random.nextInt(4) + 4;
/*  51 */     for (int i = 0; i < spots; i++) {
/*  52 */       double xr = random.nextDouble() * 6.0D + 3.0D;
/*  53 */       double yr = random.nextDouble() * 4.0D + 2.0D;
/*  54 */       double zr = random.nextDouble() * 6.0D + 3.0D;
/*  56 */       double xp = random.nextDouble() * (16.0D - xr - 2.0D) + 1.0D + xr / 2.0D;
/*  57 */       double yp = random.nextDouble() * (8.0D - yr - 4.0D) + 2.0D + yr / 2.0D;
/*  58 */       double zp = random.nextDouble() * (16.0D - zr - 2.0D) + 1.0D + zr / 2.0D;
/*  60 */       for (int j = 1; j < 15; j++) {
/*  61 */         for (int zz = 1; zz < 15; zz++) {
/*  62 */           for (int yy = 1; yy < 7; yy++) {
/*  63 */             double xd = (j - xp) / xr / 2.0D;
/*  64 */             double yd = (yy - yp) / yr / 2.0D;
/*  65 */             double zd = (zz - zp) / zr / 2.0D;
/*  66 */             double d = xd * xd + yd * yd + zd * zd;
/*  67 */             if (d < 1.0D)
/*  68 */               grid[(j * 16 + zz) * 8 + yy] = true; 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  75 */     BlockState fluid = config.fluid().getState(level, random, origin);
/*     */     int xx;
/*  77 */     for (xx = 0; xx < 16; xx++) {
/*  78 */       for (int zz = 0; zz < 16; zz++) {
/*  79 */         for (int yy = 0; yy < 8; yy++) {
/*  80 */           boolean check = (!grid[(xx * 16 + zz) * 8 + yy] && ((xx < 15 && grid[((xx + 1) * 16 + zz) * 8 + yy]) || (xx > 0 && grid[((xx - 1) * 16 + zz) * 8 + yy]) || (zz < 15 && grid[(xx * 16 + zz + 1) * 8 + yy]) || (zz > 0 && grid[(xx * 16 + zz - 1) * 8 + yy]) || (yy < 7 && grid[(xx * 16 + zz) * 8 + yy + 1]) || (yy > 0 && grid[(xx * 16 + zz) * 8 + yy - 1])));
/*  89 */           if (check) {
/*  90 */             BlockPos offsetPos = origin.offset(xx, yy, zz);
/*  91 */             BlockState blockState = level.getBlockState(offsetPos);
/*  92 */             if (yy >= 4 && blockState.liquid())
/*  93 */               return false; 
/*  95 */             if (yy < 4 && !blockState.isSolid() && blockState != fluid)
/*  96 */               return false; 
/*  98 */             if (!config.canPlaceFeature.test(level, offsetPos))
/*  99 */               return false; 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 106 */     for (xx = 0; xx < 16; xx++) {
/* 107 */       for (int zz = 0; zz < 16; zz++) {
/* 108 */         for (int yy = 0; yy < 8; yy++) {
/* 109 */           if (grid[(xx * 16 + zz) * 8 + yy]) {
/* 110 */             BlockPos placePos = origin.offset(xx, yy, zz);
/* 111 */             if (config.canReplaceWithAirOrFluid.test(level, placePos)) {
/* 114 */               boolean placeAir = (yy >= 4);
/* 115 */               level.setBlock(placePos, placeAir ? AIR : fluid, 2);
/* 116 */               if (placeAir) {
/* 117 */                 level.scheduleTick(placePos, AIR.getBlock(), 0);
/* 118 */                 markAboveForPostProcessing(level, placePos);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 125 */     BlockState barrier = config.barrier().getState(level, random, origin);
/* 127 */     if (!barrier.isAir())
/* 128 */       for (int j = 0; j < 16; j++) {
/* 129 */         for (int zz = 0; zz < 16; zz++) {
/* 130 */           for (int yy = 0; yy < 8; yy++) {
/* 131 */             boolean check = (!grid[(j * 16 + zz) * 8 + yy] && ((j < 15 && grid[((j + 1) * 16 + zz) * 8 + yy]) || (j > 0 && grid[((j - 1) * 16 + zz) * 8 + yy]) || (zz < 15 && grid[(j * 16 + zz + 1) * 8 + yy]) || (zz > 0 && grid[(j * 16 + zz - 1) * 8 + yy]) || (yy < 7 && grid[(j * 16 + zz) * 8 + yy + 1]) || (yy > 0 && grid[(j * 16 + zz) * 8 + yy - 1])));
/* 140 */             if (check && (
/* 141 */               yy < 4 || random.nextInt(2) != 0)) {
/* 142 */               BlockPos offset = origin.offset(j, yy, zz);
/* 143 */               BlockState blockState = level.getBlockState(offset);
/* 144 */               if (blockState.isSolid() && config.canReplaceWithBarrier.test(level, offset)) {
/* 145 */                 BlockPos barrierPos = origin.offset(j, yy, zz);
/* 146 */                 level.setBlock(barrierPos, barrier, 2);
/* 147 */                 markAboveForPostProcessing(level, barrierPos);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }  
/* 156 */     if (fluid.getFluidState().is(FluidTags.WATER))
/* 157 */       for (int j = 0; j < 16; j++) {
/* 158 */         for (int zz = 0; zz < 16; zz++) {
/* 159 */           int yy = 4;
/* 160 */           BlockPos offset = origin.offset(j, 4, zz);
/* 161 */           if (((Biome)level.getBiome(offset).value()).shouldFreeze((LevelReader)level, offset, false) && config.canReplaceWithAirOrFluid.test(level, offset))
/* 162 */             level.setBlock(offset, Blocks.ICE.defaultBlockState(), 2); 
/*     */         } 
/*     */       }  
/* 168 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\LakeFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */