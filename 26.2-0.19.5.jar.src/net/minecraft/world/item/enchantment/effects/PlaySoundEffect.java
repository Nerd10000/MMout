/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.util.valueproviders.FloatProviders;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class PlaySoundEffect extends Record implements EnchantmentEntityEffect {
/*    */   private final List<Holder<SoundEvent>> soundEvents;
/*    */   
/*    */   private final FloatProvider volume;
/*    */   
/*    */   private final FloatProvider pitch;
/*    */   
/*    */   public static final MapCodec<PlaySoundEffect> CODEC;
/*    */   
/*    */   public PlaySoundEffect(List<Holder<SoundEvent>> soundEvents, FloatProvider volume, FloatProvider pitch) {
/* 19 */     this.soundEvents = soundEvents;
/* 19 */     this.volume = volume;
/* 19 */     this.pitch = pitch;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/PlaySoundEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/PlaySoundEffect;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/PlaySoundEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/PlaySoundEffect;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/PlaySoundEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/PlaySoundEffect;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<Holder<SoundEvent>> soundEvents() {
/* 19 */     return this.soundEvents;
/*    */   }
/*    */   
/*    */   public FloatProvider volume() {
/* 19 */     return this.volume;
/*    */   }
/*    */   
/*    */   public FloatProvider pitch() {
/* 19 */     return this.pitch;
/*    */   }
/*    */   
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.compactListCodec(SoundEvent.CODEC, SoundEvent.CODEC.sizeLimitedListOf(255)).fieldOf("sound").forGetter(PlaySoundEffect::soundEvents), (App)FloatProviders.codec(1.0E-5F, 10.0F).fieldOf("volume").forGetter(PlaySoundEffect::volume), (App)FloatProviders.codec(1.0E-5F, 2.0F).fieldOf("pitch").forGetter(PlaySoundEffect::pitch)).apply((Applicative)i, PlaySoundEffect::new));
/*    */   }
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 32 */     if (entity.isSilent())
/*    */       return; 
/* 35 */     RandomSource random = entity.getRandom();
/* 36 */     int index = Mth.clamp(enchantmentLevel - 1, 0, this.soundEvents.size() - 1);
/* 37 */     serverLevel.playSound(null, position.x(), position.y(), position.z(), this.soundEvents.get(index), entity.getSoundSource(), this.volume.sample(random), this.pitch.sample(random));
/*    */   }
/*    */   
/*    */   public MapCodec<PlaySoundEffect> codec() {
/* 42 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\PlaySoundEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */