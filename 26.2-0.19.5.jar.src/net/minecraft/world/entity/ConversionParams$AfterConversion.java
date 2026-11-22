package net.minecraft.world.entity;

@FunctionalInterface
public interface AfterConversion<T extends Mob> {
  void finalizeConversion(T paramT);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ConversionParams$AfterConversion.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */