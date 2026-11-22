/*     */ package net.minecraft.world.item.crafting.display;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ 
/*     */ public final class ItemStackSlotDisplay extends Record implements SlotDisplay {
/*     */   private final ItemStackTemplate stack;
/*     */   
/*     */   public static final MapCodec<ItemStackSlotDisplay> MAP_CODEC;
/*     */   
/*     */   public ItemStackSlotDisplay(ItemStackTemplate stack) {
/* 354 */     this.stack = stack;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #354	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #354	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #354	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ItemStackTemplate stack() {
/* 354 */     return this.stack;
/*     */   }
/*     */   
/*     */   static {
/* 355 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ItemStackTemplate.CODEC.fieldOf("item").forGetter(ItemStackSlotDisplay::stack)).apply((Applicative)i, ItemStackSlotDisplay::new));
/*     */   }
/*     */   
/* 359 */   public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackSlotDisplay> STREAM_CODEC = StreamCodec.composite(ItemStackTemplate.STREAM_CODEC, ItemStackSlotDisplay::stack, ItemStackSlotDisplay::new);
/*     */   
/* 364 */   public static final SlotDisplay.Type<ItemStackSlotDisplay> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   public SlotDisplay.Type<ItemStackSlotDisplay> type() {
/* 368 */     return TYPE;
/*     */   }
/*     */   
/*     */   public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 373 */     if (factory instanceof DisplayContentsFactory.ForStacks) {
/* 373 */       DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 374 */       return Stream.of(stacks.forStack(this.stack.create()));
/*     */     } 
/* 376 */     return Stream.empty();
/*     */   }
/*     */   
/*     */   public boolean isEnabled(FeatureFlagSet enabledFeatures) {
/* 381 */     return ((Item)this.stack.item().value()).isEnabled(enabledFeatures);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\SlotDisplay$ItemStackSlotDisplay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */