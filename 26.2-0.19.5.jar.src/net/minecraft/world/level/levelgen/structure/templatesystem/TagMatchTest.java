/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TagMatchTest extends RuleTest {
/*    */   public static final MapCodec<TagMatchTest> CODEC;
/*    */   
/*    */   private final TagKey<Block> tag;
/*    */   
/*    */   static {
/* 11 */     CODEC = TagKey.codec(Registries.BLOCK).fieldOf("tag").xmap(TagMatchTest::new, t -> t.tag);
/*    */   }
/*    */   
/*    */   public TagMatchTest(TagKey<Block> tag) {
/* 16 */     this.tag = tag;
/*    */   }
/*    */   
/*    */   public boolean test(BlockState blockState, RandomSource random) {
/* 21 */     return blockState.is(this.tag);
/*    */   }
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 26 */     return RuleTestType.TAG_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\TagMatchTest.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */