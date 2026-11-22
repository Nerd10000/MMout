/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ActiveTextCollector;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.input.InputWithModifiers;
/*    */ import net.minecraft.client.input.KeyEvent;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ARGB;
/*    */ 
/*    */ public abstract class AbstractButton extends AbstractWidget.WithInactiveMessage {
/*    */   protected static final int TEXT_MARGIN = 2;
/*    */   
/* 20 */   private static final WidgetSprites SPRITES = new WidgetSprites(
/* 21 */       Identifier.withDefaultNamespace("widget/button"), 
/* 22 */       Identifier.withDefaultNamespace("widget/button_disabled"), 
/* 23 */       Identifier.withDefaultNamespace("widget/button_highlighted"));
/*    */   
/*    */   private Supplier<Boolean> overrideRenderHighlightedSprite;
/*    */   
/*    */   public AbstractButton(int x, int y, int width, int height, Component message) {
/* 29 */     super(x, y, width, height, message);
/*    */   }
/*    */   
/*    */   public abstract void onPress(InputWithModifiers paramInputWithModifiers);
/*    */   
/*    */   protected final void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 36 */     extractContents(graphics, mouseX, mouseY, a);
/* 38 */     handleCursor(graphics);
/*    */   }
/*    */   
/*    */   protected abstract void extractContents(GuiGraphicsExtractor paramGuiGraphicsExtractor, int paramInt1, int paramInt2, float paramFloat);
/*    */   
/*    */   protected void extractDefaultLabel(ActiveTextCollector output) {
/* 44 */     extractScrollingStringOverContents(output, getMessage(), 2);
/*    */   }
/*    */   
/*    */   protected final void extractDefaultSprite(GuiGraphicsExtractor graphics) {
/* 48 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITES.get(this.active, (this.overrideRenderHighlightedSprite != null) ? ((Boolean)this.overrideRenderHighlightedSprite.get()).booleanValue() : isHoveredOrFocused()), getX(), getY(), getWidth(), getHeight(), ARGB.white(this.alpha));
/*    */   }
/*    */   
/*    */   public void onClick(MouseButtonEvent event, boolean doubleClick) {
/* 53 */     onPress((InputWithModifiers)event);
/*    */   }
/*    */   
/*    */   public boolean keyPressed(KeyEvent event) {
/* 58 */     if (!isActive())
/* 59 */       return false; 
/* 61 */     if (event.isSelection()) {
/* 62 */       playDownSound(Minecraft.getInstance().getSoundManager());
/* 63 */       onPress((InputWithModifiers)event);
/* 64 */       return true;
/*    */     } 
/* 66 */     return false;
/*    */   }
/*    */   
/*    */   public void setOverrideRenderHighlightedSprite(Supplier<Boolean> overrideRenderHighlightedSprite) {
/* 70 */     this.overrideRenderHighlightedSprite = overrideRenderHighlightedSprite;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\AbstractButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */