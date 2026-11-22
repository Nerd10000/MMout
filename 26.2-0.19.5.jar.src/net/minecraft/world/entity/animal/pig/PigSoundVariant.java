/*    */ package net.minecraft.world.entity.animal.pig;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.RegistryFixedCodec;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ 
/*    */ public final class PigSoundVariant extends Record {
/*    */   private final PigSoundSet adultSounds;
/*    */   
/*    */   private final PigSoundSet babySounds;
/*    */   
/*    */   public PigSoundVariant(PigSoundSet adultSounds, PigSoundSet babySounds) {
/* 13 */     this.adultSounds = adultSounds;
/* 13 */     this.babySounds = babySounds;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/pig/PigSoundVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/pig/PigSoundVariant;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/pig/PigSoundVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/pig/PigSoundVariant;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/pig/PigSoundVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/pig/PigSoundVariant;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public PigSoundSet adultSounds() {
/* 13 */     return this.adultSounds;
/*    */   }
/*    */   
/*    */   public PigSoundSet babySounds() {
/* 13 */     return this.babySounds;
/*    */   }
/*    */   
/* 17 */   public static final Codec<PigSoundVariant> DIRECT_CODEC = codec();
/*    */   
/* 18 */   public static final Codec<PigSoundVariant> NETWORK_CODEC = codec();
/*    */   
/* 19 */   public static final Codec<Holder<PigSoundVariant>> CODEC = (Codec<Holder<PigSoundVariant>>)RegistryFixedCodec.create(Registries.PIG_SOUND_VARIANT);
/*    */   
/* 20 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<PigSoundVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.PIG_SOUND_VARIANT);
/*    */   
/*    */   private static Codec<PigSoundVariant> codec() {
/* 23 */     return RecordCodecBuilder.create(i -> i.group((App)PigSoundSet.CODEC.fieldOf("adult_sounds").forGetter(PigSoundVariant::adultSounds), (App)PigSoundSet.CODEC.fieldOf("baby_sounds").forGetter(PigSoundVariant::babySounds)).apply((Applicative)i, PigSoundVariant::new));
/*    */   }
/*    */   
/*    */   public static final class PigSoundSet extends Record {
/*    */     private final Holder<SoundEvent> ambientSound;
/*    */     
/*    */     private final Holder<SoundEvent> hurtSound;
/*    */     
/*    */     private final Holder<SoundEvent> deathSound;
/*    */     
/*    */     private final Holder<SoundEvent> stepSound;
/*    */     
/*    */     private final Holder<SoundEvent> eatSound;
/*    */     
/*    */     public static final Codec<PigSoundSet> CODEC;
/*    */     
/*    */     public PigSoundSet(Holder<SoundEvent> ambientSound, Holder<SoundEvent> hurtSound, Holder<SoundEvent> deathSound, Holder<SoundEvent> stepSound, Holder<SoundEvent> eatSound) {
/* 29 */       this.ambientSound = ambientSound;
/* 29 */       this.hurtSound = hurtSound;
/* 29 */       this.deathSound = deathSound;
/* 29 */       this.stepSound = stepSound;
/* 29 */       this.eatSound = eatSound;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/pig/PigSoundVariant$PigSoundSet;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/animal/pig/PigSoundVariant$PigSoundSet;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/pig/PigSoundVariant$PigSoundSet;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/animal/pig/PigSoundVariant$PigSoundSet;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/pig/PigSoundVariant$PigSoundSet;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/animal/pig/PigSoundVariant$PigSoundSet;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> ambientSound() {
/* 29 */       return this.ambientSound;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> hurtSound() {
/* 29 */       return this.hurtSound;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> deathSound() {
/* 29 */       return this.deathSound;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> stepSound() {
/* 29 */       return this.stepSound;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> eatSound() {
/* 29 */       return this.eatSound;
/*    */     }
/*    */     
/*    */     static {
/* 37 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("ambient_sound").forGetter(PigSoundSet::ambientSound), (App)SoundEvent.CODEC.fieldOf("hurt_sound").forGetter(PigSoundSet::hurtSound), (App)SoundEvent.CODEC.fieldOf("death_sound").forGetter(PigSoundSet::deathSound), (App)SoundEvent.CODEC.fieldOf("step_sound").forGetter(PigSoundSet::stepSound), (App)SoundEvent.CODEC.fieldOf("eat_sound").forGetter(PigSoundSet::eatSound)).apply((Applicative)i, PigSoundSet::new));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\pig\PigSoundVariant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */