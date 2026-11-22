/*    */ package net.minecraft;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class Optionull {
/*    */   @Deprecated
/*    */   public static <T> T orElse(T t, T defaultValue) {
/* 15 */     return Objects.requireNonNullElse(t, defaultValue);
/*    */   }
/*    */   
/*    */   public static <T, R> R map(T t, Function<T, R> map) {
/* 19 */     return (t == null) ? null : map.apply(t);
/*    */   }
/*    */   
/*    */   public static <T, R> R mapOrDefault(T t, Function<T, R> map, R defaultValue) {
/* 23 */     return (t == null) ? defaultValue : map.apply(t);
/*    */   }
/*    */   
/*    */   public static <T, R> R mapOrElse(T t, Function<T, R> map, Supplier<R> elseSupplier) {
/* 27 */     return (t == null) ? elseSupplier.get() : map.apply(t);
/*    */   }
/*    */   
/*    */   public static <T> T first(Collection<T> collection) {
/* 31 */     Iterator<T> iterator = collection.iterator();
/* 32 */     return iterator.hasNext() ? iterator.next() : null;
/*    */   }
/*    */   
/*    */   public static <T> T firstOrDefault(Collection<T> collection, T defaultValue) {
/* 36 */     Iterator<T> iterator = collection.iterator();
/* 37 */     return iterator.hasNext() ? iterator.next() : defaultValue;
/*    */   }
/*    */   
/*    */   public static <T> T firstOrElse(Collection<T> collection, Supplier<T> elseSupplier) {
/* 41 */     Iterator<T> iterator = collection.iterator();
/* 42 */     return iterator.hasNext() ? iterator.next() : elseSupplier.get();
/*    */   }
/*    */   
/*    */   public static <T> boolean isNullOrEmpty(T[] t) {
/* 46 */     return (t == null || t.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(boolean[] t) {
/* 50 */     return (t == null || t.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(byte[] t) {
/* 54 */     return (t == null || t.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(char[] t) {
/* 58 */     return (t == null || t.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(short[] t) {
/* 62 */     return (t == null || t.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(int[] t) {
/* 66 */     return (t == null || t.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(long[] t) {
/* 70 */     return (t == null || t.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(float[] t) {
/* 74 */     return (t == null || t.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(double[] t) {
/* 78 */     return (t == null || t.length == 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\Optionull.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */