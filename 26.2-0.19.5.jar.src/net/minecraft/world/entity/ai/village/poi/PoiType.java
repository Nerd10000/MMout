/*    */ package net.minecraft.world.entity.ai.village.poi;
/*    */ 
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public final class PoiType extends Record {
/*    */   private final Set<BlockState> matchingStates;
/*    */   
/*    */   private final int maxTickets;
/*    */   
/*    */   private final int validRange;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/village/poi/PoiType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiType;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/village/poi/PoiType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiType;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/village/poi/PoiType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiType;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Set<BlockState> matchingStates() {
/*  9 */     return this.matchingStates;
/*    */   }
/*    */   
/*    */   public int maxTickets() {
/*  9 */     return this.maxTickets;
/*    */   }
/*    */   
/*    */   public int validRange() {
/*  9 */     return this.validRange;
/*    */   }
/*    */   
/*    */   public static final Predicate<Holder<PoiType>> NONE = poiType -> false;
/*    */   
/*    */   public PoiType(Set<BlockState> matchingStates, int maxTickets, int validRange) {
/* 17 */     matchingStates = Set.copyOf(matchingStates);
/*    */     this.matchingStates = matchingStates;
/*    */     this.maxTickets = maxTickets;
/*    */     this.validRange = validRange;
/*    */   }
/*    */   
/*    */   public boolean is(BlockState state) {
/* 21 */     return this.matchingStates.contains(state);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\village\poi\PoiType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */