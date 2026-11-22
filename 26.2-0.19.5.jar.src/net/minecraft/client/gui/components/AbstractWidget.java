/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import java.time.Duration;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.ComponentPath;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.input.MouseButtonInfo;
/*     */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.client.sounds.SoundManager;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ 
/*     */ public abstract class AbstractWidget implements LayoutElement, Renderable, GuiEventListener, NarratableEntry {
/*     */   protected int width;
/*     */   
/*     */   protected int height;
/*     */   
/*     */   private int x;
/*     */   
/*     */   private int y;
/*     */   
/*     */   protected Component message;
/*     */   
/*     */   protected boolean isHovered;
/*     */   
/*     */   public boolean active = true;
/*     */   
/*     */   public boolean visible = true;
/*     */   
/*  40 */   protected float alpha = 1.0F;
/*     */   
/*     */   private int tabOrderGroup;
/*     */   
/*     */   private boolean focused;
/*     */   
/*  45 */   private final WidgetTooltipHolder tooltip = new WidgetTooltipHolder();
/*     */   
/*     */   public AbstractWidget(int x, int y, int width, int height, Component message) {
/*  48 */     this.x = x;
/*  49 */     this.y = y;
/*  50 */     this.width = width;
/*  51 */     this.height = height;
/*  52 */     this.message = message;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/*  57 */     return this.height;
/*     */   }
/*     */   
/*     */   public final void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  62 */     if (!this.visible)
/*     */       return; 
/*  66 */     this.isHovered = (graphics.containsPointInScissor(mouseX, mouseY) && areCoordinatesInRectangle(mouseX, mouseY));
/*  67 */     extractWidgetRenderState(graphics, mouseX, mouseY, a);
/*  69 */     extractTooltipForNextRenderPass(graphics, mouseX, mouseY);
/*     */   }
/*     */   
/*     */   protected void extractTooltipForNextRenderPass(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
/*  73 */     this.tooltip.refreshTooltipForNextRenderPass(graphics, mouseX, mouseY, isHovered(), isFocused(), getRectangle());
/*     */   }
/*     */   
/*     */   protected void handleCursor(GuiGraphicsExtractor graphics) {
/*  77 */     if (isHovered())
/*  78 */       graphics.requestCursor(isActive() ? CursorTypes.POINTING_HAND : CursorTypes.NOT_ALLOWED); 
/*     */   }
/*     */   
/*     */   public void setTooltip(Tooltip tooltip) {
/*  83 */     this.tooltip.set(tooltip);
/*     */   }
/*     */   
/*     */   public void setTooltipDelay(Duration delay) {
/*  87 */     this.tooltip.setDelay(delay);
/*     */   }
/*     */   
/*     */   protected MutableComponent createNarrationMessage() {
/*  91 */     return wrapDefaultNarrationMessage(getMessage());
/*     */   }
/*     */   
/*     */   public static MutableComponent wrapDefaultNarrationMessage(Component message) {
/*  95 */     return Component.translatable("gui.narrate.button", new Object[] { message });
/*     */   }
/*     */   
/*     */   protected abstract void extractWidgetRenderState(GuiGraphicsExtractor paramGuiGraphicsExtractor, int paramInt1, int paramInt2, float paramFloat);
/*     */   
/*     */   protected void extractScrollingStringOverContents(ActiveTextCollector output, Component message, int margin) {
/* 101 */     int left = getX() + margin;
/* 102 */     int right = getX() + getWidth() - margin;
/* 103 */     int top = getY();
/* 104 */     int bottom = getY() + getHeight();
/* 105 */     output.acceptScrollingWithDefaultCenter(message, left, right, top, bottom);
/*     */   }
/*     */   
/*     */   public void onClick(MouseButtonEvent event, boolean doubleClick) {}
/*     */   
/*     */   public void onRelease(MouseButtonEvent event) {}
/*     */   
/*     */   protected void onDrag(MouseButtonEvent event, double dx, double dy) {}
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 119 */     if (!isActive())
/* 120 */       return false; 
/* 122 */     if (isValidClickButton(event.buttonInfo())) {
/* 123 */       boolean isMouseOver = isMouseOver(event.x(), event.y());
/* 124 */       if (isMouseOver) {
/* 125 */         playDownSound(Minecraft.getInstance().getSoundManager());
/* 126 */         onClick(event, doubleClick);
/* 127 */         return true;
/*     */       } 
/*     */     } 
/* 130 */     return false;
/*     */   }
/*     */   
/*     */   public boolean mouseReleased(MouseButtonEvent event) {
/* 135 */     if (isValidClickButton(event.buttonInfo())) {
/* 136 */       onRelease(event);
/* 137 */       return true;
/*     */     } 
/* 139 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
/* 143 */     return (buttonInfo.button() == 0);
/*     */   }
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 148 */     if (isValidClickButton(event.buttonInfo())) {
/* 149 */       onDrag(event, dx, dy);
/* 150 */       return true;
/*     */     } 
/* 152 */     return false;
/*     */   }
/*     */   
/*     */   public ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 157 */     if (!isActive())
/* 158 */       return null; 
/* 161 */     if (!isFocused())
/* 162 */       return ComponentPath.leaf(this); 
/* 164 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isMouseOver(double mouseX, double mouseY) {
/* 169 */     return (isActive() && areCoordinatesInRectangle(mouseX, mouseY));
/*     */   }
/*     */   
/*     */   public void playDownSound(SoundManager soundManager) {
/* 173 */     playButtonClickSound(soundManager);
/*     */   }
/*     */   
/*     */   public static void playButtonClickSound(SoundManager soundManager) {
/* 177 */     soundManager.play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 182 */     return this.width;
/*     */   }
/*     */   
/*     */   public void setWidth(int width) {
/* 186 */     this.width = width;
/*     */   }
/*     */   
/*     */   public void setHeight(int height) {
/* 190 */     this.height = height;
/*     */   }
/*     */   
/*     */   public void setAlpha(float alpha) {
/* 194 */     this.alpha = alpha;
/*     */   }
/*     */   
/*     */   public float getAlpha() {
/* 198 */     return this.alpha;
/*     */   }
/*     */   
/*     */   public void setMessage(Component message) {
/* 202 */     this.message = message;
/*     */   }
/*     */   
/*     */   public Component getMessage() {
/* 206 */     return this.message;
/*     */   }
/*     */   
/*     */   public boolean isFocused() {
/* 211 */     return this.focused;
/*     */   }
/*     */   
/*     */   public boolean isHovered() {
/* 215 */     return this.isHovered;
/*     */   }
/*     */   
/*     */   public boolean isHoveredOrFocused() {
/* 219 */     return (isHovered() || isFocused());
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 224 */     return (this.visible && this.active);
/*     */   }
/*     */   
/*     */   public void setFocused(boolean focused) {
/* 230 */     this.focused = focused;
/*     */   }
/*     */   
/*     */   public NarratableEntry.NarrationPriority narrationPriority() {
/* 235 */     if (isFocused())
/* 236 */       return NarratableEntry.NarrationPriority.FOCUSED; 
/* 238 */     if (this.isHovered)
/* 239 */       return NarratableEntry.NarrationPriority.HOVERED; 
/* 241 */     return NarratableEntry.NarrationPriority.NONE;
/*     */   }
/*     */   
/*     */   public final void updateNarration(NarrationElementOutput output) {
/* 246 */     updateWidgetNarration(output);
/* 247 */     this.tooltip.updateNarration(output);
/*     */   }
/*     */   
/*     */   protected abstract void updateWidgetNarration(NarrationElementOutput paramNarrationElementOutput);
/*     */   
/*     */   protected void defaultButtonNarrationText(NarrationElementOutput output) {
/* 254 */     output.add(NarratedElementType.TITLE, (Component)createNarrationMessage());
/* 255 */     if (this.active)
/* 256 */       if (isFocused()) {
/* 257 */         output.add(NarratedElementType.USAGE, (Component)Component.translatable("narration.button.usage.focused"));
/*     */       } else {
/* 259 */         output.add(NarratedElementType.USAGE, (Component)Component.translatable("narration.button.usage.hovered"));
/*     */       }  
/*     */   }
/*     */   
/*     */   public int getX() {
/* 266 */     return this.x;
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/* 271 */     this.x = x;
/*     */   }
/*     */   
/*     */   public int getY() {
/* 276 */     return this.y;
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/* 281 */     this.y = y;
/*     */   }
/*     */   
/*     */   public int getRight() {
/* 285 */     return getX() + getWidth();
/*     */   }
/*     */   
/*     */   public int getBottom() {
/* 289 */     return getY() + getHeight();
/*     */   }
/*     */   
/*     */   public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {
/* 294 */     widgetVisitor.accept(this);
/*     */   }
/*     */   
/*     */   public void setSize(int width, int height) {
/* 298 */     this.width = width;
/* 299 */     this.height = height;
/*     */   }
/*     */   
/*     */   public ScreenRectangle getRectangle() {
/* 304 */     return super.getRectangle();
/*     */   }
/*     */   
/*     */   private boolean areCoordinatesInRectangle(double x, double y) {
/* 308 */     return (x >= getX() && y >= getY() && x < getRight() && y < getBottom());
/*     */   }
/*     */   
/*     */   public void setRectangle(int width, int height, int x, int y) {
/* 312 */     setSize(width, height);
/* 313 */     setPosition(x, y);
/*     */   }
/*     */   
/*     */   public int getTabOrderGroup() {
/* 318 */     return this.tabOrderGroup;
/*     */   }
/*     */   
/*     */   public void setTabOrderGroup(int tabOrderGroup) {
/* 322 */     this.tabOrderGroup = tabOrderGroup;
/*     */   }
/*     */   
/*     */   public static abstract class WithInactiveMessage extends AbstractWidget {
/*     */     private Component inactiveMessage;
/*     */     
/*     */     public static Component defaultInactiveMessage(Component activeMessage) {
/* 329 */       return ComponentUtils.mergeStyles(activeMessage, Style.EMPTY.withColor(-6250336));
/*     */     }
/*     */     
/*     */     public WithInactiveMessage(int x, int y, int width, int height, Component message) {
/* 333 */       super(x, y, width, height, message);
/* 334 */       this.inactiveMessage = defaultInactiveMessage(message);
/*     */     }
/*     */     
/*     */     public Component getMessage() {
/* 339 */       return this.active ? super.getMessage() : this.inactiveMessage;
/*     */     }
/*     */     
/*     */     public void setMessage(Component message) {
/* 344 */       super.setMessage(message);
/* 345 */       this.inactiveMessage = defaultInactiveMessage(message);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\AbstractWidget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */