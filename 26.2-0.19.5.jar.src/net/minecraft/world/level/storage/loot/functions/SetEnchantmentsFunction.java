/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class SetEnchantmentsFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<SetEnchantmentsFunction> MAP_CODEC;
/*    */   
/*    */   private final Map<Holder<Enchantment>, NumberProvider> enchantments;
/*    */   
/*    */   private final boolean add;
/*    */   
/*    */   static {
/* 23 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)Codec.unboundedMap(Enchantment.CODEC, NumberProviders.CODEC).optionalFieldOf("enchantments", Map.of()).forGetter(()), (App)Codec.BOOL.optionalFieldOf("add", Boolean.valueOf(false)).forGetter(()))).apply((Applicative)i, SetEnchantmentsFunction::new));
/*    */   }
/*    */   
/*    */   private SetEnchantmentsFunction(List<LootItemCondition> predicates, Map<Holder<Enchantment>, NumberProvider> enchantments, boolean add) {
/* 32 */     super(predicates);
/* 33 */     this.enchantments = Map.copyOf(enchantments);
/* 34 */     this.add = add;
/*    */   }
/*    */   
/*    */   public MapCodec<SetEnchantmentsFunction> codec() {
/* 39 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 44 */     super.validate(context);
/* 46 */     this.enchantments.forEach((enchantment, value) -> value.validate(context.forMapField("enchantments", enchantment.getRegisteredName())));
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 53 */     if (itemStack.is(Items.BOOK))
/* 54 */       itemStack = itemStack.transmuteCopy((ItemLike)Items.ENCHANTED_BOOK); 
/* 57 */     EnchantmentHelper.updateEnchantments(itemStack, enchantments -> {
/*    */           if (this.add) {
/*    */             this.enchantments.forEach(());
/*    */           } else {
/*    */             this.enchantments.forEach(());
/*    */           } 
/*    */         });
/* 64 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/* 68 */     private final ImmutableMap.Builder<Holder<Enchantment>, NumberProvider> enchantments = ImmutableMap.builder();
/*    */     
/*    */     private final boolean add;
/*    */     
/*    */     public Builder() {
/* 72 */       this(false);
/*    */     }
/*    */     
/*    */     public Builder(boolean add) {
/* 76 */       this.add = add;
/*    */     }
/*    */     
/*    */     protected Builder getThis() {
/* 81 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withEnchantment(Holder<Enchantment> enchantment, NumberProvider levelProvider) {
/* 85 */       this.enchantments.put(enchantment, levelProvider);
/* 86 */       return this;
/*    */     }
/*    */     
/*    */     public LootItemFunction build() {
/* 91 */       return new SetEnchantmentsFunction(getConditions(), (Map<Holder<Enchantment>, NumberProvider>)this.enchantments.build(), this.add);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetEnchantmentsFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */