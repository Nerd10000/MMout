/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.block.state.StateHolder;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ final class ExactMatcher extends Record implements StatePropertiesPredicate.ValueMatcher {
/*    */   private final String value;
/*    */   
/*    */   private ExactMatcher(String value) {
/* 76 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/StatePropertiesPredicate$ExactMatcher;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #76	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/StatePropertiesPredicate$ExactMatcher;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/StatePropertiesPredicate$ExactMatcher;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #76	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/StatePropertiesPredicate$ExactMatcher;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/StatePropertiesPredicate$ExactMatcher;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #76	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/StatePropertiesPredicate$ExactMatcher;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String value() {
/* 76 */     return this.value;
/*    */   }
/*    */   
/* 77 */   public static final Codec<ExactMatcher> CODEC = Codec.STRING.xmap(ExactMatcher::new, ExactMatcher::value);
/*    */   
/* 78 */   public static final StreamCodec<ByteBuf, ExactMatcher> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(ExactMatcher::new, ExactMatcher::value);
/*    */   
/*    */   public <T extends Comparable<T>> boolean match(StateHolder<?, ?> state, Property<T> property) {
/* 82 */     Comparable<Comparable> comparable = state.getValue(property);
/* 83 */     Optional<T> typedExpected = property.getValue(this.value);
/* 84 */     return (typedExpected.isPresent() && comparable.compareTo((Comparable)typedExpected.get()) == 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\StatePropertiesPredicate$ExactMatcher.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */