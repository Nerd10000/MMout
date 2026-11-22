package net.minecraft.client;

import net.minecraft.network.chat.Component;

@FunctionalInterface
public interface CaptionBasedToString<T> {
  Component toString(Component paramComponent, T paramT);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$CaptionBasedToString.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */