/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.resources.RegistryDataLoader;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.server.packs.repository.PackRepository;
/*    */ import net.minecraft.server.packs.resources.CloseableResourceManager;
/*    */ import net.minecraft.server.packs.resources.MultiPackResourceManager;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.server.permissions.PermissionSet;
/*    */ import net.minecraft.tags.TagLoader;
/*    */ import net.minecraft.world.level.WorldDataConfiguration;
/*    */ 
/*    */ public class WorldLoader {
/*    */   public static <D, R> CompletableFuture<R> load(InitConfig config, WorldDataSupplier<D> worldDataSupplier, ResultFactory<D, R> resultFactory, Executor backgroundExecutor, Executor mainThreadExecutor) {
/* 27 */     Objects.requireNonNull(config.packConfig);
/* 27 */     return CompletableFuture.supplyAsync(config.packConfig::createResourceManager, mainThreadExecutor)
/* 28 */       .thenComposeAsync(packsAndResourceManager -> {
/*    */           CloseableResourceManager resources = (CloseableResourceManager)packsAndResourceManager.getSecond();
/*    */           LayeredRegistryAccess<RegistryLayer> initialLayers = RegistryLayer.createRegistryAccess();
/*    */           List<Registry.PendingTags<?>> staticLayerTags = TagLoader.loadTagsForExistingRegistries((ResourceManager)resources, (RegistryAccess)initialLayers.getLayer(RegistryLayer.STATIC));
/*    */           RegistryAccess.Frozen worldgenLoadContext = initialLayers.getAccessForLoading(RegistryLayer.WORLDGEN);
/*    */           List<HolderLookup.RegistryLookup<?>> worldgenContextRegistries = TagLoader.buildUpdatedLookups(worldgenLoadContext, staticLayerTags);
/*    */           return RegistryDataLoader.load((ResourceManager)resources, worldgenContextRegistries, RegistryDataLoader.WORLDGEN_REGISTRIES, backgroundExecutor).thenComposeAsync((), backgroundExecutor);
/*    */         }backgroundExecutor);
/*    */   }
/*    */   
/*    */   public static final class DataLoadContext extends Record {
/*    */     private final ResourceManager resources;
/*    */     
/*    */     private final WorldDataConfiguration dataConfiguration;
/*    */     
/*    */     private final HolderLookup.Provider datapackWorldgen;
/*    */     
/*    */     private final RegistryAccess.Frozen datapackDimensions;
/*    */     
/*    */     public DataLoadContext(ResourceManager resources, WorldDataConfiguration dataConfiguration, HolderLookup.Provider datapackWorldgen, RegistryAccess.Frozen datapackDimensions) {
/* 66 */       this.resources = resources;
/* 66 */       this.dataConfiguration = dataConfiguration;
/* 66 */       this.datapackWorldgen = datapackWorldgen;
/* 66 */       this.datapackDimensions = datapackDimensions;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/WorldLoader$DataLoadContext;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$DataLoadContext;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/WorldLoader$DataLoadContext;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$DataLoadContext;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/WorldLoader$DataLoadContext;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/WorldLoader$DataLoadContext;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public ResourceManager resources() {
/* 66 */       return this.resources;
/*    */     }
/*    */     
/*    */     public WorldDataConfiguration dataConfiguration() {
/* 66 */       return this.dataConfiguration;
/*    */     }
/*    */     
/*    */     public HolderLookup.Provider datapackWorldgen() {
/* 66 */       return this.datapackWorldgen;
/*    */     }
/*    */     
/*    */     public RegistryAccess.Frozen datapackDimensions() {
/* 66 */       return this.datapackDimensions;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class DataLoadOutput<D> extends Record {
/*    */     private final D cookie;
/*    */     
/*    */     private final RegistryAccess.Frozen finalDimensions;
/*    */     
/*    */     public DataLoadOutput(D cookie, RegistryAccess.Frozen finalDimensions) {
/* 68 */       this.cookie = cookie;
/* 68 */       this.finalDimensions = finalDimensions;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/WorldLoader$DataLoadOutput;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$DataLoadOutput;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$DataLoadOutput<TD;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/WorldLoader$DataLoadOutput;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$DataLoadOutput;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$DataLoadOutput<TD;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/WorldLoader$DataLoadOutput;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/WorldLoader$DataLoadOutput;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/server/WorldLoader$DataLoadOutput<TD;>;
/*    */     }
/*    */     
/*    */     public D cookie() {
/* 68 */       return this.cookie;
/*    */     }
/*    */     
/*    */     public RegistryAccess.Frozen finalDimensions() {
/* 68 */       return this.finalDimensions;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class PackConfig extends Record {
/*    */     private final PackRepository packRepository;
/*    */     
/*    */     private final WorldDataConfiguration initialDataConfig;
/*    */     
/*    */     private final boolean safeMode;
/*    */     
/*    */     private final boolean initMode;
/*    */     
/*    */     public PackConfig(PackRepository packRepository, WorldDataConfiguration initialDataConfig, boolean safeMode, boolean initMode) {
/* 80 */       this.packRepository = packRepository;
/* 80 */       this.initialDataConfig = initialDataConfig;
/* 80 */       this.safeMode = safeMode;
/* 80 */       this.initMode = initMode;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/WorldLoader$PackConfig;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #80	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$PackConfig;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/WorldLoader$PackConfig;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #80	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$PackConfig;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/WorldLoader$PackConfig;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #80	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/WorldLoader$PackConfig;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public PackRepository packRepository() {
/* 80 */       return this.packRepository;
/*    */     }
/*    */     
/*    */     public WorldDataConfiguration initialDataConfig() {
/* 80 */       return this.initialDataConfig;
/*    */     }
/*    */     
/*    */     public boolean safeMode() {
/* 80 */       return this.safeMode;
/*    */     }
/*    */     
/*    */     public boolean initMode() {
/* 80 */       return this.initMode;
/*    */     }
/*    */     
/*    */     public Pair<WorldDataConfiguration, CloseableResourceManager> createResourceManager() {
/* 87 */       WorldDataConfiguration newPackConfig = MinecraftServer.configurePackRepository(this.packRepository, this.initialDataConfig, this.initMode, this.safeMode);
/* 89 */       List<PackResources> openedPacks = this.packRepository.openAllSelected();
/* 90 */       MultiPackResourceManager multiPackResourceManager = new MultiPackResourceManager(PackType.SERVER_DATA, openedPacks);
/* 91 */       return Pair.of(newPackConfig, multiPackResourceManager);
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class InitConfig extends Record {
/*    */     private final WorldLoader.PackConfig packConfig;
/*    */     
/*    */     private final Commands.CommandSelection commandSelection;
/*    */     
/*    */     private final PermissionSet functionCompilationPermissions;
/*    */     
/*    */     public InitConfig(WorldLoader.PackConfig packConfig, Commands.CommandSelection commandSelection, PermissionSet functionCompilationPermissions) {
/* 95 */       this.packConfig = packConfig;
/* 95 */       this.commandSelection = commandSelection;
/* 95 */       this.functionCompilationPermissions = functionCompilationPermissions;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/WorldLoader$InitConfig;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #95	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$InitConfig;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/WorldLoader$InitConfig;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #95	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$InitConfig;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/WorldLoader$InitConfig;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #95	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/WorldLoader$InitConfig;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public WorldLoader.PackConfig packConfig() {
/* 95 */       return this.packConfig;
/*    */     }
/*    */     
/*    */     public Commands.CommandSelection commandSelection() {
/* 95 */       return this.commandSelection;
/*    */     }
/*    */     
/*    */     public PermissionSet functionCompilationPermissions() {
/* 95 */       return this.functionCompilationPermissions;
/*    */     }
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface WorldDataSupplier<D> {
/*    */     WorldLoader.DataLoadOutput<D> get(WorldLoader.DataLoadContext param1DataLoadContext);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface ResultFactory<D, R> {
/*    */     R create(CloseableResourceManager param1CloseableResourceManager, ReloadableServerResources param1ReloadableServerResources, LayeredRegistryAccess<RegistryLayer> param1LayeredRegistryAccess, D param1D);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\WorldLoader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */