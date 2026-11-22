/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ public class ResourceOrTagKeyArgument<T> implements ArgumentType<ResourceOrTagKeyArgument.Result<T>> {
/*  32 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "012", "#skeletons", "#minecraft:skeletons" });
/*     */   
/*     */   private final ResourceKey<? extends Registry<T>> registryKey;
/*     */   
/*     */   private static final class ResourceResult<T> extends Record implements Result<T> {
/*     */     private final ResourceKey<T> key;
/*     */     
/*     */     private ResourceResult(ResourceKey<T> key) {
/*  42 */       this.key = key;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$ResourceResult<TT;>;
/*     */     }
/*     */     
/*     */     public ResourceKey<T> key() {
/*  42 */       return this.key;
/*     */     }
/*     */     
/*     */     public Either<ResourceKey<T>, TagKey<T>> unwrap() {
/*  45 */       return Either.left(this.key);
/*     */     }
/*     */     
/*     */     public <E> Optional<ResourceOrTagKeyArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> registryKey) {
/*  50 */       return this.key.cast(registryKey).map(ResourceResult::new);
/*     */     }
/*     */     
/*     */     public boolean test(Holder<T> holder) {
/*  55 */       return holder.is(this.key);
/*     */     }
/*     */     
/*     */     public String asPrintable() {
/*  60 */       return this.key.identifier().toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TagResult<T> extends Record implements Result<T> {
/*     */     private final TagKey<T> key;
/*     */     
/*     */     private TagResult(TagKey<T> key) {
/*  64 */       this.key = key;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$TagResult<TT;>;
/*     */     }
/*     */     
/*     */     public TagKey<T> key() {
/*  64 */       return this.key;
/*     */     }
/*     */     
/*     */     public Either<ResourceKey<T>, TagKey<T>> unwrap() {
/*  67 */       return Either.right(this.key);
/*     */     }
/*     */     
/*     */     public <E> Optional<ResourceOrTagKeyArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> registryKey) {
/*  72 */       return this.key.cast(registryKey).map(TagResult::new);
/*     */     }
/*     */     
/*     */     public boolean test(Holder<T> holder) {
/*  77 */       return holder.is(this.key);
/*     */     }
/*     */     
/*     */     public String asPrintable() {
/*  82 */       return "#" + String.valueOf(this.key.location());
/*     */     }
/*     */   }
/*     */   
/*     */   public ResourceOrTagKeyArgument(ResourceKey<? extends Registry<T>> registryKey) {
/*  89 */     this.registryKey = registryKey;
/*     */   }
/*     */   
/*     */   public static <T> ResourceOrTagKeyArgument<T> resourceOrTagKey(ResourceKey<? extends Registry<T>> key) {
/*  93 */     return new ResourceOrTagKeyArgument<>(key);
/*     */   }
/*     */   
/*     */   public static <T> Result<T> getResourceOrTagKey(CommandContext<CommandSourceStack> context, String name, ResourceKey<Registry<T>> registryKey, DynamicCommandExceptionType exceptionType) throws CommandSyntaxException {
/*  97 */     Result<?> argument = (Result)context.getArgument(name, Result.class);
/*  99 */     Optional<Result<T>> value = argument.cast(registryKey);
/* 100 */     return value.<Throwable>orElseThrow(() -> exceptionType.create(argument));
/*     */   }
/*     */   
/*     */   public Result<T> parse(StringReader reader) throws CommandSyntaxException {
/* 105 */     if (reader.canRead() && reader.peek() == '#') {
/* 106 */       int cursor = reader.getCursor();
/*     */       try {
/* 108 */         reader.skip();
/* 109 */         Identifier tagId = Identifier.read(reader);
/* 110 */         return new TagResult<>(TagKey.create(this.registryKey, tagId));
/* 111 */       } catch (CommandSyntaxException e) {
/* 112 */         reader.setCursor(cursor);
/* 113 */         throw e;
/*     */       } 
/*     */     } 
/* 116 */     Identifier resourceId = Identifier.read(reader);
/* 117 */     return new ResourceResult<>(ResourceKey.create(this.registryKey, resourceId));
/*     */   }
/*     */   
/*     */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 122 */     return SharedSuggestionProvider.listSuggestions(context, builder, this.registryKey, SharedSuggestionProvider.ElementSuggestionType.ALL);
/*     */   }
/*     */   
/*     */   public Collection<String> getExamples() {
/* 127 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   public static class Info<T> implements ArgumentTypeInfo<ResourceOrTagKeyArgument<T>, Info<T>.Template> {
/*     */     public final class Template implements ArgumentTypeInfo.Template<ResourceOrTagKeyArgument<T>> {
/*     */       private final ResourceKey<? extends Registry<T>> registryKey;
/*     */       
/*     */       private Template(ResourceKey<? extends Registry<T>> registryKey) {
/* 135 */         this.registryKey = registryKey;
/*     */       }
/*     */       
/*     */       public ResourceOrTagKeyArgument<T> instantiate(CommandBuildContext context) {
/* 140 */         return new ResourceOrTagKeyArgument<>(this.registryKey);
/*     */       }
/*     */       
/*     */       public ArgumentTypeInfo<ResourceOrTagKeyArgument<T>, ?> type() {
/* 145 */         return ResourceOrTagKeyArgument.Info.this;
/*     */       }
/*     */     }
/*     */     
/*     */     public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 151 */       out.writeResourceKey(template.registryKey);
/*     */     }
/*     */     
/*     */     public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 156 */       return new Template(in.readRegistryKey());
/*     */     }
/*     */     
/*     */     public void serializeToJson(Template template, JsonObject out) {
/* 161 */       out.addProperty("registry", template.registryKey.identifier().toString());
/*     */     }
/*     */     
/*     */     public Template unpack(ResourceOrTagKeyArgument<T> argument) {
/* 166 */       return new Template(argument.registryKey);
/*     */     }
/*     */   }
/*     */   
/*     */   public final class Template implements ArgumentTypeInfo.Template<ResourceOrTagKeyArgument<T>> {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private Template(ResourceKey<? extends Registry<T>> registryKey) {
/*     */       this.registryKey = registryKey;
/*     */     }
/*     */     
/*     */     public ResourceOrTagKeyArgument<T> instantiate(CommandBuildContext context) {
/*     */       return new ResourceOrTagKeyArgument<>(this.registryKey);
/*     */     }
/*     */     
/*     */     public ArgumentTypeInfo<ResourceOrTagKeyArgument<T>, ?> type() {
/*     */       return ResourceOrTagKeyArgument.Info.this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Result<T> extends Predicate<Holder<T>> {
/*     */     Either<ResourceKey<T>, TagKey<T>> unwrap();
/*     */     
/*     */     <E> Optional<Result<E>> cast(ResourceKey<? extends Registry<E>> param1ResourceKey);
/*     */     
/*     */     String asPrintable();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceOrTagKeyArgument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */