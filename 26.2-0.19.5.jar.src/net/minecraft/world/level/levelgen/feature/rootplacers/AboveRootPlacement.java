/*    */ package net.minecraft.world.level.levelgen.feature.rootplacers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public final class AboveRootPlacement extends Record {
/*    */   private final BlockStateProvider aboveRootProvider;
/*    */   
/*    */   private final float aboveRootPlacementChance;
/*    */   
/*    */   public static final Codec<AboveRootPlacement> CODEC;
/*    */   
/*    */   public AboveRootPlacement(BlockStateProvider aboveRootProvider, float aboveRootPlacementChance) {
/*  7 */     this.aboveRootProvider = aboveRootProvider;
/*  7 */     this.aboveRootPlacementChance = aboveRootPlacementChance;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/rootplacers/AboveRootPlacement;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/rootplacers/AboveRootPlacement;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/rootplacers/AboveRootPlacement;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/rootplacers/AboveRootPlacement;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/rootplacers/AboveRootPlacement;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/rootplacers/AboveRootPlacement;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockStateProvider aboveRootProvider() {
/*  7 */     return this.aboveRootProvider;
/*    */   }
/*    */   
/*    */   public float aboveRootPlacementChance() {
/*  7 */     return this.aboveRootPlacementChance;
/*    */   }
/*    */   
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockStateProvider.CODEC.fieldOf("above_root_provider").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("above_root_placement_chance").forGetter(())).apply((Applicative)i, AboveRootPlacement::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\rootplacers\AboveRootPlacement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */