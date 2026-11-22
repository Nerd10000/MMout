/*    */ package net.minecraft.server.packs.repository;
/*    */ 
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ class null implements PackSource {
/*    */   public Component decorate(Component packDescription) {
/* 28 */     return decorator.apply(packDescription);
/*    */   }
/*    */   
/*    */   public boolean shouldAddAutomatically() {
/* 33 */     return addAutomatically;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\repository\PackSource$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */