/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.PotionTags;
/*    */ import net.minecraft.world.item.alchemy.Potion;
/*    */ import net.minecraft.world.item.alchemy.PotionIds;
/*    */ 
/*    */ public class PotionTagsProvider extends TagsProvider<Potion> {
/*    */   public PotionTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/* 14 */     super(output, Registries.POTION, lookupProvider);
/*    */   }
/*    */   
/*    */   protected void addTags(HolderLookup.Provider registries) {
/* 19 */     tag(PotionTags.TRADEABLE)
/* 20 */       .add((ResourceKey<Potion>[])new ResourceKey[] { 
/*    */           PotionIds.WIND_CHARGED, PotionIds.OOZING, PotionIds.INFESTED, PotionIds.WEAVING, PotionIds.NIGHT_VISION, PotionIds.LONG_NIGHT_VISION, PotionIds.INVISIBILITY, PotionIds.LONG_INVISIBILITY, PotionIds.FIRE_RESISTANCE, PotionIds.LONG_FIRE_RESISTANCE, 
/*    */           PotionIds.LEAPING, PotionIds.LONG_LEAPING, PotionIds.STRONG_LEAPING, PotionIds.SLOWNESS, PotionIds.LONG_SLOWNESS, PotionIds.STRONG_SLOWNESS, PotionIds.TURTLE_MASTER, PotionIds.LONG_TURTLE_MASTER, PotionIds.STRONG_TURTLE_MASTER, PotionIds.SWIFTNESS, 
/*    */           PotionIds.LONG_SWIFTNESS, PotionIds.STRONG_SWIFTNESS, PotionIds.WATER_BREATHING, PotionIds.LONG_WATER_BREATHING, PotionIds.HEALING, PotionIds.STRONG_HEALING, PotionIds.HARMING, PotionIds.STRONG_HARMING, PotionIds.POISON, PotionIds.LONG_POISON, 
/*    */           PotionIds.STRONG_POISON, PotionIds.REGENERATION, PotionIds.LONG_REGENERATION, PotionIds.STRONG_REGENERATION, PotionIds.STRENGTH, PotionIds.LONG_STRENGTH, PotionIds.STRONG_STRENGTH, PotionIds.WEAKNESS, PotionIds.LONG_WEAKNESS, PotionIds.SLOW_FALLING, 
/*    */           PotionIds.LONG_SLOW_FALLING });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\PotionTagsProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */