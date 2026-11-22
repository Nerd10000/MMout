/*   */ package net.minecraft.client.gui.screens.worldselection;
/*   */ 
/*   */ import net.minecraft.resources.ResourceKey;
/*   */ import net.minecraft.world.level.gamerules.GameRuleMap;
/*   */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
/*   */ 
/*   */ public final class InitialWorldCreationOptions extends Record {
/*   */   private final WorldCreationUiState.SelectedGameMode selectedGameMode;
/*   */   
/*   */   private final GameRuleMap gameRuleOverwrites;
/*   */   
/*   */   private final ResourceKey<FlatLevelGeneratorPreset> flatLevelPreset;
/*   */   
/*   */   public InitialWorldCreationOptions(WorldCreationUiState.SelectedGameMode selectedGameMode, GameRuleMap gameRuleOverwrites, ResourceKey<FlatLevelGeneratorPreset> flatLevelPreset) {
/* 8 */     this.selectedGameMode = selectedGameMode;
/* 8 */     this.gameRuleOverwrites = gameRuleOverwrites;
/* 8 */     this.flatLevelPreset = flatLevelPreset;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/InitialWorldCreationOptions;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/InitialWorldCreationOptions;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/InitialWorldCreationOptions;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/InitialWorldCreationOptions;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/InitialWorldCreationOptions;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/InitialWorldCreationOptions;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public WorldCreationUiState.SelectedGameMode selectedGameMode() {
/* 8 */     return this.selectedGameMode;
/*   */   }
/*   */   
/*   */   public GameRuleMap gameRuleOverwrites() {
/* 8 */     return this.gameRuleOverwrites;
/*   */   }
/*   */   
/*   */   public ResourceKey<FlatLevelGeneratorPreset> flatLevelPreset() {
/* 8 */     return this.flatLevelPreset;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\InitialWorldCreationOptions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */