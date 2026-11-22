/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ 
/*    */ public class BlockPredicateFilter extends PlacementFilter {
/*    */   public static final MapCodec<BlockPredicateFilter> CODEC;
/*    */   
/*    */   private final BlockPredicate predicate;
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockPredicate.CODEC.fieldOf("predicate").forGetter(())).apply((Applicative)i, BlockPredicateFilter::new));
/*    */   }
/*    */   
/*    */   private BlockPredicateFilter(BlockPredicate predicate) {
/* 19 */     this.predicate = predicate;
/*    */   }
/*    */   
/*    */   public static BlockPredicateFilter forPredicate(BlockPredicate predicate) {
/* 23 */     return new BlockPredicateFilter(predicate);
/*    */   }
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos origin) {
/* 28 */     return this.predicate.test(context.getLevel(), origin);
/*    */   }
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 33 */     return PlacementModifierType.BLOCK_PREDICATE_FILTER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\BlockPredicateFilter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */