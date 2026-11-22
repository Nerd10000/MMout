/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.components.EditBox;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ 
/*    */ class null extends EditBox {
/*    */   null(Font font, int x, int y, int width, int height, Component narration) {
/* 51 */     super(font, x, y, width, height, narration);
/*    */   }
/*    */   
/*    */   protected MutableComponent createNarrationMessage() {
/* 54 */     return super.createNarrationMessage().append(AbstractCommandBlockEditScreen.this.commandSuggestions.getNarrationMessage());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\AbstractCommandBlockEditScreen$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */