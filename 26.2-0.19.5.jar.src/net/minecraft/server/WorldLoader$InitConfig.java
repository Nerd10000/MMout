/*    */ package net.minecraft.server;
/*    */ 
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.server.permissions.PermissionSet;
/*    */ 
/*    */ public final class InitConfig extends Record {
/*    */   private final WorldLoader.PackConfig packConfig;
/*    */   
/*    */   private final Commands.CommandSelection commandSelection;
/*    */   
/*    */   private final PermissionSet functionCompilationPermissions;
/*    */   
/*    */   public InitConfig(WorldLoader.PackConfig packConfig, Commands.CommandSelection commandSelection, PermissionSet functionCompilationPermissions) {
/* 95 */     this.packConfig = packConfig;
/* 95 */     this.commandSelection = commandSelection;
/* 95 */     this.functionCompilationPermissions = functionCompilationPermissions;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/WorldLoader$InitConfig;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #95	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/WorldLoader$InitConfig;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/WorldLoader$InitConfig;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #95	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/WorldLoader$InitConfig;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/WorldLoader$InitConfig;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #95	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/WorldLoader$InitConfig;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public WorldLoader.PackConfig packConfig() {
/* 95 */     return this.packConfig;
/*    */   }
/*    */   
/*    */   public Commands.CommandSelection commandSelection() {
/* 95 */     return this.commandSelection;
/*    */   }
/*    */   
/*    */   public PermissionSet functionCompilationPermissions() {
/* 95 */     return this.functionCompilationPermissions;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\WorldLoader$InitConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */