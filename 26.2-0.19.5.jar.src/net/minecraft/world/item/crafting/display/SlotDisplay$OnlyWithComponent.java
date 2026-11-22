/*     */ package net.minecraft.world.item.crafting.display;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public final class OnlyWithComponent extends Record implements SlotDisplay {
/*     */   private final SlotDisplay source;
/*     */   
/*     */   private final DataComponentType<?> component;
/*     */   
/*     */   public static final MapCodec<OnlyWithComponent> MAP_CODEC;
/*     */   
/*     */   public OnlyWithComponent(SlotDisplay source, DataComponentType<?> component) {
/* 178 */     this.source = source;
/* 178 */     this.component = component;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$OnlyWithComponent;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #178	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$OnlyWithComponent;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$OnlyWithComponent;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #178	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$OnlyWithComponent;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$OnlyWithComponent;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #178	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$OnlyWithComponent;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public SlotDisplay source() {
/* 178 */     return this.source;
/*     */   }
/*     */   
/*     */   public DataComponentType<?> component() {
/* 178 */     return this.component;
/*     */   }
/*     */   
/*     */   static {
/* 179 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.fieldOf("contents").forGetter(OnlyWithComponent::source), (App)DataComponentType.CODEC.fieldOf("component").forGetter(OnlyWithComponent::component)).apply((Applicative)i, OnlyWithComponent::new));
/*     */   }
/*     */   
/* 184 */   public static final StreamCodec<RegistryFriendlyByteBuf, OnlyWithComponent> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, OnlyWithComponent::source, DataComponentType.STREAM_CODEC, OnlyWithComponent::component, OnlyWithComponent::new);
/*     */   
/* 190 */   public static final SlotDisplay.Type<OnlyWithComponent> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> builder) {
/* 194 */     if (builder instanceof DisplayContentsFactory.ForStacks) {
/* 194 */       DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)builder;
/* 195 */       Objects.requireNonNull(stacks);
/* 195 */       return this.source.resolve(context, SlotDisplay.ItemStackContentsFactory.INSTANCE).filter(s -> s.has(this.component)).map(stacks::forStack);
/*     */     } 
/* 198 */     return Stream.empty();
/*     */   }
/*     */   
/*     */   public SlotDisplay.Type<OnlyWithComponent> type() {
/* 203 */     return TYPE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\SlotDisplay$OnlyWithComponent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */