/*    */ package net.minecraft.world.entity.animal.cow;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.RegistryFixedCodec;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ 
/*    */ public final class CowSoundVariant extends Record {
/*    */   private final Holder<SoundEvent> ambientSound;
/*    */   
/*    */   private final Holder<SoundEvent> hurtSound;
/*    */   
/*    */   private final Holder<SoundEvent> deathSound;
/*    */   
/*    */   private final Holder<SoundEvent> stepSound;
/*    */   
/*    */   public CowSoundVariant(Holder<SoundEvent> ambientSound, Holder<SoundEvent> hurtSound, Holder<SoundEvent> deathSound, Holder<SoundEvent> stepSound) {
/* 13 */     this.ambientSound = ambientSound;
/* 13 */     this.hurtSound = hurtSound;
/* 13 */     this.deathSound = deathSound;
/* 13 */     this.stepSound = stepSound;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/cow/CowSoundVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/cow/CowSoundVariant;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/cow/CowSoundVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/cow/CowSoundVariant;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/cow/CowSoundVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/cow/CowSoundVariant;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> ambientSound() {
/* 13 */     return this.ambientSound;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> hurtSound() {
/* 13 */     return this.hurtSound;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> deathSound() {
/* 13 */     return this.deathSound;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> stepSound() {
/* 13 */     return this.stepSound;
/*    */   }
/*    */   
/* 19 */   public static final Codec<CowSoundVariant> DIRECT_CODEC = codec();
/*    */   
/* 20 */   public static final Codec<CowSoundVariant> NETWORK_CODEC = codec();
/*    */   
/* 21 */   public static final Codec<Holder<CowSoundVariant>> CODEC = (Codec<Holder<CowSoundVariant>>)RegistryFixedCodec.create(Registries.COW_SOUND_VARIANT);
/*    */   
/* 22 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CowSoundVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.COW_SOUND_VARIANT);
/*    */   
/*    */   private static Codec<CowSoundVariant> codec() {
/* 25 */     return RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("ambient_sound").forGetter(CowSoundVariant::ambientSound), (App)SoundEvent.CODEC.fieldOf("hurt_sound").forGetter(CowSoundVariant::hurtSound), (App)SoundEvent.CODEC.fieldOf("death_sound").forGetter(CowSoundVariant::deathSound), (App)SoundEvent.CODEC.fieldOf("step_sound").forGetter(CowSoundVariant::stepSound)).apply((Applicative)i, CowSoundVariant::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\cow\CowSoundVariant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */