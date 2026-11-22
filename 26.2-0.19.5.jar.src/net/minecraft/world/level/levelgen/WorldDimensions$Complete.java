/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.storage.PrimaryLevelData;
/*     */ 
/*     */ public final class Complete extends Record {
/*     */   private final Registry<LevelStem> dimensions;
/*     */   
/*     */   private final PrimaryLevelData.SpecialWorldProperty specialWorldProperty;
/*     */   
/*     */   public Complete(Registry<LevelStem> dimensions, PrimaryLevelData.SpecialWorldProperty specialWorldProperty) {
/* 198 */     this.dimensions = dimensions;
/* 198 */     this.specialWorldProperty = specialWorldProperty;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #198	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #198	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #198	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Registry<LevelStem> dimensions() {
/* 198 */     return this.dimensions;
/*     */   }
/*     */   
/*     */   public PrimaryLevelData.SpecialWorldProperty specialWorldProperty() {
/* 198 */     return this.specialWorldProperty;
/*     */   }
/*     */   
/*     */   public Lifecycle lifecycle() {
/* 203 */     return this.dimensions.registryLifecycle();
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen dimensionsRegistryAccess() {
/* 207 */     return (new RegistryAccess.ImmutableRegistryAccess(List.of(this.dimensions))).freeze();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\WorldDimensions$Complete.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */