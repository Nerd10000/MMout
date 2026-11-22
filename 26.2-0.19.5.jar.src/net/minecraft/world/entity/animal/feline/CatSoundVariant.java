/*    */ package net.minecraft.world.entity.animal.feline;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function9;
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
/*    */ public final class CatSoundVariant extends Record {
/*    */   private final CatSoundSet adultSounds;
/*    */   
/*    */   private final CatSoundSet babySounds;
/*    */   
/*    */   public CatSoundVariant(CatSoundSet adultSounds, CatSoundSet babySounds) {
/* 13 */     this.adultSounds = adultSounds;
/* 13 */     this.babySounds = babySounds;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/feline/CatSoundVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/feline/CatSoundVariant;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/feline/CatSoundVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/feline/CatSoundVariant;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/feline/CatSoundVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/feline/CatSoundVariant;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public CatSoundSet adultSounds() {
/* 13 */     return this.adultSounds;
/*    */   }
/*    */   
/*    */   public CatSoundSet babySounds() {
/* 13 */     return this.babySounds;
/*    */   }
/*    */   
/* 17 */   public static final Codec<CatSoundVariant> DIRECT_CODEC = codec();
/*    */   
/* 18 */   public static final Codec<CatSoundVariant> NETWORK_CODEC = codec();
/*    */   
/* 19 */   public static final Codec<Holder<CatSoundVariant>> CODEC = (Codec<Holder<CatSoundVariant>>)RegistryFixedCodec.create(Registries.CAT_SOUND_VARIANT);
/*    */   
/* 20 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CatSoundVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.CAT_SOUND_VARIANT);
/*    */   
/*    */   private static Codec<CatSoundVariant> codec() {
/* 23 */     return RecordCodecBuilder.create(i -> i.group((App)CatSoundSet.CODEC.fieldOf("adult_sounds").forGetter(CatSoundVariant::adultSounds), (App)CatSoundSet.CODEC.fieldOf("baby_sounds").forGetter(CatSoundVariant::babySounds)).apply((Applicative)i, CatSoundVariant::new));
/*    */   }
/*    */   
/*    */   public static final class CatSoundSet extends Record {
/*    */     private final Holder<SoundEvent> ambientSound;
/*    */     
/*    */     private final Holder<SoundEvent> strayAmbientSound;
/*    */     
/*    */     private final Holder<SoundEvent> hissSound;
/*    */     
/*    */     private final Holder<SoundEvent> hurtSound;
/*    */     
/*    */     private final Holder<SoundEvent> deathSound;
/*    */     
/*    */     private final Holder<SoundEvent> eatSound;
/*    */     
/*    */     private final Holder<SoundEvent> begForFoodSound;
/*    */     
/*    */     private final Holder<SoundEvent> purrSound;
/*    */     
/*    */     private final Holder<SoundEvent> purreowSound;
/*    */     
/*    */     private static final Codec<CatSoundSet> CODEC;
/*    */     
/*    */     public CatSoundSet(Holder<SoundEvent> ambientSound, Holder<SoundEvent> strayAmbientSound, Holder<SoundEvent> hissSound, Holder<SoundEvent> hurtSound, Holder<SoundEvent> deathSound, Holder<SoundEvent> eatSound, Holder<SoundEvent> begForFoodSound, Holder<SoundEvent> purrSound, Holder<SoundEvent> purreowSound) {
/* 29 */       this.ambientSound = ambientSound;
/* 29 */       this.strayAmbientSound = strayAmbientSound;
/* 29 */       this.hissSound = hissSound;
/* 29 */       this.hurtSound = hurtSound;
/* 29 */       this.deathSound = deathSound;
/* 29 */       this.eatSound = eatSound;
/* 29 */       this.begForFoodSound = begForFoodSound;
/* 29 */       this.purrSound = purrSound;
/* 29 */       this.purreowSound = purreowSound;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/feline/CatSoundVariant$CatSoundSet;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/animal/feline/CatSoundVariant$CatSoundSet;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/feline/CatSoundVariant$CatSoundSet;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/animal/feline/CatSoundVariant$CatSoundSet;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/feline/CatSoundVariant$CatSoundSet;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/animal/feline/CatSoundVariant$CatSoundSet;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> ambientSound() {
/* 29 */       return this.ambientSound;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> strayAmbientSound() {
/* 29 */       return this.strayAmbientSound;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> hissSound() {
/* 29 */       return this.hissSound;
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
/*    */     public Holder<SoundEvent> eatSound() {
/* 29 */       return this.eatSound;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> begForFoodSound() {
/* 29 */       return this.begForFoodSound;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> purrSound() {
/* 29 */       return this.purrSound;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> purreowSound() {
/* 29 */       return this.purreowSound;
/*    */     }
/*    */     
/*    */     static {
/* 41 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("ambient_sound").forGetter(CatSoundSet::ambientSound), (App)SoundEvent.CODEC.fieldOf("stray_ambient_sound").forGetter(CatSoundSet::strayAmbientSound), (App)SoundEvent.CODEC.fieldOf("hiss_sound").forGetter(CatSoundSet::hissSound), (App)SoundEvent.CODEC.fieldOf("hurt_sound").forGetter(CatSoundSet::hurtSound), (App)SoundEvent.CODEC.fieldOf("death_sound").forGetter(CatSoundSet::deathSound), (App)SoundEvent.CODEC.fieldOf("eat_sound").forGetter(CatSoundSet::eatSound), (App)SoundEvent.CODEC.fieldOf("beg_for_food_sound").forGetter(CatSoundSet::begForFoodSound), (App)SoundEvent.CODEC.fieldOf("purr_sound").forGetter(CatSoundSet::purrSound), (App)SoundEvent.CODEC.fieldOf("purreow_sound").forGetter(CatSoundSet::purreowSound)).apply((Applicative)i, CatSoundSet::new));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\feline\CatSoundVariant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */