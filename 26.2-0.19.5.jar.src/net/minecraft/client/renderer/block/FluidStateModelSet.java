/*    */ package net.minecraft.client.renderer.block;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.color.block.BlockTintSources;
/*    */ import net.minecraft.client.resources.model.sprite.Material;
/*    */ import net.minecraft.client.resources.model.sprite.MaterialBaker;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class FluidStateModelSet {
/* 14 */   private static final FluidModel.Unbaked WATER_MODEL = new FluidModel.Unbaked(new Material(
/* 15 */         Identifier.withDefaultNamespace("block/water_still")), new Material(
/* 16 */         Identifier.withDefaultNamespace("block/water_flow")), new Material(
/* 17 */         Identifier.withDefaultNamespace("block/water_overlay")), 
/* 18 */       BlockTintSources.water());
/*    */   
/* 20 */   private static final FluidModel.Unbaked LAVA_MODEL = new FluidModel.Unbaked(new Material(
/* 21 */         Identifier.withDefaultNamespace("block/lava_still")), new Material(
/* 22 */         Identifier.withDefaultNamespace("block/lava_flow")), null, null);
/*    */   
/*    */   private final Map<Fluid, FluidModel> modelByFluid;
/*    */   
/*    */   private final FluidModel missingModel;
/*    */   
/*    */   public FluidStateModelSet(Map<Fluid, FluidModel> modelByFluid, FluidModel missingModel) {
/* 31 */     this.modelByFluid = modelByFluid;
/* 32 */     this.missingModel = missingModel;
/*    */   }
/*    */   
/*    */   public static Map<Fluid, FluidModel> bake(MaterialBaker materials) {
/* 37 */     FluidModel waterModel = WATER_MODEL.bake(materials, () -> "Water");
/* 38 */     FluidModel lavaModel = LAVA_MODEL.bake(materials, () -> "Lava");
/* 39 */     return (Map)Map.of(Fluids.WATER, waterModel, Fluids.FLOWING_WATER, waterModel, Fluids.LAVA, lavaModel, Fluids.FLOWING_LAVA, lavaModel);
/*    */   }
/*    */   
/*    */   public FluidModel get(FluidState state) {
/* 48 */     return this.modelByFluid.getOrDefault(state.getType(), this.missingModel);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\FluidStateModelSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */