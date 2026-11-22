/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentPatch;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class ItemStackTemplate extends Record implements ItemInstance {
/*     */   private final Holder<Item> item;
/*     */   
/*     */   private final int count;
/*     */   
/*     */   private final DataComponentPatch components;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/ItemStackTemplate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #29	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/ItemStackTemplate;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/ItemStackTemplate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #29	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/ItemStackTemplate;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/ItemStackTemplate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #29	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/ItemStackTemplate;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Holder<Item> item() {
/*  29 */     return this.item;
/*     */   }
/*     */   
/*     */   public int count() {
/*  29 */     return this.count;
/*     */   }
/*     */   
/*     */   public DataComponentPatch components() {
/*  29 */     return this.components;
/*     */   }
/*     */   
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final MapCodec<ItemStackTemplate> MAP_CODEC;
/*     */   
/*     */   public static final Codec<ItemStackTemplate> CODEC;
/*     */   
/*     */   static {
/*  36 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Item.CODEC.fieldOf("id").forGetter(ItemStackTemplate::item), (App)ExtraCodecs.intRange(1, 99).optionalFieldOf("count", Integer.valueOf(1)).forGetter(ItemStackTemplate::count), (App)DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(ItemStackTemplate::components)).apply((Applicative)i, ItemStackTemplate::new));
/*  42 */     CODEC = Codec.withAlternative(MAP_CODEC
/*  43 */         .codec(), Item.CODEC, item -> new ItemStackTemplate((Item)item.value()));
/*     */   }
/*     */   
/*  47 */   public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackTemplate> STREAM_CODEC = StreamCodec.composite(Item.STREAM_CODEC, ItemStackTemplate::item, ByteBufCodecs.VAR_INT, ItemStackTemplate::count, DataComponentPatch.STREAM_CODEC, ItemStackTemplate::components, ItemStackTemplate::new);
/*     */   
/*     */   public ItemStackTemplate(Item item) {
/*  55 */     this((Holder<Item>)item.builtInRegistryHolder(), 1, DataComponentPatch.EMPTY);
/*     */   }
/*     */   
/*     */   public ItemStackTemplate(Item item, int count) {
/*  59 */     this((Holder<Item>)item.builtInRegistryHolder(), count, DataComponentPatch.EMPTY);
/*     */   }
/*     */   
/*     */   public ItemStackTemplate(Item item, DataComponentPatch patch) {
/*  63 */     this((Holder<Item>)item.builtInRegistryHolder(), 1, patch);
/*     */   }
/*     */   
/*     */   public ItemStackTemplate(Holder<Item> item, int count, DataComponentPatch components) {
/*  67 */     if (count == 0 || item.is((Holder)Items.AIR.builtInRegistryHolder()))
/*  68 */       throw new IllegalStateException("Item must be non-empty"); 
/*     */     this.item = item;
/*     */     this.count = count;
/*     */     this.components = components;
/*     */   }
/*     */   
/*     */   public static ItemStackTemplate fromNonEmptyStack(ItemStack itemStack) {
/*  73 */     if (itemStack.isEmpty())
/*  74 */       throw new IllegalStateException("Stack must be non-empty"); 
/*  76 */     return fromStack(itemStack);
/*     */   }
/*     */   
/*     */   public static ItemStackTemplate fromStack(ItemStack itemStack) {
/*  80 */     return new ItemStackTemplate(itemStack.typeHolder(), itemStack.getCount(), itemStack.getComponentsPatch());
/*     */   }
/*     */   
/*     */   public ItemStackTemplate withCount(int count) {
/*  84 */     if (this.count == count)
/*  85 */       return this; 
/*  87 */     return new ItemStackTemplate(this.item, count, this.components);
/*     */   }
/*     */   
/*     */   public ItemStack create() {
/*  91 */     return validate(new ItemStack(this.item, this.count, this.components));
/*     */   }
/*     */   
/*     */   private ItemStack validate(ItemStack result) {
/*  95 */     Optional<DataResult.Error<ItemStack>> error = ItemStack.validateStrict(result).error();
/*  96 */     if (error.isPresent()) {
/*  99 */       LOGGER.warn("Can't create item stack with properties {}, error: {}", this, ((DataResult.Error)error.get()).message());
/* 100 */       return ItemStack.EMPTY;
/*     */     } 
/* 103 */     return result;
/*     */   }
/*     */   
/*     */   public ItemStack apply(DataComponentPatch additionalPatch) {
/* 107 */     return apply(this.count, additionalPatch);
/*     */   }
/*     */   
/*     */   public ItemStack apply(int count, DataComponentPatch additionalPatch) {
/* 111 */     ItemStack result = new ItemStack(this.item, count, additionalPatch);
/* 112 */     result.applyComponents(this.components);
/* 113 */     return validate(result);
/*     */   }
/*     */   
/*     */   public Holder<Item> typeHolder() {
/* 118 */     return this.item;
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 125 */     return (T)this.components.get((DataComponentGetter)this.item.components(), type);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\ItemStackTemplate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */