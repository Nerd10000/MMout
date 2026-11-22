/*    */ package com.mojang.realmsclient.gui.screens;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.client.RealmsClient;
/*    */ import com.mojang.realmsclient.dto.RealmsServer;
/*    */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*    */ import com.mojang.realmsclient.util.task.GetServerDetailsTask;
/*    */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.input.KeyEvent;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.realms.RealmsScreen;
/*    */ import net.minecraft.util.CommonLinks;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class RealmsTermsScreen extends RealmsScreen {
/* 23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 24 */   private static final Component TITLE = (Component)Component.translatable("mco.terms.title");
/*    */   
/* 25 */   private static final Component TERMS_STATIC_TEXT = (Component)Component.translatable("mco.terms.sentence.1");
/*    */   
/* 26 */   private static final Component TERMS_LINK_TEXT = (Component)CommonComponents.space().append((Component)Component.translatable("mco.terms.sentence.2").withStyle(Style.EMPTY.withUnderlined(Boolean.valueOf(true))));
/*    */   
/*    */   private final Screen lastScreen;
/*    */   
/*    */   private final RealmsServer realmsServer;
/*    */   
/*    */   private boolean onLink;
/*    */   
/*    */   public RealmsTermsScreen(Screen lastScreen, RealmsServer realmsServer) {
/* 34 */     super(TITLE);
/* 35 */     this.lastScreen = lastScreen;
/* 36 */     this.realmsServer = realmsServer;
/*    */   }
/*    */   
/*    */   public void init() {
/* 41 */     int columnWidth = this.width / 4 - 2;
/* 43 */     addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable("mco.terms.buttons.agree"), button -> agreedToTos()).bounds(this.width / 4, row(12), columnWidth, 20).build());
/* 44 */     addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable("mco.terms.buttons.disagree"), button -> this.minecraft.gui.setScreen(this.lastScreen)).bounds(this.width / 2 + 4, row(12), columnWidth, 20).build());
/*    */   }
/*    */   
/*    */   public boolean keyPressed(KeyEvent event) {
/* 49 */     if (event.isEscape()) {
/* 50 */       this.minecraft.gui.setScreen(this.lastScreen);
/* 51 */       return true;
/*    */     } 
/* 53 */     return super.keyPressed(event);
/*    */   }
/*    */   
/*    */   private void agreedToTos() {
/* 57 */     RealmsClient client = RealmsClient.getOrCreate();
/*    */     try {
/* 59 */       client.agreeToTos();
/* 60 */       this.minecraft.gui.setScreen((Screen)new RealmsLongRunningMcoTaskScreen(this.lastScreen, new LongRunningTask[] { (LongRunningTask)new GetServerDetailsTask(this.lastScreen, this.realmsServer) }));
/* 61 */     } catch (RealmsServiceException e) {
/* 62 */       LOGGER.error("Couldn't agree to TOS", (Throwable)e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 68 */     if (this.onLink) {
/* 69 */       this.minecraft.keyboardHandler.setClipboard(CommonLinks.REALMS_TERMS.toString());
/* 70 */       Util.getPlatform().openUri(CommonLinks.REALMS_TERMS);
/* 71 */       return true;
/*    */     } 
/* 74 */     return super.mouseClicked(event, doubleClick);
/*    */   }
/*    */   
/*    */   public Component getNarrationMessage() {
/* 79 */     return (Component)CommonComponents.joinForNarration(new Component[] { super.getNarrationMessage(), TERMS_STATIC_TEXT }).append(CommonComponents.SPACE).append(TERMS_LINK_TEXT);
/*    */   }
/*    */   
/*    */   public void extractRenderState(GuiGraphicsExtractor graphics, int xm, int ym, float a) {
/* 84 */     super.extractRenderState(graphics, xm, ym, a);
/* 86 */     graphics.centeredText(this.font, this.title, this.width / 2, 17, -1);
/* 87 */     graphics.text(this.font, TERMS_STATIC_TEXT, this.width / 2 - 120, row(5), -1);
/* 88 */     int firstPartWidth = this.font.width((FormattedText)TERMS_STATIC_TEXT);
/* 90 */     int x1 = this.width / 2 - 121 + firstPartWidth;
/* 91 */     int y1 = row(5);
/* 92 */     int x2 = x1 + this.font.width((FormattedText)TERMS_LINK_TEXT) + 1;
/* 93 */     Objects.requireNonNull(this.font);
/* 93 */     int y2 = y1 + 1 + 9;
/* 95 */     this.onLink = (x1 <= xm && xm <= x2 && y1 <= ym && ym <= y2);
/* 96 */     graphics.text(this.font, TERMS_LINK_TEXT, this.width / 2 - 120 + firstPartWidth, row(5), this.onLink ? -9670204 : -13408581);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsTermsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */