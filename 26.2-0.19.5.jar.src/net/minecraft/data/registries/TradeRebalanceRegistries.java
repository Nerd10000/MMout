/*    */ package net.minecraft.data.registries;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.RegistrySetBuilder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.item.trading.TradeRebalanceVillagerTrades;
/*    */ 
/*    */ public class TradeRebalanceRegistries {
/* 11 */   private static final RegistrySetBuilder BUILDER = (new RegistrySetBuilder())
/* 12 */     .add(Registries.VILLAGER_TRADE, TradeRebalanceVillagerTrades::bootstrap);
/*    */   
/*    */   public static CompletableFuture<RegistrySetBuilder.PatchedRegistries> createLookup(CompletableFuture<HolderLookup.Provider> vanilla) {
/* 15 */     return RegistryPatchGenerator.createLookup(vanilla, BUILDER);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\registries\TradeRebalanceRegistries.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */