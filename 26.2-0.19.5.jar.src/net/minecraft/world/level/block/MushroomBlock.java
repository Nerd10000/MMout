/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class MushroomBlock extends VegetationBlock implements BonemealableBlock {
/*     */   public static final MapCodec<MushroomBlock> CODEC;
/*     */   
/*     */   static {
/*  25 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("feature").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, MushroomBlock::new));
/*     */   }
/*     */   
/*     */   public MapCodec<MushroomBlock> codec() {
/*  32 */     return CODEC;
/*     */   }
/*     */   
/*  35 */   private static final VoxelShape SHAPE = Block.column(6.0D, 0.0D, 6.0D);
/*     */   
/*     */   private final ResourceKey<ConfiguredFeature<?, ?>> feature;
/*     */   
/*     */   public MushroomBlock(ResourceKey<ConfiguredFeature<?, ?>> feature, BlockBehaviour.Properties properties) {
/*  40 */     super(properties);
/*  41 */     this.feature = feature;
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  46 */     return SHAPE;
/*     */   }
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  51 */     if (random.nextInt(25) == 0) {
/*  52 */       int max = 5;
/*  53 */       int r = 4;
/*  54 */       for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
/*  55 */         if (level.getBlockState(blockPos).is(this) && --max <= 0)
/*     */           return; 
/*     */       } 
/*  61 */       BlockPos offset = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
/*  62 */       for (int i = 0; i < 4; i++) {
/*  63 */         if (level.isEmptyBlock(offset) && state.canSurvive((LevelReader)level, offset))
/*  64 */           pos = offset; 
/*  66 */         offset = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
/*     */       } 
/*  69 */       if (level.isEmptyBlock(offset) && state.canSurvive((LevelReader)level, offset))
/*  70 */         level.setBlock(offset, state, 2); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/*  77 */     return state.isSolidRender();
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  82 */     BlockPos belowPos = pos.below();
/*  83 */     BlockState below = level.getBlockState(belowPos);
/*  84 */     if (below.is(BlockTags.OVERRIDES_MUSHROOM_LIGHT_REQUIREMENT))
/*  85 */       return true; 
/*  88 */     return (level.getRawBrightness(pos, 0) < 13 && mayPlaceOn(below, (BlockGetter)level, belowPos));
/*     */   }
/*     */   
/*     */   public boolean growMushroom(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
/*  92 */     Optional<? extends Holder<ConfiguredFeature<?, ?>>> feature = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(this.feature);
/*  93 */     if (feature.isEmpty())
/*  94 */       return false; 
/*  97 */     level.removeBlock(pos, false);
/*  99 */     if (((ConfiguredFeature)((Holder)feature.get()).value()).place((WorldGenLevel)level, level.getChunkSource().getGenerator(), random, pos))
/* 100 */       return true; 
/* 103 */     level.setBlock(pos, state, 3);
/* 104 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 109 */     if (level instanceof ServerLevel) {
/* 109 */       ServerLevel serverLevel = (ServerLevel)level;
/* 110 */       Optional<? extends Holder<ConfiguredFeature<?, ?>>> featureHolder = serverLevel.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(this.feature);
/* 111 */       if (featureHolder.isPresent()) {
/* 112 */         ConfiguredFeature<?, ?> configuredFeature = (ConfiguredFeature<?, ?>)((Holder)featureHolder.get()).value();
/* 113 */         if (configuredFeature.feature() instanceof net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature) {
/* 113 */           FeatureConfiguration featureConfiguration = configuredFeature.config();
/* 113 */           if (featureConfiguration instanceof HugeMushroomFeatureConfiguration) {
/* 113 */             HugeMushroomFeatureConfiguration config = (HugeMushroomFeatureConfiguration)featureConfiguration;
/* 114 */             int minHeight = 4 + config.foliageRadius();
/* 115 */             return level.isInsideBuildHeight(pos.above(minHeight));
/*     */           } 
/*     */         } 
/* 117 */         return false;
/*     */       } 
/* 119 */       return false;
/*     */     } 
/* 121 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 126 */     return (random.nextFloat() < 0.4D);
/*     */   }
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 131 */     growMushroom(level, pos, state, random);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\MushroomBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */