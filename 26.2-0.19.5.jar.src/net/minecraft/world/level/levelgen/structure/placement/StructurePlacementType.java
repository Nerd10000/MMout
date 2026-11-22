/*    */ package net.minecraft.world.level.levelgen.structure.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface StructurePlacementType<SP extends StructurePlacement> {
/*  8 */   public static final StructurePlacementType<RandomSpreadStructurePlacement> RANDOM_SPREAD = register("random_spread", RandomSpreadStructurePlacement.CODEC);
/*    */   
/*  9 */   public static final StructurePlacementType<ConcentricRingsStructurePlacement> CONCENTRIC_RINGS = register("concentric_rings", ConcentricRingsStructurePlacement.CODEC);
/*    */   
/*    */   MapCodec<SP> codec();
/*    */   
/*    */   private static <SP extends StructurePlacement> StructurePlacementType<SP> register(String id, MapCodec<SP> codec) {
/* 14 */     return (StructurePlacementType<SP>)Registry.register(BuiltInRegistries.STRUCTURE_PLACEMENT, id, () -> codec);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\placement\StructurePlacementType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */