package net.minecraft.world.ticks;

@FunctionalInterface
interface PosAndContainerConsumer<T> {
  void accept(long paramLong, LevelChunkTicks<T> paramLevelChunkTicks);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\ticks\LevelTicks$PosAndContainerConsumer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */