/*      */ package net.minecraft.server;
/*      */ 
/*      */ import net.minecraft.server.packs.resources.CloseableResourceManager;
/*      */ 
/*      */ final class ReloadableResources extends Record implements AutoCloseable {
/*      */   private final CloseableResourceManager resourceManager;
/*      */   
/*      */   private final ReloadableServerResources managers;
/*      */   
/*      */   private ReloadableResources(CloseableResourceManager resourceManager, ReloadableServerResources managers) {
/* 2171 */     this.resourceManager = resourceManager;
/* 2171 */     this.managers = managers;
/*      */   }
/*      */   
/*      */   public final String toString() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/MinecraftServer$ReloadableResources;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2171	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/server/MinecraftServer$ReloadableResources;
/*      */   }
/*      */   
/*      */   public final int hashCode() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/MinecraftServer$ReloadableResources;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2171	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/server/MinecraftServer$ReloadableResources;
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/MinecraftServer$ReloadableResources;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2171	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/server/MinecraftServer$ReloadableResources;
/*      */     //   0	8	1	o	Ljava/lang/Object;
/*      */   }
/*      */   
/*      */   public CloseableResourceManager resourceManager() {
/* 2171 */     return this.resourceManager;
/*      */   }
/*      */   
/*      */   public ReloadableServerResources managers() {
/* 2171 */     return this.managers;
/*      */   }
/*      */   
/*      */   public void close() {
/* 2174 */     this.resourceManager.close();
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\MinecraftServer$ReloadableResources.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */