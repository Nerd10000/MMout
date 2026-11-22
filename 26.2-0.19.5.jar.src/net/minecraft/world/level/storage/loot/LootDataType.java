/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.util.context.ContextKeySet;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public final class LootDataType<T extends Validatable> extends Record {
/*    */   private final ResourceKey<Registry<T>> registryKey;
/*    */   
/*    */   private final Codec<T> codec;
/*    */   
/*    */   private final ContextGetter<T> contextGetter;
/*    */   
/*    */   public LootDataType(ResourceKey<Registry<T>> registryKey, Codec<T> codec, ContextGetter<T> contextGetter) {
/* 17 */     this.registryKey = registryKey;
/* 17 */     this.codec = codec;
/* 17 */     this.contextGetter = contextGetter;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/LootDataType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/LootDataType;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/LootDataType<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/LootDataType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/LootDataType;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/LootDataType<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/LootDataType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/LootDataType;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/LootDataType<TT;>;
/*    */   }
/*    */   
/*    */   public ResourceKey<Registry<T>> registryKey() {
/* 17 */     return this.registryKey;
/*    */   }
/*    */   
/*    */   public Codec<T> codec() {
/* 17 */     return this.codec;
/*    */   }
/*    */   
/*    */   public ContextGetter<T> contextGetter() {
/* 17 */     return this.contextGetter;
/*    */   }
/*    */   
/* 22 */   public static final LootDataType<LootItemCondition> PREDICATE = new LootDataType(Registries.PREDICATE, LootItemCondition.DIRECT_CODEC, ContextGetter.constant(LootContextParamSets.ALL_PARAMS));
/*    */   
/* 23 */   public static final LootDataType<LootItemFunction> MODIFIER = new LootDataType(Registries.ITEM_MODIFIER, LootItemFunctions.ROOT_CODEC, ContextGetter.constant(LootContextParamSets.ALL_PARAMS));
/*    */   
/* 24 */   public static final LootDataType<LootTable> TABLE = new LootDataType(Registries.LOOT_TABLE, (Codec)LootTable.DIRECT_CODEC, LootTable::getParamSet);
/*    */   
/*    */   public void runValidation(ValidationContextSource contextSource, ResourceKey<T> key, T value) {
/* 27 */     ContextKeySet contextKeys = this.contextGetter.context(value);
/* 28 */     ValidationContext rootContext = contextSource.context(contextKeys).enterElement((ProblemReporter.PathElement)new ProblemReporter.RootElementPathElement(key), key);
/* 29 */     value.validate(rootContext);
/*    */   }
/*    */   
/*    */   public void runValidation(ValidationContextSource contextSource, HolderLookup<T> lookup) {
/* 33 */     lookup.listElements().forEach(holder -> runValidation(contextSource, holder.key(), (T)holder.value()));
/*    */   }
/*    */   
/*    */   public static Stream<LootDataType<?>> values() {
/* 37 */     return Stream.of((LootDataType<?>[])new LootDataType[] { PREDICATE, MODIFIER, TABLE });
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface ContextGetter<T> {
/*    */     ContextKeySet context(T param1T);
/*    */     
/*    */     static <T> ContextGetter<T> constant(ContextKeySet v) {
/* 45 */       return value -> v;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\LootDataType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */