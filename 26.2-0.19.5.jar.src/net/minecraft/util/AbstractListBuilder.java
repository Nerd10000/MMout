/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import com.mojang.serialization.ListBuilder;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ public abstract class AbstractListBuilder<T, B> implements ListBuilder<T> {
/*    */   private final DynamicOps<T> ops;
/*    */   
/* 12 */   protected DataResult<B> builder = DataResult.success(initBuilder(), Lifecycle.stable());
/*    */   
/*    */   protected AbstractListBuilder(DynamicOps<T> ops) {
/* 15 */     this.ops = ops;
/*    */   }
/*    */   
/*    */   public DynamicOps<T> ops() {
/* 20 */     return this.ops;
/*    */   }
/*    */   
/*    */   public ListBuilder<T> add(T value) {
/* 31 */     this.builder = this.builder.map(b -> append((B)b, (T)value));
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public ListBuilder<T> add(DataResult<T> value) {
/* 37 */     this.builder = this.builder.apply2stable(this::append, value);
/* 38 */     return this;
/*    */   }
/*    */   
/*    */   public ListBuilder<T> withErrorsFrom(DataResult<?> result) {
/* 43 */     this.builder = this.builder.flatMap(r -> result.map(()));
/* 44 */     return this;
/*    */   }
/*    */   
/*    */   public ListBuilder<T> mapError(UnaryOperator<String> onError) {
/* 49 */     this.builder = this.builder.mapError(onError);
/* 50 */     return this;
/*    */   }
/*    */   
/*    */   public DataResult<T> build(T prefix) {
/* 55 */     DataResult<T> result = this.builder.flatMap(b -> build((B)b, (T)prefix));
/* 56 */     this.builder = DataResult.success(initBuilder(), Lifecycle.stable());
/* 57 */     return result;
/*    */   }
/*    */   
/*    */   protected abstract B initBuilder();
/*    */   
/*    */   protected abstract B append(B paramB, T paramT);
/*    */   
/*    */   protected abstract DataResult<T> build(B paramB, T paramT);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\AbstractListBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */