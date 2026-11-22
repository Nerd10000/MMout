/*     */ package com.mojang.realmsclient;
/*     */ 
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ 
/*     */ class null extends Thread {
/*     */   null(String name) {
/* 556 */     super(name);
/*     */   }
/*     */   
/*     */   public void run() {
/*     */     try {
/* 560 */       RealmsClient client = RealmsClient.getOrCreate();
/* 561 */       client.uninviteMyselfFrom(server.id);
/* 562 */       RealmsMainScreen.access$000(RealmsMainScreen.this).execute(RealmsMainScreen::refreshServerList);
/* 563 */     } catch (RealmsServiceException e) {
/* 564 */       RealmsMainScreen.LOGGER.error("Couldn't configure world", (Throwable)e);
/* 565 */       RealmsMainScreen.access$200(RealmsMainScreen.this).execute(() -> (RealmsMainScreen.access$100(RealmsMainScreen.this)).gui.setScreen((Screen)new RealmsGenericErrorScreen(e, (Screen)RealmsMainScreen.this)));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\RealmsMainScreen$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */