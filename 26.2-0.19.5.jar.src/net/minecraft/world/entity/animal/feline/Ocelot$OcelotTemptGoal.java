/*     */ package net.minecraft.world.entity.animal.feline;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ class OcelotTemptGoal extends TemptGoal {
/*     */   private final Ocelot ocelot;
/*     */   
/*     */   public OcelotTemptGoal(Ocelot ocelot, double speedModifier, Predicate<ItemStack> items, boolean canScare) {
/* 319 */     super((PathfinderMob)ocelot, speedModifier, items, canScare);
/* 320 */     this.ocelot = ocelot;
/*     */   }
/*     */   
/*     */   protected boolean canScare() {
/* 325 */     return (super.canScare() && !this.ocelot.isTrusting());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\feline\Ocelot$OcelotTemptGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */