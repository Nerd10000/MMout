/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.TextAlignment;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ 
/*     */ public interface MultiLineLabel {
/*  17 */   public static final MultiLineLabel EMPTY = new MultiLineLabel() {
/*     */       public int visitLines(TextAlignment align, int anchorX, int topY, int lineHeight, ActiveTextCollector output) {
/*  20 */         return topY;
/*     */       }
/*     */       
/*     */       public int getLineCount() {
/*  25 */         return 0;
/*     */       }
/*     */       
/*     */       public int getWidth() {
/*  30 */         return 0;
/*     */       }
/*     */     };
/*     */   
/*     */   static MultiLineLabel create(Font font, Component... messages) {
/*  35 */     return create(font, 2147483647, 2147483647, messages);
/*     */   }
/*     */   
/*     */   static MultiLineLabel create(Font font, int maxWidth, Component... messages) {
/*  39 */     return create(font, maxWidth, 2147483647, messages);
/*     */   }
/*     */   
/*     */   static MultiLineLabel create(Font font, Component message, int maxWidth) {
/*  43 */     return create(font, maxWidth, 2147483647, new Component[] { message });
/*     */   }
/*     */   
/*     */   static MultiLineLabel create(final Font font, final int maxWidth, final int maxLines, Component... messages) {
/*  47 */     if (messages.length == 0)
/*  48 */       return EMPTY; 
/*  51 */     return new MultiLineLabel() {
/*     */         private List<MultiLineLabel.TextAndWidth> cachedTextAndWidth;
/*     */         
/*     */         private Language splitWithLanguage;
/*     */         
/*     */         public int visitLines(TextAlignment align, int anchorX, int topY, int lineHeight, ActiveTextCollector output) {
/*  57 */           int y = topY;
/*  58 */           for (MultiLineLabel.TextAndWidth splitLine : getSplitMessage()) {
/*  59 */             int leftX = align.calculateLeft(anchorX, splitLine.width);
/*  60 */             output.accept(leftX, y, splitLine.text);
/*  61 */             y += lineHeight;
/*     */           } 
/*  63 */           return y;
/*     */         }
/*     */         
/*     */         private List<MultiLineLabel.TextAndWidth> getSplitMessage() {
/*  67 */           Language currentLanguage = Language.getInstance();
/*  68 */           if (this.cachedTextAndWidth != null && currentLanguage == this.splitWithLanguage)
/*  69 */             return this.cachedTextAndWidth; 
/*  71 */           this.splitWithLanguage = currentLanguage;
/*  72 */           List<FormattedText> splitMessage = new ArrayList<>();
/*  73 */           for (Component message : messages)
/*  74 */             splitMessage.addAll(font.splitIgnoringLanguage((FormattedText)message, maxWidth)); 
/*  76 */           this.cachedTextAndWidth = new ArrayList<>();
/*  78 */           int actualMaxLines = Math.min(splitMessage.size(), maxLines);
/*  79 */           List<FormattedText> linesToAdd = splitMessage.subList(0, actualMaxLines);
/*  80 */           for (int i = 0; i < linesToAdd.size(); i++) {
/*  81 */             FormattedText formattedText = linesToAdd.get(i);
/*  82 */             FormattedCharSequence formattedCharSequence = Language.getInstance().getVisualOrder(formattedText);
/*  83 */             if (i == linesToAdd.size() - 1 && actualMaxLines == maxLines && actualMaxLines != splitMessage.size()) {
/*  84 */               FormattedText clippedText = font.substrByWidth(formattedText, font.width(formattedText) - font.width((FormattedText)CommonComponents.ELLIPSIS));
/*  85 */               FormattedText withEllipsis = FormattedText.composite(new FormattedText[] { clippedText, (FormattedText)CommonComponents.ELLIPSIS.copy().withStyle(this.val$messages[this.val$messages.length - 1].getStyle()) });
/*  86 */               this.cachedTextAndWidth.add(new MultiLineLabel.TextAndWidth(Language.getInstance().getVisualOrder(withEllipsis), font.width(withEllipsis)));
/*     */             } else {
/*  88 */               this.cachedTextAndWidth.add(new MultiLineLabel.TextAndWidth(formattedCharSequence, font.width(formattedCharSequence)));
/*     */             } 
/*     */           } 
/*  91 */           return this.cachedTextAndWidth;
/*     */         }
/*     */         
/*     */         public int getLineCount() {
/*  96 */           return getSplitMessage().size();
/*     */         }
/*     */         
/*     */         public int getWidth() {
/* 101 */           return Math.min(maxWidth, getSplitMessage().stream().mapToInt(MultiLineLabel.TextAndWidth::width).max().orElse(0));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   int visitLines(TextAlignment paramTextAlignment, int paramInt1, int paramInt2, int paramInt3, ActiveTextCollector paramActiveTextCollector);
/*     */   
/*     */   int getLineCount();
/*     */   
/*     */   int getWidth();
/*     */   
/*     */   public static final class TextAndWidth extends Record {
/*     */     private final FormattedCharSequence text;
/*     */     
/*     */     private final int width;
/*     */     
/*     */     public TextAndWidth(FormattedCharSequence text, int width) {
/* 112 */       this.text = text;
/* 112 */       this.width = width;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/MultiLineLabel$TextAndWidth;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #112	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/MultiLineLabel$TextAndWidth;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/MultiLineLabel$TextAndWidth;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #112	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/MultiLineLabel$TextAndWidth;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/MultiLineLabel$TextAndWidth;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #112	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/components/MultiLineLabel$TextAndWidth;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public FormattedCharSequence text() {
/* 112 */       return this.text;
/*     */     }
/*     */     
/*     */     public int width() {
/* 112 */       return this.width;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\MultiLineLabel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */