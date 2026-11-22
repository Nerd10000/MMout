/*    */ package net.minecraft.world.level.material;
/*    */ 
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class Fluids {
/*  8 */   public static final Fluid EMPTY = register(FluidIds.EMPTY, new EmptyFluid());
/*    */   
/*  9 */   public static final FlowingFluid FLOWING_WATER = register(FluidIds.FLOWING_WATER, new WaterFluid.Flowing());
/*    */   
/* 10 */   public static final FlowingFluid WATER = register(FluidIds.WATER, new WaterFluid.Source());
/*    */   
/* 11 */   public static final FlowingFluid FLOWING_LAVA = register(FluidIds.FLOWING_LAVA, new LavaFluid.Flowing());
/*    */   
/* 12 */   public static final FlowingFluid LAVA = register(FluidIds.LAVA, new LavaFluid.Source());
/*    */   
/*    */   private static <T extends Fluid> T register(ResourceKey<Fluid> id, T fluid) {
/* 15 */     return (T)Registry.register((Registry)BuiltInRegistries.FLUID, id, fluid);
/*    */   }
/*    */   
/*    */   static {
/* 19 */     for (Fluid fluid : BuiltInRegistries.FLUID) {
/* 20 */       for (UnmodifiableIterator<FluidState> unmodifiableIterator = fluid.getStateDefinition().getPossibleStates().iterator(); unmodifiableIterator.hasNext(); ) {
/* 20 */         FluidState state = unmodifiableIterator.next();
/* 21 */         Fluid.FLUID_STATE_REGISTRY.add(state);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\material\Fluids.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */