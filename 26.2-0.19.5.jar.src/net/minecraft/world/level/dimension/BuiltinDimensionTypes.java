/*    */ package net.minecraft.world.level.dimension;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class BuiltinDimensionTypes {
/*  8 */   public static final ResourceKey<DimensionType> OVERWORLD = register("overworld");
/*    */   
/*  9 */   public static final ResourceKey<DimensionType> NETHER = register("the_nether");
/*    */   
/* 10 */   public static final ResourceKey<DimensionType> END = register("the_end");
/*    */   
/* 11 */   public static final ResourceKey<DimensionType> OVERWORLD_CAVES = register("overworld_caves");
/*    */   
/*    */   private static ResourceKey<DimensionType> register(String id) {
/* 14 */     return ResourceKey.create(Registries.DIMENSION_TYPE, Identifier.withDefaultNamespace(id));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\dimension\BuiltinDimensionTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */