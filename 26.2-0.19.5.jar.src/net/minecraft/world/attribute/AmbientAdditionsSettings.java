/*   */ package net.minecraft.world.attribute;
/*   */ 
/*   */ import com.mojang.datafixers.kinds.App;
/*   */ import com.mojang.datafixers.kinds.Applicative;
/*   */ import com.mojang.serialization.Codec;
/*   */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*   */ import java.util.function.BiFunction;
/*   */ import net.minecraft.core.Holder;
/*   */ import net.minecraft.sounds.SoundEvent;
/*   */ 
/*   */ public final class AmbientAdditionsSettings extends Record {
/*   */   private final Holder<SoundEvent> soundEvent;
/*   */   
/*   */   private final double tickChance;
/*   */   
/*   */   public static final Codec<AmbientAdditionsSettings> CODEC;
/*   */   
/*   */   public AmbientAdditionsSettings(Holder<SoundEvent> soundEvent, double tickChance) {
/* 8 */     this.soundEvent = soundEvent;
/* 8 */     this.tickChance = tickChance;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/AmbientAdditionsSettings;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientAdditionsSettings;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/AmbientAdditionsSettings;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientAdditionsSettings;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/AmbientAdditionsSettings;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/attribute/AmbientAdditionsSettings;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public Holder<SoundEvent> soundEvent() {
/* 8 */     return this.soundEvent;
/*   */   }
/*   */   
/*   */   public double tickChance() {
/* 8 */     return this.tickChance;
/*   */   }
/*   */   
/*   */   static {
/* 9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("sound").forGetter(()), (App)Codec.DOUBLE.fieldOf("tick_chance").forGetter(())).apply((Applicative)i, AmbientAdditionsSettings::new));
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\AmbientAdditionsSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */