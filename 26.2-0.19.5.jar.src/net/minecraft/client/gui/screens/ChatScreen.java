/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.ChatComponent;
/*     */ import net.minecraft.client.gui.components.CommandSuggestions;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.chat.ChatAbilities;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class ChatScreen extends Screen {
/*     */   public static final double MOUSE_SCROLL_SPEED = 7.0D;
/*     */   
/*  32 */   private static final Component USAGE_TEXT = (Component)Component.translatable("chat_screen.usage");
/*     */   
/*  33 */   private static final Component RESTRICTED_NARRATION_TEXT = (Component)Component.translatable("chat_screen.restricted.narration");
/*     */   
/*     */   public static final int USAGE_BACKGROUND_COLOR = -805306368;
/*     */   
/*     */   private final boolean closeOnSubmit;
/*     */   
/*  38 */   private String historyBuffer = "";
/*     */   
/*  39 */   private int historyPos = -1;
/*     */   
/*     */   protected EditBox input;
/*     */   
/*     */   protected String initial;
/*     */   
/*     */   protected boolean isDraft;
/*     */   
/*  44 */   private ChatComponent.DisplayMode displayMode = ChatComponent.DisplayMode.FOREGROUND;
/*     */   
/*  46 */   protected ExitReason exitReason = ExitReason.INTERRUPTED;
/*     */   
/*     */   private CommandSuggestions commandSuggestions;
/*     */   
/*     */   public ChatScreen(String initial, boolean isDraft) {
/*  51 */     this(initial, isDraft, true);
/*     */   }
/*     */   
/*     */   public ChatScreen(String initial, boolean isDraft, boolean closeOnSubmit) {
/*  55 */     super((Component)Component.translatable("chat_screen.title"));
/*  56 */     this.closeOnSubmit = closeOnSubmit;
/*  57 */     this.initial = initial;
/*  58 */     this.isDraft = isDraft;
/*     */   }
/*     */   
/*     */   protected void init() {
/*  63 */     this.historyPos = this.minecraft.gui.hud.getChat().getRecentChat().size();
/*  64 */     this.input = new EditBox(this.minecraft.fontFilterFishy, 4, this.height - 12, this.width - 4, 12, (Component)Component.translatable("chat.editBox")) {
/*     */         protected MutableComponent createNarrationMessage() {
/*  67 */           return super.createNarrationMessage().append(ChatScreen.this.commandSuggestions.getNarrationMessage());
/*     */         }
/*     */       };
/*  70 */     this.input.setMaxLength(256);
/*  71 */     this.input.setBordered(false);
/*  72 */     this.input.setValue(this.initial);
/*  73 */     this.input.setResponder(this::onEdited);
/*  74 */     this.input.addFormatter(this::formatChat);
/*  75 */     this.input.setCanLoseFocus(false);
/*  76 */     addRenderableWidget(this.input);
/*  78 */     this.commandSuggestions = new CommandSuggestions(this.minecraft, this, this.input, this.font, false, false, 1, 10, true, -805306368);
/*  79 */     this.commandSuggestions.setAllowHiding(false);
/*  80 */     this.commandSuggestions.setAllowSuggestions(false);
/*  81 */     ChatAbilities chatAbilities = this.minecraft.player.chatAbilities();
/*  82 */     this.displayMode = chatAbilities.hasAnyRestrictions() ? ChatComponent.DisplayMode.FOREGROUND_RESTRICTED : ChatComponent.DisplayMode.FOREGROUND;
/*  83 */     this.commandSuggestions.setRestrictions(chatAbilities.canSendMessages(), chatAbilities.canSendCommands());
/*  84 */     this.commandSuggestions.updateCommandInfo();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ChatConstructor<T extends ChatScreen> {
/*     */     T create(String param1String, boolean param1Boolean);
/*     */   }
/*     */   
/*     */   protected void setInitialFocus() {
/*  89 */     setInitialFocus((GuiEventListener)this.input);
/*     */   }
/*     */   
/*     */   public void resize(int width, int height) {
/*  94 */     this.initial = this.input.getValue();
/*  95 */     init(width, height);
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 100 */     this.exitReason = ExitReason.INTENTIONAL;
/* 101 */     super.onClose();
/*     */   }
/*     */   
/*     */   public void removed() {
/* 106 */     this.minecraft.gui.hud.getChat().resetChatScroll();
/* 107 */     this.initial = this.input.getValue();
/* 109 */     if (shouldDiscardDraft() || StringUtils.isBlank(this.initial)) {
/* 110 */       this.minecraft.gui.hud.getChat().discardDraft();
/* 111 */     } else if (!this.isDraft) {
/* 112 */       this.minecraft.gui.hud.getChat().saveAsDraft(this.initial);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean shouldDiscardDraft() {
/* 117 */     return (this.exitReason != ExitReason.INTERRUPTED && (this.exitReason != ExitReason.INTENTIONAL || !((Boolean)this.minecraft.options.saveChatDrafts().get()).booleanValue()));
/*     */   }
/*     */   
/*     */   private void onEdited(String value) {
/* 121 */     this.commandSuggestions.setAllowSuggestions(true);
/* 122 */     this.commandSuggestions.updateCommandInfo();
/* 123 */     this.isDraft = false;
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 129 */     if (this.commandSuggestions.keyPressed(event))
/* 130 */       return true; 
/* 133 */     if (this.isDraft && event.key() == 259) {
/* 134 */       this.input.setValue("");
/* 135 */       this.isDraft = false;
/* 136 */       return true;
/*     */     } 
/* 139 */     if (super.keyPressed(event))
/* 140 */       return true; 
/* 143 */     if (event.isConfirmation()) {
/* 144 */       if (!this.commandSuggestions.hasAllowedInput())
/* 146 */         return true; 
/* 148 */       handleChatInput(this.input.getValue(), true);
/* 149 */       if (this.closeOnSubmit) {
/* 150 */         this.exitReason = ExitReason.DONE;
/* 151 */         this.minecraft.gui.setScreen(null);
/*     */       } else {
/* 153 */         this.input.setValue("");
/* 154 */         this.minecraft.gui.hud.getChat().resetChatScroll();
/*     */       } 
/* 156 */       return true;
/*     */     } 
/* 159 */     switch (event.key()) {
/*     */       case 265:
/* 160 */         moveInHistory(-1);
/* 170 */         return true;
/*     */       case 264:
/*     */         moveInHistory(1);
/* 170 */         return true;
/*     */       case 266:
/*     */         this.minecraft.gui.hud.getChat().scrollChat(this.minecraft.gui.hud.getChat().getLinesPerPage() - 1);
/* 170 */         return true;
/*     */       case 267:
/*     */         this.minecraft.gui.hud.getChat().scrollChat(-this.minecraft.gui.hud.getChat().getLinesPerPage() + 1);
/* 170 */         return true;
/*     */     } 
/*     */     return false;
/*     */   }
/*     */   
/*     */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 175 */     scrollY = Mth.clamp(scrollY, -1.0D, 1.0D);
/* 176 */     if (this.commandSuggestions.mouseScrolled(scrollY))
/* 177 */       return true; 
/* 179 */     if (!this.minecraft.hasShiftDown())
/* 180 */       scrollY *= 7.0D; 
/* 182 */     this.minecraft.gui.hud.getChat().scrollChat((int)scrollY);
/* 183 */     return true;
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 188 */     if (this.commandSuggestions.mouseClicked(event))
/* 189 */       return true; 
/* 192 */     if (event.button() == 0) {
/* 193 */       int screenHeight = this.minecraft.getWindow().getGuiScaledHeight();
/* 195 */       ActiveTextCollector.ClickableStyleFinder finder = (new ActiveTextCollector.ClickableStyleFinder(getFont(), (int)event.x(), (int)event.y())).includeInsertions(insertionClickMode());
/* 196 */       this.minecraft.gui.hud.getChat().captureClickableText((ActiveTextCollector)finder, screenHeight, this.minecraft.gui.hud.getGuiTicks(), this.displayMode);
/* 197 */       Style clicked = finder.result();
/* 198 */       if (clicked != null && handleComponentClicked(clicked, insertionClickMode())) {
/* 199 */         this.initial = this.input.getValue();
/* 200 */         return true;
/*     */       } 
/*     */     } 
/* 203 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   private boolean insertionClickMode() {
/* 207 */     return this.minecraft.hasShiftDown();
/*     */   }
/*     */   
/*     */   private boolean handleComponentClicked(Style clicked, boolean allowInsertions) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokevirtual getClickEvent : ()Lnet/minecraft/network/chat/ClickEvent;
/*     */     //   4: astore_3
/*     */     //   5: iload_2
/*     */     //   6: ifeq -> 28
/*     */     //   9: aload_1
/*     */     //   10: invokevirtual getInsertion : ()Ljava/lang/String;
/*     */     //   13: ifnull -> 209
/*     */     //   16: aload_0
/*     */     //   17: aload_1
/*     */     //   18: invokevirtual getInsertion : ()Ljava/lang/String;
/*     */     //   21: iconst_0
/*     */     //   22: invokevirtual insertText : (Ljava/lang/String;Z)V
/*     */     //   25: goto -> 209
/*     */     //   28: aload_3
/*     */     //   29: ifnull -> 209
/*     */     //   32: aload_3
/*     */     //   33: dup
/*     */     //   34: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   37: pop
/*     */     //   38: astore #4
/*     */     //   40: iconst_0
/*     */     //   41: istore #5
/*     */     //   43: aload #4
/*     */     //   45: iload #5
/*     */     //   47: <illegal opcode> typeSwitch : (Lnet/minecraft/network/chat/ClickEvent;I)I
/*     */     //   52: lookupswitch default -> 195, 0 -> 80, 1 -> 137
/*     */     //   80: aload #4
/*     */     //   82: checkcast net/minecraft/network/chat/ClickEvent$Custom
/*     */     //   85: astore #6
/*     */     //   87: aload #6
/*     */     //   89: invokevirtual id : ()Lnet/minecraft/resources/Identifier;
/*     */     //   92: getstatic net/minecraft/client/gui/components/ChatComponent.QUEUE_EXPAND_ID : Lnet/minecraft/resources/Identifier;
/*     */     //   95: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   98: ifne -> 107
/*     */     //   101: iconst_1
/*     */     //   102: istore #5
/*     */     //   104: goto -> 43
/*     */     //   107: aload_0
/*     */     //   108: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   111: getfield gui : Lnet/minecraft/client/gui/Gui;
/*     */     //   114: invokevirtual chatListener : ()Lnet/minecraft/client/multiplayer/chat/ChatListener;
/*     */     //   117: astore #7
/*     */     //   119: aload #7
/*     */     //   121: invokevirtual queueSize : ()J
/*     */     //   124: lconst_0
/*     */     //   125: lcmp
/*     */     //   126: ifeq -> 134
/*     */     //   129: aload #7
/*     */     //   131: invokevirtual acceptNextDelayedMessage : ()V
/*     */     //   134: goto -> 207
/*     */     //   137: aload #4
/*     */     //   139: checkcast net/minecraft/network/chat/ClickEvent$Custom
/*     */     //   142: astore #7
/*     */     //   144: aload #7
/*     */     //   146: invokevirtual id : ()Lnet/minecraft/resources/Identifier;
/*     */     //   149: getstatic net/minecraft/client/gui/components/ChatComponent.GO_TO_RESTRICTIONS_SCREEN : Lnet/minecraft/resources/Identifier;
/*     */     //   152: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   155: ifne -> 164
/*     */     //   158: iconst_2
/*     */     //   159: istore #5
/*     */     //   161: goto -> 43
/*     */     //   164: aload_0
/*     */     //   165: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   168: getfield gui : Lnet/minecraft/client/gui/Gui;
/*     */     //   171: new net/minecraft/client/gui/screens/multiplayer/RestrictionsScreen
/*     */     //   174: dup
/*     */     //   175: aload_0
/*     */     //   176: aload_0
/*     */     //   177: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   180: getfield player : Lnet/minecraft/client/player/LocalPlayer;
/*     */     //   183: invokevirtual chatAbilities : ()Lnet/minecraft/client/multiplayer/chat/ChatAbilities;
/*     */     //   186: invokespecial <init> : (Lnet/minecraft/client/gui/screens/Screen;Lnet/minecraft/client/multiplayer/chat/ChatAbilities;)V
/*     */     //   189: invokevirtual setScreen : (Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   192: goto -> 207
/*     */     //   195: aload_3
/*     */     //   196: aload_0
/*     */     //   197: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   200: aload_0
/*     */     //   201: invokestatic defaultHandleGameClickEvent : (Lnet/minecraft/network/chat/ClickEvent;Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   204: goto -> 207
/*     */     //   207: iconst_1
/*     */     //   208: ireturn
/*     */     //   209: iconst_0
/*     */     //   210: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #211	-> 0
/*     */     //   #212	-> 5
/*     */     //   #213	-> 9
/*     */     //   #214	-> 16
/*     */     //   #216	-> 28
/*     */     //   #217	-> 32
/*     */     //   #219	-> 80
/*     */     //   #220	-> 107
/*     */     //   #221	-> 119
/*     */     //   #222	-> 129
/*     */     //   #224	-> 134
/*     */     //   #226	-> 137
/*     */     //   #227	-> 164
/*     */     //   #228	-> 192
/*     */     //   #229	-> 195
/*     */     //   #231	-> 207
/*     */     //   #233	-> 209
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   119	15	7	chatListener	Lnet/minecraft/client/multiplayer/chat/ChatListener;
/*     */     //   87	50	6	customEvent	Lnet/minecraft/network/chat/ClickEvent$Custom;
/*     */     //   144	51	7	customEvent	Lnet/minecraft/network/chat/ClickEvent$Custom;
/*     */     //   0	211	0	this	Lnet/minecraft/client/gui/screens/ChatScreen;
/*     */     //   0	211	1	clicked	Lnet/minecraft/network/chat/Style;
/*     */     //   0	211	2	allowInsertions	Z
/*     */     //   5	206	3	event	Lnet/minecraft/network/chat/ClickEvent;
/*     */   }
/*     */   
/*     */   public void insertText(String text, boolean replace) {
/* 238 */     if (replace) {
/* 239 */       this.input.setValue(text);
/*     */     } else {
/* 241 */       this.input.insertText(text);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void moveInHistory(int dir) {
/* 246 */     int newPos = this.historyPos + dir;
/* 247 */     int max = this.minecraft.gui.hud.getChat().getRecentChat().size();
/* 249 */     newPos = Mth.clamp(newPos, 0, max);
/* 250 */     if (newPos == this.historyPos)
/*     */       return; 
/* 254 */     if (newPos == max) {
/* 255 */       this.historyPos = max;
/* 256 */       this.input.setValue(this.historyBuffer);
/*     */       return;
/*     */     } 
/* 260 */     if (this.historyPos == max)
/* 261 */       this.historyBuffer = this.input.getValue(); 
/* 264 */     this.input.setValue((String)this.minecraft.gui.hud.getChat().getRecentChat().get(newPos));
/* 265 */     this.commandSuggestions.setAllowSuggestions(false);
/* 266 */     this.historyPos = newPos;
/*     */   }
/*     */   
/*     */   private FormattedCharSequence formatChat(String text, int offset) {
/* 270 */     if (this.isDraft)
/* 271 */       return FormattedCharSequence.forward(text, Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(Boolean.valueOf(true))); 
/* 273 */     return null;
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 279 */     graphics.fill(2, this.height - 14, this.width - 2, this.height - 2, this.minecraft.options.getBackgroundColor(-2147483648));
/* 280 */     this.minecraft.gui.hud.getChat().extractRenderState(graphics, this.font, this.minecraft.gui.hud.getGuiTicks(), mouseX, mouseY, this.displayMode, insertionClickMode());
/* 282 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/* 283 */     this.commandSuggestions.extractRenderState(graphics, mouseX, mouseY);
/*     */   }
/*     */   
/*     */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {}
/*     */   
/*     */   public boolean isPauseScreen() {
/* 293 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAllowedInPortal() {
/* 298 */     return true;
/*     */   }
/*     */   
/*     */   protected void updateNarrationState(NarrationElementOutput output) {
/* 303 */     output.add(NarratedElementType.TITLE, getTitle());
/* 304 */     if (this.displayMode.showRestrictedPrompt) {
/* 305 */       output.add(NarratedElementType.USAGE, (Component)CommonComponents.joinForNarration(new Component[] { USAGE_TEXT, RESTRICTED_NARRATION_TEXT }));
/*     */     } else {
/* 307 */       output.add(NarratedElementType.USAGE, USAGE_TEXT);
/*     */     } 
/* 309 */     String value = this.input.getValue();
/* 310 */     if (!value.isEmpty())
/* 311 */       output.nest().add(NarratedElementType.TITLE, (Component)Component.translatable("chat_screen.message", new Object[] { value })); 
/*     */   }
/*     */   
/*     */   public void handleChatInput(String msg, boolean addToRecent) {
/* 316 */     msg = normalizeChatMessage(msg);
/* 317 */     if (msg.isEmpty())
/*     */       return; 
/* 321 */     if (addToRecent)
/* 322 */       this.minecraft.gui.hud.getChat().addRecentChat(msg); 
/* 325 */     if (msg.startsWith("/")) {
/* 326 */       this.minecraft.player.connection.sendCommand(msg.substring(1));
/*     */     } else {
/* 328 */       this.minecraft.player.connection.sendChat(msg);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String normalizeChatMessage(String message) {
/* 333 */     return StringUtil.trimChatMessage(StringUtils.normalizeSpace(message.trim()));
/*     */   }
/*     */   
/*     */   protected enum ExitReason {
/* 342 */     INTENTIONAL, INTERRUPTED, DONE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\ChatScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */