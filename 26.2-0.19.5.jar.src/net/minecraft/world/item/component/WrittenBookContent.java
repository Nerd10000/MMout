/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.ResolutionContext;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.network.Filterable;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ 
/*     */ public final class WrittenBookContent extends Record implements BookContent<Component, WrittenBookContent>, TooltipProvider {
/*     */   private final Filterable<String> title;
/*     */   
/*     */   private final String author;
/*     */   
/*     */   private final int generation;
/*     */   
/*     */   private final List<Filterable<Component>> pages;
/*     */   
/*     */   private final boolean resolved;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/WrittenBookContent;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/WrittenBookContent;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/WrittenBookContent;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/WrittenBookContent;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/WrittenBookContent;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/WrittenBookContent;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Filterable<String> title() {
/*  34 */     return this.title;
/*     */   }
/*     */   
/*     */   public String author() {
/*  34 */     return this.author;
/*     */   }
/*     */   
/*     */   public int generation() {
/*  34 */     return this.generation;
/*     */   }
/*     */   
/*     */   public List<Filterable<Component>> pages() {
/*  34 */     return this.pages;
/*     */   }
/*     */   
/*     */   public boolean resolved() {
/*  34 */     return this.resolved;
/*     */   }
/*     */   
/*  41 */   public static final WrittenBookContent EMPTY = new WrittenBookContent(Filterable.passThrough(""), "", 0, List.of(), true);
/*     */   
/*     */   public static final int PAGE_LENGTH = 32767;
/*     */   
/*     */   public static final int TITLE_LENGTH = 16;
/*     */   
/*     */   public static final int TITLE_MAX_LENGTH = 32;
/*     */   
/*     */   public static final int MAX_GENERATION = 3;
/*     */   
/*     */   private static Codec<Filterable<Component>> pageCodec(Codec<Component> contentCodec) {
/*  50 */     return Filterable.codec(contentCodec);
/*     */   }
/*     */   
/*  53 */   public static final Codec<Component> CONTENT_CODEC = ComponentSerialization.flatRestrictedCodec(32767);
/*     */   
/*  54 */   public static final Codec<List<Filterable<Component>>> PAGES_CODEC = pagesCodec(CONTENT_CODEC);
/*     */   
/*     */   public static final Codec<WrittenBookContent> CODEC;
/*     */   
/*     */   public static Codec<List<Filterable<Component>>> pagesCodec(Codec<Component> contentCodec) {
/*  58 */     return pageCodec(contentCodec).listOf();
/*     */   }
/*     */   
/*     */   static {
/*  61 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Filterable.codec(Codec.string(0, 32)).fieldOf("title").forGetter(WrittenBookContent::title), (App)Codec.STRING.fieldOf("author").forGetter(WrittenBookContent::author), (App)ExtraCodecs.intRange(0, 3).optionalFieldOf("generation", Integer.valueOf(0)).forGetter(WrittenBookContent::generation), (App)PAGES_CODEC.optionalFieldOf("pages", List.of()).forGetter(WrittenBookContent::pages), (App)Codec.BOOL.optionalFieldOf("resolved", Boolean.valueOf(false)).forGetter(WrittenBookContent::resolved)).apply((Applicative)i, WrittenBookContent::new));
/*     */   }
/*     */   
/*  69 */   public static final StreamCodec<RegistryFriendlyByteBuf, WrittenBookContent> STREAM_CODEC = StreamCodec.composite(
/*  70 */       Filterable.streamCodec(ByteBufCodecs.stringUtf8(32)), WrittenBookContent::title, ByteBufCodecs.STRING_UTF8, WrittenBookContent::author, ByteBufCodecs.VAR_INT, WrittenBookContent::generation, 
/*     */       
/*  73 */       Filterable.streamCodec(ComponentSerialization.STREAM_CODEC).apply(ByteBufCodecs.list()), WrittenBookContent::pages, ByteBufCodecs.BOOL, WrittenBookContent::resolved, WrittenBookContent::new);
/*     */   
/*     */   public WrittenBookContent(Filterable<String> title, String author, int generation, List<Filterable<Component>> pages, boolean resolved) {
/*  79 */     if (generation < 0 || generation > 3)
/*  80 */       throw new IllegalArgumentException("Generation was " + generation + ", but must be between 0 and 3"); 
/*     */     this.title = title;
/*     */     this.author = author;
/*     */     this.generation = generation;
/*     */     this.pages = pages;
/*     */     this.resolved = resolved;
/*     */   }
/*     */   
/*     */   public WrittenBookContent craftCopy() {
/*  85 */     return new WrittenBookContent(this.title, this.author, this.generation + 1, this.pages, this.resolved);
/*     */   }
/*     */   
/*     */   public static boolean resolveForItem(ItemStack itemStack, ResolutionContext context, HolderLookup.Provider registries) {
/*  89 */     WrittenBookContent content = (WrittenBookContent)itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT);
/*  90 */     if (content != null && !content.resolved()) {
/*  91 */       WrittenBookContent resolvedContent = content.resolve(context, registries);
/*  92 */       if (resolvedContent != null) {
/*  93 */         itemStack.set(DataComponents.WRITTEN_BOOK_CONTENT, resolvedContent);
/*  94 */         return true;
/*     */       } 
/*  96 */       itemStack.set(DataComponents.WRITTEN_BOOK_CONTENT, content.markResolved());
/*     */     } 
/*  99 */     return false;
/*     */   }
/*     */   
/*     */   public WrittenBookContent resolve(ResolutionContext context, HolderLookup.Provider registries) {
/* 103 */     if (this.resolved)
/* 104 */       return null; 
/* 107 */     ImmutableList.Builder<Filterable<Component>> newPages = ImmutableList.builderWithExpectedSize(this.pages.size());
/* 108 */     for (Filterable<Component> page : this.pages) {
/* 109 */       Optional<Filterable<Component>> resolvedPage = resolvePage(context, registries, page);
/* 110 */       if (resolvedPage.isEmpty())
/* 111 */         return null; 
/* 113 */       newPages.add(resolvedPage.get());
/*     */     } 
/* 116 */     return new WrittenBookContent(this.title, this.author, this.generation, (List<Filterable<Component>>)newPages.build(), true);
/*     */   }
/*     */   
/*     */   public WrittenBookContent markResolved() {
/* 120 */     return new WrittenBookContent(this.title, this.author, this.generation, this.pages, true);
/*     */   }
/*     */   
/*     */   private static Optional<Filterable<Component>> resolvePage(ResolutionContext context, HolderLookup.Provider registries, Filterable<Component> page) {
/* 124 */     return page.resolve(component -> {
/*     */           try {
/*     */             MutableComponent mutableComponent = ComponentUtils.resolve(context, component);
/*     */             return isPageTooLarge((Component)mutableComponent, registries) ? Optional.empty() : Optional.of(mutableComponent);
/* 131 */           } catch (Exception ignored) {
/*     */             return Optional.of(component);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static boolean isPageTooLarge(Component page, HolderLookup.Provider registries) {
/* 138 */     DataResult<JsonElement> json = ComponentSerialization.CODEC.encodeStart((DynamicOps)registries.createSerializationContext((DynamicOps)JsonOps.INSTANCE), page);
/* 139 */     return (json.isSuccess() && GsonHelper.encodesLongerThan((JsonElement)json.getOrThrow(), 32767));
/*     */   }
/*     */   
/*     */   public List<Component> getPages(boolean filterEnabled) {
/* 143 */     return Lists.transform(this.pages, page -> (Component)page.get(filterEnabled));
/*     */   }
/*     */   
/*     */   public WrittenBookContent withReplacedPages(List<Filterable<Component>> newPages) {
/* 149 */     return new WrittenBookContent(this.title, this.author, this.generation, newPages, false);
/*     */   }
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 154 */     if (!StringUtil.isBlank(this.author))
/* 155 */       consumer.accept(Component.translatable("book.byAuthor", new Object[] { this.author }).withStyle(ChatFormatting.GRAY)); 
/* 157 */     consumer.accept(Component.translatable("book.generation." + this.generation).withStyle(ChatFormatting.GRAY));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\WrittenBookContent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */