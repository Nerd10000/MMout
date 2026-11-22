/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public interface Strategy {
/*    */   @Deprecated
/*    */   public static final Strategy LEGACY_IRON_GOLEM;
/*    */   
/*    */   public static final Strategy ON_TOP_OF_COLLIDER;
/*    */   
/*    */   public static final Strategy ON_TOP_OF_COLLIDER_NO_LEAVES;
/*    */   
/*    */   static {
/* 58 */     LEGACY_IRON_GOLEM = ((level, pos, blockState, abovePos, aboveState) -> 
/* 59 */       (blockState.is(Blocks.COBWEB) || blockState.is(Blocks.CACTUS) || blockState.is(Blocks.GLASS_PANE) || blockState.getBlock() instanceof net.minecraft.world.level.block.StainedGlassPaneBlock || blockState.getBlock() instanceof net.minecraft.world.level.block.StainedGlassBlock || blockState.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock || blockState.is(Blocks.CONDUIT) || blockState.is(Blocks.ICE) || blockState.is(Blocks.TNT) || blockState.is(Blocks.GLOWSTONE) || blockState.is(Blocks.BEACON) || blockState.is(Blocks.SEA_LANTERN) || blockState.is(Blocks.FROSTED_ICE) || blockState.is(Blocks.TINTED_GLASS) || blockState.is(Blocks.GLASS)) ? false : (
/*    */       
/* 77 */       ((aboveState.isAir() || aboveState.liquid()) && (blockState.isSolid() || blockState.is(Blocks.POWDER_SNOW)))));
/* 80 */     ON_TOP_OF_COLLIDER = ((level, pos, blockState, abovePos, aboveState) -> 
/* 81 */       (aboveState.getCollisionShape((BlockGetter)level, abovePos).isEmpty() && Block.isFaceFull(blockState.getCollisionShape((BlockGetter)level, pos), Direction.UP)));
/* 83 */     ON_TOP_OF_COLLIDER_NO_LEAVES = ((level, pos, blockState, abovePos, aboveState) -> 
/* 84 */       (aboveState.getCollisionShape((BlockGetter)level, abovePos).isEmpty() && !blockState.is(BlockTags.LEAVES) && Block.isFaceFull(blockState.getCollisionShape((BlockGetter)level, pos), Direction.UP)));
/*    */   }
/*    */   
/*    */   boolean canSpawnOn(ServerLevel paramServerLevel, BlockPos paramBlockPos1, BlockState paramBlockState1, BlockPos paramBlockPos2, BlockState paramBlockState2);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\SpawnUtil$Strategy.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */