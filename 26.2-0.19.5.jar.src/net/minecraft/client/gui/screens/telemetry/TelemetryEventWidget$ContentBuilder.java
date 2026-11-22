/*     */ package net.minecraft.client.gui.screens.telemetry;
/*     */ 
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.layouts.Layout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.layouts.SpacerElement;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ 
/*     */ class ContentBuilder {
/*     */   private final int width;
/*     */   
/*     */   private final LinearLayout layout;
/*     */   
/* 138 */   private final MutableComponent narration = Component.empty();
/*     */   
/*     */   public ContentBuilder(int width) {
/* 141 */     this.width = width;
/* 143 */     this.layout = LinearLayout.vertical();
/* 144 */     this.layout.defaultCellSetting().alignHorizontallyLeft();
/* 146 */     this.layout.addChild((LayoutElement)SpacerElement.width(width));
/*     */   }
/*     */   
/*     */   public void addLine(Font font, Component line) {
/* 150 */     addLine(font, line, 0);
/*     */   }
/*     */   
/*     */   public void addLine(Font font, Component line, int paddingBottom) {
/* 154 */     this.layout.addChild((LayoutElement)(new MultiLineTextWidget(line, font)).setMaxWidth(this.width), s -> s.paddingBottom(paddingBottom));
/* 155 */     this.narration.append(line).append("\n");
/*     */   }
/*     */   
/*     */   public void addHeader(Font font, Component line) {
/* 159 */     this.layout.addChild((LayoutElement)(new MultiLineTextWidget(line, font)).setMaxWidth(this.width - 64).setCentered(true), s -> s.alignHorizontallyCenter().paddingHorizontal(32));
/* 160 */     this.narration.append(line).append("\n");
/*     */   }
/*     */   
/*     */   public void addSpacer(int height) {
/* 164 */     this.layout.addChild((LayoutElement)SpacerElement.height(height));
/*     */   }
/*     */   
/*     */   public TelemetryEventWidget.Content build() {
/* 168 */     this.layout.arrangeElements();
/* 169 */     return new TelemetryEventWidget.Content((Layout)this.layout, (Component)this.narration);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\telemetry\TelemetryEventWidget$ContentBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */