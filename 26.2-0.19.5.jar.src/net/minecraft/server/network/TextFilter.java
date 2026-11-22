/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ public interface TextFilter {
/*  9 */   public static final TextFilter DUMMY = new TextFilter() {
/*    */       public CompletableFuture<FilteredText> processStreamMessage(String message) {
/* 12 */         return CompletableFuture.completedFuture(FilteredText.passThrough(message));
/*    */       }
/*    */       
/*    */       public CompletableFuture<List<FilteredText>> processMessageBundle(List<String> messages) {
/* 17 */         return CompletableFuture.completedFuture((List<FilteredText>)messages.stream().map(FilteredText::passThrough).collect(ImmutableList.toImmutableList()));
/*    */       }
/*    */     };
/*    */   
/*    */   default void join() {}
/*    */   
/*    */   default void leave() {}
/*    */   
/*    */   CompletableFuture<FilteredText> processStreamMessage(String paramString);
/*    */   
/*    */   CompletableFuture<List<FilteredText>> processMessageBundle(List<String> paramList);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\TextFilter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */