/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MangroveLeavesBlock extends TintedParticleLeavesBlock implements BonemealableBlock {
/*    */   public static final MapCodec<MangroveLeavesBlock> CODEC;
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, MangroveLeavesBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<MangroveLeavesBlock> codec() {
/* 21 */     return CODEC;
/*    */   }
/*    */   
/*    */   public MangroveLeavesBlock(float leafParticleChance, BlockBehaviour.Properties properties) {
/* 25 */     super(leafParticleChance, properties);
/*    */   }
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 30 */     return level.getBlockState(pos.below()).isAir();
/*    */   }
/*    */   
/*    */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 35 */     return true;
/*    */   }
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 40 */     level.setBlock(pos.below(), MangrovePropaguleBlock.createNewHangingPropagule(), 2);
/*    */   }
/*    */   
/*    */   public BlockPos getParticlePos(BlockPos blockPos) {
/* 45 */     return blockPos.below();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\MangroveLeavesBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */