/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MatchingBlockTagPredicate extends StateTestingPredicate {
/*    */   public static final MapCodec<MatchingBlockTagPredicate> CODEC;
/*    */   
/*    */   private final TagKey<Block> tag;
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> stateTestingCodec(i).and((App)TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(())).apply((Applicative)i, MatchingBlockTagPredicate::new));
/*    */   }
/*    */   
/*    */   protected MatchingBlockTagPredicate(Vec3i offset, TagKey<Block> tag) {
/* 19 */     super(offset);
/* 20 */     this.tag = tag;
/*    */   }
/*    */   
/*    */   protected boolean test(BlockState state) {
/* 25 */     return state.is(this.tag);
/*    */   }
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 30 */     return BlockPredicateType.MATCHING_BLOCK_TAG;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blockpredicates\MatchingBlockTagPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */