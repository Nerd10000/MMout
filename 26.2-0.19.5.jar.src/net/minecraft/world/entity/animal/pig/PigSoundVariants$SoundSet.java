/*    */ package net.minecraft.world.entity.animal.pig;
/*    */ 
/*    */ public enum SoundSet {
/* 14 */   CLASSIC("classic", "pig"),
/* 15 */   MINI("mini", "pig_mini"),
/* 16 */   BIG("big", "pig_big");
/*    */   
/*    */   private final String identifier;
/*    */   
/*    */   private final String soundEventIdentifier;
/*    */   
/*    */   SoundSet(String identifier, String soundEventIdentifier) {
/* 22 */     this.identifier = identifier;
/* 23 */     this.soundEventIdentifier = soundEventIdentifier;
/*    */   }
/*    */   
/*    */   public String getIdentifier() {
/* 27 */     return this.identifier;
/*    */   }
/*    */   
/*    */   public String getSoundEventIdentifier() {
/* 31 */     return this.soundEventIdentifier;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\pig\PigSoundVariants$SoundSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */