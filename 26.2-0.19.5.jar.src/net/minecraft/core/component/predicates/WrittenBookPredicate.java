/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.advancements.predicates.CollectionPredicate;
/*    */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*    */ import net.minecraft.advancements.predicates.SingleComponentItemPredicate;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ import net.minecraft.world.item.component.WrittenBookContent;
/*    */ 
/*    */ public final class WrittenBookPredicate extends Record implements SingleComponentItemPredicate<WrittenBookContent> {
/*    */   private final Optional<CollectionPredicate<Filterable<Component>, PagePredicate>> pages;
/*    */   
/*    */   private final Optional<String> author;
/*    */   
/*    */   private final Optional<String> title;
/*    */   
/*    */   private final MinMaxBounds.Ints generation;
/*    */   
/*    */   private final Optional<Boolean> resolved;
/*    */   
/*    */   public static final Codec<WrittenBookPredicate> CODEC;
/*    */   
/*    */   public WrittenBookPredicate(Optional<CollectionPredicate<Filterable<Component>, PagePredicate>> pages, Optional<String> author, Optional<String> title, MinMaxBounds.Ints generation, Optional<Boolean> resolved) {
/* 18 */     this.pages = pages;
/* 18 */     this.author = author;
/* 18 */     this.title = title;
/* 18 */     this.generation = generation;
/* 18 */     this.resolved = resolved;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<CollectionPredicate<Filterable<Component>, PagePredicate>> pages() {
/* 18 */     return this.pages;
/*    */   }
/*    */   
/*    */   public Optional<String> author() {
/* 18 */     return this.author;
/*    */   }
/*    */   
/*    */   public Optional<String> title() {
/* 18 */     return this.title;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Ints generation() {
/* 18 */     return this.generation;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> resolved() {
/* 18 */     return this.resolved;
/*    */   }
/*    */   
/*    */   public static final class PagePredicate extends Record implements Predicate<Filterable<Component>> {
/*    */     private final Component contents;
/*    */     
/*    */     public PagePredicate(Component contents) {
/* 25 */       this.contents = contents;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Component contents() {
/* 25 */       return this.contents;
/*    */     }
/*    */     
/* 26 */     public static final Codec<PagePredicate> CODEC = ComponentSerialization.CODEC.xmap(PagePredicate::new, PagePredicate::contents);
/*    */     
/*    */     public boolean test(Filterable<Component> value) {
/* 31 */       return ((Component)value.raw()).equals(this.contents);
/*    */     }
/*    */   }
/*    */   
/*    */   static {
/* 35 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)CollectionPredicate.codec(PagePredicate.CODEC).optionalFieldOf("pages").forGetter(WrittenBookPredicate::pages), (App)Codec.STRING.optionalFieldOf("author").forGetter(WrittenBookPredicate::author), (App)Codec.STRING.optionalFieldOf("title").forGetter(WrittenBookPredicate::title), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("generation", MinMaxBounds.Ints.ANY).forGetter(WrittenBookPredicate::generation), (App)Codec.BOOL.optionalFieldOf("resolved").forGetter(WrittenBookPredicate::resolved)).apply((Applicative)i, WrittenBookPredicate::new));
/*    */   }
/*    */   
/*    */   public DataComponentType<WrittenBookContent> componentType() {
/* 45 */     return DataComponents.WRITTEN_BOOK_CONTENT;
/*    */   }
/*    */   
/*    */   public boolean matches(WrittenBookContent value) {
/* 50 */     if (this.author.isPresent() && !((String)this.author.get()).equals(value.author()))
/* 51 */       return false; 
/* 54 */     if (this.title.isPresent() && !((String)this.title.get()).equals(value.title().raw()))
/* 55 */       return false; 
/* 58 */     if (!this.generation.matches(value.generation()))
/* 59 */       return false; 
/* 62 */     if (this.resolved.isPresent() && ((Boolean)this.resolved.get()).booleanValue() != value.resolved())
/* 63 */       return false; 
/* 66 */     if (this.pages.isPresent() && !((CollectionPredicate)this.pages.get()).test(value.pages()))
/* 67 */       return false; 
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\WrittenBookPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */