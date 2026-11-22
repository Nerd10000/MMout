/*    */ package net.minecraft.data.loot.packs;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.data.loot.LootTableProvider;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ 
/*    */ public class TradeRebalanceLootTableProvider {
/*    */   public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
/* 14 */     return new LootTableProvider(output, 
/*    */         
/* 16 */         Set.of(), 
/* 17 */         List.of(new LootTableProvider.SubProviderEntry(TradeRebalanceChestLoot::new, LootContextParamSets.CHEST)), registries);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\loot\packs\TradeRebalanceLootTableProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */