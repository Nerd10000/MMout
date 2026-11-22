/*    */ package net.minecraft.world.entity.animal.chicken;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ 
/*    */ public final class ChickenSoundSet extends Record {
/*    */   private final Holder<SoundEvent> ambientSound;
/*    */   
/*    */   private final Holder<SoundEvent> hurtSound;
/*    */   
/*    */   private final Holder<SoundEvent> deathSound;
/*    */   
/*    */   private final Holder<SoundEvent> stepSound;
/*    */   
/*    */   private static final Codec<ChickenSoundSet> CODEC;
/*    */   
/*    */   public ChickenSoundSet(Holder<SoundEvent> ambientSound, Holder<SoundEvent> hurtSound, Holder<SoundEvent> deathSound, Holder<SoundEvent> stepSound) {
/* 29 */     this.ambientSound = ambientSound;
/* 29 */     this.hurtSound = hurtSound;
/* 29 */     this.deathSound = deathSound;
/* 29 */     this.stepSound = stepSound;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/chicken/ChickenSoundVariant$ChickenSoundSet;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/chicken/ChickenSoundVariant$ChickenSoundSet;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/chicken/ChickenSoundVariant$ChickenSoundSet;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/chicken/ChickenSoundVariant$ChickenSoundSet;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/chicken/ChickenSoundVariant$ChickenSoundSet;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/chicken/ChickenSoundVariant$ChickenSoundSet;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> ambientSound() {
/* 29 */     return this.ambientSound;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> hurtSound() {
/* 29 */     return this.hurtSound;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> deathSound() {
/* 29 */     return this.deathSound;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> stepSound() {
/* 29 */     return this.stepSound;
/*    */   }
/*    */   
/*    */   static {
/* 36 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("ambient_sound").forGetter(ChickenSoundSet::ambientSound), (App)SoundEvent.CODEC.fieldOf("hurt_sound").forGetter(ChickenSoundSet::hurtSound), (App)SoundEvent.CODEC.fieldOf("death_sound").forGetter(ChickenSoundSet::deathSound), (App)SoundEvent.CODEC.fieldOf("step_sound").forGetter(ChickenSoundSet::stepSound)).apply((Applicative)i, ChickenSoundSet::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\chicken\ChickenSoundVariant$ChickenSoundSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */