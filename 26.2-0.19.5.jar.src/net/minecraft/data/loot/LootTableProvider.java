/*     */ package net.minecraft.data.loot;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.RegistrationInfo;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.context.ContextKeySet;
/*     */ import net.minecraft.world.RandomSequence;
/*     */ import net.minecraft.world.level.levelgen.RandomSupport;
/*     */ import net.minecraft.world.level.storage.loot.LootDataType;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContextSource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class LootTableProvider implements DataProvider {
/*  37 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final PackOutput.PathProvider pathProvider;
/*     */   
/*     */   private final Set<ResourceKey<LootTable>> requiredTables;
/*     */   
/*     */   private final List<SubProviderEntry> subProviders;
/*     */   
/*     */   private final CompletableFuture<HolderLookup.Provider> registries;
/*     */   
/*     */   public static final class SubProviderEntry extends Record {
/*     */     private final Function<HolderLookup.Provider, LootTableSubProvider> provider;
/*     */     
/*     */     private final ContextKeySet paramSet;
/*     */     
/*     */     public SubProviderEntry(Function<HolderLookup.Provider, LootTableSubProvider> provider, ContextKeySet paramSet) {
/*  39 */       this.provider = provider;
/*  39 */       this.paramSet = paramSet;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Function<HolderLookup.Provider, LootTableSubProvider> provider() {
/*  39 */       return this.provider;
/*     */     }
/*     */     
/*     */     public ContextKeySet paramSet() {
/*  39 */       return this.paramSet;
/*     */     }
/*     */   }
/*     */   
/*     */   public LootTableProvider(PackOutput output, Set<ResourceKey<LootTable>> requiredTables, List<SubProviderEntry> subProviders, CompletableFuture<HolderLookup.Provider> registries) {
/*  47 */     this.pathProvider = output.createRegistryElementsPathProvider(Registries.LOOT_TABLE);
/*  48 */     this.subProviders = subProviders;
/*  49 */     this.requiredTables = requiredTables;
/*  50 */     this.registries = registries;
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> run(CachedOutput cache) {
/*  55 */     return this.registries.thenCompose(registries -> run(cache, registries));
/*     */   }
/*     */   
/*     */   private CompletableFuture<?> run(CachedOutput cache, HolderLookup.Provider registries) {
/*  59 */     MappedRegistry mappedRegistry = new MappedRegistry(Registries.LOOT_TABLE, Lifecycle.experimental());
/*  61 */     Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap();
/*  63 */     this.subProviders.forEach(subProvider -> ((LootTableSubProvider)subProvider.provider().apply(registries)).generate(()));
/*  76 */     mappedRegistry.freeze();
/*  78 */     ProblemReporter.Collector problems = new ProblemReporter.Collector();
/*  79 */     RegistryAccess.Frozen frozen = (new RegistryAccess.ImmutableRegistryAccess(List.of(mappedRegistry))).freeze();
/*  81 */     ValidationContextSource validationContext = new ValidationContextSource((ProblemReporter)problems, (HolderGetter.Provider)frozen);
/*  83 */     Sets.SetView setView = Sets.difference(this.requiredTables, mappedRegistry.registryKeySet());
/*  85 */     for (ResourceKey<LootTable> missingTable : (Iterable<ResourceKey<LootTable>>)setView)
/*  86 */       problems.report(new MissingTableProblem(missingTable)); 
/*  89 */     LootDataType.TABLE.runValidation(validationContext, (HolderLookup)mappedRegistry);
/*  91 */     if (!problems.isEmpty()) {
/*  92 */       problems.forEach((id, problem) -> LOGGER.warn("Found validation problem in {}: {}", id, problem.description()));
/*  93 */       throw new IllegalStateException("Failed to validate loot tables, see logs");
/*     */     } 
/*  96 */     return CompletableFuture.allOf((CompletableFuture<?>[])mappedRegistry.entrySet().stream()
/*  97 */         .map(entry -> {
/*     */             ResourceKey<LootTable> id = (ResourceKey<LootTable>)entry.getKey();
/*     */             LootTable table = (LootTable)entry.getValue();
/*     */             Path path = this.pathProvider.json(id.identifier());
/*     */             return DataProvider.saveStable(cache, registries, LootTable.DIRECT_CODEC, table, path);
/* 103 */           }).toArray(x$0 -> new CompletableFuture[x$0]));
/*     */   }
/*     */   
/*     */   private static Identifier sequenceIdForLootTable(ResourceKey<LootTable> id) {
/* 107 */     return id.identifier();
/*     */   }
/*     */   
/*     */   public final String getName() {
/* 112 */     return "Loot Tables";
/*     */   }
/*     */   
/*     */   public static final class MissingTableProblem extends Record implements ProblemReporter.Problem {
/*     */     private final ResourceKey<LootTable> id;
/*     */     
/*     */     public MissingTableProblem(ResourceKey<LootTable> id) {
/* 115 */       this.id = id;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ResourceKey<LootTable> id() {
/* 115 */       return this.id;
/*     */     }
/*     */     
/*     */     public String description() {
/* 118 */       return "Missing built-in table: " + String.valueOf(this.id.identifier());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\loot\LootTableProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */