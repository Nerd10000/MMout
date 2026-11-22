package net.minecraft.server.level;

import java.util.List;
import net.minecraft.world.level.ChunkPos;

public interface PlayerProvider {
  List<ServerPlayer> getPlayers(ChunkPos paramChunkPos, boolean paramBoolean);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ChunkHolder$PlayerProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */