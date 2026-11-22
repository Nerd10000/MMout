/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ 
/*     */ public abstract class RuleEntry extends ContainerObjectSelectionList.Entry<AbstractGameRulesScreen.RuleEntry> {
/*     */   private final List<FormattedCharSequence> tooltip;
/*     */   
/*     */   public RuleEntry(List<FormattedCharSequence> tooltip) {
/* 144 */     this.tooltip = tooltip;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\AbstractGameRulesScreen$RuleEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */