/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ public enum ChatMethod {
/* 547 */   MESSAGE("") {
/*     */     public boolean isDraftRestorable(ChatComponent.Draft draft) {
/* 550 */       return true;
/*     */     }
/*     */   },
/* 553 */   COMMAND("/") {
/*     */     public boolean isDraftRestorable(ChatComponent.Draft draft) {
/* 556 */       return (this == draft.chatMethod);
/*     */     }
/*     */   };
/*     */   
/*     */   private final String prefix;
/*     */   
/*     */   ChatMethod(String prefix) {
/* 563 */     this.prefix = prefix;
/*     */   }
/*     */   
/*     */   public String prefix() {
/* 567 */     return this.prefix;
/*     */   }
/*     */   
/*     */   public abstract boolean isDraftRestorable(ChatComponent.Draft paramDraft);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ChatComponent$ChatMethod.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */