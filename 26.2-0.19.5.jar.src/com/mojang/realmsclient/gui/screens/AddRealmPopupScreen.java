/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ 
/*     */ public class AddRealmPopupScreen extends RealmsScreen {
/*  25 */   private static final Component POPUP_TEXT = (Component)Component.translatable("mco.selectServer.popup");
/*     */   
/*  26 */   private static final Component CLOSE_TEXT = (Component)Component.translatable("mco.selectServer.close");
/*     */   
/*  28 */   private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("popup/background");
/*     */   
/*  29 */   private static final Identifier TRIAL_AVAILABLE_SPRITE = Identifier.withDefaultNamespace("icon/trial_available");
/*     */   
/*  31 */   private static final WidgetSprites CROSS_BUTTON_SPRITES = new WidgetSprites(
/*  32 */       Identifier.withDefaultNamespace("widget/cross_button"), 
/*  33 */       Identifier.withDefaultNamespace("widget/cross_button_highlighted"));
/*     */   
/*     */   private static final int IMAGE_WIDTH = 195;
/*     */   
/*     */   private static final int IMAGE_HEIGHT = 152;
/*     */   
/*     */   private static final int BG_BORDER_SIZE = 6;
/*     */   
/*     */   private static final int BUTTON_SPACING = 4;
/*     */   
/*     */   private static final int PADDING = 10;
/*     */   
/*     */   private static final int WIDTH = 320;
/*     */   
/*     */   private static final int HEIGHT = 172;
/*     */   
/*     */   private static final int TEXT_WIDTH = 100;
/*     */   
/*     */   private static final int BUTTON_WIDTH = 99;
/*     */   
/*     */   private static final int CAROUSEL_SWITCH_INTERVAL = 100;
/*     */   
/*  49 */   private static List<Identifier> carouselImages = List.of();
/*     */   
/*     */   private final Screen backgroundScreen;
/*     */   
/*     */   private final boolean trialAvailable;
/*     */   
/*     */   private Button createTrialButton;
/*     */   
/*     */   private int carouselIndex;
/*     */   
/*     */   private int carouselTick;
/*     */   
/*     */   public AddRealmPopupScreen(Screen backgroundScreen, boolean trialAvailable) {
/*  60 */     super(POPUP_TEXT);
/*  61 */     this.backgroundScreen = backgroundScreen;
/*  62 */     this.trialAvailable = trialAvailable;
/*     */   }
/*     */   
/*     */   public static void updateCarouselImages(ResourceManager resourceManager) {
/*  66 */     Collection<Identifier> candidates = resourceManager.listResources("textures/gui/images", s -> s.getPath().endsWith(".png")).keySet();
/*  67 */     carouselImages = candidates.stream().filter(id -> id.getNamespace().equals("realms")).toList();
/*     */   }
/*     */   
/*     */   protected void init() {
/*  72 */     this.backgroundScreen.resize(this.width, this.height);
/*  74 */     if (this.trialAvailable)
/*  75 */       this.createTrialButton = (Button)addRenderableWidget(
/*  76 */           (GuiEventListener)Button.builder((Component)Component.translatable("mco.selectServer.trial"), 
/*  77 */             ConfirmLinkScreen.confirmLink((Screen)this, CommonLinks.START_REALMS_TRIAL))
/*  78 */           .bounds(right() - 10 - 99, bottom() - 10 - 4 - 40, 99, 20).build()); 
/*  82 */     addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable("mco.selectServer.buy"), 
/*  83 */           ConfirmLinkScreen.confirmLink((Screen)this, CommonLinks.BUY_REALMS))
/*  84 */         .bounds(right() - 10 - 99, bottom() - 10 - 20, 99, 20).build());
/*  86 */     ImageButton closeButton = (ImageButton)addRenderableWidget((GuiEventListener)new ImageButton(left() + 4, top() + 4, 14, 14, CROSS_BUTTON_SPRITES, button -> onClose(), CLOSE_TEXT));
/*  87 */     closeButton.setTooltip(Tooltip.create(CLOSE_TEXT));
/*  89 */     int textBoxHeight = 142 - (this.trialAvailable ? 40 : 20);
/*  90 */     FittingMultiLineTextWidget fittingMultiLineTextWidget = new FittingMultiLineTextWidget(right() - 10 - 100, top() + 10, 100, textBoxHeight, POPUP_TEXT, this.font);
/*  91 */     if (fittingMultiLineTextWidget.showingScrollBar())
/*  92 */       fittingMultiLineTextWidget.setWidth(100 - fittingMultiLineTextWidget.scrollbarWidth()); 
/*  94 */     addRenderableWidget((GuiEventListener)fittingMultiLineTextWidget);
/*     */   }
/*     */   
/*     */   public void tick() {
/*  99 */     super.tick();
/* 100 */     if (++this.carouselTick > 100) {
/* 101 */       this.carouselTick = 0;
/* 102 */       this.carouselIndex = (this.carouselIndex + 1) % carouselImages.size();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 108 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/* 110 */     if (this.createTrialButton != null)
/* 111 */       extractDiamond(graphics, this.createTrialButton); 
/*     */   }
/*     */   
/*     */   public static void extractDiamond(GuiGraphicsExtractor graphics, Button button) {
/* 116 */     int size = 8;
/* 117 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TRIAL_AVAILABLE_SPRITE, button.getX() + button.getWidth() - 8 - 4, button.getY() + button.getHeight() / 2 - 4, 8, 8);
/*     */   }
/*     */   
/*     */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 122 */     this.backgroundScreen.extractBackground(graphics, -1, -1, a);
/* 123 */     graphics.nextStratum();
/* 124 */     this.backgroundScreen.extractRenderState(graphics, -1, -1, a);
/* 125 */     graphics.nextStratum();
/* 126 */     extractTransparentBackground(graphics);
/* 127 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, left(), top(), 320, 172);
/* 128 */     if (!carouselImages.isEmpty())
/* 129 */       graphics.blit(RenderPipelines.GUI_TEXTURED, carouselImages.get(this.carouselIndex), left() + 10, top() + 10, 0.0F, 0.0F, 195, 152, 195, 152); 
/*     */   }
/*     */   
/*     */   private int left() {
/* 134 */     return (this.width - 320) / 2;
/*     */   }
/*     */   
/*     */   private int top() {
/* 138 */     return (this.height - 172) / 2;
/*     */   }
/*     */   
/*     */   private int right() {
/* 142 */     return left() + 320;
/*     */   }
/*     */   
/*     */   private int bottom() {
/* 146 */     return top() + 172;
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 151 */     this.minecraft.gui.setScreen(this.backgroundScreen);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\AddRealmPopupScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */