/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class NetherFungusBlock extends VegetationBlock implements BonemealableBlock {
/*    */   public static final MapCodec<NetherFungusBlock> CODEC;
/*    */   
/*    */   private static final double BONEMEAL_SUCCESS_PROBABILITY = 0.4D;
/*    */   
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("feature").forGetter(()), (App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("grows_on").forGetter(()), (App)TagKey.codec(Registries.BLOCK).fieldOf("support_blocks").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, NetherFungusBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<NetherFungusBlock> codec() {
/* 33 */     return CODEC;
/*    */   }
/*    */   
/* 38 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 9.0D);
/*    */   
/*    */   private final Block requiredBlock;
/*    */   
/*    */   private final ResourceKey<ConfiguredFeature<?, ?>> feature;
/*    */   
/*    */   private final TagKey<Block> supportBlocks;
/*    */   
/*    */   protected NetherFungusBlock(ResourceKey<ConfiguredFeature<?, ?>> feature, Block requiredBlock, TagKey<Block> supportBlocks, BlockBehaviour.Properties properties) {
/* 45 */     super(properties);
/* 46 */     this.feature = feature;
/* 47 */     this.requiredBlock = requiredBlock;
/* 48 */     this.supportBlocks = supportBlocks;
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 53 */     return SHAPE;
/*    */   }
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 58 */     return state.is(this.supportBlocks);
/*    */   }
/*    */   
/*    */   private Optional<? extends Holder<ConfiguredFeature<?, ?>>> getFeature(LevelReader level) {
/* 62 */     return level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(this.feature);
/*    */   }
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 68 */     BlockState belowState = level.getBlockState(pos.below());
/* 69 */     return (belowState.is(this.requiredBlock) && level.isInsideBuildHeight(pos.above()));
/*    */   }
/*    */   
/*    */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 74 */     return (random.nextFloat() < 0.4D);
/*    */   }
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 79 */     getFeature((LevelReader)level).ifPresent(feature -> ((ConfiguredFeature)feature.value()).place((WorldGenLevel)level, level.getChunkSource().getGenerator(), random, pos));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\NetherFungusBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */