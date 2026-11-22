/*    */ package net.minecraft.data.loot;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.util.context.ContextKeySet;
/*    */ 
/*    */ public final class SubProviderEntry extends Record {
/*    */   private final Function<HolderLookup.Provider, LootTableSubProvider> provider;
/*    */   
/*    */   private final ContextKeySet paramSet;
/*    */   
/*    */   public SubProviderEntry(Function<HolderLookup.Provider, LootTableSubProvider> provider, ContextKeySet paramSet) {
/* 39 */     this.provider = provider;
/* 39 */     this.paramSet = paramSet;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #39	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #39	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #39	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Function<HolderLookup.Provider, LootTableSubProvider> provider() {
/* 39 */     return this.provider;
/*    */   }
/*    */   
/*    */   public ContextKeySet paramSet() {
/* 39 */     return this.paramSet;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\loot\LootTableProvider$SubProviderEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */