/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import java.util.Date;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.players.IpBanListEntry;
/*    */ 
/*    */ final class IpBan extends Record {
/*    */   private final String ip;
/*    */   
/*    */   private final String reason;
/*    */   
/*    */   private final String source;
/*    */   
/*    */   private final Optional<Instant> expires;
/*    */   
/*    */   private IpBan(String ip, String reason, String source, Optional<Instant> expires) {
/* 69 */     this.ip = ip;
/* 69 */     this.reason = reason;
/* 69 */     this.source = source;
/* 69 */     this.expires = expires;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBan;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #69	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBan;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBan;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #69	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBan;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBan;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #69	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/IpBanlistService$IpBan;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String ip() {
/* 69 */     return this.ip;
/*    */   }
/*    */   
/*    */   public String reason() {
/* 69 */     return this.reason;
/*    */   }
/*    */   
/*    */   public String source() {
/* 69 */     return this.source;
/*    */   }
/*    */   
/*    */   public Optional<Instant> expires() {
/* 69 */     return this.expires;
/*    */   }
/*    */   
/*    */   private static IpBan from(IpBanListEntry entry) {
/* 72 */     return new IpBan(Objects.<String>requireNonNull((String)entry.getUser()), entry.getReason(), entry.getSource(), Optional.<Date>ofNullable(entry.getExpires()).map(Date::toInstant));
/*    */   }
/*    */   
/*    */   private IpBanListEntry toIpBanEntry() {
/* 76 */     return new IpBanListEntry(ip(), null, source(), expires().<Date>map(Date::from).orElse(null), reason());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\methods\IpBanlistService$IpBan.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */