/*     */ package net.minecraft.world.entity.animal.camel;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ 
/*     */ class CamelLookControl extends LookControl {
/*     */   private CamelLookControl() {
/* 708 */     super((Mob)paramCamel);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 713 */     if (!Camel.this.hasControllingPassenger())
/* 714 */       super.tick(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\camel\Camel$CamelLookControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */