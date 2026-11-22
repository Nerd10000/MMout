/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ enum Type {
/*     */   POINTS, LEVELS;
/*     */   
/*     */   public final BiConsumer<ServerPlayer, Integer> add;
/*     */   
/*     */   public final BiPredicate<ServerPlayer, Integer> set;
/*     */   
/*     */   public final String name;
/*     */   
/*     */   private final ToIntFunction<ServerPlayer> query;
/*     */   
/*     */   static {
/* 137 */     POINTS = new Type("POINTS", 0, "points", Player::giveExperiencePoints, (p, a) -> {
/*     */           if (a.intValue() >= p.getXpNeededForNextLevel())
/*     */             return false; 
/*     */           p.setExperiencePoints(a.intValue());
/*     */           return true;
/*     */         }p -> Mth.floor(p.experienceProgress * p.getXpNeededForNextLevel()));
/* 144 */     LEVELS = new Type("LEVELS", 1, "levels", ServerPlayer::giveExperienceLevels, (p, a) -> {
/*     */           p.setExperienceLevels(a.intValue());
/*     */           return true;
/*     */         }p -> p.experienceLevel);
/*     */   }
/*     */   
/*     */   Type(String name, BiConsumer<ServerPlayer, Integer> add, BiPredicate<ServerPlayer, Integer> set, ToIntFunction<ServerPlayer> query) {
/* 155 */     this.add = add;
/* 156 */     this.name = name;
/* 157 */     this.set = set;
/* 158 */     this.query = query;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\ExperienceCommand$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */