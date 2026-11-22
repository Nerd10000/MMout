/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public final class SharedState {
/*    */   private final ResourceManager manager;
/*    */   
/* 30 */   private final Map<PreparableReloadListener.StateKey<?>, Object> state = new IdentityHashMap<>();
/*    */   
/*    */   public SharedState(ResourceManager manager) {
/* 33 */     this.manager = manager;
/*    */   }
/*    */   
/*    */   public ResourceManager resourceManager() {
/* 37 */     return this.manager;
/*    */   }
/*    */   
/*    */   public <T> void set(PreparableReloadListener.StateKey<T> key, T value) {
/* 41 */     this.state.put(key, value);
/*    */   }
/*    */   
/*    */   public <T> T get(PreparableReloadListener.StateKey<T> key) {
/* 46 */     return Objects.requireNonNull((T)this.state.get(key));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\PreparableReloadListener$SharedState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */