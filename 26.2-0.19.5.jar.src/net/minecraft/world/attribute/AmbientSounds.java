/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class AmbientSounds extends Record {
/*    */   private final Optional<Holder<SoundEvent>> loop;
/*    */   
/*    */   private final Optional<AmbientMoodSettings> mood;
/*    */   
/*    */   private final List<AmbientAdditionsSettings> additions;
/*    */   
/*    */   public AmbientSounds(Optional<Holder<SoundEvent>> loop, Optional<AmbientMoodSettings> mood, List<AmbientAdditionsSettings> additions) {
/* 12 */     this.loop = loop;
/* 12 */     this.mood = mood;
/* 12 */     this.additions = additions;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/AmbientSounds;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientSounds;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/AmbientSounds;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientSounds;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/AmbientSounds;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/AmbientSounds;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<Holder<SoundEvent>> loop() {
/* 12 */     return this.loop;
/*    */   }
/*    */   
/*    */   public Optional<AmbientMoodSettings> mood() {
/* 12 */     return this.mood;
/*    */   }
/*    */   
/*    */   public List<AmbientAdditionsSettings> additions() {
/* 12 */     return this.additions;
/*    */   }
/*    */   
/* 17 */   public static final AmbientSounds EMPTY = new AmbientSounds(Optional.empty(), Optional.empty(), List.of());
/*    */   
/* 18 */   public static final AmbientSounds LEGACY_CAVE_SETTINGS = new AmbientSounds(Optional.empty(), Optional.of(AmbientMoodSettings.LEGACY_CAVE_SETTINGS), List.of());
/*    */   
/*    */   public static final Codec<AmbientSounds> CODEC;
/*    */   
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.optionalFieldOf("loop").forGetter(AmbientSounds::loop), (App)AmbientMoodSettings.CODEC.optionalFieldOf("mood").forGetter(AmbientSounds::mood), (App)ExtraCodecs.compactListCodec(AmbientAdditionsSettings.CODEC).optionalFieldOf("additions", List.of()).forGetter(AmbientSounds::additions)).apply((Applicative)i, AmbientSounds::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\AmbientSounds.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */