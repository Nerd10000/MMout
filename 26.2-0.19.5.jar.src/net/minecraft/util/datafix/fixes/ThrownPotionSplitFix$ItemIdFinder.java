/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ final class ItemIdFinder extends Record {
/*    */   private final OpticFinder<?> itemFinder;
/*    */   
/*    */   private final OpticFinder<Pair<String, String>> itemIdFinder;
/*    */   
/*    */   private ItemIdFinder(OpticFinder<?> itemFinder, OpticFinder<Pair<String, String>> itemIdFinder) {
/* 43 */     this.itemFinder = itemFinder;
/* 43 */     this.itemIdFinder = itemIdFinder;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/datafix/fixes/ThrownPotionSplitFix$ItemIdFinder;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #43	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/datafix/fixes/ThrownPotionSplitFix$ItemIdFinder;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/datafix/fixes/ThrownPotionSplitFix$ItemIdFinder;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #43	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/datafix/fixes/ThrownPotionSplitFix$ItemIdFinder;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/datafix/fixes/ThrownPotionSplitFix$ItemIdFinder;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #43	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/datafix/fixes/ThrownPotionSplitFix$ItemIdFinder;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public OpticFinder<?> itemFinder() {
/* 43 */     return this.itemFinder;
/*    */   }
/*    */   
/*    */   public OpticFinder<Pair<String, String>> itemIdFinder() {
/* 43 */     return this.itemIdFinder;
/*    */   }
/*    */   
/*    */   public String getItemId(Typed<?> entity) {
/* 45 */     return entity.getOptionalTyped(this.itemFinder)
/* 46 */       .flatMap(item -> item.getOptional(this.itemIdFinder))
/* 47 */       .map(Pair::getSecond)
/* 48 */       .map(NamespacedSchema::ensureNamespaced)
/* 49 */       .orElse("");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ThrownPotionSplitFix$ItemIdFinder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */