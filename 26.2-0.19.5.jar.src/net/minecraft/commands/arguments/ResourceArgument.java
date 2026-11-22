/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.clock.WorldClock;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ 
/*     */ public class ResourceArgument<T> implements ArgumentType<Holder.Reference<T>> {
/*  39 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "012" });
/*     */   
/*     */   private static final DynamicCommandExceptionType ERROR_NOT_SUMMONABLE_ENTITY;
/*     */   
/*     */   public static final Dynamic2CommandExceptionType ERROR_UNKNOWN_RESOURCE;
/*     */   
/*     */   public static final Dynamic3CommandExceptionType ERROR_INVALID_RESOURCE_TYPE;
/*     */   
/*     */   private final ResourceKey<? extends Registry<T>> registryKey;
/*     */   
/*     */   private final HolderLookup<T> registryLookup;
/*     */   
/*     */   static {
/*  41 */     ERROR_NOT_SUMMONABLE_ENTITY = new DynamicCommandExceptionType(value -> Component.translatableEscape("entity.not_summonable", new Object[] { value }));
/*  43 */     ERROR_UNKNOWN_RESOURCE = new Dynamic2CommandExceptionType((id, registry) -> Component.translatableEscape("argument.resource.not_found", new Object[] { id, registry }));
/*  44 */     ERROR_INVALID_RESOURCE_TYPE = new Dynamic3CommandExceptionType((id, actualRegistry, expectedRegistry) -> Component.translatableEscape("argument.resource.invalid_type", new Object[] { id, actualRegistry, expectedRegistry }));
/*     */   }
/*     */   
/*     */   public ResourceArgument(CommandBuildContext context, ResourceKey<? extends Registry<T>> registryKey) {
/*  50 */     this.registryKey = registryKey;
/*  51 */     this.registryLookup = (HolderLookup<T>)context.lookupOrThrow(registryKey);
/*     */   }
/*     */   
/*     */   public static <T> ResourceArgument<T> resource(CommandBuildContext context, ResourceKey<? extends Registry<T>> key) {
/*  55 */     return new ResourceArgument<>(context, key);
/*     */   }
/*     */   
/*     */   public static <T> Holder.Reference<T> getResource(CommandContext<CommandSourceStack> context, String name, ResourceKey<Registry<T>> registryKey) throws CommandSyntaxException {
/*  60 */     Holder.Reference<T> argument = (Holder.Reference<T>)context.getArgument(name, Holder.Reference.class);
/*  62 */     ResourceKey<?> argumentKey = argument.key();
/*  63 */     if (argumentKey.isFor(registryKey))
/*  64 */       return argument; 
/*  67 */     throw ERROR_INVALID_RESOURCE_TYPE.create(argumentKey.identifier(), argumentKey.registry(), registryKey.identifier());
/*     */   }
/*     */   
/*     */   public static Holder.Reference<Attribute> getAttribute(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  71 */     return getResource(context, name, Registries.ATTRIBUTE);
/*     */   }
/*     */   
/*     */   public static Holder.Reference<ConfiguredFeature<?, ?>> getConfiguredFeature(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  75 */     return getResource(context, name, Registries.CONFIGURED_FEATURE);
/*     */   }
/*     */   
/*     */   public static Holder.Reference<Structure> getStructure(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  79 */     return getResource(context, name, Registries.STRUCTURE);
/*     */   }
/*     */   
/*     */   public static Holder.Reference<EntityType<?>> getEntityType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  83 */     return getResource(context, name, Registries.ENTITY_TYPE);
/*     */   }
/*     */   
/*     */   public static Holder.Reference<EntityType<?>> getSummonableEntityType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  87 */     Holder.Reference<EntityType<?>> result = getResource(context, name, Registries.ENTITY_TYPE);
/*  88 */     if (!((EntityType)result.value()).canSummon())
/*  89 */       throw ERROR_NOT_SUMMONABLE_ENTITY.create(result.key().identifier().toString()); 
/*  91 */     return result;
/*     */   }
/*     */   
/*     */   public static Holder.Reference<MobEffect> getMobEffect(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  95 */     return getResource(context, name, Registries.MOB_EFFECT);
/*     */   }
/*     */   
/*     */   public static Holder.Reference<Enchantment> getEnchantment(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  99 */     return getResource(context, name, Registries.ENCHANTMENT);
/*     */   }
/*     */   
/*     */   public static Holder.Reference<WorldClock> getClock(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/* 103 */     return getResource(context, name, Registries.WORLD_CLOCK);
/*     */   }
/*     */   
/*     */   public static Holder.Reference<Timeline> getTimeline(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/* 107 */     return getResource(context, name, Registries.TIMELINE);
/*     */   }
/*     */   
/*     */   public Holder.Reference<T> parse(StringReader reader) throws CommandSyntaxException {
/* 112 */     Identifier resourceId = Identifier.read(reader);
/* 113 */     ResourceKey<T> keyInRegistry = ResourceKey.create(this.registryKey, resourceId);
/* 114 */     return (Holder.Reference<T>)this.registryLookup.get(keyInRegistry).orElseThrow(() -> ERROR_UNKNOWN_RESOURCE.createWithContext((ImmutableStringReader)reader, resourceId, this.registryKey.identifier()));
/*     */   }
/*     */   
/*     */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 119 */     return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider.ElementSuggestionType.ELEMENTS);
/*     */   }
/*     */   
/*     */   public Collection<String> getExamples() {
/* 124 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   public static class Info<T> implements ArgumentTypeInfo<ResourceArgument<T>, Info<T>.Template> {
/*     */     public final class Template implements ArgumentTypeInfo.Template<ResourceArgument<T>> {
/*     */       private final ResourceKey<? extends Registry<T>> registryKey;
/*     */       
/*     */       private Template(ResourceKey<? extends Registry<T>> registryKey) {
/* 132 */         this.registryKey = registryKey;
/*     */       }
/*     */       
/*     */       public ResourceArgument<T> instantiate(CommandBuildContext context) {
/* 137 */         return new ResourceArgument<>(context, this.registryKey);
/*     */       }
/*     */       
/*     */       public ArgumentTypeInfo<ResourceArgument<T>, ?> type() {
/* 142 */         return ResourceArgument.Info.this;
/*     */       }
/*     */     }
/*     */     
/*     */     public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 148 */       out.writeResourceKey(template.registryKey);
/*     */     }
/*     */     
/*     */     public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 153 */       return new Template(in.readRegistryKey());
/*     */     }
/*     */     
/*     */     public void serializeToJson(Template template, JsonObject out) {
/* 158 */       out.addProperty("registry", template.registryKey.identifier().toString());
/*     */     }
/*     */     
/*     */     public Template unpack(ResourceArgument<T> argument) {
/* 163 */       return new Template(argument.registryKey);
/*     */     }
/*     */   }
/*     */   
/*     */   public final class Template implements ArgumentTypeInfo.Template<ResourceArgument<T>> {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private Template(ResourceKey<? extends Registry<T>> registryKey) {
/*     */       this.registryKey = registryKey;
/*     */     }
/*     */     
/*     */     public ResourceArgument<T> instantiate(CommandBuildContext context) {
/*     */       return new ResourceArgument<>(context, this.registryKey);
/*     */     }
/*     */     
/*     */     public ArgumentTypeInfo<ResourceArgument<T>, ?> type() {
/*     */       return ResourceArgument.Info.this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceArgument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */