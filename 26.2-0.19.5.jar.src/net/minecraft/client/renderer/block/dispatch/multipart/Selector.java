/*    */ package net.minecraft.client.renderer.block.dispatch.multipart;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.StateHolder;
/*    */ 
/*    */ public final class Selector extends Record {
/*    */   private final Optional<Condition> condition;
/*    */   
/*    */   private final BlockStateModel.Unbaked variant;
/*    */   
/*    */   public static final Codec<Selector> CODEC;
/*    */   
/*    */   public Selector(Optional<Condition> condition, BlockStateModel.Unbaked variant) {
/* 12 */     this.condition = condition;
/* 12 */     this.variant = variant;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/dispatch/multipart/Selector;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/Selector;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/dispatch/multipart/Selector;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/Selector;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/dispatch/multipart/Selector;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/Selector;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<Condition> condition() {
/* 12 */     return this.condition;
/*    */   }
/*    */   
/*    */   public BlockStateModel.Unbaked variant() {
/* 12 */     return this.variant;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Condition.CODEC.optionalFieldOf("when").forGetter(Selector::condition), (App)BlockStateModel.Unbaked.CODEC.fieldOf("apply").forGetter(Selector::variant)).apply((Applicative)i, Selector::new));
/*    */   }
/*    */   
/*    */   public <O, S extends StateHolder<O, S>> Predicate<S> instantiate(StateDefinition<O, S> definition) {
/* 22 */     return this.condition.<Predicate<S>>map(c -> c.instantiate(definition)).orElse(state -> true);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\multipart\Selector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */