/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
/*     */ 
/*     */ public class SpikeFeature extends Feature<SpikeConfiguration> {
/*     */   public SpikeFeature(Codec<SpikeConfiguration> codec) {
/*  13 */     super(codec);
/*     */   }
/*     */   
/*     */   public boolean place(FeaturePlaceContext<SpikeConfiguration> context) {
/*  18 */     BlockPos origin = context.origin();
/*  19 */     RandomSource random = context.random();
/*  20 */     WorldGenLevel level = context.level();
/*  21 */     while (level.isEmptyBlock(origin) && origin.getY() > level.getMinY() + 2)
/*  22 */       origin = origin.below(); 
/*  25 */     SpikeConfiguration config = context.config();
/*  26 */     if (!config.canPlaceOn().test(level, origin))
/*  27 */       return false; 
/*  29 */     origin = origin.above(random.nextInt(4));
/*  31 */     int height = random.nextInt(4) + 7;
/*  32 */     int width = height / 4 + random.nextInt(2);
/*  34 */     if (width > 1 && random.nextInt(60) == 0)
/*  35 */       origin = origin.above(10 + random.nextInt(30)); 
/*  38 */     for (int yOff = 0; yOff < height; yOff++) {
/*  39 */       float scale = (1.0F - yOff / height) * width;
/*  40 */       int newWidth = Mth.ceil(scale);
/*  42 */       for (int i = -newWidth; i <= newWidth; i++) {
/*  43 */         float dx = Mth.abs(i) - 0.25F;
/*  44 */         for (int zo = -newWidth; zo <= newWidth; zo++) {
/*  45 */           float dz = Mth.abs(zo) - 0.25F;
/*  46 */           if ((i == 0 && zo == 0) || dx * dx + dz * dz <= scale * scale)
/*  49 */             if ((i != -newWidth && i != newWidth && zo != -newWidth && zo != newWidth) || 
/*  50 */               random.nextFloat() <= 0.75F) {
/*  55 */               BlockPos positiveOffset = origin.offset(i, yOff, zo);
/*  56 */               BlockState state = level.getBlockState(positiveOffset);
/*  58 */               if (state.isAir() || config.canReplace().test(level, positiveOffset))
/*  59 */                 setBlock((LevelWriter)level, positiveOffset, config.state()); 
/*  62 */               if (yOff != 0 && newWidth > 1) {
/*  63 */                 BlockPos negativeOffset = origin.offset(i, -yOff, zo);
/*  64 */                 state = level.getBlockState(negativeOffset);
/*  66 */                 if (state.isAir() || config.canReplace().test(level, negativeOffset))
/*  67 */                   setBlock((LevelWriter)level, negativeOffset, config.state()); 
/*     */               } 
/*     */             }  
/*     */         } 
/*     */       } 
/*     */     } 
/*  73 */     int pillarWidth = width - 1;
/*  74 */     if (pillarWidth < 0) {
/*  75 */       pillarWidth = 0;
/*  76 */     } else if (pillarWidth > 1) {
/*  77 */       pillarWidth = 1;
/*     */     } 
/*  79 */     for (int xo = -pillarWidth; xo <= pillarWidth; xo++) {
/*  80 */       for (int zo = -pillarWidth; zo <= pillarWidth; zo++) {
/*  81 */         BlockPos cursor = origin.offset(xo, -1, zo);
/*  82 */         int runLength = 50;
/*  83 */         if (Math.abs(xo) == 1 && Math.abs(zo) == 1)
/*  84 */           runLength = random.nextInt(5); 
/*  86 */         while (cursor.getY() > 50) {
/*  87 */           BlockState state = level.getBlockState(cursor);
/*  88 */           if (state.isAir() || config.canReplace().test(level, cursor) || state == config.state()) {
/*  89 */             setBlock((LevelWriter)level, cursor, config.state());
/*  93 */             cursor = cursor.below();
/*  94 */             runLength--;
/*  95 */             if (runLength <= 0) {
/*  96 */               cursor = cursor.below(random.nextInt(5) + 1);
/*  97 */               runLength = random.nextInt(5);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 103 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\SpikeFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */