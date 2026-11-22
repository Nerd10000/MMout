/*    */ package net.minecraft.client;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*    */ 
/*    */ public class SectionUpdateTracker {
/*    */   private final RotatingSectionStorage<SectionDirtyState> storage;
/*    */   
/*    */   public SectionUpdateTracker(LevelHeightAccessor levelHeightAccessor, int renderDistance) {
/* 15 */     this.storage = new RotatingSectionStorage<>(renderDistance, levelHeightAccessor.getMinSectionY(), levelHeightAccessor.getMaxSectionY(), (index, sectionNode) -> new SectionDirtyState(true, false, sectionNode));
/*    */   }
/*    */   
/*    */   public void setDirty(int sectionX, int sectionY, int sectionZ, boolean playerChanged) {
/* 20 */     SectionDirtyState section = this.storage.getValue(sectionX, sectionY, sectionZ);
/* 21 */     if (section != null)
/* 22 */       section.setDirty(playerChanged); 
/*    */   }
/*    */   
/*    */   public void repositionCamera(SectionPos cameraSectionPos) {
/* 27 */     this.storage.repositionCenter(cameraSectionPos);
/*    */   }
/*    */   
/*    */   public int size() {
/* 31 */     return this.storage.size();
/*    */   }
/*    */   
/*    */   public SectionDirtyState getDirtyState(long sectionNode) {
/* 35 */     return this.storage.getValue(sectionNode);
/*    */   }
/*    */   
/*    */   public boolean hasAllNeighbors(ClientLevel level, long sectionNode) {
/* 39 */     return (doesChunkExistAt(level, SectionPos.offset(sectionNode, Direction.WEST)) && 
/* 40 */       doesChunkExistAt(level, SectionPos.offset(sectionNode, Direction.NORTH)) && 
/* 41 */       doesChunkExistAt(level, SectionPos.offset(sectionNode, Direction.EAST)) && 
/* 42 */       doesChunkExistAt(level, SectionPos.offset(sectionNode, Direction.SOUTH)) && 
/* 43 */       doesChunkExistAt(level, SectionPos.offset(sectionNode, -1, 0, -1)) && 
/* 44 */       doesChunkExistAt(level, SectionPos.offset(sectionNode, -1, 0, 1)) && 
/* 45 */       doesChunkExistAt(level, SectionPos.offset(sectionNode, 1, 0, -1)) && 
/* 46 */       doesChunkExistAt(level, SectionPos.offset(sectionNode, 1, 0, 1)));
/*    */   }
/*    */   
/*    */   private boolean doesChunkExistAt(ClientLevel level, long sectionNode) {
/* 50 */     ChunkAccess chunk = level.getChunk(SectionPos.x(sectionNode), SectionPos.z(sectionNode), ChunkStatus.FULL, false);
/* 51 */     return (chunk != null && level.getLightEngine().lightOnInColumn(SectionPos.getZeroNode(sectionNode)));
/*    */   }
/*    */   
/*    */   public static class SectionDirtyState implements RotatingSectionStorage.Value {
/*    */     private boolean isDirty;
/*    */     
/*    */     private boolean isDirtyFromPlayer;
/*    */     
/*    */     private long sectionNode;
/*    */     
/*    */     private SectionDirtyState(boolean isDirty, boolean isDirtyFromPlayer, long sectionNode) {
/* 60 */       this.isDirty = isDirty;
/* 61 */       this.isDirtyFromPlayer = isDirtyFromPlayer;
/*    */     }
/*    */     
/*    */     public void setDirty(boolean fromPlayer) {
/* 65 */       boolean wasDirty = this.isDirty;
/* 66 */       this.isDirty = true;
/* 67 */       this.isDirtyFromPlayer = fromPlayer | ((wasDirty && this.isDirtyFromPlayer));
/*    */     }
/*    */     
/*    */     public void setNotDirty() {
/* 71 */       this.isDirty = false;
/* 72 */       this.isDirtyFromPlayer = false;
/*    */     }
/*    */     
/*    */     public void setSectionNode(long sectionNode) {
/* 77 */       if (this.sectionNode != sectionNode) {
/* 78 */         this.sectionNode = sectionNode;
/* 79 */         this.isDirty = true;
/* 80 */         this.isDirtyFromPlayer = false;
/*    */       } 
/*    */     }
/*    */     
/*    */     public long getSectionNode() {
/* 86 */       return this.sectionNode;
/*    */     }
/*    */     
/*    */     public boolean isDirty() {
/* 90 */       return this.isDirty;
/*    */     }
/*    */     
/*    */     public boolean isDirtyFromPlayer() {
/* 94 */       return this.isDirtyFromPlayer;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\SectionUpdateTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */