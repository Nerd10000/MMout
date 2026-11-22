/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*     */ import net.minecraft.network.chat.contents.KeybindContents;
/*     */ import net.minecraft.network.chat.contents.NbtContents;
/*     */ import net.minecraft.network.chat.contents.ObjectContents;
/*     */ import net.minecraft.network.chat.contents.PlainTextContents;
/*     */ import net.minecraft.network.chat.contents.ScoreContents;
/*     */ import net.minecraft.network.chat.contents.SelectorContents;
/*     */ import net.minecraft.network.chat.contents.TranslatableContents;
/*     */ import net.minecraft.network.chat.contents.data.DataSource;
/*     */ import net.minecraft.network.chat.contents.objects.ObjectInfo;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.CompilableString;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public interface Component extends Message, FormattedText {
/*     */   default String getString() {
/*  38 */     return super.getString();
/*     */   }
/*     */   
/*     */   default String getString(int limit) {
/*  42 */     StringBuilder builder = new StringBuilder();
/*  43 */     visit(contents -> {
/*     */           int remaining = limit - builder.length();
/*     */           if (remaining <= 0)
/*     */             return STOP_ITERATION; 
/*     */           builder.append((contents.length() <= remaining) ? contents : contents.substring(0, remaining));
/*     */           return Optional.empty();
/*     */         });
/*  51 */     return builder.toString();
/*     */   }
/*     */   
/*     */   default String tryCollapseToString() {
/*  57 */     ComponentContents componentContents = getContents();
/*  57 */     if (componentContents instanceof PlainTextContents) {
/*  57 */       PlainTextContents text = (PlainTextContents)componentContents;
/*  57 */       if (getSiblings().isEmpty() && getStyle().isEmpty())
/*  58 */         return text.text(); 
/*     */     } 
/*  60 */     return null;
/*     */   }
/*     */   
/*     */   default MutableComponent plainCopy() {
/*  68 */     return MutableComponent.create(getContents());
/*     */   }
/*     */   
/*     */   default MutableComponent copy() {
/*  76 */     return new MutableComponent(getContents(), new ArrayList<>(getSiblings()), getStyle());
/*     */   }
/*     */   
/*     */   default <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style parentStyle) {
/*  83 */     Style selfStyle = getStyle().applyTo(parentStyle);
/*  85 */     Optional<T> selfResult = getContents().visit(output, selfStyle);
/*  86 */     if (selfResult.isPresent())
/*  87 */       return selfResult; 
/*  90 */     for (Component sibling : getSiblings()) {
/*  91 */       Optional<T> result = sibling.visit(output, selfStyle);
/*  92 */       if (result.isPresent())
/*  93 */         return result; 
/*     */     } 
/*  97 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   default <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 102 */     Optional<T> selfResult = getContents().visit(output);
/* 103 */     if (selfResult.isPresent())
/* 104 */       return selfResult; 
/* 107 */     for (Component sibling : getSiblings()) {
/* 108 */       Optional<T> result = sibling.visit(output);
/* 109 */       if (result.isPresent())
/* 110 */         return result; 
/*     */     } 
/* 114 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   default List<Component> toFlatList() {
/* 118 */     return toFlatList(Style.EMPTY);
/*     */   }
/*     */   
/*     */   default List<Component> toFlatList(Style rootStyle) {
/* 122 */     List<Component> result = Lists.newArrayList();
/* 123 */     visit((style, contents) -> {
/*     */           if (!contents.isEmpty())
/*     */             result.add(literal(contents).withStyle(style)); 
/*     */           return Optional.empty();
/* 123 */         }rootStyle);
/* 129 */     return result;
/*     */   }
/*     */   
/*     */   default boolean contains(Component other) {
/* 133 */     if (equals(other))
/* 134 */       return true; 
/* 137 */     List<Component> flat = toFlatList();
/* 138 */     List<Component> otherFlat = other.toFlatList(getStyle());
/* 139 */     return (Collections.indexOfSubList(flat, otherFlat) != -1);
/*     */   }
/*     */   
/*     */   static Component nullToEmpty(String text) {
/* 143 */     return (text != null) ? literal(text) : CommonComponents.EMPTY;
/*     */   }
/*     */   
/*     */   static MutableComponent literal(String text) {
/* 147 */     return MutableComponent.create((ComponentContents)PlainTextContents.create(text));
/*     */   }
/*     */   
/*     */   static MutableComponent translatable(String key) {
/* 151 */     return MutableComponent.create((ComponentContents)new TranslatableContents(key, null, TranslatableContents.NO_ARGS));
/*     */   }
/*     */   
/*     */   static MutableComponent translatable(String key, Object... args) {
/* 156 */     return MutableComponent.create((ComponentContents)new TranslatableContents(key, null, args));
/*     */   }
/*     */   
/*     */   static MutableComponent translatableEscape(String key, Object... args) {
/* 162 */     for (int i = 0; i < args.length; i++) {
/* 163 */       Object arg = args[i];
/* 164 */       if (!TranslatableContents.isAllowedPrimitiveArgument(arg) && !(arg instanceof Component))
/* 165 */         args[i] = String.valueOf(arg); 
/*     */     } 
/* 168 */     return translatable(key, args);
/*     */   }
/*     */   
/*     */   static MutableComponent translatableWithFallback(String key, String fallback) {
/* 172 */     return MutableComponent.create((ComponentContents)new TranslatableContents(key, fallback, TranslatableContents.NO_ARGS));
/*     */   }
/*     */   
/*     */   static MutableComponent translatableWithFallback(String key, String fallback, Object... args) {
/* 176 */     return MutableComponent.create((ComponentContents)new TranslatableContents(key, fallback, args));
/*     */   }
/*     */   
/*     */   static MutableComponent empty() {
/* 180 */     return MutableComponent.create((ComponentContents)PlainTextContents.EMPTY);
/*     */   }
/*     */   
/*     */   static MutableComponent keybind(String name) {
/* 184 */     return MutableComponent.create((ComponentContents)new KeybindContents(name));
/*     */   }
/*     */   
/*     */   static MutableComponent nbt(CompilableString<NbtPathArgument.NbtPath> nbtPath, boolean interpreting, boolean plain, Optional<Component> separator, DataSource dataSource) {
/* 188 */     return MutableComponent.create((ComponentContents)new NbtContents(nbtPath, interpreting, plain, separator, dataSource));
/*     */   }
/*     */   
/*     */   static MutableComponent score(CompilableString<EntitySelector> pattern, String objective) {
/* 192 */     return MutableComponent.create((ComponentContents)new ScoreContents(Either.left(pattern), objective));
/*     */   }
/*     */   
/*     */   static MutableComponent score(String name, String objective) {
/* 196 */     return MutableComponent.create((ComponentContents)new ScoreContents(Either.right(name), objective));
/*     */   }
/*     */   
/*     */   static MutableComponent selector(CompilableString<EntitySelector> pattern, Optional<Component> separator) {
/* 200 */     return MutableComponent.create((ComponentContents)new SelectorContents(pattern, separator));
/*     */   }
/*     */   
/*     */   static MutableComponent object(ObjectInfo info) {
/* 204 */     return MutableComponent.create((ComponentContents)new ObjectContents(info, Optional.empty()));
/*     */   }
/*     */   
/*     */   static MutableComponent object(ObjectInfo info, Component fallback) {
/* 208 */     return MutableComponent.create((ComponentContents)new ObjectContents(info, Optional.of(fallback)));
/*     */   }
/*     */   
/*     */   static Component translationArg(Date date) {
/* 213 */     return literal(date.toString());
/*     */   }
/*     */   
/*     */   static Component translationArg(Message message) {
/* 217 */     Component component = (Component)message;
/* 217 */     return (message instanceof Component) ? component : literal(message.getString());
/*     */   }
/*     */   
/*     */   static Component translationArg(UUID uuid) {
/* 221 */     return literal(uuid.toString());
/*     */   }
/*     */   
/*     */   static Component translationArg(Identifier id) {
/* 225 */     return literal(id.toString());
/*     */   }
/*     */   
/*     */   static Component translationArg(ChunkPos chunkPos) {
/* 229 */     return literal(chunkPos.toString());
/*     */   }
/*     */   
/*     */   static Component translationArg(URI uri) {
/* 233 */     return literal(uri.toString());
/*     */   }
/*     */   
/*     */   Style getStyle();
/*     */   
/*     */   ComponentContents getContents();
/*     */   
/*     */   List<Component> getSiblings();
/*     */   
/*     */   FormattedCharSequence getVisualOrderText();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\Component.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */