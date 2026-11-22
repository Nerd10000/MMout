/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ 
/*     */ public final class ItemContainerContents implements TooltipProvider {
/*     */   private static final int NO_SLOT = -1;
/*     */   
/*     */   private static final int MAX_SIZE = 256;
/*     */   
/*  29 */   public static final ItemContainerContents EMPTY = new ItemContainerContents(List.of());
/*     */   
/*  31 */   public static final Codec<ItemContainerContents> CODEC = Slot.CODEC.sizeLimitedListOf(256).xmap(ItemContainerContents::fromSlots, ItemContainerContents::asSlots);
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, ItemContainerContents> STREAM_CODEC;
/*     */   
/*     */   private final List<Optional<ItemStackTemplate>> items;
/*     */   
/*     */   private final int hashCode;
/*     */   
/*     */   static {
/*  33 */     STREAM_CODEC = ItemStackTemplate.STREAM_CODEC.apply(ByteBufCodecs::optional).apply(ByteBufCodecs.list(256)).map(ItemContainerContents::new, c -> c.items);
/*     */   }
/*     */   
/*     */   private ItemContainerContents(List<Optional<ItemStackTemplate>> items) {
/*  41 */     if (items.size() > 256)
/*  42 */       throw new IllegalArgumentException("Got " + items.size() + " items, but maximum is 256"); 
/*  44 */     this.items = items;
/*  45 */     this.hashCode = items.hashCode();
/*     */   }
/*     */   
/*     */   private static List<Optional<ItemStackTemplate>> emptyContents(int size) {
/*  49 */     return new ArrayList<>(Collections.nCopies(size, Optional.empty()));
/*     */   }
/*     */   
/*     */   private static ItemContainerContents fromSlots(List<Slot> slots) {
/*  53 */     OptionalInt maxSlotIndex = slots.stream().mapToInt(Slot::index).max();
/*  55 */     if (maxSlotIndex.isEmpty())
/*  56 */       return EMPTY; 
/*  59 */     List<Optional<ItemStackTemplate>> items = emptyContents(maxSlotIndex.getAsInt() + 1);
/*  60 */     for (Slot slot : slots)
/*  61 */       items.set(slot.index(), Optional.of(slot.item())); 
/*  63 */     return new ItemContainerContents(items);
/*     */   }
/*     */   
/*     */   public static ItemContainerContents fromItems(List<ItemStack> itemStacks) {
/*  67 */     int lastNonEmptySlot = findLastNonEmptySlot(itemStacks);
/*  68 */     if (lastNonEmptySlot == -1)
/*  69 */       return EMPTY; 
/*  72 */     List<Optional<ItemStackTemplate>> items = emptyContents(lastNonEmptySlot + 1);
/*  73 */     for (int i = 0; i <= lastNonEmptySlot; i++) {
/*  74 */       ItemStack sourceStack = itemStacks.get(i);
/*  75 */       if (!sourceStack.isEmpty())
/*  76 */         items.set(i, Optional.of(ItemStackTemplate.fromNonEmptyStack(sourceStack))); 
/*     */     } 
/*  79 */     return new ItemContainerContents(items);
/*     */   }
/*     */   
/*     */   private static int findLastNonEmptySlot(List<ItemStack> itemStacks) {
/*  83 */     for (int i = itemStacks.size() - 1; i >= 0; i--) {
/*  84 */       if (!((ItemStack)itemStacks.get(i)).isEmpty())
/*  85 */         return i; 
/*     */     } 
/*  88 */     return -1;
/*     */   }
/*     */   
/*     */   private List<Slot> asSlots() {
/*  92 */     List<Slot> slots = new ArrayList<>();
/*  93 */     for (int i = 0; i < this.items.size(); i++) {
/*  94 */       Optional<ItemStackTemplate> item = this.items.get(i);
/*  95 */       if (item.isPresent())
/*  96 */         slots.add(new Slot(i, item.get())); 
/*     */     } 
/*  99 */     return slots;
/*     */   }
/*     */   
/*     */   private ItemStack createStackFromSlot(int slot) {
/* 103 */     if (slot < this.items.size()) {
/* 104 */       Optional<ItemStackTemplate> slotContents = this.items.get(slot);
/* 105 */       if (slotContents.isPresent())
/* 106 */         return ((ItemStackTemplate)slotContents.get()).create(); 
/*     */     } 
/* 109 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   public void copyInto(NonNullList<ItemStack> destination) {
/* 113 */     for (int i = 0; i < destination.size(); i++)
/* 114 */       destination.set(i, createStackFromSlot(i)); 
/*     */   }
/*     */   
/*     */   public ItemStack copyOne() {
/* 119 */     return createStackFromSlot(0);
/*     */   }
/*     */   
/*     */   public Stream<ItemStack> allItemsCopyStream() {
/* 123 */     return this.items.stream().map(i -> (ItemStack)i.map(ItemStackTemplate::create).orElse(ItemStack.EMPTY));
/*     */   }
/*     */   
/*     */   private Stream<ItemStackTemplate> nonEmptyItemsStream() {
/* 127 */     return this.items.stream().flatMap(Optional::stream);
/*     */   }
/*     */   
/*     */   public Stream<ItemStack> nonEmptyItemCopyStream() {
/* 131 */     return nonEmptyItemsStream().map(ItemStackTemplate::create);
/*     */   }
/*     */   
/*     */   public Iterable<ItemStackTemplate> nonEmptyItems() {
/* 135 */     return () -> nonEmptyItemsStream().iterator();
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 140 */     if (this == obj)
/* 141 */       return true; 
/* 143 */     if (obj instanceof ItemContainerContents) {
/* 143 */       ItemContainerContents contents = (ItemContainerContents)obj;
/* 143 */       if (this.items.equals(contents.items));
/*     */     } 
/* 143 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 148 */     return this.hashCode;
/*     */   }
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 153 */     int lineCount = 0;
/* 154 */     int itemCount = 0;
/* 155 */     for (Optional<ItemStackTemplate> item : this.items) {
/* 156 */       if (item.isEmpty())
/*     */         continue; 
/* 159 */       itemCount++;
/* 160 */       if (lineCount > 4)
/*     */         continue; 
/* 163 */       lineCount++;
/* 164 */       ItemStack itemStack = ((ItemStackTemplate)item.get()).create();
/* 165 */       consumer.accept(Component.translatable("item.container.item_count", new Object[] { itemStack.getHoverName(), Integer.valueOf(itemStack.getCount()) }));
/*     */     } 
/* 168 */     if (itemCount - lineCount > 0)
/* 169 */       consumer.accept(Component.translatable("item.container.more_items", new Object[] { Integer.valueOf(itemCount - lineCount) }).withStyle(ChatFormatting.ITALIC)); 
/*     */   }
/*     */   
/*     */   private static final class Slot extends Record {
/*     */     private final int index;
/*     */     
/*     */     private final ItemStackTemplate item;
/*     */     
/*     */     public static final Codec<Slot> CODEC;
/*     */     
/*     */     private Slot(int index, ItemStackTemplate item) {
/* 173 */       this.index = index;
/* 173 */       this.item = item;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemContainerContents$Slot;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemContainerContents$Slot;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemContainerContents$Slot;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemContainerContents$Slot;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemContainerContents$Slot;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/ItemContainerContents$Slot;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int index() {
/* 173 */       return this.index;
/*     */     }
/*     */     
/*     */     public ItemStackTemplate item() {
/* 173 */       return this.item;
/*     */     }
/*     */     
/*     */     static {
/* 174 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.intRange(0, 255).fieldOf("slot").forGetter(Slot::index), (App)ItemStackTemplate.CODEC.fieldOf("item").forGetter(Slot::item)).apply((Applicative)i, Slot::new));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\ItemContainerContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */