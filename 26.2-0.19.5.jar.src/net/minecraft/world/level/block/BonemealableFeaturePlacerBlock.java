/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public class BonemealableFeaturePlacerBlock extends Block implements BonemealableBlock {
/*    */   public static final MapCodec<BonemealableFeaturePlacerBlock> CODEC;
/*    */   
/*    */   private final ResourceKey<ConfiguredFeature<?, ?>> feature;
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("feature").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, BonemealableFeaturePlacerBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<BonemealableFeaturePlacerBlock> codec() {
/* 25 */     return CODEC;
/*    */   }
/*    */   
/*    */   public BonemealableFeaturePlacerBlock(ResourceKey<ConfiguredFeature<?, ?>> feature, BlockBehaviour.Properties properties) {
/* 29 */     super(properties);
/* 30 */     this.feature = feature;
/*    */   }
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 35 */     return level.getBlockState(pos.above()).isAir();
/*    */   }
/*    */   
/*    */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 40 */     return true;
/*    */   }
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 45 */     level.registryAccess()
/* 46 */       .lookup(Registries.CONFIGURED_FEATURE)
/* 47 */       .flatMap(registry -> registry.get(this.feature))
/* 48 */       .ifPresent(mossPatch -> ((ConfiguredFeature)mossPatch.value()).place((WorldGenLevel)level, level.getChunkSource().getGenerator(), random, pos.above()));
/*    */   }
/*    */   
/*    */   public BonemealableBlock.Type getType() {
/* 53 */     return BonemealableBlock.Type.NEIGHBOR_SPREADER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\BonemealableFeaturePlacerBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */