/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.predicates.EnchantmentPredicate;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ 
/*    */ public class StoredEnchantments extends EnchantmentsPredicate {
/* 60 */   public static final Codec<StoredEnchantments> CODEC = codec(StoredEnchantments::new);
/*    */   
/*    */   protected StoredEnchantments(List<EnchantmentPredicate> enchantments) {
/* 63 */     super(enchantments);
/*    */   }
/*    */   
/*    */   public DataComponentType<ItemEnchantments> componentType() {
/* 68 */     return DataComponents.STORED_ENCHANTMENTS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\EnchantmentsPredicate$StoredEnchantments.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */