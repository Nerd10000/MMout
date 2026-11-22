/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public final class DebugStickState extends Record {
/*    */   private final Map<Holder<Block>, Property<?>> properties;
/*    */   
/*    */   public DebugStickState(Map<Holder<Block>, Property<?>> properties) {
/* 13 */     this.properties = properties;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/DebugStickState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/DebugStickState;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/DebugStickState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/DebugStickState;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/DebugStickState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/DebugStickState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<Holder<Block>, Property<?>> properties() {
/* 13 */     return this.properties;
/*    */   }
/*    */   
/* 14 */   public static final DebugStickState EMPTY = new DebugStickState(Map.of());
/*    */   
/* 16 */   public static final Codec<DebugStickState> CODEC = Codec.dispatchedMap(BuiltInRegistries.BLOCK.holderByNameCodec(), block -> Codec.STRING.comapFlatMap((), Property::getName))
/*    */     
/* 24 */     .xmap(DebugStickState::new, DebugStickState::properties);
/*    */   
/*    */   public DebugStickState withProperty(Holder<Block> block, Property<?> property) {
/* 27 */     return new DebugStickState(Util.copyAndPut(this.properties, block, property));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\DebugStickState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */