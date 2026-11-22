package net.minecraft.world.entity;

import net.minecraft.network.chat.Component;

@FunctionalInterface
public interface LineSplitter {
  Display.TextDisplay.CachedInfo split(Component paramComponent, int paramInt);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Display$TextDisplay$LineSplitter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */