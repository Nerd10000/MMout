/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.sounds.SoundManager;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ public class FocusableTextWidget extends MultiLineTextWidget {
/*     */   public static final int DEFAULT_PADDING = 4;
/*     */   
/*     */   private final int padding;
/*     */   
/*     */   private final int maxWidth;
/*     */   
/*     */   private final boolean alwaysShowBorder;
/*     */   
/*     */   private final BackgroundFill backgroundFill;
/*     */   
/*     */   private boolean narrateMessage = true;
/*     */   
/*     */   private Component focusedUsageNarration;
/*     */   
/*     */   private Component hoveredUsageNarration;
/*     */   
/*     */   private FocusableTextWidget(Component message, Font font, int padding, int maxWidth, BackgroundFill backgroundFill, boolean alwaysShowBorder) {
/*  29 */     super(message, font);
/*  30 */     this.active = true;
/*  31 */     this.padding = padding;
/*  32 */     this.maxWidth = maxWidth;
/*  33 */     this.alwaysShowBorder = alwaysShowBorder;
/*  34 */     this.backgroundFill = backgroundFill;
/*  35 */     updateWidth();
/*  36 */     updateHeight();
/*  37 */     setCentered(true);
/*     */   }
/*     */   
/*     */   protected void updateWidgetNarration(NarrationElementOutput output) {
/*  42 */     if (this.narrateMessage)
/*  43 */       output.add(NarratedElementType.TITLE, getMessage()); 
/*  45 */     if (this.active) {
/*  46 */       Component usage = isFocused() ? this.focusedUsageNarration : this.hoveredUsageNarration;
/*  47 */       if (usage != null)
/*  48 */         output.add(NarratedElementType.USAGE, usage); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setNarrateMessage(boolean narrateMessage) {
/*  54 */     this.narrateMessage = narrateMessage;
/*     */   }
/*     */   
/*     */   public void setUsageNarration(Component focused, Component hovered) {
/*  58 */     this.focusedUsageNarration = focused;
/*  59 */     this.hoveredUsageNarration = hovered;
/*     */   }
/*     */   
/*     */   public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  64 */     int borderColor = (this.alwaysShowBorder && !isFocused()) ? ARGB.color(this.alpha, -6250336) : ARGB.white(this.alpha);
/*  65 */     switch (this.backgroundFill.ordinal()) {
/*     */       case 0:
/*  66 */         graphics.fill(getX() + 1, getY(), getRight(), getBottom(), ARGB.black(this.alpha));
/*     */         break;
/*     */       case 1:
/*  68 */         if (isFocused())
/*  69 */           graphics.fill(getX() + 1, getY(), getRight(), getBottom(), ARGB.black(this.alpha)); 
/*     */         break;
/*     */     } 
/*  74 */     if (isFocused() || this.alwaysShowBorder)
/*  75 */       graphics.outline(getX(), getY(), getWidth(), getHeight(), borderColor); 
/*  77 */     super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   protected int getTextX() {
/*  82 */     return getX() + this.padding;
/*     */   }
/*     */   
/*     */   protected int getTextY() {
/*  87 */     return super.getTextY() + this.padding;
/*     */   }
/*     */   
/*     */   public MultiLineTextWidget setMaxWidth(int maxWidth) {
/*  92 */     return super.setMaxWidth(maxWidth - this.padding * 2);
/*     */   }
/*     */   
/*     */   public int getWidth() {
/*  97 */     return this.width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 102 */     return this.height;
/*     */   }
/*     */   
/*     */   public int getPadding() {
/* 106 */     return this.padding;
/*     */   }
/*     */   
/*     */   public void updateWidth() {
/* 110 */     if (this.maxWidth != -1) {
/* 111 */       setWidth(this.maxWidth);
/* 112 */       setMaxWidth(this.maxWidth);
/*     */     } else {
/* 114 */       setWidth(getFont().width((FormattedText)getMessage()) + this.padding * 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateHeight() {
/* 119 */     Objects.requireNonNull(getFont());
/* 119 */     int textHeight = 9 * getFont().split((FormattedText)getMessage(), super.getWidth()).size();
/* 120 */     setHeight(textHeight + this.padding * 2);
/*     */   }
/*     */   
/*     */   public void setMessage(Component message) {
/*     */     int width;
/* 125 */     this.message = message;
/* 127 */     if (this.maxWidth != -1) {
/* 128 */       width = this.maxWidth;
/*     */     } else {
/* 130 */       width = getFont().width((FormattedText)message) + this.padding * 2;
/*     */     } 
/* 132 */     setWidth(width);
/* 133 */     updateHeight();
/*     */   }
/*     */   
/*     */   public void playDownSound(SoundManager soundManager) {}
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 142 */     if (isActive() && event.isSelection()) {
/* 143 */       Optional<Style> clickableStyle = getMessage().visit((style, text) -> (style.getClickEvent() != null) ? Optional.<Style>of(style) : Optional.empty(), Style.EMPTY);
/* 147 */       if (clickableStyle.isPresent() && handleStyleClick(clickableStyle.get()))
/* 148 */         return true; 
/*     */     } 
/* 151 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final Component message;
/*     */     
/*     */     private final Font font;
/*     */     
/*     */     private final int padding;
/*     */     
/* 158 */     private int maxWidth = -1;
/*     */     
/*     */     private boolean alwaysShowBorder = true;
/*     */     
/* 160 */     private FocusableTextWidget.BackgroundFill backgroundFill = FocusableTextWidget.BackgroundFill.ALWAYS;
/*     */     
/*     */     private Builder(Component message, Font font) {
/* 163 */       this(message, font, 4);
/*     */     }
/*     */     
/*     */     private Builder(Component message, Font font, int padding) {
/* 167 */       this.message = message;
/* 168 */       this.font = font;
/* 169 */       this.padding = padding;
/*     */     }
/*     */     
/*     */     public Builder maxWidth(int maxWidth) {
/* 173 */       this.maxWidth = maxWidth;
/* 174 */       return this;
/*     */     }
/*     */     
/*     */     public Builder textWidth(int textWidth) {
/* 178 */       this.maxWidth = textWidth + this.padding * 2;
/* 179 */       return this;
/*     */     }
/*     */     
/*     */     public Builder alwaysShowBorder(boolean alwaysShowBorder) {
/* 183 */       this.alwaysShowBorder = alwaysShowBorder;
/* 184 */       return this;
/*     */     }
/*     */     
/*     */     public Builder backgroundFill(FocusableTextWidget.BackgroundFill backgroundFill) {
/* 188 */       this.backgroundFill = backgroundFill;
/* 189 */       return this;
/*     */     }
/*     */     
/*     */     public FocusableTextWidget build() {
/* 193 */       return new FocusableTextWidget(this.message, this.font, this.padding, this.maxWidth, this.backgroundFill, this.alwaysShowBorder);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder builder(Component message, Font font) {
/* 198 */     return new Builder(message, font);
/*     */   }
/*     */   
/*     */   public static Builder builder(Component message, Font font, int padding) {
/* 202 */     return new Builder(message, font, padding);
/*     */   }
/*     */   
/*     */   public enum BackgroundFill {
/* 206 */     ALWAYS, ON_FOCUS, NEVER;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\FocusableTextWidget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */