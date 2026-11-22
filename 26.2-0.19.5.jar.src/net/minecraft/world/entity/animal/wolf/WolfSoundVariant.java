/*    */ package net.minecraft.world.entity.animal.wolf;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function7;
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
/*    */ public final class WolfSoundVariant extends Record {
/*    */   private final WolfSoundSet adultSounds;
/*    */   
/*    */   private final WolfSoundSet babySounds;
/*    */   
/*    */   public WolfSoundVariant(WolfSoundSet adultSounds, WolfSoundSet babySounds) {
/* 13 */     this.adultSounds = adultSounds;
/* 13 */     this.babySounds = babySounds;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public WolfSoundSet adultSounds() {
/* 13 */     return this.adultSounds;
/*    */   }
/*    */   
/*    */   public WolfSoundSet babySounds() {
/* 13 */     return this.babySounds;
/*    */   }
/*    */   
/* 17 */   public static final Codec<WolfSoundVariant> DIRECT_CODEC = getWolfSoundVariantCodec();
/*    */   
/* 18 */   public static final Codec<WolfSoundVariant> NETWORK_CODEC = getWolfSoundVariantCodec();
/*    */   
/* 19 */   public static final Codec<Holder<WolfSoundVariant>> CODEC = (Codec<Holder<WolfSoundVariant>>)RegistryFixedCodec.create(Registries.WOLF_SOUND_VARIANT);
/*    */   
/* 20 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<WolfSoundVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.WOLF_SOUND_VARIANT);
/*    */   
/*    */   private static Codec<WolfSoundVariant> getWolfSoundVariantCodec() {
/* 23 */     return RecordCodecBuilder.create(i -> i.group((App)WolfSoundSet.CODEC.fieldOf("adult_sounds").forGetter(WolfSoundVariant::adultSounds), (App)WolfSoundSet.CODEC.fieldOf("baby_sounds").forGetter(WolfSoundVariant::babySounds)).apply((Applicative)i, WolfSoundVariant::new));
/*    */   }
/*    */   
/*    */   public static final class WolfSoundSet extends Record {
/*    */     private final Holder<SoundEvent> ambientSound;
/*    */     
/*    */     private final Holder<SoundEvent> deathSound;
/*    */     
/*    */     private final Holder<SoundEvent> growlSound;
/*    */     
/*    */     private final Holder<SoundEvent> hurtSound;
/*    */     
/*    */     private final Holder<SoundEvent> pantSound;
/*    */     
/*    */     private final Holder<SoundEvent> whineSound;
/*    */     
/*    */     private final Holder<SoundEvent> stepSound;
/*    */     
/*    */     public static final Codec<WolfSoundSet> CODEC;
/*    */     
/*    */     public WolfSoundSet(Holder<SoundEvent> ambientSound, Holder<SoundEvent> deathSound, Holder<SoundEvent> growlSound, Holder<SoundEvent> hurtSound, Holder<SoundEvent> pantSound, Holder<SoundEvent> whineSound, Holder<SoundEvent> stepSound) {
/* 29 */       this.ambientSound = ambientSound;
/* 29 */       this.deathSound = deathSound;
/* 29 */       this.growlSound = growlSound;
/* 29 */       this.hurtSound = hurtSound;
/* 29 */       this.pantSound = pantSound;
/* 29 */       this.whineSound = whineSound;
/* 29 */       this.stepSound = stepSound;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant$WolfSoundSet;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant$WolfSoundSet;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant$WolfSoundSet;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant$WolfSoundSet;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant$WolfSoundSet;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant$WolfSoundSet;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> ambientSound() {
/* 29 */       return this.ambientSound;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> deathSound() {
/* 29 */       return this.deathSound;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> growlSound() {
/* 29 */       return this.growlSound;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> hurtSound() {
/* 29 */       return this.hurtSound;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> pantSound() {
/* 29 */       return this.pantSound;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> whineSound() {
/* 29 */       return this.whineSound;
/*    */     }
/*    */     
/*    */     public Holder<SoundEvent> stepSound() {
/* 29 */       return this.stepSound;
/*    */     }
/*    */     
/*    */     static {
/* 38 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("ambient_sound").forGetter(WolfSoundSet::ambientSound), (App)SoundEvent.CODEC.fieldOf("death_sound").forGetter(WolfSoundSet::deathSound), (App)SoundEvent.CODEC.fieldOf("growl_sound").forGetter(WolfSoundSet::growlSound), (App)SoundEvent.CODEC.fieldOf("hurt_sound").forGetter(WolfSoundSet::hurtSound), (App)SoundEvent.CODEC.fieldOf("pant_sound").forGetter(WolfSoundSet::pantSound), (App)SoundEvent.CODEC.fieldOf("whine_sound").forGetter(WolfSoundSet::whineSound), (App)SoundEvent.CODEC.fieldOf("step_sound").forGetter(WolfSoundSet::stepSound)).apply((Applicative)i, WolfSoundSet::new));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\wolf\WolfSoundVariant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */