/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ 
/*     */ public final class ChargingSounds extends Record {
/*     */   private final Optional<Holder<SoundEvent>> start;
/*     */   
/*     */   private final Optional<Holder<SoundEvent>> mid;
/*     */   
/*     */   private final Optional<Holder<SoundEvent>> end;
/*     */   
/*     */   public static final Codec<ChargingSounds> CODEC;
/*     */   
/*     */   public ChargingSounds(Optional<Holder<SoundEvent>> start, Optional<Holder<SoundEvent>> mid, Optional<Holder<SoundEvent>> end) {
/* 268 */     this.start = start;
/* 268 */     this.mid = mid;
/* 268 */     this.end = end;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/CrossbowItem$ChargingSounds;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #268	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/CrossbowItem$ChargingSounds;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/CrossbowItem$ChargingSounds;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #268	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/CrossbowItem$ChargingSounds;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/CrossbowItem$ChargingSounds;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #268	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/CrossbowItem$ChargingSounds;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<Holder<SoundEvent>> start() {
/* 268 */     return this.start;
/*     */   }
/*     */   
/*     */   public Optional<Holder<SoundEvent>> mid() {
/* 268 */     return this.mid;
/*     */   }
/*     */   
/*     */   public Optional<Holder<SoundEvent>> end() {
/* 268 */     return this.end;
/*     */   }
/*     */   
/*     */   static {
/* 269 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.optionalFieldOf("start").forGetter(ChargingSounds::start), (App)SoundEvent.CODEC.optionalFieldOf("mid").forGetter(ChargingSounds::mid), (App)SoundEvent.CODEC.optionalFieldOf("end").forGetter(ChargingSounds::end)).apply((Applicative)i, ChargingSounds::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\CrossbowItem$ChargingSounds.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */