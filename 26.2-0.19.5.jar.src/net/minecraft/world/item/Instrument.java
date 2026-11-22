/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class Instrument extends Record {
/*    */   private final Holder<SoundEvent> soundEvent;
/*    */   
/*    */   private final float useDuration;
/*    */   
/*    */   private final float range;
/*    */   
/*    */   private final Component description;
/*    */   
/*    */   public static final Codec<Instrument> DIRECT_CODEC;
/*    */   
/*    */   public Instrument(Holder<SoundEvent> soundEvent, float useDuration, float range, Component description) {
/* 16 */     this.soundEvent = soundEvent;
/* 16 */     this.useDuration = useDuration;
/* 16 */     this.range = range;
/* 16 */     this.description = description;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/Instrument;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/Instrument;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/Instrument;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/Instrument;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/Instrument;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/Instrument;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> soundEvent() {
/* 16 */     return this.soundEvent;
/*    */   }
/*    */   
/*    */   public float useDuration() {
/* 16 */     return this.useDuration;
/*    */   }
/*    */   
/*    */   public float range() {
/* 16 */     return this.range;
/*    */   }
/*    */   
/*    */   public Component description() {
/* 16 */     return this.description;
/*    */   }
/*    */   
/*    */   static {
/* 17 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("sound_event").forGetter(Instrument::soundEvent), (App)ExtraCodecs.POSITIVE_FLOAT.fieldOf("use_duration").forGetter(Instrument::useDuration), (App)ExtraCodecs.POSITIVE_FLOAT.fieldOf("range").forGetter(Instrument::range), (App)ComponentSerialization.CODEC.fieldOf("description").forGetter(Instrument::description)).apply((Applicative)i, Instrument::new));
/*    */   }
/*    */   
/* 23 */   public static final StreamCodec<RegistryFriendlyByteBuf, Instrument> DIRECT_STREAM_CODEC = StreamCodec.composite(SoundEvent.STREAM_CODEC, Instrument::soundEvent, ByteBufCodecs.FLOAT, Instrument::useDuration, ByteBufCodecs.FLOAT, Instrument::range, ComponentSerialization.STREAM_CODEC, Instrument::description, Instrument::new);
/*    */   
/* 31 */   public static final Codec<Holder<Instrument>> CODEC = (Codec<Holder<Instrument>>)RegistryFileCodec.create(Registries.INSTRUMENT, DIRECT_CODEC);
/*    */   
/* 32 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Instrument>> STREAM_CODEC = ByteBufCodecs.holder(Registries.INSTRUMENT, DIRECT_STREAM_CODEC);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\Instrument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */