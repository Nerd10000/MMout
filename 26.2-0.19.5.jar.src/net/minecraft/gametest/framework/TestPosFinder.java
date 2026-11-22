package net.minecraft.gametest.framework;

import java.util.stream.Stream;
import net.minecraft.core.BlockPos;

@FunctionalInterface
public interface TestPosFinder {
  Stream<BlockPos> findTestPos();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestPosFinder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */