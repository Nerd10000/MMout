/*    */ package net.minecraft.data.advancements.packs;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.data.advancements.AdvancementProvider;
/*    */ 
/*    */ public class VanillaAdvancementProvider {
/*    */   public static AdvancementProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
/* 12 */     return new AdvancementProvider(output, registries, 
/*    */         
/* 15 */         List.of(new VanillaTheEndAdvancements(), new VanillaHusbandryAdvancements(), new VanillaAdventureAdvancements(), new VanillaNetherAdvancements(), new VanillaStoryAdvancements()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\advancements\packs\VanillaAdvancementProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */