/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ public class ChunkEntities<T> {
/*    */   private final ChunkPos pos;
/*    */   
/*    */   private final List<T> entities;
/*    */   
/*    */   public ChunkEntities(ChunkPos pos, List<T> entities) {
/* 14 */     this.pos = pos;
/* 15 */     this.entities = entities;
/*    */   }
/*    */   
/*    */   public ChunkPos getPos() {
/* 19 */     return this.pos;
/*    */   }
/*    */   
/*    */   public Stream<T> getEntities() {
/* 23 */     return this.entities.stream();
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 27 */     return this.entities.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\entity\ChunkEntities.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */