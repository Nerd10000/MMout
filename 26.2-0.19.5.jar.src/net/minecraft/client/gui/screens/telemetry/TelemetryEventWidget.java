/*     */ package net.minecraft.client.gui.screens.telemetry;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.DoubleConsumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractScrollArea;
/*     */ import net.minecraft.client.gui.components.AbstractTextAreaWidget;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.layouts.Layout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.layouts.SpacerElement;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.telemetry.TelemetryEventType;
/*     */ import net.minecraft.client.telemetry.TelemetryProperty;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ 
/*     */ public class TelemetryEventWidget extends AbstractTextAreaWidget {
/*     */   private static final int HEADER_HORIZONTAL_PADDING = 32;
/*     */   
/*     */   private static final String TELEMETRY_REQUIRED_TRANSLATION_KEY = "telemetry.event.required";
/*     */   
/*     */   private static final String TELEMETRY_OPTIONAL_TRANSLATION_KEY = "telemetry.event.optional";
/*     */   
/*     */   private static final String TELEMETRY_OPTIONAL_DISABLED_TRANSLATION_KEY = "telemetry.event.optional.disabled";
/*     */   
/*  32 */   private static final Component PROPERTY_TITLE = (Component)Component.translatable("telemetry_info.property_title").withStyle(ChatFormatting.UNDERLINE);
/*     */   
/*     */   private final Font font;
/*     */   
/*     */   private Content content;
/*     */   
/*     */   private DoubleConsumer onScrolledListener;
/*     */   
/*     */   public TelemetryEventWidget(int x, int y, int width, int height, Font font) {
/*  39 */     super(x, y, width, height, (Component)Component.empty(), AbstractScrollArea.defaultSettings(9));
/*  41 */     this.font = font;
/*  42 */     this.content = buildContent(Minecraft.getInstance().telemetryOptInExtra());
/*     */   }
/*     */   
/*     */   public void onOptInChanged(boolean optIn) {
/*  46 */     this.content = buildContent(optIn);
/*  47 */     refreshScrollAmount();
/*     */   }
/*     */   
/*     */   public void updateLayout() {
/*  51 */     this.content = buildContent(Minecraft.getInstance().telemetryOptInExtra());
/*  52 */     refreshScrollAmount();
/*     */   }
/*     */   
/*     */   private Content buildContent(boolean hasOptedIn) {
/*  56 */     ContentBuilder content = new ContentBuilder(containerWidth());
/*  57 */     List<TelemetryEventType> eventTypes = new ArrayList<>(TelemetryEventType.values());
/*  58 */     eventTypes.sort(Comparator.comparing(TelemetryEventType::isOptIn));
/*  59 */     for (int i = 0; i < eventTypes.size(); i++) {
/*  60 */       TelemetryEventType eventType = eventTypes.get(i);
/*  61 */       boolean isDisabled = (eventType.isOptIn() && !hasOptedIn);
/*  63 */       addEventType(content, eventType, isDisabled);
/*  65 */       if (i < eventTypes.size() - 1) {
/*  66 */         Objects.requireNonNull(this.font);
/*  66 */         content.addSpacer(9);
/*     */       } 
/*     */     } 
/*  69 */     return content.build();
/*     */   }
/*     */   
/*     */   public void setOnScrolledListener(DoubleConsumer listener) {
/*  73 */     this.onScrolledListener = listener;
/*     */   }
/*     */   
/*     */   public void setScrollAmount(double scrollAmount) {
/*  78 */     super.setScrollAmount(scrollAmount);
/*  79 */     if (this.onScrolledListener != null)
/*  80 */       this.onScrolledListener.accept(scrollAmount()); 
/*     */   }
/*     */   
/*     */   protected int getInnerHeight() {
/*  86 */     return this.content.container().getHeight();
/*     */   }
/*     */   
/*     */   protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  91 */     int top = getInnerTop();
/*  92 */     int left = getInnerLeft();
/*  94 */     graphics.pose().pushMatrix();
/*  95 */     graphics.pose().translate(left, top);
/*  96 */     this.content.container().visitWidgets(widget -> widget.extractRenderState(graphics, mouseX, mouseY, a));
/*  97 */     graphics.pose().popMatrix();
/*     */   }
/*     */   
/*     */   protected void updateWidgetNarration(NarrationElementOutput output) {
/* 102 */     output.add(NarratedElementType.TITLE, this.content.narration());
/*     */   }
/*     */   
/*     */   private Component grayOutIfDisabled(Component component, boolean isDisabled) {
/* 106 */     if (isDisabled)
/* 107 */       return (Component)component.copy().withStyle(ChatFormatting.GRAY); 
/* 109 */     return component;
/*     */   }
/*     */   
/*     */   private void addEventType(ContentBuilder builder, TelemetryEventType eventType, boolean isDisabled) {
/* 113 */     String titleTranslationPattern = eventType.isOptIn() ? (isDisabled ? "telemetry.event.optional.disabled" : "telemetry.event.optional") : "telemetry.event.required";
/* 114 */     builder.addHeader(this.font, grayOutIfDisabled((Component)Component.translatable(titleTranslationPattern, new Object[] { eventType.title() }), isDisabled));
/* 115 */     builder.addHeader(this.font, (Component)eventType.description().withStyle(ChatFormatting.GRAY));
/* 116 */     Objects.requireNonNull(this.font);
/* 116 */     builder.addSpacer(9 / 2);
/* 118 */     builder.addLine(this.font, grayOutIfDisabled(PROPERTY_TITLE, isDisabled), 2);
/* 120 */     addEventTypeProperties(eventType, builder, isDisabled);
/*     */   }
/*     */   
/*     */   private void addEventTypeProperties(TelemetryEventType eventType, ContentBuilder content, boolean isDisabled) {
/* 124 */     for (TelemetryProperty<?> property : (Iterable<TelemetryProperty<?>>)eventType.properties())
/* 125 */       content.addLine(this.font, grayOutIfDisabled((Component)property.title(), isDisabled)); 
/*     */   }
/*     */   
/*     */   private int containerWidth() {
/* 130 */     return this.width - totalInnerPadding();
/*     */   }
/*     */   
/*     */   private static class ContentBuilder {
/*     */     private final int width;
/*     */     
/*     */     private final LinearLayout layout;
/*     */     
/* 138 */     private final MutableComponent narration = Component.empty();
/*     */     
/*     */     public ContentBuilder(int width) {
/* 141 */       this.width = width;
/* 143 */       this.layout = LinearLayout.vertical();
/* 144 */       this.layout.defaultCellSetting().alignHorizontallyLeft();
/* 146 */       this.layout.addChild((LayoutElement)SpacerElement.width(width));
/*     */     }
/*     */     
/*     */     public void addLine(Font font, Component line) {
/* 150 */       addLine(font, line, 0);
/*     */     }
/*     */     
/*     */     public void addLine(Font font, Component line, int paddingBottom) {
/* 154 */       this.layout.addChild((LayoutElement)(new MultiLineTextWidget(line, font)).setMaxWidth(this.width), s -> s.paddingBottom(paddingBottom));
/* 155 */       this.narration.append(line).append("\n");
/*     */     }
/*     */     
/*     */     public void addHeader(Font font, Component line) {
/* 159 */       this.layout.addChild((LayoutElement)(new MultiLineTextWidget(line, font)).setMaxWidth(this.width - 64).setCentered(true), s -> s.alignHorizontallyCenter().paddingHorizontal(32));
/* 160 */       this.narration.append(line).append("\n");
/*     */     }
/*     */     
/*     */     public void addSpacer(int height) {
/* 164 */       this.layout.addChild((LayoutElement)SpacerElement.height(height));
/*     */     }
/*     */     
/*     */     public TelemetryEventWidget.Content build() {
/* 168 */       this.layout.arrangeElements();
/* 169 */       return new TelemetryEventWidget.Content((Layout)this.layout, (Component)this.narration);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Content extends Record {
/*     */     private final Layout container;
/*     */     
/*     */     private final Component narration;
/*     */     
/*     */     private Content(Layout container, Component narration) {
/* 173 */       this.container = container;
/* 173 */       this.narration = narration;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Layout container() {
/* 173 */       return this.container;
/*     */     }
/*     */     
/*     */     public Component narration() {
/* 173 */       return this.narration;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\telemetry\TelemetryEventWidget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */