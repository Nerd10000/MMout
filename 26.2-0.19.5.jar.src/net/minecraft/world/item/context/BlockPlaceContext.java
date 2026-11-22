/*    */ package net.minecraft.world.item.context;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BlockPlaceContext extends UseOnContext {
/*    */   private final BlockPos relativePos;
/*    */   
/*    */   protected boolean replaceClicked = true;
/*    */   
/*    */   public BlockPlaceContext(Player player, InteractionHand hand, ItemStack itemInHand, BlockHitResult hitResult) {
/* 20 */     this(player.level(), player, hand, itemInHand, hitResult);
/*    */   }
/*    */   
/*    */   public BlockPlaceContext(UseOnContext context) {
/* 24 */     this(context.getLevel(), context.getPlayer(), context.getHand(), context.getItemInHand(), context.getHitResult());
/*    */   }
/*    */   
/*    */   protected BlockPlaceContext(Level level, Player player, InteractionHand hand, ItemStack itemStackInHand, BlockHitResult hitResult) {
/* 28 */     super(level, player, hand, itemStackInHand, hitResult);
/* 30 */     this.relativePos = hitResult.getBlockPos().relative(hitResult.getDirection());
/* 31 */     this.replaceClicked = level.getBlockState(hitResult.getBlockPos()).canBeReplaced(this);
/*    */   }
/*    */   
/*    */   public static BlockPlaceContext at(BlockPlaceContext context, BlockPos pos, Direction direction) {
/* 35 */     return new BlockPlaceContext(context
/* 36 */         .getLevel(), context
/* 37 */         .getPlayer(), context
/* 38 */         .getHand(), context
/* 39 */         .getItemInHand(), new BlockHitResult(new Vec3(pos
/*    */             
/* 42 */             .getX() + 0.5D + direction.getStepX() * 0.5D, pos
/* 43 */             .getY() + 0.5D + direction.getStepY() * 0.5D, pos
/* 44 */             .getZ() + 0.5D + direction.getStepZ() * 0.5D), direction, pos, false));
/*    */   }
/*    */   
/*    */   public BlockPos getClickedPos() {
/* 55 */     return this.replaceClicked ? super.getClickedPos() : this.relativePos;
/*    */   }
/*    */   
/*    */   public boolean canPlace() {
/* 59 */     return (this.replaceClicked || getLevel().getBlockState(getClickedPos()).canBeReplaced(this));
/*    */   }
/*    */   
/*    */   public boolean replacingClickedOnBlock() {
/* 63 */     return this.replaceClicked;
/*    */   }
/*    */   
/*    */   public Direction getNearestLookingDirection() {
/* 67 */     return Direction.orderedByNearest((Entity)getPlayer())[0];
/*    */   }
/*    */   
/*    */   public Direction getNearestLookingVerticalDirection() {
/* 71 */     return Direction.getFacingAxis((Entity)getPlayer(), Direction.Axis.Y);
/*    */   }
/*    */   
/*    */   public Direction[] getNearestLookingDirections() {
/* 75 */     Direction[] directions = Direction.orderedByNearest((Entity)getPlayer());
/* 77 */     if (this.replaceClicked)
/* 78 */       return directions; 
/* 81 */     Direction clickedFace = getClickedFace();
/* 84 */     int index = 0;
/* 85 */     for (; index < directions.length && 
/* 86 */       directions[index] != clickedFace.getOpposite(); index++);
/* 91 */     if (index > 0) {
/* 92 */       System.arraycopy(directions, 0, directions, 1, index);
/* 93 */       directions[0] = clickedFace.getOpposite();
/*    */     } 
/* 95 */     return directions;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\context\BlockPlaceContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */