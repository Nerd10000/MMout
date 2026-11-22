/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
/*    */ 
/*    */ public final class TagPredicate<T> extends Record {
/*    */   private final TagKey<T> tag;
/*    */   
/*    */   private final boolean expected;
/*    */   
/*    */   public TagPredicate(TagKey<T> tag, boolean expected) {
/* 10 */     this.tag = tag;
/* 10 */     this.expected = expected;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/TagPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/TagPredicate;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/TagPredicate<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/TagPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/TagPredicate;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/TagPredicate<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/TagPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/TagPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/TagPredicate<TT;>;
/*    */   }
/*    */   
/*    */   public TagKey<T> tag() {
/* 10 */     return this.tag;
/*    */   }
/*    */   
/*    */   public boolean expected() {
/* 10 */     return this.expected;
/*    */   }
/*    */   
/*    */   public static <T> Codec<TagPredicate<T>> codec(ResourceKey<? extends Registry<T>> registryKey) {
/* 12 */     return RecordCodecBuilder.create(i -> i.group((App)TagKey.codec(registryKey).fieldOf("id").forGetter(TagPredicate::tag), (App)Codec.BOOL.fieldOf("expected").forGetter(TagPredicate::expected)).apply((Applicative)i, TagPredicate::new));
/*    */   }
/*    */   
/*    */   public static <T> TagPredicate<T> is(TagKey<T> tag) {
/* 19 */     return new TagPredicate<>(tag, true);
/*    */   }
/*    */   
/*    */   public static <T> TagPredicate<T> isNot(TagKey<T> tag) {
/* 23 */     return new TagPredicate<>(tag, false);
/*    */   }
/*    */   
/*    */   public boolean matches(Holder<T> holder) {
/* 27 */     return (holder.is(this.tag) == this.expected);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\TagPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */