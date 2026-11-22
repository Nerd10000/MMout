/*    */ package net.minecraft.client.multiplayer.chat;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.time.ZoneId;
/*    */ import java.time.ZonedDateTime;
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import java.time.format.FormatStyle;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.PlayerChatMessage;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public final class Player extends Record implements LoggedChatMessage {
/*    */   private final GameProfile profile;
/*    */   
/*    */   private final PlayerChatMessage message;
/*    */   
/*    */   private final ChatTrustLevel trustLevel;
/*    */   
/*    */   public static final MapCodec<Player> CODEC;
/*    */   
/*    */   public Player(GameProfile profile, PlayerChatMessage message, ChatTrustLevel trustLevel) {
/* 37 */     this.profile = profile;
/* 37 */     this.message = message;
/* 37 */     this.trustLevel = trustLevel;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$Player;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$Player;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$Player;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$Player;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$Player;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$Player;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public GameProfile profile() {
/* 37 */     return this.profile;
/*    */   }
/*    */   
/*    */   public PlayerChatMessage message() {
/* 37 */     return this.message;
/*    */   }
/*    */   
/*    */   public ChatTrustLevel trustLevel() {
/* 37 */     return this.trustLevel;
/*    */   }
/*    */   
/*    */   static {
/* 38 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.AUTHLIB_GAME_PROFILE.fieldOf("profile").forGetter(Player::profile), (App)PlayerChatMessage.MAP_CODEC.forGetter(Player::message), (App)ChatTrustLevel.CODEC.optionalFieldOf("trust_level", ChatTrustLevel.SECURE).forGetter(Player::trustLevel)).apply((Applicative)i, Player::new));
/*    */   }
/*    */   
/* 44 */   private static final DateTimeFormatter TIME_FORMATTER = Util.localizedDateFormatter(FormatStyle.SHORT);
/*    */   
/*    */   public Component toContentComponent() {
/* 48 */     if (!this.message.filterMask().isEmpty()) {
/* 49 */       Component filtered = this.message.filterMask().applyWithFormatting(this.message.signedContent());
/* 50 */       return (filtered != null) ? filtered : (Component)Component.empty();
/*    */     } 
/* 52 */     return this.message.decoratedContent();
/*    */   }
/*    */   
/*    */   public Component toNarrationComponent() {
/* 57 */     Component content = toContentComponent();
/* 58 */     Component time = getTimeComponent();
/* 59 */     return (Component)Component.translatable("gui.chatSelection.message.narrate", new Object[] { this.profile.name(), content, time });
/*    */   }
/*    */   
/*    */   public Component toHeadingComponent() {
/* 63 */     Component time = getTimeComponent();
/* 64 */     return (Component)Component.translatable("gui.chatSelection.heading", new Object[] { this.profile.name(), time });
/*    */   }
/*    */   
/*    */   private Component getTimeComponent() {
/* 68 */     ZonedDateTime dateTime = ZonedDateTime.ofInstant(this.message.timeStamp(), ZoneId.systemDefault());
/* 69 */     return (Component)Component.literal(dateTime.format(TIME_FORMATTER)).withStyle(new ChatFormatting[] { ChatFormatting.ITALIC, ChatFormatting.GRAY });
/*    */   }
/*    */   
/*    */   public boolean canReport(UUID reportedPlayerId) {
/* 74 */     return this.message.hasSignatureFrom(reportedPlayerId);
/*    */   }
/*    */   
/*    */   public UUID profileId() {
/* 78 */     return this.profile.id();
/*    */   }
/*    */   
/*    */   public LoggedChatEvent.Type type() {
/* 83 */     return LoggedChatEvent.Type.PLAYER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\LoggedChatMessage$Player.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */