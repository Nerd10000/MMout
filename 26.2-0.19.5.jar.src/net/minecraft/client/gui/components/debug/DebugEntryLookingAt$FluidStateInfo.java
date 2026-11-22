/*     */ package net.minecraft.client.gui.components.debug;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ 
/*     */ public class FluidStateInfo extends DebugEntryLookingAt.DebugEntryLookingAtState<Fluid, FluidState> {
/*     */   protected FluidStateInfo() {
/* 142 */     super("Targeted Fluid");
/*     */   }
/*     */   
/*     */   public HitResult getHitResult(Entity cameraEntity) {
/* 147 */     return cameraEntity.pick(20.0D, 0.0F, true);
/*     */   }
/*     */   
/*     */   public FluidState getInstance(Level level, BlockPos pos) {
/* 152 */     return level.getFluidState(pos);
/*     */   }
/*     */   
/*     */   public Identifier group() {
/* 157 */     return DebugEntryLookingAt.FLUID_GROUP;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryLookingAt$FluidStateInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */