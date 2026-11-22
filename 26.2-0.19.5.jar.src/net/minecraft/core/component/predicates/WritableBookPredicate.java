/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.advancements.predicates.CollectionPredicate;
/*    */ import net.minecraft.advancements.predicates.SingleComponentItemPredicate;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ import net.minecraft.world.item.component.WritableBookContent;
/*    */ 
/*    */ public final class WritableBookPredicate extends Record implements SingleComponentItemPredicate<WritableBookContent> {
/*    */   private final Optional<CollectionPredicate<Filterable<String>, PagePredicate>> pages;
/*    */   
/*    */   public static final Codec<WritableBookPredicate> CODEC;
/*    */   
/*    */   public WritableBookPredicate(Optional<CollectionPredicate<Filterable<String>, PagePredicate>> pages) {
/* 15 */     this.pages = pages;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/WritableBookPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/WritableBookPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/WritableBookPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<CollectionPredicate<Filterable<String>, PagePredicate>> pages() {
/* 15 */     return this.pages;
/*    */   }
/*    */   
/*    */   public static final class PagePredicate extends Record implements Predicate<Filterable<String>> {
/*    */     private final String contents;
/*    */     
/*    */     public PagePredicate(String contents) {
/* 16 */       this.contents = contents;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public String contents() {
/* 16 */       return this.contents;
/*    */     }
/*    */     
/* 17 */     public static final Codec<PagePredicate> CODEC = Codec.STRING.xmap(PagePredicate::new, PagePredicate::contents);
/*    */     
/*    */     public boolean test(Filterable<String> value) {
/* 22 */       return ((String)value.raw()).equals(this.contents);
/*    */     }
/*    */   }
/*    */   
/*    */   static {
/* 26 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)CollectionPredicate.codec(PagePredicate.CODEC).optionalFieldOf("pages").forGetter(WritableBookPredicate::pages)).apply((Applicative)i, WritableBookPredicate::new));
/*    */   }
/*    */   
/*    */   public DataComponentType<WritableBookContent> componentType() {
/* 32 */     return DataComponents.WRITABLE_BOOK_CONTENT;
/*    */   }
/*    */   
/*    */   public boolean matches(WritableBookContent value) {
/* 37 */     if (this.pages.isPresent() && !((CollectionPredicate)this.pages.get()).test(value.pages()))
/* 38 */       return false; 
/* 41 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\WritableBookPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */