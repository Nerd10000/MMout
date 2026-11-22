/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.Style;
/*     */ 
/*     */ public abstract class WithInactiveMessage extends AbstractWidget {
/*     */   private Component inactiveMessage;
/*     */   
/*     */   public static Component defaultInactiveMessage(Component activeMessage) {
/* 329 */     return ComponentUtils.mergeStyles(activeMessage, Style.EMPTY.withColor(-6250336));
/*     */   }
/*     */   
/*     */   public WithInactiveMessage(int x, int y, int width, int height, Component message) {
/* 333 */     super(x, y, width, height, message);
/* 334 */     this.inactiveMessage = defaultInactiveMessage(message);
/*     */   }
/*     */   
/*     */   public Component getMessage() {
/* 339 */     return this.active ? super.getMessage() : this.inactiveMessage;
/*     */   }
/*     */   
/*     */   public void setMessage(Component message) {
/* 344 */     super.setMessage(message);
/* 345 */     this.inactiveMessage = defaultInactiveMessage(message);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\AbstractWidget$WithInactiveMessage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */