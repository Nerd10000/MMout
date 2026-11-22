/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ 
/*    */ public final class PagePredicate extends Record implements Predicate<Filterable<String>> {
/*    */   private final String contents;
/*    */   
/*    */   public PagePredicate(String contents) {
/* 16 */     this.contents = contents;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String contents() {
/* 16 */     return this.contents;
/*    */   }
/*    */   
/* 17 */   public static final Codec<PagePredicate> CODEC = Codec.STRING.xmap(PagePredicate::new, PagePredicate::contents);
/*    */   
/*    */   public boolean test(Filterable<String> value) {
/* 22 */     return ((String)value.raw()).equals(this.contents);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\WritableBookPredicate$PagePredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */