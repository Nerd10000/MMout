/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.google.common.net.InetAddresses;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.time.Instant;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class IncomingIpBanDto extends Record {
/*    */   private final Optional<PlayerDto> player;
/*    */   
/*    */   private final Optional<String> ip;
/*    */   
/*    */   private final Optional<String> reason;
/*    */   
/*    */   private final Optional<String> source;
/*    */   
/*    */   private final Optional<Instant> expires;
/*    */   
/*    */   public static final MapCodec<IncomingIpBanDto> CODEC;
/*    */   
/*    */   public IncomingIpBanDto(Optional<PlayerDto> player, Optional<String> ip, Optional<String> reason, Optional<String> source, Optional<Instant> expires) {
/* 27 */     this.player = player;
/* 27 */     this.ip = ip;
/* 27 */     this.reason = reason;
/* 27 */     this.source = source;
/* 27 */     this.expires = expires;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IncomingIpBanDto;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<PlayerDto> player() {
/* 27 */     return this.player;
/*    */   }
/*    */   
/*    */   public Optional<String> ip() {
/* 27 */     return this.ip;
/*    */   }
/*    */   
/*    */   public Optional<String> reason() {
/* 27 */     return this.reason;
/*    */   }
/*    */   
/*    */   public Optional<String> source() {
/* 27 */     return this.source;
/*    */   }
/*    */   
/*    */   public Optional<Instant> expires() {
/* 27 */     return this.expires;
/*    */   }
/*    */   
/*    */   static {
/* 28 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)PlayerDto.CODEC.codec().optionalFieldOf("player").forGetter(IncomingIpBanDto::player), (App)Codec.STRING.optionalFieldOf("ip").forGetter(IncomingIpBanDto::ip), (App)Codec.STRING.optionalFieldOf("reason").forGetter(IncomingIpBanDto::reason), (App)Codec.STRING.optionalFieldOf("source").forGetter(IncomingIpBanDto::source), (App)ExtraCodecs.INSTANT_ISO8601.optionalFieldOf("expires").forGetter(IncomingIpBanDto::expires)).apply((Applicative)i, IncomingIpBanDto::new));
/*    */   }
/*    */   
/*    */   private IpBanlistService.IpBan toIpBan(ServerPlayer player) {
/* 37 */     return new IpBanlistService.IpBan(player.getIpAddress(), reason().orElse(null), source().orElse("Management server"), expires());
/*    */   }
/*    */   
/*    */   private IpBanlistService.IpBan toIpBan() {
/* 41 */     if (ip().isEmpty() || !InetAddresses.isInetAddress(ip().get()))
/* 42 */       return null; 
/* 44 */     return new IpBanlistService.IpBan(ip().get(), reason().orElse(null), source().orElse("Management server"), expires());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\methods\IpBanlistService$IncomingIpBanDto.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */