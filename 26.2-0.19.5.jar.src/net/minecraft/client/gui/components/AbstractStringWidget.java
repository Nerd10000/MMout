/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.gui.ActiveTextCollector;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.Style;
/*    */ 
/*    */ public abstract class AbstractStringWidget extends AbstractWidget {
/* 15 */   private Consumer<Style> componentClickHandler = null;
/*    */   
/*    */   private final Font font;
/*    */   
/*    */   public AbstractStringWidget(int x, int y, int width, int height, Component message, Font font) {
/* 19 */     super(x, y, width, height, message);
/* 20 */     this.font = font;
/*    */   }
/*    */   
/*    */   public abstract void visitLines(ActiveTextCollector paramActiveTextCollector);
/*    */   
/*    */   public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*    */     GuiGraphicsExtractor.HoveredTextEffects effects;
/* 28 */     if (isHovered()) {
/* 29 */       if (this.componentClickHandler != null) {
/* 30 */         effects = GuiGraphicsExtractor.HoveredTextEffects.TOOLTIP_AND_CURSOR;
/*    */       } else {
/* 32 */         effects = GuiGraphicsExtractor.HoveredTextEffects.TOOLTIP_ONLY;
/*    */       } 
/*    */     } else {
/* 35 */       effects = GuiGraphicsExtractor.HoveredTextEffects.NONE;
/*    */     } 
/* 37 */     visitLines(graphics.textRendererForWidget(this, effects));
/*    */   }
/*    */   
/*    */   public void onClick(MouseButtonEvent event, boolean doubleClick) {
/* 42 */     if (this.componentClickHandler != null) {
/* 43 */       ActiveTextCollector.ClickableStyleFinder finder = new ActiveTextCollector.ClickableStyleFinder(getFont(), (int)event.x(), (int)event.y());
/* 44 */       visitLines((ActiveTextCollector)finder);
/* 45 */       Style clickedStyle = finder.result();
/* 46 */       if (clickedStyle != null) {
/* 47 */         this.componentClickHandler.accept(clickedStyle);
/*    */         return;
/*    */       } 
/*    */     } 
/* 52 */     super.onClick(event, doubleClick);
/*    */   }
/*    */   
/*    */   protected void updateWidgetNarration(NarrationElementOutput output) {}
/*    */   
/*    */   protected final Font getFont() {
/* 60 */     return this.font;
/*    */   }
/*    */   
/*    */   public void setMessage(Component message) {
/* 65 */     super.setMessage(message);
/* 66 */     setWidth(getFont().width(message.getVisualOrderText()));
/*    */   }
/*    */   
/*    */   public AbstractStringWidget setComponentClickHandler(Consumer<Style> clickEventConsumer) {
/* 70 */     this.componentClickHandler = clickEventConsumer;
/* 71 */     return this;
/*    */   }
/*    */   
/*    */   protected boolean handleStyleClick(Style style) {
/* 75 */     if (this.componentClickHandler != null && style.getClickEvent() != null) {
/* 76 */       this.componentClickHandler.accept(style);
/* 77 */       return true;
/*    */     } 
/* 79 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\AbstractStringWidget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */