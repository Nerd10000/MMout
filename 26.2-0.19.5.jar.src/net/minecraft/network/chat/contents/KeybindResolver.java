/*    */ package net.minecraft.network.chat.contents;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class KeybindResolver {
/*    */   static Function<String, Supplier<Component>> keyResolver = name -> ();
/*    */   
/*    */   public static void setKeyResolver(Function<String, Supplier<Component>> resolver) {
/* 14 */     keyResolver = resolver;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\KeybindResolver.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */