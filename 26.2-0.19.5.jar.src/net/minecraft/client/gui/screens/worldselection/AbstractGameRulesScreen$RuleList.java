/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
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
/*     */ public class RuleList extends ContainerObjectSelectionList<AbstractGameRulesScreen.RuleEntry> {
/*     */   private static final int ITEM_HEIGHT = 24;
/*     */   
/*     */   private final GameRules gameRules;
/*     */   
/*     */   public RuleList(GameRules gameRules) {
/* 274 */     super(Minecraft.getInstance(), AbstractGameRulesScreen.this.width, AbstractGameRulesScreen.this.layout.getContentHeight(), AbstractGameRulesScreen.this.layout.getHeaderHeight(), 24);
/* 275 */     this.gameRules = gameRules;
/* 277 */     populateChildren("");
/*     */   }
/*     */   
/*     */   private void populateChildren(String filter) {
/* 281 */     clearEntries();
/* 282 */     final Map<GameRuleCategory, Map<GameRule<?>, AbstractGameRulesScreen.RuleEntry>> entries = Maps.newHashMap();
/* 283 */     final String lowerCaseFilter = filter.toLowerCase(Locale.ROOT);
/* 285 */     this.gameRules.visitGameRuleTypes(new GameRuleTypeVisitor() {
/*     */           public void visitBoolean(GameRule<Boolean> gameRule) {
/* 288 */             addEntry(gameRule, (x$0, x$1, x$2, x$3) -> new AbstractGameRulesScreen.BooleanRuleEntry(AbstractGameRulesScreen.this, x$0, x$1, x$2, x$3));
/*     */           }
/*     */           
/*     */           public void visitInteger(GameRule<Integer> gameRule) {
/* 293 */             addEntry(gameRule, (x$0, x$1, x$2, x$3) -> new AbstractGameRulesScreen.IntegerRuleEntry(AbstractGameRulesScreen.this, x$0, x$1, x$2, x$3));
/*     */           }
/*     */           
/*     */           private <T> void addEntry(GameRule<T> gameRule, AbstractGameRulesScreen.EntryFactory<T> factory) {
/*     */             ImmutableList immutableList;
/*     */             String narration;
/* 297 */             MutableComponent mutableComponent1 = Component.translatable(gameRule.getDescriptionId());
/* 298 */             String descriptionKey = gameRule.getDescriptionId() + ".description";
/* 299 */             Optional<MutableComponent> optionalDescription = Optional.<MutableComponent>of(Component.translatable(descriptionKey)).filter(ComponentUtils::isTranslationResolvable);
/* 300 */             if (!AbstractGameRulesScreen.RuleList.matchesFilter(gameRule.id(), mutableComponent1.getString(), gameRule.category().label().getString(), optionalDescription, lowerCaseFilter))
/*     */               return; 
/* 304 */             MutableComponent mutableComponent2 = Component.literal(gameRule.id()).withStyle(ChatFormatting.YELLOW);
/* 305 */             MutableComponent mutableComponent3 = Component.translatable("editGamerule.default", new Object[] { Component.literal(gameRule.serialize(gameRule.defaultValue())) }).withStyle(ChatFormatting.GRAY);
/* 309 */             if (optionalDescription.isPresent()) {
/* 310 */               ImmutableList.Builder<FormattedCharSequence> result = ImmutableList.builder().add(mutableComponent2.getVisualOrderText());
/* 311 */               Objects.requireNonNull(result);
/* 311 */               AbstractGameRulesScreen.access$600(AbstractGameRulesScreen.this).split((FormattedText)optionalDescription.get(), 150).forEach(result::add);
/* 312 */               immutableList = result.add(mutableComponent3.getVisualOrderText()).build();
/* 313 */               narration = ((MutableComponent)optionalDescription.get()).getString() + "\n" + ((MutableComponent)optionalDescription.get()).getString();
/*     */             } else {
/* 315 */               immutableList = ImmutableList.of(mutableComponent2.getVisualOrderText(), mutableComponent3.getVisualOrderText());
/* 316 */               narration = mutableComponent3.getString();
/*     */             } 
/* 319 */             ((Map<GameRule<T>, AbstractGameRulesScreen.RuleEntry>)entries.computeIfAbsent(gameRule.category(), k -> Maps.newHashMap())).put(gameRule, factory.create((Component)mutableComponent1, (List<FormattedCharSequence>)immutableList, narration, gameRule));
/*     */           }
/*     */         });
/* 323 */     entries.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.comparing(GameRuleCategory::getDescriptionId))).forEach(e -> {
/*     */           addEntry((AbstractSelectionList.Entry)new AbstractGameRulesScreen.CategoryRuleEntry(AbstractGameRulesScreen.this, (Component)((GameRuleCategory)e.getKey()).label().withStyle(new ChatFormatting[] { ChatFormatting.BOLD, ChatFormatting.YELLOW })));
/*     */           ((Map)e.getValue()).entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.comparing(GameRule::getDescriptionId))).forEach(());
/*     */         });
/*     */   }
/*     */   
/*     */   private static boolean matchesFilter(String gameRuleId, String readableName, String categoryName, Optional<MutableComponent> optionalDescription, String lowerCaseFilter) {
/* 330 */     return (toLowerCaseMatchesFilter(gameRuleId, lowerCaseFilter) || 
/* 331 */       toLowerCaseMatchesFilter(readableName, lowerCaseFilter) || 
/* 332 */       toLowerCaseMatchesFilter(categoryName, lowerCaseFilter) || ((Boolean)optionalDescription
/* 333 */       .<Boolean>map(description -> Boolean.valueOf(toLowerCaseMatchesFilter(description.getString(), lowerCaseFilter))).orElse(Boolean.valueOf(false))).booleanValue());
/*     */   }
/*     */   
/*     */   private static boolean toLowerCaseMatchesFilter(String gameRuleId, String lowerCaseFilter) {
/* 337 */     return gameRuleId.toLowerCase(Locale.ROOT).contains(lowerCaseFilter);
/*     */   }
/*     */   
/*     */   public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 342 */     super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
/* 343 */     AbstractGameRulesScreen.RuleEntry hovered = (AbstractGameRulesScreen.RuleEntry)getHovered();
/* 344 */     if (hovered != null && hovered.tooltip != null)
/* 345 */       graphics.setTooltipForNextFrame(hovered.tooltip, mouseX, mouseY); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\AbstractGameRulesScreen$RuleList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */