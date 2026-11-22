/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.RegistryFixedCodec;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public final class JukeboxSong extends Record {
/*    */   private final Holder<SoundEvent> soundEvent;
/*    */   
/*    */   private final Component description;
/*    */   
/*    */   private final float lengthInSeconds;
/*    */   
/*    */   private final int comparatorOutput;
/*    */   
/*    */   public static final Codec<JukeboxSong> DIRECT_CODEC;
/*    */   
/*    */   public JukeboxSong(Holder<SoundEvent> soundEvent, Component description, float lengthInSeconds, int comparatorOutput) {
/* 21 */     this.soundEvent = soundEvent;
/* 21 */     this.description = description;
/* 21 */     this.lengthInSeconds = lengthInSeconds;
/* 21 */     this.comparatorOutput = comparatorOutput;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/JukeboxSong;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/JukeboxSong;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/JukeboxSong;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/JukeboxSong;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/JukeboxSong;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/JukeboxSong;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> soundEvent() {
/* 21 */     return this.soundEvent;
/*    */   }
/*    */   
/*    */   public Component description() {
/* 21 */     return this.description;
/*    */   }
/*    */   
/*    */   public float lengthInSeconds() {
/* 21 */     return this.lengthInSeconds;
/*    */   }
/*    */   
/*    */   public int comparatorOutput() {
/* 21 */     return this.comparatorOutput;
/*    */   }
/*    */   
/*    */   static {
/* 22 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("sound_event").forGetter(JukeboxSong::soundEvent), (App)ComponentSerialization.CODEC.fieldOf("description").forGetter(JukeboxSong::description), (App)ExtraCodecs.POSITIVE_FLOAT.fieldOf("length_in_seconds").forGetter(JukeboxSong::lengthInSeconds), (App)ExtraCodecs.intRange(0, 15).fieldOf("comparator_output").forGetter(JukeboxSong::comparatorOutput)).apply((Applicative)i, JukeboxSong::new));
/*    */   }
/*    */   
/* 28 */   public static final StreamCodec<RegistryFriendlyByteBuf, JukeboxSong> DIRECT_STREAM_CODEC = StreamCodec.composite(SoundEvent.STREAM_CODEC, JukeboxSong::soundEvent, ComponentSerialization.STREAM_CODEC, JukeboxSong::description, ByteBufCodecs.FLOAT, JukeboxSong::lengthInSeconds, ByteBufCodecs.VAR_INT, JukeboxSong::comparatorOutput, JukeboxSong::new);
/*    */   
/* 36 */   public static final Codec<Holder<JukeboxSong>> CODEC = (Codec<Holder<JukeboxSong>>)RegistryFixedCodec.create(Registries.JUKEBOX_SONG);
/*    */   
/* 37 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<JukeboxSong>> STREAM_CODEC = ByteBufCodecs.holder(Registries.JUKEBOX_SONG, DIRECT_STREAM_CODEC);
/*    */   
/*    */   private static final int SONG_END_PADDING_TICKS = 20;
/*    */   
/*    */   public int lengthInTicks() {
/* 41 */     return Mth.ceil(this.lengthInSeconds * 20.0F);
/*    */   }
/*    */   
/*    */   public boolean hasFinished(long ticksElapsed) {
/* 45 */     return (ticksElapsed >= (lengthInTicks() + 20));
/*    */   }
/*    */   
/*    */   public static Optional<Holder<JukeboxSong>> fromStack(ItemStack stack) {
/* 49 */     JukeboxPlayable jukeboxPlayable = (JukeboxPlayable)stack.get(DataComponents.JUKEBOX_PLAYABLE);
/* 50 */     if (jukeboxPlayable != null)
/* 51 */       return Optional.of(jukeboxPlayable.song()); 
/* 53 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\JukeboxSong.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */