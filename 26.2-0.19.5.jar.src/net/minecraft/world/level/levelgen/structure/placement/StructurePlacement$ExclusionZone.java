/*    */ package net.minecraft.world.level.levelgen.structure.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*    */ 
/*    */ @Deprecated
/*    */ public final class ExclusionZone extends Record {
/*    */   private final Holder<StructureSet> otherSet;
/*    */   
/*    */   private final int chunkCount;
/*    */   
/*    */   public static final Codec<ExclusionZone> CODEC;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<StructureSet> otherSet() {
/* 37 */     return this.otherSet;
/*    */   }
/*    */   
/*    */   public int chunkCount() {
/* 37 */     return this.chunkCount;
/*    */   }
/*    */   
/*    */   public ExclusionZone(Holder<StructureSet> otherSet, int chunkCount) {
/* 38 */     this.otherSet = otherSet;
/* 38 */     this.chunkCount = chunkCount;
/*    */   }
/*    */   
/*    */   static {
/* 39 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryFileCodec.create(Registries.STRUCTURE_SET, StructureSet.DIRECT_CODEC, false).fieldOf("other_set").forGetter(ExclusionZone::otherSet), (App)Codec.intRange(1, 16).fieldOf("chunk_count").forGetter(ExclusionZone::chunkCount)).apply((Applicative)i, ExclusionZone::new));
/*    */   }
/*    */   
/*    */   private boolean isPlacementForbidden(ChunkGeneratorStructureState state, int sourceX, int sourceZ) {
/* 46 */     return state.hasStructureChunkInRange(this.otherSet, sourceX, sourceZ, this.chunkCount);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\placement\StructurePlacement$ExclusionZone.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */