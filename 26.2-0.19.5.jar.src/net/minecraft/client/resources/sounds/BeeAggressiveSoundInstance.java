/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.animal.bee.Bee;
/*    */ 
/*    */ public class BeeAggressiveSoundInstance extends BeeSoundInstance {
/*    */   public BeeAggressiveSoundInstance(Bee bee) {
/*  9 */     super(bee, SoundEvents.BEE_LOOP_AGGRESSIVE, SoundSource.NEUTRAL);
/* 10 */     this.delay = 0;
/*    */   }
/*    */   
/*    */   protected AbstractTickableSoundInstance getAlternativeSoundInstance() {
/* 15 */     return new BeeFlyingSoundInstance(this.bee);
/*    */   }
/*    */   
/*    */   protected boolean shouldSwitchSounds() {
/* 20 */     return !this.bee.isAngry();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\sounds\BeeAggressiveSoundInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */