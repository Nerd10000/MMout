/*    */ package net.minecraft.world.level.levelgen.feature.rootplacers;
/*    */ 
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public abstract class RootPlacer {
/* 24 */   public static final Codec<RootPlacer> CODEC = BuiltInRegistries.ROOT_PLACER_TYPE.byNameCodec().dispatch(RootPlacer::type, RootPlacerType::codec);
/*    */   
/*    */   protected final IntProvider trunkOffsetY;
/*    */   
/*    */   protected final BlockStateProvider rootProvider;
/*    */   
/*    */   protected final Optional<AboveRootPlacement> aboveRootPlacement;
/*    */   
/*    */   protected static <P extends RootPlacer> Products.P3<RecordCodecBuilder.Mu<P>, IntProvider, BlockStateProvider, Optional<AboveRootPlacement>> rootPlacerParts(RecordCodecBuilder.Instance<P> instance) {
/* 31 */     return instance.group((App)IntProviders.CODEC
/* 32 */         .fieldOf("trunk_offset_y").forGetter(c -> c.trunkOffsetY), (App)BlockStateProvider.CODEC
/* 33 */         .fieldOf("root_provider").forGetter(c -> c.rootProvider), (App)AboveRootPlacement.CODEC
/* 34 */         .optionalFieldOf("above_root_placement").forGetter(c -> c.aboveRootPlacement));
/*    */   }
/*    */   
/*    */   public RootPlacer(IntProvider trunkOffsetY, BlockStateProvider rootProvider, Optional<AboveRootPlacement> aboveRootPlacement) {
/* 39 */     this.trunkOffsetY = trunkOffsetY;
/* 40 */     this.rootProvider = rootProvider;
/* 41 */     this.aboveRootPlacement = aboveRootPlacement;
/*    */   }
/*    */   
/*    */   protected boolean canPlaceRoot(LevelSimulatedReader level, BlockPos pos) {
/* 49 */     return TreeFeature.validTreePos(level, pos);
/*    */   }
/*    */   
/*    */   protected void placeRoot(WorldGenLevel level, BiConsumer<BlockPos, BlockState> rootSetter, RandomSource random, BlockPos pos, TreeConfiguration config) {
/* 53 */     if (!canPlaceRoot((LevelSimulatedReader)level, pos))
/*    */       return; 
/* 56 */     rootSetter.accept(pos, getPotentiallyWaterloggedState((LevelSimulatedReader)level, pos, this.rootProvider.getState(level, random, pos)));
/* 57 */     if (this.aboveRootPlacement.isPresent()) {
/* 58 */       AboveRootPlacement abovePlacement = this.aboveRootPlacement.get();
/* 59 */       BlockPos above = pos.above();
/* 60 */       if (random.nextFloat() < abovePlacement.aboveRootPlacementChance() && level.isStateAtPosition(above, BlockBehaviour.BlockStateBase::isAir))
/* 61 */         rootSetter.accept(above, getPotentiallyWaterloggedState((LevelSimulatedReader)level, above, abovePlacement.aboveRootProvider().getState(level, random, above))); 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected BlockState getPotentiallyWaterloggedState(LevelSimulatedReader level, BlockPos pos, BlockState state) {
/* 67 */     if (state.hasProperty((Property)BlockStateProperties.WATERLOGGED)) {
/* 68 */       boolean waterlogged = level.isFluidAtPosition(pos, s -> s.is(FluidTags.WATER));
/* 69 */       return (BlockState)state.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(waterlogged));
/*    */     } 
/* 71 */     return state;
/*    */   }
/*    */   
/*    */   public BlockPos getTrunkOrigin(BlockPos origin, RandomSource random) {
/* 75 */     return origin.above(this.trunkOffsetY.sample(random));
/*    */   }
/*    */   
/*    */   protected abstract RootPlacerType<?> type();
/*    */   
/*    */   public abstract boolean placeRoots(WorldGenLevel paramWorldGenLevel, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, BlockPos paramBlockPos1, BlockPos paramBlockPos2, TreeConfiguration paramTreeConfiguration);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\rootplacers\RootPlacer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */