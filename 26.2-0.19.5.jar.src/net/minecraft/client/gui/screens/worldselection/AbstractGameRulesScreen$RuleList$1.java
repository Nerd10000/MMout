/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ import net.minecraft.world.level.gamerules.GameRuleCategory;
/*     */ import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
/*     */ 
/*     */ class null implements GameRuleTypeVisitor {
/*     */   public void visitBoolean(GameRule<Boolean> gameRule) {
/* 288 */     addEntry(gameRule, (x$0, x$1, x$2, x$3) -> new AbstractGameRulesScreen.BooleanRuleEntry(this.this$1.this$0, x$0, x$1, x$2, x$3));
/*     */   }
/*     */   
/*     */   public void visitInteger(GameRule<Integer> gameRule) {
/* 293 */     addEntry(gameRule, (x$0, x$1, x$2, x$3) -> new AbstractGameRulesScreen.IntegerRuleEntry(this.this$1.this$0, x$0, x$1, x$2, x$3));
/*     */   }
/*     */   
/*     */   private <T> void addEntry(GameRule<T> gameRule, AbstractGameRulesScreen.EntryFactory<T> factory) {
/*     */     ImmutableList immutableList;
/*     */     String narration;
/* 297 */     MutableComponent mutableComponent1 = Component.translatable(gameRule.getDescriptionId());
/* 298 */     String descriptionKey = gameRule.getDescriptionId() + ".description";
/* 299 */     Optional<MutableComponent> optionalDescription = Optional.<MutableComponent>of(Component.translatable(descriptionKey)).filter(ComponentUtils::isTranslationResolvable);
/* 300 */     if (!AbstractGameRulesScreen.RuleList.matchesFilter(gameRule.id(), mutableComponent1.getString(), gameRule.category().label().getString(), optionalDescription, lowerCaseFilter))
/*     */       return; 
/* 304 */     MutableComponent mutableComponent2 = Component.literal(gameRule.id()).withStyle(ChatFormatting.YELLOW);
/* 305 */     MutableComponent mutableComponent3 = Component.translatable("editGamerule.default", new Object[] { Component.literal(gameRule.serialize(gameRule.defaultValue())) }).withStyle(ChatFormatting.GRAY);
/* 309 */     if (optionalDescription.isPresent()) {
/* 310 */       ImmutableList.Builder<FormattedCharSequence> result = ImmutableList.builder().add(mutableComponent2.getVisualOrderText());
/* 311 */       Objects.requireNonNull(result);
/* 311 */       AbstractGameRulesScreen.access$600(this.this$1.this$0).split((FormattedText)optionalDescription.get(), 150).forEach(result::add);
/* 312 */       immutableList = result.add(mutableComponent3.getVisualOrderText()).build();
/* 313 */       narration = ((MutableComponent)optionalDescription.get()).getString() + "\n" + ((MutableComponent)optionalDescription.get()).getString();
/*     */     } else {
/* 315 */       immutableList = ImmutableList.of(mutableComponent2.getVisualOrderText(), mutableComponent3.getVisualOrderText());
/* 316 */       narration = mutableComponent3.getString();
/*     */     } 
/* 319 */     ((Map<GameRule<T>, AbstractGameRulesScreen.RuleEntry>)entries.computeIfAbsent(gameRule.category(), k -> Maps.newHashMap())).put(gameRule, factory.create((Component)mutableComponent1, (List<FormattedCharSequence>)immutableList, narration, gameRule));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\AbstractGameRulesScreen$RuleList$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */