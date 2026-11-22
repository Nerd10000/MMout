/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.TicketStorage;
/*    */ 
/*    */ public class SimulationChunkTracker extends ChunkTracker {
/*    */   public static final int MAX_LEVEL = 33;
/*    */   
/* 12 */   protected final Long2ByteMap chunks = (Long2ByteMap)new Long2ByteOpenHashMap();
/*    */   
/*    */   private final TicketStorage ticketStorage;
/*    */   
/*    */   public SimulationChunkTracker(TicketStorage ticketStorage) {
/* 17 */     super(34, 16, 256);
/* 18 */     this.ticketStorage = ticketStorage;
/* 19 */     ticketStorage.setSimulationChunkUpdatedListener(this::update);
/* 20 */     this.chunks.defaultReturnValue((byte)33);
/*    */   }
/*    */   
/*    */   protected int getLevelFromSource(long to) {
/* 25 */     return this.ticketStorage.getTicketLevelAt(to, true);
/*    */   }
/*    */   
/*    */   public int getLevel(ChunkPos node) {
/* 29 */     return getLevel(node.pack());
/*    */   }
/*    */   
/*    */   protected int getLevel(long node) {
/* 34 */     return this.chunks.get(node);
/*    */   }
/*    */   
/*    */   protected void setLevel(long node, int level) {
/* 39 */     if (level >= 33) {
/* 40 */       this.chunks.remove(node);
/*    */     } else {
/* 42 */       this.chunks.put(node, (byte)level);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void runAllUpdates() {
/* 47 */     runUpdates(2147483647);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\SimulationChunkTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */