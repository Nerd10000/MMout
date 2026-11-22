/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ class null implements ChunkTrackingView {
/*    */   public boolean contains(int chunkX, int chunkZ, boolean includeNeighbors) {
/* 13 */     return false;
/*    */   }
/*    */   
/*    */   public void forEach(Consumer<ChunkPos> consumer) {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ChunkTrackingView$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */