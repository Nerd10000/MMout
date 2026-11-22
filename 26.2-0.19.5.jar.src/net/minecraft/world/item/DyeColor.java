/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentLookup;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.item.crafting.CraftingInput;
/*     */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeInput;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.material.MapColor;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ 
/*     */ public enum DyeColor implements StringRepresentable {
/*  32 */   WHITE(0, "white", 16383998, MapColor.SNOW, MapColor.TERRACOTTA_WHITE, 15790320, 16777215),
/*  33 */   ORANGE(1, "orange", 16351261, MapColor.COLOR_ORANGE, MapColor.TERRACOTTA_ORANGE, 15435844, 16738335),
/*  34 */   MAGENTA(2, "magenta", 13061821, MapColor.COLOR_MAGENTA, MapColor.TERRACOTTA_MAGENTA, 12801229, 16711935),
/*  35 */   LIGHT_BLUE(3, "light_blue", 3847130, MapColor.COLOR_LIGHT_BLUE, MapColor.TERRACOTTA_LIGHT_BLUE, 6719955, 10141901),
/*  36 */   YELLOW(4, "yellow", 16701501, MapColor.COLOR_YELLOW, MapColor.TERRACOTTA_YELLOW, 14602026, 16776960),
/*  37 */   LIME(5, "lime", 8439583, MapColor.COLOR_LIGHT_GREEN, MapColor.TERRACOTTA_LIGHT_GREEN, 4312372, 12582656),
/*  38 */   PINK(6, "pink", 15961002, MapColor.COLOR_PINK, MapColor.TERRACOTTA_PINK, 14188952, 16738740),
/*  39 */   GRAY(7, "gray", 4673362, MapColor.COLOR_GRAY, MapColor.TERRACOTTA_GRAY, 4408131, 8421504),
/*  40 */   LIGHT_GRAY(8, "light_gray", 10329495, MapColor.COLOR_LIGHT_GRAY, MapColor.TERRACOTTA_LIGHT_GRAY, 11250603, 13882323),
/*  41 */   CYAN(9, "cyan", 1481884, MapColor.COLOR_CYAN, MapColor.TERRACOTTA_CYAN, 2651799, 65535),
/*  42 */   PURPLE(10, "purple", 8991416, MapColor.COLOR_PURPLE, MapColor.TERRACOTTA_PURPLE, 8073150, 10494192),
/*  43 */   BLUE(11, "blue", 3949738, MapColor.COLOR_BLUE, MapColor.TERRACOTTA_BLUE, 2437522, 255),
/*  44 */   BROWN(12, "brown", 8606770, MapColor.COLOR_BROWN, MapColor.TERRACOTTA_BROWN, 5320730, 9127187),
/*  45 */   GREEN(13, "green", 6192150, MapColor.COLOR_GREEN, MapColor.TERRACOTTA_GREEN, 3887386, 65280),
/*  46 */   RED(14, "red", 11546150, MapColor.COLOR_RED, MapColor.TERRACOTTA_RED, 11743532, 16711680),
/*  47 */   BLACK(15, "black", 1908001, MapColor.COLOR_BLACK, MapColor.TERRACOTTA_BLACK, 1973019, 0);
/*     */   
/*     */   public static final List<DyeColor> VALUES;
/*     */   
/*     */   private static final IntFunction<DyeColor> BY_ID;
/*     */   
/*     */   private static final Int2ObjectOpenHashMap<DyeColor> BY_FIREWORK_COLOR;
/*     */   
/*     */   public static final StringRepresentable.EnumCodec<DyeColor> CODEC;
/*     */   
/*     */   public static final StreamCodec<ByteBuf, DyeColor> STREAM_CODEC;
/*     */   
/*     */   @Deprecated
/*     */   public static final Codec<DyeColor> LEGACY_ID_CODEC;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final MapColor mapColor;
/*     */   
/*     */   private final MapColor terracottaColor;
/*     */   
/*     */   private final int textureDiffuseColor;
/*     */   
/*     */   private final int fireworkColor;
/*     */   
/*     */   private final int textColor;
/*     */   
/*     */   static {
/*  50 */     VALUES = List.of(values());
/*  52 */     BY_ID = ByIdMap.continuous(DyeColor::getId, VALUES.toArray(x$0 -> new DyeColor[x$0]), ByIdMap.OutOfBoundsStrategy.ZERO);
/*  53 */     BY_FIREWORK_COLOR = new Int2ObjectOpenHashMap((Map)VALUES.stream().collect(Collectors.toMap(v -> Integer.valueOf(v.fireworkColor), v -> v)));
/*  55 */     CODEC = StringRepresentable.fromEnum(DyeColor::values);
/*  56 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, DyeColor::getId);
/*  59 */     LEGACY_ID_CODEC = Codec.BYTE.xmap(DyeColor::byId, color -> Byte.valueOf((byte)color.id));
/*     */   }
/*     */   
/*     */   DyeColor(int id, String name, int textureDiffuseColor, MapColor mapColor, MapColor terracottaColor, int fireworkColor, int textColor) {
/*  70 */     this.id = id;
/*  71 */     this.name = name;
/*  72 */     this.mapColor = mapColor;
/*  73 */     this.terracottaColor = terracottaColor;
/*  74 */     this.textColor = ARGB.opaque(textColor);
/*  75 */     this.textureDiffuseColor = ARGB.opaque(textureDiffuseColor);
/*  76 */     this.fireworkColor = fireworkColor;
/*     */   }
/*     */   
/*     */   public int getId() {
/*  80 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  84 */     return this.name;
/*     */   }
/*     */   
/*     */   public int getTextureDiffuseColor() {
/*  88 */     return this.textureDiffuseColor;
/*     */   }
/*     */   
/*     */   public MapColor getMapColor() {
/*  92 */     return this.mapColor;
/*     */   }
/*     */   
/*     */   public MapColor getTerracottaColor() {
/*  96 */     return this.terracottaColor;
/*     */   }
/*     */   
/*     */   public int getFireworkColor() {
/* 100 */     return this.fireworkColor;
/*     */   }
/*     */   
/*     */   public int getTextColor() {
/* 104 */     return this.textColor;
/*     */   }
/*     */   
/*     */   public static DyeColor byId(int id) {
/* 108 */     return BY_ID.apply(id);
/*     */   }
/*     */   
/*     */   @Contract("_,!null->!null;_,null->_")
/*     */   public static DyeColor byName(String name, DyeColor def) {
/* 113 */     DyeColor result = (DyeColor)CODEC.byName(name);
/* 114 */     return (result != null) ? result : def;
/*     */   }
/*     */   
/*     */   public static DyeColor byFireworkColor(int color) {
/* 118 */     return (DyeColor)BY_FIREWORK_COLOR.get(color);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 123 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 128 */     return this.name;
/*     */   }
/*     */   
/*     */   public static DyeColor getMixedColor(ServerLevel level, DyeColor dyeColor1, DyeColor dyeColor2) {
/* 132 */     DyeColor mixedColor = findColorMixInRecipes(level, dyeColor1, dyeColor2);
/* 133 */     if (mixedColor != null)
/* 134 */       return mixedColor; 
/* 136 */     return level.getRandom().nextBoolean() ? dyeColor1 : dyeColor2;
/*     */   }
/*     */   
/*     */   private static DyeColor findColorMixInRecipes(ServerLevel level, DyeColor dyeColor1, DyeColor dyeColor2) {
/* 140 */     DataComponentLookup<Item> itemComponents = level.registryAccess().lookupOrThrow(Registries.ITEM).componentLookup();
/* 141 */     Collection<Holder<Item>> dye1Items = itemComponents.findAll(DataComponents.DYE, dyeColor1);
/* 142 */     if (dye1Items.isEmpty())
/* 143 */       return null; 
/* 145 */     Collection<Holder<Item>> dye2Items = itemComponents.findAll(DataComponents.DYE, dyeColor2);
/* 146 */     if (dye2Items.isEmpty())
/* 147 */       return null; 
/* 150 */     for (Holder<Item> dye1Item : dye1Items) {
/* 151 */       for (Holder<Item> dye2Item : dye2Items) {
/* 152 */         CraftingInput input = CraftingInput.of(2, 1, List.of(new ItemStack(dye1Item), new ItemStack(dye2Item)));
/* 153 */         Optional<RecipeHolder<CraftingRecipe>> foundRecipe = level.recipeAccess().getRecipeFor(RecipeType.CRAFTING, (RecipeInput)input, (Level)level);
/* 154 */         if (foundRecipe.isPresent()) {
/* 155 */           ItemStack craftingResult = ((CraftingRecipe)((RecipeHolder)foundRecipe.get()).value()).assemble((RecipeInput)input);
/* 156 */           DyeColor craftedDyeColor = (DyeColor)craftingResult.get(DataComponents.DYE);
/* 157 */           if (craftedDyeColor != null)
/* 158 */             return craftedDyeColor; 
/*     */         } 
/*     */       } 
/*     */     } 
/* 164 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\DyeColor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */