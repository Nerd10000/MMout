/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.WritableBookContent;
/*    */ import net.minecraft.world.item.component.WrittenBookContent;
/*    */ 
/*    */ public final class BookAccess extends Record {
/*    */   private final List<Component> pages;
/*    */   
/*    */   public BookAccess(List<Component> pages) {
/* 47 */     this.pages = pages;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/inventory/BookViewScreen$BookAccess;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/inventory/BookViewScreen$BookAccess;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/inventory/BookViewScreen$BookAccess;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/inventory/BookViewScreen$BookAccess;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/inventory/BookViewScreen$BookAccess;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/inventory/BookViewScreen$BookAccess;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<Component> pages() {
/* 47 */     return this.pages;
/*    */   }
/*    */   
/*    */   public int getPageCount() {
/* 49 */     return this.pages.size();
/*    */   }
/*    */   
/*    */   public Component getPage(int page) {
/* 53 */     if (page >= 0 && page < getPageCount())
/* 54 */       return this.pages.get(page); 
/* 56 */     return CommonComponents.EMPTY;
/*    */   }
/*    */   
/*    */   public static BookAccess fromItem(ItemStack itemStack) {
/* 60 */     boolean filterEnabled = Minecraft.getInstance().isTextFilteringEnabled();
/* 61 */     WrittenBookContent writtenContent = (WrittenBookContent)itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT);
/* 62 */     if (writtenContent != null)
/* 63 */       return new BookAccess(writtenContent.getPages(filterEnabled)); 
/* 65 */     WritableBookContent writableContent = (WritableBookContent)itemStack.get(DataComponents.WRITABLE_BOOK_CONTENT);
/* 66 */     if (writableContent != null)
/* 67 */       return new BookAccess(writableContent.getPages(filterEnabled).map(Component::literal).toList()); 
/* 69 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\BookViewScreen$BookAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */