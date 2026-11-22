/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeManager;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ 
/*     */ public class ResourceKeyArgument<T> implements ArgumentType<ResourceKey<T>> {
/*  39 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "012" });
/*     */   
/*     */   private static final DynamicCommandExceptionType ERROR_INVALID_FEATURE;
/*     */   
/*     */   private static final DynamicCommandExceptionType ERROR_INVALID_STRUCTURE;
/*     */   
/*     */   private static final DynamicCommandExceptionType ERROR_INVALID_TEMPLATE_POOL;
/*     */   
/*     */   private static final DynamicCommandExceptionType ERROR_INVALID_RECIPE;
/*     */   
/*     */   private static final DynamicCommandExceptionType ERROR_INVALID_ADVANCEMENT;
/*     */   
/*     */   private final ResourceKey<? extends Registry<T>> registryKey;
/*     */   
/*     */   static {
/*  41 */     ERROR_INVALID_FEATURE = new DynamicCommandExceptionType(value -> Component.translatableEscape("commands.place.feature.invalid", new Object[] { value }));
/*  42 */     ERROR_INVALID_STRUCTURE = new DynamicCommandExceptionType(value -> Component.translatableEscape("commands.place.structure.invalid", new Object[] { value }));
/*  43 */     ERROR_INVALID_TEMPLATE_POOL = new DynamicCommandExceptionType(value -> Component.translatableEscape("commands.place.jigsaw.invalid", new Object[] { value }));
/*  44 */     ERROR_INVALID_RECIPE = new DynamicCommandExceptionType(value -> Component.translatableEscape("recipe.notFound", new Object[] { value }));
/*  45 */     ERROR_INVALID_ADVANCEMENT = new DynamicCommandExceptionType(value -> Component.translatableEscape("advancement.advancementNotFound", new Object[] { value }));
/*     */   }
/*     */   
/*     */   public ResourceKeyArgument(ResourceKey<? extends Registry<T>> registryKey) {
/*  50 */     this.registryKey = registryKey;
/*     */   }
/*     */   
/*     */   public static <T> ResourceKeyArgument<T> key(ResourceKey<? extends Registry<T>> key) {
/*  54 */     return new ResourceKeyArgument<>(key);
/*     */   }
/*     */   
/*     */   public static <T> ResourceKey<T> getRegistryKey(CommandContext<CommandSourceStack> context, String name, ResourceKey<Registry<T>> registryKey, DynamicCommandExceptionType exceptionType) throws CommandSyntaxException {
/*  58 */     ResourceKey<?> argument = (ResourceKey)context.getArgument(name, ResourceKey.class);
/*  60 */     Optional<ResourceKey<T>> value = argument.cast(registryKey);
/*  61 */     return value.<Throwable>orElseThrow(() -> exceptionType.create(argument.identifier()));
/*     */   }
/*     */   
/*     */   private static <T> Registry<T> getRegistry(CommandContext<CommandSourceStack> context, ResourceKey<? extends Registry<T>> registryKey) {
/*  65 */     return ((CommandSourceStack)context.getSource()).getServer().registryAccess().lookupOrThrow(registryKey);
/*     */   }
/*     */   
/*     */   private static <T> Holder.Reference<T> resolveKey(CommandContext<CommandSourceStack> context, String name, ResourceKey<Registry<T>> registryKey, DynamicCommandExceptionType exception) throws CommandSyntaxException {
/*  69 */     ResourceKey<T> key = getRegistryKey(context, name, registryKey, exception);
/*  70 */     return (Holder.Reference<T>)getRegistry(context, registryKey).get(key).orElseThrow(() -> exception.create(key.identifier()));
/*     */   }
/*     */   
/*     */   public static Holder.Reference<ConfiguredFeature<?, ?>> getConfiguredFeature(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  74 */     return resolveKey(context, name, Registries.CONFIGURED_FEATURE, ERROR_INVALID_FEATURE);
/*     */   }
/*     */   
/*     */   public static Holder.Reference<Structure> getStructure(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  78 */     return resolveKey(context, name, Registries.STRUCTURE, ERROR_INVALID_STRUCTURE);
/*     */   }
/*     */   
/*     */   public static Holder.Reference<StructureTemplatePool> getStructureTemplatePool(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  82 */     return resolveKey(context, name, Registries.TEMPLATE_POOL, ERROR_INVALID_TEMPLATE_POOL);
/*     */   }
/*     */   
/*     */   public static RecipeHolder<?> getRecipe(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  86 */     RecipeManager recipeManager = ((CommandSourceStack)context.getSource()).getServer().getRecipeManager();
/*  87 */     ResourceKey<Recipe<?>> key = getRegistryKey(context, name, Registries.RECIPE, ERROR_INVALID_RECIPE);
/*  88 */     return (RecipeHolder)recipeManager.byKey(key).orElseThrow(() -> ERROR_INVALID_RECIPE.create(key.identifier()));
/*     */   }
/*     */   
/*     */   public static AdvancementHolder getAdvancement(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  92 */     ResourceKey<Advancement> key = getRegistryKey(context, name, Registries.ADVANCEMENT, ERROR_INVALID_ADVANCEMENT);
/*  93 */     AdvancementHolder advancement = ((CommandSourceStack)context.getSource()).getServer().getAdvancements().get(key.identifier());
/*  94 */     if (advancement == null)
/*  95 */       throw ERROR_INVALID_ADVANCEMENT.create(key.identifier()); 
/*  97 */     return advancement;
/*     */   }
/*     */   
/*     */   public ResourceKey<T> parse(StringReader reader) throws CommandSyntaxException {
/* 102 */     Identifier resourceId = Identifier.read(reader);
/* 103 */     return ResourceKey.create(this.registryKey, resourceId);
/*     */   }
/*     */   
/*     */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 108 */     return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider.ElementSuggestionType.ELEMENTS);
/*     */   }
/*     */   
/*     */   public Collection<String> getExamples() {
/* 113 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   public static class Info<T> implements ArgumentTypeInfo<ResourceKeyArgument<T>, Info<T>.Template> {
/*     */     public final class Template implements ArgumentTypeInfo.Template<ResourceKeyArgument<T>> {
/*     */       private final ResourceKey<? extends Registry<T>> registryKey;
/*     */       
/*     */       private Template(ResourceKey<? extends Registry<T>> registryKey) {
/* 121 */         this.registryKey = registryKey;
/*     */       }
/*     */       
/*     */       public ResourceKeyArgument<T> instantiate(CommandBuildContext context) {
/* 126 */         return new ResourceKeyArgument<>(this.registryKey);
/*     */       }
/*     */       
/*     */       public ArgumentTypeInfo<ResourceKeyArgument<T>, ?> type() {
/* 131 */         return ResourceKeyArgument.Info.this;
/*     */       }
/*     */     }
/*     */     
/*     */     public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 137 */       out.writeResourceKey(template.registryKey);
/*     */     }
/*     */     
/*     */     public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 142 */       return new Template(in.readRegistryKey());
/*     */     }
/*     */     
/*     */     public void serializeToJson(Template template, JsonObject out) {
/* 147 */       out.addProperty("registry", template.registryKey.identifier().toString());
/*     */     }
/*     */     
/*     */     public Template unpack(ResourceKeyArgument<T> argument) {
/* 152 */       return new Template(argument.registryKey);
/*     */     }
/*     */   }
/*     */   
/*     */   public final class Template implements ArgumentTypeInfo.Template<ResourceKeyArgument<T>> {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private Template(ResourceKey<? extends Registry<T>> registryKey) {
/*     */       this.registryKey = registryKey;
/*     */     }
/*     */     
/*     */     public ResourceKeyArgument<T> instantiate(CommandBuildContext context) {
/*     */       return new ResourceKeyArgument<>(this.registryKey);
/*     */     }
/*     */     
/*     */     public ArgumentTypeInfo<ResourceKeyArgument<T>, ?> type() {
/*     */       return ResourceKeyArgument.Info.this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceKeyArgument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */