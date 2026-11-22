/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ 
/*     */ public class BooleanRuleEntry extends AbstractGameRulesScreen.GameRuleEntry {
/*     */   private final CycleButton<Boolean> checkbox;
/*     */   
/*     */   public BooleanRuleEntry(AbstractGameRulesScreen this$0, Component name, List<FormattedCharSequence> tooltip, String narration, GameRule<Boolean> gameRule) {
/* 220 */     super(this$0, tooltip, name);
/* 221 */     this
/*     */       
/* 224 */       .checkbox = CycleButton.onOffBuilder(((Boolean)this$0.gameRules.get(gameRule)).booleanValue()).displayOnlyValue().withCustomNarration(button -> button.createDefaultNarrationMessage().append("\n").append(narration)).create(10, 5, 44, 20, name, (button, newValue) -> this$0.gameRules.set(gameRule, newValue, null));
/* 225 */     this.children.add(this.checkbox);
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 230 */     extractLabel(graphics, getContentY(), getContentX());
/* 231 */     this.checkbox.setX(getContentRight() - 45);
/* 232 */     this.checkbox.setY(getContentY());
/* 233 */     this.checkbox.extractRenderState(graphics, mouseX, mouseY, a);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\AbstractGameRulesScreen$BooleanRuleEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */