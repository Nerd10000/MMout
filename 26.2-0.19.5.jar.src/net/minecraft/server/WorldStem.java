/*    */ package net.minecraft.server;
/*    */ 
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.server.packs.resources.CloseableResourceManager;
/*    */ import net.minecraft.world.level.storage.LevelDataAndDimensions;
/*    */ 
/*    */ public final class WorldStem extends Record implements AutoCloseable {
/*    */   private final CloseableResourceManager resourceManager;
/*    */   
/*    */   private final ReloadableServerResources dataPackResources;
/*    */   
/*    */   private final LayeredRegistryAccess<RegistryLayer> registries;
/*    */   
/*    */   private final LevelDataAndDimensions.WorldDataAndGenSettings worldDataAndGenSettings;
/*    */   
/*    */   public WorldStem(CloseableResourceManager resourceManager, ReloadableServerResources dataPackResources, LayeredRegistryAccess<RegistryLayer> registries, LevelDataAndDimensions.WorldDataAndGenSettings worldDataAndGenSettings) {
/*  7 */     this.resourceManager = resourceManager;
/*  7 */     this.dataPackResources = dataPackResources;
/*  7 */     this.registries = registries;
/*  7 */     this.worldDataAndGenSettings = worldDataAndGenSettings;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/WorldStem;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/WorldStem;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/WorldStem;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/WorldStem;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/WorldStem;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/WorldStem;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public CloseableResourceManager resourceManager() {
/*  7 */     return this.resourceManager;
/*    */   }
/*    */   
/*    */   public ReloadableServerResources dataPackResources() {
/*  7 */     return this.dataPackResources;
/*    */   }
/*    */   
/*    */   public LayeredRegistryAccess<RegistryLayer> registries() {
/*  7 */     return this.registries;
/*    */   }
/*    */   
/*    */   public LevelDataAndDimensions.WorldDataAndGenSettings worldDataAndGenSettings() {
/*  7 */     return this.worldDataAndGenSettings;
/*    */   }
/*    */   
/*    */   public void close() {
/* 15 */     this.resourceManager.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\WorldStem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */