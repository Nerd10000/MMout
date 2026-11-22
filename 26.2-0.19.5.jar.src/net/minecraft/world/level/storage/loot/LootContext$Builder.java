/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class Builder {
/*     */   private final LootParams params;
/*     */   
/*     */   private RandomSource random;
/*     */   
/*     */   public Builder(LootParams params) {
/*  86 */     this.params = params;
/*     */   }
/*     */   
/*     */   public Builder withOptionalRandomSeed(long seed) {
/*  90 */     if (seed != 0L)
/*  91 */       this.random = RandomSource.create(seed); 
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withOptionalRandomSource(RandomSource randomSource) {
/*  97 */     this.random = randomSource;
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public ServerLevel getLevel() {
/* 102 */     return this.params.getLevel();
/*     */   }
/*     */   
/*     */   public LootContext create(Optional<Identifier> randomSequenceKey) {
/* 106 */     ServerLevel level = getLevel();
/* 107 */     MinecraftServer server = level.getServer();
/* 110 */     Objects.requireNonNull(level);
/* 110 */     RandomSource random = Optional.<RandomSource>ofNullable(this.random).or(() -> {
/*     */           Objects.requireNonNull(server);
/*     */           return randomSequenceKey.map(server::getRandomSequence);
/* 110 */         }).orElseGet(level::getRandom);
/* 111 */     return new LootContext(this.params, random, (HolderGetter.Provider)server.reloadableRegistries().lookup());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\LootContext$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */