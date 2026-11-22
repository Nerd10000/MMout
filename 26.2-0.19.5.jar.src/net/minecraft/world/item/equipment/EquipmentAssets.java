/*    */ package net.minecraft.world.item.equipment;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public interface EquipmentAssets {
/* 12 */   public static final ResourceKey<? extends Registry<EquipmentAsset>> ROOT_ID = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));
/*    */   
/* 14 */   public static final ResourceKey<EquipmentAsset> LEATHER = createId("leather");
/*    */   
/* 15 */   public static final ResourceKey<EquipmentAsset> COPPER = createId("copper");
/*    */   
/* 16 */   public static final ResourceKey<EquipmentAsset> CHAINMAIL = createId("chainmail");
/*    */   
/* 17 */   public static final ResourceKey<EquipmentAsset> IRON = createId("iron");
/*    */   
/* 18 */   public static final ResourceKey<EquipmentAsset> GOLD = createId("gold");
/*    */   
/* 19 */   public static final ResourceKey<EquipmentAsset> DIAMOND = createId("diamond");
/*    */   
/* 20 */   public static final ResourceKey<EquipmentAsset> TURTLE_SCUTE = createId("turtle_scute");
/*    */   
/* 21 */   public static final ResourceKey<EquipmentAsset> NETHERITE = createId("netherite");
/*    */   
/* 22 */   public static final ResourceKey<EquipmentAsset> ARMADILLO_SCUTE = createId("armadillo_scute");
/*    */   
/* 23 */   public static final ResourceKey<EquipmentAsset> ELYTRA = createId("elytra");
/*    */   
/* 24 */   public static final ResourceKey<EquipmentAsset> SADDLE = createId("saddle");
/*    */   
/*    */   public static final Map<DyeColor, ResourceKey<EquipmentAsset>> CARPETS;
/*    */   
/*    */   static {
/* 26 */     CARPETS = Util.makeEnumMap(DyeColor.class, color -> createId(color.getSerializedName() + "_carpet"));
/*    */   }
/*    */   
/* 27 */   public static final ResourceKey<EquipmentAsset> TRADER_LLAMA = createId("trader_llama");
/*    */   
/* 28 */   public static final ResourceKey<EquipmentAsset> TRADER_LLAMA_BABY = createId("trader_llama_baby");
/*    */   
/*    */   public static final Map<DyeColor, ResourceKey<EquipmentAsset>> HARNESSES;
/*    */   
/*    */   static {
/* 30 */     HARNESSES = Util.makeEnumMap(DyeColor.class, color -> createId(color.getSerializedName() + "_harness"));
/*    */   }
/*    */   
/*    */   static ResourceKey<EquipmentAsset> createId(String name) {
/* 33 */     return ResourceKey.create(ROOT_ID, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\equipment\EquipmentAssets.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */