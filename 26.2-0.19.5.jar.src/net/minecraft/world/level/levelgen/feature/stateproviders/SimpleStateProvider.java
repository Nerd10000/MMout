/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SimpleStateProvider extends BlockStateProvider {
/*    */   public static final MapCodec<SimpleStateProvider> CODEC;
/*    */   
/*    */   private final BlockState state;
/*    */   
/*    */   static {
/* 10 */     CODEC = BlockState.CODEC.fieldOf("state").xmap(SimpleStateProvider::new, p -> p.state);
/*    */   }
/*    */   
/*    */   protected SimpleStateProvider(BlockState state) {
/* 15 */     this.state = state;
/*    */   }
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 20 */     return BlockStateProviderType.SIMPLE_STATE_PROVIDER;
/*    */   }
/*    */   
/*    */   public BlockState getState(WorldGenLevel level, RandomSource random, BlockPos pos) {
/* 25 */     return this.state;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\SimpleStateProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */