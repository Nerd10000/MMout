/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.RemoteChatSession;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.codec.StreamEncoder;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ public enum Action {
/*     */   ADD_PLAYER, INITIALIZE_CHAT, UPDATE_GAME_MODE, UPDATE_LISTED, UPDATE_LATENCY, UPDATE_DISPLAY_NAME, UPDATE_LIST_ORDER, UPDATE_HAT;
/*     */   
/*     */   private final Reader reader;
/*     */   
/*     */   private final Writer writer;
/*     */   
/*     */   static {
/*  92 */     ADD_PLAYER = new Action("ADD_PLAYER", 0, (entry, input) -> {
/*     */           String name = (String)ByteBufCodecs.PLAYER_NAME.decode(input);
/*     */           PropertyMap properties = (PropertyMap)ByteBufCodecs.GAME_PROFILE_PROPERTIES.decode(input);
/*     */           entry.profile = new GameProfile(entry.profileId, name, properties);
/*     */         }(output, entry) -> {
/*     */           GameProfile profile = Objects.<GameProfile>requireNonNull(entry.profile());
/*     */           ByteBufCodecs.PLAYER_NAME.encode(output, profile.name());
/*     */           ByteBufCodecs.GAME_PROFILE_PROPERTIES.encode(output, profile.properties());
/*     */         });
/* 104 */     INITIALIZE_CHAT = new Action("INITIALIZE_CHAT", 1, (entry, input) -> entry.chatSession = (RemoteChatSession.Data)input.readNullable(RemoteChatSession.Data::read), (output, entry) -> output.writeNullable(entry.chatSession, RemoteChatSession.Data::write));
/* 108 */     UPDATE_GAME_MODE = new Action("UPDATE_GAME_MODE", 2, (entry, input) -> entry.gameMode = GameType.byId(input.readVarInt()), (output, entry) -> output.writeVarInt(entry.gameMode().getId()));
/* 112 */     UPDATE_LISTED = new Action("UPDATE_LISTED", 3, (entry, input) -> entry.listed = input.readBoolean(), (output, entry) -> output.writeBoolean(entry.listed()));
/* 116 */     UPDATE_LATENCY = new Action("UPDATE_LATENCY", 4, (entry, input) -> entry.latency = input.readVarInt(), (output, entry) -> output.writeVarInt(entry.latency()));
/* 120 */     UPDATE_DISPLAY_NAME = new Action("UPDATE_DISPLAY_NAME", 5, (entry, input) -> entry.displayName = (Component)FriendlyByteBuf.readNullable((ByteBuf)input, (StreamDecoder)ComponentSerialization.TRUSTED_STREAM_CODEC), (output, entry) -> FriendlyByteBuf.writeNullable((ByteBuf)output, entry.displayName(), (StreamEncoder)ComponentSerialization.TRUSTED_STREAM_CODEC));
/* 124 */     UPDATE_LIST_ORDER = new Action("UPDATE_LIST_ORDER", 6, (entry, input) -> entry.listOrder = input.readVarInt(), (output, entry) -> output.writeVarInt(entry.listOrder));
/* 128 */     UPDATE_HAT = new Action("UPDATE_HAT", 7, (entry, input) -> entry.showHat = input.readBoolean(), (output, entry) -> output.writeBoolean(entry.showHat));
/*     */   }
/*     */   
/*     */   Action(Reader reader, Writer writer) {
/* 138 */     this.reader = reader;
/* 139 */     this.writer = writer;
/*     */   }
/*     */   
/*     */   public static interface Reader {
/*     */     void read(ClientboundPlayerInfoUpdatePacket.EntryBuilder param2EntryBuilder, RegistryFriendlyByteBuf param2RegistryFriendlyByteBuf);
/*     */   }
/*     */   
/*     */   public static interface Writer {
/*     */     void write(RegistryFriendlyByteBuf param2RegistryFriendlyByteBuf, ClientboundPlayerInfoUpdatePacket.Entry param2Entry);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundPlayerInfoUpdatePacket$Action.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */