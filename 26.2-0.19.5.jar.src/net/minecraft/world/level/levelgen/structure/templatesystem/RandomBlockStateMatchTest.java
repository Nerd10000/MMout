/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RandomBlockStateMatchTest extends RuleTest {
/*    */   public static final MapCodec<RandomBlockStateMatchTest> CODEC;
/*    */   
/*    */   private final BlockState blockState;
/*    */   
/*    */   private final float probability;
/*    */   
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockState.CODEC.fieldOf("block_state").forGetter(()), (App)Codec.FLOAT.fieldOf("probability").forGetter(())).apply((Applicative)i, RandomBlockStateMatchTest::new));
/*    */   }
/*    */   
/*    */   public RandomBlockStateMatchTest(BlockState blockState, float probability) {
/* 19 */     this.blockState = blockState;
/* 20 */     this.probability = probability;
/*    */   }
/*    */   
/*    */   public boolean test(BlockState blockState, RandomSource random) {
/* 25 */     return (blockState == this.blockState && random.nextFloat() < this.probability);
/*    */   }
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 30 */     return RuleTestType.RANDOM_BLOCKSTATE_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\RandomBlockStateMatchTest.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */