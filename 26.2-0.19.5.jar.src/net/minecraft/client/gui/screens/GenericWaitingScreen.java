/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.TextAlignment;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.MultiLineLabel;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class GenericWaitingScreen extends Screen {
/*     */   private static final int TITLE_Y = 80;
/*     */   
/*     */   private static final int MESSAGE_Y = 120;
/*     */   
/*     */   private static final int MESSAGE_MAX_WIDTH = 360;
/*     */   
/*     */   private final boolean showLoadingDots;
/*     */   
/*     */   private final Component messageText;
/*     */   
/*     */   private final Component buttonLabel;
/*     */   
/*     */   private final Runnable buttonCallback;
/*     */   
/*     */   private final boolean showButton;
/*     */   
/*     */   private final boolean closeOnEscape;
/*     */   
/*     */   private final MultiLineLabel message;
/*     */   
/*     */   private Button button;
/*     */   
/*     */   private int disableButtonTicks;
/*     */   
/*     */   public static GenericWaitingScreen createWaitingWithoutButton(Component title, Component messageText) {
/*  32 */     return new GenericWaitingScreen(title, true, messageText, (Component)Component.empty(), () -> {
/*     */         
/*  32 */         }0, false, false);
/*     */   }
/*     */   
/*     */   public static GenericWaitingScreen createWaiting(Component title, Component buttonLabel, Runnable buttonCallback) {
/*  36 */     return new GenericWaitingScreen(title, true, (Component)Component.empty(), buttonLabel, buttonCallback, 0, true, false);
/*     */   }
/*     */   
/*     */   public static GenericWaitingScreen createCompleted(Component title, Component messageText, Component buttonLabel, Runnable buttonCallback) {
/*  40 */     return new GenericWaitingScreen(title, false, messageText, buttonLabel, buttonCallback, 20, true, true);
/*     */   }
/*     */   
/*     */   protected GenericWaitingScreen(Component title, boolean showLoadingDots, Component messageText, Component buttonLabel, Runnable buttonCallback, int disableButtonTicks, boolean showButton, boolean closeOnEscape) {
/*  44 */     super(title);
/*  45 */     this.showLoadingDots = showLoadingDots;
/*  46 */     this.messageText = messageText;
/*  47 */     this.buttonLabel = buttonLabel;
/*  48 */     this.buttonCallback = buttonCallback;
/*  49 */     this.disableButtonTicks = disableButtonTicks;
/*  50 */     this.showButton = showButton;
/*  51 */     this.closeOnEscape = closeOnEscape;
/*  52 */     this.message = MultiLineLabel.create(this.font, messageText, 360);
/*     */   }
/*     */   
/*     */   protected void init() {
/*  57 */     super.init();
/*  59 */     int buttonWidth = 150;
/*  60 */     int buttonHeight = 20;
/*  62 */     int lineCount = this.message.getLineCount() + 1;
/*  63 */     Objects.requireNonNull(this.font);
/*  63 */     int messageButtonSpacing = Math.max(lineCount, 5) * 9;
/*  64 */     int buttonY = Math.min(120 + messageButtonSpacing, this.height - 40);
/*  66 */     if (this.showButton)
/*  67 */       this.button = addRenderableWidget(Button.builder(this.buttonLabel, b -> onClose()).bounds((this.width - 150) / 2, buttonY, 150, 20).build()); 
/*     */   }
/*     */   
/*     */   public void tick() {
/*  73 */     if (this.disableButtonTicks > 0)
/*  74 */       this.disableButtonTicks--; 
/*  76 */     if (this.button != null)
/*  77 */       this.button.active = (this.disableButtonTicks == 0); 
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  83 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/*  84 */     ActiveTextCollector textRenderer = graphics.textRenderer();
/*  85 */     graphics.centeredText(this.font, this.title, this.width / 2, 80, -1);
/*  86 */     int messageY = 120;
/*  87 */     if (this.showLoadingDots) {
/*  88 */       String loadingDots = LoadingDotsText.get(Util.getMillis());
/*  89 */       graphics.centeredText(this.font, loadingDots, this.width / 2, messageY, -6250336);
/*  90 */       Objects.requireNonNull(this.font);
/*  90 */       messageY += 9 + 3;
/*     */     } 
/*  92 */     Objects.requireNonNull(this.font);
/*  92 */     this.message.visitLines(TextAlignment.CENTER, this.width / 2, messageY, 9, textRenderer);
/*     */   }
/*     */   
/*     */   public boolean shouldCloseOnEsc() {
/*  97 */     return (this.closeOnEscape && this.button != null && this.button.active);
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 102 */     this.buttonCallback.run();
/*     */   }
/*     */   
/*     */   public Component getNarrationMessage() {
/* 107 */     return (Component)CommonComponents.joinForNarration(new Component[] { this.title, this.messageText });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\GenericWaitingScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */