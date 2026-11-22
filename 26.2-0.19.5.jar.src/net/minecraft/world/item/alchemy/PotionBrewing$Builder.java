/*     */ package net.minecraft.world.item.alchemy;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class Builder {
/* 226 */   private final List<Ingredient> containers = new ArrayList<>();
/*     */   
/* 227 */   private final List<PotionBrewing.Mix<Potion>> potionMixes = new ArrayList<>();
/*     */   
/* 228 */   private final List<PotionBrewing.Mix<Item>> containerMixes = new ArrayList<>();
/*     */   
/*     */   private final FeatureFlagSet enabledFeatures;
/*     */   
/*     */   public Builder(FeatureFlagSet enabledFeatures) {
/* 233 */     this.enabledFeatures = enabledFeatures;
/*     */   }
/*     */   
/*     */   private static void expectPotion(Item from) {
/* 237 */     if (!(from instanceof net.minecraft.world.item.PotionItem))
/* 238 */       throw new IllegalArgumentException("Expected a potion, got: " + String.valueOf(BuiltInRegistries.ITEM.getKey(from))); 
/*     */   }
/*     */   
/*     */   public void addContainerRecipe(Item from, Item ingredient, Item to) {
/* 243 */     if (!from.isEnabled(this.enabledFeatures) || !ingredient.isEnabled(this.enabledFeatures) || !to.isEnabled(this.enabledFeatures))
/*     */       return; 
/* 247 */     expectPotion(from);
/* 248 */     expectPotion(to);
/* 249 */     this.containerMixes.add(new PotionBrewing.Mix<>((Holder<Item>)from.builtInRegistryHolder(), Ingredient.of((ItemLike)ingredient), (Holder<Item>)to.builtInRegistryHolder()));
/*     */   }
/*     */   
/*     */   public void addContainer(Item item) {
/* 253 */     if (!item.isEnabled(this.enabledFeatures))
/*     */       return; 
/* 256 */     expectPotion(item);
/* 257 */     this.containers.add(Ingredient.of((ItemLike)item));
/*     */   }
/*     */   
/*     */   public void addMix(Holder<Potion> from, Item ingredient, Holder<Potion> to) {
/* 261 */     if (((Potion)from.value()).isEnabled(this.enabledFeatures) && ingredient.isEnabled(this.enabledFeatures) && ((Potion)to.value()).isEnabled(this.enabledFeatures))
/* 262 */       this.potionMixes.add(new PotionBrewing.Mix<>(from, Ingredient.of((ItemLike)ingredient), to)); 
/*     */   }
/*     */   
/*     */   public void addStartMix(Item ingredient, Holder<Potion> potion) {
/* 267 */     if (((Potion)potion.value()).isEnabled(this.enabledFeatures)) {
/* 268 */       addMix(Potions.WATER, ingredient, Potions.MUNDANE);
/* 269 */       addMix(Potions.AWKWARD, ingredient, potion);
/*     */     } 
/*     */   }
/*     */   
/*     */   public PotionBrewing build() {
/* 274 */     return new PotionBrewing(
/* 275 */         List.copyOf(this.containers), 
/* 276 */         List.copyOf(this.potionMixes), 
/* 277 */         List.copyOf(this.containerMixes));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\alchemy\PotionBrewing$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */