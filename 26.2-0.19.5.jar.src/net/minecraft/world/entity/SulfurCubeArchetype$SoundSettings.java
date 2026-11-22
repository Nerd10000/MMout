/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ 
/*    */ public final class SoundSettings extends Record {
/*    */   private final Holder<SoundEvent> hitSound;
/*    */   
/*    */   private final Holder<SoundEvent> pushSound;
/*    */   
/*    */   private final float pushSoundImpulseThreshold;
/*    */   
/*    */   private final float pushSoundCooldown;
/*    */   
/*    */   public static final Codec<SoundSettings> CODEC;
/*    */   
/*    */   public SoundSettings(Holder<SoundEvent> hitSound, Holder<SoundEvent> pushSound, float pushSoundImpulseThreshold, float pushSoundCooldown) {
/* 81 */     this.hitSound = hitSound;
/* 81 */     this.pushSound = pushSound;
/* 81 */     this.pushSoundImpulseThreshold = pushSoundImpulseThreshold;
/* 81 */     this.pushSoundCooldown = pushSoundCooldown;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/SulfurCubeArchetype$SoundSettings;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #81	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$SoundSettings;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/SulfurCubeArchetype$SoundSettings;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #81	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$SoundSettings;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/SulfurCubeArchetype$SoundSettings;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #81	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$SoundSettings;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> hitSound() {
/* 81 */     return this.hitSound;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> pushSound() {
/* 81 */     return this.pushSound;
/*    */   }
/*    */   
/*    */   public float pushSoundImpulseThreshold() {
/* 81 */     return this.pushSoundImpulseThreshold;
/*    */   }
/*    */   
/*    */   public float pushSoundCooldown() {
/* 81 */     return this.pushSoundCooldown;
/*    */   }
/*    */   
/*    */   static {
/* 82 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("hit_sound").forGetter(SoundSettings::hitSound), (App)SoundEvent.CODEC.fieldOf("push_sound").forGetter(SoundSettings::pushSound), (App)Codec.FLOAT.fieldOf("push_sound_impulse_threshold").forGetter(SoundSettings::pushSoundImpulseThreshold), (App)Codec.FLOAT.fieldOf("push_sound_cooldown").forGetter(SoundSettings::pushSoundCooldown)).apply((Applicative)i, SoundSettings::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\SulfurCubeArchetype$SoundSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */