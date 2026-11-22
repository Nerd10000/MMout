/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class AlwaysTrueTest extends RuleTest {
/* 10 */   public static final MapCodec<AlwaysTrueTest> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 12 */   public static final AlwaysTrueTest INSTANCE = new AlwaysTrueTest();
/*    */   
/*    */   public boolean testAgainstWorldState(LevelReader level, BlockPos pos, RandomSource random) {
/* 19 */     return true;
/*    */   }
/*    */   
/*    */   public boolean test(BlockState blockState, RandomSource random) {
/* 24 */     return true;
/*    */   }
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 29 */     return RuleTestType.ALWAYS_TRUE_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\AlwaysTrueTest.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */