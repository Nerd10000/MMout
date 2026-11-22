/*     */ package net.minecraft.client.gui.screens.dialog;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.ScrollableLayout;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.Layout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.dialog.body.DialogBodyHandlers;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ import net.minecraft.server.dialog.DialogAction;
/*     */ import net.minecraft.server.dialog.Input;
/*     */ import net.minecraft.server.dialog.body.DialogBody;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public abstract class DialogScreen<T extends Dialog> extends Screen {
/*  34 */   public static final Component DISCONNECT = (Component)Component.translatable("menu.custom_screen_info.disconnect");
/*     */   
/*     */   private static final int WARNING_BUTTON_SIZE = 20;
/*     */   
/*  36 */   private static final WidgetSprites WARNING_BUTTON_SPRITES = new WidgetSprites(
/*  37 */       Identifier.withDefaultNamespace("dialog/warning_button"), 
/*  38 */       Identifier.withDefaultNamespace("dialog/warning_button_disabled"), 
/*  39 */       Identifier.withDefaultNamespace("dialog/warning_button_highlighted"));
/*     */   
/*     */   private final T dialog;
/*     */   
/*  43 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   
/*     */   private final Screen previousScreen;
/*     */   
/*     */   private ScrollableLayout bodyScroll;
/*     */   
/*     */   private Button warningButton;
/*     */   
/*     */   private final DialogConnectionAccess connectionAccess;
/*     */   
/*  48 */   private Supplier<Optional<ClickEvent>> onClose = DialogControlSet.EMPTY_ACTION;
/*     */   
/*     */   public DialogScreen(Screen previousScreen, T dialog, DialogConnectionAccess connectionAccess) {
/*  51 */     super(dialog.common().title());
/*  52 */     this.dialog = dialog;
/*  53 */     this.previousScreen = previousScreen;
/*  54 */     this.connectionAccess = connectionAccess;
/*     */   }
/*     */   
/*     */   protected final void init() {
/*  59 */     super.init();
/*  61 */     this.warningButton = createWarningButton();
/*  63 */     this.warningButton.setTabOrderGroup(-10);
/*  65 */     DialogControlSet controlSet = new DialogControlSet(this);
/*  67 */     LinearLayout body = LinearLayout.vertical().spacing(10);
/*  68 */     body.defaultCellSetting().alignHorizontallyCenter();
/*  70 */     this.layout.addToHeader(createTitleWithWarningButton());
/*  72 */     for (DialogBody dialogBody : this.dialog.common().body()) {
/*  73 */       LayoutElement bodyElement = DialogBodyHandlers.createBodyElement(this, dialogBody);
/*  74 */       if (bodyElement != null)
/*  75 */         body.addChild(bodyElement); 
/*     */     } 
/*  79 */     for (Input input : this.dialog.common().inputs()) {
/*  80 */       Objects.requireNonNull(body);
/*  80 */       controlSet.addInput(input, body::addChild);
/*     */     } 
/*  83 */     populateBodyElements(body, controlSet, this.dialog, this.connectionAccess);
/*  85 */     this.bodyScroll = new ScrollableLayout(this.minecraft, (Layout)body, this.layout.getContentHeight());
/*  86 */     this.layout.addToContents((LayoutElement)this.bodyScroll);
/*  88 */     updateHeaderAndFooter(this.layout, controlSet, this.dialog, this.connectionAccess);
/*  90 */     this.onClose = controlSet.bindAction(this.dialog.onCancel());
/*  93 */     this.layout.visitWidgets(widget -> {
/*     */           if (widget != this.warningButton)
/*     */             addRenderableWidget((GuiEventListener)widget); 
/*     */         });
/*  98 */     addRenderableWidget((GuiEventListener)this.warningButton);
/* 100 */     repositionElements();
/*     */   }
/*     */   
/*     */   protected void populateBodyElements(LinearLayout layout, DialogControlSet controlSet, T dialog, DialogConnectionAccess connectionAccess) {}
/*     */   
/*     */   protected void updateHeaderAndFooter(HeaderAndFooterLayout layout, DialogControlSet controlSet, T dialog, DialogConnectionAccess connectionAccess) {}
/*     */   
/*     */   protected void repositionElements() {
/* 111 */     this.bodyScroll.arrangeElements();
/* 112 */     this.bodyScroll.setMaxHeight(this.layout.getContentHeight());
/* 113 */     this.layout.arrangeElements();
/* 114 */     makeSureWarningButtonIsInBounds();
/*     */   }
/*     */   
/*     */   protected LayoutElement createTitleWithWarningButton() {
/* 118 */     LinearLayout layout = LinearLayout.horizontal().spacing(10);
/* 119 */     layout.defaultCellSetting().alignHorizontallyCenter().alignVerticallyMiddle();
/* 120 */     layout.addChild((LayoutElement)new StringWidget(this.title, this.font));
/* 121 */     layout.addChild((LayoutElement)this.warningButton);
/* 122 */     return (LayoutElement)layout;
/*     */   }
/*     */   
/*     */   protected void makeSureWarningButtonIsInBounds() {
/* 126 */     int x = this.warningButton.getX();
/* 127 */     int y = this.warningButton.getY();
/* 129 */     if (x < 0 || y < 0 || x > this.width - 20 || y > this.height - 20) {
/* 131 */       this.warningButton.setX(Math.max(0, this.width - 40));
/* 132 */       this.warningButton.setY(Math.min(5, this.height));
/*     */     } 
/*     */   }
/*     */   
/*     */   private Button createWarningButton() {
/* 139 */     ImageButton result = new ImageButton(0, 0, 20, 20, WARNING_BUTTON_SPRITES, paramButton -> this.minecraft.gui.setScreen(WarningScreen.create(this.minecraft, this.connectionAccess, this)), (Component)Component.translatable("menu.custom_screen_info.button_narration"));
/* 141 */     result.setTooltip(Tooltip.create((Component)Component.translatable("menu.custom_screen_info.tooltip")));
/* 142 */     return (Button)result;
/*     */   }
/*     */   
/*     */   public boolean isPauseScreen() {
/* 147 */     return this.dialog.common().pause();
/*     */   }
/*     */   
/*     */   public boolean shouldCloseOnEsc() {
/* 152 */     return this.dialog.common().canCloseWithEscape();
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 157 */     runAction(this.onClose.get(), DialogAction.CLOSE);
/*     */   }
/*     */   
/*     */   public void runAction(Optional<ClickEvent> closeAction) {
/* 161 */     runAction(closeAction, this.dialog.common().afterAction());
/*     */   }
/*     */   
/*     */   public void runAction(Optional<ClickEvent> closeAction, DialogAction afterAction) {
/* 165 */     switch (afterAction) {
/*     */       default:
/* 165 */         throw new MatchException(null, null);
/*     */       case NONE:
/*     */       
/*     */       case CLOSE:
/*     */       
/*     */       case WAIT_FOR_RESPONSE:
/*     */         break;
/*     */     } 
/* 165 */     Screen screenToActivate = 
/*     */       
/* 168 */       new WaitingForResponseScreen(this.previousScreen);
/* 171 */     if (closeAction.isPresent()) {
/* 172 */       handleDialogClickEvent(closeAction.get(), screenToActivate);
/*     */     } else {
/* 174 */       this.minecraft.gui.setScreen(screenToActivate);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleDialogClickEvent(ClickEvent event, Screen activeScreen) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore_3
/*     */     //   7: iconst_0
/*     */     //   8: istore #4
/*     */     //   10: aload_3
/*     */     //   11: iload #4
/*     */     //   13: <illegal opcode> typeSwitch : (Lnet/minecraft/network/chat/ClickEvent;I)I
/*     */     //   18: tableswitch default -> 142, 0 -> 44, 1 -> 79, 2 -> 103
/*     */     //   44: aload_3
/*     */     //   45: checkcast net/minecraft/network/chat/ClickEvent$RunCommand
/*     */     //   48: astore #5
/*     */     //   50: aload #5
/*     */     //   52: invokevirtual command : ()Ljava/lang/String;
/*     */     //   55: astore #7
/*     */     //   57: aload #7
/*     */     //   59: astore #6
/*     */     //   61: aload_0
/*     */     //   62: getfield connectionAccess : Lnet/minecraft/client/gui/screens/dialog/DialogConnectionAccess;
/*     */     //   65: aload #6
/*     */     //   67: invokestatic trimOptionalPrefix : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   70: aload_2
/*     */     //   71: invokeinterface runCommand : (Ljava/lang/String;Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   76: goto -> 151
/*     */     //   79: aload_3
/*     */     //   80: checkcast net/minecraft/network/chat/ClickEvent$ShowDialog
/*     */     //   83: astore #7
/*     */     //   85: aload_0
/*     */     //   86: getfield connectionAccess : Lnet/minecraft/client/gui/screens/dialog/DialogConnectionAccess;
/*     */     //   89: aload #7
/*     */     //   91: invokevirtual dialog : ()Lnet/minecraft/core/Holder;
/*     */     //   94: aload_2
/*     */     //   95: invokeinterface openDialog : (Lnet/minecraft/core/Holder;Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   100: goto -> 151
/*     */     //   103: aload_3
/*     */     //   104: checkcast net/minecraft/network/chat/ClickEvent$Custom
/*     */     //   107: astore #8
/*     */     //   109: aload_0
/*     */     //   110: getfield connectionAccess : Lnet/minecraft/client/gui/screens/dialog/DialogConnectionAccess;
/*     */     //   113: aload #8
/*     */     //   115: invokevirtual id : ()Lnet/minecraft/resources/Identifier;
/*     */     //   118: aload #8
/*     */     //   120: invokevirtual payload : ()Ljava/util/Optional;
/*     */     //   123: invokeinterface sendCustomAction : (Lnet/minecraft/resources/Identifier;Ljava/util/Optional;)V
/*     */     //   128: aload_0
/*     */     //   129: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   132: getfield gui : Lnet/minecraft/client/gui/Gui;
/*     */     //   135: aload_2
/*     */     //   136: invokevirtual setScreen : (Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   139: goto -> 151
/*     */     //   142: aload_1
/*     */     //   143: aload_0
/*     */     //   144: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   147: aload_2
/*     */     //   148: invokestatic defaultHandleClickEvent : (Lnet/minecraft/network/chat/ClickEvent;Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   151: goto -> 168
/*     */     //   154: astore_3
/*     */     //   155: new java/lang/MatchException
/*     */     //   158: dup
/*     */     //   159: aload_3
/*     */     //   160: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   163: aload_3
/*     */     //   164: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   167: athrow
/*     */     //   168: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #180	-> 0
/*     */     //   #181	-> 44
/*     */     //   #182	-> 61
/*     */     //   #183	-> 79
/*     */     //   #184	-> 103
/*     */     //   #185	-> 109
/*     */     //   #186	-> 128
/*     */     //   #187	-> 139
/*     */     //   #188	-> 142
/*     */     //   #184	-> 154
/*     */     //   #190	-> 168
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   61	18	6	command	Ljava/lang/String;
/*     */     //   85	18	7	dialog	Lnet/minecraft/network/chat/ClickEvent$ShowDialog;
/*     */     //   109	33	8	custom	Lnet/minecraft/network/chat/ClickEvent$Custom;
/*     */     //   0	169	0	this	Lnet/minecraft/client/gui/screens/dialog/DialogScreen;
/*     */     //   0	169	1	event	Lnet/minecraft/network/chat/ClickEvent;
/*     */     //   0	169	2	activeScreen	Lnet/minecraft/client/gui/screens/Screen;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	169	0	this	Lnet/minecraft/client/gui/screens/dialog/DialogScreen<TT;>;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   52	55	154	java/lang/Throwable
/*     */   }
/*     */   
/*     */   public Screen previousScreen() {
/* 193 */     return this.previousScreen;
/*     */   }
/*     */   
/*     */   protected static LayoutElement packControlsIntoColumns(List<? extends LayoutElement> controls, int columns) {
/* 197 */     GridLayout gridLayout = new GridLayout();
/* 198 */     gridLayout.defaultCellSetting().alignHorizontallyCenter();
/* 199 */     gridLayout.columnSpacing(2).rowSpacing(2);
/* 200 */     int count = controls.size();
/* 201 */     int lastFullRow = count / columns;
/* 202 */     int countInFullRows = lastFullRow * columns;
/* 204 */     for (int i = 0; i < countInFullRows; i++)
/* 205 */       gridLayout.addChild(controls.get(i), i / columns, i % columns); 
/* 208 */     if (count != countInFullRows) {
/* 209 */       LinearLayout lastRow = LinearLayout.horizontal().spacing(2);
/* 210 */       lastRow.defaultCellSetting().alignHorizontallyCenter();
/* 211 */       for (int j = countInFullRows; j < count; j++)
/* 212 */         lastRow.addChild(controls.get(j)); 
/* 214 */       gridLayout.addChild((LayoutElement)lastRow, lastFullRow, 0, 1, columns);
/*     */     } 
/* 217 */     return (LayoutElement)gridLayout;
/*     */   }
/*     */   
/*     */   public static class WarningScreen extends ConfirmScreen {
/*     */     private final MutableObject<Screen> returnScreen;
/*     */     
/*     */     public static Screen create(Minecraft minecraft, DialogConnectionAccess connectionAccess, Screen returnScreen) {
/* 224 */       return (Screen)new WarningScreen(minecraft, connectionAccess, new MutableObject(returnScreen));
/*     */     }
/*     */     
/*     */     private WarningScreen(Minecraft minecraft, DialogConnectionAccess connectionAccess, MutableObject<Screen> returnScreen) {
/* 228 */       super(disconnect -> {
/*     */             if (disconnect) {
/*     */               connectionAccess.disconnect(DialogScreen.DISCONNECT);
/*     */             } else {
/*     */               minecraft.gui.setScreen((Screen)returnScreen.get());
/*     */             } 
/* 236 */           }(Component)Component.translatable("menu.custom_screen_info.title"), 
/* 237 */           (Component)Component.translatable("menu.custom_screen_info.contents"), 
/* 238 */           CommonComponents.disconnectButtonLabel(minecraft.isLocalServer()), CommonComponents.GUI_BACK);
/* 241 */       this.returnScreen = returnScreen;
/*     */     }
/*     */     
/*     */     public Screen returnScreen() {
/* 245 */       return (Screen)this.returnScreen.get();
/*     */     }
/*     */     
/*     */     public void updateReturnScreen(Screen newReturnScreen) {
/* 249 */       this.returnScreen.setValue(newReturnScreen);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\DialogScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */