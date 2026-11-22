/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.multiplayer.chat.GuiMessage;
/*     */ 
/*     */ public class State {
/*     */   private final List<GuiMessage> messages;
/*     */   
/*     */   private final List<String> history;
/*     */   
/*     */   private final List<ChatComponent.DelayedMessageDeletion> delayedMessageDeletions;
/*     */   
/*     */   public State(List<GuiMessage> messages, List<String> history, List<ChatComponent.DelayedMessageDeletion> delayedMessageDeletions) {
/* 537 */     this.messages = messages;
/* 538 */     this.history = history;
/* 539 */     this.delayedMessageDeletions = delayedMessageDeletions;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ChatComponent$State.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */