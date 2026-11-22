/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.chat.GuiMessage;
/*     */ import net.minecraft.client.multiplayer.chat.GuiMessageTag;
/*     */ 
/*     */ class null implements ChatComponent.LineConsumer {
/*     */   private boolean hoveredOverCurrentMessage;
/*     */   
/*     */   public void accept(GuiMessage.Line line, int lineIndex, float alpha) {
/*     */     boolean forceIconRendering;
/* 205 */     int entryBottom = chatBottom - lineIndex * entryHeight;
/* 206 */     int entryTop = entryBottom - entryHeight;
/* 208 */     int textTop = entryBottom - entryBottomToMessageY;
/* 209 */     boolean hoveredOverCurrentLine = graphics.handleMessage(textTop, alpha * textOpacity, line.content());
/* 210 */     this.hoveredOverCurrentMessage |= hoveredOverCurrentLine;
/* 215 */     if (line.endOfEntry()) {
/* 216 */       forceIconRendering = this.hoveredOverCurrentMessage;
/* 217 */       this.hoveredOverCurrentMessage = false;
/*     */     } else {
/* 219 */       forceIconRendering = false;
/*     */     } 
/* 222 */     GuiMessageTag tag = line.tag();
/* 223 */     if (tag != null) {
/* 225 */       graphics.handleTag(-4, entryTop, -2, entryBottom, alpha * textOpacity, tag);
/* 226 */       if (tag.icon() != null) {
/* 227 */         int iconLeft = line.getTagIconLeft(ChatComponent.this.minecraft.font);
/* 228 */         int textBottom = textTop + messageHeight;
/* 230 */         graphics.handleTagIcon(iconLeft, textBottom, forceIconRendering, tag, tag.icon());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ChatComponent$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */