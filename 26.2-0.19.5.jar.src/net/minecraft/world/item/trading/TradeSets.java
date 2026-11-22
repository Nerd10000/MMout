/*     */ package net.minecraft.world.item.trading;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.tags.VillagerTradeTags;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ 
/*     */ public class TradeSets {
/*  17 */   public static final ResourceKey<TradeSet> WANDERING_TRADER_BUYING = resourceKey("wandering_trader/buying");
/*     */   
/*  18 */   public static final ResourceKey<TradeSet> WANDERING_TRADER_COMMON = resourceKey("wandering_trader/common");
/*     */   
/*  19 */   public static final ResourceKey<TradeSet> WANDERING_TRADER_UNCOMMON = resourceKey("wandering_trader/uncommon");
/*     */   
/*  21 */   public static final ResourceKey<TradeSet> ARMORER_LEVEL_1 = resourceKey("armorer/level_1");
/*     */   
/*  22 */   public static final ResourceKey<TradeSet> ARMORER_LEVEL_2 = resourceKey("armorer/level_2");
/*     */   
/*  23 */   public static final ResourceKey<TradeSet> ARMORER_LEVEL_3 = resourceKey("armorer/level_3");
/*     */   
/*  24 */   public static final ResourceKey<TradeSet> ARMORER_LEVEL_4 = resourceKey("armorer/level_4");
/*     */   
/*  25 */   public static final ResourceKey<TradeSet> ARMORER_LEVEL_5 = resourceKey("armorer/level_5");
/*     */   
/*  27 */   public static final ResourceKey<TradeSet> BUTCHER_LEVEL_1 = resourceKey("butcher/level_1");
/*     */   
/*  28 */   public static final ResourceKey<TradeSet> BUTCHER_LEVEL_2 = resourceKey("butcher/level_2");
/*     */   
/*  29 */   public static final ResourceKey<TradeSet> BUTCHER_LEVEL_3 = resourceKey("butcher/level_3");
/*     */   
/*  30 */   public static final ResourceKey<TradeSet> BUTCHER_LEVEL_4 = resourceKey("butcher/level_4");
/*     */   
/*  31 */   public static final ResourceKey<TradeSet> BUTCHER_LEVEL_5 = resourceKey("butcher/level_5");
/*     */   
/*  33 */   public static final ResourceKey<TradeSet> CARTOGRAPHER_LEVEL_1 = resourceKey("cartographer/level_1");
/*     */   
/*  34 */   public static final ResourceKey<TradeSet> CARTOGRAPHER_LEVEL_2 = resourceKey("cartographer/level_2");
/*     */   
/*  35 */   public static final ResourceKey<TradeSet> CARTOGRAPHER_LEVEL_3 = resourceKey("cartographer/level_3");
/*     */   
/*  36 */   public static final ResourceKey<TradeSet> CARTOGRAPHER_LEVEL_4 = resourceKey("cartographer/level_4");
/*     */   
/*  37 */   public static final ResourceKey<TradeSet> CARTOGRAPHER_LEVEL_5 = resourceKey("cartographer/level_5");
/*     */   
/*  39 */   public static final ResourceKey<TradeSet> CLERIC_LEVEL_1 = resourceKey("cleric/level_1");
/*     */   
/*  40 */   public static final ResourceKey<TradeSet> CLERIC_LEVEL_2 = resourceKey("cleric/level_2");
/*     */   
/*  41 */   public static final ResourceKey<TradeSet> CLERIC_LEVEL_3 = resourceKey("cleric/level_3");
/*     */   
/*  42 */   public static final ResourceKey<TradeSet> CLERIC_LEVEL_4 = resourceKey("cleric/level_4");
/*     */   
/*  43 */   public static final ResourceKey<TradeSet> CLERIC_LEVEL_5 = resourceKey("cleric/level_5");
/*     */   
/*  45 */   public static final ResourceKey<TradeSet> FARMER_LEVEL_1 = resourceKey("farmer/level_1");
/*     */   
/*  46 */   public static final ResourceKey<TradeSet> FARMER_LEVEL_2 = resourceKey("farmer/level_2");
/*     */   
/*  47 */   public static final ResourceKey<TradeSet> FARMER_LEVEL_3 = resourceKey("farmer/level_3");
/*     */   
/*  48 */   public static final ResourceKey<TradeSet> FARMER_LEVEL_4 = resourceKey("farmer/level_4");
/*     */   
/*  49 */   public static final ResourceKey<TradeSet> FARMER_LEVEL_5 = resourceKey("farmer/level_5");
/*     */   
/*  51 */   public static final ResourceKey<TradeSet> FISHERMAN_LEVEL_1 = resourceKey("fisherman/level_1");
/*     */   
/*  52 */   public static final ResourceKey<TradeSet> FISHERMAN_LEVEL_2 = resourceKey("fisherman/level_2");
/*     */   
/*  53 */   public static final ResourceKey<TradeSet> FISHERMAN_LEVEL_3 = resourceKey("fisherman/level_3");
/*     */   
/*  54 */   public static final ResourceKey<TradeSet> FISHERMAN_LEVEL_4 = resourceKey("fisherman/level_4");
/*     */   
/*  55 */   public static final ResourceKey<TradeSet> FISHERMAN_LEVEL_5 = resourceKey("fisherman/level_5");
/*     */   
/*  57 */   public static final ResourceKey<TradeSet> FLETCHER_LEVEL_1 = resourceKey("fletcher/level_1");
/*     */   
/*  58 */   public static final ResourceKey<TradeSet> FLETCHER_LEVEL_2 = resourceKey("fletcher/level_2");
/*     */   
/*  59 */   public static final ResourceKey<TradeSet> FLETCHER_LEVEL_3 = resourceKey("fletcher/level_3");
/*     */   
/*  60 */   public static final ResourceKey<TradeSet> FLETCHER_LEVEL_4 = resourceKey("fletcher/level_4");
/*     */   
/*  61 */   public static final ResourceKey<TradeSet> FLETCHER_LEVEL_5 = resourceKey("fletcher/level_5");
/*     */   
/*  63 */   public static final ResourceKey<TradeSet> LEATHERWORKER_LEVEL_1 = resourceKey("leatherworker/level_1");
/*     */   
/*  64 */   public static final ResourceKey<TradeSet> LEATHERWORKER_LEVEL_2 = resourceKey("leatherworker/level_2");
/*     */   
/*  65 */   public static final ResourceKey<TradeSet> LEATHERWORKER_LEVEL_3 = resourceKey("leatherworker/level_3");
/*     */   
/*  66 */   public static final ResourceKey<TradeSet> LEATHERWORKER_LEVEL_4 = resourceKey("leatherworker/level_4");
/*     */   
/*  67 */   public static final ResourceKey<TradeSet> LEATHERWORKER_LEVEL_5 = resourceKey("leatherworker/level_5");
/*     */   
/*  69 */   public static final ResourceKey<TradeSet> LIBRARIAN_LEVEL_1 = resourceKey("librarian/level_1");
/*     */   
/*  70 */   public static final ResourceKey<TradeSet> LIBRARIAN_LEVEL_2 = resourceKey("librarian/level_2");
/*     */   
/*  71 */   public static final ResourceKey<TradeSet> LIBRARIAN_LEVEL_3 = resourceKey("librarian/level_3");
/*     */   
/*  72 */   public static final ResourceKey<TradeSet> LIBRARIAN_LEVEL_4 = resourceKey("librarian/level_4");
/*     */   
/*  73 */   public static final ResourceKey<TradeSet> LIBRARIAN_LEVEL_5 = resourceKey("librarian/level_5");
/*     */   
/*  75 */   public static final ResourceKey<TradeSet> MASON_LEVEL_1 = resourceKey("mason/level_1");
/*     */   
/*  76 */   public static final ResourceKey<TradeSet> MASON_LEVEL_2 = resourceKey("mason/level_2");
/*     */   
/*  77 */   public static final ResourceKey<TradeSet> MASON_LEVEL_3 = resourceKey("mason/level_3");
/*     */   
/*  78 */   public static final ResourceKey<TradeSet> MASON_LEVEL_4 = resourceKey("mason/level_4");
/*     */   
/*  79 */   public static final ResourceKey<TradeSet> MASON_LEVEL_5 = resourceKey("mason/level_5");
/*     */   
/*  81 */   public static final ResourceKey<TradeSet> SHEPHERD_LEVEL_1 = resourceKey("shepherd/level_1");
/*     */   
/*  82 */   public static final ResourceKey<TradeSet> SHEPHERD_LEVEL_2 = resourceKey("shepherd/level_2");
/*     */   
/*  83 */   public static final ResourceKey<TradeSet> SHEPHERD_LEVEL_3 = resourceKey("shepherd/level_3");
/*     */   
/*  84 */   public static final ResourceKey<TradeSet> SHEPHERD_LEVEL_4 = resourceKey("shepherd/level_4");
/*     */   
/*  85 */   public static final ResourceKey<TradeSet> SHEPHERD_LEVEL_5 = resourceKey("shepherd/level_5");
/*     */   
/*  87 */   public static final ResourceKey<TradeSet> TOOLSMITH_LEVEL_1 = resourceKey("toolsmith/level_1");
/*     */   
/*  88 */   public static final ResourceKey<TradeSet> TOOLSMITH_LEVEL_2 = resourceKey("toolsmith/level_2");
/*     */   
/*  89 */   public static final ResourceKey<TradeSet> TOOLSMITH_LEVEL_3 = resourceKey("toolsmith/level_3");
/*     */   
/*  90 */   public static final ResourceKey<TradeSet> TOOLSMITH_LEVEL_4 = resourceKey("toolsmith/level_4");
/*     */   
/*  91 */   public static final ResourceKey<TradeSet> TOOLSMITH_LEVEL_5 = resourceKey("toolsmith/level_5");
/*     */   
/*  93 */   public static final ResourceKey<TradeSet> WEAPONSMITH_LEVEL_1 = resourceKey("weaponsmith/level_1");
/*     */   
/*  94 */   public static final ResourceKey<TradeSet> WEAPONSMITH_LEVEL_2 = resourceKey("weaponsmith/level_2");
/*     */   
/*  95 */   public static final ResourceKey<TradeSet> WEAPONSMITH_LEVEL_3 = resourceKey("weaponsmith/level_3");
/*     */   
/*  96 */   public static final ResourceKey<TradeSet> WEAPONSMITH_LEVEL_4 = resourceKey("weaponsmith/level_4");
/*     */   
/*  97 */   public static final ResourceKey<TradeSet> WEAPONSMITH_LEVEL_5 = resourceKey("weaponsmith/level_5");
/*     */   
/*     */   public static Holder<TradeSet> bootstrap(BootstrapContext<TradeSet> context) {
/* 100 */     register(context, WANDERING_TRADER_BUYING, VillagerTradeTags.WANDERING_TRADER_BUYING);
/* 101 */     register(context, WANDERING_TRADER_COMMON, VillagerTradeTags.WANDERING_TRADER_COMMON, (NumberProvider)ConstantValue.exactly(5.0F));
/* 102 */     register(context, WANDERING_TRADER_UNCOMMON, VillagerTradeTags.WANDERING_TRADER_UNCOMMON);
/* 104 */     register(context, ARMORER_LEVEL_1, VillagerTradeTags.ARMORER_LEVEL_1);
/* 105 */     register(context, ARMORER_LEVEL_2, VillagerTradeTags.ARMORER_LEVEL_2);
/* 106 */     register(context, ARMORER_LEVEL_3, VillagerTradeTags.ARMORER_LEVEL_3);
/* 107 */     register(context, ARMORER_LEVEL_4, VillagerTradeTags.ARMORER_LEVEL_4);
/* 108 */     register(context, ARMORER_LEVEL_5, VillagerTradeTags.ARMORER_LEVEL_5);
/* 110 */     register(context, BUTCHER_LEVEL_1, VillagerTradeTags.BUTCHER_LEVEL_1);
/* 111 */     register(context, BUTCHER_LEVEL_2, VillagerTradeTags.BUTCHER_LEVEL_2);
/* 112 */     register(context, BUTCHER_LEVEL_3, VillagerTradeTags.BUTCHER_LEVEL_3);
/* 113 */     register(context, BUTCHER_LEVEL_4, VillagerTradeTags.BUTCHER_LEVEL_4);
/* 114 */     register(context, BUTCHER_LEVEL_5, VillagerTradeTags.BUTCHER_LEVEL_5);
/* 116 */     register(context, CARTOGRAPHER_LEVEL_1, VillagerTradeTags.CARTOGRAPHER_LEVEL_1);
/* 117 */     register(context, CARTOGRAPHER_LEVEL_2, VillagerTradeTags.CARTOGRAPHER_LEVEL_2);
/* 118 */     register(context, CARTOGRAPHER_LEVEL_3, VillagerTradeTags.CARTOGRAPHER_LEVEL_3);
/* 119 */     register(context, CARTOGRAPHER_LEVEL_4, VillagerTradeTags.CARTOGRAPHER_LEVEL_4);
/* 120 */     register(context, CARTOGRAPHER_LEVEL_5, VillagerTradeTags.CARTOGRAPHER_LEVEL_5);
/* 122 */     register(context, CLERIC_LEVEL_1, VillagerTradeTags.CLERIC_LEVEL_1);
/* 123 */     register(context, CLERIC_LEVEL_2, VillagerTradeTags.CLERIC_LEVEL_2);
/* 124 */     register(context, CLERIC_LEVEL_3, VillagerTradeTags.CLERIC_LEVEL_3);
/* 125 */     register(context, CLERIC_LEVEL_4, VillagerTradeTags.CLERIC_LEVEL_4);
/* 126 */     register(context, CLERIC_LEVEL_5, VillagerTradeTags.CLERIC_LEVEL_5);
/* 128 */     register(context, FARMER_LEVEL_1, VillagerTradeTags.FARMER_LEVEL_1);
/* 129 */     register(context, FARMER_LEVEL_2, VillagerTradeTags.FARMER_LEVEL_2);
/* 130 */     register(context, FARMER_LEVEL_3, VillagerTradeTags.FARMER_LEVEL_3);
/* 131 */     register(context, FARMER_LEVEL_4, VillagerTradeTags.FARMER_LEVEL_4);
/* 132 */     register(context, FARMER_LEVEL_5, VillagerTradeTags.FARMER_LEVEL_5);
/* 134 */     register(context, FISHERMAN_LEVEL_1, VillagerTradeTags.FISHERMAN_LEVEL_1);
/* 135 */     register(context, FISHERMAN_LEVEL_2, VillagerTradeTags.FISHERMAN_LEVEL_2);
/* 136 */     register(context, FISHERMAN_LEVEL_3, VillagerTradeTags.FISHERMAN_LEVEL_3);
/* 137 */     register(context, FISHERMAN_LEVEL_4, VillagerTradeTags.FISHERMAN_LEVEL_4);
/* 138 */     register(context, FISHERMAN_LEVEL_5, VillagerTradeTags.FISHERMAN_LEVEL_5);
/* 140 */     register(context, FLETCHER_LEVEL_1, VillagerTradeTags.FLETCHER_LEVEL_1);
/* 141 */     register(context, FLETCHER_LEVEL_2, VillagerTradeTags.FLETCHER_LEVEL_2);
/* 142 */     register(context, FLETCHER_LEVEL_3, VillagerTradeTags.FLETCHER_LEVEL_3);
/* 143 */     register(context, FLETCHER_LEVEL_4, VillagerTradeTags.FLETCHER_LEVEL_4);
/* 144 */     register(context, FLETCHER_LEVEL_5, VillagerTradeTags.FLETCHER_LEVEL_5);
/* 146 */     register(context, LEATHERWORKER_LEVEL_1, VillagerTradeTags.LEATHERWORKER_LEVEL_1);
/* 147 */     register(context, LEATHERWORKER_LEVEL_2, VillagerTradeTags.LEATHERWORKER_LEVEL_2);
/* 148 */     register(context, LEATHERWORKER_LEVEL_3, VillagerTradeTags.LEATHERWORKER_LEVEL_3);
/* 149 */     register(context, LEATHERWORKER_LEVEL_4, VillagerTradeTags.LEATHERWORKER_LEVEL_4);
/* 150 */     register(context, LEATHERWORKER_LEVEL_5, VillagerTradeTags.LEATHERWORKER_LEVEL_5);
/* 152 */     register(context, LIBRARIAN_LEVEL_1, VillagerTradeTags.LIBRARIAN_LEVEL_1);
/* 153 */     register(context, LIBRARIAN_LEVEL_2, VillagerTradeTags.LIBRARIAN_LEVEL_2);
/* 154 */     register(context, LIBRARIAN_LEVEL_3, VillagerTradeTags.LIBRARIAN_LEVEL_3);
/* 155 */     register(context, LIBRARIAN_LEVEL_4, VillagerTradeTags.LIBRARIAN_LEVEL_4);
/* 156 */     register(context, LIBRARIAN_LEVEL_5, VillagerTradeTags.LIBRARIAN_LEVEL_5, (NumberProvider)ConstantValue.exactly(3.0F));
/* 158 */     register(context, MASON_LEVEL_1, VillagerTradeTags.MASON_LEVEL_1);
/* 159 */     register(context, MASON_LEVEL_2, VillagerTradeTags.MASON_LEVEL_2);
/* 160 */     register(context, MASON_LEVEL_3, VillagerTradeTags.MASON_LEVEL_3);
/* 161 */     register(context, MASON_LEVEL_4, VillagerTradeTags.MASON_LEVEL_4);
/* 162 */     register(context, MASON_LEVEL_5, VillagerTradeTags.MASON_LEVEL_5);
/* 164 */     register(context, SHEPHERD_LEVEL_1, VillagerTradeTags.SHEPHERD_LEVEL_1);
/* 165 */     register(context, SHEPHERD_LEVEL_2, VillagerTradeTags.SHEPHERD_LEVEL_2);
/* 166 */     register(context, SHEPHERD_LEVEL_3, VillagerTradeTags.SHEPHERD_LEVEL_3);
/* 167 */     register(context, SHEPHERD_LEVEL_4, VillagerTradeTags.SHEPHERD_LEVEL_4);
/* 168 */     register(context, SHEPHERD_LEVEL_5, VillagerTradeTags.SHEPHERD_LEVEL_5);
/* 170 */     register(context, TOOLSMITH_LEVEL_1, VillagerTradeTags.TOOLSMITH_LEVEL_1);
/* 171 */     register(context, TOOLSMITH_LEVEL_2, VillagerTradeTags.TOOLSMITH_LEVEL_2);
/* 172 */     register(context, TOOLSMITH_LEVEL_3, VillagerTradeTags.TOOLSMITH_LEVEL_3);
/* 173 */     register(context, TOOLSMITH_LEVEL_4, VillagerTradeTags.TOOLSMITH_LEVEL_4);
/* 174 */     register(context, TOOLSMITH_LEVEL_5, VillagerTradeTags.TOOLSMITH_LEVEL_5);
/* 176 */     register(context, WEAPONSMITH_LEVEL_1, VillagerTradeTags.WEAPONSMITH_LEVEL_1);
/* 177 */     register(context, WEAPONSMITH_LEVEL_2, VillagerTradeTags.WEAPONSMITH_LEVEL_2);
/* 178 */     register(context, WEAPONSMITH_LEVEL_3, VillagerTradeTags.WEAPONSMITH_LEVEL_3);
/* 179 */     register(context, WEAPONSMITH_LEVEL_4, VillagerTradeTags.WEAPONSMITH_LEVEL_4);
/* 180 */     return (Holder<TradeSet>)register(context, WEAPONSMITH_LEVEL_5, VillagerTradeTags.WEAPONSMITH_LEVEL_5);
/*     */   }
/*     */   
/*     */   public static Holder.Reference<TradeSet> register(BootstrapContext<TradeSet> context, ResourceKey<TradeSet> resourceKey, TagKey<VillagerTrade> tradeTag) {
/* 184 */     return register(context, resourceKey, tradeTag, (NumberProvider)ConstantValue.exactly(2.0F));
/*     */   }
/*     */   
/*     */   public static Holder.Reference<TradeSet> register(BootstrapContext<TradeSet> context, ResourceKey<TradeSet> resourceKey, TagKey<VillagerTrade> tradeTag, NumberProvider numberProvider) {
/* 188 */     return context.register(resourceKey, new TradeSet((HolderSet<VillagerTrade>)context.lookup(Registries.VILLAGER_TRADE).getOrThrow(tradeTag), numberProvider, false, Optional.of(resourceKey.identifier().withPrefix("trade_set/"))));
/*     */   }
/*     */   
/*     */   public static ResourceKey<TradeSet> resourceKey(String path) {
/* 192 */     return ResourceKey.create(Registries.TRADE_SET, Identifier.withDefaultNamespace(path));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\trading\TradeSets.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */