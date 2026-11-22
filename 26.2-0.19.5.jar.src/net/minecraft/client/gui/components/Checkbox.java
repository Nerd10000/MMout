/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.OptionInstance;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.input.InputWithModifiers;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ public class Checkbox extends AbstractButton {
/*  18 */   private static final Identifier CHECKBOX_SELECTED_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("widget/checkbox_selected_highlighted");
/*     */   
/*  19 */   private static final Identifier CHECKBOX_SELECTED_SPRITE = Identifier.withDefaultNamespace("widget/checkbox_selected");
/*     */   
/*  20 */   private static final Identifier CHECKBOX_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("widget/checkbox_highlighted");
/*     */   
/*  21 */   private static final Identifier CHECKBOX_SPRITE = Identifier.withDefaultNamespace("widget/checkbox");
/*     */   
/*     */   private static final int SPACING = 4;
/*     */   
/*     */   private static final int ROWS = 2;
/*     */   
/*     */   private static final int BOX_PADDING = 8;
/*     */   
/*     */   private boolean selected;
/*     */   
/*     */   private final OnValueChange onValueChange;
/*     */   
/*     */   private final MultiLineTextWidget textWidget;
/*     */   
/*     */   public static interface OnValueChange {
/*     */     public static final OnValueChange NOP = (checkbox, value) -> {
/*     */       
/*     */       };
/*     */     
/*     */     void onValueChange(Checkbox param1Checkbox, boolean param1Boolean);
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final Component message;
/*     */     
/*     */     private final Font font;
/*     */     
/*     */     private int maxWidth;
/*     */     
/*  43 */     private int x = 0;
/*     */     
/*  44 */     private int y = 0;
/*     */     
/*  45 */     private Checkbox.OnValueChange onValueChange = Checkbox.OnValueChange.NOP;
/*     */     
/*     */     private boolean selected = false;
/*     */     
/*  47 */     private OptionInstance<Boolean> option = null;
/*     */     
/*  48 */     private Tooltip tooltip = null;
/*     */     
/*     */     private Builder(Component message, Font font) {
/*  51 */       this.message = message;
/*  52 */       this.font = font;
/*  53 */       this.maxWidth = Checkbox.getDefaultWidth(message, font);
/*     */     }
/*     */     
/*     */     public Builder pos(int x, int y) {
/*  57 */       this.x = x;
/*  58 */       this.y = y;
/*  59 */       return this;
/*     */     }
/*     */     
/*     */     public Builder onValueChange(Checkbox.OnValueChange onValueChange) {
/*  63 */       this.onValueChange = onValueChange;
/*  64 */       return this;
/*     */     }
/*     */     
/*     */     public Builder selected(boolean selected) {
/*  68 */       this.selected = selected;
/*  69 */       this.option = null;
/*  70 */       return this;
/*     */     }
/*     */     
/*     */     public Builder selected(OptionInstance<Boolean> option) {
/*  74 */       this.option = option;
/*  75 */       this.selected = ((Boolean)option.get()).booleanValue();
/*  76 */       return this;
/*     */     }
/*     */     
/*     */     public Builder tooltip(Tooltip tooltip) {
/*  80 */       this.tooltip = tooltip;
/*  81 */       return this;
/*     */     }
/*     */     
/*     */     public Builder maxWidth(int maxWidth) {
/*  85 */       this.maxWidth = maxWidth;
/*  86 */       return this;
/*     */     }
/*     */     
/*     */     public Checkbox build() {
/*  90 */       Checkbox.OnValueChange onChange = (this.option == null) ? this.onValueChange : ((checkbox, value) -> {
/*     */           this.option.set(Boolean.valueOf(value));
/*     */           this.onValueChange.onValueChange(checkbox, value);
/*     */         });
/*  95 */       Checkbox box = new Checkbox(this.x, this.y, this.maxWidth, this.message, this.font, this.selected, onChange);
/*  96 */       if (box.overflowsRowLimit(this.font))
/*  97 */         box.setTooltip(this.tooltip); 
/*  99 */       return box;
/*     */     }
/*     */   }
/*     */   
/*     */   private Checkbox(int x, int y, int maxWidth, Component message, Font font, boolean selected, OnValueChange onValueChange) {
/* 104 */     super(x, y, 0, 0, message);
/* 106 */     this.textWidget = new MultiLineTextWidget(message, font);
/* 107 */     this.textWidget.setMaxRows(2);
/* 108 */     this.width = adjustWidth(maxWidth, font);
/* 109 */     this.height = getAdjustedHeight(font);
/* 110 */     this.selected = selected;
/* 111 */     this.onValueChange = onValueChange;
/*     */   }
/*     */   
/*     */   public int adjustWidth(int maxWidth, Font font) {
/* 115 */     this.width = getAdjustedWidth(maxWidth, getMessage(), font);
/* 116 */     this.textWidget.setMaxWidth(this.width);
/* 117 */     return this.width;
/*     */   }
/*     */   
/*     */   private int getAdjustedWidth(int maxWidth, Component message, Font font) {
/* 121 */     return Math.min(getDefaultWidth(message, font), maxWidth);
/*     */   }
/*     */   
/*     */   private int getAdjustedHeight(Font font) {
/* 125 */     return Math.max(getBoxSize(font), this.textWidget.getHeight());
/*     */   }
/*     */   
/*     */   private static int getDefaultWidth(Component message, Font font) {
/* 129 */     return getBoxSize(font) + 4 + font.width((FormattedText)message);
/*     */   }
/*     */   
/*     */   private boolean overflowsRowLimit(Font font) {
/* 133 */     return (font.getSplitter().splitLines((FormattedText)this.textWidget.getMessage(), this.width, Style.EMPTY).size() > 2);
/*     */   }
/*     */   
/*     */   public static Builder builder(Component message, Font font) {
/* 137 */     return new Builder(message, font);
/*     */   }
/*     */   
/*     */   public static int getBoxSize(Font font) {
/* 141 */     Objects.requireNonNull(font);
/* 141 */     return 9 + 8;
/*     */   }
/*     */   
/*     */   public void onPress(InputWithModifiers input) {
/* 146 */     this.selected = !this.selected;
/* 147 */     this.onValueChange.onValueChange(this, this.selected);
/*     */   }
/*     */   
/*     */   public boolean selected() {
/* 151 */     return this.selected;
/*     */   }
/*     */   
/*     */   public void updateWidgetNarration(NarrationElementOutput output) {
/* 156 */     output.add(NarratedElementType.TITLE, (Component)createNarrationMessage());
/* 157 */     if (this.active)
/* 158 */       if (isFocused()) {
/* 159 */         output.add(NarratedElementType.USAGE, (Component)Component.translatable(this.selected ? "narration.checkbox.usage.focused.uncheck" : "narration.checkbox.usage.focused.check"));
/*     */       } else {
/* 161 */         output.add(NarratedElementType.USAGE, (Component)Component.translatable(this.selected ? "narration.checkbox.usage.hovered.uncheck" : "narration.checkbox.usage.hovered.check"));
/*     */       }  
/*     */   }
/*     */   
/*     */   public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*     */     Identifier sprite;
/* 168 */     Minecraft minecraft = Minecraft.getInstance();
/* 169 */     Font font = minecraft.font;
/* 172 */     if (this.selected) {
/* 173 */       sprite = isFocused() ? CHECKBOX_SELECTED_HIGHLIGHTED_SPRITE : CHECKBOX_SELECTED_SPRITE;
/*     */     } else {
/* 175 */       sprite = isFocused() ? CHECKBOX_HIGHLIGHTED_SPRITE : CHECKBOX_SPRITE;
/*     */     } 
/* 177 */     int boxSize = getBoxSize(font);
/* 178 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), boxSize, boxSize, ARGB.white(this.alpha));
/* 180 */     int textX = getX() + boxSize + 4;
/* 181 */     int textY = getY() + boxSize / 2 - this.textWidget.getHeight() / 2;
/* 182 */     this.textWidget.setPosition(textX, textY);
/* 184 */     this.textWidget.visitLines(graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.notClickable(isHovered())));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\Checkbox.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */