/*     */ package net.minecraft.commands.arguments.item;
/*     */ 
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentPatch;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.Item;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class ItemParser {
/*     */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_ITEM;
/*     */   
/*     */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_COMPONENT;
/*     */   
/*     */   private static final Dynamic2CommandExceptionType ERROR_MALFORMED_COMPONENT;
/*     */   
/*     */   static {
/*  37 */     ERROR_UNKNOWN_ITEM = new DynamicCommandExceptionType(id -> Component.translatableEscape("argument.item.id.invalid", new Object[] { id }));
/*  38 */     ERROR_UNKNOWN_COMPONENT = new DynamicCommandExceptionType(id -> Component.translatableEscape("arguments.item.component.unknown", new Object[] { id }));
/*  39 */     ERROR_MALFORMED_COMPONENT = new Dynamic2CommandExceptionType((type, message) -> Component.translatableEscape("arguments.item.component.malformed", new Object[] { type, message }));
/*     */   }
/*     */   
/*  40 */   private static final SimpleCommandExceptionType ERROR_EXPECTED_COMPONENT = new SimpleCommandExceptionType((Message)Component.translatable("arguments.item.component.expected"));
/*     */   
/*     */   private static final DynamicCommandExceptionType ERROR_REPEATED_COMPONENT;
/*     */   
/*     */   public static final char SYNTAX_START_COMPONENTS = '[';
/*     */   
/*     */   public static final char SYNTAX_END_COMPONENTS = ']';
/*     */   
/*     */   public static final char SYNTAX_COMPONENT_SEPARATOR = ',';
/*     */   
/*     */   public static final char SYNTAX_COMPONENT_ASSIGNMENT = '=';
/*     */   
/*     */   public static final char SYNTAX_REMOVED_COMPONENT = '!';
/*     */   
/*     */   static {
/*  41 */     ERROR_REPEATED_COMPONENT = new DynamicCommandExceptionType(id -> Component.translatableEscape("arguments.item.component.repeated", new Object[] { id }));
/*     */   }
/*     */   
/*  49 */   private static final Function<SuggestionsBuilder, CompletableFuture<Suggestions>> SUGGEST_NOTHING = SuggestionsBuilder::buildFuture;
/*     */   
/*     */   private final HolderLookup.RegistryLookup<Item> items;
/*     */   
/*     */   private final RegistryOps<Tag> registryOps;
/*     */   
/*     */   private final TagParser<Tag> tagParser;
/*     */   
/*     */   public ItemParser(HolderLookup.Provider registries) {
/*  56 */     this.items = registries.lookupOrThrow(Registries.ITEM);
/*  57 */     this.registryOps = registries.createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/*  58 */     this.tagParser = TagParser.create((DynamicOps)this.registryOps);
/*     */   }
/*     */   
/*     */   public ItemInput parse(StringReader reader) throws CommandSyntaxException {
/*  62 */     final MutableObject<Holder<Item>> itemResult = new MutableObject();
/*  63 */     final DataComponentPatch.Builder componentsBuilder = DataComponentPatch.builder();
/*  64 */     parse(reader, new Visitor(this) {
/*     */           public void visitItem(Holder<Item> item) {
/*  67 */             itemResult.setValue(item);
/*     */           }
/*     */           
/*     */           public <T> void visitComponent(DataComponentType<T> type, T value) {
/*  72 */             componentsBuilder.set(type, value);
/*     */           }
/*     */           
/*     */           public <T> void visitRemovedComponent(DataComponentType<T> type) {
/*  77 */             componentsBuilder.remove(type);
/*     */           }
/*     */         });
/*  80 */     Holder<Item> item = Objects.<Holder<Item>>requireNonNull((Holder<Item>)itemResult.get(), "Parser gave no item");
/*  81 */     DataComponentPatch components = componentsBuilder.build();
/*  82 */     return new ItemInput(item, components);
/*     */   }
/*     */   
/*     */   public void parse(StringReader reader, Visitor visitor) throws CommandSyntaxException {
/*  86 */     int cursor = reader.getCursor();
/*     */     try {
/*  88 */       (new State(reader, visitor)).parse();
/*  89 */     } catch (CommandSyntaxException e) {
/*  90 */       reader.setCursor(cursor);
/*  91 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder builder) {
/*  96 */     StringReader reader = new StringReader(builder.getInput());
/*  97 */     reader.setCursor(builder.getStart());
/*  98 */     SuggestionsVisitor handler = new SuggestionsVisitor();
/*  99 */     State state = new State(reader, handler);
/*     */     try {
/* 101 */       state.parse();
/* 102 */     } catch (CommandSyntaxException commandSyntaxException) {}
/* 105 */     return handler.resolveSuggestions(builder, reader);
/*     */   }
/*     */   
/*     */   private class State {
/*     */     private final StringReader reader;
/*     */     
/*     */     private final ItemParser.Visitor visitor;
/*     */     
/*     */     private State(StringReader reader, ItemParser.Visitor visitor) {
/* 113 */       this.reader = reader;
/* 114 */       this.visitor = visitor;
/*     */     }
/*     */     
/*     */     public void parse() throws CommandSyntaxException {
/* 118 */       this.visitor.visitSuggestions(this::suggestItem);
/* 119 */       readItem();
/* 120 */       this.visitor.visitSuggestions(this::suggestStartComponents);
/* 121 */       if (this.reader.canRead() && this.reader.peek() == '[') {
/* 122 */         this.visitor.visitSuggestions(ItemParser.SUGGEST_NOTHING);
/* 123 */         readComponents();
/*     */       } 
/*     */     }
/*     */     
/*     */     private void readItem() throws CommandSyntaxException {
/* 128 */       int cursor = this.reader.getCursor();
/* 129 */       Identifier id = Identifier.read(this.reader);
/* 130 */       this.visitor.visitItem((Holder<Item>)ItemParser.this.items.get(ResourceKey.create(Registries.ITEM, id)).orElseThrow(() -> {
/*     */               this.reader.setCursor(cursor);
/*     */               return ItemParser.ERROR_UNKNOWN_ITEM.createWithContext((ImmutableStringReader)this.reader, id);
/*     */             }));
/*     */     }
/*     */     
/*     */     private void readComponents() throws CommandSyntaxException {
/* 137 */       this.reader.expect('[');
/* 139 */       this.visitor.visitSuggestions(this::suggestComponentAssignmentOrRemoval);
/* 141 */       ReferenceArraySet<DataComponentType<?>> referenceArraySet = new ReferenceArraySet();
/* 143 */       while (this.reader.canRead() && this.reader.peek() != ']') {
/* 144 */         this.reader.skipWhitespace();
/* 146 */         if (this.reader.canRead() && this.reader.peek() == '!') {
/* 147 */           this.reader.skip();
/* 148 */           this.visitor.visitSuggestions(this::suggestComponent);
/* 149 */           DataComponentType<?> componentType = readComponentType(this.reader);
/* 150 */           if (!referenceArraySet.add(componentType))
/* 151 */             throw ItemParser.ERROR_REPEATED_COMPONENT.create(componentType); 
/* 153 */           this.visitor.visitRemovedComponent(componentType);
/* 154 */           this.visitor.visitSuggestions(ItemParser.SUGGEST_NOTHING);
/* 155 */           this.reader.skipWhitespace();
/*     */         } else {
/* 157 */           DataComponentType<?> componentType = readComponentType(this.reader);
/* 158 */           if (!referenceArraySet.add(componentType))
/* 159 */             throw ItemParser.ERROR_REPEATED_COMPONENT.create(componentType); 
/* 162 */           this.visitor.visitSuggestions(this::suggestAssignment);
/* 164 */           this.reader.skipWhitespace();
/* 165 */           this.reader.expect('=');
/* 166 */           this.visitor.visitSuggestions(ItemParser.SUGGEST_NOTHING);
/* 168 */           this.reader.skipWhitespace();
/* 169 */           readComponent(ItemParser.this.tagParser, ItemParser.this.registryOps, componentType);
/* 170 */           this.reader.skipWhitespace();
/*     */         } 
/* 173 */         this.visitor.visitSuggestions(this::suggestNextOrEndComponents);
/* 175 */         if (!this.reader.canRead() || this.reader.peek() != ',')
/*     */           break; 
/* 179 */         this.reader.skip();
/* 180 */         this.reader.skipWhitespace();
/* 181 */         this.visitor.visitSuggestions(this::suggestComponentAssignmentOrRemoval);
/* 183 */         if (!this.reader.canRead())
/* 184 */           throw ItemParser.ERROR_EXPECTED_COMPONENT.createWithContext(this.reader); 
/*     */       } 
/* 188 */       this.reader.expect(']');
/* 189 */       this.visitor.visitSuggestions(ItemParser.SUGGEST_NOTHING);
/*     */     }
/*     */     
/*     */     public static DataComponentType<?> readComponentType(StringReader reader) throws CommandSyntaxException {
/* 193 */       if (!reader.canRead())
/* 194 */         throw ItemParser.ERROR_EXPECTED_COMPONENT.createWithContext(reader); 
/* 197 */       int cursor = reader.getCursor();
/* 198 */       Identifier id = Identifier.read(reader);
/* 199 */       DataComponentType<?> component = (DataComponentType)BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(id);
/* 200 */       if (component == null || component.isTransient()) {
/* 201 */         reader.setCursor(cursor);
/* 202 */         throw ItemParser.ERROR_UNKNOWN_COMPONENT.createWithContext(reader, id);
/*     */       } 
/* 205 */       return component;
/*     */     }
/*     */     
/*     */     private <T, O> void readComponent(TagParser<O> tagParser, RegistryOps<O> registryOps, DataComponentType<T> componentType) throws CommandSyntaxException {
/* 209 */       int cursor = this.reader.getCursor();
/* 210 */       O tag = (O)tagParser.parseAsArgument(this.reader);
/* 211 */       DataResult<T> result = componentType.codecOrThrow().parse((DynamicOps)registryOps, tag);
/* 212 */       this.visitor.visitComponent(componentType, (T)result.getOrThrow(message -> {
/*     */               this.reader.setCursor(cursor);
/*     */               return ItemParser.ERROR_MALFORMED_COMPONENT.createWithContext((ImmutableStringReader)this.reader, componentType.toString(), message);
/*     */             }));
/*     */     }
/*     */     
/*     */     private CompletableFuture<Suggestions> suggestStartComponents(SuggestionsBuilder builder) {
/* 219 */       if (builder.getRemaining().isEmpty())
/* 220 */         builder.suggest(String.valueOf('[')); 
/* 222 */       return builder.buildFuture();
/*     */     }
/*     */     
/*     */     private CompletableFuture<Suggestions> suggestNextOrEndComponents(SuggestionsBuilder builder) {
/* 226 */       if (builder.getRemaining().isEmpty()) {
/* 227 */         builder.suggest(String.valueOf(','));
/* 228 */         builder.suggest(String.valueOf(']'));
/*     */       } 
/* 230 */       return builder.buildFuture();
/*     */     }
/*     */     
/*     */     private CompletableFuture<Suggestions> suggestAssignment(SuggestionsBuilder builder) {
/* 234 */       if (builder.getRemaining().isEmpty())
/* 235 */         builder.suggest(String.valueOf('=')); 
/* 237 */       return builder.buildFuture();
/*     */     }
/*     */     
/*     */     private CompletableFuture<Suggestions> suggestItem(SuggestionsBuilder builder) {
/* 241 */       return SharedSuggestionProvider.suggestResource(ItemParser.this.items.listElementIds().map(ResourceKey::identifier), builder);
/*     */     }
/*     */     
/*     */     private CompletableFuture<Suggestions> suggestComponentAssignmentOrRemoval(SuggestionsBuilder builder) {
/* 245 */       builder.suggest(String.valueOf('!'));
/* 246 */       return suggestComponent(builder, String.valueOf('='));
/*     */     }
/*     */     
/*     */     private CompletableFuture<Suggestions> suggestComponent(SuggestionsBuilder builder) {
/* 250 */       return suggestComponent(builder, "");
/*     */     }
/*     */     
/*     */     private CompletableFuture<Suggestions> suggestComponent(SuggestionsBuilder builder, String suffix) {
/* 254 */       String contents = builder.getRemaining().toLowerCase(Locale.ROOT);
/* 255 */       SharedSuggestionProvider.filterResources(BuiltInRegistries.DATA_COMPONENT_TYPE.entrySet(), contents, entry -> ((ResourceKey)entry.getKey()).identifier(), entry -> {
/*     */             DataComponentType<?> type = (DataComponentType)entry.getValue();
/*     */             if (type.codec() != null) {
/*     */               Identifier id = ((ResourceKey)entry.getKey()).identifier();
/*     */               builder.suggest(String.valueOf(id) + String.valueOf(id));
/*     */             } 
/*     */           });
/* 262 */       return builder.buildFuture();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SuggestionsVisitor implements Visitor {
/*     */     private Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions;
/*     */     
/*     */     private SuggestionsVisitor() {
/* 267 */       this.suggestions = ItemParser.SUGGEST_NOTHING;
/*     */     }
/*     */     
/*     */     public void visitSuggestions(Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions) {
/* 271 */       this.suggestions = suggestions;
/*     */     }
/*     */     
/*     */     public CompletableFuture<Suggestions> resolveSuggestions(SuggestionsBuilder builder, StringReader reader) {
/* 275 */       return this.suggestions.apply(builder.createOffset(reader.getCursor()));
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Visitor {
/*     */     default void visitItem(Holder<Item> item) {}
/*     */     
/*     */     default <T> void visitComponent(DataComponentType<T> type, T value) {}
/*     */     
/*     */     default <T> void visitRemovedComponent(DataComponentType<T> type) {}
/*     */     
/*     */     default void visitSuggestions(Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\item\ItemParser.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */