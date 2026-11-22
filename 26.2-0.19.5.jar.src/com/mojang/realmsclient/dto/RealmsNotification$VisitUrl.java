/*     */ package com.mojang.realmsclient.dto;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.realmsclient.util.JsonUtils;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class VisitUrl extends RealmsNotification {
/*     */   private static final String URL = "url";
/*     */   
/*     */   private static final String BUTTON_TEXT = "buttonText";
/*     */   
/*     */   private static final String MESSAGE = "message";
/*     */   
/*     */   private final String url;
/*     */   
/*     */   private final RealmsText buttonText;
/*     */   
/*     */   private final RealmsText message;
/*     */   
/*     */   private VisitUrl(RealmsNotification base, String url, RealmsText buttonText, RealmsText message) {
/* 102 */     super(base.uuid, base.dismissable, base.seen, base.type);
/* 103 */     this.url = url;
/* 104 */     this.buttonText = buttonText;
/* 105 */     this.message = message;
/*     */   }
/*     */   
/*     */   public static VisitUrl parse(RealmsNotification base, JsonObject jsonObject) {
/* 109 */     String url = JsonUtils.getRequiredString("url", jsonObject);
/* 110 */     RealmsText buttonText = (RealmsText)JsonUtils.getRequired("buttonText", jsonObject, RealmsText::parse);
/* 111 */     RealmsText message = (RealmsText)JsonUtils.getRequired("message", jsonObject, RealmsText::parse);
/* 112 */     return new VisitUrl(base, url, buttonText, message);
/*     */   }
/*     */   
/*     */   public Component getMessage() {
/* 116 */     return this.message.createComponent((Component)Component.translatable("mco.notification.visitUrl.message.default"));
/*     */   }
/*     */   
/*     */   public Button buildOpenLinkButton(Screen parentScreen) {
/* 120 */     Component buttonLabel = this.buttonText.createComponent(RealmsNotification.BUTTON_TEXT_FALLBACK);
/* 121 */     return Button.builder(buttonLabel, ConfirmLinkScreen.confirmLink(parentScreen, this.url)).build();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RealmsNotification$VisitUrl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */