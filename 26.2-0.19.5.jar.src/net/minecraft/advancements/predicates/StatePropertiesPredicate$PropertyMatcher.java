/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.StateHolder;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ final class PropertyMatcher extends Record {
/*    */   private final String name;
/*    */   
/*    */   private final StatePropertiesPredicate.ValueMatcher valueMatcher;
/*    */   
/*    */   private PropertyMatcher(String name, StatePropertiesPredicate.ValueMatcher valueMatcher) {
/* 31 */     this.name = name;
/* 31 */     this.valueMatcher = valueMatcher;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/StatePropertiesPredicate$PropertyMatcher;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/StatePropertiesPredicate$PropertyMatcher;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/StatePropertiesPredicate$PropertyMatcher;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/StatePropertiesPredicate$PropertyMatcher;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/StatePropertiesPredicate$PropertyMatcher;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/StatePropertiesPredicate$PropertyMatcher;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String name() {
/* 31 */     return this.name;
/*    */   }
/*    */   
/*    */   public StatePropertiesPredicate.ValueMatcher valueMatcher() {
/* 31 */     return this.valueMatcher;
/*    */   }
/*    */   
/* 32 */   public static final StreamCodec<ByteBuf, PropertyMatcher> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, PropertyMatcher::name, StatePropertiesPredicate.ValueMatcher.STREAM_CODEC, PropertyMatcher::valueMatcher, PropertyMatcher::new);
/*    */   
/*    */   public <S extends StateHolder<?, S>> boolean match(StateDefinition<?, S> definition, S state) {
/* 39 */     Property<?> property = definition.getProperty(this.name);
/* 40 */     return (property != null && this.valueMatcher.match((StateHolder<?, ?>)state, property));
/*    */   }
/*    */   
/*    */   public Optional<String> checkState(StateDefinition<?, ?> states) {
/* 44 */     Property<?> property = states.getProperty(this.name);
/* 45 */     return (property != null) ? Optional.<String>empty() : Optional.<String>of(this.name);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\StatePropertiesPredicate$PropertyMatcher.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */