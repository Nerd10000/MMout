/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public abstract class RepeatingPlacement extends PlacementModifier {
/*    */   protected abstract int count(RandomSource paramRandomSource, BlockPos paramBlockPos);
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 14 */     return IntStream.range(0, count(random, origin)).mapToObj(i -> origin);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\RepeatingPlacement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */