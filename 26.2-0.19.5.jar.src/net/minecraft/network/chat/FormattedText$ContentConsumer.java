package net.minecraft.network.chat;

import java.util.Optional;

public interface ContentConsumer<T> {
  Optional<T> accept(String paramString);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\FormattedText$ContentConsumer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */