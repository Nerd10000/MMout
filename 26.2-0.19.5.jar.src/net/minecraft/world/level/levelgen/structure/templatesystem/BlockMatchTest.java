/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockMatchTest extends RuleTest {
/*    */   public static final MapCodec<BlockMatchTest> CODEC;
/*    */   
/*    */   private final Block block;
/*    */   
/*    */   static {
/* 10 */     CODEC = BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").xmap(BlockMatchTest::new, t -> t.block);
/*    */   }
/*    */   
/*    */   public BlockMatchTest(Block block) {
/* 15 */     this.block = block;
/*    */   }
/*    */   
/*    */   public boolean test(BlockState blockState, RandomSource random) {
/* 20 */     return blockState.is(this.block);
/*    */   }
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 25 */     return RuleTestType.BLOCK_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockMatchTest.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */