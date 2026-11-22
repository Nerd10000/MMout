/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RandomBlockMatchTest extends RuleTest {
/*    */   public static final MapCodec<RandomBlockMatchTest> CODEC;
/*    */   
/*    */   private final Block block;
/*    */   
/*    */   private final float probability;
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(()), (App)Codec.FLOAT.fieldOf("probability").forGetter(())).apply((Applicative)i, RandomBlockMatchTest::new));
/*    */   }
/*    */   
/*    */   public RandomBlockMatchTest(Block block, float probability) {
/* 21 */     this.block = block;
/* 22 */     this.probability = probability;
/*    */   }
/*    */   
/*    */   public boolean test(BlockState blockState, RandomSource random) {
/* 27 */     return (blockState.is(this.block) && random.nextFloat() < this.probability);
/*    */   }
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 32 */     return RuleTestType.RANDOM_BLOCK_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\RandomBlockMatchTest.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */