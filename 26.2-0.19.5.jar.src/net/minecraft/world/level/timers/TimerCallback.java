package net.minecraft.world.level.timers;

import com.mojang.serialization.MapCodec;

public interface TimerCallback<T> {
  void handle(T paramT, TimerQueue<T> paramTimerQueue, long paramLong);
  
  MapCodec<? extends TimerCallback<T>> codec();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\timers\TimerCallback.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */