/*     */ package net.minecraft.advancements.triggers;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*     */ import net.minecraft.advancements.predicates.DataComponentMatchers;
/*     */ import net.minecraft.advancements.predicates.ItemPredicate;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*     */   private final Optional<ContextAwarePredicate> player;
/*     */   
/*     */   private final Slots slots;
/*     */   
/*     */   private final List<ItemPredicate> items;
/*     */   
/*     */   public static final Codec<TriggerInstance> CODEC;
/*     */   
/*     */   public TriggerInstance(Optional<ContextAwarePredicate> player, Slots slots, List<ItemPredicate> items) {
/*  51 */     this.player = player;
/*  51 */     this.slots = slots;
/*  51 */     this.items = items;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/InventoryChangeTrigger$TriggerInstance;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #51	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/InventoryChangeTrigger$TriggerInstance;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/InventoryChangeTrigger$TriggerInstance;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #51	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/InventoryChangeTrigger$TriggerInstance;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/InventoryChangeTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #51	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/triggers/InventoryChangeTrigger$TriggerInstance;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<ContextAwarePredicate> player() {
/*  51 */     return this.player;
/*     */   }
/*     */   
/*     */   public Slots slots() {
/*  51 */     return this.slots;
/*     */   }
/*     */   
/*     */   public List<ItemPredicate> items() {
/*  51 */     return this.items;
/*     */   }
/*     */   
/*     */   static {
/*  56 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)Slots.CODEC.optionalFieldOf("slots", Slots.ANY).forGetter(TriggerInstance::slots), (App)ItemPredicate.CODEC.listOf().optionalFieldOf("items", List.of()).forGetter(TriggerInstance::items)).apply((Applicative)i, TriggerInstance::new));
/*     */   }
/*     */   
/*     */   public static Criterion<TriggerInstance> hasItems(ItemPredicate.Builder... items) {
/*  63 */     return hasItems((ItemPredicate[])Stream.<ItemPredicate.Builder>of(items).map(ItemPredicate.Builder::build).toArray(x$0 -> new ItemPredicate[x$0]));
/*     */   }
/*     */   
/*     */   public static Criterion<TriggerInstance> hasItems(ItemPredicate... items) {
/*  67 */     return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new TriggerInstance(Optional.empty(), Slots.ANY, List.of(items)));
/*     */   }
/*     */   
/*     */   public static Criterion<TriggerInstance> hasItems(ItemLike... items) {
/*  71 */     ItemPredicate[] predicates = new ItemPredicate[items.length];
/*  72 */     for (int i = 0; i < items.length; i++) {
/*  73 */       predicates[i] = new ItemPredicate(Optional.of(HolderSet.direct(new Holder[] { (Holder)items[i].asItem().builtInRegistryHolder() }, )), MinMaxBounds.Ints.ANY, DataComponentMatchers.ANY);
/*     */     } 
/*  75 */     return hasItems(predicates);
/*     */   }
/*     */   
/*     */   public boolean matches(Inventory inventory, ItemStack changedItem, int slotsFull, int slotsEmpty, int slotsOccupied) {
/*  79 */     if (!this.slots.matches(slotsFull, slotsEmpty, slotsOccupied))
/*  80 */       return false; 
/*  83 */     if (this.items.isEmpty())
/*  84 */       return true; 
/*  89 */     if (this.items.size() == 1)
/*  90 */       return (!changedItem.isEmpty() && ((ItemPredicate)this.items.get(0)).test((ItemInstance)changedItem)); 
/*  93 */     ObjectArrayList objectArrayList = new ObjectArrayList(this.items);
/*  94 */     int count = inventory.getContainerSize();
/*  95 */     for (int slot = 0; slot < count; slot++) {
/*  96 */       if (objectArrayList.isEmpty())
/*  97 */         return true; 
/* 100 */       ItemStack itemStack = inventory.getItem(slot);
/* 101 */       if (!itemStack.isEmpty())
/* 102 */         objectArrayList.removeIf(predicate -> predicate.test((ItemInstance)itemStack)); 
/*     */     } 
/* 105 */     return objectArrayList.isEmpty();
/*     */   }
/*     */   
/*     */   public static final class Slots extends Record {
/*     */     private final MinMaxBounds.Ints occupied;
/*     */     
/*     */     private final MinMaxBounds.Ints full;
/*     */     
/*     */     private final MinMaxBounds.Ints empty;
/*     */     
/*     */     public static final Codec<Slots> CODEC;
/*     */     
/*     */     public Slots(MinMaxBounds.Ints occupied, MinMaxBounds.Ints full, MinMaxBounds.Ints empty) {
/* 108 */       this.occupied = occupied;
/* 108 */       this.full = full;
/* 108 */       this.empty = empty;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/InventoryChangeTrigger$TriggerInstance$Slots;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #108	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/InventoryChangeTrigger$TriggerInstance$Slots;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/InventoryChangeTrigger$TriggerInstance$Slots;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #108	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/InventoryChangeTrigger$TriggerInstance$Slots;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/InventoryChangeTrigger$TriggerInstance$Slots;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #108	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/triggers/InventoryChangeTrigger$TriggerInstance$Slots;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public MinMaxBounds.Ints occupied() {
/* 108 */       return this.occupied;
/*     */     }
/*     */     
/*     */     public MinMaxBounds.Ints full() {
/* 108 */       return this.full;
/*     */     }
/*     */     
/*     */     public MinMaxBounds.Ints empty() {
/* 108 */       return this.empty;
/*     */     }
/*     */     
/*     */     static {
/* 113 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Ints.CODEC.optionalFieldOf("occupied", MinMaxBounds.Ints.ANY).forGetter(Slots::occupied), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("full", MinMaxBounds.Ints.ANY).forGetter(Slots::full), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("empty", MinMaxBounds.Ints.ANY).forGetter(Slots::empty)).apply((Applicative)i, Slots::new));
/*     */     }
/*     */     
/* 119 */     public static final Slots ANY = new Slots(MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY);
/*     */     
/*     */     public boolean matches(int slotsFull, int slotsEmpty, int slotsOccupied) {
/* 122 */       if (!this.full.matches(slotsFull))
/* 123 */         return false; 
/* 125 */       if (!this.empty.matches(slotsEmpty))
/* 126 */         return false; 
/* 128 */       if (!this.occupied.matches(slotsOccupied))
/* 129 */         return false; 
/* 131 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\InventoryChangeTrigger$TriggerInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */