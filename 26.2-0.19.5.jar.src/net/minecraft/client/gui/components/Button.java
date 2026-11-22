/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.input.InputWithModifiers;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ 
/*     */ public abstract class Button extends AbstractButton {
/*     */   public static final int SMALL_WIDTH = 120;
/*     */   
/*     */   public static final int DEFAULT_WIDTH = 150;
/*     */   
/*     */   public static final int BIG_WIDTH = 200;
/*     */   
/*     */   public static final int DEFAULT_HEIGHT = 20;
/*     */   
/*     */   public static final int DEFAULT_SPACING = 8;
/*     */   
/*     */   protected static final CreateNarration DEFAULT_NARRATION;
/*     */   
/*     */   protected final OnPress onPress;
/*     */   
/*     */   protected final CreateNarration createNarration;
/*     */   
/*     */   static {
/*  18 */     DEFAULT_NARRATION = (defaultNarrationSupplier -> (MutableComponent)defaultNarrationSupplier.get());
/*     */   }
/*     */   
/*     */   public static class Plain extends Button {
/*     */     protected Plain(int x, int y, int width, int height, Component message, Button.OnPress onPress, Button.CreateNarration createNarration) {
/*  30 */       super(x, y, width, height, message, onPress, createNarration);
/*     */     }
/*     */     
/*     */     protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  35 */       extractDefaultSprite(graphics);
/*  36 */       extractDefaultLabel(graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE));
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface CreateNarration {
/*     */     MutableComponent createNarrationMessage(Supplier<MutableComponent> param1Supplier);
/*     */   }
/*     */   
/*     */   public static interface OnPress {
/*     */     void onPress(Button param1Button);
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final Component message;
/*     */     
/*     */     private final Button.OnPress onPress;
/*     */     
/*     */     private Tooltip tooltip;
/*     */     
/*     */     private int x;
/*     */     
/*     */     private int y;
/*     */     
/*  46 */     private int width = 150;
/*     */     
/*  47 */     private int height = 20;
/*     */     
/*  48 */     private Button.CreateNarration createNarration = Button.DEFAULT_NARRATION;
/*     */     
/*     */     public Builder(Component message, Button.OnPress onPress) {
/*  51 */       this.message = message;
/*  52 */       this.onPress = onPress;
/*     */     }
/*     */     
/*     */     public Builder pos(int x, int y) {
/*  56 */       this.x = x;
/*  57 */       this.y = y;
/*  58 */       return this;
/*     */     }
/*     */     
/*     */     public Builder width(int width) {
/*  62 */       this.width = width;
/*  63 */       return this;
/*     */     }
/*     */     
/*     */     public Builder size(int width, int height) {
/*  67 */       this.width = width;
/*  68 */       this.height = height;
/*  69 */       return this;
/*     */     }
/*     */     
/*     */     public Builder bounds(int x, int y, int width, int height) {
/*  73 */       return pos(x, y).size(width, height);
/*     */     }
/*     */     
/*     */     public Builder tooltip(Tooltip tooltip) {
/*  77 */       this.tooltip = tooltip;
/*  78 */       return this;
/*     */     }
/*     */     
/*     */     public Builder createNarration(Button.CreateNarration createNarration) {
/*  82 */       this.createNarration = createNarration;
/*  83 */       return this;
/*     */     }
/*     */     
/*     */     public Button build() {
/*  87 */       Button button = new Button.Plain(this.x, this.y, this.width, this.height, this.message, this.onPress, this.createNarration);
/*  88 */       button.setTooltip(this.tooltip);
/*  89 */       return button;
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder builder(Component message, OnPress onPress) {
/*  94 */     return new Builder(message, onPress);
/*     */   }
/*     */   
/*     */   protected Button(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration createNarration) {
/* 101 */     super(x, y, width, height, message);
/* 103 */     this.onPress = onPress;
/* 104 */     this.createNarration = createNarration;
/*     */   }
/*     */   
/*     */   public void onPress(InputWithModifiers input) {
/* 109 */     this.onPress.onPress(this);
/*     */   }
/*     */   
/*     */   protected MutableComponent createNarrationMessage() {
/* 114 */     return this.createNarration.createNarrationMessage(() -> super.createNarrationMessage());
/*     */   }
/*     */   
/*     */   public void updateWidgetNarration(NarrationElementOutput output) {
/* 119 */     defaultButtonNarrationText(output);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\Button.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */