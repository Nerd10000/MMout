/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.OptionInstance;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.options.OptionsSubScreen;
/*     */ 
/*     */ public class Entry extends OptionsList.AbstractEntry {
/*     */   private final List<OptionsList.OptionInstanceWidget> children;
/*     */   
/*     */   private final Screen screen;
/*     */   
/*     */   private static final int X_OFFSET = 160;
/*     */   
/*     */   private Entry(List<OptionsList.OptionInstanceWidget> widgets, Screen screen) {
/* 144 */     this.children = widgets;
/* 145 */     this.screen = screen;
/*     */   }
/*     */   
/*     */   public static Entry big(Options options, OptionInstance<?> optionInstance, Screen screen) {
/* 149 */     return new Entry(List.of(new OptionsList.OptionInstanceWidget(optionInstance.createButton(options, 0, 0, 310), optionInstance)), screen);
/*     */   }
/*     */   
/*     */   public static Entry big(AbstractWidget widget, Screen screen) {
/* 153 */     widget.setWidth(310);
/* 154 */     return new Entry(List.of(new OptionsList.OptionInstanceWidget(widget, null)), screen);
/*     */   }
/*     */   
/*     */   public static Entry small(AbstractWidget leftWidget, AbstractWidget rightWidget, Screen screen) {
/* 158 */     if (rightWidget == null)
/* 159 */       return new Entry(List.of(new OptionsList.OptionInstanceWidget(leftWidget)), screen); 
/* 161 */     return new Entry(List.of(new OptionsList.OptionInstanceWidget(leftWidget), new OptionsList.OptionInstanceWidget(rightWidget)), screen);
/*     */   }
/*     */   
/*     */   public static Entry small(AbstractWidget leftWidget, OptionInstance<?> leftWidgetOptionInstance, AbstractWidget rightWidget, Screen screen) {
/* 165 */     if (rightWidget == null)
/* 166 */       return new Entry(List.of(new OptionsList.OptionInstanceWidget(leftWidget, leftWidgetOptionInstance)), screen); 
/* 168 */     return new Entry(List.of(new OptionsList.OptionInstanceWidget(leftWidget, leftWidgetOptionInstance), new OptionsList.OptionInstanceWidget(rightWidget)), screen);
/*     */   }
/*     */   
/*     */   public static Entry small(Options options, OptionInstance<?> optionA, OptionInstance<?> optionB, OptionsSubScreen screen) {
/* 172 */     AbstractWidget buttonA = optionA.createButton(options);
/* 173 */     if (optionB == null)
/* 174 */       return new Entry(List.of(new OptionsList.OptionInstanceWidget(buttonA, optionA)), (Screen)screen); 
/* 176 */     return new Entry(List.of(new OptionsList.OptionInstanceWidget(buttonA, optionA), new OptionsList.OptionInstanceWidget(optionB.createButton(options), optionB)), (Screen)screen);
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 181 */     int xOffset = 0;
/* 182 */     int x = this.screen.width / 2 - 155;
/* 183 */     for (OptionsList.OptionInstanceWidget optionInstanceWidget : this.children) {
/* 184 */       optionInstanceWidget.widget().setPosition(x + xOffset, getContentY());
/* 185 */       optionInstanceWidget.widget().extractRenderState(graphics, mouseX, mouseY, a);
/* 186 */       xOffset += 160;
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 192 */     return Lists.transform(this.children, OptionsList.OptionInstanceWidget::widget);
/*     */   }
/*     */   
/*     */   public List<? extends NarratableEntry> narratables() {
/* 197 */     return Lists.transform(this.children, OptionsList.OptionInstanceWidget::widget);
/*     */   }
/*     */   
/*     */   public AbstractWidget findOption(OptionInstance<?> option) {
/* 201 */     for (OptionsList.OptionInstanceWidget child : this.children) {
/* 202 */       if (child.optionInstance == option)
/* 203 */         return child.widget(); 
/*     */     } 
/* 206 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\OptionsList$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */