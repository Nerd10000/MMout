/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class SoundEventRegistration {
/*    */   private final List<Sound> sounds;
/*    */   
/*    */   private final boolean replace;
/*    */   
/*    */   private final String subtitle;
/*    */   
/*    */   public SoundEventRegistration(List<Sound> sounds, boolean replace, String subtitle) {
/* 13 */     this.sounds = sounds;
/* 14 */     this.replace = replace;
/* 15 */     this.subtitle = subtitle;
/*    */   }
/*    */   
/*    */   public List<Sound> getSounds() {
/* 19 */     return this.sounds;
/*    */   }
/*    */   
/*    */   public boolean isReplace() {
/* 23 */     return this.replace;
/*    */   }
/*    */   
/*    */   public String getSubtitle() {
/* 27 */     return this.subtitle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\sounds\SoundEventRegistration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */