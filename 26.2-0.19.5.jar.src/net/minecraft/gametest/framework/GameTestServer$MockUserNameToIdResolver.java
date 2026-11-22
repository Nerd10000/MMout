/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.UserNameToIdResolver;
/*     */ 
/*     */ class MockUserNameToIdResolver implements UserNameToIdResolver {
/* 393 */   private final Set<NameAndId> savedIds = new HashSet<>();
/*     */   
/*     */   public void add(NameAndId nameAndId) {
/* 397 */     this.savedIds.add(nameAndId);
/*     */   }
/*     */   
/*     */   public Optional<NameAndId> get(String name) {
/* 402 */     return this.savedIds.stream().filter(e -> e.name().equals(name)).findFirst()
/* 403 */       .or(() -> Optional.of(NameAndId.createOffline(name)));
/*     */   }
/*     */   
/*     */   public Optional<NameAndId> get(UUID id) {
/* 408 */     return this.savedIds.stream().filter(e -> e.id().equals(id)).findFirst();
/*     */   }
/*     */   
/*     */   public void resolveOfflineUsers(boolean value) {}
/*     */   
/*     */   public void save() {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestServer$MockUserNameToIdResolver.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */