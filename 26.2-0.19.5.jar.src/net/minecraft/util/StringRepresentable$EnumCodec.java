/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class EnumCodec<E extends Enum<E> & StringRepresentable> extends StringRepresentable.StringRepresentableCodec<E> {
/*    */   private final Function<String, E> resolver;
/*    */   
/*    */   public EnumCodec(E[] valueArray, Function<String, E> nameResolver) {
/* 49 */     super(valueArray, nameResolver, rec$ -> rec$.ordinal());
/* 50 */     this.resolver = nameResolver;
/*    */   }
/*    */   
/*    */   public E byName(String name) {
/* 54 */     return this.resolver.apply(name);
/*    */   }
/*    */   
/*    */   public E byName(String name, E _default) {
/* 58 */     return (E)Objects.<Enum>requireNonNullElse((Enum)byName(name), (Enum)_default);
/*    */   }
/*    */   
/*    */   public E byName(String name, Supplier<? extends E> defaultSupplier) {
/* 62 */     return (E)Objects.<Enum>requireNonNullElseGet((Enum)byName(name), defaultSupplier);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\StringRepresentable$EnumCodec.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */