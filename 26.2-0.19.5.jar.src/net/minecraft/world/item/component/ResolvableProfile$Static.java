/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.players.ProfileResolver;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ 
/*     */ public final class Static extends ResolvableProfile {
/* 140 */   public static final Static EMPTY = new Static(Either.right(ResolvableProfile.Partial.EMPTY), PlayerSkin.Patch.EMPTY);
/*     */   
/*     */   private final Either<GameProfile, ResolvableProfile.Partial> contents;
/*     */   
/*     */   private Static(Either<GameProfile, ResolvableProfile.Partial> contents, PlayerSkin.Patch skinPatch) {
/* 145 */     super((GameProfile)contents.map(gameProfile -> gameProfile, ResolvableProfile.Partial::createProfile), skinPatch);
/* 150 */     this.contents = contents;
/*     */   }
/*     */   
/*     */   public CompletableFuture<GameProfile> resolveProfile(ProfileResolver profileResolver) {
/* 155 */     return CompletableFuture.completedFuture(this.partialProfile);
/*     */   }
/*     */   
/*     */   protected Either<GameProfile, ResolvableProfile.Partial> unpack() {
/* 160 */     return this.contents;
/*     */   }
/*     */   
/*     */   public Optional<String> name() {
/* 165 */     return (Optional<String>)this.contents.map(gameProfile -> Optional.of(gameProfile.name()), partial -> partial.name);
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 173 */     if (this != o) {
/* 173 */       if (o instanceof Static) {
/* 173 */         Static that = (Static)o;
/* 173 */         if (this.contents.equals(that.contents) && this.skinPatch.equals(that.skinPatch));
/*     */       } 
/* 173 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 178 */     int result = 31 + this.contents.hashCode();
/* 179 */     result = 31 * result + this.skinPatch.hashCode();
/* 180 */     return result;
/*     */   }
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\ResolvableProfile$Static.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */