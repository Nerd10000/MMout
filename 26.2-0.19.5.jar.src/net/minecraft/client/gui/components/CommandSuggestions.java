/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.ParseResults;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.context.CommandContextBuilder;
/*     */ import com.mojang.brigadier.context.ParsedArgument;
/*     */ import com.mojang.brigadier.context.SuggestionContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.Suggestion;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.brigadier.tree.ArgumentCommandNode;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.ClientSuggestionProvider;
/*     */ import net.minecraft.client.renderer.Rect2i;
/*     */ import net.minecraft.commands.ArgumentVisitor;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ 
/*     */ public class CommandSuggestions {
/*  55 */   private static final Pattern WHITESPACE_PATTERN = Pattern.compile("(\\s+)");
/*     */   
/*  57 */   private static final Style UNPARSED_STYLE = Style.EMPTY.withColor(ChatFormatting.RED);
/*     */   
/*  58 */   private static final Style LITERAL_STYLE = Style.EMPTY.withColor(ChatFormatting.GRAY);
/*     */   
/*  59 */   public static final Style USAGE_FORMAT = Style.EMPTY.withColor(ChatFormatting.GRAY);
/*     */   
/*  60 */   private static final List<Style> ARGUMENT_STYLES = (List<Style>)Stream.<ChatFormatting>of(new ChatFormatting[] { ChatFormatting.AQUA, ChatFormatting.YELLOW, ChatFormatting.GREEN, ChatFormatting.LIGHT_PURPLE, ChatFormatting.GOLD }).map(Style.EMPTY::withColor).collect(ImmutableList.toImmutableList());
/*     */   
/*     */   public static final int LINE_HEIGHT = 12;
/*     */   
/*     */   public static final int USAGE_OFFSET_FROM_BOTTOM = 27;
/*     */   
/*     */   static {
/*  60 */     Objects.requireNonNull(Style.EMPTY);
/*     */   }
/*     */   
/*  64 */   private static final Component COMMANDS_NOT_ALLOWED_TEXT = (Component)Component.translatable("chat_screen.commands_not_allowed").withStyle(ChatFormatting.RED);
/*     */   
/*  65 */   private static final Component MESSAGES_NOT_ALLOWED_TEXT = (Component)Component.translatable("chat_screen.messages_not_allowed").withStyle(ChatFormatting.RED);
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final Screen screen;
/*     */   
/*     */   private final EditBox input;
/*     */   
/*     */   private final Font font;
/*     */   
/*     */   private final boolean commandsOnly;
/*     */   
/*     */   private final boolean onlyShowIfCursorPastError;
/*     */   
/*     */   private final int lineStartOffset;
/*     */   
/*     */   private final int suggestionLineLimit;
/*     */   
/*     */   private final boolean anchorToBottom;
/*     */   
/*     */   private final int fillColor;
/*     */   
/*  78 */   private final List<FormattedCharSequence> commandUsage = Lists.newArrayList();
/*     */   
/*     */   private int commandUsagePosition;
/*     */   
/*     */   private int commandUsageWidth;
/*     */   
/*     */   private ParseResults<ClientSuggestionProvider> currentParse;
/*     */   
/*     */   private CompletableFuture<Suggestions> pendingSuggestions;
/*     */   
/*     */   private SuggestionsList suggestions;
/*     */   
/*     */   private boolean currentParseIsCommand;
/*     */   
/*     */   private boolean currentParseIsMessage;
/*     */   
/*     */   private boolean allowSuggestions;
/*     */   
/*     */   private boolean keepSuggestions;
/*     */   
/*     */   private boolean allowHiding = true;
/*     */   
/*     */   private boolean messagesAllowed = true;
/*     */   
/*     */   private boolean commandsAllowed = true;
/*     */   
/*     */   public CommandSuggestions(Minecraft minecraft, Screen screen, EditBox input, Font font, boolean commandsOnly, boolean onlyShowIfCursorPastError, int lineStartOffset, int suggestionLineLimit, boolean anchorToBottom, int fillColor) {
/*  94 */     this.minecraft = minecraft;
/*  95 */     this.screen = screen;
/*  96 */     this.input = input;
/*  97 */     this.font = font;
/*  98 */     this.commandsOnly = commandsOnly;
/*  99 */     this.onlyShowIfCursorPastError = onlyShowIfCursorPastError;
/* 100 */     this.lineStartOffset = lineStartOffset;
/* 101 */     this.suggestionLineLimit = suggestionLineLimit;
/* 102 */     this.anchorToBottom = anchorToBottom;
/* 103 */     this.fillColor = fillColor;
/* 105 */     input.addFormatter(this::formatChat);
/*     */   }
/*     */   
/*     */   public void setAllowSuggestions(boolean allowSuggestions) {
/* 109 */     this.allowSuggestions = allowSuggestions;
/* 110 */     if (!allowSuggestions)
/* 111 */       this.suggestions = null; 
/*     */   }
/*     */   
/*     */   public void setAllowHiding(boolean allowHiding) {
/* 116 */     this.allowHiding = allowHiding;
/*     */   }
/*     */   
/*     */   public void setRestrictions(boolean messagesAllowed, boolean commandsAllowed) {
/* 120 */     this.messagesAllowed = messagesAllowed;
/* 121 */     this.commandsAllowed = commandsAllowed;
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 125 */     boolean isVisible = (this.suggestions != null);
/* 127 */     if (isVisible && this.suggestions.keyPressed(event))
/* 128 */       return true; 
/* 129 */     if (this.screen.getFocused() == this.input && event.isCycleFocus() && (!this.allowHiding || isVisible)) {
/* 130 */       showSuggestions(true);
/* 131 */       return true;
/*     */     } 
/* 133 */     return false;
/*     */   }
/*     */   
/*     */   public boolean mouseScrolled(double scroll) {
/* 137 */     return (this.suggestions != null && this.suggestions.mouseScrolled(Mth.clamp(scroll, -1.0D, 1.0D)));
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event) {
/* 141 */     return (this.suggestions != null && this.suggestions.mouseClicked((int)event.x(), (int)event.y()));
/*     */   }
/*     */   
/*     */   public void showSuggestions(boolean immediateNarration) {
/* 145 */     if (this.pendingSuggestions != null && this.pendingSuggestions.isDone()) {
/* 146 */       Suggestions suggestions = this.pendingSuggestions.join();
/* 147 */       if (!suggestions.isEmpty()) {
/* 148 */         int maxSuggestionWidth = 0;
/* 149 */         for (Suggestion suggestion : suggestions.getList())
/* 150 */           maxSuggestionWidth = Math.max(maxSuggestionWidth, this.font.width(suggestion.getText())); 
/* 153 */         int x = Mth.clamp(this.input.getScreenX(suggestions.getRange().getStart()), 0, this.input.getScreenX(0) + this.input.getInnerWidth() - maxSuggestionWidth);
/* 154 */         int y = this.anchorToBottom ? (this.screen.height - 12) : 72;
/* 155 */         this.suggestions = new SuggestionsList(x, y, maxSuggestionWidth, sortSuggestions(suggestions), immediateNarration);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 161 */     return (this.suggestions != null);
/*     */   }
/*     */   
/*     */   public Component getUsageNarration() {
/* 165 */     if (this.suggestions != null && this.suggestions.tabCycles) {
/* 166 */       if (this.allowHiding)
/* 167 */         return (Component)Component.translatable("narration.suggestion.usage.cycle.hidable"); 
/* 169 */       return (Component)Component.translatable("narration.suggestion.usage.cycle.fixed");
/*     */     } 
/* 172 */     if (this.allowHiding)
/* 173 */       return (Component)Component.translatable("narration.suggestion.usage.fill.hidable"); 
/* 175 */     return (Component)Component.translatable("narration.suggestion.usage.fill.fixed");
/*     */   }
/*     */   
/*     */   public void hide() {
/* 181 */     this.suggestions = null;
/*     */   }
/*     */   
/*     */   private List<Suggestion> sortSuggestions(Suggestions suggestions) {
/* 186 */     String partialCommand = this.input.getValue().substring(0, this.input.getCursorPosition());
/* 187 */     int lastWordIndex = getLastWordIndex(partialCommand);
/* 188 */     String lastWord = partialCommand.substring(lastWordIndex).toLowerCase(Locale.ROOT);
/* 190 */     List<Suggestion> suggestionList = Lists.newArrayList();
/* 191 */     List<Suggestion> partial = Lists.newArrayList();
/* 192 */     for (Suggestion suggestion : suggestions.getList()) {
/* 193 */       if (suggestion.getText().startsWith(lastWord) || suggestion.getText().startsWith("minecraft:" + lastWord)) {
/* 194 */         suggestionList.add(suggestion);
/*     */         continue;
/*     */       } 
/* 196 */       partial.add(suggestion);
/*     */     } 
/* 199 */     suggestionList.addAll(partial);
/* 200 */     return suggestionList;
/*     */   }
/*     */   
/*     */   public void updateCommandInfo() {
/* 204 */     String command = this.input.getValue();
/* 206 */     if (this.currentParse != null && !this.currentParse.getReader().getString().equals(command)) {
/* 207 */       this.currentParse = null;
/* 208 */       this.currentParseIsCommand = false;
/* 209 */       this.currentParseIsMessage = false;
/*     */     } 
/* 212 */     if (!this.keepSuggestions) {
/* 213 */       this.input.setSuggestion(null);
/* 214 */       this.suggestions = null;
/*     */     } 
/* 217 */     this.commandUsage.clear();
/* 218 */     StringReader reader = new StringReader(command);
/* 219 */     boolean startsWithSlash = (reader.canRead() && reader.peek() == '/');
/* 220 */     if (startsWithSlash)
/* 221 */       reader.skip(); 
/* 223 */     boolean isCommand = (this.commandsOnly || startsWithSlash);
/* 225 */     int cursorPosition = this.input.getCursorPosition();
/* 226 */     if (isCommand) {
/* 227 */       CommandDispatcher<ClientSuggestionProvider> commands = this.minecraft.player.connection.getCommands();
/* 229 */       if (this.currentParse == null) {
/* 230 */         this.currentParse = commands.parse(reader, this.minecraft.player.connection.getSuggestionsProvider());
/* 231 */         this.currentParseIsCommand = true;
/* 232 */         this.currentParseIsMessage = hasMessageArguments(this.currentParse);
/*     */       } 
/* 235 */       int parseStart = this.onlyShowIfCursorPastError ? reader.getCursor() : 1;
/* 237 */       if (cursorPosition >= parseStart && (this.suggestions == null || !this.keepSuggestions)) {
/* 238 */         this.pendingSuggestions = commands.getCompletionSuggestions(this.currentParse, cursorPosition);
/* 239 */         this.pendingSuggestions.thenAccept(suggestionResult -> {
/*     */               if (!this.pendingSuggestions.isDone())
/*     */                 return; 
/*     */               updateUsageInfo(this.currentParse, suggestionResult);
/*     */             });
/*     */       } 
/* 246 */     } else if (!command.isBlank()) {
/* 247 */       this.currentParseIsMessage = true;
/* 249 */       String partialCommand = command.substring(0, cursorPosition);
/* 250 */       int lastWord = getLastWordIndex(partialCommand);
/* 251 */       Collection<String> nonCommandSuggestions = this.minecraft.player.connection.getSuggestionsProvider().getCustomTabSuggestions();
/* 252 */       this.pendingSuggestions = SharedSuggestionProvider.suggest(nonCommandSuggestions, new SuggestionsBuilder(partialCommand, lastWord));
/* 254 */       if (this.currentParseIsMessage && !this.messagesAllowed)
/* 255 */         this.commandUsage.add(MESSAGES_NOT_ALLOWED_TEXT.getVisualOrderText()); 
/* 258 */       recomputeUsageBoxWidth();
/* 259 */       this.commandUsagePosition = 0;
/*     */     } else {
/* 261 */       this.pendingSuggestions = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean hasMessageArguments(ParseResults<ClientSuggestionProvider> parseResults) {
/*     */     class Visitor implements ArgumentVisitor.Output<ClientSuggestionProvider> {
/*     */       private boolean foundMessageArgument;
/*     */       
/*     */       public <T> void accept(CommandContextBuilder<ClientSuggestionProvider> context, ArgumentCommandNode<ClientSuggestionProvider, T> argument, ParsedArgument<ClientSuggestionProvider, T> value) {
/* 271 */         this.foundMessageArgument |= (value != null && value.getResult() instanceof net.minecraft.commands.arguments.MessageArgument.Message) ? 1 : 0;
/*     */       }
/*     */     };
/* 274 */     Visitor visitor = new Visitor();
/* 275 */     ArgumentVisitor.visitArguments(parseResults, visitor, false);
/* 276 */     return visitor.foundMessageArgument;
/*     */   }
/*     */   
/*     */   private static int getLastWordIndex(String text) {
/* 280 */     if (Strings.isNullOrEmpty(text))
/* 281 */       return 0; 
/* 284 */     int result = 0;
/* 286 */     Matcher matcher = WHITESPACE_PATTERN.matcher(text);
/* 287 */     while (matcher.find())
/* 288 */       result = matcher.end(); 
/* 291 */     return result;
/*     */   }
/*     */   
/*     */   private static FormattedCharSequence getExceptionMessage(CommandSyntaxException e) {
/* 295 */     Component message = ComponentUtils.fromMessage(e.getRawMessage());
/* 296 */     String context = e.getContext();
/* 297 */     if (context == null)
/* 298 */       return message.getVisualOrderText(); 
/* 300 */     return Component.translatable("command.context.parse_error", new Object[] { message, Integer.valueOf(e.getCursor()), context }).getVisualOrderText();
/*     */   }
/*     */   
/*     */   private void updateUsageInfo(ParseResults<ClientSuggestionProvider> currentParse, Suggestions suggestions) {
/* 304 */     boolean trailingCharacters = false;
/* 306 */     if (this.input.getCursorPosition() == this.input.getValue().length())
/* 307 */       if (suggestions.isEmpty() && !currentParse.getExceptions().isEmpty()) {
/* 308 */         int literals = 0;
/* 309 */         for (Map.Entry<CommandNode<ClientSuggestionProvider>, CommandSyntaxException> entry : (Iterable<Map.Entry<CommandNode<ClientSuggestionProvider>, CommandSyntaxException>>)currentParse.getExceptions().entrySet()) {
/* 310 */           CommandSyntaxException exception = entry.getValue();
/* 311 */           if (exception.getType() == CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect()) {
/* 312 */             literals++;
/*     */             continue;
/*     */           } 
/* 314 */           this.commandUsage.add(getExceptionMessage(exception));
/*     */         } 
/* 317 */         if (literals > 0)
/* 318 */           this.commandUsage.add(getExceptionMessage(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(currentParse.getReader()))); 
/* 320 */       } else if (currentParse.getReader().canRead()) {
/* 321 */         trailingCharacters = true;
/*     */       }  
/* 325 */     SuggestionContext<ClientSuggestionProvider> suggestionContextAtCursor = currentParse.getContext().findSuggestionContext(this.input.getCursorPosition());
/* 327 */     if (this.commandUsage.isEmpty()) {
/* 328 */       List<FormattedCharSequence> usageEntries = fillNodeUsage(suggestionContextAtCursor, USAGE_FORMAT);
/* 329 */       if (usageEntries.isEmpty())
/* 331 */         if (trailingCharacters)
/* 332 */           this.commandUsage.add(getExceptionMessage(Commands.getParseException(currentParse)));  
/* 335 */       this.commandUsage.addAll(usageEntries);
/*     */     } 
/* 338 */     if (this.currentParseIsCommand && !this.commandsAllowed)
/* 339 */       this.commandUsage.add(COMMANDS_NOT_ALLOWED_TEXT.getVisualOrderText()); 
/* 342 */     if (this.currentParseIsMessage && !this.messagesAllowed)
/* 343 */       this.commandUsage.add(MESSAGES_NOT_ALLOWED_TEXT.getVisualOrderText()); 
/* 346 */     recomputeUsageBoxWidth();
/* 347 */     if (!this.commandUsage.isEmpty()) {
/* 348 */       this.commandUsagePosition = Mth.clamp(this.input.getScreenX(suggestionContextAtCursor.startPos), 0, this.input.getScreenX(0) + this.input.getInnerWidth() - this.commandUsageWidth);
/*     */     } else {
/* 350 */       this.commandUsagePosition = 0;
/*     */     } 
/* 353 */     this.suggestions = null;
/* 354 */     if (this.allowSuggestions && ((Boolean)this.minecraft.options.autoSuggestions().get()).booleanValue())
/* 355 */       showSuggestions(false); 
/*     */   }
/*     */   
/*     */   private List<FormattedCharSequence> fillNodeUsage(SuggestionContext<ClientSuggestionProvider> suggestionContext, Style usageFormat) {
/* 360 */     Map<CommandNode<ClientSuggestionProvider>, String> usage = this.minecraft.player.connection.getCommands().getSmartUsage(suggestionContext.parent, this.minecraft.player.connection.getSuggestionsProvider());
/* 361 */     List<FormattedCharSequence> lines = new ArrayList<>();
/* 363 */     for (Map.Entry<CommandNode<ClientSuggestionProvider>, String> entry : usage.entrySet()) {
/* 364 */       if (!(entry.getKey() instanceof com.mojang.brigadier.tree.LiteralCommandNode))
/* 365 */         lines.add(FormattedCharSequence.forward(entry.getValue(), usageFormat)); 
/*     */     } 
/* 369 */     return lines;
/*     */   }
/*     */   
/*     */   private void recomputeUsageBoxWidth() {
/* 373 */     int longest = 0;
/* 374 */     for (FormattedCharSequence entry : this.commandUsage)
/* 375 */       longest = Math.max(longest, this.font.width(entry)); 
/* 377 */     this.commandUsageWidth = longest;
/*     */   }
/*     */   
/*     */   private FormattedCharSequence formatChat(String text, int offset) {
/* 381 */     if (this.currentParse != null)
/* 382 */       return formatText(this.currentParse, text, offset); 
/* 384 */     return null;
/*     */   }
/*     */   
/*     */   private static String calculateSuggestionSuffix(String contents, String suggestion) {
/* 389 */     if (suggestion.startsWith(contents))
/* 390 */       return suggestion.substring(contents.length()); 
/* 393 */     return null;
/*     */   }
/*     */   
/*     */   private static FormattedCharSequence formatText(ParseResults<ClientSuggestionProvider> currentParse, String text, int offset) {
/* 398 */     List<FormattedCharSequence> parts = Lists.newArrayList();
/* 399 */     int unformattedStart = 0;
/* 400 */     int nextColor = -1;
/* 402 */     CommandContextBuilder<ClientSuggestionProvider> context = currentParse.getContext().getLastChild();
/* 403 */     for (ParsedArgument<ClientSuggestionProvider, ?> argument : (Iterable<ParsedArgument<ClientSuggestionProvider, ?>>)context.getArguments().values()) {
/* 404 */       if (++nextColor >= ARGUMENT_STYLES.size())
/* 405 */         nextColor = 0; 
/* 408 */       int start = Math.max(argument.getRange().getStart() - offset, 0);
/* 409 */       if (start >= text.length())
/*     */         break; 
/* 412 */       int end = Math.min(argument.getRange().getEnd() - offset, text.length());
/* 413 */       if (end <= 0)
/*     */         continue; 
/* 416 */       parts.add(FormattedCharSequence.forward(text.substring(unformattedStart, start), LITERAL_STYLE));
/* 417 */       parts.add(FormattedCharSequence.forward(text.substring(start, end), ARGUMENT_STYLES.get(nextColor)));
/* 418 */       unformattedStart = end;
/*     */     } 
/* 420 */     if (currentParse.getReader().canRead()) {
/* 421 */       int start = Math.max(currentParse.getReader().getCursor() - offset, 0);
/* 422 */       if (start < text.length()) {
/* 423 */         int end = Math.min(start + currentParse.getReader().getRemainingLength(), text.length());
/* 424 */         parts.add(FormattedCharSequence.forward(text.substring(unformattedStart, start), LITERAL_STYLE));
/* 425 */         parts.add(FormattedCharSequence.forward(text.substring(start, end), UNPARSED_STYLE));
/* 426 */         unformattedStart = end;
/*     */       } 
/*     */     } 
/* 429 */     parts.add(FormattedCharSequence.forward(text.substring(unformattedStart), LITERAL_STYLE));
/* 430 */     return FormattedCharSequence.composite(parts);
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
/* 434 */     if (!extractSuggestions(graphics, mouseX, mouseY))
/* 435 */       extractUsage(graphics); 
/*     */   }
/*     */   
/*     */   public boolean extractSuggestions(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
/* 440 */     if (this.suggestions != null) {
/* 441 */       this.suggestions.extractRenderState(graphics, mouseX, mouseY);
/* 442 */       return true;
/*     */     } 
/* 444 */     return false;
/*     */   }
/*     */   
/*     */   public void extractUsage(GuiGraphicsExtractor graphics) {
/* 448 */     int y = 0;
/* 449 */     for (FormattedCharSequence line : this.commandUsage) {
/* 450 */       int lineY = this.anchorToBottom ? (this.screen.height - 27 - 12 * y) : (72 + 12 * y);
/* 451 */       graphics.fill(this.commandUsagePosition - 1, lineY, this.commandUsagePosition + this.commandUsageWidth + 1, lineY + 12, this.fillColor);
/* 452 */       graphics.text(this.font, line, this.commandUsagePosition, lineY + 2, -1);
/* 453 */       y++;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Component getNarrationMessage() {
/* 458 */     if (this.suggestions != null)
/* 459 */       return (Component)CommonComponents.NEW_LINE.copy().append(this.suggestions.getNarrationMessage()); 
/* 461 */     return CommonComponents.EMPTY;
/*     */   }
/*     */   
/*     */   public boolean hasAllowedInput() {
/* 465 */     if (this.currentParseIsMessage && !this.messagesAllowed)
/* 466 */       return false; 
/* 469 */     if (this.currentParseIsCommand && !this.commandsAllowed)
/* 470 */       return false; 
/* 473 */     return true;
/*     */   }
/*     */   
/*     */   public class SuggestionsList {
/*     */     private final Rect2i rect;
/*     */     
/*     */     private final String originalContents;
/*     */     
/*     */     private final List<Suggestion> suggestionList;
/*     */     
/*     */     private int offset;
/*     */     
/*     */     private int current;
/*     */     
/* 482 */     private Vec2 lastMouse = Vec2.ZERO;
/*     */     
/*     */     private boolean tabCycles;
/*     */     
/*     */     private int lastNarratedEntry;
/*     */     
/*     */     private SuggestionsList(int x, int y, int width, List<Suggestion> suggestionList, boolean immediateNarration) {
/* 487 */       int listX = x - (CommandSuggestions.this.input.isBordered() ? 0 : 1);
/* 488 */       int listY = CommandSuggestions.this.anchorToBottom ? (y - 3 - Math.min(suggestionList.size(), CommandSuggestions.this.suggestionLineLimit) * 12) : (y - (CommandSuggestions.this.input.isBordered() ? 1 : 0));
/* 489 */       this.rect = new Rect2i(listX, listY, width + 1, Math.min(suggestionList.size(), CommandSuggestions.this.suggestionLineLimit) * 12);
/* 490 */       this.originalContents = CommandSuggestions.this.input.getValue();
/* 491 */       this.lastNarratedEntry = immediateNarration ? -1 : 0;
/* 492 */       this.suggestionList = suggestionList;
/* 493 */       select(0);
/*     */     }
/*     */     
/*     */     public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
/* 497 */       int limit = Math.min(this.suggestionList.size(), CommandSuggestions.this.suggestionLineLimit);
/* 498 */       int unselectedColor = -5592406;
/* 499 */       boolean hasPrevious = (this.offset > 0);
/* 500 */       boolean hasNext = (this.suggestionList.size() > this.offset + limit);
/* 501 */       boolean limited = (hasPrevious || hasNext);
/* 502 */       boolean mouseMoved = (this.lastMouse.x != mouseX || this.lastMouse.y != mouseY);
/* 504 */       if (mouseMoved)
/* 505 */         this.lastMouse = new Vec2(mouseX, mouseY); 
/* 508 */       if (limited) {
/* 509 */         graphics.fill(this.rect.getX(), this.rect.getY() - 1, this.rect.getX() + this.rect.getWidth(), this.rect.getY(), CommandSuggestions.this.fillColor);
/* 510 */         graphics.fill(this.rect.getX(), this.rect.getY() + this.rect.getHeight(), this.rect.getX() + this.rect.getWidth(), this.rect.getY() + this.rect.getHeight() + 1, CommandSuggestions.this.fillColor);
/* 511 */         if (hasPrevious)
/* 512 */           for (int x = 0; x < this.rect.getWidth(); x++) {
/* 513 */             if (x % 2 == 0)
/* 514 */               graphics.fill(this.rect.getX() + x, this.rect.getY() - 1, this.rect.getX() + x + 1, this.rect.getY(), -1); 
/*     */           }  
/* 518 */         if (hasNext)
/* 519 */           for (int x = 0; x < this.rect.getWidth(); x++) {
/* 520 */             if (x % 2 == 0)
/* 521 */               graphics.fill(this.rect.getX() + x, this.rect.getY() + this.rect.getHeight(), this.rect.getX() + x + 1, this.rect.getY() + this.rect.getHeight() + 1, -1); 
/*     */           }  
/*     */       } 
/* 527 */       boolean hovered = false;
/* 528 */       for (int i = 0; i < limit; i++) {
/* 529 */         Suggestion suggestion = this.suggestionList.get(i + this.offset);
/* 530 */         graphics.fill(this.rect.getX(), this.rect.getY() + 12 * i, this.rect.getX() + this.rect.getWidth(), this.rect.getY() + 12 * i + 12, CommandSuggestions.this.fillColor);
/* 531 */         if (mouseX > this.rect.getX() && mouseX < this.rect.getX() + this.rect.getWidth() && mouseY > this.rect.getY() + 12 * i && mouseY < this.rect.getY() + 12 * i + 12) {
/* 532 */           if (mouseMoved)
/* 533 */             select(i + this.offset); 
/* 535 */           hovered = true;
/*     */         } 
/* 537 */         graphics.text(CommandSuggestions.this.font, suggestion.getText(), this.rect.getX() + 1, this.rect.getY() + 2 + 12 * i, (i + this.offset == this.current) ? -256 : -5592406);
/*     */       } 
/* 540 */       if (hovered) {
/* 541 */         Message tooltip = ((Suggestion)this.suggestionList.get(this.current)).getTooltip();
/* 542 */         if (tooltip != null)
/* 543 */           graphics.setTooltipForNextFrame(CommandSuggestions.this.font, ComponentUtils.fromMessage(tooltip), mouseX, mouseY); 
/*     */       } 
/* 547 */       if (this.rect.contains(mouseX, mouseY))
/* 548 */         graphics.requestCursor(CursorTypes.POINTING_HAND); 
/*     */     }
/*     */     
/*     */     public boolean mouseClicked(int x, int y) {
/* 553 */       if (!this.rect.contains(x, y))
/* 554 */         return false; 
/* 557 */       int line = (y - this.rect.getY()) / 12 + this.offset;
/* 558 */       if (line >= 0 && line < this.suggestionList.size()) {
/* 559 */         select(line);
/* 560 */         useSuggestion();
/*     */       } 
/* 563 */       return true;
/*     */     }
/*     */     
/*     */     public boolean mouseScrolled(double scroll) {
/* 567 */       int mouseX = (int)CommandSuggestions.this.minecraft.mouseHandler.getScaledXPos(CommandSuggestions.this.minecraft.getWindow());
/* 568 */       int mouseY = (int)CommandSuggestions.this.minecraft.mouseHandler.getScaledYPos(CommandSuggestions.this.minecraft.getWindow());
/* 570 */       if (this.rect.contains(mouseX, mouseY)) {
/* 571 */         this.offset = Mth.clamp((int)(this.offset - scroll), 0, Math.max(this.suggestionList.size() - CommandSuggestions.this.suggestionLineLimit, 0));
/* 572 */         return true;
/*     */       } 
/* 575 */       return false;
/*     */     }
/*     */     
/*     */     public boolean keyPressed(KeyEvent event) {
/* 579 */       if (event.isUp()) {
/* 580 */         cycle(-1);
/* 581 */         this.tabCycles = false;
/* 582 */         return true;
/*     */       } 
/* 583 */       if (event.isDown()) {
/* 584 */         cycle(1);
/* 585 */         this.tabCycles = false;
/* 586 */         return true;
/*     */       } 
/* 587 */       if (event.isCycleFocus()) {
/* 588 */         if (this.tabCycles)
/* 589 */           cycle(event.hasShiftDown() ? -1 : 1); 
/* 591 */         useSuggestion();
/* 592 */         return true;
/*     */       } 
/* 593 */       if (event.isEscape()) {
/* 594 */         CommandSuggestions.this.hide();
/* 595 */         CommandSuggestions.this.input.setSuggestion(null);
/* 597 */         return true;
/*     */       } 
/* 600 */       return false;
/*     */     }
/*     */     
/*     */     public void cycle(int direction) {
/* 604 */       select(this.current + direction);
/* 605 */       int first = this.offset;
/* 606 */       int last = this.offset + CommandSuggestions.this.suggestionLineLimit - 1;
/* 607 */       if (this.current < first) {
/* 608 */         this.offset = Mth.clamp(this.current, 0, Math.max(this.suggestionList.size() - CommandSuggestions.this.suggestionLineLimit, 0));
/* 609 */       } else if (this.current > last) {
/* 610 */         this.offset = Mth.clamp(this.current + CommandSuggestions.this.lineStartOffset - CommandSuggestions.this.suggestionLineLimit, 0, Math.max(this.suggestionList.size() - CommandSuggestions.this.suggestionLineLimit, 0));
/*     */       } 
/*     */     }
/*     */     
/*     */     public void select(int index) {
/* 615 */       this.current = index;
/* 617 */       if (this.current < 0)
/* 618 */         this.current += this.suggestionList.size(); 
/* 620 */       if (this.current >= this.suggestionList.size())
/* 621 */         this.current -= this.suggestionList.size(); 
/* 624 */       Suggestion suggestion = this.suggestionList.get(this.current);
/* 625 */       CommandSuggestions.this.input.setSuggestion(CommandSuggestions.calculateSuggestionSuffix(CommandSuggestions.this.input.getValue(), suggestion.apply(this.originalContents)));
/* 627 */       if (this.lastNarratedEntry != this.current)
/* 628 */         CommandSuggestions.this.minecraft.getNarrator().saySystemNow(getNarrationMessage()); 
/*     */     }
/*     */     
/*     */     public void useSuggestion() {
/* 633 */       Suggestion suggestion = this.suggestionList.get(this.current);
/* 634 */       CommandSuggestions.this.keepSuggestions = true;
/* 635 */       CommandSuggestions.this.input.setValue(suggestion.apply(this.originalContents));
/* 636 */       int end = suggestion.getRange().getStart() + suggestion.getText().length();
/* 637 */       CommandSuggestions.this.input.setCursorPosition(end);
/* 638 */       CommandSuggestions.this.input.setHighlightPos(end);
/* 639 */       select(this.current);
/* 640 */       CommandSuggestions.this.keepSuggestions = false;
/* 641 */       this.tabCycles = true;
/*     */     }
/*     */     
/*     */     private Component getNarrationMessage() {
/* 645 */       this.lastNarratedEntry = this.current;
/* 646 */       Suggestion suggestion = this.suggestionList.get(this.current);
/* 647 */       Message tooltip = suggestion.getTooltip();
/* 648 */       if (tooltip != null)
/* 649 */         return (Component)Component.translatable("narration.suggestion.tooltip", new Object[] { Integer.valueOf(this.current + 1), Integer.valueOf(this.suggestionList.size()), suggestion.getText(), Component.translationArg(tooltip) }); 
/* 651 */       return (Component)Component.translatable("narration.suggestion", new Object[] { Integer.valueOf(this.current + 1), Integer.valueOf(this.suggestionList.size()), suggestion.getText() });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\CommandSuggestions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */