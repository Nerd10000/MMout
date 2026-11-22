/*     */ package net.minecraft.world.item.crafting.display;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ 
/*     */ public final class WithAnyPotion extends Record implements SlotDisplay {
/*     */   private final SlotDisplay display;
/*     */   
/*     */   public static final MapCodec<WithAnyPotion> MAP_CODEC;
/*     */   
/*     */   public WithAnyPotion(SlotDisplay display) {
/* 138 */     this.display = display;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithAnyPotion;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithAnyPotion;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithAnyPotion;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithAnyPotion;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithAnyPotion;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithAnyPotion;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public SlotDisplay display() {
/* 138 */     return this.display;
/*     */   }
/*     */   
/*     */   static {
/* 139 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.fieldOf("contents").forGetter(WithAnyPotion::display)).apply((Applicative)i, WithAnyPotion::new));
/*     */   }
/*     */   
/* 143 */   public static final StreamCodec<RegistryFriendlyByteBuf, WithAnyPotion> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, WithAnyPotion::display, WithAnyPotion::new);
/*     */   
/* 148 */   public static final SlotDisplay.Type<WithAnyPotion> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   public SlotDisplay.Type<WithAnyPotion> type() {
/* 152 */     return TYPE;
/*     */   }
/*     */   
/*     */   public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 157 */     if (factory instanceof DisplayContentsFactory.ForStacks) {
/* 157 */       DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 158 */       List<ItemStack> displayItems = this.display.resolveForStacks(context);
/* 161 */       Optional<? extends HolderLookup.RegistryLookup<Potion>> potions = Optional.<HolderLookup.Provider>ofNullable((HolderLookup.Provider)context.getOptional(SlotDisplayContext.REGISTRIES)).flatMap(r -> r.lookup(Registries.POTION));
/* 162 */       return potions
/* 163 */         .stream()
/* 164 */         .flatMap(HolderLookup::listElements)
/* 165 */         .flatMap(potion -> {
/*     */             PotionContents potionContents = new PotionContents((Holder)potion);
/*     */             return displayItems.stream().map(());
/*     */           });
/*     */     } 
/* 174 */     return Stream.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\SlotDisplay$WithAnyPotion.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */