/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.Validatable;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ 
/*     */ public class EnchantedCountIncreaseFunction extends LootItemConditionalFunction {
/*     */   public static final int NO_LIMIT = 0;
/*     */   
/*     */   public static final MapCodec<EnchantedCountIncreaseFunction> MAP_CODEC;
/*     */   
/*     */   private final Holder<Enchantment> enchantment;
/*     */   
/*     */   private final NumberProvider count;
/*     */   
/*     */   private final int limit;
/*     */   
/*     */   static {
/*  30 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)Enchantment.CODEC.fieldOf("enchantment").forGetter(()), (App)NumberProviders.CODEC.fieldOf("count").forGetter(()), (App)Codec.INT.optionalFieldOf("limit", Integer.valueOf(0)).forGetter(()))).apply((Applicative)i, EnchantedCountIncreaseFunction::new));
/*     */   }
/*     */   
/*     */   private EnchantedCountIncreaseFunction(List<LootItemCondition> predicates, Holder<Enchantment> enchantment, NumberProvider count, int limit) {
/*  41 */     super(predicates);
/*  42 */     this.enchantment = enchantment;
/*  43 */     this.count = count;
/*  44 */     this.limit = limit;
/*     */   }
/*     */   
/*     */   public MapCodec<EnchantedCountIncreaseFunction> codec() {
/*  49 */     return MAP_CODEC;
/*     */   }
/*     */   
/*     */   public Set<ContextKey<?>> getReferencedContextParams() {
/*  54 */     return Set.of(LootContextParams.ATTACKING_ENTITY);
/*     */   }
/*     */   
/*     */   public void validate(ValidationContext context) {
/*  59 */     super.validate(context);
/*  61 */     Validatable.validate(context, "count", (Validatable)this.count);
/*     */   }
/*     */   
/*     */   private boolean hasLimit() {
/*  65 */     return (this.limit > 0);
/*     */   }
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  70 */     Entity killer = (Entity)context.getOptionalParameter(LootContextParams.ATTACKING_ENTITY);
/*  72 */     if (killer instanceof LivingEntity) {
/*  72 */       LivingEntity entity = (LivingEntity)killer;
/*  73 */       int level = EnchantmentHelper.getEnchantmentLevel(this.enchantment, entity);
/*  74 */       if (level == 0)
/*  75 */         return itemStack; 
/*  77 */       float addition = level * this.count.getFloat(context);
/*  78 */       itemStack.grow(Math.round(addition));
/*  80 */       if (hasLimit())
/*  81 */         itemStack.limitSize(this.limit); 
/*     */     } 
/*  85 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*     */     private final Holder<Enchantment> enchantment;
/*     */     
/*     */     private final NumberProvider count;
/*     */     
/*  91 */     private int limit = 0;
/*     */     
/*     */     public Builder(Holder<Enchantment> enchantment, NumberProvider count) {
/*  94 */       this.enchantment = enchantment;
/*  95 */       this.count = count;
/*     */     }
/*     */     
/*     */     protected Builder getThis() {
/* 100 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setLimit(int limit) {
/* 104 */       this.limit = limit;
/* 105 */       return this;
/*     */     }
/*     */     
/*     */     public LootItemFunction build() {
/* 110 */       return new EnchantedCountIncreaseFunction(getConditions(), this.enchantment, this.count, this.limit);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder lootingMultiplier(HolderLookup.Provider registries, NumberProvider count) {
/* 115 */     HolderLookup.RegistryLookup<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 116 */     return new Builder((Holder<Enchantment>)enchantments.getOrThrow(Enchantments.LOOTING), count);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\EnchantedCountIncreaseFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */