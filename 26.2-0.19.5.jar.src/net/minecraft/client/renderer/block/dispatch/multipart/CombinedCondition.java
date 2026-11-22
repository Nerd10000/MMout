/*    */ package net.minecraft.client.renderer.block.dispatch.multipart;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.StateHolder;
/*    */ 
/*    */ public final class CombinedCondition extends Record implements Condition {
/*    */   private final Operation operation;
/*    */   
/*    */   private final List<Condition> terms;
/*    */   
/*    */   public CombinedCondition(Operation operation, List<Condition> terms) {
/* 13 */     this.operation = operation;
/* 13 */     this.terms = terms;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/dispatch/multipart/CombinedCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/CombinedCondition;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/dispatch/multipart/CombinedCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/CombinedCondition;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/dispatch/multipart/CombinedCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/CombinedCondition;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Operation operation() {
/* 13 */     return this.operation;
/*    */   }
/*    */   
/*    */   public List<Condition> terms() {
/* 13 */     return this.terms;
/*    */   }
/*    */   
/*    */   public enum Operation implements StringRepresentable {
/* 19 */     AND("AND") {
/*    */       public <V> Predicate<V> apply(List<Predicate<V>> terms) {
/* 22 */         return Util.allOf(terms);
/*    */       }
/*    */     },
/* 25 */     OR("OR") {
/*    */       public <V> Predicate<V> apply(List<Predicate<V>> terms) {
/* 28 */         return Util.anyOf(terms);
/*    */       }
/*    */     };
/*    */     
/* 33 */     public static final Codec<Operation> CODEC = (Codec<Operation>)StringRepresentable.fromEnum(Operation::values);
/*    */     
/*    */     private final String name;
/*    */     
/*    */     static {
/*    */     
/*    */     }
/*    */     
/*    */     Operation(String name) {
/* 38 */       this.name = name;
/*    */     }
/*    */     
/*    */     public String getSerializedName() {
/* 43 */       return this.name;
/*    */     }
/*    */     
/*    */     public abstract <V> Predicate<V> apply(List<Predicate<V>> param1List);
/*    */   }
/*    */   
/*    */   enum null {
/*    */     public <V> Predicate<V> apply(List<Predicate<V>> terms) {
/*    */       return Util.allOf(terms);
/*    */     }
/*    */   }
/*    */   
/*    */   enum null {
/*    */     public <V> Predicate<V> apply(List<Predicate<V>> terms) {
/*    */       return Util.anyOf(terms);
/*    */     }
/*    */   }
/*    */   
/*    */   public <O, S extends StateHolder<O, S>> Predicate<S> instantiate(StateDefinition<O, S> definition) {
/* 51 */     return this.operation.apply(Lists.transform(this.terms, c -> c.instantiate(definition)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\multipart\CombinedCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */