/*     */ package net.minecraft.server;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.PrintStream;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.SuppressForbidden;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
/*     */ import net.minecraft.core.cauldron.CauldronInteractions;
/*     */ import net.minecraft.core.dispenser.DispenseItemBehavior;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.item.CreativeModeTabs;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.ComposterBlock;
/*     */ import net.minecraft.world.level.block.FireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ @SuppressForbidden(reason = "System.out setup")
/*     */ public class Bootstrap {
/*  39 */   public static final PrintStream STDOUT = System.out;
/*     */   
/*     */   private static volatile boolean isBootstrapped;
/*     */   
/*  42 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  44 */   public static final AtomicLong bootstrapDuration = new AtomicLong(-1L);
/*     */   
/*     */   public static void bootStrap() {
/*  47 */     if (isBootstrapped)
/*     */       return; 
/*  50 */     isBootstrapped = true;
/*  52 */     Instant start = Instant.now();
/*  54 */     if (BuiltInRegistries.REGISTRY.keySet().isEmpty())
/*  55 */       throw new IllegalStateException("Unable to load registries"); 
/*  58 */     FireBlock.bootStrap();
/*  59 */     ComposterBlock.bootStrap();
/*  61 */     if (EntityType.getKey(EntityTypes.PLAYER) == null)
/*  62 */       throw new IllegalStateException("Failed loading EntityTypes"); 
/*  65 */     EntitySelectorOptions.bootStrap();
/*  67 */     DispenseItemBehavior.bootStrap();
/*  69 */     CauldronInteractions.bootStrap();
/*  71 */     BuiltInRegistries.bootStrap();
/*  73 */     CreativeModeTabs.validate();
/*  75 */     wrapStreams();
/*  77 */     bootstrapDuration.set(Duration.between(start, Instant.now()).toMillis());
/*     */   }
/*     */   
/*     */   private static <T> void checkTranslations(Language language, Iterable<T> registry, Function<T, String> descriptionGetter, Set<String> output) {
/*  81 */     registry.forEach(t -> {
/*     */           String id = descriptionGetter.apply(t);
/*     */           if (!language.has(id))
/*     */             output.add(id); 
/*     */         });
/*     */   }
/*     */   
/*     */   private static void checkGameruleTranslations(final Language language, final Set<String> missing) {
/*  90 */     GameRules rules = new GameRules(FeatureFlags.REGISTRY.allFlags());
/*  91 */     rules.visitGameRuleTypes(new GameRuleTypeVisitor() {
/*     */           public <T> void visit(GameRule<T> gameRule) {
/*  94 */             if (!language.has(gameRule.getDescriptionId()))
/*  95 */               missing.add(gameRule.id()); 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static Set<String> getMissingTranslations(Language language) {
/* 102 */     Set<String> missing = new TreeSet<>();
/* 103 */     checkTranslations(language, (Iterable<?>)BuiltInRegistries.ATTRIBUTE, Attribute::getDescriptionId, missing);
/* 104 */     checkTranslations(language, (Iterable<?>)BuiltInRegistries.ENTITY_TYPE, EntityType::getDescriptionId, missing);
/* 105 */     checkTranslations(language, (Iterable<?>)BuiltInRegistries.MOB_EFFECT, MobEffect::getDescriptionId, missing);
/* 106 */     checkTranslations(language, (Iterable<?>)BuiltInRegistries.ITEM, Item::getDescriptionId, missing);
/* 107 */     checkTranslations(language, (Iterable<?>)BuiltInRegistries.BLOCK, BlockBehaviour::getDescriptionId, missing);
/* 108 */     checkTranslations(language, (Iterable<?>)BuiltInRegistries.CUSTOM_STAT, id -> "stat." + id.toString().replace(':', '.'), missing);
/* 110 */     checkGameruleTranslations(language, missing);
/* 111 */     return missing;
/*     */   }
/*     */   
/*     */   public static void checkBootstrapCalled(Supplier<String> location) {
/* 115 */     if (!isBootstrapped)
/* 116 */       throw createBootstrapException(location); 
/*     */   }
/*     */   
/*     */   private static RuntimeException createBootstrapException(Supplier<String> location) {
/*     */     try {
/* 122 */       String resolvedLocation = location.get();
/* 123 */       return new IllegalArgumentException("Not bootstrapped (called from " + resolvedLocation + ")");
/* 124 */     } catch (Exception e) {
/* 125 */       RuntimeException result = new IllegalArgumentException("Not bootstrapped (failed to resolve location)");
/* 126 */       result.addSuppressed(e);
/* 127 */       return result;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void validate() {
/* 132 */     checkBootstrapCalled(() -> "validate");
/* 134 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 135 */       getMissingTranslations(Language.DEFAULT_INSTANCE).forEach(key -> LOGGER.error("Missing translations: {}", key));
/* 136 */       Commands.validate();
/*     */     } 
/* 139 */     DefaultAttributes.validate();
/*     */   }
/*     */   
/*     */   private static void wrapStreams() {
/* 143 */     if (LOGGER.isDebugEnabled()) {
/* 144 */       System.setErr(new DebugLoggedPrintStream("STDERR", System.err));
/* 145 */       System.setOut(new DebugLoggedPrintStream("STDOUT", STDOUT));
/*     */     } else {
/* 147 */       System.setErr(new LoggedPrintStream("STDERR", System.err));
/* 148 */       System.setOut(new LoggedPrintStream("STDOUT", STDOUT));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void realStdoutPrintln(String string) {
/* 153 */     STDOUT.println(string);
/*     */   }
/*     */   
/*     */   public static void shutdownStdout() {
/* 157 */     STDOUT.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\Bootstrap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */