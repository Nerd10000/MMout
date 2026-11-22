/*     */ package com.mojang.realmsclient.dto;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.realmsclient.util.JsonUtils;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.PopupScreen;
/*     */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class InfoPopup extends RealmsNotification {
/*     */   private static final String TITLE = "title";
/*     */   
/*     */   private static final String MESSAGE = "message";
/*     */   
/*     */   private static final String IMAGE = "image";
/*     */   
/*     */   private static final String URL_BUTTON = "urlButton";
/*     */   
/*     */   private final RealmsText title;
/*     */   
/*     */   private final RealmsText message;
/*     */   
/*     */   private final Identifier image;
/*     */   
/*     */   private final RealmsNotification.UrlButton urlButton;
/*     */   
/*     */   private InfoPopup(RealmsNotification base, RealmsText title, RealmsText message, Identifier image, RealmsNotification.UrlButton urlButton) {
/* 138 */     super(base.uuid, base.dismissable, base.seen, base.type);
/* 139 */     this.title = title;
/* 140 */     this.message = message;
/* 141 */     this.image = image;
/* 142 */     this.urlButton = urlButton;
/*     */   }
/*     */   
/*     */   public static InfoPopup parse(RealmsNotification base, JsonObject object) {
/* 146 */     RealmsText title = (RealmsText)JsonUtils.getRequired("title", object, RealmsText::parse);
/* 147 */     RealmsText message = (RealmsText)JsonUtils.getRequired("message", object, RealmsText::parse);
/* 148 */     Identifier image = Identifier.parse(JsonUtils.getRequiredString("image", object));
/* 149 */     RealmsNotification.UrlButton urlButton = (RealmsNotification.UrlButton)JsonUtils.getOptional("urlButton", object, RealmsNotification.UrlButton::parse);
/* 150 */     return new InfoPopup(base, title, message, image, urlButton);
/*     */   }
/*     */   
/*     */   public PopupScreen buildScreen(Screen parentScreen, Consumer<UUID> dismiss) {
/* 154 */     Component title = this.title.createComponent();
/* 155 */     if (title == null) {
/* 156 */       RealmsNotification.LOGGER.warn("Realms info popup had title with no available translation: {}", this.title);
/* 157 */       return null;
/*     */     } 
/* 161 */     PopupScreen.Builder builder = (new PopupScreen.Builder(parentScreen, title)).setImage(this.image).addMessage(this.message.createComponent(CommonComponents.EMPTY));
/* 162 */     if (this.urlButton != null)
/* 163 */       builder.addButton(this.urlButton.urlText.createComponent(RealmsNotification.BUTTON_TEXT_FALLBACK), popup -> {
/*     */             Minecraft minecraft = Minecraft.getInstance();
/*     */             minecraft.gui.setScreen((Screen)new ConfirmLinkScreen((), this.urlButton.url, true));
/*     */             dismiss.accept(uuid());
/*     */           }); 
/* 176 */     builder.addButton(CommonComponents.GUI_OK, popup -> {
/*     */           popup.onClose();
/*     */           dismiss.accept(uuid());
/*     */         });
/* 180 */     builder.onClose(() -> dismiss.accept(uuid()));
/* 181 */     return builder.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RealmsNotification$InfoPopup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */