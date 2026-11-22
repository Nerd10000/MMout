/*    */ package net.minecraft.world.entity.animal.chicken;
/*    */ 
/*    */ public enum SoundSet {
/* 14 */   CLASSIC("classic", "chicken"),
/* 15 */   PICKY("picky", "chicken_picky");
/*    */   
/*    */   private final String identifier;
/*    */   
/*    */   private final String soundEventIdentifier;
/*    */   
/*    */   SoundSet(String identifier, String soundEventIdentifier) {
/* 21 */     this.identifier = identifier;
/* 22 */     this.soundEventIdentifier = soundEventIdentifier;
/*    */   }
/*    */   
/*    */   public String getIdentifier() {
/* 26 */     return this.identifier;
/*    */   }
/*    */   
/*    */   public String getSoundEventIdentifier() {
/* 30 */     return this.soundEventIdentifier;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\chicken\ChickenSoundVariants$SoundSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */