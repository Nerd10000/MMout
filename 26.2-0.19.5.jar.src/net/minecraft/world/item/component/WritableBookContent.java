/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ 
/*    */ public final class WritableBookContent extends Record implements BookContent<String, WritableBookContent> {
/*    */   private final List<Filterable<String>> pages;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/WritableBookContent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/WritableBookContent;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/WritableBookContent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/WritableBookContent;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/WritableBookContent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/WritableBookContent;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<Filterable<String>> pages() {
/* 14 */     return this.pages;
/*    */   }
/*    */   
/* 15 */   public static final WritableBookContent EMPTY = new WritableBookContent(List.of());
/*    */   
/*    */   public static final int PAGE_EDIT_LENGTH = 1024;
/*    */   
/*    */   public static final int MAX_PAGES = 100;
/*    */   
/* 19 */   private static final Codec<Filterable<String>> PAGE_CODEC = Filterable.codec(Codec.string(0, 1024));
/*    */   
/* 20 */   public static final Codec<List<Filterable<String>>> PAGES_CODEC = PAGE_CODEC.sizeLimitedListOf(100);
/*    */   
/*    */   public static final Codec<WritableBookContent> CODEC;
/*    */   
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)PAGES_CODEC.optionalFieldOf("pages", List.of()).forGetter(WritableBookContent::pages)).apply((Applicative)i, WritableBookContent::new));
/*    */   }
/*    */   
/* 26 */   public static final StreamCodec<ByteBuf, WritableBookContent> STREAM_CODEC = Filterable.streamCodec(ByteBufCodecs.stringUtf8(1024))
/* 27 */     .apply(ByteBufCodecs.list(100))
/* 28 */     .map(WritableBookContent::new, WritableBookContent::pages);
/*    */   
/*    */   public WritableBookContent(List<Filterable<String>> pages) {
/* 31 */     if (pages.size() > 100)
/* 32 */       throw new IllegalArgumentException("Got " + pages.size() + " pages, but maximum is 100"); 
/*    */     this.pages = pages;
/*    */   }
/*    */   
/*    */   public Stream<String> getPages(boolean filterEnabled) {
/* 37 */     return this.pages.stream().map(page -> (String)page.get(filterEnabled));
/*    */   }
/*    */   
/*    */   public WritableBookContent withReplacedPages(List<Filterable<String>> newPages) {
/* 42 */     return new WritableBookContent(newPages);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\WritableBookContent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */