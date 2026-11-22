/*    */ package net.minecraft.world.item.trading;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class TradeSet {
/*    */   public static final Codec<TradeSet> CODEC;
/*    */   
/*    */   private final HolderSet<VillagerTrade> trades;
/*    */   
/*    */   private final NumberProvider amount;
/*    */   
/*    */   private final boolean allowDuplicates;
/*    */   
/*    */   private final Optional<Identifier> randomSequence;
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.VILLAGER_TRADE).fieldOf("trades").forGetter(()), (App)NumberProviders.CODEC.fieldOf("amount").forGetter(()), (App)Codec.BOOL.optionalFieldOf("allow_duplicates", Boolean.valueOf(false)).forGetter(()), (App)Identifier.CODEC.optionalFieldOf("random_sequence").forGetter(())).apply((Applicative)i, TradeSet::new));
/*    */   }
/*    */   
/*    */   public TradeSet(HolderSet<VillagerTrade> trades, NumberProvider amount, boolean allowDuplicates, Optional<Identifier> randomSequence) {
/* 29 */     this.trades = trades;
/* 30 */     this.amount = amount;
/* 31 */     this.allowDuplicates = allowDuplicates;
/* 32 */     this.randomSequence = randomSequence;
/*    */   }
/*    */   
/*    */   public HolderSet<VillagerTrade> getTrades() {
/* 36 */     return this.trades;
/*    */   }
/*    */   
/*    */   public int calculateNumberOfTrades(LootContext lootContext) {
/* 40 */     return this.amount.getInt(lootContext);
/*    */   }
/*    */   
/*    */   public boolean allowDuplicates() {
/* 44 */     return this.allowDuplicates;
/*    */   }
/*    */   
/*    */   public Optional<Identifier> randomSequence() {
/* 48 */     return this.randomSequence;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\trading\TradeSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */