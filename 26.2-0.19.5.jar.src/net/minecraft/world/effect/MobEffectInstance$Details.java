/*     */ package net.minecraft.world.effect;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ final class Details extends Record {
/*     */   private final int amplifier;
/*     */   
/*     */   private final int duration;
/*     */   
/*     */   private final boolean ambient;
/*     */   
/*     */   private final boolean showParticles;
/*     */   
/*     */   private final boolean showIcon;
/*     */   
/*     */   private final Optional<Details> hiddenEffect;
/*     */   
/*     */   private Details(int amplifier, int duration, boolean ambient, boolean showParticles, boolean showIcon, Optional<Details> hiddenEffect) {
/* 346 */     this.amplifier = amplifier;
/* 346 */     this.duration = duration;
/* 346 */     this.ambient = ambient;
/* 346 */     this.showParticles = showParticles;
/* 346 */     this.showIcon = showIcon;
/* 346 */     this.hiddenEffect = hiddenEffect;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/effect/MobEffectInstance$Details;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #346	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/effect/MobEffectInstance$Details;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/effect/MobEffectInstance$Details;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #346	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/effect/MobEffectInstance$Details;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/effect/MobEffectInstance$Details;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #346	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/effect/MobEffectInstance$Details;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int amplifier() {
/* 346 */     return this.amplifier;
/*     */   }
/*     */   
/*     */   public int duration() {
/* 346 */     return this.duration;
/*     */   }
/*     */   
/*     */   public boolean ambient() {
/* 346 */     return this.ambient;
/*     */   }
/*     */   
/*     */   public boolean showParticles() {
/* 346 */     return this.showParticles;
/*     */   }
/*     */   
/*     */   public boolean showIcon() {
/* 346 */     return this.showIcon;
/*     */   }
/*     */   
/*     */   public Optional<Details> hiddenEffect() {
/* 346 */     return this.hiddenEffect;
/*     */   }
/*     */   
/* 347 */   public static final MapCodec<Details> MAP_CODEC = MapCodec.recursive("MobEffectInstance.Details", codec -> RecordCodecBuilder.mapCodec(()));
/*     */   
/*     */   public static final StreamCodec<ByteBuf, Details> STREAM_CODEC;
/*     */   
/*     */   static {
/* 357 */     STREAM_CODEC = StreamCodec.recursive(subCodec -> StreamCodec.composite(ByteBufCodecs.VAR_INT, Details::amplifier, ByteBufCodecs.VAR_INT, Details::duration, ByteBufCodecs.BOOL, Details::ambient, ByteBufCodecs.BOOL, Details::showParticles, ByteBufCodecs.BOOL, Details::showIcon, subCodec.apply(ByteBufCodecs::optional), Details::hiddenEffect, Details::new));
/*     */   }
/*     */   
/*     */   private static Details create(int amplifier, int duration, boolean ambient, boolean showParticles, Optional<Boolean> showIcon, Optional<Details> hiddenEffect) {
/* 368 */     return new Details(amplifier, duration, ambient, showParticles, ((Boolean)showIcon.orElse(Boolean.valueOf(showParticles))).booleanValue(), hiddenEffect);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\MobEffectInstance$Details.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */