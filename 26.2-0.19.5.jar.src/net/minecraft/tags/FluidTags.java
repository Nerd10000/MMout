/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ public final class FluidTags {
/* 11 */   public static final TagKey<Fluid> WATER = create("water");
/*    */   
/* 12 */   public static final TagKey<Fluid> LAVA = create("lava");
/*    */   
/* 13 */   public static final TagKey<Fluid> SUPPORTS_SUGAR_CANE_ADJACENTLY = create("supports_sugar_cane_adjacently");
/*    */   
/* 14 */   public static final TagKey<Fluid> SUPPORTS_LILY_PAD = create("supports_lily_pad");
/*    */   
/* 15 */   public static final TagKey<Fluid> SUPPORTS_FROGSPAWN = create("supports_frogspawn");
/*    */   
/* 16 */   public static final TagKey<Fluid> BUBBLE_COLUMN_CAN_OCCUPY = create("bubble_column_can_occupy");
/*    */   
/*    */   private static TagKey<Fluid> create(String name) {
/* 19 */     return TagKey.create(Registries.FLUID, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\FluidTags.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */