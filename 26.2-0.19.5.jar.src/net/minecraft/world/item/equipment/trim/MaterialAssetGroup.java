/*    */ package net.minecraft.world.item.equipment.trim;
/*    */ 
/*    */ import com.google.common.base.Function;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.equipment.EquipmentAsset;
/*    */ import net.minecraft.world.item.equipment.EquipmentAssets;
/*    */ 
/*    */ public final class MaterialAssetGroup extends Record {
/*    */   private final AssetInfo base;
/*    */   
/*    */   private final Map<ResourceKey<EquipmentAsset>, AssetInfo> overrides;
/*    */   
/*    */   public static final String SEPARATOR = "_";
/*    */   
/*    */   public static final MapCodec<MaterialAssetGroup> MAP_CODEC;
/*    */   
/*    */   public MaterialAssetGroup(AssetInfo base, Map<ResourceKey<EquipmentAsset>, AssetInfo> overrides) {
/* 19 */     this.base = base;
/* 19 */     this.overrides = overrides;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public AssetInfo base() {
/* 19 */     return this.base;
/*    */   }
/*    */   
/*    */   public Map<ResourceKey<EquipmentAsset>, AssetInfo> overrides() {
/* 19 */     return this.overrides;
/*    */   }
/*    */   
/*    */   static {
/* 25 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)AssetInfo.CODEC.fieldOf("asset_name").forGetter(MaterialAssetGroup::base), (App)Codec.unboundedMap(ResourceKey.codec(EquipmentAssets.ROOT_ID), AssetInfo.CODEC).optionalFieldOf("override_armor_assets", Map.of()).forGetter(MaterialAssetGroup::overrides)).apply((Applicative)i, MaterialAssetGroup::new));
/*    */   }
/*    */   
/* 30 */   public static final StreamCodec<ByteBuf, MaterialAssetGroup> STREAM_CODEC = StreamCodec.composite(AssetInfo.STREAM_CODEC, MaterialAssetGroup::base, 
/*    */       
/* 32 */       ByteBufCodecs.map(it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap::new, ResourceKey.streamCodec(EquipmentAssets.ROOT_ID), AssetInfo.STREAM_CODEC), MaterialAssetGroup::overrides, MaterialAssetGroup::new);
/*    */   
/* 36 */   public static final MaterialAssetGroup QUARTZ = create("quartz");
/*    */   
/* 37 */   public static final MaterialAssetGroup IRON = create("iron", Map.of(EquipmentAssets.IRON, "iron_darker"));
/*    */   
/* 38 */   public static final MaterialAssetGroup NETHERITE = create("netherite", Map.of(EquipmentAssets.NETHERITE, "netherite_darker"));
/*    */   
/* 39 */   public static final MaterialAssetGroup REDSTONE = create("redstone");
/*    */   
/* 40 */   public static final MaterialAssetGroup COPPER = create("copper", Map.of(EquipmentAssets.COPPER, "copper_darker"));
/*    */   
/* 41 */   public static final MaterialAssetGroup GOLD = create("gold", Map.of(EquipmentAssets.GOLD, "gold_darker"));
/*    */   
/* 42 */   public static final MaterialAssetGroup EMERALD = create("emerald");
/*    */   
/* 43 */   public static final MaterialAssetGroup DIAMOND = create("diamond", Map.of(EquipmentAssets.DIAMOND, "diamond_darker"));
/*    */   
/* 44 */   public static final MaterialAssetGroup LAPIS = create("lapis");
/*    */   
/* 45 */   public static final MaterialAssetGroup AMETHYST = create("amethyst");
/*    */   
/* 46 */   public static final MaterialAssetGroup RESIN = create("resin");
/*    */   
/*    */   public static MaterialAssetGroup create(String base) {
/* 49 */     return new MaterialAssetGroup(new AssetInfo(base), Map.of());
/*    */   }
/*    */   
/*    */   public static MaterialAssetGroup create(String base, Map<ResourceKey<EquipmentAsset>, String> overrides) {
/* 53 */     return new MaterialAssetGroup(new AssetInfo(base), Map.copyOf(Maps.transformValues(overrides, AssetInfo::new)));
/*    */   }
/*    */   
/*    */   public AssetInfo assetId(ResourceKey<EquipmentAsset> equipmentAssetId) {
/* 57 */     return this.overrides.getOrDefault(equipmentAssetId, this.base);
/*    */   }
/*    */   
/*    */   public static final class AssetInfo extends Record {
/*    */     private final String suffix;
/*    */     
/*    */     public String suffix() {
/* 60 */       return this.suffix;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup$AssetInfo;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #60	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup$AssetInfo;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup$AssetInfo;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #60	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup$AssetInfo;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup$AssetInfo;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #60	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup$AssetInfo;
/*    */     }
/*    */     
/* 61 */     public static final Codec<AssetInfo> CODEC = ExtraCodecs.RESOURCE_PATH_CODEC.xmap(AssetInfo::new, AssetInfo::suffix);
/*    */     
/* 63 */     public static final StreamCodec<ByteBuf, AssetInfo> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(AssetInfo::new, AssetInfo::suffix);
/*    */     
/*    */     public AssetInfo(String suffix) {
/* 66 */       if (!Identifier.isValidPath(suffix))
/* 67 */         throw new IllegalArgumentException("Invalid string to use as a resource path element: " + suffix); 
/*    */       this.suffix = suffix;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\equipment\trim\MaterialAssetGroup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */