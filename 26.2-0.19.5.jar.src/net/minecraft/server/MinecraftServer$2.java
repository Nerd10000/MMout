/*      */ package net.minecraft.server;
/*      */ 
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import net.minecraft.world.level.gamerules.GameRule;
/*      */ import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ 
/*      */ class null implements GameRuleTypeVisitor {
/*      */   null(MinecraftServer this$0) {}
/*      */   
/*      */   public <T> void visit(GameRule<T> gameRule) {
/* 1885 */     entries.add(String.format(Locale.ROOT, "%s=%s\n", new Object[] { gameRule.getIdentifier(), this.val$gameRules.getAsString(gameRule) }));
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\MinecraftServer$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */