/*     */ package net.minecraft.world.item.crafting.display;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.SmithingTrimRecipe;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ 
/*     */ public final class SmithingTrimDemoSlotDisplay extends Record implements SlotDisplay {
/*     */   private final SlotDisplay base;
/*     */   
/*     */   private final SlotDisplay material;
/*     */   
/*     */   private final Holder<TrimPattern> pattern;
/*     */   
/*     */   public static final MapCodec<SmithingTrimDemoSlotDisplay> MAP_CODEC;
/*     */   
/*     */   public SmithingTrimDemoSlotDisplay(SlotDisplay base, SlotDisplay material, Holder<TrimPattern> pattern) {
/* 237 */     this.base = base;
/* 237 */     this.material = material;
/* 237 */     this.pattern = pattern;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #237	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #237	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #237	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public SlotDisplay base() {
/* 237 */     return this.base;
/*     */   }
/*     */   
/*     */   public SlotDisplay material() {
/* 237 */     return this.material;
/*     */   }
/*     */   
/*     */   public Holder<TrimPattern> pattern() {
/* 237 */     return this.pattern;
/*     */   }
/*     */   
/*     */   static {
/* 243 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.fieldOf("base").forGetter(SmithingTrimDemoSlotDisplay::base), (App)SlotDisplay.CODEC.fieldOf("material").forGetter(SmithingTrimDemoSlotDisplay::material), (App)TrimPattern.CODEC.fieldOf("pattern").forGetter(SmithingTrimDemoSlotDisplay::pattern)).apply((Applicative)i, SmithingTrimDemoSlotDisplay::new));
/*     */   }
/*     */   
/* 249 */   public static final StreamCodec<RegistryFriendlyByteBuf, SmithingTrimDemoSlotDisplay> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, SmithingTrimDemoSlotDisplay::base, SlotDisplay.STREAM_CODEC, SmithingTrimDemoSlotDisplay::material, TrimPattern.STREAM_CODEC, SmithingTrimDemoSlotDisplay::pattern, SmithingTrimDemoSlotDisplay::new);
/*     */   
/* 256 */   public static final SlotDisplay.Type<SmithingTrimDemoSlotDisplay> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   public SlotDisplay.Type<SmithingTrimDemoSlotDisplay> type() {
/* 260 */     return TYPE;
/*     */   }
/*     */   
/*     */   public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 266 */     RandomSource randomSource = RandomSource.createThreadLocalInstance(System.identityHashCode(this));
/* 267 */     BinaryOperator<ItemStack> transformation = (base, material) -> SmithingTrimRecipe.applyTrim(base, material, this.pattern);
/* 268 */     return SlotDisplay.applyDemoTransformation(context, factory, this.base, this.material, randomSource, transformation);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\SlotDisplay$SmithingTrimDemoSlotDisplay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */