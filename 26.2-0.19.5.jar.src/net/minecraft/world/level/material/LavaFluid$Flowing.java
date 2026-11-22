/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class Flowing extends LavaFluid {
/*     */   protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
/* 244 */     super.createFluidStateDefinition(builder);
/* 245 */     builder.add(new Property[] { (Property)LEVEL });
/*     */   }
/*     */   
/*     */   public int getAmount(FluidState fluidState) {
/* 250 */     return ((Integer)fluidState.getValue((Property)LEVEL)).intValue();
/*     */   }
/*     */   
/*     */   public boolean isSource(FluidState fluidState) {
/* 255 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\material\LavaFluid$Flowing.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */