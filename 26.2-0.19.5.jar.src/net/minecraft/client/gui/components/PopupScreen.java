/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class PopupScreen extends Screen {
/*  19 */   private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("popup/background");
/*     */   
/*     */   private static final int SPACING = 12;
/*     */   
/*     */   private static final int BG_BORDER_WITH_SPACING = 18;
/*     */   
/*     */   private static final int BUTTON_SPACING = 6;
/*     */   
/*     */   private static final int IMAGE_SIZE_X = 130;
/*     */   
/*     */   private static final int IMAGE_SIZE_Y = 64;
/*     */   
/*     */   private static final int POPUP_DEFAULT_WIDTH = 250;
/*     */   
/*     */   private final Screen backgroundScreen;
/*     */   
/*     */   private final Identifier image;
/*     */   
/*     */   private final List<Component> messages;
/*     */   
/*     */   private final List<ButtonOption> buttons;
/*     */   
/*     */   private final Runnable onClose;
/*     */   
/*     */   private final int contentWidth;
/*     */   
/*  34 */   private final LinearLayout layout = LinearLayout.vertical();
/*     */   
/*     */   private PopupScreen(Screen backgroundScreen, int backgroundWidth, Identifier image, Component title, List<Component> messages, List<ButtonOption> buttons, Runnable onClose) {
/*  37 */     super(title);
/*  38 */     this.backgroundScreen = backgroundScreen;
/*  39 */     this.image = image;
/*  40 */     this.messages = messages;
/*  41 */     this.buttons = buttons;
/*  42 */     this.onClose = onClose;
/*  43 */     this.contentWidth = backgroundWidth - 36;
/*     */   }
/*     */   
/*     */   public void added() {
/*  48 */     super.added();
/*  49 */     if (this.backgroundScreen != null)
/*  50 */       this.backgroundScreen.clearFocus(); 
/*     */   }
/*     */   
/*     */   protected void init() {
/*  56 */     if (this.backgroundScreen != null)
/*  57 */       this.backgroundScreen.init(this.width, this.height); 
/*  59 */     this.layout.spacing(12).defaultCellSetting().alignHorizontallyCenter();
/*  60 */     this.layout.addChild((new MultiLineTextWidget((Component)this.title.copy().withStyle(ChatFormatting.BOLD), this.font)).setMaxWidth(this.contentWidth).setCentered(true));
/*  61 */     if (this.image != null)
/*  62 */       this.layout.addChild(ImageWidget.texture(130, 64, this.image, 130, 64)); 
/*  64 */     this.messages.forEach(message -> this.layout.addChild((new MultiLineTextWidget(message, this.font)).setMaxWidth(this.contentWidth).setCentered(true)));
/*  65 */     this.layout.addChild((LayoutElement)buildButtonRow());
/*  67 */     PopupScreen popupScreen = this;
/*  67 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget(x$0));
/*  68 */     repositionElements();
/*     */   }
/*     */   
/*     */   private LinearLayout buildButtonRow() {
/*  72 */     int totalSpacing = 6 * (this.buttons.size() - 1);
/*  73 */     int buttonWidth = Math.min((this.contentWidth - totalSpacing) / this.buttons.size(), 150);
/*  74 */     LinearLayout row = LinearLayout.horizontal();
/*  75 */     row.spacing(6);
/*  76 */     for (ButtonOption button : this.buttons)
/*  77 */       row.addChild(Button.builder(button.message(), b -> button.action().accept(this)).width(buttonWidth).build()); 
/*  79 */     return row;
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/*  84 */     if (this.backgroundScreen != null)
/*  85 */       this.backgroundScreen.resize(this.width, this.height); 
/*  87 */     this.layout.arrangeElements();
/*  88 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*     */   }
/*     */   
/*     */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  93 */     if (this.backgroundScreen != null) {
/*  94 */       this.backgroundScreen.extractBackground(graphics, mouseX, mouseY, a);
/*  95 */       graphics.nextStratum();
/*  96 */       this.backgroundScreen.extractRenderState(graphics, -1, -1, a);
/*  97 */       graphics.nextStratum();
/*  98 */       extractTransparentBackground(graphics);
/*     */     } else {
/* 100 */       super.extractBackground(graphics, mouseX, mouseY, a);
/*     */     } 
/* 103 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, this.layout.getX() - 18, this.layout.getY() - 18, this.layout.getWidth() + 36, this.layout.getHeight() + 36);
/*     */   }
/*     */   
/*     */   public Component getNarrationMessage() {
/* 108 */     return (Component)CommonComponents.joinForNarration(new Component[] { this.title, CommonComponents.joinLines(this.messages) });
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 113 */     if (this.onClose != null)
/* 114 */       this.onClose.run(); 
/* 116 */     this.minecraft.gui.setScreen(this.backgroundScreen);
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final Screen backgroundScreen;
/*     */     
/*     */     private final Component title;
/*     */     
/* 122 */     private final List<Component> messages = new ArrayList<>();
/*     */     
/* 123 */     private int width = 250;
/*     */     
/*     */     private Identifier image;
/*     */     
/* 125 */     private final List<PopupScreen.ButtonOption> buttons = new ArrayList<>();
/*     */     
/* 126 */     private Runnable onClose = null;
/*     */     
/*     */     public Builder(Screen backgroundScreen, Component title) {
/* 129 */       this.backgroundScreen = backgroundScreen;
/* 130 */       this.title = title;
/*     */     }
/*     */     
/*     */     public Builder setWidth(int width) {
/* 134 */       this.width = width;
/* 135 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setImage(Identifier image) {
/* 139 */       this.image = image;
/* 140 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addMessage(Component message) {
/* 144 */       this.messages.add(message);
/* 145 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addButton(Component message, Consumer<PopupScreen> action) {
/* 149 */       this.buttons.add(new PopupScreen.ButtonOption(message, action));
/* 150 */       return this;
/*     */     }
/*     */     
/*     */     public Builder onClose(Runnable onClose) {
/* 154 */       this.onClose = onClose;
/* 155 */       return this;
/*     */     }
/*     */     
/*     */     public PopupScreen build() {
/* 159 */       if (this.buttons.isEmpty())
/* 160 */         throw new IllegalStateException("Popup must have at least one button"); 
/* 162 */       return new PopupScreen(this.backgroundScreen, this.width, this.image, this.title, this.messages, List.copyOf(this.buttons), this.onClose);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ButtonOption extends Record {
/*     */     private final Component message;
/*     */     
/*     */     private final Consumer<PopupScreen> action;
/*     */     
/*     */     private ButtonOption(Component message, Consumer<PopupScreen> action) {
/* 166 */       this.message = message;
/* 166 */       this.action = action;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Component message() {
/* 166 */       return this.message;
/*     */     }
/*     */     
/*     */     public Consumer<PopupScreen> action() {
/* 166 */       return this.action;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\PopupScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */