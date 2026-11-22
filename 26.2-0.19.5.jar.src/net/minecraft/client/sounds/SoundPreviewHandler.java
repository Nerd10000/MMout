/*    */ package net.minecraft.client.sounds;
/*    */ 
/*    */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*    */ import net.minecraft.client.resources.sounds.SoundInstance;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ 
/*    */ public final class SoundPreviewHandler {
/*    */   private static SoundInstance activePreview;
/*    */   
/*    */   private static SoundSource previousCategory;
/*    */   
/*    */   public static void preview(SoundManager soundManager, SoundSource category, float volume) {
/* 16 */     stopOtherCategoryPreview(soundManager, category);
/* 17 */     if (canPlaySound(soundManager)) {
/* 18 */       switch (category) {
/*    */         case RECORDS:
/*    */         
/*    */         case WEATHER:
/*    */         
/*    */         case BLOCKS:
/*    */         
/*    */         case HOSTILE:
/*    */         
/*    */         case NEUTRAL:
/*    */         
/*    */         case PLAYERS:
/*    */         
/*    */         case AMBIENT:
/*    */         
/*    */         case UI:
/*    */         
/*    */         default:
/*    */           break;
/*    */       } 
/* 18 */       SoundEvent previewSound = 
/*    */         
/* 27 */         SoundEvents.EMPTY;
/* 29 */       if (previewSound != SoundEvents.EMPTY) {
/* 30 */         activePreview = (SoundInstance)SimpleSoundInstance.forUI(previewSound, 1.0F, volume);
/* 31 */         soundManager.play(activePreview);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void stopOtherCategoryPreview(SoundManager soundManager, SoundSource category) {
/* 37 */     if (previousCategory != category) {
/* 38 */       previousCategory = category;
/* 39 */       if (activePreview != null)
/* 40 */         soundManager.stop(activePreview); 
/*    */     } 
/*    */   }
/*    */   
/*    */   private static boolean canPlaySound(SoundManager soundManager) {
/* 46 */     return (activePreview == null || !soundManager.isActive(activePreview));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\sounds\SoundPreviewHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */