/*    */ package net.minecraft.sounds;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ 
/*    */ public final class SoundEvent extends Record {
/*    */   private final Identifier location;
/*    */   
/*    */   private final Optional<Float> fixedRange;
/*    */   
/*    */   public static final Codec<SoundEvent> DIRECT_CODEC;
/*    */   
/*    */   public SoundEvent(Identifier location, Optional<Float> fixedRange) {
/* 17 */     this.location = location;
/* 17 */     this.fixedRange = fixedRange;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/sounds/SoundEvent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/sounds/SoundEvent;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/sounds/SoundEvent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/sounds/SoundEvent;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/sounds/SoundEvent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/sounds/SoundEvent;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier location() {
/* 17 */     return this.location;
/*    */   }
/*    */   
/*    */   public Optional<Float> fixedRange() {
/* 17 */     return this.fixedRange;
/*    */   }
/*    */   
/*    */   static {
/* 21 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("sound_id").forGetter(SoundEvent::location), (App)Codec.FLOAT.lenientOptionalFieldOf("range").forGetter(SoundEvent::fixedRange)).apply((Applicative)i, SoundEvent::create));
/*    */   }
/*    */   
/* 26 */   public static final Codec<Holder<SoundEvent>> CODEC = (Codec<Holder<SoundEvent>>)RegistryFileCodec.create(Registries.SOUND_EVENT, DIRECT_CODEC);
/*    */   
/* 28 */   public static final StreamCodec<ByteBuf, SoundEvent> DIRECT_STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, SoundEvent::location, ByteBufCodecs.FLOAT
/*    */       
/* 30 */       .apply(ByteBufCodecs::optional), SoundEvent::fixedRange, SoundEvent::create);
/*    */   
/* 34 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<SoundEvent>> STREAM_CODEC = ByteBufCodecs.holder(Registries.SOUND_EVENT, DIRECT_STREAM_CODEC);
/*    */   
/*    */   private static SoundEvent create(Identifier location, Optional<Float> range) {
/* 37 */     return range.<SoundEvent>map(r -> createFixedRangeEvent(location, r.floatValue())).orElseGet(() -> createVariableRangeEvent(location));
/*    */   }
/*    */   
/*    */   public static SoundEvent createVariableRangeEvent(Identifier location) {
/* 41 */     return new SoundEvent(location, Optional.empty());
/*    */   }
/*    */   
/*    */   public static SoundEvent createFixedRangeEvent(Identifier location, float range) {
/* 45 */     return new SoundEvent(location, Optional.of(Float.valueOf(range)));
/*    */   }
/*    */   
/*    */   public float getRange(float volume) {
/* 49 */     return ((Float)this.fixedRange.orElse(Float.valueOf((volume > 1.0F) ? (16.0F * volume) : 16.0F))).floatValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\sounds\SoundEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */