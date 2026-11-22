/*     */ package net.minecraft.world.item.crafting.display;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.item.Item;
/*     */ 
/*     */ public final class TagSlotDisplay extends Record implements SlotDisplay {
/*     */   private final TagKey<Item> tag;
/*     */   
/*     */   public static final MapCodec<TagSlotDisplay> MAP_CODEC;
/*     */   
/*     */   public TagSlotDisplay(TagKey<Item> tag) {
/* 385 */     this.tag = tag;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #385	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #385	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #385	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public TagKey<Item> tag() {
/* 385 */     return this.tag;
/*     */   }
/*     */   
/*     */   static {
/* 386 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(TagSlotDisplay::tag)).apply((Applicative)i, TagSlotDisplay::new));
/*     */   }
/*     */   
/* 390 */   public static final StreamCodec<RegistryFriendlyByteBuf, TagSlotDisplay> STREAM_CODEC = StreamCodec.composite(
/* 391 */       TagKey.streamCodec(Registries.ITEM), TagSlotDisplay::tag, TagSlotDisplay::new);
/*     */   
/* 395 */   public static final SlotDisplay.Type<TagSlotDisplay> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   public SlotDisplay.Type<TagSlotDisplay> type() {
/* 399 */     return TYPE;
/*     */   }
/*     */   
/*     */   public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 404 */     if (factory instanceof DisplayContentsFactory.ForStacks) {
/* 404 */       DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 405 */       HolderLookup.Provider registries = (HolderLookup.Provider)context.getOptional(SlotDisplayContext.REGISTRIES);
/* 406 */       if (registries != null)
/* 407 */         return registries.lookupOrThrow(Registries.ITEM)
/* 408 */           .get(this.tag)
/* 409 */           .map(t -> {
/*     */               Objects.requireNonNull(stacks);
/*     */               return t.stream().map(stacks::forStack);
/* 410 */             }).stream().flatMap(s -> s); 
/*     */     } 
/* 413 */     return Stream.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\SlotDisplay$TagSlotDisplay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */