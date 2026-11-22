/*     */ package net.minecraft.world.entity.monster.spider;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ 
/*     */ public class SpiderEffectsGroupData implements SpawnGroupData {
/*     */   public Holder<MobEffect> effect;
/*     */   
/*     */   public void setRandomEffect(RandomSource random) {
/* 204 */     int selection = random.nextInt(5);
/* 205 */     if (selection <= 1) {
/* 206 */       this.effect = MobEffects.SPEED;
/* 207 */     } else if (selection <= 2) {
/* 208 */       this.effect = MobEffects.STRENGTH;
/* 209 */     } else if (selection <= 3) {
/* 210 */       this.effect = MobEffects.REGENERATION;
/* 211 */     } else if (selection <= 4) {
/* 212 */       this.effect = MobEffects.INVISIBILITY;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\spider\Spider$SpiderEffectsGroupData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */