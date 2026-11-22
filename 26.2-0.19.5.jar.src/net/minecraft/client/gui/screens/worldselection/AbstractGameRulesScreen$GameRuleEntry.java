/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ 
/*     */ public abstract class GameRuleEntry extends AbstractGameRulesScreen.RuleEntry {
/*     */   private final List<FormattedCharSequence> label;
/*     */   
/* 189 */   protected final List<AbstractWidget> children = Lists.newArrayList();
/*     */   
/*     */   public GameRuleEntry(List<FormattedCharSequence> tooltip, Component label) {
/* 192 */     super(tooltip);
/* 193 */     this.label = (AbstractGameRulesScreen.access$100(AbstractGameRulesScreen.this)).font.split((FormattedText)label, 170);
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 198 */     return (List)this.children;
/*     */   }
/*     */   
/*     */   public List<? extends NarratableEntry> narratables() {
/* 203 */     return (List)this.children;
/*     */   }
/*     */   
/*     */   protected void extractLabel(GuiGraphicsExtractor graphics, int rowTop, int rowLeft) {
/* 207 */     if (this.label.size() == 1) {
/* 208 */       graphics.text((AbstractGameRulesScreen.access$200(AbstractGameRulesScreen.this)).font, this.label.get(0), rowLeft, rowTop + 5, -1);
/* 209 */     } else if (this.label.size() >= 2) {
/* 210 */       graphics.text((AbstractGameRulesScreen.access$300(AbstractGameRulesScreen.this)).font, this.label.get(0), rowLeft, rowTop, -1);
/* 211 */       graphics.text((AbstractGameRulesScreen.access$400(AbstractGameRulesScreen.this)).font, this.label.get(1), rowLeft, rowTop + 10, -1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\AbstractGameRulesScreen$GameRuleEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */