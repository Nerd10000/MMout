/*    */ package net.minecraft.client.gui.screens.friends;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.components.EditBox;
/*    */ import net.minecraft.client.input.InputWithModifiers;
/*    */ import net.minecraft.client.input.KeyEvent;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ class null extends EditBox {
/*    */   null(Font font, int width, int height, Component narration) {
/* 71 */     super(font, width, height, narration);
/*    */   }
/*    */   
/*    */   public boolean keyPressed(KeyEvent event) {
/* 74 */     boolean enterPressed = (event.key() == 257 || event.key() == 335);
/* 75 */     boolean elementsActive = (isActive() && AddFriendWidget.this.addButton.active);
/* 76 */     if (elementsActive && isFocused() && enterPressed) {
/* 77 */       AddFriendWidget.this.addButton.playDownSound(AddFriendWidget.this.minecraft.getSoundManager());
/* 78 */       AddFriendWidget.this.addButton.onPress((InputWithModifiers)event);
/* 79 */       return true;
/*    */     } 
/* 81 */     return super.keyPressed(event);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\friends\AddFriendWidget$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */