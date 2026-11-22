/*    */ package com.mojang.realmsclient.gui.screens;
/*    */ 
/*    */ import com.mojang.realmsclient.client.RealmsError;
/*    */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.gui.ActiveTextCollector;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.TextAlignment;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.MultiLineLabel;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.realms.RealmsScreen;
/*    */ 
/*    */ public class RealmsGenericErrorScreen extends RealmsScreen {
/* 19 */   private static final Component GENERIC_TITLE = (Component)Component.translatable("mco.errorMessage.generic");
/*    */   
/*    */   private final Screen nextScreen;
/*    */   
/*    */   private final Component detail;
/*    */   
/* 24 */   private MultiLineLabel splitDetail = MultiLineLabel.EMPTY;
/*    */   
/*    */   public RealmsGenericErrorScreen(RealmsServiceException realmsServiceException, Screen nextScreen) {
/* 27 */     this(ErrorMessage.forServiceError(realmsServiceException), nextScreen);
/*    */   }
/*    */   
/*    */   public RealmsGenericErrorScreen(Component message, Screen nextScreen) {
/* 31 */     this(new ErrorMessage(GENERIC_TITLE, message), nextScreen);
/*    */   }
/*    */   
/*    */   public RealmsGenericErrorScreen(Component title, Component message, Screen nextScreen) {
/* 35 */     this(new ErrorMessage(title, message), nextScreen);
/*    */   }
/*    */   
/*    */   private RealmsGenericErrorScreen(ErrorMessage message, Screen nextScreen) {
/* 39 */     super(message.title);
/* 40 */     this.nextScreen = nextScreen;
/* 41 */     this.detail = ComponentUtils.mergeStyles(message.detail, Style.EMPTY.withColor(-2142128));
/*    */   }
/*    */   
/*    */   public void init() {
/* 46 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_OK, button -> onClose()).bounds(this.width / 2 - 100, this.height - 52, 200, 20).build());
/* 47 */     this.splitDetail = MultiLineLabel.create(this.font, this.detail, this.width * 3 / 4);
/*    */   }
/*    */   
/*    */   public void onClose() {
/* 52 */     this.minecraft.gui.setScreen(this.nextScreen);
/*    */   }
/*    */   
/*    */   public Component getNarrationMessage() {
/* 57 */     return (Component)CommonComponents.joinForNarration(new Component[] { super.getNarrationMessage(), this.detail });
/*    */   }
/*    */   
/*    */   public void extractRenderState(GuiGraphicsExtractor graphics, int xm, int ym, float a) {
/* 62 */     super.extractRenderState(graphics, xm, ym, a);
/* 64 */     graphics.centeredText(this.font, this.title, this.width / 2, 80, -1);
/* 65 */     ActiveTextCollector textRenderer = graphics.textRenderer();
/* 66 */     Objects.requireNonNull(this.minecraft.font);
/* 66 */     this.splitDetail.visitLines(TextAlignment.CENTER, this.width / 2, 100, 9, textRenderer);
/*    */   }
/*    */   
/*    */   private static final class ErrorMessage extends Record {
/*    */     private final Component title;
/*    */     
/*    */     private final Component detail;
/*    */     
/*    */     private ErrorMessage(Component title, Component detail) {
/* 69 */       this.title = title;
/* 69 */       this.detail = detail;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/gui/screens/RealmsGenericErrorScreen$ErrorMessage;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #69	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/screens/RealmsGenericErrorScreen$ErrorMessage;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/gui/screens/RealmsGenericErrorScreen$ErrorMessage;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #69	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/screens/RealmsGenericErrorScreen$ErrorMessage;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/gui/screens/RealmsGenericErrorScreen$ErrorMessage;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #69	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/realmsclient/gui/screens/RealmsGenericErrorScreen$ErrorMessage;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Component title() {
/* 69 */       return this.title;
/*    */     }
/*    */     
/*    */     public Component detail() {
/* 69 */       return this.detail;
/*    */     }
/*    */     
/*    */     private static ErrorMessage forServiceError(RealmsServiceException realmsServiceException) {
/* 71 */       RealmsError errorDetails = realmsServiceException.realmsError;
/* 72 */       return new ErrorMessage((Component)Component.translatable("mco.errorMessage.realmsService.realmsError", new Object[] { Integer.valueOf(errorDetails.errorCode()) }), errorDetails.errorMessage());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsGenericErrorScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */