/*     */ package net.minecraft.world.item.crafting.display;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ 
/*     */ public final class ItemSlotDisplay extends Record implements SlotDisplay {
/*     */   private final Holder<Item> item;
/*     */   
/*     */   public static final MapCodec<ItemSlotDisplay> MAP_CODEC;
/*     */   
/*     */   public ItemSlotDisplay(Holder<Item> item) {
/* 319 */     this.item = item;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #319	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #319	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #319	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Holder<Item> item() {
/* 319 */     return this.item;
/*     */   }
/*     */   
/*     */   static {
/* 320 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Item.CODEC.fieldOf("item").forGetter(ItemSlotDisplay::item)).apply((Applicative)i, ItemSlotDisplay::new));
/*     */   }
/*     */   
/* 324 */   public static final StreamCodec<RegistryFriendlyByteBuf, ItemSlotDisplay> STREAM_CODEC = StreamCodec.composite(Item.STREAM_CODEC, ItemSlotDisplay::item, ItemSlotDisplay::new);
/*     */   
/* 329 */   public static final SlotDisplay.Type<ItemSlotDisplay> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   public SlotDisplay.Type<ItemSlotDisplay> type() {
/* 333 */     return TYPE;
/*     */   }
/*     */   
/*     */   public ItemSlotDisplay(Item item) {
/* 337 */     this((Holder<Item>)item.builtInRegistryHolder());
/*     */   }
/*     */   
/*     */   public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 342 */     if (factory instanceof DisplayContentsFactory.ForStacks) {
/* 342 */       DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 343 */       return Stream.of(stacks.forStack(this.item));
/*     */     } 
/* 345 */     return Stream.empty();
/*     */   }
/*     */   
/*     */   public boolean isEnabled(FeatureFlagSet enabledFeatures) {
/* 350 */     return ((Item)this.item.value()).isEnabled(enabledFeatures);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\SlotDisplay$ItemSlotDisplay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */