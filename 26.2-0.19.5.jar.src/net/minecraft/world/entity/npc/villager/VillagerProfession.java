/*     */ package net.minecraft.world.entity.npc.villager;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.PoiTypeTags;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.trading.TradeSet;
/*     */ import net.minecraft.world.item.trading.TradeSets;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ public final class VillagerProfession extends Record {
/*     */   private final Component name;
/*     */   
/*     */   private final Predicate<Holder<PoiType>> heldJobSite;
/*     */   
/*     */   private final Predicate<Holder<PoiType>> acquirableJobSite;
/*     */   
/*     */   private final ImmutableSet<Item> requestedItems;
/*     */   
/*     */   private final ImmutableSet<Block> secondaryPoi;
/*     */   
/*     */   private final SoundEvent workSound;
/*     */   
/*     */   private final Int2ObjectMap<ResourceKey<TradeSet>> tradeSetsByLevel;
/*     */   
/*     */   public static final Predicate<Holder<PoiType>> ALL_ACQUIRABLE_JOBS;
/*     */   
/*     */   public VillagerProfession(Component name, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, ImmutableSet<Item> requestedItems, ImmutableSet<Block> secondaryPoi, SoundEvent workSound, Int2ObjectMap<ResourceKey<TradeSet>> tradeSetsByLevel) {
/*  26 */     this.name = name;
/*  26 */     this.heldJobSite = heldJobSite;
/*  26 */     this.acquirableJobSite = acquirableJobSite;
/*  26 */     this.requestedItems = requestedItems;
/*  26 */     this.secondaryPoi = secondaryPoi;
/*  26 */     this.workSound = workSound;
/*  26 */     this.tradeSetsByLevel = tradeSetsByLevel;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/npc/villager/VillagerProfession;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #26	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerProfession;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/npc/villager/VillagerProfession;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #26	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerProfession;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/npc/villager/VillagerProfession;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #26	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerProfession;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Component name() {
/*  26 */     return this.name;
/*     */   }
/*     */   
/*     */   public Predicate<Holder<PoiType>> heldJobSite() {
/*  26 */     return this.heldJobSite;
/*     */   }
/*     */   
/*     */   public Predicate<Holder<PoiType>> acquirableJobSite() {
/*  26 */     return this.acquirableJobSite;
/*     */   }
/*     */   
/*     */   public ImmutableSet<Item> requestedItems() {
/*  26 */     return this.requestedItems;
/*     */   }
/*     */   
/*     */   public ImmutableSet<Block> secondaryPoi() {
/*  26 */     return this.secondaryPoi;
/*     */   }
/*     */   
/*     */   public SoundEvent workSound() {
/*  26 */     return this.workSound;
/*     */   }
/*     */   
/*     */   public Int2ObjectMap<ResourceKey<TradeSet>> tradeSetsByLevel() {
/*  26 */     return this.tradeSetsByLevel;
/*     */   }
/*     */   
/*     */   static {
/*  35 */     ALL_ACQUIRABLE_JOBS = (holder -> holder.is(PoiTypeTags.ACQUIRABLE_JOB_SITE));
/*     */   }
/*     */   
/*  37 */   public static final ResourceKey<VillagerProfession> NONE = createKey("none");
/*     */   
/*  38 */   public static final ResourceKey<VillagerProfession> ARMORER = createKey("armorer");
/*     */   
/*  39 */   public static final ResourceKey<VillagerProfession> BUTCHER = createKey("butcher");
/*     */   
/*  40 */   public static final ResourceKey<VillagerProfession> CARTOGRAPHER = createKey("cartographer");
/*     */   
/*  41 */   public static final ResourceKey<VillagerProfession> CLERIC = createKey("cleric");
/*     */   
/*  42 */   public static final ResourceKey<VillagerProfession> FARMER = createKey("farmer");
/*     */   
/*  43 */   public static final ResourceKey<VillagerProfession> FISHERMAN = createKey("fisherman");
/*     */   
/*  44 */   public static final ResourceKey<VillagerProfession> FLETCHER = createKey("fletcher");
/*     */   
/*  45 */   public static final ResourceKey<VillagerProfession> LEATHERWORKER = createKey("leatherworker");
/*     */   
/*  46 */   public static final ResourceKey<VillagerProfession> LIBRARIAN = createKey("librarian");
/*     */   
/*  47 */   public static final ResourceKey<VillagerProfession> MASON = createKey("mason");
/*     */   
/*  48 */   public static final ResourceKey<VillagerProfession> NITWIT = createKey("nitwit");
/*     */   
/*  49 */   public static final ResourceKey<VillagerProfession> SHEPHERD = createKey("shepherd");
/*     */   
/*  50 */   public static final ResourceKey<VillagerProfession> TOOLSMITH = createKey("toolsmith");
/*     */   
/*  51 */   public static final ResourceKey<VillagerProfession> WEAPONSMITH = createKey("weaponsmith");
/*     */   
/*     */   private static ResourceKey<VillagerProfession> createKey(String name) {
/*  54 */     return ResourceKey.create(Registries.VILLAGER_PROFESSION, Identifier.withDefaultNamespace(name));
/*     */   }
/*     */   
/*     */   private static VillagerProfession register(Registry<VillagerProfession> registry, ResourceKey<VillagerProfession> name, ResourceKey<PoiType> jobSite, SoundEvent workSound, Int2ObjectMap<ResourceKey<TradeSet>> trades) {
/*  58 */     return register(registry, name, poiType -> poiType.is(jobSite), poiType -> poiType.is(jobSite), workSound, trades);
/*     */   }
/*     */   
/*     */   private static VillagerProfession register(Registry<VillagerProfession> registry, ResourceKey<VillagerProfession> name, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, SoundEvent workSound) {
/*  62 */     return register(registry, name, heldJobSite, acquirableJobSite, ImmutableSet.of(), ImmutableSet.of(), workSound, Int2ObjectMap.ofEntries(new Int2ObjectMap.Entry[0]));
/*     */   }
/*     */   
/*     */   private static VillagerProfession register(Registry<VillagerProfession> registry, ResourceKey<VillagerProfession> name, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, SoundEvent workSound, Int2ObjectMap<ResourceKey<TradeSet>> trades) {
/*  66 */     return register(registry, name, heldJobSite, acquirableJobSite, ImmutableSet.of(), ImmutableSet.of(), workSound, trades);
/*     */   }
/*     */   
/*     */   private static VillagerProfession register(Registry<VillagerProfession> registry, ResourceKey<VillagerProfession> name, ResourceKey<PoiType> jobSite, ImmutableSet<Item> requestedItems, ImmutableSet<Block> secondaryPoi, SoundEvent workSound, Int2ObjectMap<ResourceKey<TradeSet>> trades) {
/*  70 */     return register(registry, name, poiType -> poiType.is(jobSite), poiType -> poiType.is(jobSite), requestedItems, secondaryPoi, workSound, trades);
/*     */   }
/*     */   
/*     */   private static VillagerProfession register(Registry<VillagerProfession> registry, ResourceKey<VillagerProfession> name, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, ImmutableSet<Item> requestedItems, ImmutableSet<Block> secondaryPoi, SoundEvent workSound, Int2ObjectMap<ResourceKey<TradeSet>> trades) {
/*  74 */     return (VillagerProfession)Registry.register(registry, name, new VillagerProfession(
/*  75 */           (Component)Component.translatable("entity." + name.identifier().getNamespace() + ".villager." + name.identifier().getPath()), heldJobSite, acquirableJobSite, requestedItems, secondaryPoi, workSound, trades));
/*     */   }
/*     */   
/*     */   public static VillagerProfession bootstrap(Registry<VillagerProfession> registry) {
/*  86 */     register(registry, NONE, PoiType.NONE, ALL_ACQUIRABLE_JOBS, (SoundEvent)null);
/*  87 */     register(registry, ARMORER, PoiTypes.ARMORER, SoundEvents.VILLAGER_WORK_ARMORER, Int2ObjectMap.ofEntries(new Int2ObjectMap.Entry[] { Int2ObjectMap.entry(1, TradeSets.ARMORER_LEVEL_1), Int2ObjectMap.entry(2, TradeSets.ARMORER_LEVEL_2), Int2ObjectMap.entry(3, TradeSets.ARMORER_LEVEL_3), Int2ObjectMap.entry(4, TradeSets.ARMORER_LEVEL_4), Int2ObjectMap.entry(5, TradeSets.ARMORER_LEVEL_5) }));
/*  88 */     register(registry, BUTCHER, PoiTypes.BUTCHER, SoundEvents.VILLAGER_WORK_BUTCHER, Int2ObjectMap.ofEntries(new Int2ObjectMap.Entry[] { Int2ObjectMap.entry(1, TradeSets.BUTCHER_LEVEL_1), Int2ObjectMap.entry(2, TradeSets.BUTCHER_LEVEL_2), Int2ObjectMap.entry(3, TradeSets.BUTCHER_LEVEL_3), Int2ObjectMap.entry(4, TradeSets.BUTCHER_LEVEL_4), Int2ObjectMap.entry(5, TradeSets.BUTCHER_LEVEL_5) }));
/*  89 */     register(registry, CARTOGRAPHER, PoiTypes.CARTOGRAPHER, SoundEvents.VILLAGER_WORK_CARTOGRAPHER, Int2ObjectMap.ofEntries(new Int2ObjectMap.Entry[] { Int2ObjectMap.entry(1, TradeSets.CARTOGRAPHER_LEVEL_1), Int2ObjectMap.entry(2, TradeSets.CARTOGRAPHER_LEVEL_2), Int2ObjectMap.entry(3, TradeSets.CARTOGRAPHER_LEVEL_3), Int2ObjectMap.entry(4, TradeSets.CARTOGRAPHER_LEVEL_4), Int2ObjectMap.entry(5, TradeSets.CARTOGRAPHER_LEVEL_5) }));
/*  90 */     register(registry, CLERIC, PoiTypes.CLERIC, SoundEvents.VILLAGER_WORK_CLERIC, Int2ObjectMap.ofEntries(new Int2ObjectMap.Entry[] { Int2ObjectMap.entry(1, TradeSets.CLERIC_LEVEL_1), Int2ObjectMap.entry(2, TradeSets.CLERIC_LEVEL_2), Int2ObjectMap.entry(3, TradeSets.CLERIC_LEVEL_3), Int2ObjectMap.entry(4, TradeSets.CLERIC_LEVEL_4), Int2ObjectMap.entry(5, TradeSets.CLERIC_LEVEL_5) }));
/*  91 */     register(registry, FARMER, PoiTypes.FARMER, ImmutableSet.of(Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.BONE_MEAL), ImmutableSet.of(Blocks.FARMLAND), SoundEvents.VILLAGER_WORK_FARMER, Int2ObjectMap.ofEntries(new Int2ObjectMap.Entry[] { Int2ObjectMap.entry(1, TradeSets.FARMER_LEVEL_1), Int2ObjectMap.entry(2, TradeSets.FARMER_LEVEL_2), Int2ObjectMap.entry(3, TradeSets.FARMER_LEVEL_3), Int2ObjectMap.entry(4, TradeSets.FARMER_LEVEL_4), Int2ObjectMap.entry(5, TradeSets.FARMER_LEVEL_5) }));
/*  92 */     register(registry, FISHERMAN, PoiTypes.FISHERMAN, SoundEvents.VILLAGER_WORK_FISHERMAN, Int2ObjectMap.ofEntries(new Int2ObjectMap.Entry[] { Int2ObjectMap.entry(1, TradeSets.FISHERMAN_LEVEL_1), Int2ObjectMap.entry(2, TradeSets.FISHERMAN_LEVEL_2), Int2ObjectMap.entry(3, TradeSets.FISHERMAN_LEVEL_3), Int2ObjectMap.entry(4, TradeSets.FISHERMAN_LEVEL_4), Int2ObjectMap.entry(5, TradeSets.FISHERMAN_LEVEL_5) }));
/*  93 */     register(registry, FLETCHER, PoiTypes.FLETCHER, SoundEvents.VILLAGER_WORK_FLETCHER, Int2ObjectMap.ofEntries(new Int2ObjectMap.Entry[] { Int2ObjectMap.entry(1, TradeSets.FLETCHER_LEVEL_1), Int2ObjectMap.entry(2, TradeSets.FLETCHER_LEVEL_2), Int2ObjectMap.entry(3, TradeSets.FLETCHER_LEVEL_3), Int2ObjectMap.entry(4, TradeSets.FLETCHER_LEVEL_4), Int2ObjectMap.entry(5, TradeSets.FLETCHER_LEVEL_5) }));
/*  94 */     register(registry, LEATHERWORKER, PoiTypes.LEATHERWORKER, SoundEvents.VILLAGER_WORK_LEATHERWORKER, Int2ObjectMap.ofEntries(new Int2ObjectMap.Entry[] { Int2ObjectMap.entry(1, TradeSets.LEATHERWORKER_LEVEL_1), Int2ObjectMap.entry(2, TradeSets.LEATHERWORKER_LEVEL_2), Int2ObjectMap.entry(3, TradeSets.LEATHERWORKER_LEVEL_3), Int2ObjectMap.entry(4, TradeSets.LEATHERWORKER_LEVEL_4), Int2ObjectMap.entry(5, TradeSets.LEATHERWORKER_LEVEL_5) }));
/*  95 */     register(registry, LIBRARIAN, PoiTypes.LIBRARIAN, SoundEvents.VILLAGER_WORK_LIBRARIAN, Int2ObjectMap.ofEntries(new Int2ObjectMap.Entry[] { Int2ObjectMap.entry(1, TradeSets.LIBRARIAN_LEVEL_1), Int2ObjectMap.entry(2, TradeSets.LIBRARIAN_LEVEL_2), Int2ObjectMap.entry(3, TradeSets.LIBRARIAN_LEVEL_3), Int2ObjectMap.entry(4, TradeSets.LIBRARIAN_LEVEL_4), Int2ObjectMap.entry(5, TradeSets.LIBRARIAN_LEVEL_5) }));
/*  96 */     register(registry, MASON, PoiTypes.MASON, SoundEvents.VILLAGER_WORK_MASON, Int2ObjectMap.ofEntries(new Int2ObjectMap.Entry[] { Int2ObjectMap.entry(1, TradeSets.MASON_LEVEL_1), Int2ObjectMap.entry(2, TradeSets.MASON_LEVEL_2), Int2ObjectMap.entry(3, TradeSets.MASON_LEVEL_3), Int2ObjectMap.entry(4, TradeSets.MASON_LEVEL_4), Int2ObjectMap.entry(5, TradeSets.MASON_LEVEL_5) }));
/*  97 */     register(registry, NITWIT, PoiType.NONE, PoiType.NONE, (SoundEvent)null);
/*  98 */     register(registry, SHEPHERD, PoiTypes.SHEPHERD, SoundEvents.VILLAGER_WORK_SHEPHERD, Int2ObjectMap.ofEntries(new Int2ObjectMap.Entry[] { Int2ObjectMap.entry(1, TradeSets.SHEPHERD_LEVEL_1), Int2ObjectMap.entry(2, TradeSets.SHEPHERD_LEVEL_2), Int2ObjectMap.entry(3, TradeSets.SHEPHERD_LEVEL_3), Int2ObjectMap.entry(4, TradeSets.SHEPHERD_LEVEL_4), Int2ObjectMap.entry(5, TradeSets.SHEPHERD_LEVEL_5) }));
/*  99 */     register(registry, TOOLSMITH, PoiTypes.TOOLSMITH, SoundEvents.VILLAGER_WORK_TOOLSMITH, Int2ObjectMap.ofEntries(new Int2ObjectMap.Entry[] { Int2ObjectMap.entry(1, TradeSets.TOOLSMITH_LEVEL_1), Int2ObjectMap.entry(2, TradeSets.TOOLSMITH_LEVEL_2), Int2ObjectMap.entry(3, TradeSets.TOOLSMITH_LEVEL_3), Int2ObjectMap.entry(4, TradeSets.TOOLSMITH_LEVEL_4), Int2ObjectMap.entry(5, TradeSets.TOOLSMITH_LEVEL_5) }));
/* 100 */     return register(registry, WEAPONSMITH, PoiTypes.WEAPONSMITH, SoundEvents.VILLAGER_WORK_WEAPONSMITH, Int2ObjectMap.ofEntries(new Int2ObjectMap.Entry[] { Int2ObjectMap.entry(1, TradeSets.WEAPONSMITH_LEVEL_1), Int2ObjectMap.entry(2, TradeSets.WEAPONSMITH_LEVEL_2), Int2ObjectMap.entry(3, TradeSets.WEAPONSMITH_LEVEL_3), Int2ObjectMap.entry(4, TradeSets.WEAPONSMITH_LEVEL_4), Int2ObjectMap.entry(5, TradeSets.WEAPONSMITH_LEVEL_5) }));
/*     */   }
/*     */   
/*     */   public ResourceKey<TradeSet> getTrades(int level) {
/* 104 */     return (ResourceKey<TradeSet>)this.tradeSetsByLevel.get(level);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\npc\villager\VillagerProfession.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */