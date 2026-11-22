/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ 
/*    */ class PendingStore {
/*    */   private CompoundTag data;
/*    */   
/* 57 */   private final CompletableFuture<Void> result = new CompletableFuture<>();
/*    */   
/*    */   public PendingStore(CompoundTag data) {
/* 60 */     this.data = data;
/*    */   }
/*    */   
/*    */   private CompoundTag copyData() {
/* 64 */     CompoundTag data = this.data;
/* 65 */     return (data == null) ? null : data.copy();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\storage\IOWorker$PendingStore.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */