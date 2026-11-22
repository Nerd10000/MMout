/*    */ package net.minecraft.client.searchtree;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface SearchTree<T> {
/*    */   static <T> SearchTree<T> empty() {
/* 11 */     return text -> List.of();
/*    */   }
/*    */   
/*    */   static <T> SearchTree<T> plainText(List<T> elements, Function<T, Stream<String>> idGetter) {
/* 15 */     if (elements.isEmpty())
/* 16 */       return empty(); 
/* 19 */     SuffixArray<T> tree = new SuffixArray<>();
/* 20 */     for (T element : elements)
/* 21 */       ((Stream)idGetter.apply(element)).forEach(elementId -> tree.add(element, elementId.toLowerCase(Locale.ROOT))); 
/* 23 */     tree.generate();
/* 25 */     Objects.requireNonNull(tree);
/* 25 */     return tree::search;
/*    */   }
/*    */   
/*    */   List<T> search(String paramString);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\searchtree\SearchTree.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */