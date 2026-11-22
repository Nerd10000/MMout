/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public enum ServerPackStatus {
/* 130 */   ENABLED("enabled"),
/* 131 */   DISABLED("disabled"),
/* 132 */   PROMPT("prompt");
/*     */   
/*     */   public static final MapCodec<ServerPackStatus> FIELD_CODEC;
/*     */   
/*     */   private final Component name;
/*     */   
/*     */   static {
/* 135 */     FIELD_CODEC = Codec.BOOL.optionalFieldOf("acceptTextures").xmap(acceptTextures -> (ServerPackStatus)acceptTextures.map(()).orElse(PROMPT), status -> {
/*     */           switch (status.ordinal()) {
/*     */             default:
/*     */               throw new MatchException(null, null);
/*     */             case 0:
/*     */             
/*     */             case 1:
/*     */             
/*     */             case 2:
/*     */               break;
/*     */           } 
/*     */           return Optional.empty();
/*     */         });
/*     */   }
/*     */   
/*     */   ServerPackStatus(String name) {
/* 147 */     this.name = (Component)Component.translatable("manageServer.resourcePack." + name);
/*     */   }
/*     */   
/*     */   public Component getName() {
/* 151 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ServerData$ServerPackStatus.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */