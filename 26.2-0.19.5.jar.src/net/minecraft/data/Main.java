/*     */ package net.minecraft.data;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiFunction;
/*     */ import joptsimple.AbstractOptionSpec;
/*     */ import joptsimple.ArgumentAcceptingOptionSpec;
/*     */ import joptsimple.OptionParser;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
/*     */ import joptsimple.OptionSpecBuilder;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.SuppressForbidden;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.RegistrySetBuilder;
/*     */ import net.minecraft.data.advancements.packs.VanillaAdvancementProvider;
/*     */ import net.minecraft.data.loot.packs.TradeRebalanceLootTableProvider;
/*     */ import net.minecraft.data.loot.packs.VanillaLootTableProvider;
/*     */ import net.minecraft.data.metadata.PackMetadataGenerator;
/*     */ import net.minecraft.data.registries.TradeRebalanceRegistries;
/*     */ import net.minecraft.data.registries.VanillaRegistries;
/*     */ import net.minecraft.data.structures.NbtToSnbt;
/*     */ import net.minecraft.data.structures.SnbtToNbt;
/*     */ import net.minecraft.data.structures.StructureUpdater;
/*     */ import net.minecraft.data.tags.TagsProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ 
/*     */ public class Main {
/*     */   @SuppressForbidden(reason = "System.out needed before bootstrap")
/*     */   public static void main(String[] args) throws IOException {
/*  72 */     SharedConstants.tryDetectVersion();
/*  74 */     OptionParser parser = new OptionParser();
/*  75 */     AbstractOptionSpec abstractOptionSpec = parser.accepts("help", "Show the help menu").forHelp();
/*  76 */     OptionSpecBuilder optionSpecBuilder1 = parser.accepts("server", "Include server generators");
/*  77 */     OptionSpecBuilder optionSpecBuilder2 = parser.accepts("dev", "Include development tools");
/*  78 */     OptionSpecBuilder optionSpecBuilder3 = parser.accepts("reports", "Include data reports");
/*  79 */     parser.accepts("validate", "Validate inputs");
/*  80 */     OptionSpecBuilder optionSpecBuilder4 = parser.accepts("all", "Include all generators");
/*  81 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec1 = parser.accepts("output", "Output folder").withRequiredArg().defaultsTo("generated", (Object[])new String[0]);
/*  82 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec2 = parser.accepts("input", "Input folder").withRequiredArg();
/*  83 */     OptionSet optionSet = parser.parse(args);
/*  85 */     if (optionSet.has((OptionSpec)abstractOptionSpec) || !optionSet.hasOptions()) {
/*  86 */       parser.printHelpOn(System.out);
/*     */       return;
/*     */     } 
/*  90 */     Path output = Paths.get((String)argumentAcceptingOptionSpec1.value(optionSet), new String[0]);
/*  91 */     boolean allOptions = optionSet.has((OptionSpec)optionSpecBuilder4);
/*  92 */     boolean server = (allOptions || optionSet.has((OptionSpec)optionSpecBuilder1));
/*  93 */     boolean dev = (allOptions || optionSet.has((OptionSpec)optionSpecBuilder2));
/*  94 */     boolean reports = (allOptions || optionSet.has((OptionSpec)optionSpecBuilder3));
/*  95 */     Collection<Path> input = optionSet.valuesOf((OptionSpec)argumentAcceptingOptionSpec2).stream().map(x$0 -> Paths.get(x$0, new String[0])).toList();
/*  96 */     DataGenerator generator = new DataGenerator.Cached(output, SharedConstants.getCurrentVersion(), true);
/*  97 */     addServerDefinitionProviders(generator, server, reports);
/*  98 */     addServerConverters(generator, input, server, dev);
/*  99 */     generator.run();
/* 100 */     Util.shutdownExecutors();
/*     */   }
/*     */   
/*     */   private static <T extends DataProvider> DataProvider.Factory<T> bindRegistries(BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, T> target, CompletableFuture<HolderLookup.Provider> registries) {
/* 104 */     return output -> (DataProvider)target.apply(output, registries);
/*     */   }
/*     */   
/*     */   public static void addServerConverters(DataGenerator generator, Collection<Path> input, boolean server, boolean dev) {
/* 108 */     DataGenerator.PackGenerator commonVanillaPack = generator.getVanillaPack(server);
/* 109 */     commonVanillaPack.addProvider(o -> (new SnbtToNbt(o, input)).addFilter((SnbtToNbt.Filter)new StructureUpdater()));
/* 111 */     DataGenerator.PackGenerator devVanillaPack = generator.getVanillaPack(dev);
/* 112 */     devVanillaPack.addProvider(o -> new NbtToSnbt(o, input));
/*     */   }
/*     */   
/*     */   public static void addServerDefinitionProviders(DataGenerator generator, boolean server, boolean reports) {
/* 116 */     CompletableFuture<HolderLookup.Provider> vanillaRegistries = CompletableFuture.supplyAsync(VanillaRegistries::createLookup, (Executor)Util.backgroundExecutor());
/* 124 */     DataGenerator.PackGenerator serverVanillaPack = generator.getVanillaPack(server);
/* 126 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.registries.RegistriesDatapackGenerator::new, vanillaRegistries));
/* 129 */     serverVanillaPack.addProvider(bindRegistries(VanillaAdvancementProvider::create, vanillaRegistries));
/* 130 */     serverVanillaPack.addProvider(bindRegistries(VanillaLootTableProvider::create, vanillaRegistries));
/* 131 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.recipes.packs.VanillaRecipeProvider.Runner::new, vanillaRegistries));
/* 134 */     TagsProvider<Block> vanillaBlockTagsProvider = serverVanillaPack.<TagsProvider<Block>>addProvider(bindRegistries(net.minecraft.data.tags.VanillaBlockTagsProvider::new, vanillaRegistries));
/* 135 */     TagsProvider<Item> vanillaItemTagsProvider = serverVanillaPack.<TagsProvider<Item>>addProvider(bindRegistries(net.minecraft.data.tags.VanillaItemTagsProvider::new, vanillaRegistries));
/* 136 */     TagsProvider<Biome> vanillaBiomeTagsProvider = serverVanillaPack.<TagsProvider<Biome>>addProvider(bindRegistries(net.minecraft.data.tags.BiomeTagsProvider::new, vanillaRegistries));
/* 137 */     TagsProvider<BannerPattern> vanillaBannerPatternTagsProvider = serverVanillaPack.<TagsProvider<BannerPattern>>addProvider(bindRegistries(net.minecraft.data.tags.BannerPatternTagsProvider::new, vanillaRegistries));
/* 138 */     TagsProvider<Structure> vanillaStructureTagsProvider = serverVanillaPack.<TagsProvider<Structure>>addProvider(bindRegistries(net.minecraft.data.tags.StructureTagsProvider::new, vanillaRegistries));
/* 140 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.DamageTypeTagsProvider::new, vanillaRegistries));
/* 141 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.DialogTagsProvider::new, vanillaRegistries));
/* 142 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.EntityTypeTagsProvider::new, vanillaRegistries));
/* 143 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.FlatLevelGeneratorPresetTagsProvider::new, vanillaRegistries));
/* 144 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.FluidTagsProvider::new, vanillaRegistries));
/* 145 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.GameEventTagsProvider::new, vanillaRegistries));
/* 146 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.InstrumentTagsProvider::new, vanillaRegistries));
/* 147 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.PaintingVariantTagsProvider::new, vanillaRegistries));
/* 148 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.PoiTypeTagsProvider::new, vanillaRegistries));
/* 149 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.WorldPresetTagsProvider::new, vanillaRegistries));
/* 150 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.VanillaEnchantmentTagsProvider::new, vanillaRegistries));
/* 151 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.TimelineTagsProvider::new, vanillaRegistries));
/* 152 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.PotionTagsProvider::new, vanillaRegistries));
/* 153 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.VillagerTradesTagsProvider::new, vanillaRegistries));
/* 154 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.FeatureTagsProvider::new, vanillaRegistries));
/* 158 */     DataGenerator.PackGenerator reportsVanillaPack = generator.getVanillaPack(reports);
/* 159 */     reportsVanillaPack.addProvider(bindRegistries(net.minecraft.data.info.BiomeParametersDumpReport::new, vanillaRegistries));
/* 160 */     reportsVanillaPack.addProvider(bindRegistries(net.minecraft.data.info.RegistryComponentsReport::new, vanillaRegistries));
/* 161 */     reportsVanillaPack.addProvider(bindRegistries(net.minecraft.data.info.BlockListReport::new, vanillaRegistries));
/* 162 */     reportsVanillaPack.addProvider(bindRegistries(net.minecraft.data.info.CommandsReport::new, vanillaRegistries));
/* 163 */     reportsVanillaPack.addProvider(net.minecraft.data.info.RegistryDumpReport::new);
/* 164 */     reportsVanillaPack.addProvider(net.minecraft.data.info.PacketReport::new);
/* 165 */     reportsVanillaPack.addProvider(net.minecraft.data.info.DatapackStructureReport::new);
/* 166 */     reportsVanillaPack.addProvider(net.minecraft.server.jsonrpc.dataprovider.JsonRpcApiSchema::new);
/* 170 */     CompletableFuture<RegistrySetBuilder.PatchedRegistries> tradeRebalanceRegistries = TradeRebalanceRegistries.createLookup(vanillaRegistries);
/* 171 */     CompletableFuture<HolderLookup.Provider> patchedRegistrySet = tradeRebalanceRegistries.thenApply(RegistrySetBuilder.PatchedRegistries::patches);
/* 173 */     DataGenerator.PackGenerator tradeRebalancePack = generator.getBuiltinDatapack(server, "trade_rebalance");
/* 174 */     tradeRebalancePack.addProvider(bindRegistries(net.minecraft.data.registries.RegistriesDatapackGenerator::new, patchedRegistrySet));
/* 175 */     tradeRebalancePack.addProvider(o -> PackMetadataGenerator.forFeaturePack(o, (Component)Component.translatable("dataPack.trade_rebalance.description"), FeatureFlagSet.of(FeatureFlags.TRADE_REBALANCE)));
/* 176 */     CompletableFuture<HolderLookup.Provider> patchedRegistries = tradeRebalanceRegistries.thenApply(RegistrySetBuilder.PatchedRegistries::full);
/* 177 */     tradeRebalancePack.addProvider(bindRegistries(TradeRebalanceLootTableProvider::create, patchedRegistries));
/* 178 */     tradeRebalancePack.addProvider(bindRegistries(net.minecraft.data.tags.TradeRebalanceEnchantmentTagsProvider::new, patchedRegistries));
/* 179 */     tradeRebalancePack.addProvider(bindRegistries(net.minecraft.data.tags.TradeRebalanceTradeTagsProvider::new, patchedRegistries));
/* 183 */     DataGenerator.PackGenerator redstoneChangesPack = generator.getBuiltinDatapack(server, "redstone_experiments");
/* 184 */     redstoneChangesPack.addProvider(o -> PackMetadataGenerator.forFeaturePack(o, (Component)Component.translatable("dataPack.redstone_experiments.description"), FeatureFlagSet.of(FeatureFlags.REDSTONE_EXPERIMENTS)));
/* 188 */     DataGenerator.PackGenerator minecartImprovementsPack = generator.getBuiltinDatapack(server, "minecart_improvements");
/* 189 */     minecartImprovementsPack.addProvider(o -> PackMetadataGenerator.forFeaturePack(o, (Component)Component.translatable("dataPack.minecart_improvements.description"), FeatureFlagSet.of(FeatureFlags.MINECART_IMPROVEMENTS)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\Main.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */