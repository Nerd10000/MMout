/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.input.InputWithModifiers;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ public abstract class SpriteIconButton extends Button {
/*  13 */   private static final Identifier BUTTON_DISABLED_SPRITE = Identifier.withDefaultNamespace("widget/button_disabled");
/*     */   
/*  14 */   private static final Identifier LOADING_SPRITE = Identifier.withDefaultNamespace("friends/loading");
/*     */   
/*  15 */   private static final Tooltip LOADING_TOOLTIP = Tooltip.create((Component)Component.translatable("gui.friends.button.loading"));
/*     */   
/*     */   private static final int LOADING_SPRITE_W = 5;
/*     */   
/*     */   private static final int LOADING_SPRITE_H = 2;
/*     */   
/*     */   protected final WidgetSprites sprite;
/*     */   
/*     */   protected final int spriteWidth;
/*     */   
/*     */   protected final int spriteHeight;
/*     */   
/*     */   protected final int spriteOffsetX;
/*     */   
/*     */   protected final int spriteOffsetY;
/*     */   
/*     */   private final boolean switchToLoadingAfterPress;
/*     */   
/*     */   private final Tooltip defaultTooltip;
/*     */   
/*     */   private boolean loading;
/*     */   
/*     */   private SpriteIconButton(int width, int height, Component message, int spriteWidth, int spriteHeight, int spriteOffsetX, int spriteOffsetY, WidgetSprites sprite, Button.OnPress onPress, Component tooltip, Button.CreateNarration narration, boolean switchToLoadingAfterPress) {
/*  35 */     super(0, 0, width, height, message, onPress, (narration == null) ? DEFAULT_NARRATION : narration);
/*  36 */     this.defaultTooltip = (tooltip != null) ? Tooltip.create(tooltip) : null;
/*  37 */     setTooltip(this.defaultTooltip);
/*  38 */     this.spriteWidth = spriteWidth;
/*  39 */     this.spriteHeight = spriteHeight;
/*  40 */     this.spriteOffsetX = spriteOffsetX;
/*  41 */     this.spriteOffsetY = spriteOffsetY;
/*  42 */     this.sprite = sprite;
/*  43 */     this.switchToLoadingAfterPress = switchToLoadingAfterPress;
/*     */   }
/*     */   
/*     */   public void setLoading(boolean loading) {
/*  47 */     setLoading(loading, LOADING_TOOLTIP);
/*     */   }
/*     */   
/*     */   public void setLoading(boolean loading, Tooltip loadingTooltip) {
/*  51 */     this.loading = loading;
/*  52 */     if (loading) {
/*  53 */       setTooltip(loadingTooltip);
/*     */     } else {
/*  55 */       setTooltip(this.defaultTooltip);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/*  61 */     return (super.isActive() && !this.loading);
/*     */   }
/*     */   
/*     */   public void onPress(InputWithModifiers input) {
/*  66 */     if (this.switchToLoadingAfterPress)
/*  67 */       setLoading(true); 
/*  69 */     super.onPress(input);
/*     */   }
/*     */   
/*     */   protected void extractSprite(GuiGraphicsExtractor graphics, int x, int y) {
/*  73 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite.get(isActive(), isHoveredOrFocused()), x, y, this.spriteWidth, this.spriteHeight, this.alpha);
/*     */   }
/*     */   
/*     */   protected boolean extractLoadingStateIfLoading(GuiGraphicsExtractor graphics) {
/*  77 */     if (!this.loading)
/*  78 */       return false; 
/*  80 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BUTTON_DISABLED_SPRITE, getX(), getY(), getWidth(), getHeight(), ARGB.white(this.alpha));
/*  81 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, LOADING_SPRITE, 
/*     */         
/*  84 */         getX() + (getWidth() - 5) / 2, 
/*  85 */         getY() + (getHeight() - 2) / 2, 5, 2, 
/*     */         
/*  88 */         ARGB.white(this.alpha));
/*  90 */     return true;
/*     */   }
/*     */   
/*     */   public static Builder builder(Component message, Button.OnPress onPress, boolean iconOnly) {
/*  94 */     return new Builder(message, onPress, iconOnly);
/*     */   }
/*     */   
/*     */   public static class CenteredIcon extends SpriteIconButton {
/*     */     protected CenteredIcon(int width, int height, Component message, int spriteWidth, int spriteHeight, int spriteOffsetX, int spriteOffsetY, WidgetSprites sprite, Button.OnPress onPress, Component tooltip, Button.CreateNarration narration, boolean switchToLoadingAfterPress) {
/* 104 */       super(width, height, message, spriteWidth, spriteHeight, spriteOffsetX, spriteOffsetY, sprite, onPress, tooltip, narration, switchToLoadingAfterPress);
/*     */     }
/*     */     
/*     */     public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 109 */       if (extractLoadingStateIfLoading(graphics))
/*     */         return; 
/* 112 */       extractDefaultSprite(graphics);
/* 114 */       int x = this.spriteOffsetX + getX() + getWidth() / 2 - this.spriteWidth / 2;
/* 115 */       int y = this.spriteOffsetY + getY() + getHeight() / 2 - this.spriteHeight / 2;
/* 116 */       extractSprite(graphics, x, y);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TextAndIcon extends SpriteIconButton {
/*     */     protected TextAndIcon(int width, int height, Component message, int spriteWidth, int spriteHeight, int spriteOffsetX, int spriteOffsetY, WidgetSprites sprite, Button.OnPress onPress, Component tooltip, Button.CreateNarration narration, boolean switchToLoadingAfterPress) {
/* 127 */       super(width, height, message, spriteWidth, spriteHeight, spriteOffsetX, spriteOffsetY, sprite, onPress, tooltip, narration, switchToLoadingAfterPress);
/*     */     }
/*     */     
/*     */     public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 132 */       if (extractLoadingStateIfLoading(graphics))
/*     */         return; 
/* 135 */       extractDefaultSprite(graphics);
/* 137 */       int left = getX() + 2;
/* 138 */       int right = getX() + getWidth() - this.spriteWidth - 4;
/* 140 */       int centerX = getX() + getWidth() / 2;
/* 141 */       ActiveTextCollector output = graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE);
/* 142 */       output.acceptScrolling(getMessage(), centerX, left, right, getY(), getY() + getHeight());
/* 144 */       int x = this.spriteOffsetX + getX() + getWidth() - this.spriteWidth - 2;
/* 145 */       int y = this.spriteOffsetY + getY() + getHeight() / 2 - this.spriteHeight / 2;
/* 146 */       extractSprite(graphics, x, y);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final Component message;
/*     */     
/*     */     private final Button.OnPress onPress;
/*     */     
/*     */     private final boolean iconOnly;
/*     */     
/* 155 */     private int width = 150;
/*     */     
/* 156 */     private int height = 20;
/*     */     
/*     */     private WidgetSprites sprite;
/*     */     
/*     */     private int spriteWidth;
/*     */     
/*     */     private int spriteHeight;
/*     */     
/*     */     private int spriteOffsetX;
/*     */     
/*     */     private int spriteOffsetY;
/*     */     
/*     */     private Component tooltip;
/*     */     
/*     */     private Button.CreateNarration narration;
/*     */     
/*     */     private boolean switchToLoadingAfterPress;
/*     */     
/*     */     private Builder(Component message, Button.OnPress onPress, boolean iconOnly) {
/* 168 */       this.message = message;
/* 169 */       this.onPress = onPress;
/* 170 */       this.iconOnly = iconOnly;
/*     */     }
/*     */     
/*     */     public Builder width(int width) {
/* 174 */       this.width = width;
/* 175 */       return this;
/*     */     }
/*     */     
/*     */     public Builder size(int width, int height) {
/* 179 */       this.width = width;
/* 180 */       this.height = height;
/* 181 */       return this;
/*     */     }
/*     */     
/*     */     public Builder sprite(Identifier sprite, int spriteWidth, int spriteHeight) {
/* 185 */       this.sprite = new WidgetSprites(sprite);
/* 186 */       this.spriteWidth = spriteWidth;
/* 187 */       this.spriteHeight = spriteHeight;
/* 188 */       return this;
/*     */     }
/*     */     
/*     */     public Builder sprite(WidgetSprites sprite, int spriteWidth, int spriteHeight) {
/* 191 */       this.sprite = sprite;
/* 192 */       this.spriteWidth = spriteWidth;
/* 193 */       this.spriteHeight = spriteHeight;
/* 194 */       return this;
/*     */     }
/*     */     
/*     */     public Builder spriteOffset(int spriteOffsetX, int spriteOffsetY) {
/* 198 */       this.spriteOffsetX = spriteOffsetX;
/* 199 */       this.spriteOffsetY = spriteOffsetY;
/* 200 */       return this;
/*     */     }
/*     */     
/*     */     public Builder tooltip(Component tooltip) {
/* 204 */       this.tooltip = tooltip;
/* 205 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withTootip() {
/* 209 */       this.tooltip = this.message;
/* 210 */       return this;
/*     */     }
/*     */     
/*     */     public Builder narration(Button.CreateNarration narration) {
/* 214 */       this.narration = narration;
/* 215 */       return this;
/*     */     }
/*     */     
/*     */     public Builder switchToLoadingAfterPress() {
/* 219 */       this.switchToLoadingAfterPress = true;
/* 220 */       return this;
/*     */     }
/*     */     
/*     */     public SpriteIconButton build() {
/* 224 */       if (this.sprite == null)
/* 225 */         throw new IllegalStateException("Sprite not set"); 
/* 227 */       if (this.iconOnly)
/* 228 */         return new SpriteIconButton.CenteredIcon(this.width, this.height, this.message, this.spriteWidth, this.spriteHeight, this.spriteOffsetX, this.spriteOffsetY, this.sprite, this.onPress, this.tooltip, this.narration, this.switchToLoadingAfterPress); 
/* 230 */       return new SpriteIconButton.TextAndIcon(this.width, this.height, this.message, this.spriteWidth, this.spriteHeight, this.spriteOffsetX, this.spriteOffsetY, this.sprite, this.onPress, this.tooltip, this.narration, this.switchToLoadingAfterPress);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\SpriteIconButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */