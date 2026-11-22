/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.references.BlockItemIds;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MyceliumBlock extends SpreadingSnowyBlock {
/* 13 */   public static final MapCodec<MyceliumBlock> CODEC = simpleCodec(MyceliumBlock::new);
/*    */   
/*    */   public MapCodec<MyceliumBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */   
/*    */   public MyceliumBlock(BlockBehaviour.Properties properties) {
/* 21 */     super(properties, BlockItemIds.DIRT.block());
/*    */   }
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 26 */     super.animateTick(state, level, pos, random);
/* 27 */     if (random.nextInt(10) == 0)
/* 28 */       level.addParticle((ParticleOptions)ParticleTypes.MYCELIUM, pos.getX() + random.nextDouble(), pos.getY() + 1.1D, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\MyceliumBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */