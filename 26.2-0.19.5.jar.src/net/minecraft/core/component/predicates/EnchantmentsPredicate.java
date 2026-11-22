/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.predicates.EnchantmentPredicate;
/*    */ import net.minecraft.advancements.predicates.SingleComponentItemPredicate;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ 
/*    */ public abstract class EnchantmentsPredicate implements SingleComponentItemPredicate<ItemEnchantments> {
/*    */   private final List<EnchantmentPredicate> enchantments;
/*    */   
/*    */   protected EnchantmentsPredicate(List<EnchantmentPredicate> enchantments) {
/* 17 */     this.enchantments = enchantments;
/*    */   }
/*    */   
/*    */   public static <T extends EnchantmentsPredicate> Codec<T> codec(Function<List<EnchantmentPredicate>, T> constructor) {
/* 21 */     return EnchantmentPredicate.CODEC.listOf().xmap(constructor, EnchantmentsPredicate::enchantments);
/*    */   }
/*    */   
/*    */   protected List<EnchantmentPredicate> enchantments() {
/* 25 */     return this.enchantments;
/*    */   }
/*    */   
/*    */   public boolean matches(ItemEnchantments appliedEnchantments) {
/* 30 */     for (EnchantmentPredicate enchantment : this.enchantments) {
/* 31 */       if (!enchantment.containedIn(appliedEnchantments))
/* 32 */         return false; 
/*    */     } 
/* 35 */     return true;
/*    */   }
/*    */   
/*    */   public static Enchantments enchantments(List<EnchantmentPredicate> predicates) {
/* 39 */     return new Enchantments(predicates);
/*    */   }
/*    */   
/*    */   public static StoredEnchantments storedEnchantments(List<EnchantmentPredicate> predicates) {
/* 43 */     return new StoredEnchantments(predicates);
/*    */   }
/*    */   
/*    */   public static class Enchantments extends EnchantmentsPredicate {
/* 47 */     public static final Codec<Enchantments> CODEC = codec(Enchantments::new);
/*    */     
/*    */     protected Enchantments(List<EnchantmentPredicate> enchantments) {
/* 50 */       super(enchantments);
/*    */     }
/*    */     
/*    */     public DataComponentType<ItemEnchantments> componentType() {
/* 55 */       return DataComponents.ENCHANTMENTS;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class StoredEnchantments extends EnchantmentsPredicate {
/* 60 */     public static final Codec<StoredEnchantments> CODEC = codec(StoredEnchantments::new);
/*    */     
/*    */     protected StoredEnchantments(List<EnchantmentPredicate> enchantments) {
/* 63 */       super(enchantments);
/*    */     }
/*    */     
/*    */     public DataComponentType<ItemEnchantments> componentType() {
/* 68 */       return DataComponents.STORED_ENCHANTMENTS;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\EnchantmentsPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */