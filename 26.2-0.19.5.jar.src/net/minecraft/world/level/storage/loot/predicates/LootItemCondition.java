/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextUser;
/*    */ 
/*    */ public interface LootItemCondition extends LootContextUser, Predicate<LootContext> {
/*    */   public static final Codec<LootItemCondition> TYPED_CODEC;
/*    */   
/*    */   static {
/* 17 */     TYPED_CODEC = BuiltInRegistries.LOOT_CONDITION_TYPE.byNameCodec().dispatch("condition", LootItemCondition::codec, c -> c);
/*    */   }
/*    */   
/* 18 */   public static final Codec<LootItemCondition> DIRECT_CODEC = Codec.lazyInitialized(() -> Codec.withAlternative(TYPED_CODEC, AllOfCondition.INLINE_CODEC));
/*    */   
/* 19 */   public static final Codec<Holder<LootItemCondition>> CODEC = (Codec<Holder<LootItemCondition>>)RegistryFileCodec.create(Registries.PREDICATE, DIRECT_CODEC);
/*    */   
/*    */   MapCodec<? extends LootItemCondition> codec();
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Builder {
/*    */     LootItemCondition build();
/*    */     
/*    */     default Builder invert() {
/* 29 */       return InvertedLootItemCondition.invert(this);
/*    */     }
/*    */     
/*    */     default AnyOfCondition.Builder or(Builder other) {
/* 33 */       return AnyOfCondition.anyOf(new Builder[] { this, other });
/*    */     }
/*    */     
/*    */     default AllOfCondition.Builder and(Builder other) {
/* 37 */       return AllOfCondition.allOf(new Builder[] { this, other });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */