/*    */ package net.minecraft.commands.arguments.item;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponentPatch;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.world.item.Item;
/*    */ import org.apache.commons.lang3.mutable.MutableObject;
/*    */ 
/*    */ class null implements ItemParser.Visitor {
/*    */   null(ItemParser this$0) {}
/*    */   
/*    */   public void visitItem(Holder<Item> item) {
/* 67 */     itemResult.setValue(item);
/*    */   }
/*    */   
/*    */   public <T> void visitComponent(DataComponentType<T> type, T value) {
/* 72 */     componentsBuilder.set(type, value);
/*    */   }
/*    */   
/*    */   public <T> void visitRemovedComponent(DataComponentType<T> type) {
/* 77 */     componentsBuilder.remove(type);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\item\ItemParser$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */