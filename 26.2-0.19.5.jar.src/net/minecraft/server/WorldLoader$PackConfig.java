/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.server.packs.repository.PackRepository;
/*    */ import net.minecraft.server.packs.resources.CloseableResourceManager;
/*    */ import net.minecraft.server.packs.resources.MultiPackResourceManager;
/*    */ import net.minecraft.world.level.WorldDataConfiguration;
/*    */ 
/*    */ public final class PackConfig extends Record {
/*    */   private final PackRepository packRepository;
/*    */   
/*    */   private final WorldDataConfiguration initialDataConfig;
/*    */   
/*    */   private final boolean safeMode;
/*    */   
/*    */   private final boolean initMode;
/*    */   
/*    */   public PackConfig(PackRepository packRepository, WorldDataConfiguration initialDataConfig, boolean safeMode, boolean initMode) {
/* 80 */     this.packRepository = packRepository;
/* 80 */     this.initialDataConfig = initialDataConfig;
/* 80 */     this.safeMode = safeMode;
/* 80 */     this.initMode = initMode;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/WorldLoader$PackConfig;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #80	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/WorldLoader$PackConfig;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/WorldLoader$PackConfig;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #80	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/WorldLoader$PackConfig;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/WorldLoader$PackConfig;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #80	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/WorldLoader$PackConfig;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public PackRepository packRepository() {
/* 80 */     return this.packRepository;
/*    */   }
/*    */   
/*    */   public WorldDataConfiguration initialDataConfig() {
/* 80 */     return this.initialDataConfig;
/*    */   }
/*    */   
/*    */   public boolean safeMode() {
/* 80 */     return this.safeMode;
/*    */   }
/*    */   
/*    */   public boolean initMode() {
/* 80 */     return this.initMode;
/*    */   }
/*    */   
/*    */   public Pair<WorldDataConfiguration, CloseableResourceManager> createResourceManager() {
/* 87 */     WorldDataConfiguration newPackConfig = MinecraftServer.configurePackRepository(this.packRepository, this.initialDataConfig, this.initMode, this.safeMode);
/* 89 */     List<PackResources> openedPacks = this.packRepository.openAllSelected();
/* 90 */     MultiPackResourceManager multiPackResourceManager = new MultiPackResourceManager(PackType.SERVER_DATA, openedPacks);
/* 91 */     return Pair.of(newPackConfig, multiPackResourceManager);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\WorldLoader$PackConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */