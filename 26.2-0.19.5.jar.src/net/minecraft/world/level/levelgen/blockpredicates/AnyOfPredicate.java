/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ 
/*    */ public class AnyOfPredicate extends CombiningPredicate {
/* 10 */   public static final MapCodec<AnyOfPredicate> CODEC = codec(AnyOfPredicate::new);
/*    */   
/*    */   public AnyOfPredicate(List<BlockPredicate> predicates) {
/* 13 */     super(predicates);
/*    */   }
/*    */   
/*    */   public boolean test(WorldGenLevel level, BlockPos origin) {
/* 18 */     for (BlockPredicate predicate : this.predicates) {
/* 19 */       if (predicate.test(level, origin))
/* 20 */         return true; 
/*    */     } 
/* 23 */     return false;
/*    */   }
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 28 */     return BlockPredicateType.ANY_OF;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blockpredicates\AnyOfPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */