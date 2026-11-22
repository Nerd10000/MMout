package net.minecraft.world.entity;

import java.util.function.Consumer;

class null implements InsideBlockEffectApplier {
  public void apply(InsideBlockEffectType type) {}
  
  public void runBefore(InsideBlockEffectType type, Consumer<Entity> effect) {}
  
  public void runAfter(InsideBlockEffectType type, Consumer<Entity> effect) {}
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\InsideBlockEffectApplier$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */