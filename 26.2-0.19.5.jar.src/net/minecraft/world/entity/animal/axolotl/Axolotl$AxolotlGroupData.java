/*     */ package net.minecraft.world.entity.animal.axolotl;
/*     */ 
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ 
/*     */ public class AxolotlGroupData extends AgeableMob.AgeableMobGroupData {
/*     */   public final Axolotl.Variant[] types;
/*     */   
/*     */   public AxolotlGroupData(Axolotl.Variant... types) {
/* 703 */     super(false);
/* 704 */     this.types = types;
/*     */   }
/*     */   
/*     */   public Axolotl.Variant getVariant(RandomSource random) {
/* 708 */     return this.types[random.nextInt(this.types.length)];
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\axolotl\Axolotl$AxolotlGroupData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */