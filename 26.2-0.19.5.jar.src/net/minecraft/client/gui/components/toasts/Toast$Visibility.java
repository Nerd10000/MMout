/*    */ package net.minecraft.client.gui.components.toasts;
/*    */ 
/*    */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*    */ import net.minecraft.client.resources.sounds.SoundInstance;
/*    */ import net.minecraft.client.sounds.SoundManager;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ 
/*    */ public enum Visibility {
/* 56 */   SHOW(SoundEvents.UI_TOAST_IN),
/* 57 */   HIDE(SoundEvents.UI_TOAST_OUT);
/*    */   
/*    */   private final SoundEvent soundEvent;
/*    */   
/*    */   Visibility(SoundEvent soundEvent) {
/* 63 */     this.soundEvent = soundEvent;
/*    */   }
/*    */   
/*    */   public void playSound(SoundManager manager) {
/* 67 */     manager.play((SoundInstance)SimpleSoundInstance.forUI(this.soundEvent, 1.0F, 1.0F));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\toasts\Toast$Visibility.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */