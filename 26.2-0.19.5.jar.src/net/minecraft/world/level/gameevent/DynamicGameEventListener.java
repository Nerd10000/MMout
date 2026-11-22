/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*    */ 
/*    */ public class DynamicGameEventListener<T extends GameEventListener> {
/*    */   private final T listener;
/*    */   
/*    */   private SectionPos lastSection;
/*    */   
/*    */   public DynamicGameEventListener(T listener) {
/* 19 */     this.listener = listener;
/*    */   }
/*    */   
/*    */   public void add(ServerLevel level) {
/* 23 */     move(level);
/*    */   }
/*    */   
/*    */   public T getListener() {
/* 27 */     return this.listener;
/*    */   }
/*    */   
/*    */   public void remove(ServerLevel level) {
/* 31 */     ifChunkExists((LevelReader)level, this.lastSection, dispatcher -> dispatcher.unregister((GameEventListener)this.listener));
/*    */   }
/*    */   
/*    */   public void move(ServerLevel level) {
/* 35 */     this.listener.getListenerSource().getPosition((Level)level)
/* 36 */       .map(SectionPos::of)
/* 37 */       .ifPresent(currentSection -> {
/*    */           if (this.lastSection == null || !this.lastSection.equals(currentSection)) {
/*    */             ifChunkExists((LevelReader)level, this.lastSection, ());
/*    */             this.lastSection = currentSection;
/*    */             ifChunkExists((LevelReader)level, this.lastSection, ());
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   private static void ifChunkExists(LevelReader level, SectionPos sectionPos, Consumer<GameEventListenerRegistry> action) {
/* 47 */     if (sectionPos == null)
/*    */       return; 
/* 51 */     ChunkAccess chunk = level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.FULL, false);
/* 53 */     if (chunk != null)
/* 54 */       action.accept(chunk.getListenerRegistry(sectionPos.y())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\DynamicGameEventListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */