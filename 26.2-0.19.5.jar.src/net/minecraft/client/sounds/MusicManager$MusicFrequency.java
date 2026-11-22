/*     */ package net.minecraft.client.sounds;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.sounds.Music;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum MusicFrequency implements StringRepresentable {
/* 164 */   DEFAULT("DEFAULT", "options.music_frequency.default", 20),
/* 165 */   FREQUENT("FREQUENT", "options.music_frequency.frequent", 10),
/* 166 */   CONSTANT("CONSTANT", "options.music_frequency.constant", 0);
/*     */   
/*     */   public static final Codec<MusicFrequency> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final int maxFrequency;
/*     */   
/*     */   private final Component caption;
/*     */   
/*     */   static {
/* 169 */     CODEC = (Codec<MusicFrequency>)StringRepresentable.fromEnum(MusicFrequency::values);
/*     */   }
/*     */   
/*     */   MusicFrequency(String name, String translationKey, int maxFrequencyMinutes) {
/* 176 */     this.name = name;
/* 177 */     this.maxFrequency = maxFrequencyMinutes * 1200;
/* 178 */     this.caption = (Component)Component.translatable(translationKey);
/*     */   }
/*     */   
/*     */   private int getNextSongDelay(Music music, RandomSource random) {
/* 182 */     if (music == null)
/* 183 */       return this.maxFrequency; 
/* 184 */     if (this == CONSTANT)
/* 185 */       return 100; 
/* 187 */     int minFrequency = Math.min(music.minDelay(), this.maxFrequency);
/* 188 */     int maxFrequency = Math.min(music.maxDelay(), this.maxFrequency);
/* 189 */     return Mth.nextInt(random, minFrequency, maxFrequency);
/*     */   }
/*     */   
/*     */   public Component caption() {
/* 194 */     return this.caption;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 199 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\sounds\MusicManager$MusicFrequency.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */