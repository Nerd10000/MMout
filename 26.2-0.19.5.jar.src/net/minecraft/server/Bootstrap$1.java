/*    */ package net.minecraft.server;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.locale.Language;
/*    */ import net.minecraft.world.level.gamerules.GameRule;
/*    */ import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
/*    */ 
/*    */ class null implements GameRuleTypeVisitor {
/*    */   public <T> void visit(GameRule<T> gameRule) {
/* 94 */     if (!language.has(gameRule.getDescriptionId()))
/* 95 */       missing.add(gameRule.id()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\Bootstrap$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */