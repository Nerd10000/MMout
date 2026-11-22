/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class Subtitle {
/*     */   private final Component text;
/*     */   
/*     */   private final float range;
/*     */   
/* 152 */   private final List<SubtitleOverlay.SoundPlayedAt> playedAt = new ArrayList<>();
/*     */   
/*     */   public Subtitle(Component text, float range, Vec3 location) {
/* 155 */     this.text = text;
/* 156 */     this.range = range;
/* 157 */     this.playedAt.add(new SubtitleOverlay.SoundPlayedAt(location, Util.getMillis()));
/*     */   }
/*     */   
/*     */   public Component getText() {
/* 161 */     return this.text;
/*     */   }
/*     */   
/*     */   public SubtitleOverlay.SoundPlayedAt getClosest(Vec3 position) {
/* 165 */     if (this.playedAt.isEmpty())
/* 166 */       return null; 
/* 169 */     if (this.playedAt.size() == 1)
/* 170 */       return this.playedAt.getFirst(); 
/* 173 */     return this.playedAt.stream()
/* 174 */       .min(Comparator.comparingDouble(soundPlayedAt -> soundPlayedAt.location().distanceTo(position)))
/* 175 */       .orElse(null);
/*     */   }
/*     */   
/*     */   public void refresh(Vec3 location) {
/* 179 */     this.playedAt.removeIf(soundPlayedAt -> location.equals(soundPlayedAt.location()));
/* 180 */     this.playedAt.add(new SubtitleOverlay.SoundPlayedAt(location, Util.getMillis()));
/*     */   }
/*     */   
/*     */   public boolean isAudibleFrom(Vec3 camera) {
/* 184 */     if (Float.isInfinite(this.range))
/* 185 */       return true; 
/* 188 */     if (this.playedAt.isEmpty())
/* 189 */       return false; 
/* 192 */     SubtitleOverlay.SoundPlayedAt closest = getClosest(camera);
/* 194 */     if (closest == null)
/* 195 */       return false; 
/* 198 */     return camera.closerThan((Position)closest.location, this.range);
/*     */   }
/*     */   
/*     */   public void purgeOldInstances(double maxAge) {
/* 202 */     long currentTime = Util.getMillis();
/* 203 */     this.playedAt.removeIf(soundPlayedAt -> ((currentTime - soundPlayedAt.time()) > maxAge));
/*     */   }
/*     */   
/*     */   public boolean isStillActive() {
/* 207 */     return !this.playedAt.isEmpty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\SubtitleOverlay$Subtitle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */