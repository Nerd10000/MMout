/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*    */ import net.minecraft.server.permissions.PermissionLevel;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.ServerOpListEntry;
/*    */ 
/*    */ public final class OperatorDto extends Record {
/*    */   private final PlayerDto player;
/*    */   
/*    */   private final Optional<PermissionLevel> permissionLevel;
/*    */   
/*    */   private final Optional<Boolean> bypassesPlayerLimit;
/*    */   
/*    */   public static final MapCodec<OperatorDto> CODEC;
/*    */   
/*    */   public OperatorDto(PlayerDto player, Optional<PermissionLevel> permissionLevel, Optional<Boolean> bypassesPlayerLimit) {
/* 22 */     this.player = player;
/* 22 */     this.permissionLevel = permissionLevel;
/* 22 */     this.bypassesPlayerLimit = bypassesPlayerLimit;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public PlayerDto player() {
/* 22 */     return this.player;
/*    */   }
/*    */   
/*    */   public Optional<PermissionLevel> permissionLevel() {
/* 22 */     return this.permissionLevel;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> bypassesPlayerLimit() {
/* 22 */     return this.bypassesPlayerLimit;
/*    */   }
/*    */   
/*    */   static {
/* 23 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)PlayerDto.CODEC.codec().fieldOf("player").forGetter(OperatorDto::player), (App)PermissionLevel.INT_CODEC.optionalFieldOf("permissionLevel").forGetter(OperatorDto::permissionLevel), (App)Codec.BOOL.optionalFieldOf("bypassesPlayerLimit").forGetter(OperatorDto::bypassesPlayerLimit)).apply((Applicative)i, OperatorDto::new));
/*    */   }
/*    */   
/*    */   public static OperatorDto from(ServerOpListEntry serverOpListEntry) {
/* 30 */     return new OperatorDto(PlayerDto.from(
/* 31 */           Objects.<NameAndId>requireNonNull((NameAndId)serverOpListEntry.getUser())), 
/* 32 */         Optional.of(serverOpListEntry.permissions().level()), 
/* 33 */         Optional.of(Boolean.valueOf(serverOpListEntry.getBypassesPlayerLimit())));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\methods\OperatorService$OperatorDto.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */