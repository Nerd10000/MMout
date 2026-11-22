/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class HeaderEntry extends OptionsList.AbstractEntry {
/*     */   private final Screen screen;
/*     */   
/*     */   private final int paddingTop;
/*     */   
/*     */   private final StringWidget widget;
/*     */   
/*     */   protected HeaderEntry(Screen screen, Component text, int paddingTop) {
/* 116 */     this.screen = screen;
/* 117 */     this.paddingTop = paddingTop;
/* 118 */     this.widget = new StringWidget(text, screen.getFont());
/*     */   }
/*     */   
/*     */   public List<? extends NarratableEntry> narratables() {
/* 123 */     return List.of(this.widget);
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 128 */     this.widget.setPosition(this.screen.width / 2 - 155, getContentY() + this.paddingTop);
/* 129 */     this.widget.extractRenderState(graphics, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 134 */     return List.of(this.widget);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\OptionsList$HeaderEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */