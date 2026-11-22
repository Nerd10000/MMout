/*    */ package net.minecraft.client.renderer.block.dispatch.multipart;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public enum Operation implements StringRepresentable {
/* 19 */   AND("AND") {
/*    */     public <V> Predicate<V> apply(List<Predicate<V>> terms) {
/* 22 */       return Util.allOf(terms);
/*    */     }
/*    */   },
/* 25 */   OR("OR") {
/*    */     public <V> Predicate<V> apply(List<Predicate<V>> terms) {
/* 28 */       return Util.anyOf(terms);
/*    */     }
/*    */   };
/*    */   
/*    */   public static final Codec<Operation> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 33 */     CODEC = (Codec<Operation>)StringRepresentable.fromEnum(Operation::values);
/*    */   }
/*    */   
/*    */   Operation(String name) {
/* 38 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 43 */     return this.name;
/*    */   }
/*    */   
/*    */   public abstract <V> Predicate<V> apply(List<Predicate<V>> paramList);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\multipart\CombinedCondition$Operation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */