/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class UseEffects extends Record {
/*    */   private final boolean canSprint;
/*    */   
/*    */   private final boolean interactVibrations;
/*    */   
/*    */   private final float speedMultiplier;
/*    */   
/*    */   public UseEffects(boolean canSprint, boolean interactVibrations, float speedMultiplier) {
/*  9 */     this.canSprint = canSprint;
/*  9 */     this.interactVibrations = interactVibrations;
/*  9 */     this.speedMultiplier = speedMultiplier;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/UseEffects;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/UseEffects;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/UseEffects;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/UseEffects;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/UseEffects;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/UseEffects;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean canSprint() {
/*  9 */     return this.canSprint;
/*    */   }
/*    */   
/*    */   public boolean interactVibrations() {
/*  9 */     return this.interactVibrations;
/*    */   }
/*    */   
/*    */   public float speedMultiplier() {
/*  9 */     return this.speedMultiplier;
/*    */   }
/*    */   
/* 14 */   public static final UseEffects DEFAULT = new UseEffects(false, true, 0.2F);
/*    */   
/*    */   public static final Codec<UseEffects> CODEC;
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.optionalFieldOf("can_sprint", Boolean.valueOf(DEFAULT.canSprint)).forGetter(UseEffects::canSprint), (App)Codec.BOOL.optionalFieldOf("interact_vibrations", Boolean.valueOf(DEFAULT.interactVibrations)).forGetter(UseEffects::interactVibrations), (App)Codec.floatRange(0.0F, 1.0F).optionalFieldOf("speed_multiplier", Float.valueOf(DEFAULT.speedMultiplier)).forGetter(UseEffects::speedMultiplier)).apply((Applicative)i, UseEffects::new));
/*    */   }
/*    */   
/* 21 */   public static final StreamCodec<ByteBuf, UseEffects> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, UseEffects::canSprint, ByteBufCodecs.BOOL, UseEffects::interactVibrations, ByteBufCodecs.FLOAT, UseEffects::speedMultiplier, UseEffects::new);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\UseEffects.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */