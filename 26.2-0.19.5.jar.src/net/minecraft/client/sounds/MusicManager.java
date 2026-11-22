/*     */ package net.minecraft.client.sounds;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*     */ import net.minecraft.client.resources.sounds.Sound;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.sounds.Music;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public class MusicManager {
/*     */   private static final int STARTING_DELAY = 100;
/*     */   
/*  22 */   private final RandomSource random = RandomSource.create();
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private SoundInstance currentMusic;
/*     */   
/*     */   private MusicFrequency gameMusicFrequency;
/*     */   
/*  26 */   private float currentGain = 1.0F;
/*     */   
/*  27 */   private int nextSongDelay = 100;
/*     */   
/*     */   private boolean toastShown = false;
/*     */   
/*     */   public MusicManager(Minecraft minecraft) {
/*  31 */     this.minecraft = minecraft;
/*  32 */     this.gameMusicFrequency = (MusicFrequency)minecraft.options.musicFrequency().get();
/*     */   }
/*     */   
/*     */   public void tick() {
/*  36 */     float volume = this.minecraft.getMusicVolume();
/*  37 */     if (this.currentMusic != null && this.currentGain != volume) {
/*  38 */       boolean stillPlaying = fadePlaying(volume);
/*  39 */       if (!stillPlaying)
/*     */         return; 
/*     */     } 
/*  44 */     Music music = this.minecraft.getSituationalMusic();
/*  45 */     if (music == null) {
/*  46 */       this.nextSongDelay = Math.max(this.nextSongDelay, 100);
/*     */       return;
/*     */     } 
/*  50 */     if (this.currentMusic != null) {
/*  51 */       if (canReplace(music, this.currentMusic)) {
/*  52 */         this.minecraft.getSoundManager().stop(this.currentMusic);
/*  53 */         this.nextSongDelay = Mth.nextInt(this.random, 0, music.minDelay() / 2);
/*     */       } 
/*  56 */       if (!this.minecraft.getSoundManager().isActive(this.currentMusic)) {
/*  57 */         this.currentMusic = null;
/*  58 */         this.nextSongDelay = Math.min(this.nextSongDelay, this.gameMusicFrequency.getNextSongDelay(music, this.random));
/*     */       } 
/*     */     } 
/*  62 */     this.nextSongDelay = Math.min(this.nextSongDelay, music.maxDelay());
/*  64 */     if (this.currentMusic == null && !(this.minecraft.gui.screen() instanceof net.minecraft.client.gui.screens.LevelLoadingScreen) && --this.nextSongDelay <= 0)
/*  65 */       startPlaying(music); 
/*     */   }
/*     */   
/*     */   private static boolean canReplace(Music music, SoundInstance currentMusic) {
/*  70 */     return (music.replaceCurrentMusic() && !((SoundEvent)music.sound().value()).location().equals(currentMusic.getIdentifier()));
/*     */   }
/*     */   
/*     */   public void startPlaying(Music music) {
/*  74 */     SoundEvent soundEvent = (SoundEvent)music.sound().value();
/*  75 */     this.currentMusic = (SoundInstance)SimpleSoundInstance.forMusic(soundEvent);
/*  76 */     switch (this.minecraft.getSoundManager().play(this.currentMusic)) {
/*     */       case STARTED:
/*  78 */         this.minecraft.gui.toastManager().showNowPlayingToast();
/*  79 */         this.toastShown = true;
/*     */         break;
/*     */       case STARTED_SILENTLY:
/*  81 */         this.toastShown = false;
/*     */         break;
/*     */     } 
/*  83 */     this.nextSongDelay = Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */   public void showNowPlayingToastIfNeeded() {
/*  87 */     if (!this.toastShown) {
/*  88 */       this.minecraft.gui.toastManager().showNowPlayingToast();
/*  89 */       this.toastShown = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stopPlaying(Music music) {
/*  94 */     if (isPlayingMusic(music))
/*  95 */       stopPlaying(); 
/*     */   }
/*     */   
/*     */   public void stopPlaying() {
/* 100 */     if (this.currentMusic != null) {
/* 101 */       this.minecraft.getSoundManager().stop(this.currentMusic);
/* 102 */       this.currentMusic = null;
/* 103 */       this.minecraft.gui.toastManager().hideNowPlayingToast();
/*     */     } 
/* 105 */     this.nextSongDelay = this.gameMusicFrequency.getNextSongDelay(this.minecraft.getSituationalMusic(), this.random) + 100;
/*     */   }
/*     */   
/*     */   private boolean fadePlaying(float volume) {
/* 109 */     if (this.currentMusic == null)
/* 110 */       return false; 
/* 112 */     if (this.currentGain == volume)
/* 113 */       return true; 
/* 115 */     if (this.currentGain < volume) {
/* 118 */       this.currentGain += Mth.clamp(this.currentGain, 5.0E-4F, 0.005F);
/* 119 */       if (this.currentGain > volume)
/* 120 */         this.currentGain = volume; 
/*     */     } else {
/* 125 */       this.currentGain = 0.03F * volume + 0.97F * this.currentGain;
/* 126 */       if (Math.abs(this.currentGain - volume) < 1.0E-4F || this.currentGain < volume)
/* 127 */         this.currentGain = volume; 
/*     */     } 
/* 130 */     this.currentGain = Mth.clamp(this.currentGain, 0.0F, 1.0F);
/* 131 */     if (this.currentGain <= 1.0E-4F) {
/* 132 */       stopPlaying();
/* 133 */       return false;
/*     */     } 
/* 136 */     this.minecraft.getSoundManager().updateCategoryVolume(SoundSource.MUSIC, this.currentGain);
/* 137 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isPlayingMusic(Music music) {
/* 141 */     if (this.currentMusic == null)
/* 142 */       return false; 
/* 145 */     return ((SoundEvent)music.sound().value()).location().equals(this.currentMusic.getIdentifier());
/*     */   }
/*     */   
/*     */   public String getCurrentMusicTranslationKey() {
/* 149 */     if (this.currentMusic != null) {
/* 150 */       Sound sound = this.currentMusic.getSound();
/* 151 */       if (sound != null)
/* 152 */         return sound.getLocation().toShortLanguageKey(); 
/*     */     } 
/* 155 */     return null;
/*     */   }
/*     */   
/*     */   public void setMinutesBetweenSongs(MusicFrequency musicFrequency) {
/* 159 */     this.gameMusicFrequency = musicFrequency;
/* 160 */     this.nextSongDelay = this.gameMusicFrequency.getNextSongDelay(this.minecraft.getSituationalMusic(), this.random);
/*     */   }
/*     */   
/*     */   public enum MusicFrequency implements StringRepresentable {
/* 164 */     DEFAULT("DEFAULT", "options.music_frequency.default", 20),
/* 165 */     FREQUENT("FREQUENT", "options.music_frequency.frequent", 10),
/* 166 */     CONSTANT("CONSTANT", "options.music_frequency.constant", 0);
/*     */     
/* 169 */     public static final Codec<MusicFrequency> CODEC = (Codec<MusicFrequency>)StringRepresentable.fromEnum(MusicFrequency::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final int maxFrequency;
/*     */     
/*     */     private final Component caption;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     MusicFrequency(String name, String translationKey, int maxFrequencyMinutes) {
/* 176 */       this.name = name;
/* 177 */       this.maxFrequency = maxFrequencyMinutes * 1200;
/* 178 */       this.caption = (Component)Component.translatable(translationKey);
/*     */     }
/*     */     
/*     */     private int getNextSongDelay(Music music, RandomSource random) {
/* 182 */       if (music == null)
/* 183 */         return this.maxFrequency; 
/* 184 */       if (this == CONSTANT)
/* 185 */         return 100; 
/* 187 */       int minFrequency = Math.min(music.minDelay(), this.maxFrequency);
/* 188 */       int maxFrequency = Math.min(music.maxDelay(), this.maxFrequency);
/* 189 */       return Mth.nextInt(random, minFrequency, maxFrequency);
/*     */     }
/*     */     
/*     */     public Component caption() {
/* 194 */       return this.caption;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 199 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\sounds\MusicManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */