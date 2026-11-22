/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ 
/*     */ public class CategoryRuleEntry extends AbstractGameRulesScreen.RuleEntry {
/*     */   private final Component label;
/*     */   
/*     */   public CategoryRuleEntry(Component label) {
/* 152 */     super((List<FormattedCharSequence>)null);
/* 153 */     this.label = label;
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 158 */     graphics.centeredText((AbstractGameRulesScreen.access$000(AbstractGameRulesScreen.this)).font, this.label, getContentXMiddle(), getContentY() + 5, -1);
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 163 */     return (List<? extends GuiEventListener>)ImmutableList.of();
/*     */   }
/*     */   
/*     */   public List<? extends NarratableEntry> narratables() {
/* 168 */     return (List<? extends NarratableEntry>)ImmutableList.of(new NarratableEntry() {
/*     */           public NarratableEntry.NarrationPriority narrationPriority() {
/* 171 */             return NarratableEntry.NarrationPriority.HOVERED;
/*     */           }
/*     */           
/*     */           public void updateNarration(NarrationElementOutput output) {
/* 176 */             output.add(NarratedElementType.TITLE, AbstractGameRulesScreen.CategoryRuleEntry.this.label);
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\AbstractGameRulesScreen$CategoryRuleEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */