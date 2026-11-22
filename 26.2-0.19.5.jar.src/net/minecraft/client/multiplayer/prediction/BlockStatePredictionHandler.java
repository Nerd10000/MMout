/*    */ package net.minecraft.client.multiplayer.prediction;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.player.LocalPlayer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BlockStatePredictionHandler implements AutoCloseable {
/* 13 */   private final Long2ObjectOpenHashMap<ServerVerifiedState> serverVerifiedStates = new Long2ObjectOpenHashMap();
/*    */   
/*    */   private int currentSequenceNr;
/*    */   
/*    */   private boolean isPredicting;
/*    */   
/* 16 */   private int lastTeleportSequence = -1;
/*    */   
/*    */   public void retainKnownServerState(BlockPos pos, BlockState state, LocalPlayer player) {
/* 19 */     this.serverVerifiedStates.compute(pos.asLong(), (key, serverVerifiedState) -> (serverVerifiedState != null) ? serverVerifiedState.setSequence(this.currentSequenceNr) : new ServerVerifiedState(this.currentSequenceNr, state, player.position()));
/*    */   }
/*    */   
/*    */   public boolean updateKnownServerState(BlockPos pos, BlockState blockState) {
/* 28 */     ServerVerifiedState serverVerifiedState = (ServerVerifiedState)this.serverVerifiedStates.get(pos.asLong());
/* 29 */     if (serverVerifiedState == null)
/* 30 */       return false; 
/* 32 */     serverVerifiedState.setBlockState(blockState);
/* 33 */     return true;
/*    */   }
/*    */   
/*    */   public void endPredictionsUpTo(int sequence, ClientLevel clientLevel) {
/* 37 */     ObjectIterator<Long2ObjectMap.Entry<ServerVerifiedState>> stateIterator = this.serverVerifiedStates.long2ObjectEntrySet().iterator();
/* 38 */     while (stateIterator.hasNext()) {
/* 39 */       Long2ObjectMap.Entry<ServerVerifiedState> next = (Long2ObjectMap.Entry<ServerVerifiedState>)stateIterator.next();
/* 40 */       ServerVerifiedState serverVerifiedState = (ServerVerifiedState)next.getValue();
/* 41 */       if (serverVerifiedState.sequence <= sequence) {
/* 42 */         BlockPos pos = BlockPos.of(next.getLongKey());
/* 43 */         stateIterator.remove();
/* 45 */         clientLevel.syncBlockState(pos, serverVerifiedState.blockState, (this.lastTeleportSequence < sequence) ? serverVerifiedState.playerPos : null);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public BlockStatePredictionHandler startPredicting() {
/* 51 */     this.currentSequenceNr++;
/* 52 */     this.isPredicting = true;
/* 53 */     return this;
/*    */   }
/*    */   
/*    */   public void close() {
/* 58 */     this.isPredicting = false;
/*    */   }
/*    */   
/*    */   public int currentSequence() {
/* 62 */     return this.currentSequenceNr;
/*    */   }
/*    */   
/*    */   public void onTeleport() {
/* 66 */     this.lastTeleportSequence = this.currentSequenceNr;
/*    */   }
/*    */   
/*    */   public boolean isPredicting() {
/* 70 */     return this.isPredicting;
/*    */   }
/*    */   
/*    */   private static class ServerVerifiedState {
/*    */     private final Vec3 playerPos;
/*    */     
/*    */     private int sequence;
/*    */     
/*    */     private BlockState blockState;
/*    */     
/*    */     private ServerVerifiedState(int sequence, BlockState blockState, Vec3 playerPos) {
/* 79 */       this.sequence = sequence;
/* 80 */       this.blockState = blockState;
/* 81 */       this.playerPos = playerPos;
/*    */     }
/*    */     
/*    */     private ServerVerifiedState setSequence(int sequence) {
/* 85 */       this.sequence = sequence;
/* 86 */       return this;
/*    */     }
/*    */     
/*    */     private void setBlockState(BlockState blockState) {
/* 90 */       this.blockState = blockState;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\prediction\BlockStatePredictionHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */