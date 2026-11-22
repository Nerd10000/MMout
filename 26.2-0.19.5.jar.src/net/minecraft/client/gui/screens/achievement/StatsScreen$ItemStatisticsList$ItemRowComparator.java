/*     */ package net.minecraft.client.gui.screens.achievement;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ class ItemRowComparator implements Comparator<StatsScreen.ItemStatisticsList.ItemRow> {
/*     */   public int compare(StatsScreen.ItemStatisticsList.ItemRow one, StatsScreen.ItemStatisticsList.ItemRow two) {
/*     */     int key1, key2;
/* 388 */     Item item1 = one.getItem();
/* 389 */     Item item2 = two.getItem();
/* 393 */     if (StatsScreen.ItemStatisticsList.this.sortColumn == null) {
/* 394 */       key1 = 0;
/* 395 */       key2 = 0;
/* 396 */     } else if (StatsScreen.ItemStatisticsList.this.blockColumns.contains(StatsScreen.ItemStatisticsList.this.sortColumn)) {
/* 397 */       StatType<Block> type = (StatType)StatsScreen.ItemStatisticsList.this.sortColumn;
/* 398 */       BlockItem blockItem = (BlockItem)item1;
/* 398 */       key1 = (item1 instanceof BlockItem) ? this.this$1.this$0.stats.getValue(type, blockItem.getBlock()) : -1;
/* 399 */       blockItem = (BlockItem)item2;
/* 399 */       key2 = (item2 instanceof BlockItem) ? this.this$1.this$0.stats.getValue(type, blockItem.getBlock()) : -1;
/*     */     } else {
/* 401 */       StatType<Item> type = (StatType)StatsScreen.ItemStatisticsList.this.sortColumn;
/* 402 */       key1 = this.this$1.this$0.stats.getValue(type, item1);
/* 403 */       key2 = this.this$1.this$0.stats.getValue(type, item2);
/*     */     } 
/* 406 */     if (key1 == key2)
/* 407 */       return StatsScreen.ItemStatisticsList.this.sortOrder * Integer.compare(Item.getId(item1), Item.getId(item2)); 
/* 410 */     return StatsScreen.ItemStatisticsList.this.sortOrder * Integer.compare(key1, key2);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\achievement\StatsScreen$ItemStatisticsList$ItemRowComparator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */