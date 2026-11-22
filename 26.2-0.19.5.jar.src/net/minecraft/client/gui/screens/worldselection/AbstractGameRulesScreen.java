/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ import net.minecraft.world.level.gamerules.GameRuleCategory;
/*     */ import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public abstract class AbstractGameRulesScreen extends Screen {
/*  45 */   protected static final Component TITLE = (Component)Component.translatable("editGamerule.title");
/*     */   
/*  47 */   private static final Component SEARCH_HINT = (Component)Component.translatable("gui.game_rule.search").withStyle(EditBox.SEARCH_HINT_STYLE);
/*     */   
/*     */   private static final int SEARCH_BOX_HEIGHT = 15;
/*     */   
/*  50 */   private final Set<RuleEntry> invalidEntries = Sets.newHashSet();
/*     */   
/*     */   private final Consumer<Optional<GameRules>> exitCallback;
/*     */   
/*     */   protected final HeaderAndFooterLayout layout;
/*     */   
/*     */   protected final GameRules gameRules;
/*     */   
/*     */   protected EditBox searchBox;
/*     */   
/*     */   protected RuleList ruleList;
/*     */   
/*     */   protected Button doneButton;
/*     */   
/*     */   public AbstractGameRulesScreen(GameRules gameRules, Consumer<Optional<GameRules>> exitCallback) {
/*  61 */     super(TITLE);
/*  62 */     this.gameRules = gameRules;
/*  63 */     this.exitCallback = exitCallback;
/*  64 */     Objects.requireNonNull(this.font);
/*  64 */     this.layout = new HeaderAndFooterLayout(this, (int)(12.0D + 9.0D + 15.0D), 33);
/*     */   }
/*     */   
/*     */   protected void createAndConfigureSearchBox(LinearLayout headerLayout) {
/*  68 */     this.searchBox = (EditBox)headerLayout.addChild((LayoutElement)new EditBox(this.font, 200, 15, (Component)Component.empty()));
/*  69 */     this.searchBox.setHint(SEARCH_HINT);
/*  70 */     this.searchBox.setResponder(this::filterGameRules);
/*     */   }
/*     */   
/*     */   protected void init() {
/*  75 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical().spacing(4));
/*  76 */     header.defaultCellSetting().alignHorizontallyCenter();
/*  77 */     header.addChild((LayoutElement)new StringWidget(TITLE, this.font));
/*  78 */     createAndConfigureSearchBox(header);
/*  80 */     initContent();
/*  82 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/*  83 */     this.doneButton = (Button)footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onDone()).build());
/*  84 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, button -> onClose()).build());
/*  85 */     AbstractGameRulesScreen abstractGameRulesScreen = this;
/*  85 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/*  86 */     repositionElements();
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/*  95 */     this.layout.arrangeElements();
/*  96 */     if (this.ruleList != null)
/*  97 */       this.ruleList.updateSize(this.width, this.layout); 
/*     */   }
/*     */   
/*     */   protected void setInitialFocus() {
/* 103 */     if (this.searchBox != null)
/* 104 */       setInitialFocus((GuiEventListener)this.searchBox); 
/*     */   }
/*     */   
/*     */   private void markInvalid(RuleEntry invalidEntry) {
/* 109 */     this.invalidEntries.add(invalidEntry);
/* 110 */     updateDoneButton();
/*     */   }
/*     */   
/*     */   private void clearInvalid(RuleEntry invalidEntry) {
/* 114 */     this.invalidEntries.remove(invalidEntry);
/* 115 */     updateDoneButton();
/*     */   }
/*     */   
/*     */   private void updateDoneButton() {
/* 119 */     if (this.doneButton != null)
/* 120 */       this.doneButton.active = this.invalidEntries.isEmpty(); 
/*     */   }
/*     */   
/*     */   protected void closeAndDiscardChanges() {
/* 125 */     this.exitCallback.accept(Optional.empty());
/*     */   }
/*     */   
/*     */   protected void closeAndApplyChanges() {
/* 129 */     this.exitCallback.accept(Optional.of(this.gameRules));
/*     */   }
/*     */   
/*     */   protected void filterGameRules(String filter) {
/* 133 */     if (this.ruleList != null) {
/* 134 */       this.ruleList.populateChildren(filter);
/* 135 */       this.ruleList.setScrollAmount(0.0D);
/* 136 */       repositionElements();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract void initContent();
/*     */   
/*     */   protected abstract void onDone();
/*     */   
/*     */   public static abstract class RuleEntry extends ContainerObjectSelectionList.Entry<RuleEntry> {
/*     */     private final List<FormattedCharSequence> tooltip;
/*     */     
/*     */     public RuleEntry(List<FormattedCharSequence> tooltip) {
/* 144 */       this.tooltip = tooltip;
/*     */     }
/*     */   }
/*     */   
/*     */   public class CategoryRuleEntry extends RuleEntry {
/*     */     private final Component label;
/*     */     
/*     */     public CategoryRuleEntry(Component label) {
/* 152 */       super((List<FormattedCharSequence>)null);
/* 153 */       this.label = label;
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 158 */       graphics.centeredText(AbstractGameRulesScreen.this.minecraft.font, this.label, getContentXMiddle(), getContentY() + 5, -1);
/*     */     }
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 163 */       return (List<? extends GuiEventListener>)ImmutableList.of();
/*     */     }
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/* 168 */       return (List<? extends NarratableEntry>)ImmutableList.of(new NarratableEntry() {
/*     */             public NarratableEntry.NarrationPriority narrationPriority() {
/* 171 */               return NarratableEntry.NarrationPriority.HOVERED;
/*     */             }
/*     */             
/*     */             public void updateNarration(NarrationElementOutput output) {
/* 176 */               output.add(NarratedElementType.TITLE, AbstractGameRulesScreen.CategoryRuleEntry.this.label);
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements NarratableEntry {
/*     */     public NarratableEntry.NarrationPriority narrationPriority() {
/*     */       return NarratableEntry.NarrationPriority.HOVERED;
/*     */     }
/*     */     
/*     */     public void updateNarration(NarrationElementOutput output) {
/* 176 */       output.add(NarratedElementType.TITLE, AbstractGameRulesScreen.CategoryRuleEntry.this.label);
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract class GameRuleEntry extends RuleEntry {
/*     */     private final List<FormattedCharSequence> label;
/*     */     
/* 189 */     protected final List<AbstractWidget> children = Lists.newArrayList();
/*     */     
/*     */     public GameRuleEntry(List<FormattedCharSequence> tooltip, Component label) {
/* 192 */       super(tooltip);
/* 193 */       this.label = AbstractGameRulesScreen.this.minecraft.font.split((FormattedText)label, 170);
/*     */     }
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 198 */       return (List)this.children;
/*     */     }
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/* 203 */       return (List)this.children;
/*     */     }
/*     */     
/*     */     protected void extractLabel(GuiGraphicsExtractor graphics, int rowTop, int rowLeft) {
/* 207 */       if (this.label.size() == 1) {
/* 208 */         graphics.text(AbstractGameRulesScreen.this.minecraft.font, this.label.get(0), rowLeft, rowTop + 5, -1);
/* 209 */       } else if (this.label.size() >= 2) {
/* 210 */         graphics.text(AbstractGameRulesScreen.this.minecraft.font, this.label.get(0), rowLeft, rowTop, -1);
/* 211 */         graphics.text(AbstractGameRulesScreen.this.minecraft.font, this.label.get(1), rowLeft, rowTop + 10, -1);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public class BooleanRuleEntry extends GameRuleEntry {
/*     */     private final CycleButton<Boolean> checkbox;
/*     */     
/*     */     public BooleanRuleEntry(AbstractGameRulesScreen this$0, Component name, List<FormattedCharSequence> tooltip, String narration, GameRule<Boolean> gameRule) {
/* 220 */       super(this$0, tooltip, name);
/* 221 */       this
/*     */         
/* 224 */         .checkbox = CycleButton.onOffBuilder(((Boolean)this$0.gameRules.get(gameRule)).booleanValue()).displayOnlyValue().withCustomNarration(button -> button.createDefaultNarrationMessage().append("\n").append(narration)).create(10, 5, 44, 20, name, (button, newValue) -> this$0.gameRules.set(gameRule, newValue, null));
/* 225 */       this.children.add(this.checkbox);
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 230 */       extractLabel(graphics, getContentY(), getContentX());
/* 231 */       this.checkbox.setX(getContentRight() - 45);
/* 232 */       this.checkbox.setY(getContentY());
/* 233 */       this.checkbox.extractRenderState(graphics, mouseX, mouseY, a);
/*     */     }
/*     */   }
/*     */   
/*     */   public class IntegerRuleEntry extends GameRuleEntry {
/*     */     private final EditBox input;
/*     */     
/*     */     public IntegerRuleEntry(AbstractGameRulesScreen this$0, Component label, List<FormattedCharSequence> tooltip, String narration, GameRule<Integer> gameRule) {
/* 241 */       super(this$0, tooltip, label);
/* 243 */       this.input = new EditBox(this$0.minecraft.font, 10, 5, 44, 20, (Component)label.copy().append("\n").append(narration).append("\n"));
/* 244 */       this.input.setValue(this$0.gameRules.getAsString(gameRule));
/* 245 */       this.input.setResponder(v -> {
/*     */             DataResult<Integer> value = gameRule.deserialize(v);
/*     */             if (value.isSuccess()) {
/*     */               this.input.setTextColor(-2039584);
/*     */               this$0.clearInvalid(this);
/*     */               this$0.gameRules.set(gameRule, value.getOrThrow(), null);
/*     */             } else {
/*     */               this.input.setTextColor(-65536);
/*     */               this$0.markInvalid(this);
/*     */             } 
/*     */           });
/* 256 */       this.children.add(this.input);
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 261 */       extractLabel(graphics, getContentY(), getContentX());
/* 262 */       this.input.setX(getContentRight() - 45);
/* 263 */       this.input.setY(getContentY());
/* 264 */       this.input.extractRenderState(graphics, mouseX, mouseY, a);
/*     */     }
/*     */   }
/*     */   
/*     */   public class RuleList extends ContainerObjectSelectionList<RuleEntry> {
/*     */     private static final int ITEM_HEIGHT = 24;
/*     */     
/*     */     private final GameRules gameRules;
/*     */     
/*     */     public RuleList(GameRules gameRules) {
/* 274 */       super(Minecraft.getInstance(), AbstractGameRulesScreen.this.width, AbstractGameRulesScreen.this.layout.getContentHeight(), AbstractGameRulesScreen.this.layout.getHeaderHeight(), 24);
/* 275 */       this.gameRules = gameRules;
/* 277 */       populateChildren("");
/*     */     }
/*     */     
/*     */     private void populateChildren(String filter) {
/* 281 */       clearEntries();
/* 282 */       final Map<GameRuleCategory, Map<GameRule<?>, AbstractGameRulesScreen.RuleEntry>> entries = Maps.newHashMap();
/* 283 */       final String lowerCaseFilter = filter.toLowerCase(Locale.ROOT);
/* 285 */       this.gameRules.visitGameRuleTypes(new GameRuleTypeVisitor() {
/*     */             public void visitBoolean(GameRule<Boolean> gameRule) {
/* 288 */               addEntry(gameRule, (x$0, x$1, x$2, x$3) -> new AbstractGameRulesScreen.BooleanRuleEntry(AbstractGameRulesScreen.this, x$0, x$1, x$2, x$3));
/*     */             }
/*     */             
/*     */             public void visitInteger(GameRule<Integer> gameRule) {
/* 293 */               addEntry(gameRule, (x$0, x$1, x$2, x$3) -> new AbstractGameRulesScreen.IntegerRuleEntry(AbstractGameRulesScreen.this, x$0, x$1, x$2, x$3));
/*     */             }
/*     */             
/*     */             private <T> void addEntry(GameRule<T> gameRule, AbstractGameRulesScreen.EntryFactory<T> factory) {
/*     */               ImmutableList immutableList;
/*     */               String narration;
/* 297 */               MutableComponent mutableComponent1 = Component.translatable(gameRule.getDescriptionId());
/* 298 */               String descriptionKey = gameRule.getDescriptionId() + ".description";
/* 299 */               Optional<MutableComponent> optionalDescription = Optional.<MutableComponent>of(Component.translatable(descriptionKey)).filter(ComponentUtils::isTranslationResolvable);
/* 300 */               if (!AbstractGameRulesScreen.RuleList.matchesFilter(gameRule.id(), mutableComponent1.getString(), gameRule.category().label().getString(), optionalDescription, lowerCaseFilter))
/*     */                 return; 
/* 304 */               MutableComponent mutableComponent2 = Component.literal(gameRule.id()).withStyle(ChatFormatting.YELLOW);
/* 305 */               MutableComponent mutableComponent3 = Component.translatable("editGamerule.default", new Object[] { Component.literal(gameRule.serialize(gameRule.defaultValue())) }).withStyle(ChatFormatting.GRAY);
/* 309 */               if (optionalDescription.isPresent()) {
/* 310 */                 ImmutableList.Builder<FormattedCharSequence> result = ImmutableList.builder().add(mutableComponent2.getVisualOrderText());
/* 311 */                 Objects.requireNonNull(result);
/* 311 */                 AbstractGameRulesScreen.this.font.split((FormattedText)optionalDescription.get(), 150).forEach(result::add);
/* 312 */                 immutableList = result.add(mutableComponent3.getVisualOrderText()).build();
/* 313 */                 narration = ((MutableComponent)optionalDescription.get()).getString() + "\n" + ((MutableComponent)optionalDescription.get()).getString();
/*     */               } else {
/* 315 */                 immutableList = ImmutableList.of(mutableComponent2.getVisualOrderText(), mutableComponent3.getVisualOrderText());
/* 316 */                 narration = mutableComponent3.getString();
/*     */               } 
/* 319 */               ((Map<GameRule<T>, AbstractGameRulesScreen.RuleEntry>)entries.computeIfAbsent(gameRule.category(), k -> Maps.newHashMap())).put(gameRule, factory.create((Component)mutableComponent1, (List<FormattedCharSequence>)immutableList, narration, gameRule));
/*     */             }
/*     */           });
/* 323 */       entries.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.comparing(GameRuleCategory::getDescriptionId))).forEach(e -> {
/*     */             addEntry((AbstractSelectionList.Entry)new AbstractGameRulesScreen.CategoryRuleEntry((Component)((GameRuleCategory)e.getKey()).label().withStyle(new ChatFormatting[] { ChatFormatting.BOLD, ChatFormatting.YELLOW })));
/*     */             ((Map)e.getValue()).entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.comparing(GameRule::getDescriptionId))).forEach(());
/*     */           });
/*     */     }
/*     */     
/*     */     private static boolean matchesFilter(String gameRuleId, String readableName, String categoryName, Optional<MutableComponent> optionalDescription, String lowerCaseFilter) {
/* 330 */       return (toLowerCaseMatchesFilter(gameRuleId, lowerCaseFilter) || 
/* 331 */         toLowerCaseMatchesFilter(readableName, lowerCaseFilter) || 
/* 332 */         toLowerCaseMatchesFilter(categoryName, lowerCaseFilter) || ((Boolean)optionalDescription
/* 333 */         .<Boolean>map(description -> Boolean.valueOf(toLowerCaseMatchesFilter(description.getString(), lowerCaseFilter))).orElse(Boolean.valueOf(false))).booleanValue());
/*     */     }
/*     */     
/*     */     private static boolean toLowerCaseMatchesFilter(String gameRuleId, String lowerCaseFilter) {
/* 337 */       return gameRuleId.toLowerCase(Locale.ROOT).contains(lowerCaseFilter);
/*     */     }
/*     */     
/*     */     public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 342 */       super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
/* 343 */       AbstractGameRulesScreen.RuleEntry hovered = (AbstractGameRulesScreen.RuleEntry)getHovered();
/* 344 */       if (hovered != null && hovered.tooltip != null)
/* 345 */         graphics.setTooltipForNextFrame(hovered.tooltip, mouseX, mouseY); 
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements GameRuleTypeVisitor {
/*     */     public void visitBoolean(GameRule<Boolean> gameRule) {
/*     */       addEntry(gameRule, (x$0, x$1, x$2, x$3) -> new AbstractGameRulesScreen.BooleanRuleEntry(AbstractGameRulesScreen.this, x$0, x$1, x$2, x$3));
/*     */     }
/*     */     
/*     */     public void visitInteger(GameRule<Integer> gameRule) {
/*     */       addEntry(gameRule, (x$0, x$1, x$2, x$3) -> new AbstractGameRulesScreen.IntegerRuleEntry(AbstractGameRulesScreen.this, x$0, x$1, x$2, x$3));
/*     */     }
/*     */     
/*     */     private <T> void addEntry(GameRule<T> gameRule, AbstractGameRulesScreen.EntryFactory<T> factory) {
/*     */       ImmutableList immutableList;
/*     */       String narration;
/*     */       MutableComponent mutableComponent1 = Component.translatable(gameRule.getDescriptionId());
/*     */       String descriptionKey = gameRule.getDescriptionId() + ".description";
/*     */       Optional<MutableComponent> optionalDescription = Optional.<MutableComponent>of(Component.translatable(descriptionKey)).filter(ComponentUtils::isTranslationResolvable);
/*     */       if (!AbstractGameRulesScreen.RuleList.matchesFilter(gameRule.id(), mutableComponent1.getString(), gameRule.category().label().getString(), optionalDescription, lowerCaseFilter))
/*     */         return; 
/*     */       MutableComponent mutableComponent2 = Component.literal(gameRule.id()).withStyle(ChatFormatting.YELLOW);
/*     */       MutableComponent mutableComponent3 = Component.translatable("editGamerule.default", new Object[] { Component.literal(gameRule.serialize(gameRule.defaultValue())) }).withStyle(ChatFormatting.GRAY);
/*     */       if (optionalDescription.isPresent()) {
/*     */         ImmutableList.Builder<FormattedCharSequence> result = ImmutableList.builder().add(mutableComponent2.getVisualOrderText());
/*     */         Objects.requireNonNull(result);
/*     */         AbstractGameRulesScreen.this.font.split((FormattedText)optionalDescription.get(), 150).forEach(result::add);
/*     */         immutableList = result.add(mutableComponent3.getVisualOrderText()).build();
/*     */         narration = ((MutableComponent)optionalDescription.get()).getString() + "\n" + ((MutableComponent)optionalDescription.get()).getString();
/*     */       } else {
/*     */         immutableList = ImmutableList.of(mutableComponent2.getVisualOrderText(), mutableComponent3.getVisualOrderText());
/*     */         narration = mutableComponent3.getString();
/*     */       } 
/*     */       ((Map<GameRule<T>, AbstractGameRulesScreen.RuleEntry>)entries.computeIfAbsent(gameRule.category(), k -> Maps.newHashMap())).put(gameRule, factory.create((Component)mutableComponent1, (List<FormattedCharSequence>)immutableList, narration, gameRule));
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface EntryFactory<T> {
/*     */     AbstractGameRulesScreen.RuleEntry create(Component param1Component, List<FormattedCharSequence> param1List, String param1String, GameRule<T> param1GameRule);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\AbstractGameRulesScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */