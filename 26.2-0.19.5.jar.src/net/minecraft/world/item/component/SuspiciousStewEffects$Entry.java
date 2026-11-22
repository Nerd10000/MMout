/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ 
/*    */ public final class Entry extends Record {
/*    */   private final Holder<MobEffect> effect;
/*    */   
/*    */   private final int duration;
/*    */   
/*    */   public static final Codec<Entry> CODEC;
/*    */   
/*    */   public Entry(Holder<MobEffect> effect, int duration) {
/* 56 */     this.effect = effect;
/* 56 */     this.duration = duration;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/SuspiciousStewEffects$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #56	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/SuspiciousStewEffects$Entry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/SuspiciousStewEffects$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #56	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/SuspiciousStewEffects$Entry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/SuspiciousStewEffects$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #56	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/SuspiciousStewEffects$Entry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<MobEffect> effect() {
/* 56 */     return this.effect;
/*    */   }
/*    */   
/*    */   public int duration() {
/* 56 */     return this.duration;
/*    */   }
/*    */   
/*    */   static {
/* 57 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)MobEffect.CODEC.fieldOf("id").forGetter(Entry::effect), (App)Codec.INT.lenientOptionalFieldOf("duration", Integer.valueOf(160)).forGetter(Entry::duration)).apply((Applicative)i, Entry::new));
/*    */   }
/*    */   
/* 62 */   public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(MobEffect.STREAM_CODEC, Entry::effect, ByteBufCodecs.VAR_INT, Entry::duration, Entry::new);
/*    */   
/*    */   public MobEffectInstance createEffectInstance() {
/* 69 */     return new MobEffectInstance(this.effect, this.duration);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\SuspiciousStewEffects$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */