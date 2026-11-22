/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.Music;
/*    */ import net.minecraft.sounds.Musics;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ 
/*    */ public final class BackgroundMusic extends Record {
/*    */   private final Optional<Music> defaultMusic;
/*    */   
/*    */   private final Optional<Music> creativeMusic;
/*    */   
/*    */   private final Optional<Music> underwaterMusic;
/*    */   
/*    */   public BackgroundMusic(Optional<Music> defaultMusic, Optional<Music> creativeMusic, Optional<Music> underwaterMusic) {
/* 12 */     this.defaultMusic = defaultMusic;
/* 12 */     this.creativeMusic = creativeMusic;
/* 12 */     this.underwaterMusic = underwaterMusic;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/BackgroundMusic;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/BackgroundMusic;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/BackgroundMusic;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/BackgroundMusic;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/BackgroundMusic;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/BackgroundMusic;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<Music> defaultMusic() {
/* 12 */     return this.defaultMusic;
/*    */   }
/*    */   
/*    */   public Optional<Music> creativeMusic() {
/* 12 */     return this.creativeMusic;
/*    */   }
/*    */   
/*    */   public Optional<Music> underwaterMusic() {
/* 12 */     return this.underwaterMusic;
/*    */   }
/*    */   
/* 17 */   public static final BackgroundMusic EMPTY = new BackgroundMusic(Optional.empty(), Optional.empty(), Optional.empty());
/*    */   
/* 18 */   public static final BackgroundMusic OVERWORLD = new BackgroundMusic(Optional.of(Musics.GAME), Optional.of(Musics.CREATIVE), Optional.empty());
/*    */   
/*    */   public static final Codec<BackgroundMusic> CODEC;
/*    */   
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Music.CODEC.optionalFieldOf("default").forGetter(BackgroundMusic::defaultMusic), (App)Music.CODEC.optionalFieldOf("creative").forGetter(BackgroundMusic::creativeMusic), (App)Music.CODEC.optionalFieldOf("underwater").forGetter(BackgroundMusic::underwaterMusic)).apply((Applicative)i, BackgroundMusic::new));
/*    */   }
/*    */   
/*    */   public BackgroundMusic(Music music) {
/* 27 */     this(Optional.of(music), Optional.empty(), Optional.empty());
/*    */   }
/*    */   
/*    */   public BackgroundMusic(Holder<SoundEvent> sound) {
/* 31 */     this(Musics.createGameMusic(sound));
/*    */   }
/*    */   
/*    */   public BackgroundMusic withUnderwater(Music underwaterMusic) {
/* 35 */     return new BackgroundMusic(this.defaultMusic, this.creativeMusic, Optional.of(underwaterMusic));
/*    */   }
/*    */   
/*    */   public Optional<Music> select(boolean isCreative, boolean isUnderwater) {
/* 39 */     if (isUnderwater && this.underwaterMusic.isPresent())
/* 40 */       return this.underwaterMusic; 
/* 42 */     if (isCreative && this.creativeMusic.isPresent())
/* 43 */       return this.creativeMusic; 
/* 45 */     return this.defaultMusic;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\BackgroundMusic.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */