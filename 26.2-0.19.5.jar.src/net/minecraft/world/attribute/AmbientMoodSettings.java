/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ 
/*    */ public final class AmbientMoodSettings extends Record {
/*    */   private final Holder<SoundEvent> soundEvent;
/*    */   
/*    */   private final int tickDelay;
/*    */   
/*    */   private final int blockSearchExtent;
/*    */   
/*    */   private final double soundPositionOffset;
/*    */   
/*    */   public static final Codec<AmbientMoodSettings> CODEC;
/*    */   
/*    */   public AmbientMoodSettings(Holder<SoundEvent> soundEvent, int tickDelay, int blockSearchExtent, double soundPositionOffset) {
/* 10 */     this.soundEvent = soundEvent;
/* 10 */     this.tickDelay = tickDelay;
/* 10 */     this.blockSearchExtent = blockSearchExtent;
/* 10 */     this.soundPositionOffset = soundPositionOffset;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/AmbientMoodSettings;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientMoodSettings;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/AmbientMoodSettings;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientMoodSettings;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/AmbientMoodSettings;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/AmbientMoodSettings;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> soundEvent() {
/* 10 */     return this.soundEvent;
/*    */   }
/*    */   
/*    */   public int tickDelay() {
/* 10 */     return this.tickDelay;
/*    */   }
/*    */   
/*    */   public int blockSearchExtent() {
/* 10 */     return this.blockSearchExtent;
/*    */   }
/*    */   
/*    */   public double soundPositionOffset() {
/* 10 */     return this.soundPositionOffset;
/*    */   }
/*    */   
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("sound").forGetter(()), (App)Codec.INT.fieldOf("tick_delay").forGetter(()), (App)Codec.INT.fieldOf("block_search_extent").forGetter(()), (App)Codec.DOUBLE.fieldOf("offset").forGetter(())).apply((Applicative)i, AmbientMoodSettings::new));
/*    */   }
/*    */   
/* 18 */   public static final AmbientMoodSettings LEGACY_CAVE_SETTINGS = new AmbientMoodSettings((Holder<SoundEvent>)SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0D);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\AmbientMoodSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */