/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ class null extends EditBox {
/*     */   null(Font font, int x, int y, int width, int height, Component narration) {
/* 176 */     super(font, x, y, width, height, narration);
/*     */   }
/*     */   
/*     */   public boolean charTyped(CharacterEvent event) {
/* 180 */     if (!StructureBlockEditScreen.access$000(StructureBlockEditScreen.this, getValue(), event.codepoint(), getCursorPosition()))
/* 181 */       return false; 
/* 183 */     return super.charTyped(event);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\StructureBlockEditScreen$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */