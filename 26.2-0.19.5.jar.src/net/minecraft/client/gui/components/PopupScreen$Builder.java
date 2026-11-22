/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class Builder {
/*     */   private final Screen backgroundScreen;
/*     */   
/*     */   private final Component title;
/*     */   
/* 122 */   private final List<Component> messages = new ArrayList<>();
/*     */   
/* 123 */   private int width = 250;
/*     */   
/*     */   private Identifier image;
/*     */   
/* 125 */   private final List<PopupScreen.ButtonOption> buttons = new ArrayList<>();
/*     */   
/* 126 */   private Runnable onClose = null;
/*     */   
/*     */   public Builder(Screen backgroundScreen, Component title) {
/* 129 */     this.backgroundScreen = backgroundScreen;
/* 130 */     this.title = title;
/*     */   }
/*     */   
/*     */   public Builder setWidth(int width) {
/* 134 */     this.width = width;
/* 135 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setImage(Identifier image) {
/* 139 */     this.image = image;
/* 140 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addMessage(Component message) {
/* 144 */     this.messages.add(message);
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addButton(Component message, Consumer<PopupScreen> action) {
/* 149 */     this.buttons.add(new PopupScreen.ButtonOption(message, action));
/* 150 */     return this;
/*     */   }
/*     */   
/*     */   public Builder onClose(Runnable onClose) {
/* 154 */     this.onClose = onClose;
/* 155 */     return this;
/*     */   }
/*     */   
/*     */   public PopupScreen build() {
/* 159 */     if (this.buttons.isEmpty())
/* 160 */       throw new IllegalStateException("Popup must have at least one button"); 
/* 162 */     return new PopupScreen(this.backgroundScreen, this.width, this.image, this.title, this.messages, List.copyOf(this.buttons), this.onClose);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\PopupScreen$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */