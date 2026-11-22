/*     */ package net.minecraft.client.gui.screens.friends;
/*     */ 
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.PlainTextButton;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ 
/*     */ class null extends PlainTextButton {
/*     */   null(AddFriendWidget this$0, int x, int y, int width, int height, Component message, Button.OnPress onPress, Font font) {
/* 122 */     super(x, y, width, height, message, onPress, font);
/*     */   }
/*     */   
/*     */   protected MutableComponent createNarrationMessage() {
/* 125 */     return wrapDefaultNarrationMessage(
/* 126 */         (Component)Component.translatable("gui.friends.my_profile_name.narration", new Object[] { this.val$profileNameComponent }));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\friends\AddFriendWidget$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */