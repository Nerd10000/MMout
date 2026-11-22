/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.WorldlyContainer;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ class InputContainer extends SimpleContainer implements WorldlyContainer {
/*     */   private final BlockState state;
/*     */   
/*     */   private final LevelAccessor level;
/*     */   
/*     */   private final BlockPos pos;
/*     */   
/*     */   private boolean changed;
/*     */   
/*     */   public InputContainer(BlockState state, LevelAccessor level, BlockPos pos) {
/* 443 */     super(1);
/* 444 */     this.state = state;
/* 445 */     this.level = level;
/* 446 */     this.pos = pos;
/*     */   }
/*     */   
/*     */   public int getMaxStackSize() {
/* 451 */     return 1;
/*     */   }
/*     */   
/*     */   public int[] getSlotsForFace(Direction direction) {
/* 456 */     (new int[1])[0] = 0;
/* 456 */     return (direction == Direction.UP) ? new int[1] : new int[0];
/*     */   }
/*     */   
/*     */   public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
/* 461 */     return (!this.changed && direction == Direction.UP && ComposterBlock.COMPOSTABLES.containsKey(itemStack.getItem()));
/*     */   }
/*     */   
/*     */   public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
/* 466 */     return false;
/*     */   }
/*     */   
/*     */   public void setChanged() {
/* 471 */     ItemStack contents = getItem(0);
/* 472 */     if (!contents.isEmpty()) {
/* 473 */       this.changed = true;
/* 474 */       BlockState newState = ComposterBlock.addItem(null, this.state, this.level, this.pos, contents);
/* 475 */       this.level.levelEvent(1500, this.pos, (newState != this.state) ? 1 : 0);
/* 476 */       removeItemNoUpdate(0);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\ComposterBlock$InputContainer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */