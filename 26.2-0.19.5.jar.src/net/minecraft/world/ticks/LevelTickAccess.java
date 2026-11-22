package net.minecraft.world.ticks;

import net.minecraft.core.BlockPos;

public interface LevelTickAccess<T> extends TickAccess<T> {
  boolean willTickThisTick(BlockPos paramBlockPos, T paramT);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\ticks\LevelTickAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */