/*     */ package net.minecraft.advancements.triggers;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*     */ import net.minecraft.advancements.predicates.ItemPredicate;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.Validatable;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContextSource;
/*     */ 
/*     */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*     */   private final Optional<ContextAwarePredicate> player;
/*     */   
/*     */   private final List<ContextAwarePredicate> victims;
/*     */   
/*     */   private final MinMaxBounds.Ints uniqueEntityTypes;
/*     */   
/*     */   private final Optional<ItemPredicate> firedFromWeapon;
/*     */   
/*     */   public static final Codec<TriggerInstance> CODEC;
/*     */   
/*     */   public TriggerInstance(Optional<ContextAwarePredicate> player, List<ContextAwarePredicate> victims, MinMaxBounds.Ints uniqueEntityTypes, Optional<ItemPredicate> firedFromWeapon) {
/*  46 */     this.player = player;
/*  46 */     this.victims = victims;
/*  46 */     this.uniqueEntityTypes = uniqueEntityTypes;
/*  46 */     this.firedFromWeapon = firedFromWeapon;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/KilledByArrowTrigger$TriggerInstance;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #46	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/KilledByArrowTrigger$TriggerInstance;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/KilledByArrowTrigger$TriggerInstance;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #46	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/KilledByArrowTrigger$TriggerInstance;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/KilledByArrowTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #46	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/triggers/KilledByArrowTrigger$TriggerInstance;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<ContextAwarePredicate> player() {
/*  46 */     return this.player;
/*     */   }
/*     */   
/*     */   public List<ContextAwarePredicate> victims() {
/*  46 */     return this.victims;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.Ints uniqueEntityTypes() {
/*  46 */     return this.uniqueEntityTypes;
/*     */   }
/*     */   
/*     */   public Optional<ItemPredicate> firedFromWeapon() {
/*  46 */     return this.firedFromWeapon;
/*     */   }
/*     */   
/*     */   static {
/*  52 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)EntityPredicate.ADVANCEMENT_CODEC.listOf().optionalFieldOf("victims", List.of()).forGetter(TriggerInstance::victims), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("unique_entity_types", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::uniqueEntityTypes), (App)ItemPredicate.CODEC.optionalFieldOf("fired_from_weapon").forGetter(TriggerInstance::firedFromWeapon)).apply((Applicative)i, TriggerInstance::new));
/*     */   }
/*     */   
/*     */   public static Criterion<TriggerInstance> crossbowKilled(HolderGetter<Item> items, EntityPredicate.Builder... victims) {
/*  60 */     return CriteriaTriggers.KILLED_BY_ARROW.createCriterion(new TriggerInstance(
/*  61 */           Optional.empty(), 
/*  62 */           EntityPredicate.wrap(victims), MinMaxBounds.Ints.ANY, 
/*     */           
/*  64 */           Optional.of(ItemPredicate.Builder.item()
/*  65 */             .of(items, new ItemLike[] { (ItemLike)Items.CROSSBOW }).build())));
/*     */   }
/*     */   
/*     */   public static Criterion<TriggerInstance> crossbowKilled(HolderGetter<Item> items, MinMaxBounds.Ints uniqueEntityTypes) {
/*  71 */     return CriteriaTriggers.KILLED_BY_ARROW.createCriterion(new TriggerInstance(
/*  72 */           Optional.empty(), 
/*  73 */           List.of(), uniqueEntityTypes, 
/*     */           
/*  75 */           Optional.of(ItemPredicate.Builder.item()
/*  76 */             .of(items, new ItemLike[] { (ItemLike)Items.CROSSBOW }).build())));
/*     */   }
/*     */   
/*     */   public boolean matches(Collection<LootContext> victims, int uniqueEntityTypes, ItemStack firedFromWeapon) {
/*  82 */     if (this.firedFromWeapon.isPresent() && (
/*  83 */       firedFromWeapon == null || !((ItemPredicate)this.firedFromWeapon.get()).test((ItemInstance)firedFromWeapon)))
/*  84 */       return false; 
/*  88 */     if (!this.victims.isEmpty()) {
/*  89 */       List<LootContext> victimsCopy = Lists.newArrayList(victims);
/*  90 */       for (ContextAwarePredicate predicate : this.victims) {
/*  91 */         boolean found = false;
/*  92 */         for (Iterator<LootContext> iterator = victimsCopy.iterator(); iterator.hasNext(); ) {
/*  93 */           LootContext entity = iterator.next();
/*  94 */           if (predicate.matches(entity)) {
/*  95 */             iterator.remove();
/*  96 */             found = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 101 */         if (!found)
/* 102 */           return false; 
/*     */       } 
/*     */     } 
/* 107 */     return this.uniqueEntityTypes.matches(uniqueEntityTypes);
/*     */   }
/*     */   
/*     */   public void validate(ValidationContextSource validator) {
/* 112 */     super.validate(validator);
/* 114 */     Validatable.validate(validator.entityContext(), "victims", this.victims);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\KilledByArrowTrigger$TriggerInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */