/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.ItemPredicate;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class FilteredFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<FilteredFunction> MAP_CODEC;
/*    */   
/*    */   private final ItemPredicate filter;
/*    */   
/*    */   private final Optional<LootItemFunction> onPass;
/*    */   
/*    */   private final Optional<LootItemFunction> onFail;
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)ItemPredicate.CODEC.fieldOf("item_filter").forGetter(()), (App)LootItemFunctions.ROOT_CODEC.optionalFieldOf("on_pass").forGetter(()), (App)LootItemFunctions.ROOT_CODEC.optionalFieldOf("on_fail").forGetter(()))).apply((Applicative)i, FilteredFunction::new));
/*    */   }
/*    */   
/*    */   private FilteredFunction(List<LootItemCondition> predicates, ItemPredicate filter, Optional<LootItemFunction> onPass, Optional<LootItemFunction> onFail) {
/* 27 */     super(predicates);
/* 28 */     this.filter = filter;
/* 29 */     this.onPass = onPass;
/* 30 */     this.onFail = onFail;
/*    */   }
/*    */   
/*    */   public MapCodec<FilteredFunction> codec() {
/* 35 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 40 */     Optional<LootItemFunction> function = this.filter.test((ItemInstance)itemStack) ? this.onPass : this.onFail;
/* 41 */     if (function.isPresent())
/* 42 */       return ((LootItemFunction)function.get()).apply(itemStack, context); 
/* 44 */     return itemStack;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 49 */     super.validate(context);
/* 51 */     Validatable.validate(context, "on_pass", this.onPass);
/* 52 */     Validatable.validate(context, "on_fail", this.onFail);
/*    */   }
/*    */   
/*    */   public static Builder filtered(ItemPredicate predicate) {
/* 56 */     return new Builder(predicate);
/*    */   }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*    */     private final ItemPredicate itemPredicate;
/*    */     
/* 61 */     private Optional<LootItemFunction> onPass = Optional.empty();
/*    */     
/* 62 */     private Optional<LootItemFunction> onFail = Optional.empty();
/*    */     
/*    */     private Builder(ItemPredicate itemPredicate) {
/* 65 */       this.itemPredicate = itemPredicate;
/*    */     }
/*    */     
/*    */     protected Builder getThis() {
/* 70 */       return this;
/*    */     }
/*    */     
/*    */     public Builder onPass(Optional<LootItemFunction> onPass) {
/* 74 */       this.onPass = onPass;
/* 75 */       return this;
/*    */     }
/*    */     
/*    */     public Builder onFail(Optional<LootItemFunction> onFail) {
/* 79 */       this.onFail = onFail;
/* 80 */       return this;
/*    */     }
/*    */     
/*    */     public LootItemFunction build() {
/* 85 */       return new FilteredFunction(getConditions(), this.itemPredicate, this.onPass, this.onFail);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\FilteredFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */