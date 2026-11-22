/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.Holder;
/*     */ 
/*     */ public class Mutable {
/* 128 */   private final Object2IntOpenHashMap<Holder<Enchantment>> enchantments = new Object2IntOpenHashMap();
/*     */   
/*     */   public Mutable(ItemEnchantments enchantments) {
/* 131 */     this.enchantments.putAll((Map)enchantments.enchantments);
/*     */   }
/*     */   
/*     */   public void set(Holder<Enchantment> enchantment, int level) {
/* 135 */     if (level <= 0) {
/* 136 */       this.enchantments.removeInt(enchantment);
/*     */     } else {
/* 138 */       this.enchantments.put(enchantment, Math.min(level, 255));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void upgrade(Holder<Enchantment> enchantment, int level) {
/* 143 */     if (level > 0)
/* 144 */       this.enchantments.merge(enchantment, Math.min(level, 255), Integer::max); 
/*     */   }
/*     */   
/*     */   public void removeIf(Predicate<Holder<Enchantment>> predicate) {
/* 149 */     this.enchantments.keySet().removeIf(predicate);
/*     */   }
/*     */   
/*     */   public int getLevel(Holder<Enchantment> enchantment) {
/* 153 */     return this.enchantments.getOrDefault(enchantment, 0);
/*     */   }
/*     */   
/*     */   public Set<Holder<Enchantment>> keySet() {
/* 157 */     return (Set<Holder<Enchantment>>)this.enchantments.keySet();
/*     */   }
/*     */   
/*     */   public ItemEnchantments toImmutable() {
/* 161 */     return new ItemEnchantments(this.enchantments);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\ItemEnchantments$Mutable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */