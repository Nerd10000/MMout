/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.ItemContainerContents;
/*    */ 
/*    */ class null implements ContainerComponentManipulator<ItemContainerContents> {
/*    */   public DataComponentType<ItemContainerContents> type() {
/* 21 */     return DataComponents.CONTAINER;
/*    */   }
/*    */   
/*    */   public Stream<ItemStack> getContents(ItemContainerContents component) {
/* 26 */     return component.allItemsCopyStream();
/*    */   }
/*    */   
/*    */   public ItemContainerContents empty() {
/* 31 */     return ItemContainerContents.EMPTY;
/*    */   }
/*    */   
/*    */   public ItemContainerContents setContents(ItemContainerContents component, Stream<ItemStack> newContents) {
/* 36 */     return ItemContainerContents.fromItems(newContents.toList());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\ContainerComponentManipulators$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */