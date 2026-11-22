/*     */ package net.minecraft.world.item.crafting.display;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.DyedItemColor;
/*     */ 
/*     */ public final class DyedSlotDemo extends Record implements SlotDisplay {
/*     */   private final SlotDisplay dye;
/*     */   
/*     */   private final SlotDisplay target;
/*     */   
/*     */   public static final MapCodec<DyedSlotDemo> MAP_CODEC;
/*     */   
/*     */   public DyedSlotDemo(SlotDisplay dye, SlotDisplay target) {
/* 207 */     this.dye = dye;
/* 207 */     this.target = target;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$DyedSlotDemo;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #207	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$DyedSlotDemo;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$DyedSlotDemo;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #207	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$DyedSlotDemo;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$DyedSlotDemo;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #207	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$DyedSlotDemo;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public SlotDisplay dye() {
/* 207 */     return this.dye;
/*     */   }
/*     */   
/*     */   public SlotDisplay target() {
/* 207 */     return this.target;
/*     */   }
/*     */   
/*     */   static {
/* 208 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.fieldOf("dye").forGetter(DyedSlotDemo::dye), (App)SlotDisplay.CODEC.fieldOf("target").forGetter(DyedSlotDemo::target)).apply((Applicative)i, DyedSlotDemo::new));
/*     */   }
/*     */   
/* 213 */   public static final StreamCodec<RegistryFriendlyByteBuf, DyedSlotDemo> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, DyedSlotDemo::dye, SlotDisplay.STREAM_CODEC, DyedSlotDemo::target, DyedSlotDemo::new);
/*     */   
/* 219 */   public static final SlotDisplay.Type<DyedSlotDemo> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   public SlotDisplay.Type<DyedSlotDemo> type() {
/* 223 */     return TYPE;
/*     */   }
/*     */   
/*     */   public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 229 */     BinaryOperator<ItemStack> transformation = (target, dye) -> {
/*     */         DyeColor dyeValue = (DyeColor)dye.getOrDefault(DataComponents.DYE, DyeColor.WHITE);
/*     */         return DyedItemColor.applyDyes(target.copy(), List.of(dyeValue));
/*     */       };
/* 233 */     return SlotDisplay.applyDemoTransformation(context, factory, this.target, this.dye, transformation);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\SlotDisplay$DyedSlotDemo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */