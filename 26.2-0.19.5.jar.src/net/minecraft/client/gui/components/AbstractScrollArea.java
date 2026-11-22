/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public abstract class AbstractScrollArea extends AbstractWidget {
/*     */   public static final int SCROLLBAR_WIDTH = 6;
/*     */   
/*     */   private static final int SCROLLBAR_MIN_HEIGHT = 32;
/*     */   
/*  15 */   private static final Identifier SCROLLER_SPRITE = Identifier.withDefaultNamespace("widget/scroller");
/*     */   
/*  16 */   private static final Identifier SCROLLER_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("widget/scroller_background");
/*     */   
/*     */   private final ScrollbarSettings scrollbarSettings;
/*     */   
/*     */   private double scrollAmount;
/*     */   
/*     */   private boolean scrolling;
/*     */   
/*     */   public AbstractScrollArea(int x, int y, int width, int height, Component message, ScrollbarSettings scrollbarSettings) {
/*  23 */     super(x, y, width, height, message);
/*  24 */     this.scrollbarSettings = scrollbarSettings;
/*     */   }
/*     */   
/*     */   public boolean mouseScrolled(double mx, double my, double scrollX, double scrollY) {
/*  29 */     if (!this.visible)
/*  30 */       return false; 
/*  32 */     setScrollAmount(scrollAmount() - scrollY * scrollRate());
/*  33 */     return true;
/*     */   }
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/*  38 */     if (this.scrolling) {
/*  39 */       if (event.y() < getY()) {
/*  40 */         setScrollAmount(0.0D);
/*  41 */       } else if (event.y() > getBottom()) {
/*  42 */         setScrollAmount(maxScrollAmount());
/*     */       } else {
/*  44 */         double max = Math.max(1, maxScrollAmount());
/*  45 */         int barHeight = scrollerHeight();
/*  46 */         double yDragScale = Math.max(1.0D, max / (this.height - barHeight));
/*  47 */         setScrollAmount(scrollAmount() + dy * yDragScale);
/*     */       } 
/*  49 */       return true;
/*     */     } 
/*  52 */     return super.mouseDragged(event, dx, dy);
/*     */   }
/*     */   
/*     */   public void onRelease(MouseButtonEvent event) {
/*  57 */     this.scrolling = false;
/*     */   }
/*     */   
/*     */   public double scrollAmount() {
/*  61 */     return this.scrollAmount;
/*     */   }
/*     */   
/*     */   public void setScrollAmount(double scrollAmount) {
/*  65 */     this.scrollAmount = Mth.clamp(scrollAmount, 0.0D, maxScrollAmount());
/*     */   }
/*     */   
/*     */   public boolean updateScrolling(MouseButtonEvent event) {
/*  69 */     this.scrolling = (scrollable() && isValidClickButton(event.buttonInfo()) && isOverScrollbar(event.x(), event.y()));
/*  70 */     return this.scrolling;
/*     */   }
/*     */   
/*     */   protected boolean isOverScrollbar(double x, double y) {
/*  74 */     return (x >= scrollBarX() && x <= (scrollBarX() + scrollbarWidth()) && y >= getY() && y < getBottom());
/*     */   }
/*     */   
/*     */   public void refreshScrollAmount() {
/*  78 */     setScrollAmount(this.scrollAmount);
/*     */   }
/*     */   
/*     */   public int maxScrollAmount() {
/*  82 */     return Math.max(0, contentHeight() - this.height);
/*     */   }
/*     */   
/*     */   protected boolean scrollable() {
/*  86 */     return (maxScrollAmount() > 0);
/*     */   }
/*     */   
/*     */   public int scrollbarWidth() {
/*  90 */     return this.scrollbarSettings.scrollbarWidth();
/*     */   }
/*     */   
/*     */   protected int scrollerHeight() {
/*  94 */     return Mth.clamp((int)((this.height * this.height) / contentHeight()), 32, this.height - 8);
/*     */   }
/*     */   
/*     */   protected int scrollBarX() {
/*  98 */     return getRight() - scrollbarWidth();
/*     */   }
/*     */   
/*     */   public int scrollBarY() {
/* 102 */     return (maxScrollAmount() == 0) ? getY() : Math.max(getY(), (int)this.scrollAmount * (this.height - scrollerHeight()) / maxScrollAmount() + getY());
/*     */   }
/*     */   
/*     */   protected void extractScrollbar(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
/* 106 */     int scrollbarX = scrollBarX();
/* 107 */     int scrollerHeight = scrollerHeight();
/* 108 */     int scrollerY = scrollBarY();
/* 110 */     if (!scrollable() && this.scrollbarSettings.disabledScrollerSprite() != null) {
/* 111 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.scrollbarSettings.backgroundSprite(), scrollbarX, getY(), scrollbarWidth(), getHeight());
/* 112 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.scrollbarSettings.disabledScrollerSprite(), scrollbarX, getY(), scrollbarWidth(), scrollerHeight);
/* 113 */       if (isOverScrollbar(mouseX, mouseY))
/* 114 */         graphics.requestCursor(CursorTypes.NOT_ALLOWED); 
/*     */     } 
/* 118 */     if (scrollable()) {
/* 119 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.scrollbarSettings.backgroundSprite(), scrollbarX, getY(), scrollbarWidth(), getHeight());
/* 120 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.scrollbarSettings.scrollerSprite(), scrollbarX, scrollerY, scrollbarWidth(), scrollerHeight);
/* 122 */       if (isOverScrollbar(mouseX, mouseY))
/* 123 */         graphics.requestCursor(this.scrolling ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract int contentHeight();
/*     */   
/*     */   protected double scrollRate() {
/* 131 */     return this.scrollbarSettings.scrollRate();
/*     */   }
/*     */   
/*     */   public static ScrollbarSettings defaultSettings(int scrollRate) {
/* 135 */     return new ScrollbarSettings(SCROLLER_SPRITE, null, SCROLLER_BACKGROUND_SPRITE, 6, 32, scrollRate, true);
/*     */   }
/*     */   
/*     */   public static final class ScrollbarSettings extends Record {
/*     */     private final Identifier scrollerSprite;
/*     */     
/*     */     private final Identifier disabledScrollerSprite;
/*     */     
/*     */     private final Identifier backgroundSprite;
/*     */     
/*     */     private final int scrollbarWidth;
/*     */     
/*     */     private final int scrollbarMinHeight;
/*     */     
/*     */     private final int scrollRate;
/*     */     
/*     */     private final boolean resizingScrollbar;
/*     */     
/*     */     public ScrollbarSettings(Identifier scrollerSprite, Identifier disabledScrollerSprite, Identifier backgroundSprite, int scrollbarWidth, int scrollbarMinHeight, int scrollRate, boolean resizingScrollbar) {
/* 145 */       this.scrollerSprite = scrollerSprite;
/* 145 */       this.disabledScrollerSprite = disabledScrollerSprite;
/* 145 */       this.backgroundSprite = backgroundSprite;
/* 145 */       this.scrollbarWidth = scrollbarWidth;
/* 145 */       this.scrollbarMinHeight = scrollbarMinHeight;
/* 145 */       this.scrollRate = scrollRate;
/* 145 */       this.resizingScrollbar = resizingScrollbar;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/AbstractScrollArea$ScrollbarSettings;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #145	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/AbstractScrollArea$ScrollbarSettings;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/AbstractScrollArea$ScrollbarSettings;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #145	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/AbstractScrollArea$ScrollbarSettings;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/AbstractScrollArea$ScrollbarSettings;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #145	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/components/AbstractScrollArea$ScrollbarSettings;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Identifier scrollerSprite() {
/* 145 */       return this.scrollerSprite;
/*     */     }
/*     */     
/*     */     public Identifier disabledScrollerSprite() {
/* 145 */       return this.disabledScrollerSprite;
/*     */     }
/*     */     
/*     */     public Identifier backgroundSprite() {
/* 145 */       return this.backgroundSprite;
/*     */     }
/*     */     
/*     */     public int scrollbarWidth() {
/* 145 */       return this.scrollbarWidth;
/*     */     }
/*     */     
/*     */     public int scrollbarMinHeight() {
/* 145 */       return this.scrollbarMinHeight;
/*     */     }
/*     */     
/*     */     public int scrollRate() {
/* 145 */       return this.scrollRate;
/*     */     }
/*     */     
/*     */     public boolean resizingScrollbar() {
/* 145 */       return this.resizingScrollbar;
/*     */     }
/*     */     
/* 154 */     public static final ScrollbarSettings NO_SCROLL = AbstractScrollArea.defaultSettings(0);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\AbstractScrollArea.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */