/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.trading.VillagerTrade;
/*    */ 
/*    */ public class VillagerTradeTags {
/* 11 */   public static final TagKey<VillagerTrade> FARMER_LEVEL_1 = create("farmer/level_1");
/*    */   
/* 12 */   public static final TagKey<VillagerTrade> FARMER_LEVEL_2 = create("farmer/level_2");
/*    */   
/* 13 */   public static final TagKey<VillagerTrade> FARMER_LEVEL_3 = create("farmer/level_3");
/*    */   
/* 14 */   public static final TagKey<VillagerTrade> FARMER_LEVEL_4 = create("farmer/level_4");
/*    */   
/* 15 */   public static final TagKey<VillagerTrade> FARMER_LEVEL_5 = create("farmer/level_5");
/*    */   
/* 16 */   public static final TagKey<VillagerTrade> FISHERMAN_LEVEL_1 = create("fisherman/level_1");
/*    */   
/* 17 */   public static final TagKey<VillagerTrade> FISHERMAN_LEVEL_2 = create("fisherman/level_2");
/*    */   
/* 18 */   public static final TagKey<VillagerTrade> FISHERMAN_LEVEL_3 = create("fisherman/level_3");
/*    */   
/* 19 */   public static final TagKey<VillagerTrade> FISHERMAN_LEVEL_4 = create("fisherman/level_4");
/*    */   
/* 20 */   public static final TagKey<VillagerTrade> FISHERMAN_LEVEL_5 = create("fisherman/level_5");
/*    */   
/* 21 */   public static final TagKey<VillagerTrade> SHEPHERD_LEVEL_1 = create("shepherd/level_1");
/*    */   
/* 22 */   public static final TagKey<VillagerTrade> SHEPHERD_LEVEL_2 = create("shepherd/level_2");
/*    */   
/* 23 */   public static final TagKey<VillagerTrade> SHEPHERD_LEVEL_3 = create("shepherd/level_3");
/*    */   
/* 24 */   public static final TagKey<VillagerTrade> SHEPHERD_LEVEL_4 = create("shepherd/level_4");
/*    */   
/* 25 */   public static final TagKey<VillagerTrade> SHEPHERD_LEVEL_5 = create("shepherd/level_5");
/*    */   
/* 26 */   public static final TagKey<VillagerTrade> FLETCHER_LEVEL_1 = create("fletcher/level_1");
/*    */   
/* 27 */   public static final TagKey<VillagerTrade> FLETCHER_LEVEL_2 = create("fletcher/level_2");
/*    */   
/* 28 */   public static final TagKey<VillagerTrade> FLETCHER_LEVEL_3 = create("fletcher/level_3");
/*    */   
/* 29 */   public static final TagKey<VillagerTrade> FLETCHER_LEVEL_4 = create("fletcher/level_4");
/*    */   
/* 30 */   public static final TagKey<VillagerTrade> FLETCHER_LEVEL_5 = create("fletcher/level_5");
/*    */   
/* 31 */   public static final TagKey<VillagerTrade> LIBRARIAN_LEVEL_1 = create("librarian/level_1");
/*    */   
/* 32 */   public static final TagKey<VillagerTrade> LIBRARIAN_LEVEL_2 = create("librarian/level_2");
/*    */   
/* 33 */   public static final TagKey<VillagerTrade> LIBRARIAN_LEVEL_3 = create("librarian/level_3");
/*    */   
/* 34 */   public static final TagKey<VillagerTrade> LIBRARIAN_LEVEL_4 = create("librarian/level_4");
/*    */   
/* 35 */   public static final TagKey<VillagerTrade> LIBRARIAN_LEVEL_5 = create("librarian/level_5");
/*    */   
/* 36 */   public static final TagKey<VillagerTrade> CARTOGRAPHER_LEVEL_1 = create("cartographer/level_1");
/*    */   
/* 37 */   public static final TagKey<VillagerTrade> CARTOGRAPHER_LEVEL_2 = create("cartographer/level_2");
/*    */   
/* 38 */   public static final TagKey<VillagerTrade> CARTOGRAPHER_LEVEL_3 = create("cartographer/level_3");
/*    */   
/* 39 */   public static final TagKey<VillagerTrade> CARTOGRAPHER_LEVEL_4 = create("cartographer/level_4");
/*    */   
/* 40 */   public static final TagKey<VillagerTrade> CARTOGRAPHER_LEVEL_5 = create("cartographer/level_5");
/*    */   
/* 41 */   public static final TagKey<VillagerTrade> CLERIC_LEVEL_1 = create("cleric/level_1");
/*    */   
/* 42 */   public static final TagKey<VillagerTrade> CLERIC_LEVEL_2 = create("cleric/level_2");
/*    */   
/* 43 */   public static final TagKey<VillagerTrade> CLERIC_LEVEL_3 = create("cleric/level_3");
/*    */   
/* 44 */   public static final TagKey<VillagerTrade> CLERIC_LEVEL_4 = create("cleric/level_4");
/*    */   
/* 45 */   public static final TagKey<VillagerTrade> CLERIC_LEVEL_5 = create("cleric/level_5");
/*    */   
/* 46 */   public static final TagKey<VillagerTrade> COMMON_SMITH_LEVEL_1 = create("common_smith/level_1");
/*    */   
/* 47 */   public static final TagKey<VillagerTrade> COMMON_SMITH_LEVEL_2 = create("common_smith/level_2");
/*    */   
/* 48 */   public static final TagKey<VillagerTrade> COMMON_SMITH_LEVEL_3 = create("common_smith/level_3");
/*    */   
/* 49 */   public static final TagKey<VillagerTrade> COMMON_SMITH_LEVEL_4 = create("common_smith/level_4");
/*    */   
/* 50 */   public static final TagKey<VillagerTrade> COMMON_SMITH_LEVEL_5 = create("common_smith/level_5");
/*    */   
/* 51 */   public static final TagKey<VillagerTrade> ARMORER_LEVEL_1 = create("armorer/level_1");
/*    */   
/* 52 */   public static final TagKey<VillagerTrade> ARMORER_LEVEL_2 = create("armorer/level_2");
/*    */   
/* 53 */   public static final TagKey<VillagerTrade> ARMORER_LEVEL_3 = create("armorer/level_3");
/*    */   
/* 54 */   public static final TagKey<VillagerTrade> ARMORER_LEVEL_4 = create("armorer/level_4");
/*    */   
/* 55 */   public static final TagKey<VillagerTrade> ARMORER_LEVEL_5 = create("armorer/level_5");
/*    */   
/* 56 */   public static final TagKey<VillagerTrade> WEAPONSMITH_LEVEL_1 = create("weaponsmith/level_1");
/*    */   
/* 57 */   public static final TagKey<VillagerTrade> WEAPONSMITH_LEVEL_2 = create("weaponsmith/level_2");
/*    */   
/* 58 */   public static final TagKey<VillagerTrade> WEAPONSMITH_LEVEL_3 = create("weaponsmith/level_3");
/*    */   
/* 59 */   public static final TagKey<VillagerTrade> WEAPONSMITH_LEVEL_4 = create("weaponsmith/level_4");
/*    */   
/* 60 */   public static final TagKey<VillagerTrade> WEAPONSMITH_LEVEL_5 = create("weaponsmith/level_5");
/*    */   
/* 61 */   public static final TagKey<VillagerTrade> TOOLSMITH_LEVEL_1 = create("toolsmith/level_1");
/*    */   
/* 62 */   public static final TagKey<VillagerTrade> TOOLSMITH_LEVEL_2 = create("toolsmith/level_2");
/*    */   
/* 63 */   public static final TagKey<VillagerTrade> TOOLSMITH_LEVEL_3 = create("toolsmith/level_3");
/*    */   
/* 64 */   public static final TagKey<VillagerTrade> TOOLSMITH_LEVEL_4 = create("toolsmith/level_4");
/*    */   
/* 65 */   public static final TagKey<VillagerTrade> TOOLSMITH_LEVEL_5 = create("toolsmith/level_5");
/*    */   
/* 66 */   public static final TagKey<VillagerTrade> BUTCHER_LEVEL_1 = create("butcher/level_1");
/*    */   
/* 67 */   public static final TagKey<VillagerTrade> BUTCHER_LEVEL_2 = create("butcher/level_2");
/*    */   
/* 68 */   public static final TagKey<VillagerTrade> BUTCHER_LEVEL_3 = create("butcher/level_3");
/*    */   
/* 69 */   public static final TagKey<VillagerTrade> BUTCHER_LEVEL_4 = create("butcher/level_4");
/*    */   
/* 70 */   public static final TagKey<VillagerTrade> BUTCHER_LEVEL_5 = create("butcher/level_5");
/*    */   
/* 71 */   public static final TagKey<VillagerTrade> LEATHERWORKER_LEVEL_1 = create("leatherworker/level_1");
/*    */   
/* 72 */   public static final TagKey<VillagerTrade> LEATHERWORKER_LEVEL_2 = create("leatherworker/level_2");
/*    */   
/* 73 */   public static final TagKey<VillagerTrade> LEATHERWORKER_LEVEL_3 = create("leatherworker/level_3");
/*    */   
/* 74 */   public static final TagKey<VillagerTrade> LEATHERWORKER_LEVEL_4 = create("leatherworker/level_4");
/*    */   
/* 75 */   public static final TagKey<VillagerTrade> LEATHERWORKER_LEVEL_5 = create("leatherworker/level_5");
/*    */   
/* 76 */   public static final TagKey<VillagerTrade> MASON_LEVEL_1 = create("mason/level_1");
/*    */   
/* 77 */   public static final TagKey<VillagerTrade> MASON_LEVEL_2 = create("mason/level_2");
/*    */   
/* 78 */   public static final TagKey<VillagerTrade> MASON_LEVEL_3 = create("mason/level_3");
/*    */   
/* 79 */   public static final TagKey<VillagerTrade> MASON_LEVEL_4 = create("mason/level_4");
/*    */   
/* 80 */   public static final TagKey<VillagerTrade> MASON_LEVEL_5 = create("mason/level_5");
/*    */   
/* 82 */   public static final TagKey<VillagerTrade> WANDERING_TRADER_BUYING = create("wandering_trader/buying");
/*    */   
/* 83 */   public static final TagKey<VillagerTrade> WANDERING_TRADER_UNCOMMON = create("wandering_trader/uncommon");
/*    */   
/* 84 */   public static final TagKey<VillagerTrade> WANDERING_TRADER_COMMON = create("wandering_trader/common");
/*    */   
/*    */   private static TagKey<VillagerTrade> create(String name) {
/* 87 */     return TagKey.create(Registries.VILLAGER_TRADE, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\VillagerTradeTags.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */