/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ class null implements Container {
/*     */   public int getContainerSize() {
/*  44 */     return 1;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  49 */     return LecternBlockEntity.this.book.isEmpty();
/*     */   }
/*     */   
/*     */   public ItemStack getItem(int slot) {
/*  54 */     return (slot == 0) ? LecternBlockEntity.this.book : ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/*  59 */     if (slot == 0) {
/*  60 */       ItemStack result = LecternBlockEntity.this.book.split(count);
/*  61 */       if (LecternBlockEntity.this.book.isEmpty())
/*  62 */         LecternBlockEntity.this.onBookItemRemove(); 
/*  64 */       return result;
/*     */     } 
/*  66 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int slot) {
/*  71 */     if (slot == 0) {
/*  72 */       ItemStack prev = LecternBlockEntity.this.book;
/*  73 */       LecternBlockEntity.this.book = ItemStack.EMPTY;
/*  74 */       LecternBlockEntity.this.onBookItemRemove();
/*  75 */       return prev;
/*     */     } 
/*  77 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {}
/*     */   
/*     */   public int getMaxStackSize() {
/*  87 */     return 1;
/*     */   }
/*     */   
/*     */   public void setChanged() {
/*  92 */     LecternBlockEntity.this.setChanged();
/*     */   }
/*     */   
/*     */   public boolean stillValid(Player player) {
/*  97 */     return (Container.stillValidBlockEntity(LecternBlockEntity.this, player) && LecternBlockEntity.this.hasBook());
/*     */   }
/*     */   
/*     */   public boolean canPlaceItem(int slot, ItemStack itemStack) {
/* 102 */     return false;
/*     */   }
/*     */   
/*     */   public void clearContent() {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\LecternBlockEntity$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */