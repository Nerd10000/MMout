/*     */ package net.minecraft.client.sounds;
/*     */ 
/*     */ import net.minecraft.client.resources.sounds.Sound;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.MultipliedFloats;
/*     */ import net.minecraft.util.valueproviders.SampledFloat;
/*     */ 
/*     */ class null implements Weighted<Sound> {
/*     */   public int getWeight() {
/* 169 */     WeighedSoundEvents registration = SoundManager.Preparations.this.registry.get(soundLocation);
/* 170 */     return (registration == null) ? 0 : registration.getWeight();
/*     */   }
/*     */   
/*     */   public Sound getSound(RandomSource random) {
/* 175 */     WeighedSoundEvents registration = SoundManager.Preparations.this.registry.get(soundLocation);
/* 176 */     if (registration == null)
/* 177 */       return SoundManager.EMPTY_SOUND; 
/* 181 */     Sound wrappedSound = registration.getSound(random);
/* 182 */     return new Sound(wrappedSound
/* 183 */         .getLocation(), (SampledFloat)new MultipliedFloats(new SampledFloat[] { wrappedSound
/* 184 */             .getVolume(), this.val$sound.getVolume() }, ), (SampledFloat)new MultipliedFloats(new SampledFloat[] { wrappedSound
/* 185 */             .getPitch(), this.val$sound.getPitch() }, ), sound
/* 186 */         .getWeight(), Sound.Type.FILE, (wrappedSound
/*     */         
/* 188 */         .shouldStream() || sound.shouldStream()), wrappedSound
/* 189 */         .shouldPreload(), wrappedSound
/* 190 */         .getAttenuationDistance());
/*     */   }
/*     */   
/*     */   public void preloadIfRequired(SoundEngine soundEngine) {
/* 196 */     WeighedSoundEvents registration = SoundManager.Preparations.this.registry.get(soundLocation);
/* 197 */     if (registration == null)
/*     */       return; 
/* 200 */     registration.preloadIfRequired(soundEngine);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\sounds\SoundManager$Preparations$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */