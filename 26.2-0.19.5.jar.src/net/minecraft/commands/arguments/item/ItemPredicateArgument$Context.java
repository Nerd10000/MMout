/*     */ package net.minecraft.commands.arguments.item;
/*     */ 
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ class Context implements ComponentPredicateParser.Context<Predicate<ItemStack>, ItemPredicateArgument.ComponentWrapper, ItemPredicateArgument.PredicateWrapper> {
/*     */   private final HolderLookup.Provider registries;
/*     */   
/*     */   private final HolderLookup.RegistryLookup<Item> items;
/*     */   
/*     */   private final HolderLookup.RegistryLookup<DataComponentType<?>> components;
/*     */   
/*     */   private final HolderLookup.RegistryLookup<DataComponentPredicate.Type<?>> predicates;
/*     */   
/*     */   private Context(HolderLookup.Provider registries) {
/* 121 */     this.registries = registries;
/* 122 */     this.items = registries.lookupOrThrow(Registries.ITEM);
/* 123 */     this.components = registries.lookupOrThrow(Registries.DATA_COMPONENT_TYPE);
/* 124 */     this.predicates = registries.lookupOrThrow(Registries.DATA_COMPONENT_PREDICATE_TYPE);
/*     */   }
/*     */   
/*     */   public Predicate<ItemStack> forElementType(ImmutableStringReader reader, Identifier id) throws CommandSyntaxException {
/* 129 */     Holder.Reference<Item> item = (Holder.Reference<Item>)this.items.get(ResourceKey.create(Registries.ITEM, id)).orElseThrow(() -> ItemPredicateArgument.ERROR_UNKNOWN_ITEM.createWithContext(reader, id));
/* 130 */     return itemStack -> itemStack.is((Holder)item);
/*     */   }
/*     */   
/*     */   public Predicate<ItemStack> forTagType(ImmutableStringReader reader, Identifier id) throws CommandSyntaxException {
/* 135 */     HolderSet<Item> tag = (HolderSet<Item>)this.items.get(TagKey.create(Registries.ITEM, id)).orElseThrow(() -> ItemPredicateArgument.ERROR_UNKNOWN_TAG.createWithContext(reader, id));
/* 136 */     return itemStack -> itemStack.is(tag);
/*     */   }
/*     */   
/*     */   public ItemPredicateArgument.ComponentWrapper lookupComponentType(ImmutableStringReader reader, Identifier componentId) throws CommandSyntaxException {
/* 141 */     ItemPredicateArgument.ComponentWrapper wrapper = ItemPredicateArgument.PSEUDO_COMPONENTS.get(componentId);
/* 142 */     if (wrapper != null)
/* 143 */       return wrapper; 
/* 148 */     DataComponentType<?> componentType = (DataComponentType)this.components.get(ResourceKey.create(Registries.DATA_COMPONENT_TYPE, componentId)).map(Holder::value).orElseThrow(() -> ItemPredicateArgument.ERROR_UNKNOWN_COMPONENT.createWithContext(reader, componentId));
/* 149 */     return ItemPredicateArgument.ComponentWrapper.create(reader, componentId, componentType);
/*     */   }
/*     */   
/*     */   public Predicate<ItemStack> createComponentTest(ImmutableStringReader reader, ItemPredicateArgument.ComponentWrapper componentType, Dynamic<?> value) throws CommandSyntaxException {
/* 154 */     return componentType.decode(reader, RegistryOps.injectRegistryContext(value, this.registries));
/*     */   }
/*     */   
/*     */   public Predicate<ItemStack> createComponentTest(ImmutableStringReader reader, ItemPredicateArgument.ComponentWrapper componentType) {
/* 159 */     return componentType.presenceChecker;
/*     */   }
/*     */   
/*     */   public ItemPredicateArgument.PredicateWrapper lookupPredicateType(ImmutableStringReader reader, Identifier componentId) throws CommandSyntaxException {
/* 164 */     ItemPredicateArgument.PredicateWrapper wrapper = ItemPredicateArgument.PSEUDO_PREDICATES.get(componentId);
/* 165 */     if (wrapper != null)
/* 166 */       return wrapper; 
/* 169 */     return (ItemPredicateArgument.PredicateWrapper)this.predicates.get(ResourceKey.create(Registries.DATA_COMPONENT_PREDICATE_TYPE, componentId))
/* 170 */       .map(PredicateWrapper::new)
/* 171 */       .or(() -> this.components.get(ResourceKey.create(Registries.DATA_COMPONENT_TYPE, componentId)).map(ItemPredicateArgument::createComponentExistencePredicate))
/*     */       
/* 175 */       .orElseThrow(() -> ItemPredicateArgument.ERROR_UNKNOWN_PREDICATE.createWithContext(reader, componentId));
/*     */   }
/*     */   
/*     */   public Predicate<ItemStack> createPredicateTest(ImmutableStringReader reader, ItemPredicateArgument.PredicateWrapper predicateType, Dynamic<?> value) throws CommandSyntaxException {
/* 180 */     return predicateType.decode(reader, RegistryOps.injectRegistryContext(value, this.registries));
/*     */   }
/*     */   
/*     */   public Stream<Identifier> listElementTypes() {
/* 185 */     return this.items.listElementIds().map(ResourceKey::identifier);
/*     */   }
/*     */   
/*     */   public Stream<Identifier> listTagTypes() {
/* 190 */     return this.items.listTagIds().map(TagKey::location);
/*     */   }
/*     */   
/*     */   public Stream<Identifier> listComponentTypes() {
/* 195 */     return Stream.concat(ItemPredicateArgument.PSEUDO_COMPONENTS
/* 196 */         .keySet().stream(), this.components
/* 197 */         .listElements().filter(e -> !((DataComponentType)e.value()).isTransient()).map(e -> e.key().identifier()));
/*     */   }
/*     */   
/*     */   public Stream<Identifier> listPredicateTypes() {
/* 203 */     return Stream.concat(ItemPredicateArgument.PSEUDO_PREDICATES
/* 204 */         .keySet().stream(), this.predicates
/* 205 */         .listElementIds().map(ResourceKey::identifier));
/*     */   }
/*     */   
/*     */   public Predicate<ItemStack> negate(Predicate<ItemStack> value) {
/* 211 */     return value.negate();
/*     */   }
/*     */   
/*     */   public Predicate<ItemStack> anyOf(List<Predicate<ItemStack>> alternatives) {
/* 216 */     return Util.anyOf(alternatives);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\item\ItemPredicateArgument$Context.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */