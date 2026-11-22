/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ public enum Fluid {
/* 102 */   NONE(state -> false),
/* 103 */   SOURCE_ONLY(FluidState::isSource),
/* 103 */   ANY(FluidState::isSource),
/* 103 */   WATER(FluidState::isSource);
/*     */   
/*     */   private final Predicate<FluidState> canPick;
/*     */   
/*     */   static {
/* 104 */     ANY = new Fluid("ANY", 2, state -> !state.isEmpty());
/* 105 */     WATER = new Fluid("WATER", 3, fluidState -> fluidState.is(FluidTags.WATER));
/*     */   }
/*     */   
/*     */   Fluid(Predicate<FluidState> canPick) {
/* 111 */     this.canPick = canPick;
/*     */   }
/*     */   
/*     */   public boolean canPick(FluidState fluidState) {
/* 115 */     return this.canPick.test(fluidState);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\ClipContext$Fluid.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */