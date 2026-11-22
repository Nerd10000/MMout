/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.server.players.ProfileResolver;
/*     */ 
/*     */ class MockProfileResolver implements ProfileResolver {
/*     */   public Optional<GameProfile> fetchByName(String name) {
/* 425 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<GameProfile> fetchById(UUID id) {
/* 430 */     return Optional.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestServer$MockProfileResolver.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */