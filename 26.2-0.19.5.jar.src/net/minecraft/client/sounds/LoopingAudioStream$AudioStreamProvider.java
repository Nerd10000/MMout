package net.minecraft.client.sounds;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface AudioStreamProvider {
  AudioStream create(InputStream paramInputStream) throws IOException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\sounds\LoopingAudioStream$AudioStreamProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */