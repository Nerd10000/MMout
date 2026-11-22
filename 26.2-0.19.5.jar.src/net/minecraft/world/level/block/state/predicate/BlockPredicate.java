/*    */ package net.minecraft.world.level.block.state.predicate;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockPredicate implements Predicate<BlockState> {
/*    */   private final Block block;
/*    */   
/*    */   public BlockPredicate(Block block) {
/* 13 */     this.block = block;
/*    */   }
/*    */   
/*    */   public static BlockPredicate forBlock(Block block) {
/* 17 */     return new BlockPredicate(block);
/*    */   }
/*    */   
/*    */   public boolean test(BlockState input) {
/* 22 */     return (input != null && input.is(this.block));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\predicate\BlockPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */