/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.client.CommandHistory;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.TextAlignment;
/*     */ import net.minecraft.client.gui.screens.ChatScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.multiplayer.chat.GuiMessage;
/*     */ import net.minecraft.client.multiplayer.chat.GuiMessageSource;
/*     */ import net.minecraft.client.multiplayer.chat.GuiMessageTag;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MessageSignature;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.ArrayListDeque;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import org.joml.Matrix3x2f;
/*     */ import org.joml.Matrix3x2fc;
/*     */ import org.joml.Vector2f;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChatComponent {
/*  48 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MAX_CHAT_HISTORY = 100;
/*     */   
/*     */   private static final int MESSAGE_INDENT = 4;
/*     */   
/*     */   private static final int BOTTOM_MARGIN = 40;
/*     */   
/*     */   private static final int TOOLTIP_MAX_WIDTH = 210;
/*     */   
/*     */   private static final int TIME_BEFORE_MESSAGE_DELETION = 60;
/*     */   
/*  56 */   private static final Component DELETED_CHAT_MESSAGE = (Component)Component.translatable("chat.deleted_marker").withStyle(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC });
/*     */   
/*     */   public static final int MESSAGE_BOTTOM_TO_MESSAGE_TOP = 8;
/*     */   
/*  61 */   public static final Identifier QUEUE_EXPAND_ID = Identifier.withDefaultNamespace("internal/expand_chat_queue");
/*     */   
/*  62 */   private static final Style QUEUE_EXPAND_TEXT_STYLE = Style.EMPTY
/*  63 */     .withClickEvent((ClickEvent)new ClickEvent.Custom(QUEUE_EXPAND_ID, Optional.empty()))
/*  64 */     .withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)Component.translatable("chat.queue.tooltip")));
/*     */   
/*  66 */   public static final Identifier GO_TO_RESTRICTIONS_SCREEN = Identifier.withDefaultNamespace("internal/go_to_restrictions_screen");
/*     */   
/*  67 */   private static final Component RESTRICTED_CHAT_MESSAGE = (Component)Component.translatable("chat_screen.restricted")
/*  68 */     .withStyle(Style.EMPTY
/*  69 */       .withColor(ChatFormatting.RED)
/*  70 */       .withUnderlined(Boolean.valueOf(true))
/*  71 */       .withClickEvent((ClickEvent)new ClickEvent.Custom(GO_TO_RESTRICTIONS_SCREEN, Optional.empty())));
/*     */   
/*  73 */   private static final Component RESTRICTED_CHAT_MESSAGE_WITH_HOVER = ComponentUtils.mergeStyles(RESTRICTED_CHAT_MESSAGE, Style.EMPTY
/*  74 */       .withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)Component.translatable("chat_screen.restricted"))));
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final CommandHistory commandHistory;
/*     */   
/*  79 */   private final ArrayListDeque<String> recentChat = new ArrayListDeque(100);
/*     */   
/*  80 */   private final List<GuiMessage> allMessages = Lists.newArrayList();
/*     */   
/*  81 */   private final List<GuiMessage.Line> trimmedMessages = Lists.newArrayList();
/*     */   
/*     */   private int chatScrollbarPos;
/*     */   
/*     */   private boolean newMessageSinceScroll;
/*     */   
/*     */   private Draft latestDraft;
/*     */   
/*     */   private ChatScreen preservedScreen;
/*     */   
/*  88 */   private final List<DelayedMessageDeletion> messageDeletionQueue = new ArrayList<>();
/*     */   
/*     */   private Predicate<GuiMessage> visibleMessageFilter = paramGuiMessage -> true;
/*     */   
/*     */   public ChatComponent(Minecraft minecraft) {
/*  93 */     this.minecraft = minecraft;
/*  94 */     this.commandHistory = new CommandHistory(minecraft.gameDirectory.toPath());
/*  95 */     this.recentChat.addAll(this.commandHistory.history());
/*     */   }
/*     */   
/*     */   public void tick() {
/*  99 */     if (!this.messageDeletionQueue.isEmpty())
/* 100 */       processMessageDeletionQueue(); 
/*     */   }
/*     */   
/*     */   public void setVisibleMessageFilter(Predicate<GuiMessage> visibleMessageFilter) {
/* 105 */     this.visibleMessageFilter = visibleMessageFilter;
/* 106 */     refreshTrimmedMessages();
/*     */   }
/*     */   
/*     */   private int forEachLine(AlphaCalculator alphaCalculator, LineConsumer lineConsumer) {
/* 110 */     int perPage = getLinesPerPage();
/* 112 */     int count = 0;
/* 113 */     for (int i = Math.min(this.trimmedMessages.size() - this.chatScrollbarPos, perPage) - 1; i >= 0; i--) {
/* 114 */       int messageIndex = i + this.chatScrollbarPos;
/* 115 */       GuiMessage.Line message = this.trimmedMessages.get(messageIndex);
/* 116 */       float alpha = alphaCalculator.calculate(message);
/* 117 */       if (alpha > 1.0E-5F) {
/* 118 */         count++;
/* 120 */         lineConsumer.accept(message, i, alpha);
/*     */       } 
/*     */     } 
/* 124 */     return count;
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, Font font, int ticks, int mouseX, int mouseY, DisplayMode displayMode, boolean changeCursorOnInsertions) {
/* 128 */     graphics.pose().pushMatrix();
/* 129 */     extractRenderState(displayMode.foreground ? new DrawingFocusedGraphicsAccess(graphics, font, mouseX, mouseY, changeCursorOnInsertions) : new DrawingBackgroundGraphicsAccess(graphics), graphics.guiHeight(), ticks, displayMode);
/* 130 */     graphics.pose().popMatrix();
/*     */   }
/*     */   
/*     */   public void captureClickableText(ActiveTextCollector activeTextCollector, int screenHeight, int ticks, DisplayMode displayMode) {
/* 134 */     extractRenderState(new ClickableTextOnlyGraphicsAccess(activeTextCollector), screenHeight, ticks, displayMode);
/*     */   }
/*     */   
/*     */   private void extractRenderState(final ChatGraphicsAccess graphics, int screenHeight, int ticks, DisplayMode displayMode) {
/* 138 */     boolean isForeground = displayMode.foreground;
/* 139 */     boolean isRestricted = displayMode.showRestrictedPrompt;
/* 141 */     int total = this.trimmedMessages.size();
/* 143 */     if (total <= 0 && !isRestricted)
/*     */       return; 
/* 147 */     ProfilerFiller profiler = Profiler.get();
/* 148 */     profiler.push("chat");
/* 150 */     float scale = (float)getScale();
/* 151 */     int maxWidth = Mth.ceil(getWidth() / scale);
/* 153 */     final int chatBottom = Mth.floor((screenHeight - 40) / scale);
/* 155 */     final float textOpacity = ((Double)this.minecraft.options.chatOpacity().get()).floatValue() * 0.9F + 0.1F;
/* 156 */     float backgroundOpacity = ((Double)this.minecraft.options.textBackgroundOpacity().get()).floatValue();
/* 160 */     Objects.requireNonNull(this.minecraft.font);
/* 160 */     final int messageHeight = 9;
/* 161 */     int messageBottomToMessageTop = 8;
/* 164 */     double chatLineSpacing = ((Double)this.minecraft.options.chatLineSpacing().get()).doubleValue();
/* 166 */     final int entryHeight = (int)(messageHeight * (chatLineSpacing + 1.0D));
/* 168 */     final int entryBottomToMessageY = (int)Math.round(8.0D * (chatLineSpacing + 1.0D) - 4.0D * chatLineSpacing);
/* 170 */     long queueSize = this.minecraft.gui.chatListener().queueSize();
/* 172 */     AlphaCalculator alphaCalculator = isForeground ? AlphaCalculator.FULLY_VISIBLE : AlphaCalculator.timeBased(ticks);
/* 174 */     graphics.updatePose(pose -> {
/*     */           pose.scale(scale, scale);
/*     */           pose.translate(4.0F, 0.0F);
/*     */         });
/* 182 */     int count = forEachLine(alphaCalculator, (paramLine, lineIndex, alpha) -> {
/*     */           int entryBottom = chatBottom - lineIndex * entryHeight;
/*     */           int entryTop = entryBottom - entryHeight;
/*     */           graphics.fill(-4, entryTop, maxWidth + 4 + 4, entryBottom, ARGB.black(alpha * backgroundOpacity));
/*     */         });
/* 189 */     int lineAboveMessagesY = chatBottom - (count + 1) * entryHeight;
/* 191 */     if (queueSize > 0L)
/* 192 */       graphics.fill(-2, chatBottom, maxWidth + 4, chatBottom + messageHeight, ARGB.black(backgroundOpacity)); 
/* 195 */     if (isRestricted)
/* 196 */       graphics.fill(-2, lineAboveMessagesY, maxWidth + 4 + 4, lineAboveMessagesY + entryHeight, ARGB.black(backgroundOpacity)); 
/* 200 */     forEachLine(alphaCalculator, new LineConsumer() {
/*     */           private boolean hoveredOverCurrentMessage;
/*     */           
/*     */           public void accept(GuiMessage.Line line, int lineIndex, float alpha) {
/*     */             boolean forceIconRendering;
/* 205 */             int entryBottom = chatBottom - lineIndex * entryHeight;
/* 206 */             int entryTop = entryBottom - entryHeight;
/* 208 */             int textTop = entryBottom - entryBottomToMessageY;
/* 209 */             boolean hoveredOverCurrentLine = graphics.handleMessage(textTop, alpha * textOpacity, line.content());
/* 210 */             this.hoveredOverCurrentMessage |= hoveredOverCurrentLine;
/* 215 */             if (line.endOfEntry()) {
/* 216 */               forceIconRendering = this.hoveredOverCurrentMessage;
/* 217 */               this.hoveredOverCurrentMessage = false;
/*     */             } else {
/* 219 */               forceIconRendering = false;
/*     */             } 
/* 222 */             GuiMessageTag tag = line.tag();
/* 223 */             if (tag != null) {
/* 225 */               graphics.handleTag(-4, entryTop, -2, entryBottom, alpha * textOpacity, tag);
/* 226 */               if (tag.icon() != null) {
/* 227 */                 int iconLeft = line.getTagIconLeft(ChatComponent.this.minecraft.font);
/* 228 */                 int textBottom = textTop + messageHeight;
/* 230 */                 graphics.handleTagIcon(iconLeft, textBottom, forceIconRendering, tag, tag.icon());
/*     */               } 
/*     */             } 
/*     */           }
/*     */         });
/* 236 */     if (queueSize > 0L) {
/* 237 */       int queueLineBottom = chatBottom + messageHeight;
/* 238 */       MutableComponent mutableComponent = Component.translatable("chat.queue", new Object[] { Long.valueOf(queueSize) }).setStyle(QUEUE_EXPAND_TEXT_STYLE);
/* 239 */       graphics.handleMessage(queueLineBottom - 8, 0.5F * textOpacity, mutableComponent.getVisualOrderText());
/*     */     } 
/* 242 */     if (isRestricted) {
/* 243 */       int restrictedMessageWidth = this.minecraft.font.width((FormattedText)RESTRICTED_CHAT_MESSAGE);
/* 244 */       FormattedCharSequence restrictedMessage = (restrictedMessageWidth > maxWidth) ? ComponentRenderUtils.clipText(RESTRICTED_CHAT_MESSAGE_WITH_HOVER, this.minecraft.font, maxWidth) : RESTRICTED_CHAT_MESSAGE.getVisualOrderText();
/* 246 */       graphics.handleMessage(lineAboveMessagesY + entryHeight - entryBottomToMessageY - 1, textOpacity, restrictedMessage);
/*     */     } 
/* 250 */     if (total > 0 && isForeground) {
/* 251 */       int chatHeight = count * entryHeight;
/* 252 */       int virtualHeight = total * entryHeight;
/* 253 */       int y = this.chatScrollbarPos * chatHeight / total - chatBottom;
/* 254 */       int height = chatHeight * chatHeight / virtualHeight;
/* 256 */       if (virtualHeight != chatHeight) {
/* 257 */         int alpha = (y > 0) ? 170 : 96;
/* 258 */         int color = this.newMessageSinceScroll ? 13382451 : 3355562;
/* 259 */         int scrollBarStartX = maxWidth + 4;
/* 260 */         graphics.fill(scrollBarStartX, -y, scrollBarStartX + 2, -y - height, ARGB.color(alpha, color));
/* 261 */         graphics.fill(scrollBarStartX + 2, -y, scrollBarStartX + 1, -y - height, ARGB.color(alpha, 13421772));
/*     */       } 
/*     */     } 
/* 265 */     profiler.pop();
/*     */   }
/*     */   
/*     */   public void clearMessages(boolean history) {
/* 269 */     this.minecraft.gui.chatListener().flushQueue();
/* 270 */     this.messageDeletionQueue.clear();
/* 271 */     this.trimmedMessages.clear();
/* 272 */     this.allMessages.clear();
/* 273 */     if (history) {
/* 274 */       this.recentChat.clear();
/* 275 */       this.recentChat.addAll(this.commandHistory.history());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addClientSystemMessage(Component message) {
/* 280 */     addMessage(message, null, GuiMessageSource.SYSTEM_CLIENT, GuiMessageTag.systemSinglePlayer());
/*     */   }
/*     */   
/*     */   public void addServerSystemMessage(Component message) {
/* 284 */     addMessage(message, null, GuiMessageSource.SYSTEM_SERVER, GuiMessageTag.systemSinglePlayer());
/*     */   }
/*     */   
/*     */   public void addPlayerMessage(Component message, MessageSignature signature, GuiMessageTag tag) {
/* 288 */     addMessage(message, signature, GuiMessageSource.PLAYER, tag);
/*     */   }
/*     */   
/*     */   private void addMessage(Component contents, MessageSignature signature, GuiMessageSource source, GuiMessageTag tag) {
/* 292 */     GuiMessage message = new GuiMessage(this.minecraft.gui.hud.getGuiTicks(), contents, signature, source, tag);
/* 295 */     if (this.visibleMessageFilter.test(message)) {
/* 296 */       logChatMessage(message);
/* 297 */       addMessageToDisplayQueue(message);
/* 298 */       addMessageToQueue(message);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void logChatMessage(GuiMessage message) {
/* 303 */     String messageString = message.content().getString().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n");
/* 304 */     String logTag = (String)Optionull.map(message.tag(), GuiMessageTag::logTag);
/* 305 */     if (logTag != null) {
/* 306 */       LOGGER.info("[{}] [CHAT] {}", logTag, messageString);
/*     */     } else {
/* 308 */       LOGGER.info("[CHAT] {}", messageString);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addMessageToDisplayQueue(GuiMessage message) {
/* 313 */     int maxWidth = Mth.floor(getWidth() / getScale());
/* 315 */     List<FormattedCharSequence> lines = message.splitLines(this.minecraft.font, maxWidth);
/* 317 */     boolean chatting = isChatFocused();
/* 318 */     for (int i = 0; i < lines.size(); i++) {
/* 319 */       FormattedCharSequence line = lines.get(i);
/* 320 */       if (chatting && this.chatScrollbarPos > 0) {
/* 321 */         this.newMessageSinceScroll = true;
/* 322 */         scrollChat(1);
/*     */       } 
/* 325 */       boolean endOfEntry = (i == lines.size() - 1);
/* 326 */       this.trimmedMessages.addFirst(new GuiMessage.Line(message, line, endOfEntry));
/*     */     } 
/* 329 */     while (this.trimmedMessages.size() > 100)
/* 330 */       this.trimmedMessages.removeLast(); 
/*     */   }
/*     */   
/*     */   private void addMessageToQueue(GuiMessage message) {
/* 335 */     this.allMessages.addFirst(message);
/* 337 */     while (this.allMessages.size() > 100)
/* 338 */       this.allMessages.removeLast(); 
/*     */   }
/*     */   
/*     */   private void processMessageDeletionQueue() {
/* 343 */     int time = this.minecraft.gui.hud.getGuiTicks();
/* 344 */     this.messageDeletionQueue.removeIf(entry -> (time >= entry.deletableAfter()) ? ((deleteMessageOrDelay(entry.signature()) == null)) : false);
/*     */   }
/*     */   
/*     */   public void deleteMessage(MessageSignature signature) {
/* 353 */     DelayedMessageDeletion delayedMessage = deleteMessageOrDelay(signature);
/* 354 */     if (delayedMessage != null)
/* 355 */       this.messageDeletionQueue.add(delayedMessage); 
/*     */   }
/*     */   
/*     */   private DelayedMessageDeletion deleteMessageOrDelay(MessageSignature signature) {
/* 360 */     int time = this.minecraft.gui.hud.getGuiTicks();
/* 362 */     for (ListIterator<GuiMessage> iterator = this.allMessages.listIterator(); iterator.hasNext(); ) {
/* 363 */       GuiMessage message = iterator.next();
/* 364 */       if (signature.equals(message.signature())) {
/* 365 */         int deletableAfter = message.addedTime() + 60;
/* 366 */         if (time >= deletableAfter) {
/* 367 */           iterator.set(createDeletedMarker(message));
/* 368 */           refreshTrimmedMessages();
/* 369 */           return null;
/*     */         } 
/* 371 */         return new DelayedMessageDeletion(signature, deletableAfter);
/*     */       } 
/*     */     } 
/* 376 */     return null;
/*     */   }
/*     */   
/*     */   private static GuiMessage createDeletedMarker(GuiMessage message) {
/* 380 */     return new GuiMessage(message.addedTime(), DELETED_CHAT_MESSAGE, null, GuiMessageSource.SYSTEM_SERVER, GuiMessageTag.system());
/*     */   }
/*     */   
/*     */   public void rescaleChat() {
/* 384 */     resetChatScroll();
/* 385 */     refreshTrimmedMessages();
/*     */   }
/*     */   
/*     */   private void refreshTrimmedMessages() {
/* 389 */     this.trimmedMessages.clear();
/* 390 */     for (GuiMessage message : Lists.reverse(this.allMessages)) {
/* 391 */       if (this.visibleMessageFilter.test(message))
/* 392 */         addMessageToDisplayQueue(message); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ArrayListDeque<String> getRecentChat() {
/* 398 */     return this.recentChat;
/*     */   }
/*     */   
/*     */   public void addRecentChat(String message) {
/* 402 */     if (!message.equals(this.recentChat.peekLast())) {
/* 403 */       if (this.recentChat.size() >= 100)
/* 404 */         this.recentChat.removeFirst(); 
/* 406 */       this.recentChat.addLast(message);
/*     */     } 
/* 408 */     if (message.startsWith("/"))
/* 409 */       this.commandHistory.addCommand(message); 
/*     */   }
/*     */   
/*     */   public void resetChatScroll() {
/* 414 */     this.chatScrollbarPos = 0;
/* 415 */     this.newMessageSinceScroll = false;
/*     */   }
/*     */   
/*     */   public void scrollChat(int dir) {
/* 419 */     this.chatScrollbarPos += dir;
/* 420 */     int max = this.trimmedMessages.size();
/* 422 */     if (this.chatScrollbarPos > max - getLinesPerPage())
/* 423 */       this.chatScrollbarPos = max - getLinesPerPage(); 
/* 426 */     if (this.chatScrollbarPos <= 0) {
/* 427 */       this.chatScrollbarPos = 0;
/* 428 */       this.newMessageSinceScroll = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isChatFocused() {
/* 433 */     return this.minecraft.gui.screen() instanceof ChatScreen;
/*     */   }
/*     */   
/*     */   private int getWidth() {
/* 437 */     return getWidth(((Double)this.minecraft.options.chatWidth().get()).doubleValue());
/*     */   }
/*     */   
/*     */   private int getHeight() {
/* 441 */     return getHeight((isChatFocused() ? (Double)this.minecraft.options.chatHeightFocused().get() : (Double)this.minecraft.options.chatHeightUnfocused().get()).doubleValue());
/*     */   }
/*     */   
/*     */   private double getScale() {
/* 445 */     return ((Double)this.minecraft.options.chatScale().get()).doubleValue();
/*     */   }
/*     */   
/*     */   public static int getWidth(double pct) {
/* 449 */     int max = 320;
/* 450 */     int min = 40;
/* 451 */     return Mth.floor(pct * 280.0D + 40.0D);
/*     */   }
/*     */   
/*     */   public static int getHeight(double pct) {
/* 455 */     int max = 180;
/* 456 */     int min = 20;
/* 457 */     return Mth.floor(pct * 160.0D + 20.0D);
/*     */   }
/*     */   
/*     */   public static double defaultUnfocusedPct() {
/* 461 */     int max = 180;
/* 462 */     int min = 20;
/* 463 */     return 70.0D / (getHeight(1.0D) - 20);
/*     */   }
/*     */   
/*     */   public int getLinesPerPage() {
/* 467 */     return getHeight() / getLineHeight();
/*     */   }
/*     */   
/*     */   private int getLineHeight() {
/* 471 */     Objects.requireNonNull(this.minecraft.font);
/* 471 */     return (int)(9.0D * (((Double)this.minecraft.options.chatLineSpacing().get()).doubleValue() + 1.0D));
/*     */   }
/*     */   
/*     */   public void saveAsDraft(String text) {
/* 475 */     boolean isCommand = text.startsWith("/");
/* 476 */     this.latestDraft = new Draft(text, isCommand ? ChatMethod.COMMAND : ChatMethod.MESSAGE);
/*     */   }
/*     */   
/*     */   public void discardDraft() {
/* 480 */     this.latestDraft = null;
/*     */   }
/*     */   
/*     */   public <T extends ChatScreen> T createScreen(ChatMethod chatMethod, ChatScreen.ChatConstructor<T> chat) {
/* 484 */     if (this.latestDraft != null && chatMethod.isDraftRestorable(this.latestDraft))
/* 485 */       return (T)chat.create(this.latestDraft.text(), true); 
/* 487 */     return (T)chat.create(chatMethod.prefix(), false);
/*     */   }
/*     */   
/*     */   public void openScreen(ChatMethod chatMethod, ChatScreen.ChatConstructor<?> chat) {
/* 492 */     this.minecraft.gui.setScreen((Screen)createScreen(chatMethod, chat));
/*     */   }
/*     */   
/*     */   public void preserveCurrentChatScreen() {
/* 496 */     Screen screen = this.minecraft.gui.screen();
/* 496 */     if (screen instanceof ChatScreen) {
/* 496 */       ChatScreen chatScreen = (ChatScreen)screen;
/* 497 */       this.preservedScreen = chatScreen;
/*     */     } 
/*     */   }
/*     */   
/*     */   public ChatScreen restoreChatScreen() {
/* 502 */     ChatScreen restoredScreen = this.preservedScreen;
/* 503 */     this.preservedScreen = null;
/* 504 */     return restoredScreen;
/*     */   }
/*     */   
/*     */   private static final class DelayedMessageDeletion extends Record {
/*     */     private final MessageSignature signature;
/*     */     
/*     */     private final int deletableAfter;
/*     */     
/*     */     private DelayedMessageDeletion(MessageSignature signature, int deletableAfter) {
/* 507 */       this.signature = signature;
/* 507 */       this.deletableAfter = deletableAfter;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #507	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #507	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #507	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public MessageSignature signature() {
/* 507 */       return this.signature;
/*     */     }
/*     */     
/*     */     public int deletableAfter() {
/* 507 */       return this.deletableAfter;
/*     */     }
/*     */   }
/*     */   
/*     */   public State storeState() {
/* 511 */     return new State(
/* 512 */         List.copyOf(this.allMessages), 
/* 513 */         List.copyOf((Collection<? extends String>)this.recentChat), 
/* 514 */         List.copyOf(this.messageDeletionQueue));
/*     */   }
/*     */   
/*     */   public void restoreState(State state) {
/* 519 */     this.recentChat.clear();
/* 520 */     this.recentChat.addAll(state.history);
/* 522 */     this.messageDeletionQueue.clear();
/* 523 */     this.messageDeletionQueue.addAll(state.delayedMessageDeletions);
/* 525 */     this.allMessages.clear();
/* 526 */     this.allMessages.addAll(state.messages);
/* 528 */     refreshTrimmedMessages();
/*     */   }
/*     */   
/*     */   public static class State {
/*     */     private final List<GuiMessage> messages;
/*     */     
/*     */     private final List<String> history;
/*     */     
/*     */     private final List<ChatComponent.DelayedMessageDeletion> delayedMessageDeletions;
/*     */     
/*     */     public State(List<GuiMessage> messages, List<String> history, List<ChatComponent.DelayedMessageDeletion> delayedMessageDeletions) {
/* 537 */       this.messages = messages;
/* 538 */       this.history = history;
/* 539 */       this.delayedMessageDeletions = delayedMessageDeletions;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Draft extends Record {
/*     */     private final String text;
/*     */     
/*     */     private final ChatComponent.ChatMethod chatMethod;
/*     */     
/*     */     public Draft(String text, ChatComponent.ChatMethod chatMethod) {
/* 543 */       this.text = text;
/* 543 */       this.chatMethod = chatMethod;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/ChatComponent$Draft;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #543	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/ChatComponent$Draft;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/ChatComponent$Draft;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #543	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/ChatComponent$Draft;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/ChatComponent$Draft;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #543	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/components/ChatComponent$Draft;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String text() {
/* 543 */       return this.text;
/*     */     }
/*     */     
/*     */     public ChatComponent.ChatMethod chatMethod() {
/* 543 */       return this.chatMethod;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum ChatMethod {
/* 547 */     MESSAGE("") {
/*     */       public boolean isDraftRestorable(ChatComponent.Draft draft) {
/* 550 */         return true;
/*     */       }
/*     */     },
/* 553 */     COMMAND("/") {
/*     */       public boolean isDraftRestorable(ChatComponent.Draft draft) {
/* 556 */         return (this == draft.chatMethod);
/*     */       }
/*     */     };
/*     */     
/*     */     private final String prefix;
/*     */     
/*     */     ChatMethod(String prefix) {
/* 563 */       this.prefix = prefix;
/*     */     }
/*     */     
/*     */     public String prefix() {
/* 567 */       return this.prefix;
/*     */     }
/*     */     
/*     */     public abstract boolean isDraftRestorable(ChatComponent.Draft param1Draft);
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public boolean isDraftRestorable(ChatComponent.Draft draft) {
/*     */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public boolean isDraftRestorable(ChatComponent.Draft draft) {
/*     */       return (this == draft.chatMethod);
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface AlphaCalculator {
/*     */     public static final AlphaCalculator FULLY_VISIBLE = param1Line -> 1.0F;
/*     */     
/*     */     static AlphaCalculator timeBased(int currentTickTime) {
/* 583 */       return message -> {
/*     */           int tickDelta = currentTickTime - message.addedTime();
/*     */           double t = tickDelta / 200.0D;
/*     */           t = 1.0D - t;
/*     */           t *= 10.0D;
/*     */           t = Mth.clamp(t, 0.0D, 1.0D);
/*     */           t *= t;
/*     */           return (float)t;
/*     */         };
/*     */     }
/*     */     
/*     */     float calculate(GuiMessage.Line param1Line);
/*     */   }
/*     */   
/*     */   private static class DrawingBackgroundGraphicsAccess implements ChatGraphicsAccess {
/*     */     private final GuiGraphicsExtractor graphics;
/*     */     
/*     */     private final ActiveTextCollector textRenderer;
/*     */     
/*     */     private ActiveTextCollector.Parameters parameters;
/*     */     
/*     */     public DrawingBackgroundGraphicsAccess(GuiGraphicsExtractor graphics) {
/* 616 */       this.graphics = graphics;
/* 617 */       this.textRenderer = graphics.textRenderer(GuiGraphicsExtractor.HoveredTextEffects.NONE, null);
/* 618 */       this.parameters = this.textRenderer.defaultParameters();
/*     */     }
/*     */     
/*     */     public void updatePose(Consumer<Matrix3x2f> updater) {
/* 623 */       updater.accept(this.graphics.pose());
/* 624 */       this.parameters = this.parameters.withPose((Matrix3x2fc)new Matrix3x2f((Matrix3x2fc)this.graphics.pose()));
/*     */     }
/*     */     
/*     */     public void fill(int x0, int y0, int x1, int y1, int color) {
/* 629 */       this.graphics.fill(x0, y0, x1, y1, color);
/*     */     }
/*     */     
/*     */     public boolean handleMessage(int textTop, float opacity, FormattedCharSequence message) {
/* 634 */       this.textRenderer.accept(TextAlignment.LEFT, 0, textTop, this.parameters.withOpacity(opacity), message);
/* 635 */       return false;
/*     */     }
/*     */     
/*     */     public void handleTag(int x0, int y0, int x1, int y1, float opacity, GuiMessageTag tag) {
/* 640 */       int indicatorColor = ARGB.color(opacity, tag.indicatorColor());
/* 641 */       this.graphics.fill(x0, y0, x1, y1, indicatorColor);
/*     */     }
/*     */     
/*     */     public void handleTagIcon(int left, int bottom, boolean forceVisible, GuiMessageTag tag, GuiMessageTag.Icon icon) {}
/*     */   }
/*     */   
/*     */   private static class DrawingFocusedGraphicsAccess implements ChatGraphicsAccess, Consumer<Style> {
/*     */     private final GuiGraphicsExtractor graphics;
/*     */     
/*     */     private final Font font;
/*     */     
/*     */     private final ActiveTextCollector textRenderer;
/*     */     
/*     */     private ActiveTextCollector.Parameters parameters;
/*     */     
/*     */     private final int globalMouseX;
/*     */     
/*     */     private final int globalMouseY;
/*     */     
/* 657 */     private final Vector2f localMousePos = new Vector2f();
/*     */     
/*     */     private Style hoveredStyle;
/*     */     
/*     */     private final boolean changeCursorOnInsertions;
/*     */     
/*     */     public DrawingFocusedGraphicsAccess(GuiGraphicsExtractor graphics, Font font, int mouseX, int mouseY, boolean changeCursorOnInsertions) {
/* 663 */       this.graphics = graphics;
/* 664 */       this.font = font;
/* 665 */       this.textRenderer = graphics.textRenderer(GuiGraphicsExtractor.HoveredTextEffects.TOOLTIP_AND_CURSOR, this);
/* 666 */       this.globalMouseX = mouseX;
/* 667 */       this.globalMouseY = mouseY;
/* 668 */       this.changeCursorOnInsertions = changeCursorOnInsertions;
/* 669 */       this.parameters = this.textRenderer.defaultParameters();
/* 670 */       updateLocalMousePos();
/*     */     }
/*     */     
/*     */     private void updateLocalMousePos() {
/* 674 */       this.graphics.pose().invert(new Matrix3x2f()).transformPosition(this.globalMouseX, this.globalMouseY, this.localMousePos);
/*     */     }
/*     */     
/*     */     public void updatePose(Consumer<Matrix3x2f> updater) {
/* 679 */       updater.accept(this.graphics.pose());
/* 680 */       this.parameters = this.parameters.withPose((Matrix3x2fc)new Matrix3x2f((Matrix3x2fc)this.graphics.pose()));
/* 681 */       updateLocalMousePos();
/*     */     }
/*     */     
/*     */     public void fill(int x0, int y0, int x1, int y1, int color) {
/* 686 */       this.graphics.fill(x0, y0, x1, y1, color);
/*     */     }
/*     */     
/*     */     public void accept(Style style) {
/* 691 */       this.hoveredStyle = style;
/*     */     }
/*     */     
/*     */     public boolean handleMessage(int textTop, float opacity, FormattedCharSequence message) {
/* 696 */       this.hoveredStyle = null;
/* 697 */       this.textRenderer.accept(TextAlignment.LEFT, 0, textTop, this.parameters.withOpacity(opacity), message);
/* 698 */       if (this.changeCursorOnInsertions && this.hoveredStyle != null && this.hoveredStyle.getInsertion() != null)
/* 699 */         this.graphics.requestCursor(CursorTypes.POINTING_HAND); 
/* 701 */       return (this.hoveredStyle != null);
/*     */     }
/*     */     
/*     */     private boolean isMouseOver(int left, int top, int right, int bottom) {
/* 705 */       return ActiveTextCollector.isPointInRectangle(this.localMousePos.x, this.localMousePos.y, left, top, right, bottom);
/*     */     }
/*     */     
/*     */     public void handleTag(int x0, int y0, int x1, int y1, float opacity, GuiMessageTag tag) {
/* 710 */       int indicatorColor = ARGB.color(opacity, tag.indicatorColor());
/* 711 */       this.graphics.fill(x0, y0, x1, y1, indicatorColor);
/* 712 */       if (isMouseOver(x0, y0, x1, y1))
/* 713 */         showTooltip(tag); 
/*     */     }
/*     */     
/*     */     public void handleTagIcon(int left, int bottom, boolean forceVisible, GuiMessageTag tag, GuiMessageTag.Icon icon) {
/* 719 */       int top = bottom - icon.height - 1;
/* 720 */       int right = left + icon.width;
/* 721 */       boolean isMouseOver = isMouseOver(left, top, right, bottom);
/* 722 */       if (isMouseOver)
/* 723 */         showTooltip(tag); 
/* 726 */       if (forceVisible || isMouseOver)
/* 727 */         icon.extractRenderState(this.graphics, left, top); 
/*     */     }
/*     */     
/*     */     private void showTooltip(GuiMessageTag tag) {
/* 732 */       if (tag.text() != null)
/* 733 */         this.graphics.setTooltipForNextFrame(this.font, this.font.split((FormattedText)tag.text(), 210), this.globalMouseX, this.globalMouseY); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ClickableTextOnlyGraphicsAccess implements ChatGraphicsAccess {
/*     */     private final ActiveTextCollector output;
/*     */     
/*     */     public ClickableTextOnlyGraphicsAccess(ActiveTextCollector output) {
/* 742 */       this.output = output;
/*     */     }
/*     */     
/*     */     public void updatePose(Consumer<Matrix3x2f> updater) {
/* 747 */       ActiveTextCollector.Parameters defaultParameters = this.output.defaultParameters();
/* 748 */       Matrix3x2f newPose = new Matrix3x2f(defaultParameters.pose());
/* 749 */       updater.accept(newPose);
/* 750 */       this.output.defaultParameters(defaultParameters.withPose((Matrix3x2fc)newPose));
/*     */     }
/*     */     
/*     */     public void fill(int x0, int y0, int x1, int y1, int color) {}
/*     */     
/*     */     public boolean handleMessage(int textTop, float opacity, FormattedCharSequence message) {
/* 760 */       this.output.accept(TextAlignment.LEFT, 0, textTop, message);
/* 761 */       return false;
/*     */     }
/*     */     
/*     */     public void handleTag(int x0, int y0, int x1, int y1, float opacity, GuiMessageTag tag) {}
/*     */     
/*     */     public void handleTagIcon(int left, int bottom, boolean forceVisible, GuiMessageTag tag, GuiMessageTag.Icon icon) {}
/*     */   }
/*     */   
/*     */   public enum DisplayMode {
/* 776 */     BACKGROUND(false, false),
/* 777 */     FOREGROUND(true, false),
/* 778 */     FOREGROUND_RESTRICTED(true, true);
/*     */     
/*     */     public final boolean foreground;
/*     */     
/*     */     public final boolean showRestrictedPrompt;
/*     */     
/*     */     DisplayMode(boolean foreground, boolean showRestrictedPrompt) {
/* 785 */       this.foreground = foreground;
/* 786 */       this.showRestrictedPrompt = showRestrictedPrompt;
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface LineConsumer {
/*     */     void accept(GuiMessage.Line param1Line, int param1Int, float param1Float);
/*     */   }
/*     */   
/*     */   public static interface ChatGraphicsAccess {
/*     */     void updatePose(Consumer<Matrix3x2f> param1Consumer);
/*     */     
/*     */     void fill(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5);
/*     */     
/*     */     boolean handleMessage(int param1Int, float param1Float, FormattedCharSequence param1FormattedCharSequence);
/*     */     
/*     */     void handleTag(int param1Int1, int param1Int2, int param1Int3, int param1Int4, float param1Float, GuiMessageTag param1GuiMessageTag);
/*     */     
/*     */     void handleTagIcon(int param1Int1, int param1Int2, boolean param1Boolean, GuiMessageTag param1GuiMessageTag, GuiMessageTag.Icon param1Icon);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ChatComponent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */