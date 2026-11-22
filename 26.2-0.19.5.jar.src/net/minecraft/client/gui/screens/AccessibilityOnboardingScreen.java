/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import com.mojang.text2speech.Narrator;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CommonButtons;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*     */ import net.minecraft.client.gui.components.LogoRenderer;
/*     */ import net.minecraft.client.gui.components.SpriteIconButton;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
/*     */ import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
/*     */ import net.minecraft.client.gui.screens.options.SoundOptionsScreen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class AccessibilityOnboardingScreen extends Screen {
/*  30 */   private static final Component TITLE = (Component)Component.translatable("accessibility.onboarding.screen.title");
/*     */   
/*  31 */   private static final Component ONBOARDING_NARRATOR_MESSAGE = (Component)Component.translatable("accessibility.onboarding.screen.narrator");
/*     */   
/*     */   private static final int PADDING = 4;
/*     */   
/*     */   private static final int TITLE_PADDING = 16;
/*     */   
/*     */   private static final float FADE_OUT_TIME = 1000.0F;
/*     */   
/*     */   private static final int TEXT_WIDGET_WIDTH = 374;
/*     */   
/*     */   private final LogoRenderer logoRenderer;
/*     */   
/*     */   private final Options options;
/*     */   
/*     */   private final boolean narratorAvailable;
/*     */   
/*     */   private boolean hasNarrated;
/*     */   
/*     */   private float timer;
/*     */   
/*     */   private final Runnable onClose;
/*     */   
/*  47 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, initTitleYPos(), 33);
/*     */   
/*     */   private FocusableTextWidget focusableTextWidget;
/*     */   
/*     */   private float fadeInStart;
/*     */   
/*     */   private boolean fadingIn = true;
/*     */   
/*     */   private float fadeOutStart;
/*     */   
/*     */   public AccessibilityOnboardingScreen(Options options, Runnable onClose) {
/*  56 */     super(TITLE);
/*  57 */     this.options = options;
/*  58 */     this.onClose = onClose;
/*  59 */     this.logoRenderer = new LogoRenderer(true);
/*  60 */     this.narratorAvailable = Minecraft.getInstance().getNarrator().isActive();
/*  61 */     this.minecraft.gameRenderer.panorama().holdSpin();
/*     */   }
/*     */   
/*     */   public void init() {
/*  66 */     LinearLayout content = (LinearLayout)this.layout.addToContents((LayoutElement)LinearLayout.vertical());
/*  67 */     content.defaultCellSetting().alignHorizontallyCenter().padding(4);
/*  69 */     this.focusableTextWidget = (FocusableTextWidget)content.addChild((LayoutElement)FocusableTextWidget.builder(this.title, this.font).maxWidth(374).build(), w -> w.padding(8));
/*  71 */     GridLayout grid = (GridLayout)content.addChild((LayoutElement)new GridLayout());
/*  72 */     grid.defaultCellSetting().padding(4);
/*  73 */     GridLayout.RowHelper rowHelper = grid.createRowHelper(2);
/*  74 */     AbstractWidget abstractWidget = this.options.narrator().createButton(this.options);
/*  74 */     if (abstractWidget instanceof CycleButton) {
/*  74 */       CycleButton<?> cycleButton = (CycleButton)abstractWidget;
/*  75 */       this.narratorButton = (CycleButton)cycleButton;
/*  76 */       this.narratorButton.active = this.narratorAvailable;
/*  77 */       rowHelper.addChild((LayoutElement)this.narratorButton);
/*     */     } 
/*  79 */     rowHelper.addChild(
/*  80 */         (LayoutElement)SpriteIconButton.builder((Component)Component.translatable("options.sounds"), button -> closeAndSetScreen((Screen)new SoundOptionsScreen(this, this.options)), false)
/*  81 */         .width(150)
/*  82 */         .sprite(Identifier.withDefaultNamespace("icon/music_notes"), 16, 16)
/*  83 */         .build());
/*  85 */     rowHelper.addChild((LayoutElement)CommonButtons.accessibility(150, button -> closeAndSetScreen((Screen)new AccessibilityOptionsScreen(this, this.minecraft.options)), false));
/*  86 */     rowHelper.addChild((LayoutElement)CommonButtons.language(150, button -> closeAndSetScreen((Screen)new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager())), false));
/*  88 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_CONTINUE, button -> onClose()).build());
/*  90 */     this.layout.visitWidgets(this::addRenderableWidget);
/*  91 */     repositionElements();
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/*  96 */     if (this.focusableTextWidget != null)
/*  97 */       this.focusableTextWidget.updateHeight(); 
/*  99 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   protected void setInitialFocus() {
/* 104 */     if (this.narratorAvailable && this.narratorButton != null) {
/* 105 */       setInitialFocus((GuiEventListener)this.narratorButton);
/*     */     } else {
/* 107 */       super.setInitialFocus();
/*     */     } 
/*     */   }
/*     */   
/*     */   private int initTitleYPos() {
/* 112 */     return 90;
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 117 */     if (this.fadeOutStart == 0.0F)
/* 118 */       this.fadeOutStart = (float)Util.getMillis(); 
/*     */   }
/*     */   
/*     */   private void closeAndSetScreen(Screen screen) {
/* 123 */     close(false, () -> this.minecraft.gui.setScreen(screen));
/*     */   }
/*     */   
/*     */   private void close(boolean onboardingFinished, Runnable runnable) {
/* 127 */     if (onboardingFinished)
/* 128 */       this.options.onboardingAccessibilityFinished(); 
/* 130 */     Narrator.getNarrator().clear();
/* 131 */     runnable.run();
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 136 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/* 138 */     handleInitialNarrationDelay();
/* 140 */     if (this.fadeInStart == 0.0F && this.fadingIn)
/* 141 */       this.fadeInStart = (float)Util.getMillis(); 
/* 144 */     if (this.fadeInStart > 0.0F) {
/* 145 */       float fade = ((float)Util.getMillis() - this.fadeInStart) / 2000.0F;
/* 146 */       float widgetAlpha = 1.0F;
/* 147 */       if (fade >= 1.0F) {
/* 148 */         this.fadingIn = false;
/* 149 */         this.fadeInStart = 0.0F;
/*     */       } else {
/* 151 */         fade = Mth.clamp(fade, 0.0F, 1.0F);
/* 152 */         widgetAlpha = Mth.clampedMap(fade, 0.5F, 1.0F, 0.0F, 1.0F);
/*     */       } 
/* 154 */       fadeWidgets(widgetAlpha);
/*     */     } 
/* 157 */     if (this.fadeOutStart > 0.0F) {
/* 158 */       float fade = 1.0F - ((float)Util.getMillis() - this.fadeOutStart) / 1000.0F;
/* 159 */       float widgetAlpha = 0.0F;
/* 160 */       if (fade <= 0.0F) {
/* 161 */         this.fadeOutStart = 0.0F;
/* 162 */         close(true, this.onClose);
/*     */       } else {
/* 164 */         fade = Mth.clamp(fade, 0.0F, 1.0F);
/* 165 */         widgetAlpha = Mth.clampedMap(fade, 0.5F, 1.0F, 0.0F, 1.0F);
/*     */       } 
/* 167 */       fadeWidgets(widgetAlpha);
/*     */     } 
/* 170 */     this.logoRenderer.extractRenderState(graphics, this.width, 1.0F);
/*     */   }
/*     */   
/*     */   private void handleInitialNarrationDelay() {
/* 174 */     if (!this.hasNarrated && this.narratorAvailable)
/* 175 */       if (this.timer < 40.0F) {
/* 176 */         this.timer++;
/* 177 */       } else if (this.minecraft.isWindowActive()) {
/* 179 */         Narrator.getNarrator().say(ONBOARDING_NARRATOR_MESSAGE.getString(), true, this.minecraft.options.getFinalSoundSourceVolume(SoundSource.VOICE));
/* 180 */         this.hasNarrated = true;
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\AccessibilityOnboardingScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */