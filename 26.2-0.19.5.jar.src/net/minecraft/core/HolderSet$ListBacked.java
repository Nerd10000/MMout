/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Spliterator;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public abstract class ListBacked<T> implements HolderSet<T> {
/*    */   protected abstract List<Holder<T>> contents();
/*    */   
/*    */   public int size() {
/* 44 */     return contents().size();
/*    */   }
/*    */   
/*    */   public Spliterator<Holder<T>> spliterator() {
/* 49 */     return contents().spliterator();
/*    */   }
/*    */   
/*    */   public Iterator<Holder<T>> iterator() {
/* 54 */     return contents().iterator();
/*    */   }
/*    */   
/*    */   public Stream<Holder<T>> stream() {
/* 59 */     return contents().stream();
/*    */   }
/*    */   
/*    */   public Optional<Holder<T>> getRandomElement(RandomSource random) {
/* 64 */     return Util.getRandomSafe(contents(), random);
/*    */   }
/*    */   
/*    */   public Holder<T> get(int index) {
/* 69 */     return contents().get(index);
/*    */   }
/*    */   
/*    */   public boolean canSerializeIn(HolderOwner<T> owner) {
/* 74 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\HolderSet$ListBacked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */