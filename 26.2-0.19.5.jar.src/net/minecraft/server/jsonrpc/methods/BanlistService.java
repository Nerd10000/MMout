/*     */ package net.minecraft.server.jsonrpc.methods;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.time.Instant;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.UserBanListEntry;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class BanlistService {
/*     */   private static final String BAN_SOURCE = "Management server";
/*     */   
/*     */   public static final class UserBanDto extends Record {
/*     */     private final PlayerDto player;
/*     */     
/*     */     private final Optional<String> reason;
/*     */     
/*     */     private final Optional<String> source;
/*     */     
/*     */     private final Optional<Instant> expires;
/*     */     
/*     */     public static final MapCodec<UserBanDto> CODEC;
/*     */     
/*     */     public UserBanDto(PlayerDto player, Optional<String> reason, Optional<String> source, Optional<Instant> expires) {
/*  28 */       this.player = player;
/*  28 */       this.reason = reason;
/*  28 */       this.source = source;
/*  28 */       this.expires = expires;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBanDto;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public PlayerDto player() {
/*  28 */       return this.player;
/*     */     }
/*     */     
/*     */     public Optional<String> reason() {
/*  28 */       return this.reason;
/*     */     }
/*     */     
/*     */     public Optional<String> source() {
/*  28 */       return this.source;
/*     */     }
/*     */     
/*     */     public Optional<Instant> expires() {
/*  28 */       return this.expires;
/*     */     }
/*     */     
/*     */     static {
/*  29 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)PlayerDto.CODEC.codec().fieldOf("player").forGetter(UserBanDto::player), (App)Codec.STRING.optionalFieldOf("reason").forGetter(UserBanDto::reason), (App)Codec.STRING.optionalFieldOf("source").forGetter(UserBanDto::source), (App)ExtraCodecs.INSTANT_ISO8601.optionalFieldOf("expires").forGetter(UserBanDto::expires)).apply((Applicative)i, UserBanDto::new));
/*     */     }
/*     */     
/*     */     private static UserBanDto from(BanlistService.UserBan ban) {
/*  37 */       return new UserBanDto(PlayerDto.from(ban.player()), Optional.ofNullable(ban.reason()), Optional.of(ban.source()), ban.expires());
/*     */     }
/*     */     
/*     */     public static UserBanDto from(UserBanListEntry entry) {
/*  41 */       return from(BanlistService.UserBan.from(entry));
/*     */     }
/*     */     
/*     */     private BanlistService.UserBan toUserBan(NameAndId nameAndId) {
/*  45 */       return new BanlistService.UserBan(nameAndId, reason().orElse(null), source().orElse("Management server"), expires());
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class UserBan extends Record {
/*     */     private final NameAndId player;
/*     */     
/*     */     private final String reason;
/*     */     
/*     */     private final String source;
/*     */     
/*     */     private final Optional<Instant> expires;
/*     */     
/*     */     private UserBan(NameAndId player, String reason, String source, Optional<Instant> expires) {
/*  49 */       this.player = player;
/*  49 */       this.reason = reason;
/*  49 */       this.source = source;
/*  49 */       this.expires = expires;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #49	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #49	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #49	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public NameAndId player() {
/*  49 */       return this.player;
/*     */     }
/*     */     
/*     */     public String reason() {
/*  49 */       return this.reason;
/*     */     }
/*     */     
/*     */     public String source() {
/*  49 */       return this.source;
/*     */     }
/*     */     
/*     */     public Optional<Instant> expires() {
/*  49 */       return this.expires;
/*     */     }
/*     */     
/*     */     private static UserBan from(UserBanListEntry entry) {
/*  51 */       return new UserBan(Objects.<NameAndId>requireNonNull((NameAndId)entry.getUser()), entry.getReason(), entry.getSource(), Optional.<Date>ofNullable(entry.getExpires()).map(Date::toInstant));
/*     */     }
/*     */     
/*     */     private UserBanListEntry toBanEntry() {
/*  55 */       return new UserBanListEntry(new NameAndId(player().id(), player().name()), null, source(), expires().<Date>map(Date::from).orElse(null), reason());
/*     */     }
/*     */   }
/*     */   
/*     */   public static List<UserBanDto> get(MinecraftApi minecraftApi) {
/*  60 */     return minecraftApi.banListService().getUserBanEntries().stream()
/*  61 */       .filter(p -> (p.getUser() != null))
/*  62 */       .map(UserBan::from)
/*  63 */       .map(UserBanDto::from)
/*  64 */       .toList();
/*     */   }
/*     */   
/*     */   public static List<UserBanDto> add(MinecraftApi minecraftApi, List<UserBanDto> bans, ClientInfo clientInfo) {
/*  71 */     List<CompletableFuture<Optional<UserBan>>> fetch = bans.stream().map(ban -> minecraftApi.playerListService().getUser(ban.player().id(), ban.player().name()).thenApply(())).toList();
/*  73 */     for (Optional<UserBan> ban : (Iterable<Optional<UserBan>>)Util.sequence(fetch).join()) {
/*  74 */       if (ban.isEmpty())
/*     */         continue; 
/*  77 */       UserBan userBan = ban.get();
/*  78 */       minecraftApi.banListService().addUserBan(userBan.toBanEntry(), clientInfo);
/*  79 */       ServerPlayer player = minecraftApi.playerListService().getPlayer(((UserBan)ban.get()).player().id());
/*  80 */       if (player != null)
/*  81 */         player.connection.disconnect((Component)Component.translatable("multiplayer.disconnect.banned")); 
/*     */     } 
/*  85 */     return get(minecraftApi);
/*     */   }
/*     */   
/*     */   public static List<UserBanDto> clear(MinecraftApi minecraftApi, ClientInfo clientInfo) {
/*  89 */     minecraftApi.banListService().clearUserBans(clientInfo);
/*  90 */     return get(minecraftApi);
/*     */   }
/*     */   
/*     */   public static List<UserBanDto> remove(MinecraftApi minecraftApi, List<PlayerDto> remove, ClientInfo clientInfo) {
/*  96 */     List<CompletableFuture<Optional<NameAndId>>> fetch = remove.stream().map(playerDto -> minecraftApi.playerListService().getUser(playerDto.id(), playerDto.name())).toList();
/*  98 */     for (Optional<NameAndId> user : (Iterable<Optional<NameAndId>>)Util.sequence(fetch).join()) {
/*  99 */       if (user.isEmpty())
/*     */         continue; 
/* 102 */       minecraftApi.banListService().removeUserBan(user.get(), clientInfo);
/*     */     } 
/* 104 */     return get(minecraftApi);
/*     */   }
/*     */   
/*     */   public static List<UserBanDto> set(MinecraftApi minecraftApi, List<UserBanDto> bans, ClientInfo clientInfo) {
/* 111 */     List<CompletableFuture<Optional<UserBan>>> fetch = bans.stream().map(ban -> minecraftApi.playerListService().getUser(ban.player().id(), ban.player().name()).thenApply(())).toList();
/* 115 */     Set<UserBan> finalAllowList = (Set<UserBan>)((List)Util.sequence(fetch).join()).stream().flatMap(Optional::stream).collect(Collectors.toSet());
/* 120 */     Set<UserBan> currentAllowList = (Set<UserBan>)minecraftApi.banListService().getUserBanEntries().stream().filter(entry -> (entry.getUser() != null)).map(UserBan::from).collect(Collectors.toSet());
/* 122 */     currentAllowList.stream()
/* 123 */       .filter(ban -> !finalAllowList.contains(ban))
/* 124 */       .forEach(ban -> minecraftApi.banListService().removeUserBan(ban.player(), clientInfo));
/* 126 */     finalAllowList.stream()
/* 127 */       .filter(ban -> !currentAllowList.contains(ban))
/* 128 */       .forEach(ban -> {
/*     */           minecraftApi.banListService().addUserBan(ban.toBanEntry(), clientInfo);
/*     */           ServerPlayer player = minecraftApi.playerListService().getPlayer(ban.player().id());
/*     */           if (player != null)
/*     */             player.connection.disconnect((Component)Component.translatable("multiplayer.disconnect.banned")); 
/*     */         });
/* 136 */     return get(minecraftApi);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\methods\BanlistService.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */