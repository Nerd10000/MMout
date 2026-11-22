/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Streams;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class BlockEntitySignDoubleSidedEditableTextFix extends NamedEntityWriteReadFix {
/* 13 */   public static final List<String> FIELDS_TO_DROP = List.of("Text1", "Text2", "Text3", "Text4", "FilteredText1", "FilteredText2", "FilteredText3", "FilteredText4", "Color", "GlowingText");
/*    */   
/*    */   public static final String FILTERED_CORRECT = "_filtered_correct";
/*    */   
/*    */   private static final String DEFAULT_COLOR = "black";
/*    */   
/*    */   public BlockEntitySignDoubleSidedEditableTextFix(Schema outputSchema, String name, String entityName) {
/* 23 */     super(outputSchema, true, name, References.BLOCK_ENTITY, entityName);
/*    */   }
/*    */   
/*    */   protected <T> Dynamic<T> fix(Dynamic<T> input) {
/* 32 */     input = input.set("front_text", fixFrontTextTag(input)).set("back_text", createDefaultText(input)).set("is_waxed", input.createBoolean(false)).set("_filtered_correct", input.createBoolean(true));
/* 33 */     for (String field : FIELDS_TO_DROP)
/* 34 */       input = input.remove(field); 
/* 36 */     return input;
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> fixFrontTextTag(Dynamic<T> tag) {
/* 40 */     Dynamic<T> emptyLine = LegacyComponentDataFixUtils.createEmptyComponent(tag.getOps());
/* 41 */     List<Dynamic<T>> lines = getLines(tag, "Text").map(line -> (Dynamic)line.orElse(emptyLine)).toList();
/* 46 */     Dynamic<T> text = tag.emptyMap().set("messages", tag.createList(lines.stream())).set("color", tag.get("Color").result().orElse(tag.createString("black"))).set("has_glowing_text", tag.get("GlowingText").result().orElse(tag.createBoolean(false)));
/* 48 */     List<Optional<Dynamic<T>>> filteredLines = getLines(tag, "FilteredText").toList();
/* 49 */     if (filteredLines.stream().anyMatch(Optional::isPresent))
/* 50 */       text = text.set("filtered_messages", tag.createList(Streams.mapWithIndex(filteredLines.stream(), (line, index) -> {
/*    */                 Dynamic<T> fallbackLine = lines.get((int)index);
/*    */                 return line.orElse(fallbackLine);
/*    */               }))); 
/* 56 */     return text;
/*    */   }
/*    */   
/*    */   private static <T> Stream<Optional<Dynamic<T>>> getLines(Dynamic<T> tag, String linePrefix) {
/* 60 */     return Stream.of((Optional<Dynamic<T>>[])new Optional[] { tag
/* 61 */           .get(linePrefix + "1").result(), tag
/* 62 */           .get(linePrefix + "2").result(), tag
/* 63 */           .get(linePrefix + "3").result(), tag
/* 64 */           .get(linePrefix + "4").result() });
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> createDefaultText(Dynamic<T> tag) {
/* 69 */     return tag.emptyMap()
/* 70 */       .set("messages", createEmptyLines(tag))
/* 71 */       .set("color", tag.createString("black"))
/* 72 */       .set("has_glowing_text", tag.createBoolean(false));
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> createEmptyLines(Dynamic<T> tag) {
/* 76 */     Dynamic<T> emptyComponent = LegacyComponentDataFixUtils.createEmptyComponent(tag.getOps());
/* 77 */     return tag.createList(Stream.of(new Dynamic[] { emptyComponent, emptyComponent, emptyComponent, emptyComponent }));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\BlockEntitySignDoubleSidedEditableTextFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */