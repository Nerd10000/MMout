/*    */ package net.minecraft.world.entity.animal.wolf;
/*    */ 
/*    */ public enum SoundSet {
/* 14 */   CLASSIC("classic", "wolf"),
/* 15 */   PUGLIN("puglin", "wolf_puglin"),
/* 16 */   SAD("sad", "wolf_sad"),
/* 17 */   ANGRY("angry", "wolf_angry"),
/* 18 */   GRUMPY("grumpy", "wolf_grumpy"),
/* 19 */   BIG("big", "wolf_big"),
/* 20 */   CUTE("cute", "wolf_cute");
/*    */   
/*    */   private final String identifier;
/*    */   
/*    */   private final String soundEventIdentifier;
/*    */   
/*    */   SoundSet(String identifier, String soundEventIdentifier) {
/* 26 */     this.identifier = identifier;
/* 27 */     this.soundEventIdentifier = soundEventIdentifier;
/*    */   }
/*    */   
/*    */   public String getIdentifier() {
/* 31 */     return this.identifier;
/*    */   }
/*    */   
/*    */   public String getSoundEventIdentifier() {
/* 35 */     return this.soundEventIdentifier;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\wolf\WolfSoundVariants$SoundSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */