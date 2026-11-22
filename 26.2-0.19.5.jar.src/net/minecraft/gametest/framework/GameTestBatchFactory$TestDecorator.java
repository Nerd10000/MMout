package net.minecraft.gametest.framework;

import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;

@FunctionalInterface
public interface TestDecorator {
  Stream<GameTestInfo> decorate(Holder.Reference<GameTestInstance> paramReference, ServerLevel paramServerLevel);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestBatchFactory$TestDecorator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */