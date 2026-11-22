/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.players.ProfileResolver;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ 
/*     */ public final class Dynamic extends ResolvableProfile {
/* 192 */   private static final Component DYNAMIC_TOOLTIP = (Component)Component.translatable("component.profile.dynamic").withStyle(ChatFormatting.GRAY);
/*     */   
/*     */   private final Either<String, UUID> nameOrId;
/*     */   
/*     */   private Dynamic(Either<String, UUID> nameOrId, PlayerSkin.Patch skinPatch) {
/* 196 */     super(ResolvableProfile.createPartialProfile(nameOrId.left(), nameOrId.right(), PropertyMap.EMPTY), skinPatch);
/* 197 */     this.nameOrId = nameOrId;
/*     */   }
/*     */   
/*     */   public Optional<String> name() {
/* 203 */     return this.nameOrId.left();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 208 */     if (this != o) {
/* 208 */       if (o instanceof Dynamic) {
/* 208 */         Dynamic that = (Dynamic)o;
/* 208 */         if (this.nameOrId.equals(that.nameOrId) && this.skinPatch.equals(that.skinPatch));
/*     */       } 
/* 208 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 213 */     int result = 31 + this.nameOrId.hashCode();
/* 214 */     result = 31 * result + this.skinPatch.hashCode();
/* 215 */     return result;
/*     */   }
/*     */   
/*     */   protected Either<GameProfile, ResolvableProfile.Partial> unpack() {
/* 220 */     return Either.right(new ResolvableProfile.Partial(this.nameOrId.left(), this.nameOrId.right(), PropertyMap.EMPTY));
/*     */   }
/*     */   
/*     */   public CompletableFuture<GameProfile> resolveProfile(ProfileResolver profileResolver) {
/* 225 */     return CompletableFuture.supplyAsync(() -> (GameProfile)profileResolver.fetchByNameOrId(this.nameOrId).orElse(this.partialProfile), (Executor)Util.nonCriticalIoPool());
/*     */   }
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 230 */     consumer.accept(DYNAMIC_TOOLTIP);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\ResolvableProfile$Dynamic.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */