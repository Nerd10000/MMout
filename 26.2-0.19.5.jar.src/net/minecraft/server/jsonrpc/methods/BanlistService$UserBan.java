/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import java.util.Date;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.UserBanListEntry;
/*    */ 
/*    */ final class UserBan extends Record {
/*    */   private final NameAndId player;
/*    */   
/*    */   private final String reason;
/*    */   
/*    */   private final String source;
/*    */   
/*    */   private final Optional<Instant> expires;
/*    */   
/*    */   private UserBan(NameAndId player, String reason, String source, Optional<Instant> expires) {
/* 49 */     this.player = player;
/* 49 */     this.reason = reason;
/* 49 */     this.source = source;
/* 49 */     this.expires = expires;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/BanlistService$UserBan;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public NameAndId player() {
/* 49 */     return this.player;
/*    */   }
/*    */   
/*    */   public String reason() {
/* 49 */     return this.reason;
/*    */   }
/*    */   
/*    */   public String source() {
/* 49 */     return this.source;
/*    */   }
/*    */   
/*    */   public Optional<Instant> expires() {
/* 49 */     return this.expires;
/*    */   }
/*    */   
/*    */   private static UserBan from(UserBanListEntry entry) {
/* 51 */     return new UserBan(Objects.<NameAndId>requireNonNull((NameAndId)entry.getUser()), entry.getReason(), entry.getSource(), Optional.<Date>ofNullable(entry.getExpires()).map(Date::toInstant));
/*    */   }
/*    */   
/*    */   private UserBanListEntry toBanEntry() {
/* 55 */     return new UserBanListEntry(new NameAndId(player().id(), player().name()), null, source(), expires().<Date>map(Date::from).orElse(null), reason());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\methods\BanlistService$UserBan.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */