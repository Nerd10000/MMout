/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.animal.sniffer.Sniffer;
/*    */ 
/*    */ public class SnifferSoundInstance extends AbstractTickableSoundInstance {
/*    */   private static final float VOLUME = 1.0F;
/*    */   
/*    */   private static final float PITCH = 1.0F;
/*    */   
/*    */   private final Sniffer sniffer;
/*    */   
/*    */   public SnifferSoundInstance(Sniffer sniffer) {
/* 15 */     super(SoundEvents.SNIFFER_DIGGING, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
/* 17 */     this.sniffer = sniffer;
/* 19 */     this.attenuation = SoundInstance.Attenuation.LINEAR;
/* 20 */     this.looping = false;
/* 21 */     this.delay = 0;
/*    */   }
/*    */   
/*    */   public boolean canPlaySound() {
/* 26 */     return !this.sniffer.isSilent();
/*    */   }
/*    */   
/*    */   public void tick() {
/* 31 */     if (this.sniffer.isRemoved() || this.sniffer.getTarget() != null || !this.sniffer.canPlayDiggingSound()) {
/* 32 */       stop();
/*    */       return;
/*    */     } 
/* 36 */     this.x = (float)this.sniffer.getX();
/* 37 */     this.y = (float)this.sniffer.getY();
/* 38 */     this.z = (float)this.sniffer.getZ();
/* 40 */     this.volume = 1.0F;
/* 41 */     this.pitch = 1.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\sounds\SnifferSoundInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */