/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.OptionInstance;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.options.OptionsSubScreen;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class OptionsList extends ContainerObjectSelectionList<OptionsList.AbstractEntry> {
/*     */   private static final int BIG_BUTTON_WIDTH = 310;
/*     */   
/*     */   private static final int DEFAULT_ITEM_HEIGHT = 25;
/*     */   
/*     */   private final OptionsSubScreen screen;
/*     */   
/*     */   public OptionsList(Minecraft minecraft, int width, OptionsSubScreen screen) {
/*  25 */     super(minecraft, width, screen.layout.getContentHeight(), screen.layout.getHeaderHeight(), 25);
/*  26 */     this.centerListVertically = false;
/*  27 */     this.screen = screen;
/*     */   }
/*     */   
/*     */   public void addBig(OptionInstance<?> option) {
/*  31 */     addEntry(Entry.big(this.minecraft.options, option, (Screen)this.screen));
/*     */   }
/*     */   
/*     */   public void addBig(AbstractWidget widget) {
/*  35 */     addEntry(Entry.big(widget, (Screen)this.screen));
/*     */   }
/*     */   
/*     */   public void addSmall(OptionInstance<?>... options) {
/*  39 */     for (int i = 0; i < options.length; i += 2) {
/*  40 */       OptionInstance<?> secondOption = (i < options.length - 1) ? options[i + 1] : null;
/*  41 */       addEntry(Entry.small(this.minecraft.options, options[i], secondOption, this.screen));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addSmall(List<AbstractWidget> widgets) {
/*  46 */     for (int i = 0; i < widgets.size(); i += 2)
/*  47 */       addSmall(widgets.get(i), (i < widgets.size() - 1) ? widgets.get(i + 1) : null); 
/*     */   }
/*     */   
/*     */   public void addSmall(AbstractWidget firstOption, AbstractWidget secondOption) {
/*  52 */     addEntry(Entry.small(firstOption, secondOption, (Screen)this.screen));
/*     */   }
/*     */   
/*     */   public void addSmall(AbstractWidget firstOption, OptionInstance<?> firstOptionInstance, AbstractWidget secondOption) {
/*  56 */     addEntry(Entry.small(firstOption, firstOptionInstance, secondOption, (Screen)this.screen));
/*     */   }
/*     */   
/*     */   public void addHeader(Component text) {
/*  60 */     Objects.requireNonNull(this.minecraft.font);
/*  60 */     int lineHeight = 9;
/*  61 */     int paddingTop = children().isEmpty() ? 0 : (lineHeight * 2);
/*  62 */     addEntry(new HeaderEntry((Screen)this.screen, text, paddingTop), paddingTop + lineHeight + 4);
/*     */   }
/*     */   
/*     */   public int getRowWidth() {
/*  67 */     return 310;
/*     */   }
/*     */   
/*     */   public AbstractWidget findOption(OptionInstance<?> option) {
/*  71 */     for (AbstractEntry child : children()) {
/*  72 */       if (child instanceof Entry) {
/*  72 */         Entry entry = (Entry)child;
/*  73 */         AbstractWidget widgetForOption = entry.findOption(option);
/*  74 */         if (widgetForOption != null)
/*  75 */           return widgetForOption; 
/*     */       } 
/*     */     } 
/*  79 */     return null;
/*     */   }
/*     */   
/*     */   public void applyUnsavedChanges() {
/*  83 */     for (AbstractEntry child : children()) {
/*  84 */       if (child instanceof Entry) {
/*  84 */         Entry entry = (Entry)child;
/*  85 */         for (OptionInstanceWidget optionInstanceWidget : entry.children) {
/*  86 */           if (optionInstanceWidget.optionInstance() != null) {
/*  86 */             AbstractWidget abstractWidget = optionInstanceWidget.widget();
/*  86 */             if (abstractWidget instanceof OptionInstance.OptionInstanceSliderButton) {
/*  86 */               OptionInstance.OptionInstanceSliderButton<?> optionSlider = (OptionInstance.OptionInstanceSliderButton)abstractWidget;
/*  87 */               optionSlider.applyUnsavedValue();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resetOption(OptionInstance<?> option) {
/*  95 */     for (AbstractEntry child : children()) {
/*  96 */       if (child instanceof Entry) {
/*  96 */         Entry entry = (Entry)child;
/*  97 */         for (OptionInstanceWidget optionInstanceWidget : entry.children) {
/*  98 */           if (optionInstanceWidget.optionInstance() == option) {
/*  98 */             AbstractWidget abstractWidget = optionInstanceWidget.widget();
/*  98 */             if (abstractWidget instanceof ResettableOptionWidget) {
/*  98 */               ResettableOptionWidget resettableOptionWidget = (ResettableOptionWidget)abstractWidget;
/*  99 */               resettableOptionWidget.resetValue();
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static abstract class AbstractEntry extends ContainerObjectSelectionList.Entry<AbstractEntry> {}
/*     */   
/*     */   protected static class HeaderEntry extends AbstractEntry {
/*     */     private final Screen screen;
/*     */     
/*     */     private final int paddingTop;
/*     */     
/*     */     private final StringWidget widget;
/*     */     
/*     */     protected HeaderEntry(Screen screen, Component text, int paddingTop) {
/* 116 */       this.screen = screen;
/* 117 */       this.paddingTop = paddingTop;
/* 118 */       this.widget = new StringWidget(text, screen.getFont());
/*     */     }
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/* 123 */       return List.of(this.widget);
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 128 */       this.widget.setPosition(this.screen.width / 2 - 155, getContentY() + this.paddingTop);
/* 129 */       this.widget.extractRenderState(graphics, mouseX, mouseY, a);
/*     */     }
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 134 */       return List.of(this.widget);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class Entry extends AbstractEntry {
/*     */     private final List<OptionsList.OptionInstanceWidget> children;
/*     */     
/*     */     private final Screen screen;
/*     */     
/*     */     private static final int X_OFFSET = 160;
/*     */     
/*     */     private Entry(List<OptionsList.OptionInstanceWidget> widgets, Screen screen) {
/* 144 */       this.children = widgets;
/* 145 */       this.screen = screen;
/*     */     }
/*     */     
/*     */     public static Entry big(Options options, OptionInstance<?> optionInstance, Screen screen) {
/* 149 */       return new Entry(List.of(new OptionsList.OptionInstanceWidget(optionInstance.createButton(options, 0, 0, 310), optionInstance)), screen);
/*     */     }
/*     */     
/*     */     public static Entry big(AbstractWidget widget, Screen screen) {
/* 153 */       widget.setWidth(310);
/* 154 */       return new Entry(List.of(new OptionsList.OptionInstanceWidget(widget, null)), screen);
/*     */     }
/*     */     
/*     */     public static Entry small(AbstractWidget leftWidget, AbstractWidget rightWidget, Screen screen) {
/* 158 */       if (rightWidget == null)
/* 159 */         return new Entry(List.of(new OptionsList.OptionInstanceWidget(leftWidget)), screen); 
/* 161 */       return new Entry(List.of(new OptionsList.OptionInstanceWidget(leftWidget), new OptionsList.OptionInstanceWidget(rightWidget)), screen);
/*     */     }
/*     */     
/*     */     public static Entry small(AbstractWidget leftWidget, OptionInstance<?> leftWidgetOptionInstance, AbstractWidget rightWidget, Screen screen) {
/* 165 */       if (rightWidget == null)
/* 166 */         return new Entry(List.of(new OptionsList.OptionInstanceWidget(leftWidget, leftWidgetOptionInstance)), screen); 
/* 168 */       return new Entry(List.of(new OptionsList.OptionInstanceWidget(leftWidget, leftWidgetOptionInstance), new OptionsList.OptionInstanceWidget(rightWidget)), screen);
/*     */     }
/*     */     
/*     */     public static Entry small(Options options, OptionInstance<?> optionA, OptionInstance<?> optionB, OptionsSubScreen screen) {
/* 172 */       AbstractWidget buttonA = optionA.createButton(options);
/* 173 */       if (optionB == null)
/* 174 */         return new Entry(List.of(new OptionsList.OptionInstanceWidget(buttonA, optionA)), (Screen)screen); 
/* 176 */       return new Entry(List.of(new OptionsList.OptionInstanceWidget(buttonA, optionA), new OptionsList.OptionInstanceWidget(optionB.createButton(options), optionB)), (Screen)screen);
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 181 */       int xOffset = 0;
/* 182 */       int x = this.screen.width / 2 - 155;
/* 183 */       for (OptionsList.OptionInstanceWidget optionInstanceWidget : this.children) {
/* 184 */         optionInstanceWidget.widget().setPosition(x + xOffset, getContentY());
/* 185 */         optionInstanceWidget.widget().extractRenderState(graphics, mouseX, mouseY, a);
/* 186 */         xOffset += 160;
/*     */       } 
/*     */     }
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 192 */       return Lists.transform(this.children, OptionsList.OptionInstanceWidget::widget);
/*     */     }
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/* 197 */       return Lists.transform(this.children, OptionsList.OptionInstanceWidget::widget);
/*     */     }
/*     */     
/*     */     public AbstractWidget findOption(OptionInstance<?> option) {
/* 201 */       for (OptionsList.OptionInstanceWidget child : this.children) {
/* 202 */         if (child.optionInstance == option)
/* 203 */           return child.widget(); 
/*     */       } 
/* 206 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class OptionInstanceWidget extends Record {
/*     */     private final AbstractWidget widget;
/*     */     
/*     */     private final OptionInstance<?> optionInstance;
/*     */     
/*     */     public OptionInstance<?> optionInstance() {
/* 210 */       return this.optionInstance;
/*     */     }
/*     */     
/*     */     public AbstractWidget widget() {
/* 210 */       return this.widget;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #210	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #210	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #210	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget;
/*     */     }
/*     */     
/*     */     public OptionInstanceWidget(AbstractWidget widget, OptionInstance<?> optionInstance) {
/* 210 */       this.widget = widget;
/* 210 */       this.optionInstance = optionInstance;
/*     */     }
/*     */     
/*     */     public OptionInstanceWidget(AbstractWidget widget) {
/* 212 */       this(widget, null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\OptionsList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */