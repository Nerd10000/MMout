/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ 
/*     */ import com.mojang.realmsclient.dto.WorldTemplate;
/*     */ import com.mojang.realmsclient.util.RealmsTextureManager;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class Entry extends ObjectSelectionList.Entry<RealmsSelectWorldTemplateScreen.Entry> {
/* 280 */   private static final WidgetSprites WEBSITE_LINK_SPRITES = new WidgetSprites(
/* 281 */       Identifier.withDefaultNamespace("icon/link"), 
/* 282 */       Identifier.withDefaultNamespace("icon/link_highlighted"));
/*     */   
/* 284 */   private static final WidgetSprites TRAILER_LINK_SPRITES = new WidgetSprites(
/* 285 */       Identifier.withDefaultNamespace("icon/video_link"), 
/* 286 */       Identifier.withDefaultNamespace("icon/video_link_highlighted"));
/*     */   
/* 289 */   private static final Component PUBLISHER_LINK_TOOLTIP = (Component)Component.translatable("mco.template.info.tooltip");
/*     */   
/* 290 */   private static final Component TRAILER_LINK_TOOLTIP = (Component)Component.translatable("mco.template.trailer.tooltip");
/*     */   
/*     */   public final WorldTemplate template;
/*     */   
/*     */   private ImageButton websiteButton;
/*     */   
/*     */   private ImageButton trailerButton;
/*     */   
/*     */   public Entry(WorldTemplate template) {
/* 298 */     this.template = template;
/* 299 */     if (!template.link().isBlank()) {
/* 300 */       this
/*     */         
/* 302 */         .websiteButton = new ImageButton(15, 15, WEBSITE_LINK_SPRITES, ConfirmLinkScreen.confirmLink((Screen)paramRealmsSelectWorldTemplateScreen, template.link()), PUBLISHER_LINK_TOOLTIP);
/* 304 */       this.websiteButton.setTooltip(Tooltip.create(PUBLISHER_LINK_TOOLTIP));
/*     */     } 
/* 306 */     if (!template.trailer().isBlank()) {
/* 307 */       this
/*     */         
/* 309 */         .trailerButton = new ImageButton(15, 15, TRAILER_LINK_SPRITES, ConfirmLinkScreen.confirmLink((Screen)paramRealmsSelectWorldTemplateScreen, template.trailer()), TRAILER_LINK_TOOLTIP);
/* 311 */       this.trailerButton.setTooltip(Tooltip.create(TRAILER_LINK_TOOLTIP));
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 317 */     RealmsSelectWorldTemplateScreen.this.selectedTemplate = this.template;
/* 318 */     RealmsSelectWorldTemplateScreen.this.updateButtonStates();
/* 319 */     if (doubleClick && isFocused())
/* 320 */       RealmsSelectWorldTemplateScreen.this.callback.accept(this.template); 
/* 322 */     if (this.websiteButton != null)
/* 323 */       this.websiteButton.mouseClicked(event, doubleClick); 
/* 325 */     if (this.trailerButton != null)
/* 326 */       this.trailerButton.mouseClicked(event, doubleClick); 
/* 329 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 334 */     graphics.blit(RenderPipelines.GUI_TEXTURED, RealmsTextureManager.worldTemplate(this.template.id(), this.template.image()), getContentX() + 1, getContentY() + 1 + 1, 0.0F, 0.0F, 38, 38, 38, 38);
/* 335 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsSelectWorldTemplateScreen.SLOT_FRAME_SPRITE, getContentX(), getContentY() + 1, 40, 40);
/* 336 */     int padding = 5;
/* 337 */     int versionTextWidth = RealmsSelectWorldTemplateScreen.access$100(RealmsSelectWorldTemplateScreen.this).width(this.template.version());
/* 339 */     if (this.websiteButton != null) {
/* 340 */       this.websiteButton.setPosition(getContentRight() - versionTextWidth - this.websiteButton.getWidth() - 10, getContentY());
/* 341 */       this.websiteButton.extractRenderState(graphics, mouseX, mouseY, a);
/*     */     } 
/* 343 */     if (this.trailerButton != null) {
/* 344 */       this.trailerButton.setPosition(getContentRight() - versionTextWidth - this.trailerButton.getWidth() * 2 - 15, getContentY());
/* 345 */       this.trailerButton.extractRenderState(graphics, mouseX, mouseY, a);
/*     */     } 
/* 347 */     int textX = getContentX() + 45 + 20;
/* 348 */     int textY = getContentY() + 5;
/* 349 */     graphics.text(RealmsSelectWorldTemplateScreen.access$200(RealmsSelectWorldTemplateScreen.this), this.template.name(), textX, textY, -1);
/* 350 */     graphics.text(RealmsSelectWorldTemplateScreen.access$300(RealmsSelectWorldTemplateScreen.this), this.template.version(), getContentRight() - versionTextWidth - 5, textY, -6250336);
/* 351 */     Objects.requireNonNull(RealmsSelectWorldTemplateScreen.access$500(RealmsSelectWorldTemplateScreen.this));
/* 351 */     graphics.text(RealmsSelectWorldTemplateScreen.access$400(RealmsSelectWorldTemplateScreen.this), this.template.author(), textX, textY + 9 + 5, -6250336);
/* 352 */     if (!this.template.recommendedPlayers().isBlank()) {
/* 353 */       Objects.requireNonNull(RealmsSelectWorldTemplateScreen.access$700(RealmsSelectWorldTemplateScreen.this));
/* 353 */       graphics.text(RealmsSelectWorldTemplateScreen.access$600(RealmsSelectWorldTemplateScreen.this), this.template.recommendedPlayers(), textX, getContentBottom() - 9 / 2 - 5, -8355712);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 359 */     Component entryName = CommonComponents.joinLines(new Component[] { (Component)Component.literal(this.template.name()), 
/* 361 */           (Component)Component.translatable("mco.template.select.narrate.authors", new Object[] { this.template.author() }), (Component)Component.literal(this.template.recommendedPlayers()), 
/* 363 */           (Component)Component.translatable("mco.template.select.narrate.version", new Object[] { this.template.version() }) });
/* 365 */     return (Component)Component.translatable("narrator.select", new Object[] { entryName });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsSelectWorldTemplateScreen$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */