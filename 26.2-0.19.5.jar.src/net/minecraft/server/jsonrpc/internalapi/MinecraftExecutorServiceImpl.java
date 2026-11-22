/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.notifications.NotificationManager;
/*    */ 
/*    */ public class MinecraftExecutorServiceImpl implements MinecraftExecutorService {
/*    */   private final NotificationManager notificationManager;
/*    */   
/*    */   public MinecraftExecutorServiceImpl(NotificationManager notificationManager) {
/* 15 */     this.notificationManager = notificationManager;
/*    */   }
/*    */   
/*    */   private DedicatedServer server() {
/* 19 */     return Objects.<DedicatedServer>requireNonNull(this.notificationManager.server());
/*    */   }
/*    */   
/*    */   public <V> CompletableFuture<V> submit(Supplier<V> supplier) {
/* 24 */     return server().submit(supplier);
/*    */   }
/*    */   
/*    */   public CompletableFuture<Void> submit(Runnable runnable) {
/* 29 */     return server().submit(runnable);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftExecutorServiceImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */