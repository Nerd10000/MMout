/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.WorldlyContainer;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ class EmptyContainer extends SimpleContainer implements WorldlyContainer {
/*     */   public EmptyContainer() {
/* 377 */     super(0);
/*     */   }
/*     */   
/*     */   public int[] getSlotsForFace(Direction direction) {
/* 382 */     return new int[0];
/*     */   }
/*     */   
/*     */   public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
/* 387 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
/* 392 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\ComposterBlock$EmptyContainer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */