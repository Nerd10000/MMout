/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.time.Instant;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.UserBanListEntry;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class UserBanDto extends Record {
/*    */   private final PlayerDto player;
/*    */   
/*    */   private final Optional<String> reason;
/*    */   
/*    */   private final Optional<String> source;
/*    */   
/*    */   private final Optional<Instant> expires;
/*    */   
/*    */   public static final MapCodec<UserBanDto> CODEC;
/*    */   
/*    */   public UserBanDto(PlayerDto player, Optional<String> reason, Optional<String> source, Optional<Instant> expires) {
/* 28 */     this.player = player;
/* 28 */     this.reason = reason;
/* 28 */     this.source = source;
/* 28 */     this.expires = expires;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public PlayerDto player() {
/* 28 */     return this.player;
/*    */   }
/*    */   
/*    */   public Optional<String> reason() {
/* 28 */     return this.reason;
/*    */   }
/*    */   
/*    */   public Optional<String> source() {
/* 28 */     return this.source;
/*    */   }
/*    */   
/*    */   public Optional<Instant> expires() {
/* 28 */     return this.expires;
/*    */   }
/*    */   
/*    */   static {
/* 29 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)PlayerDto.CODEC.codec().fieldOf("player").forGetter(UserBanDto::player), (App)Codec.STRING.optionalFieldOf("reason").forGetter(UserBanDto::reason), (App)Codec.STRING.optionalFieldOf("source").forGetter(UserBanDto::source), (App)ExtraCodecs.INSTANT_ISO8601.optionalFieldOf("expires").forGetter(UserBanDto::expires)).apply((Applicative)i, UserBanDto::new));
/*    */   }
/*    */   
/*    */   private static UserBanDto from(BanlistService.UserBan ban) {
/* 37 */     return new UserBanDto(PlayerDto.from(ban.player()), Optional.ofNullable(ban.reason()), Optional.of(ban.source()), ban.expires());
/*    */   }
/*    */   
/*    */   public static UserBanDto from(UserBanListEntry entry) {
/* 41 */     return from(BanlistService.UserBan.from(entry));
/*    */   }
/*    */   
/*    */   private BanlistService.UserBan toUserBan(NameAndId nameAndId) {
/* 45 */     return new BanlistService.UserBan(nameAndId, reason().orElse(null), source().orElse("Management server"), expires());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\methods\BanlistService$UserBanDto.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */