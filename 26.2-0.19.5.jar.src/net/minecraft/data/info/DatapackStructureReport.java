/*     */ package net.minecraft.data.info;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryDataLoader;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public class DatapackStructureReport implements DataProvider {
/*     */   private final PackOutput output;
/*     */   
/*  27 */   private static final Entry PSEUDO_REGISTRY = new Entry(true, false, true);
/*     */   
/*  29 */   private static final Entry STABLE_DYNAMIC_REGISTRY = new Entry(true, true, true);
/*     */   
/*  30 */   private static final Entry UNSTABLE_DYNAMIC_REGISTRY = new Entry(true, true, false);
/*     */   
/*  32 */   private static final Entry BUILT_IN_REGISTRY = new Entry(false, true, true);
/*     */   
/*  34 */   private static final Map<ResourceKey<? extends Registry<?>>, Entry> MANUAL_ENTRIES = Map.of(Registries.RECIPE, PSEUDO_REGISTRY, Registries.ADVANCEMENT, PSEUDO_REGISTRY, Registries.LOOT_TABLE, STABLE_DYNAMIC_REGISTRY, Registries.ITEM_MODIFIER, STABLE_DYNAMIC_REGISTRY, Registries.PREDICATE, STABLE_DYNAMIC_REGISTRY);
/*     */   
/*  45 */   private static final Map<String, CustomPackEntry> NON_REGISTRY_ENTRIES = Map.of("structure", new CustomPackEntry(Format.STRUCTURE, new Entry(true, false, true)), "function", new CustomPackEntry(Format.MCFUNCTION, new Entry(true, true, true)));
/*     */   
/*  50 */   private static final Codec<ResourceKey<? extends Registry<?>>> REGISTRY_KEY_CODEC = Identifier.CODEC.xmap(ResourceKey::createRegistryKey, ResourceKey::identifier);
/*     */   
/*     */   private static final class Report extends Record {
/*     */     private final Map<ResourceKey<? extends Registry<?>>, DatapackStructureReport.Entry> registries;
/*     */     
/*     */     private final Map<String, DatapackStructureReport.CustomPackEntry> others;
/*     */     
/*     */     public static final Codec<Report> CODEC;
/*     */     
/*     */     private Report(Map<ResourceKey<? extends Registry<?>>, DatapackStructureReport.Entry> registries, Map<String, DatapackStructureReport.CustomPackEntry> others) {
/*  52 */       this.registries = registries;
/*  52 */       this.others = others;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/info/DatapackStructureReport$Report;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #52	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Report;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/info/DatapackStructureReport$Report;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #52	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Report;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/info/DatapackStructureReport$Report;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #52	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Report;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Map<ResourceKey<? extends Registry<?>>, DatapackStructureReport.Entry> registries() {
/*  52 */       return this.registries;
/*     */     }
/*     */     
/*     */     public Map<String, DatapackStructureReport.CustomPackEntry> others() {
/*  52 */       return this.others;
/*     */     }
/*     */     
/*     */     static {
/*  56 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.unboundedMap(DatapackStructureReport.REGISTRY_KEY_CODEC, DatapackStructureReport.Entry.CODEC).fieldOf("registries").forGetter(Report::registries), (App)Codec.unboundedMap((Codec)Codec.STRING, DatapackStructureReport.CustomPackEntry.CODEC).fieldOf("others").forGetter(Report::others)).apply((Applicative)i, Report::new));
/*     */     }
/*     */   }
/*     */   
/*     */   public DatapackStructureReport(PackOutput output) {
/*  63 */     this.output = output;
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> run(CachedOutput cache) {
/*  69 */     Report report = new Report(listRegistries(), NON_REGISTRY_ENTRIES);
/*  73 */     Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("datapack.json");
/*  74 */     return DataProvider.saveStable(cache, (JsonElement)Report.CODEC.encodeStart((DynamicOps)JsonOps.INSTANCE, report).getOrThrow(), path);
/*     */   }
/*     */   
/*     */   public String getName() {
/*  79 */     return "Datapack Structure";
/*     */   }
/*     */   
/*     */   private void putIfNotPresent(Map<ResourceKey<? extends Registry<?>>, Entry> output, ResourceKey<? extends Registry<?>> key, Entry entry) {
/*  83 */     Entry previous = output.putIfAbsent(key, entry);
/*  84 */     if (previous != null)
/*  85 */       throw new IllegalStateException("Duplicate entry for key " + String.valueOf(key.identifier())); 
/*     */   }
/*     */   
/*     */   private Map<ResourceKey<? extends Registry<?>>, Entry> listRegistries() {
/*  90 */     Map<ResourceKey<? extends Registry<?>>, Entry> result = new HashMap<>();
/*  93 */     BuiltInRegistries.REGISTRY.forEach(entry -> putIfNotPresent(result, entry.key(), BUILT_IN_REGISTRY));
/*  94 */     RegistryDataLoader.WORLDGEN_REGISTRIES.forEach(entry -> putIfNotPresent(result, entry.key(), UNSTABLE_DYNAMIC_REGISTRY));
/*  95 */     RegistryDataLoader.DIMENSION_REGISTRIES.forEach(entry -> putIfNotPresent(result, entry.key(), UNSTABLE_DYNAMIC_REGISTRY));
/*  98 */     MANUAL_ENTRIES.forEach((key, entry) -> putIfNotPresent(result, key, entry));
/* 100 */     return result;
/*     */   }
/*     */   
/*     */   private static final class Entry extends Record {
/*     */     private final boolean elements;
/*     */     
/*     */     private final boolean tags;
/*     */     
/*     */     private final boolean stable;
/*     */     
/*     */     public static final MapCodec<Entry> MAP_CODEC;
/*     */     
/*     */     private Entry(boolean elements, boolean tags, boolean stable) {
/* 103 */       this.elements = elements;
/* 103 */       this.tags = tags;
/* 103 */       this.stable = stable;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/info/DatapackStructureReport$Entry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #103	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Entry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/info/DatapackStructureReport$Entry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #103	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Entry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/info/DatapackStructureReport$Entry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #103	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Entry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public boolean elements() {
/* 103 */       return this.elements;
/*     */     }
/*     */     
/*     */     public boolean tags() {
/* 103 */       return this.tags;
/*     */     }
/*     */     
/*     */     public boolean stable() {
/* 103 */       return this.stable;
/*     */     }
/*     */     
/*     */     static {
/* 108 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.fieldOf("elements").forGetter(Entry::elements), (App)Codec.BOOL.fieldOf("tags").forGetter(Entry::tags), (App)Codec.BOOL.fieldOf("stable").forGetter(Entry::stable)).apply((Applicative)i, Entry::new));
/*     */     }
/*     */     
/* 114 */     public static final Codec<Entry> CODEC = MAP_CODEC.codec();
/*     */   }
/*     */   
/*     */   private enum Format implements StringRepresentable {
/* 118 */     STRUCTURE("structure"),
/* 119 */     MCFUNCTION("mcfunction");
/*     */     
/* 122 */     public static final Codec<Format> CODEC = (Codec<Format>)StringRepresentable.fromEnum(Format::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     Format(String name) {
/* 127 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 132 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class CustomPackEntry extends Record {
/*     */     private final DatapackStructureReport.Format format;
/*     */     
/*     */     private final DatapackStructureReport.Entry entry;
/*     */     
/*     */     public static final Codec<CustomPackEntry> CODEC;
/*     */     
/*     */     private CustomPackEntry(DatapackStructureReport.Format format, DatapackStructureReport.Entry entry) {
/* 136 */       this.format = format;
/* 136 */       this.entry = entry;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #136	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #136	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #136	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public DatapackStructureReport.Format format() {
/* 136 */       return this.format;
/*     */     }
/*     */     
/*     */     public DatapackStructureReport.Entry entry() {
/* 136 */       return this.entry;
/*     */     }
/*     */     
/*     */     static {
/* 140 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)DatapackStructureReport.Format.CODEC.fieldOf("format").forGetter(CustomPackEntry::format), (App)DatapackStructureReport.Entry.MAP_CODEC.forGetter(CustomPackEntry::entry)).apply((Applicative)i, CustomPackEntry::new));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\info\DatapackStructureReport.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */