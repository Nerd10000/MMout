/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.client.sounds.SoundManager;
/*    */ import net.minecraft.client.sounds.WeighedSoundEvents;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public interface SoundInstance {
/*    */   Identifier getIdentifier();
/*    */   
/*    */   WeighedSoundEvents resolve(SoundManager paramSoundManager);
/*    */   
/*    */   Sound getSound();
/*    */   
/*    */   SoundSource getSource();
/*    */   
/*    */   boolean isLooping();
/*    */   
/*    */   boolean isRelative();
/*    */   
/*    */   int getDelay();
/*    */   
/*    */   float getVolume();
/*    */   
/*    */   float getPitch();
/*    */   
/*    */   double getX();
/*    */   
/*    */   double getY();
/*    */   
/*    */   double getZ();
/*    */   
/*    */   Attenuation getAttenuation();
/*    */   
/*    */   public enum Attenuation {
/* 12 */     NONE, LINEAR;
/*    */   }
/*    */   
/*    */   default boolean canStartSilent() {
/* 46 */     return false;
/*    */   }
/*    */   
/*    */   default boolean canPlaySound() {
/* 50 */     return true;
/*    */   }
/*    */   
/*    */   static RandomSource createUnseededRandom() {
/* 58 */     return RandomSource.create();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\sounds\SoundInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */