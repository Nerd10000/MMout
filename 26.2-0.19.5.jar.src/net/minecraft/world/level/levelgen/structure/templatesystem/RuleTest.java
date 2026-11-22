/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class RuleTest {
/* 11 */   public static final Codec<RuleTest> CODEC = BuiltInRegistries.RULE_TEST.byNameCodec().dispatch("predicate_type", RuleTest::getType, RuleTestType::codec);
/*    */   
/*    */   public boolean testAgainstWorldState(LevelReader level, BlockPos pos, RandomSource random) {
/* 14 */     return test(level.getBlockState(pos), random);
/*    */   }
/*    */   
/*    */   public abstract boolean test(BlockState paramBlockState, RandomSource paramRandomSource);
/*    */   
/*    */   protected abstract RuleTestType<?> getType();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\RuleTest.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */