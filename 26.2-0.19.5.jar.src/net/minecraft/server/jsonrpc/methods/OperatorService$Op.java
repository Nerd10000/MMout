/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.permissions.PermissionLevel;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ 
/*    */ final class Op extends Record {
/*    */   private final NameAndId user;
/*    */   
/*    */   private final Optional<PermissionLevel> permissionLevel;
/*    */   
/*    */   private final Optional<Boolean> bypassesPlayerLimit;
/*    */   
/*    */   private Op(NameAndId user, Optional<PermissionLevel> permissionLevel, Optional<Boolean> bypassesPlayerLimit) {
/* 60 */     this.user = user;
/* 60 */     this.permissionLevel = permissionLevel;
/* 60 */     this.bypassesPlayerLimit = bypassesPlayerLimit;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/OperatorService$Op;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #60	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/OperatorService$Op;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/OperatorService$Op;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #60	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/OperatorService$Op;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/OperatorService$Op;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #60	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/OperatorService$Op;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public NameAndId user() {
/* 60 */     return this.user;
/*    */   }
/*    */   
/*    */   public Optional<PermissionLevel> permissionLevel() {
/* 60 */     return this.permissionLevel;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> bypassesPlayerLimit() {
/* 60 */     return this.bypassesPlayerLimit;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\methods\OperatorService$Op.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */