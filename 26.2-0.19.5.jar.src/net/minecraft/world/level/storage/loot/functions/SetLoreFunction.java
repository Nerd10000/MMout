/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.ItemLore;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetLoreFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<SetLoreFunction> MAP_CODEC;
/*    */   
/*    */   private final List<Component> lore;
/*    */   
/*    */   private final ListOperation mode;
/*    */   
/*    */   private final Optional<LootContext.EntityTarget> resolutionContext;
/*    */   
/*    */   static {
/* 23 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)ComponentSerialization.CODEC.sizeLimitedListOf(256).fieldOf("lore").forGetter(()), (App)ListOperation.codec(256).forGetter(()), (App)LootContext.EntityTarget.CODEC.optionalFieldOf("entity").forGetter(()))).apply((Applicative)i, SetLoreFunction::new));
/*    */   }
/*    */   
/*    */   public SetLoreFunction(List<LootItemCondition> predicates, List<Component> lore, ListOperation mode, Optional<LootContext.EntityTarget> resolutionContext) {
/* 34 */     super(predicates);
/* 35 */     this.lore = List.copyOf(lore);
/* 36 */     this.mode = mode;
/* 37 */     this.resolutionContext = resolutionContext;
/*    */   }
/*    */   
/*    */   public MapCodec<SetLoreFunction> codec() {
/* 42 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 47 */     return (Set<ContextKey<?>>)DataFixUtils.orElse(this.resolutionContext.map(target -> Set.of(target.contextParam())), Set.of());
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 52 */     itemStack.update(DataComponents.LORE, ItemLore.EMPTY, oldLore -> new ItemLore(updateLore(oldLore, context)));
/* 53 */     return itemStack;
/*    */   }
/*    */   
/*    */   private List<Component> updateLore(ItemLore itemLore, LootContext context) {
/* 57 */     if (itemLore == null && this.lore.isEmpty())
/* 58 */       return List.of(); 
/* 60 */     UnaryOperator<Component> resolver = SetNameFunction.createResolver(context, this.resolutionContext.orElse(null));
/* 61 */     List<Component> resolvedLines = this.lore.stream().map(resolver).toList();
/* 62 */     return this.mode.apply(itemLore.lines(), resolvedLines, 256);
/*    */   }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/* 66 */     private Optional<LootContext.EntityTarget> resolutionContext = Optional.empty();
/*    */     
/* 67 */     private final ImmutableList.Builder<Component> lore = ImmutableList.builder();
/*    */     
/* 68 */     private ListOperation mode = ListOperation.Append.INSTANCE;
/*    */     
/*    */     public Builder setMode(ListOperation mode) {
/* 71 */       this.mode = mode;
/* 72 */       return this;
/*    */     }
/*    */     
/*    */     public Builder setResolutionContext(LootContext.EntityTarget resolutionContext) {
/* 76 */       this.resolutionContext = Optional.of(resolutionContext);
/* 77 */       return this;
/*    */     }
/*    */     
/*    */     public Builder addLine(Component line) {
/* 81 */       this.lore.add(line);
/* 82 */       return this;
/*    */     }
/*    */     
/*    */     protected Builder getThis() {
/* 87 */       return this;
/*    */     }
/*    */     
/*    */     public LootItemFunction build() {
/* 92 */       return new SetLoreFunction(getConditions(), (List<Component>)this.lore.build(), this.mode, this.resolutionContext);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder setLore() {
/* 97 */     return new Builder();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetLoreFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */