/*     */ package net.minecraft.client.resources.sounds;
/*     */ 
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class LoopSoundInstance extends AbstractTickableSoundInstance {
/*     */   private int fadeDirection;
/*     */   
/*     */   private int fade;
/*     */   
/*     */   public LoopSoundInstance(SoundEvent soundEvent) {
/* 128 */     super(soundEvent, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
/* 130 */     this.looping = true;
/* 131 */     this.delay = 0;
/* 132 */     this.volume = 1.0F;
/* 133 */     this.relative = true;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 138 */     if (this.fade < 0)
/* 139 */       stop(); 
/* 142 */     this.fade += this.fadeDirection;
/* 143 */     this.volume = Mth.clamp(this.fade / 40.0F, 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void fadeOut() {
/* 147 */     this.fade = Math.min(this.fade, 40);
/* 148 */     this.fadeDirection = -1;
/*     */   }
/*     */   
/*     */   public void fadeIn() {
/* 152 */     this.fade = Math.max(0, this.fade);
/* 153 */     this.fadeDirection = 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\sounds\BiomeAmbientSoundsHandler$LoopSoundInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */