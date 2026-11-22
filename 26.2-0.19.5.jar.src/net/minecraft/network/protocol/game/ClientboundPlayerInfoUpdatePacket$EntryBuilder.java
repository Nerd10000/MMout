/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.RemoteChatSession;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ class EntryBuilder {
/*     */   private final UUID profileId;
/*     */   
/*     */   private GameProfile profile;
/*     */   
/*     */   private boolean listed;
/*     */   
/*     */   private int latency;
/*     */   
/* 180 */   private GameType gameMode = GameType.DEFAULT_MODE;
/*     */   
/*     */   private Component displayName;
/*     */   
/*     */   private boolean showHat;
/*     */   
/*     */   private int listOrder;
/*     */   
/*     */   private RemoteChatSession.Data chatSession;
/*     */   
/*     */   private EntryBuilder(UUID profileId) {
/* 187 */     this.profileId = profileId;
/*     */   }
/*     */   
/*     */   private ClientboundPlayerInfoUpdatePacket.Entry build() {
/* 191 */     return new ClientboundPlayerInfoUpdatePacket.Entry(this.profileId, this.profile, this.listed, this.latency, this.gameMode, this.displayName, this.showHat, this.listOrder, this.chatSession);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundPlayerInfoUpdatePacket$EntryBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */