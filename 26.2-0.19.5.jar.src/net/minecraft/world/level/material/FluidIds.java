/*    */ package net.minecraft.world.level.material;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class FluidIds {
/*  8 */   public static final ResourceKey<Fluid> EMPTY = create("empty");
/*    */   
/*  9 */   public static final ResourceKey<Fluid> FLOWING_WATER = create("flowing_water");
/*    */   
/* 10 */   public static final ResourceKey<Fluid> WATER = create("water");
/*    */   
/* 11 */   public static final ResourceKey<Fluid> FLOWING_LAVA = create("flowing_lava");
/*    */   
/* 12 */   public static final ResourceKey<Fluid> LAVA = create("lava");
/*    */   
/*    */   private static ResourceKey<Fluid> create(String name) {
/* 15 */     return ResourceKey.create(Registries.FLUID, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\material\FluidIds.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */