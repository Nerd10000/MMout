/*    */ package net.minecraft.sounds;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class Music extends Record {
/*    */   private final Holder<SoundEvent> sound;
/*    */   
/*    */   private final int minDelay;
/*    */   
/*    */   private final int maxDelay;
/*    */   
/*    */   private final boolean replaceCurrentMusic;
/*    */   
/*    */   public static final Codec<Music> CODEC;
/*    */   
/*    */   public Music(Holder<SoundEvent> sound, int minDelay, int maxDelay, boolean replaceCurrentMusic) {
/*  8 */     this.sound = sound;
/*  8 */     this.minDelay = minDelay;
/*  8 */     this.maxDelay = maxDelay;
/*  8 */     this.replaceCurrentMusic = replaceCurrentMusic;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/sounds/Music;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/sounds/Music;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/sounds/Music;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/sounds/Music;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/sounds/Music;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/sounds/Music;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> sound() {
/*  8 */     return this.sound;
/*    */   }
/*    */   
/*    */   public int minDelay() {
/*  8 */     return this.minDelay;
/*    */   }
/*    */   
/*    */   public int maxDelay() {
/*  8 */     return this.maxDelay;
/*    */   }
/*    */   
/*    */   public boolean replaceCurrentMusic() {
/*  8 */     return this.replaceCurrentMusic;
/*    */   }
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("sound").forGetter(Music::sound), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("min_delay").forGetter(Music::minDelay), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("max_delay").forGetter(Music::maxDelay), (App)Codec.BOOL.optionalFieldOf("replace_current_music", Boolean.valueOf(false)).forGetter(Music::replaceCurrentMusic)).apply((Applicative)i, Music::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\sounds\Music.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */