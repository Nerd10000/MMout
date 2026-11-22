/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ 
/*    */ public final class StructureCheck extends Record implements SpawnCondition {
/*    */   private final HolderSet<Structure> requiredStructures;
/*    */   
/*    */   public static final MapCodec<StructureCheck> MAP_CODEC;
/*    */   
/*    */   public StructureCheck(HolderSet<Structure> requiredStructures) {
/* 10 */     this.requiredStructures = requiredStructures;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/StructureCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/StructureCheck;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/StructureCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/StructureCheck;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/StructureCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/StructureCheck;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HolderSet<Structure> requiredStructures() {
/* 10 */     return this.requiredStructures;
/*    */   }
/*    */   
/*    */   static {
/* 14 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.STRUCTURE).fieldOf("structures").forGetter(StructureCheck::requiredStructures)).apply((Applicative)i, StructureCheck::new));
/*    */   }
/*    */   
/*    */   public boolean test(SpawnContext context) {
/* 20 */     return context.level().getLevel().structureManager().getStructureWithPieceAt(context.pos(), this.requiredStructures).isValid();
/*    */   }
/*    */   
/*    */   public MapCodec<StructureCheck> codec() {
/* 25 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\variant\StructureCheck.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */