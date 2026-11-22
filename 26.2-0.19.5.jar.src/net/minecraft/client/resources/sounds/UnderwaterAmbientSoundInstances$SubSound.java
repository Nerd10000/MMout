/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.client.player.LocalPlayer;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ 
/*    */ public class SubSound extends AbstractTickableSoundInstance {
/*    */   private final LocalPlayer player;
/*    */   
/*    */   protected SubSound(LocalPlayer player, SoundEvent event) {
/* 14 */     super(event, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
/* 16 */     this.player = player;
/* 17 */     this.looping = false;
/* 18 */     this.delay = 0;
/* 19 */     this.volume = 1.0F;
/* 20 */     this.relative = true;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 25 */     if (this.player.isRemoved() || !this.player.isUnderWater())
/* 26 */       stop(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\sounds\UnderwaterAmbientSoundInstances$SubSound.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */