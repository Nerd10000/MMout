/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.references.ItemIds;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.Item;
/*     */ 
/*     */ public class DecoratedPotPatterns {
/*  13 */   public static final ResourceKey<DecoratedPotPattern> BLANK = create("blank");
/*     */   
/*  14 */   public static final ResourceKey<DecoratedPotPattern> ANGLER = create("angler");
/*     */   
/*  15 */   public static final ResourceKey<DecoratedPotPattern> ARCHER = create("archer");
/*     */   
/*  16 */   public static final ResourceKey<DecoratedPotPattern> ARMS_UP = create("arms_up");
/*     */   
/*  17 */   public static final ResourceKey<DecoratedPotPattern> BLADE = create("blade");
/*     */   
/*  18 */   public static final ResourceKey<DecoratedPotPattern> BREWER = create("brewer");
/*     */   
/*  19 */   public static final ResourceKey<DecoratedPotPattern> BURN = create("burn");
/*     */   
/*  20 */   public static final ResourceKey<DecoratedPotPattern> DANGER = create("danger");
/*     */   
/*  21 */   public static final ResourceKey<DecoratedPotPattern> EXPLORER = create("explorer");
/*     */   
/*  22 */   public static final ResourceKey<DecoratedPotPattern> FLOW = create("flow");
/*     */   
/*  23 */   public static final ResourceKey<DecoratedPotPattern> FRIEND = create("friend");
/*     */   
/*  24 */   public static final ResourceKey<DecoratedPotPattern> GUSTER = create("guster");
/*     */   
/*  25 */   public static final ResourceKey<DecoratedPotPattern> HEART = create("heart");
/*     */   
/*  26 */   public static final ResourceKey<DecoratedPotPattern> HEARTBREAK = create("heartbreak");
/*     */   
/*  27 */   public static final ResourceKey<DecoratedPotPattern> HOWL = create("howl");
/*     */   
/*  28 */   public static final ResourceKey<DecoratedPotPattern> MINER = create("miner");
/*     */   
/*  29 */   public static final ResourceKey<DecoratedPotPattern> MOURNER = create("mourner");
/*     */   
/*  30 */   public static final ResourceKey<DecoratedPotPattern> PLENTY = create("plenty");
/*     */   
/*  31 */   public static final ResourceKey<DecoratedPotPattern> PRIZE = create("prize");
/*     */   
/*  32 */   public static final ResourceKey<DecoratedPotPattern> SCRAPE = create("scrape");
/*     */   
/*  33 */   public static final ResourceKey<DecoratedPotPattern> SHEAF = create("sheaf");
/*     */   
/*  34 */   public static final ResourceKey<DecoratedPotPattern> SHELTER = create("shelter");
/*     */   
/*  35 */   public static final ResourceKey<DecoratedPotPattern> SKULL = create("skull");
/*     */   
/*  36 */   public static final ResourceKey<DecoratedPotPattern> SNORT = create("snort");
/*     */   
/*     */   public static void itemToPatternMappings(BiConsumer<ResourceKey<Item>, ResourceKey<DecoratedPotPattern>> itemToPattern) {
/*  40 */     itemToPattern.accept(ItemIds.BRICK, BLANK);
/*  42 */     itemToPattern.accept(ItemIds.ANGLER_POTTERY_SHERD, ANGLER);
/*  43 */     itemToPattern.accept(ItemIds.ARCHER_POTTERY_SHERD, ARCHER);
/*  44 */     itemToPattern.accept(ItemIds.ARMS_UP_POTTERY_SHERD, ARMS_UP);
/*  45 */     itemToPattern.accept(ItemIds.BLADE_POTTERY_SHERD, BLADE);
/*  46 */     itemToPattern.accept(ItemIds.BREWER_POTTERY_SHERD, BREWER);
/*  47 */     itemToPattern.accept(ItemIds.BURN_POTTERY_SHERD, BURN);
/*  48 */     itemToPattern.accept(ItemIds.DANGER_POTTERY_SHERD, DANGER);
/*  49 */     itemToPattern.accept(ItemIds.EXPLORER_POTTERY_SHERD, EXPLORER);
/*  50 */     itemToPattern.accept(ItemIds.FLOW_POTTERY_SHERD, FLOW);
/*  51 */     itemToPattern.accept(ItemIds.FRIEND_POTTERY_SHERD, FRIEND);
/*  52 */     itemToPattern.accept(ItemIds.GUSTER_POTTERY_SHERD, GUSTER);
/*  53 */     itemToPattern.accept(ItemIds.HEART_POTTERY_SHERD, HEART);
/*  54 */     itemToPattern.accept(ItemIds.HEARTBREAK_POTTERY_SHERD, HEARTBREAK);
/*  55 */     itemToPattern.accept(ItemIds.HOWL_POTTERY_SHERD, HOWL);
/*  56 */     itemToPattern.accept(ItemIds.MINER_POTTERY_SHERD, MINER);
/*  57 */     itemToPattern.accept(ItemIds.MOURNER_POTTERY_SHERD, MOURNER);
/*  58 */     itemToPattern.accept(ItemIds.PLENTY_POTTERY_SHERD, PLENTY);
/*  59 */     itemToPattern.accept(ItemIds.PRIZE_POTTERY_SHERD, PRIZE);
/*  60 */     itemToPattern.accept(ItemIds.SCRAPE_POTTERY_SHERD, SCRAPE);
/*  61 */     itemToPattern.accept(ItemIds.SHEAF_POTTERY_SHERD, SHEAF);
/*  62 */     itemToPattern.accept(ItemIds.SHELTER_POTTERY_SHERD, SHELTER);
/*  63 */     itemToPattern.accept(ItemIds.SKULL_POTTERY_SHERD, SKULL);
/*  64 */     itemToPattern.accept(ItemIds.SNORT_POTTERY_SHERD, SNORT);
/*     */   }
/*     */   
/*     */   private static ResourceKey<DecoratedPotPattern> create(String id) {
/*  68 */     return ResourceKey.create(Registries.DECORATED_POT_PATTERN, Identifier.withDefaultNamespace(id));
/*     */   }
/*     */   
/*     */   public static DecoratedPotPattern bootstrap(Registry<DecoratedPotPattern> registry) {
/*  72 */     register(registry, ANGLER, "angler_pottery_pattern");
/*  73 */     register(registry, ARCHER, "archer_pottery_pattern");
/*  74 */     register(registry, ARMS_UP, "arms_up_pottery_pattern");
/*  75 */     register(registry, BLADE, "blade_pottery_pattern");
/*  76 */     register(registry, BREWER, "brewer_pottery_pattern");
/*  77 */     register(registry, BURN, "burn_pottery_pattern");
/*  78 */     register(registry, DANGER, "danger_pottery_pattern");
/*  79 */     register(registry, EXPLORER, "explorer_pottery_pattern");
/*  80 */     register(registry, FLOW, "flow_pottery_pattern");
/*  81 */     register(registry, FRIEND, "friend_pottery_pattern");
/*  82 */     register(registry, GUSTER, "guster_pottery_pattern");
/*  83 */     register(registry, HEART, "heart_pottery_pattern");
/*  84 */     register(registry, HEARTBREAK, "heartbreak_pottery_pattern");
/*  85 */     register(registry, HOWL, "howl_pottery_pattern");
/*  86 */     register(registry, MINER, "miner_pottery_pattern");
/*  87 */     register(registry, MOURNER, "mourner_pottery_pattern");
/*  88 */     register(registry, PLENTY, "plenty_pottery_pattern");
/*  89 */     register(registry, PRIZE, "prize_pottery_pattern");
/*  90 */     register(registry, SCRAPE, "scrape_pottery_pattern");
/*  91 */     register(registry, SHEAF, "sheaf_pottery_pattern");
/*  92 */     register(registry, SHELTER, "shelter_pottery_pattern");
/*  93 */     register(registry, SKULL, "skull_pottery_pattern");
/*  94 */     register(registry, SNORT, "snort_pottery_pattern");
/*  96 */     return register(registry, BLANK, "decorated_pot_side");
/*     */   }
/*     */   
/*     */   private static DecoratedPotPattern register(Registry<DecoratedPotPattern> registry, ResourceKey<DecoratedPotPattern> id, String assetId) {
/* 100 */     return (DecoratedPotPattern)Registry.register(registry, id, new DecoratedPotPattern(Identifier.withDefaultNamespace(assetId)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\DecoratedPotPatterns.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */