/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class PoweredBlock extends Block {
/* 11 */   public static final MapCodec<PoweredBlock> CODEC = simpleCodec(PoweredBlock::new);
/*    */   
/*    */   public MapCodec<PoweredBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */   
/*    */   public PoweredBlock(BlockBehaviour.Properties properties) {
/* 19 */     super(properties);
/*    */   }
/*    */   
/*    */   protected boolean isSignalSource(BlockState state) {
/* 24 */     return true;
/*    */   }
/*    */   
/*    */   protected int ownSignal(BlockState state, BlockGetter level, BlockPos pos) {
/* 29 */     return 15;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\PoweredBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */