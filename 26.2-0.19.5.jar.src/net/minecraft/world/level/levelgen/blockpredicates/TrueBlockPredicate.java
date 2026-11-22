/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ 
/*    */ public class TrueBlockPredicate implements BlockPredicate {
/*  8 */   public static final TrueBlockPredicate INSTANCE = new TrueBlockPredicate();
/*    */   
/*  9 */   public static final MapCodec<TrueBlockPredicate> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/*    */   public boolean test(WorldGenLevel level, BlockPos origin) {
/* 16 */     return true;
/*    */   }
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 21 */     return BlockPredicateType.TRUE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blockpredicates\TrueBlockPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */