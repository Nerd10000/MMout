/*    */ package net.minecraft.client.data.models.blockstates;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.data.models.MultiVariant;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
/*    */ import net.minecraft.client.renderer.block.dispatch.multipart.Condition;
/*    */ import net.minecraft.client.renderer.block.dispatch.multipart.Selector;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class MultiPartGenerator implements BlockModelDefinitionGenerator {
/*    */   private final Block block;
/*    */   
/* 15 */   private final List<Entry> parts = new ArrayList<>();
/*    */   
/*    */   private MultiPartGenerator(Block block) {
/* 18 */     this.block = block;
/*    */   }
/*    */   
/*    */   public Block block() {
/* 23 */     return this.block;
/*    */   }
/*    */   
/*    */   public static MultiPartGenerator multiPart(Block block) {
/* 27 */     return new MultiPartGenerator(block);
/*    */   }
/*    */   
/*    */   public MultiPartGenerator with(MultiVariant variants) {
/* 31 */     this.parts.add(new Entry(Optional.empty(), variants));
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   private void validateCondition(Condition condition) {
/* 36 */     condition.instantiate(this.block.getStateDefinition());
/*    */   }
/*    */   
/*    */   public MultiPartGenerator with(Condition condition, MultiVariant variants) {
/* 40 */     validateCondition(condition);
/* 41 */     this.parts.add(new Entry(Optional.of(condition), variants));
/* 42 */     return this;
/*    */   }
/*    */   
/*    */   public MultiPartGenerator with(ConditionBuilder condition, MultiVariant variants) {
/* 46 */     return with(condition.build(), variants);
/*    */   }
/*    */   
/*    */   public BlockStateModelDispatcher create() {
/* 51 */     return new BlockStateModelDispatcher(
/* 52 */         Optional.empty(), 
/* 53 */         Optional.of(new BlockStateModelDispatcher.MultiPartDefinition(this.parts.stream().map(Entry::toUnbaked).toList())));
/*    */   }
/*    */   
/*    */   private static final class Entry extends Record {
/*    */     private final Optional<Condition> condition;
/*    */     
/*    */     private final MultiVariant variants;
/*    */     
/*    */     private Entry(Optional<Condition> condition, MultiVariant variants) {
/* 57 */       this.condition = condition;
/* 57 */       this.variants = variants;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/data/models/blockstates/MultiPartGenerator$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #57	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/data/models/blockstates/MultiPartGenerator$Entry;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/data/models/blockstates/MultiPartGenerator$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #57	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/data/models/blockstates/MultiPartGenerator$Entry;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/data/models/blockstates/MultiPartGenerator$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #57	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/data/models/blockstates/MultiPartGenerator$Entry;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Optional<Condition> condition() {
/* 57 */       return this.condition;
/*    */     }
/*    */     
/*    */     public MultiVariant variants() {
/* 57 */       return this.variants;
/*    */     }
/*    */     
/*    */     public Selector toUnbaked() {
/* 62 */       return new Selector(this.condition, this.variants.toUnbaked());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\blockstates\MultiPartGenerator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */