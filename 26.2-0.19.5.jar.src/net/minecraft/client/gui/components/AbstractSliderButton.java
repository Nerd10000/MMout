/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import net.minecraft.client.InputType;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.sounds.SoundManager;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public abstract class AbstractSliderButton extends AbstractWidget.WithInactiveMessage {
/*  20 */   private static final Identifier SLIDER_SPRITE = Identifier.withDefaultNamespace("widget/slider");
/*     */   
/*  21 */   private static final Identifier HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("widget/slider_highlighted");
/*     */   
/*  22 */   private static final Identifier SLIDER_HANDLE_SPRITE = Identifier.withDefaultNamespace("widget/slider_handle");
/*     */   
/*  23 */   private static final Identifier SLIDER_HANDLE_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("widget/slider_handle_highlighted");
/*     */   
/*     */   protected static final int TEXT_MARGIN = 2;
/*     */   
/*     */   public static final int DEFAULT_HEIGHT = 20;
/*     */   
/*     */   protected static final int HANDLE_WIDTH = 8;
/*     */   
/*     */   private static final int HANDLE_HALF_WIDTH = 4;
/*     */   
/*     */   protected double value;
/*     */   
/*     */   protected boolean canChangeValue;
/*     */   
/*     */   private boolean dragging;
/*     */   
/*     */   public AbstractSliderButton(int x, int y, int width, int height, Component message, double initialValue) {
/*  35 */     super(x, y, width, height, message);
/*  36 */     this.value = initialValue;
/*     */   }
/*     */   
/*     */   private Identifier getSprite() {
/*  40 */     if (isActive() && isFocused() && !this.canChangeValue)
/*  41 */       return HIGHLIGHTED_SPRITE; 
/*  43 */     return SLIDER_SPRITE;
/*     */   }
/*     */   
/*     */   private Identifier getHandleSprite() {
/*  48 */     if (isActive() && (this.isHovered || this.canChangeValue))
/*  49 */       return SLIDER_HANDLE_HIGHLIGHTED_SPRITE; 
/*  51 */     return SLIDER_HANDLE_SPRITE;
/*     */   }
/*     */   
/*     */   protected MutableComponent createNarrationMessage() {
/*  57 */     return Component.translatable("gui.narrate.slider", new Object[] { getMessage() });
/*     */   }
/*     */   
/*     */   public void updateWidgetNarration(NarrationElementOutput output) {
/*  62 */     output.add(NarratedElementType.TITLE, (Component)createNarrationMessage());
/*  63 */     if (this.active)
/*  64 */       if (isFocused()) {
/*  65 */         if (this.canChangeValue) {
/*  66 */           output.add(NarratedElementType.USAGE, (Component)Component.translatable("narration.slider.usage.focused"));
/*     */         } else {
/*  68 */           output.add(NarratedElementType.USAGE, (Component)Component.translatable("narration.slider.usage.focused.keyboard_cannot_change_value"));
/*     */         } 
/*     */       } else {
/*  71 */         output.add(NarratedElementType.USAGE, (Component)Component.translatable("narration.slider.usage.hovered"));
/*     */       }  
/*     */   }
/*     */   
/*     */   public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  78 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getSprite(), getX(), getY(), getWidth(), getHeight(), ARGB.white(this.alpha));
/*  79 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getHandleSprite(), getX() + (int)(this.value * (this.width - 8)), getY(), 8, getHeight(), ARGB.white(this.alpha));
/*  81 */     extractScrollingStringOverContents(graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE), getMessage(), 2);
/*  83 */     handleCursor(graphics);
/*     */   }
/*     */   
/*     */   protected void handleCursor(GuiGraphicsExtractor graphics) {
/*  88 */     if (isHovered())
/*  89 */       graphics.requestCursor(isActive() ? (this.dragging ? CursorTypes.RESIZE_EW : CursorTypes.POINTING_HAND) : CursorTypes.NOT_ALLOWED); 
/*     */   }
/*     */   
/*     */   public void onClick(MouseButtonEvent event, boolean doubleClick) {
/*  95 */     this.dragging = this.active;
/*  96 */     setValueFromMouse(event);
/*     */   }
/*     */   
/*     */   public void setFocused(boolean focused) {
/* 101 */     super.setFocused(focused);
/* 102 */     if (!focused) {
/* 103 */       this.canChangeValue = false;
/*     */       return;
/*     */     } 
/* 106 */     InputType lastInputType = Minecraft.getInstance().getLastInputType();
/* 107 */     if (lastInputType == InputType.MOUSE || lastInputType == InputType.KEYBOARD_TAB)
/* 108 */       this.canChangeValue = true; 
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 114 */     if (event.isSelection()) {
/* 115 */       this.canChangeValue = !this.canChangeValue;
/* 116 */       return true;
/*     */     } 
/* 118 */     if (this.canChangeValue) {
/* 119 */       boolean left = event.isLeft();
/* 120 */       boolean right = event.isRight();
/* 121 */       if (left || right) {
/* 122 */         float direction = left ? -1.0F : 1.0F;
/* 123 */         setValue(this.value + (direction / (this.width - 8)));
/* 124 */         return true;
/*     */       } 
/*     */     } 
/* 127 */     return false;
/*     */   }
/*     */   
/*     */   private void setValueFromMouse(MouseButtonEvent event) {
/* 131 */     setValue((event.x() - (getX() + 4)) / (this.width - 8));
/*     */   }
/*     */   
/*     */   protected void setValue(double newValue) {
/* 135 */     double oldValue = this.value;
/* 136 */     this.value = Mth.clamp(newValue, 0.0D, 1.0D);
/* 137 */     if (oldValue != this.value)
/* 138 */       applyValue(); 
/* 140 */     updateMessage();
/*     */   }
/*     */   
/*     */   protected void onDrag(MouseButtonEvent event, double dx, double dy) {
/* 145 */     setValueFromMouse(event);
/* 146 */     super.onDrag(event, dx, dy);
/*     */   }
/*     */   
/*     */   public void playDownSound(SoundManager soundManager) {}
/*     */   
/*     */   public void onRelease(MouseButtonEvent event) {
/* 155 */     this.dragging = false;
/* 156 */     super.playDownSound(Minecraft.getInstance().getSoundManager());
/*     */   }
/*     */   
/*     */   protected abstract void updateMessage();
/*     */   
/*     */   protected abstract void applyValue();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\AbstractSliderButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */