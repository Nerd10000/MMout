/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ 
/*    */ public class Builder extends LootItemConditionalFunction.Builder<SetEnchantmentsFunction.Builder> {
/* 68 */   private final ImmutableMap.Builder<Holder<Enchantment>, NumberProvider> enchantments = ImmutableMap.builder();
/*    */   
/*    */   private final boolean add;
/*    */   
/*    */   public Builder() {
/* 72 */     this(false);
/*    */   }
/*    */   
/*    */   public Builder(boolean add) {
/* 76 */     this.add = add;
/*    */   }
/*    */   
/*    */   protected Builder getThis() {
/* 81 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withEnchantment(Holder<Enchantment> enchantment, NumberProvider levelProvider) {
/* 85 */     this.enchantments.put(enchantment, levelProvider);
/* 86 */     return this;
/*    */   }
/*    */   
/*    */   public LootItemFunction build() {
/* 91 */     return new SetEnchantmentsFunction(getConditions(), (Map<Holder<Enchantment>, NumberProvider>)this.enchantments.build(), this.add);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetEnchantmentsFunction$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */