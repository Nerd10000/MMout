/*    */ package net.minecraft.client.gui.narration;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.components.TabOrderedElement;
/*    */ 
/*    */ public interface NarratableEntry extends NarrationSupplier, TabOrderedElement {
/*    */   NarrationPriority narrationPriority();
/*    */   
/*    */   default boolean isActive() {
/* 12 */     return true;
/*    */   }
/*    */   
/*    */   default Collection<? extends NarratableEntry> getNarratables() {
/* 16 */     return List.of(this);
/*    */   }
/*    */   
/*    */   public enum NarrationPriority {
/* 21 */     NONE, HOVERED, FOCUSED;
/*    */     
/*    */     public boolean isTerminal() {
/* 27 */       return (this == FOCUSED);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\narration\NarratableEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */