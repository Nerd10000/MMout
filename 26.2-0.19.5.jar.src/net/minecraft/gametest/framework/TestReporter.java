package net.minecraft.gametest.framework;

public interface TestReporter {
  void onTestFailed(GameTestInfo paramGameTestInfo);
  
  void onTestSuccess(GameTestInfo paramGameTestInfo);
  
  default void finish() {}
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestReporter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */