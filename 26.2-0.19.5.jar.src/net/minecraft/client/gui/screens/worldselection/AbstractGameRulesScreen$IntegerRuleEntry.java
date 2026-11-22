/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ 
/*     */ public class IntegerRuleEntry extends AbstractGameRulesScreen.GameRuleEntry {
/*     */   private final EditBox input;
/*     */   
/*     */   public IntegerRuleEntry(AbstractGameRulesScreen this$0, Component label, List<FormattedCharSequence> tooltip, String narration, GameRule<Integer> gameRule) {
/* 241 */     super(this$0, tooltip, label);
/* 243 */     this.input = new EditBox((AbstractGameRulesScreen.access$500(this$0)).font, 10, 5, 44, 20, (Component)label.copy().append("\n").append(narration).append("\n"));
/* 244 */     this.input.setValue(this$0.gameRules.getAsString(gameRule));
/* 245 */     this.input.setResponder(v -> {
/*     */           DataResult<Integer> value = gameRule.deserialize(v);
/*     */           if (value.isSuccess()) {
/*     */             this.input.setTextColor(-2039584);
/*     */             this$0.clearInvalid(this);
/*     */             this$0.gameRules.set(gameRule, value.getOrThrow(), null);
/*     */           } else {
/*     */             this.input.setTextColor(-65536);
/*     */             this$0.markInvalid(this);
/*     */           } 
/*     */         });
/* 256 */     this.children.add(this.input);
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 261 */     extractLabel(graphics, getContentY(), getContentX());
/* 262 */     this.input.setX(getContentRight() - 45);
/* 263 */     this.input.setY(getContentY());
/* 264 */     this.input.extractRenderState(graphics, mouseX, mouseY, a);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\AbstractGameRulesScreen$IntegerRuleEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */