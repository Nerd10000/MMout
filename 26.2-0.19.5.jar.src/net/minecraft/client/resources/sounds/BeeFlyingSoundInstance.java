/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.animal.bee.Bee;
/*    */ 
/*    */ public class BeeFlyingSoundInstance extends BeeSoundInstance {
/*    */   public BeeFlyingSoundInstance(Bee bee) {
/*  9 */     super(bee, SoundEvents.BEE_LOOP, SoundSource.NEUTRAL);
/*    */   }
/*    */   
/*    */   protected AbstractTickableSoundInstance getAlternativeSoundInstance() {
/* 14 */     return new BeeAggressiveSoundInstance(this.bee);
/*    */   }
/*    */   
/*    */   protected boolean shouldSwitchSounds() {
/* 19 */     return this.bee.isAngry();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\sounds\BeeFlyingSoundInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */