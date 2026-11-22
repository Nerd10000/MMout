/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TallFlowerBlock extends DoublePlantBlock implements BonemealableBlock {
/* 13 */   public static final MapCodec<TallFlowerBlock> CODEC = simpleCodec(TallFlowerBlock::new);
/*    */   
/*    */   public MapCodec<TallFlowerBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */   
/*    */   public TallFlowerBlock(BlockBehaviour.Properties properties) {
/* 21 */     super(properties);
/*    */   }
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 26 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 31 */     return true;
/*    */   }
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 36 */     popResource((Level)level, pos, new ItemStack(this));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\TallFlowerBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */