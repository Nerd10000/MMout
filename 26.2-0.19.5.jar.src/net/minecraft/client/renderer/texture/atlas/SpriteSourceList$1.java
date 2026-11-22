/*    */ package net.minecraft.client.renderer.texture.atlas;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ class null implements SpriteSource.Output {
/*    */   null(SpriteSourceList this$0) {}
/*    */   
/*    */   public void add(Identifier id, SpriteSource.DiscardableLoader sprite) {
/* 40 */     SpriteSource.DiscardableLoader previous = sprites.put(id, sprite);
/* 41 */     if (previous != null)
/* 42 */       previous.discard(); 
/*    */   }
/*    */   
/*    */   public void removeAll(Predicate<Identifier> predicate) {
/* 48 */     Iterator<Map.Entry<Identifier, SpriteSource.DiscardableLoader>> it = sprites.entrySet().iterator();
/* 49 */     while (it.hasNext()) {
/* 50 */       Map.Entry<Identifier, SpriteSource.DiscardableLoader> entry = it.next();
/* 51 */       if (predicate.test(entry.getKey())) {
/* 52 */         ((SpriteSource.DiscardableLoader)entry.getValue()).discard();
/* 53 */         it.remove();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\atlas\SpriteSourceList$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */