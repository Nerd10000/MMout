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
/*    */ public class Enchantments extends EnchantmentsPredicate {
/* 47 */   public static final Codec<Enchantments> CODEC = codec(Enchantments::new);
/*    */   
/*    */   protected Enchantments(List<EnchantmentPredicate> enchantments) {
/* 50 */     super(enchantments);
/*    */   }
/*    */   
/*    */   public DataComponentType<ItemEnchantments> componentType() {
/* 55 */     return DataComponents.ENCHANTMENTS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\EnchantmentsPredicate$Enchantments.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */