/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class ConfirmScreen extends Screen {
/*     */   protected final Component message;
/*     */   
/*  18 */   protected final LinearLayout layout = LinearLayout.vertical().spacing(8);
/*     */   
/*     */   protected Component yesButtonComponent;
/*     */   
/*     */   protected Component noButtonComponent;
/*     */   
/*     */   protected Button yesButton;
/*     */   
/*     */   protected Button noButton;
/*     */   
/*     */   private int delayTicker;
/*     */   
/*     */   protected final BooleanConsumer callback;
/*     */   
/*     */   public ConfirmScreen(BooleanConsumer callback, Component title, Component message) {
/*  27 */     this(callback, title, message, CommonComponents.GUI_YES, CommonComponents.GUI_NO);
/*     */   }
/*     */   
/*     */   public ConfirmScreen(BooleanConsumer callback, Component title, Component message, Component yesButtonComponent, Component noButtonComponent) {
/*  31 */     super(title);
/*  32 */     this.callback = callback;
/*  33 */     this.message = message;
/*  34 */     this.yesButtonComponent = yesButtonComponent;
/*  35 */     this.noButtonComponent = noButtonComponent;
/*     */   }
/*     */   
/*     */   public Component getNarrationMessage() {
/*  40 */     return (Component)CommonComponents.joinForNarration(new Component[] { super.getNarrationMessage(), this.message });
/*     */   }
/*     */   
/*     */   protected void init() {
/*  45 */     super.init();
/*  46 */     this.layout.defaultCellSetting().alignHorizontallyCenter();
/*  48 */     this.layout.addChild((LayoutElement)new StringWidget(this.title, this.font));
/*  49 */     this.layout.addChild(addMessage());
/*  51 */     addAdditionalText();
/*  52 */     LinearLayout buttonLayout = (LinearLayout)this.layout.addChild((LayoutElement)LinearLayout.horizontal().spacing(4));
/*  53 */     buttonLayout.defaultCellSetting().paddingTop(16);
/*  54 */     addButtons(buttonLayout);
/*  56 */     this.layout.visitWidgets(this::addRenderableWidget);
/*  57 */     repositionElements();
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/*  62 */     this.layout.arrangeElements();
/*  63 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*     */   }
/*     */   
/*     */   protected void addAdditionalText() {}
/*     */   
/*     */   protected LayoutElement addMessage() {
/*  72 */     return (LayoutElement)(new MultiLineTextWidget(this.message, this.font)).setMaxWidth(this.width - 50).setMaxRows(15).setCentered(true);
/*     */   }
/*     */   
/*     */   protected void addButtons(LinearLayout buttonLayout) {
/*  76 */     this.yesButton = (Button)buttonLayout.addChild((LayoutElement)Button.builder(this.yesButtonComponent, button -> this.callback.accept(true)).build());
/*  77 */     this.noButton = (Button)buttonLayout.addChild((LayoutElement)Button.builder(this.noButtonComponent, button -> this.callback.accept(false)).build());
/*     */   }
/*     */   
/*     */   public void setDelay(int delay) {
/*  81 */     this.delayTicker = delay;
/*  83 */     this.yesButton.active = false;
/*  84 */     this.noButton.active = false;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  89 */     super.tick();
/*  91 */     if (--this.delayTicker == 0) {
/*  92 */       this.yesButton.active = true;
/*  93 */       this.noButton.active = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean shouldCloseOnEsc() {
/*  99 */     return false;
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 104 */     if (this.delayTicker <= 0 && event.isEscape()) {
/* 105 */       this.callback.accept(false);
/* 106 */       return true;
/*     */     } 
/* 108 */     return super.keyPressed(event);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\ConfirmScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */