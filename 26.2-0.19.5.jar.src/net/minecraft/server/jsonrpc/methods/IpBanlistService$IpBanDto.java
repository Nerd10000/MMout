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
/*    */ import net.minecraft.server.players.IpBanListEntry;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class IpBanDto extends Record {
/*    */   private final String ip;
/*    */   
/*    */   private final Optional<String> reason;
/*    */   
/*    */   private final Optional<String> source;
/*    */   
/*    */   private final Optional<Instant> expires;
/*    */   
/*    */   public static final MapCodec<IpBanDto> CODEC;
/*    */   
/*    */   public IpBanDto(String ip, Optional<String> reason, Optional<String> source, Optional<Instant> expires) {
/* 48 */     this.ip = ip;
/* 48 */     this.reason = reason;
/* 48 */     this.source = source;
/* 48 */     this.expires = expires;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBanDto;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String ip() {
/* 48 */     return this.ip;
/*    */   }
/*    */   
/*    */   public Optional<String> reason() {
/* 48 */     return this.reason;
/*    */   }
/*    */   
/*    */   public Optional<String> source() {
/* 48 */     return this.source;
/*    */   }
/*    */   
/*    */   public Optional<Instant> expires() {
/* 48 */     return this.expires;
/*    */   }
/*    */   
/*    */   static {
/* 49 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("ip").forGetter(IpBanDto::ip), (App)Codec.STRING.optionalFieldOf("reason").forGetter(IpBanDto::reason), (App)Codec.STRING.optionalFieldOf("source").forGetter(IpBanDto::source), (App)ExtraCodecs.INSTANT_ISO8601.optionalFieldOf("expires").forGetter(IpBanDto::expires)).apply((Applicative)i, IpBanDto::new));
/*    */   }
/*    */   
/*    */   private static IpBanDto from(IpBanlistService.IpBan ban) {
/* 57 */     return new IpBanDto(ban.ip(), Optional.ofNullable(ban.reason()), Optional.of(ban.source()), ban.expires());
/*    */   }
/*    */   
/*    */   public static IpBanDto from(IpBanListEntry ban) {
/* 61 */     return from(IpBanlistService.IpBan.from(ban));
/*    */   }
/*    */   
/*    */   private IpBanlistService.IpBan toIpBan() {
/* 65 */     return new IpBanlistService.IpBan(ip(), reason().orElse(null), source().orElse("Management server"), expires());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\methods\IpBanlistService$IpBanDto.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */