package net.minecraft.client.sounds;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.sound.sampled.AudioFormat;

public interface AudioStream extends Closeable {
  AudioFormat getFormat();
  
  ByteBuffer read(int paramInt) throws IOException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\sounds\AudioStream.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */