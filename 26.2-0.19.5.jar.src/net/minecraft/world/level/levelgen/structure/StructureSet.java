/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
/*    */ 
/*    */ public final class StructureSet extends Record {
/*    */   private final List<StructureSelectionEntry> structures;
/*    */   
/*    */   private final StructurePlacement placement;
/*    */   
/*    */   public static final Codec<StructureSet> DIRECT_CODEC;
/*    */   
/*    */   public StructureSet(List<StructureSelectionEntry> structures, StructurePlacement placement) {
/* 14 */     this.structures = structures;
/* 14 */     this.placement = placement;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/StructureSet;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSet;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/StructureSet;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSet;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/StructureSet;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSet;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<StructureSelectionEntry> structures() {
/* 14 */     return this.structures;
/*    */   }
/*    */   
/*    */   public StructurePlacement placement() {
/* 14 */     return this.placement;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)StructureSelectionEntry.CODEC.listOf().fieldOf("structures").forGetter(StructureSet::structures), (App)StructurePlacement.CODEC.fieldOf("placement").forGetter(StructureSet::placement)).apply((Applicative)i, StructureSet::new));
/*    */   }
/*    */   
/* 22 */   public static final Codec<Holder<StructureSet>> CODEC = (Codec<Holder<StructureSet>>)RegistryFileCodec.create(Registries.STRUCTURE_SET, DIRECT_CODEC);
/*    */   
/*    */   public StructureSet(Holder<Structure> singleEntry, StructurePlacement placement) {
/* 25 */     this(List.of(new StructureSelectionEntry(singleEntry, 1)), placement);
/*    */   }
/*    */   
/*    */   public static final class StructureSelectionEntry extends Record {
/*    */     private final Holder<Structure> structure;
/*    */     
/*    */     private final int weight;
/*    */     
/*    */     public static final Codec<StructureSelectionEntry> CODEC;
/*    */     
/*    */     public StructureSelectionEntry(Holder<Structure> structure, int weight) {
/* 28 */       this.structure = structure;
/* 28 */       this.weight = weight;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/StructureSet$StructureSelectionEntry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSet$StructureSelectionEntry;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/StructureSet$StructureSelectionEntry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSet$StructureSelectionEntry;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/StructureSet$StructureSelectionEntry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSet$StructureSelectionEntry;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Holder<Structure> structure() {
/* 28 */       return this.structure;
/*    */     }
/*    */     
/*    */     public int weight() {
/* 28 */       return this.weight;
/*    */     }
/*    */     
/*    */     static {
/* 32 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Structure.CODEC.fieldOf("structure").forGetter(StructureSelectionEntry::structure), (App)ExtraCodecs.POSITIVE_INT.fieldOf("weight").forGetter(StructureSelectionEntry::weight)).apply((Applicative)i, StructureSelectionEntry::new));
/*    */     }
/*    */   }
/*    */   
/*    */   public static StructureSelectionEntry entry(Holder<Structure> structure, int weight) {
/* 39 */     return new StructureSelectionEntry(structure, weight);
/*    */   }
/*    */   
/*    */   public static StructureSelectionEntry entry(Holder<Structure> structure) {
/* 43 */     return new StructureSelectionEntry(structure, 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\StructureSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */