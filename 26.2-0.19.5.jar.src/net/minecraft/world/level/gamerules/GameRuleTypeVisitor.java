package net.minecraft.world.level.gamerules;

public interface GameRuleTypeVisitor {
  default <T> void visit(GameRule<T> gameRule) {}
  
  default void visitBoolean(GameRule<Boolean> gameRule) {}
  
  default void visitInteger(GameRule<Integer> gameRule) {}
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gamerules\GameRuleTypeVisitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */