/*    */ package net.minecraft.server.jsonrpc.api;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ 
/*    */ public final class PlayerDto extends Record {
/*    */   private final Optional<UUID> id;
/*    */   
/*    */   private final Optional<String> name;
/*    */   
/*    */   public static final MapCodec<PlayerDto> CODEC;
/*    */   
/*    */   public PlayerDto(Optional<UUID> id, Optional<String> name) {
/* 14 */     this.id = id;
/* 14 */     this.name = name;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/api/PlayerDto;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/PlayerDto;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/api/PlayerDto;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/PlayerDto;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/api/PlayerDto;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/PlayerDto;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<UUID> id() {
/* 14 */     return this.id;
/*    */   }
/*    */   
/*    */   public Optional<String> name() {
/* 14 */     return this.name;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)UUIDUtil.STRING_CODEC.optionalFieldOf("id").forGetter(PlayerDto::id), (App)Codec.STRING.optionalFieldOf("name").forGetter(PlayerDto::name)).apply((Applicative)i, PlayerDto::new));
/*    */   }
/*    */   
/*    */   public static PlayerDto from(GameProfile gameProfile) {
/* 22 */     return new PlayerDto(Optional.of(gameProfile.id()), Optional.of(gameProfile.name()));
/*    */   }
/*    */   
/*    */   public static PlayerDto from(NameAndId nameAndId) {
/* 26 */     return new PlayerDto(Optional.of(nameAndId.id()), Optional.of(nameAndId.name()));
/*    */   }
/*    */   
/*    */   public static PlayerDto from(ServerPlayer player) {
/* 30 */     GameProfile gameProfile = player.getGameProfile();
/* 31 */     return from(gameProfile);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\api\PlayerDto.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */