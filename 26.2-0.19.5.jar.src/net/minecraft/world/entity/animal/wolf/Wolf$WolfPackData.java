/*     */ package net.minecraft.world.entity.animal.wolf;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ 
/*     */ public class WolfPackData extends AgeableMob.AgeableMobGroupData {
/*     */   public final Holder<WolfVariant> type;
/*     */   
/*     */   public WolfPackData(Holder<WolfVariant> type) {
/* 286 */     super(false);
/* 287 */     this.type = type;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\wolf\Wolf$WolfPackData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */