/*    */ package net.minecraft.world.item.equipment.trim;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class TrimMaterials {
/* 13 */   public static final ResourceKey<TrimMaterial> QUARTZ = registryKey("quartz");
/*    */   
/* 14 */   public static final ResourceKey<TrimMaterial> IRON = registryKey("iron");
/*    */   
/* 15 */   public static final ResourceKey<TrimMaterial> NETHERITE = registryKey("netherite");
/*    */   
/* 16 */   public static final ResourceKey<TrimMaterial> REDSTONE = registryKey("redstone");
/*    */   
/* 17 */   public static final ResourceKey<TrimMaterial> COPPER = registryKey("copper");
/*    */   
/* 18 */   public static final ResourceKey<TrimMaterial> GOLD = registryKey("gold");
/*    */   
/* 19 */   public static final ResourceKey<TrimMaterial> EMERALD = registryKey("emerald");
/*    */   
/* 20 */   public static final ResourceKey<TrimMaterial> DIAMOND = registryKey("diamond");
/*    */   
/* 21 */   public static final ResourceKey<TrimMaterial> LAPIS = registryKey("lapis");
/*    */   
/* 22 */   public static final ResourceKey<TrimMaterial> AMETHYST = registryKey("amethyst");
/*    */   
/* 23 */   public static final ResourceKey<TrimMaterial> RESIN = registryKey("resin");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<TrimMaterial> context) {
/* 26 */     register(context, QUARTZ, Style.EMPTY.withColor(14931140), MaterialAssetGroup.QUARTZ);
/* 27 */     register(context, IRON, Style.EMPTY.withColor(15527148), MaterialAssetGroup.IRON);
/* 28 */     register(context, NETHERITE, Style.EMPTY.withColor(6445145), MaterialAssetGroup.NETHERITE);
/* 29 */     register(context, REDSTONE, Style.EMPTY.withColor(9901575), MaterialAssetGroup.REDSTONE);
/* 30 */     register(context, COPPER, Style.EMPTY.withColor(11823181), MaterialAssetGroup.COPPER);
/* 31 */     register(context, GOLD, Style.EMPTY.withColor(14594349), MaterialAssetGroup.GOLD);
/* 32 */     register(context, EMERALD, Style.EMPTY.withColor(1155126), MaterialAssetGroup.EMERALD);
/* 33 */     register(context, DIAMOND, Style.EMPTY.withColor(7269586), MaterialAssetGroup.DIAMOND);
/* 34 */     register(context, LAPIS, Style.EMPTY.withColor(4288151), MaterialAssetGroup.LAPIS);
/* 35 */     register(context, AMETHYST, Style.EMPTY.withColor(10116294), MaterialAssetGroup.AMETHYST);
/* 36 */     register(context, RESIN, Style.EMPTY.withColor(16545810), MaterialAssetGroup.RESIN);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> registryKey, Style hoverTextStyle, MaterialAssetGroup assets) {
/* 40 */     MutableComponent mutableComponent = Component.translatable(Util.makeDescriptionId("trim_material", registryKey.identifier())).withStyle(hoverTextStyle);
/* 41 */     context.register(registryKey, new TrimMaterial(assets, (Component)mutableComponent));
/*    */   }
/*    */   
/*    */   private static ResourceKey<TrimMaterial> registryKey(String id) {
/* 45 */     return ResourceKey.create(Registries.TRIM_MATERIAL, Identifier.withDefaultNamespace(id));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\equipment\trim\TrimMaterials.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */