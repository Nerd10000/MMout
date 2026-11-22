package net.minecraft.gametest.framework;

public interface GameTestListener {
  void testStructureLoaded(GameTestInfo paramGameTestInfo);
  
  void testPassed(GameTestInfo paramGameTestInfo, GameTestRunner paramGameTestRunner);
  
  void testFailed(GameTestInfo paramGameTestInfo, GameTestRunner paramGameTestRunner);
  
  void testAddedForRerun(GameTestInfo paramGameTestInfo1, GameTestInfo paramGameTestInfo2, GameTestRunner paramGameTestRunner);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */