/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ 
/*     */ public final class WorldTypeEntry extends Record {
/*     */   private final Holder<WorldPreset> preset;
/*     */   
/*     */   public WorldTypeEntry(Holder<WorldPreset> preset) {
/* 282 */     this.preset = preset;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #282	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #282	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #282	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Holder<WorldPreset> preset() {
/* 282 */     return this.preset;
/*     */   }
/*     */   
/* 283 */   private static final Component CUSTOM_WORLD_DESCRIPTION = (Component)Component.translatable("generator.custom");
/*     */   
/*     */   public Component describePreset() {
/* 286 */     return Optional.<Holder<WorldPreset>>ofNullable(this.preset).flatMap(Holder::unwrapKey)
/* 287 */       .map(key -> Component.translatable(key.identifier().toLanguageKey("generator")))
/* 288 */       .orElse(CUSTOM_WORLD_DESCRIPTION);
/*     */   }
/*     */   
/*     */   public boolean isAmplified() {
/* 292 */     return Optional.<Holder<WorldPreset>>ofNullable(this.preset).flatMap(Holder::unwrapKey).filter(k -> k.equals(WorldPresets.AMPLIFIED)).isPresent();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\WorldCreationUiState$WorldTypeEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */