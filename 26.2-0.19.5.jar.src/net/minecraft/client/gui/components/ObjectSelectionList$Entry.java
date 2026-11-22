/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.gui.narration.NarratedElementType;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.client.gui.narration.NarrationSupplier;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public abstract class Entry<E extends ObjectSelectionList.Entry<E>> extends AbstractSelectionList.Entry<E> implements NarrationSupplier {
/*    */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 74 */     return true;
/*    */   }
/*    */   
/*    */   public void updateNarration(NarrationElementOutput output) {
/* 79 */     output.add(NarratedElementType.TITLE, getNarration());
/*    */   }
/*    */   
/*    */   public abstract Component getNarration();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ObjectSelectionList$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */