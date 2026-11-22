/*     */ package net.minecraft.world.item.crafting.display;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ 
/*     */ public final class WithRemainder extends Record implements SlotDisplay {
/*     */   private final SlotDisplay input;
/*     */   
/*     */   private final SlotDisplay remainder;
/*     */   
/*     */   public static final MapCodec<WithRemainder> MAP_CODEC;
/*     */   
/*     */   public WithRemainder(SlotDisplay input, SlotDisplay remainder) {
/* 445 */     this.input = input;
/* 445 */     this.remainder = remainder;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #445	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #445	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #445	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public SlotDisplay input() {
/* 445 */     return this.input;
/*     */   }
/*     */   
/*     */   public SlotDisplay remainder() {
/* 445 */     return this.remainder;
/*     */   }
/*     */   
/*     */   static {
/* 449 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.fieldOf("input").forGetter(WithRemainder::input), (App)SlotDisplay.CODEC.fieldOf("remainder").forGetter(WithRemainder::remainder)).apply((Applicative)i, WithRemainder::new));
/*     */   }
/*     */   
/* 454 */   public static final StreamCodec<RegistryFriendlyByteBuf, WithRemainder> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, WithRemainder::input, SlotDisplay.STREAM_CODEC, WithRemainder::remainder, WithRemainder::new);
/*     */   
/* 460 */   public static final SlotDisplay.Type<WithRemainder> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   public SlotDisplay.Type<WithRemainder> type() {
/* 464 */     return TYPE;
/*     */   }
/*     */   
/*     */   public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 469 */     if (factory instanceof DisplayContentsFactory.ForRemainders) {
/* 469 */       DisplayContentsFactory.ForRemainders<T> remainders = (DisplayContentsFactory.ForRemainders<T>)factory;
/* 470 */       List<T> resolvedRemainders = this.remainder.<T>resolve(context, factory).toList();
/* 471 */       return this.input.<T>resolve(context, factory).map(input -> remainders.addRemainder(input, resolvedRemainders));
/*     */     } 
/* 473 */     return this.input.resolve(context, factory);
/*     */   }
/*     */   
/*     */   public boolean isEnabled(FeatureFlagSet enabledFeatures) {
/* 478 */     return (this.input.isEnabled(enabledFeatures) && this.remainder.isEnabled(enabledFeatures));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\SlotDisplay$WithRemainder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */