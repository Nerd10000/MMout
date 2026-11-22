/*     */ package net.minecraft.client.gui.screens.reporting;
/*     */ 
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public abstract class Entry extends ObjectSelectionList.Entry<ChatSelectionScreen.ChatSelectionList.Entry> {
/*     */   public Component getNarration() {
/* 243 */     return CommonComponents.EMPTY;
/*     */   }
/*     */   
/*     */   public boolean isSelected() {
/* 247 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canSelect() {
/* 251 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canReport() {
/* 255 */     return canSelect();
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 260 */     return canSelect();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\reporting\ChatSelectionScreen$ChatSelectionList$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */