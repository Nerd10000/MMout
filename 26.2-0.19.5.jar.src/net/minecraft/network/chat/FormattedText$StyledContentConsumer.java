package net.minecraft.network.chat;

import java.util.Optional;

public interface StyledContentConsumer<T> {
  Optional<T> accept(Style paramStyle, String paramString);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\FormattedText$StyledContentConsumer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */