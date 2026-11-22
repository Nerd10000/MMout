/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockStateMatchTest extends RuleTest {
/*    */   public static final MapCodec<BlockStateMatchTest> CODEC;
/*    */   
/*    */   private final BlockState blockState;
/*    */   
/*    */   static {
/*  8 */     CODEC = BlockState.CODEC.fieldOf("block_state").xmap(BlockStateMatchTest::new, t -> t.blockState);
/*    */   }
/*    */   
/*    */   public BlockStateMatchTest(BlockState blockState) {
/* 13 */     this.blockState = blockState;
/*    */   }
/*    */   
/*    */   public boolean test(BlockState blockState, RandomSource random) {
/* 18 */     return (blockState == this.blockState);
/*    */   }
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 23 */     return RuleTestType.BLOCKSTATE_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockStateMatchTest.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */