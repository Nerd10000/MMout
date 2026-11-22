/*    */ package net.minecraft.client.gui.screens.worldselection;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.server.RegistryLayer;
/*    */ import net.minecraft.server.ReloadableServerResources;
/*    */ import net.minecraft.world.level.WorldDataConfiguration;
/*    */ import net.minecraft.world.level.dimension.LevelStem;
/*    */ import net.minecraft.world.level.gamerules.GameRuleMap;
/*    */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*    */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*    */ import net.minecraft.world.level.levelgen.WorldOptions;
/*    */ 
/*    */ public final class WorldCreationContext extends Record {
/*    */   private final WorldOptions options;
/*    */   
/*    */   private final Registry<LevelStem> datapackDimensions;
/*    */   
/*    */   private final WorldDimensions selectedDimensions;
/*    */   
/*    */   private final LayeredRegistryAccess<RegistryLayer> worldgenRegistries;
/*    */   
/*    */   private final ReloadableServerResources dataPackResources;
/*    */   
/*    */   private final WorldDataConfiguration dataConfiguration;
/*    */   
/*    */   private final InitialWorldCreationOptions initialWorldCreationOptions;
/*    */   
/*    */   public WorldCreationContext(WorldOptions options, Registry<LevelStem> datapackDimensions, WorldDimensions selectedDimensions, LayeredRegistryAccess<RegistryLayer> worldgenRegistries, ReloadableServerResources dataPackResources, WorldDataConfiguration dataConfiguration, InitialWorldCreationOptions initialWorldCreationOptions) {
/* 19 */     this.options = options;
/* 19 */     this.datapackDimensions = datapackDimensions;
/* 19 */     this.selectedDimensions = selectedDimensions;
/* 19 */     this.worldgenRegistries = worldgenRegistries;
/* 19 */     this.dataPackResources = dataPackResources;
/* 19 */     this.dataConfiguration = dataConfiguration;
/* 19 */     this.initialWorldCreationOptions = initialWorldCreationOptions;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/WorldCreationContext;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldCreationContext;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/WorldCreationContext;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldCreationContext;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/WorldCreationContext;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldCreationContext;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public WorldOptions options() {
/* 19 */     return this.options;
/*    */   }
/*    */   
/*    */   public Registry<LevelStem> datapackDimensions() {
/* 19 */     return this.datapackDimensions;
/*    */   }
/*    */   
/*    */   public WorldDimensions selectedDimensions() {
/* 19 */     return this.selectedDimensions;
/*    */   }
/*    */   
/*    */   public LayeredRegistryAccess<RegistryLayer> worldgenRegistries() {
/* 19 */     return this.worldgenRegistries;
/*    */   }
/*    */   
/*    */   public ReloadableServerResources dataPackResources() {
/* 19 */     return this.dataPackResources;
/*    */   }
/*    */   
/*    */   public WorldDataConfiguration dataConfiguration() {
/* 19 */     return this.dataConfiguration;
/*    */   }
/*    */   
/*    */   public InitialWorldCreationOptions initialWorldCreationOptions() {
/* 19 */     return this.initialWorldCreationOptions;
/*    */   }
/*    */   
/*    */   public WorldCreationContext(WorldGenSettings worldGenSettings, LayeredRegistryAccess<RegistryLayer> loadedRegistries, ReloadableServerResources dataPackResources, WorldDataConfiguration dataConfiguration) {
/* 29 */     this(worldGenSettings
/* 30 */         .options(), worldGenSettings
/* 31 */         .dimensions(), loadedRegistries, dataPackResources, dataConfiguration, new InitialWorldCreationOptions(WorldCreationUiState.SelectedGameMode.SURVIVAL, 
/*    */           
/* 35 */           GameRuleMap.of(), null));
/*    */   }
/*    */   
/*    */   public WorldCreationContext(WorldOptions worldOptions, WorldDimensions worldDimensions, LayeredRegistryAccess<RegistryLayer> loadedRegistries, ReloadableServerResources dataPackResources, WorldDataConfiguration dataConfiguration, InitialWorldCreationOptions initialWorldCreationOptions) {
/* 40 */     this(worldOptions, loadedRegistries
/*    */         
/* 42 */         .getLayer(RegistryLayer.DIMENSIONS).lookupOrThrow(Registries.LEVEL_STEM), worldDimensions, loadedRegistries
/*    */         
/* 44 */         .replaceFrom(RegistryLayer.DIMENSIONS, new RegistryAccess.Frozen[0]), dataPackResources, dataConfiguration, initialWorldCreationOptions);
/*    */   }
/*    */   
/*    */   public WorldCreationContext withSettings(WorldOptions options, WorldDimensions dimensions) {
/* 52 */     return new WorldCreationContext(options, this.datapackDimensions, dimensions, this.worldgenRegistries, this.dataPackResources, this.dataConfiguration, this.initialWorldCreationOptions);
/*    */   }
/*    */   
/*    */   public WorldCreationContext withOptions(OptionsModifier modifier) {
/* 59 */     return new WorldCreationContext(modifier.apply(this.options), this.datapackDimensions, this.selectedDimensions, this.worldgenRegistries, this.dataPackResources, this.dataConfiguration, this.initialWorldCreationOptions);
/*    */   }
/*    */   
/*    */   public WorldCreationContext withDimensions(DimensionsUpdater modifier) {
/* 66 */     return new WorldCreationContext(this.options, this.datapackDimensions, modifier.apply(worldgenLoadContext(), this.selectedDimensions), this.worldgenRegistries, this.dataPackResources, this.dataConfiguration, this.initialWorldCreationOptions);
/*    */   }
/*    */   
/*    */   public RegistryAccess.Frozen worldgenLoadContext() {
/* 70 */     return this.worldgenRegistries.compositeAccess();
/*    */   }
/*    */   
/*    */   public void validate() {
/* 74 */     for (LevelStem stem : datapackDimensions())
/* 75 */       stem.generator().validate(); 
/*    */   }
/*    */   
/*    */   public static interface OptionsModifier extends UnaryOperator<WorldOptions> {}
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface DimensionsUpdater extends BiFunction<RegistryAccess.Frozen, WorldDimensions, WorldDimensions> {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\WorldCreationContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */