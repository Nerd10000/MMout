/*    */ package net.minecraft.client.gui.screens.worldselection;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ public class WorldCreationGameRulesScreen extends AbstractGameRulesScreen {
/*    */   public WorldCreationGameRulesScreen(GameRules gameRules, Consumer<Optional<GameRules>> exitCallback) {
/* 11 */     super(gameRules, exitCallback);
/*    */   }
/*    */   
/*    */   protected void initContent() {
/* 16 */     this.ruleList = (AbstractGameRulesScreen.RuleList)this.layout.addToContents((LayoutElement)new AbstractGameRulesScreen.RuleList(this, this.gameRules));
/*    */   }
/*    */   
/*    */   protected void onDone() {
/* 21 */     closeAndApplyChanges();
/*    */   }
/*    */   
/*    */   public void onClose() {
/* 26 */     closeAndDiscardChanges();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\WorldCreationGameRulesScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */