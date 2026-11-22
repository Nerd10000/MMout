/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Comparator;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public interface PriorityProvider<Context, Condition extends PriorityProvider.SelectorCondition<Context>> {
/*    */   public static final class Selector<Context, Condition extends SelectorCondition<Context>> extends Record {
/*    */     private final Optional<Condition> condition;
/*    */     
/*    */     private final int priority;
/*    */     
/*    */     public Selector(Optional<Condition> condition, int priority) {
/* 21 */       this.condition = condition;
/* 21 */       this.priority = priority;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector<TContext;TCondition;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector<TContext;TCondition;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector<TContext;TCondition;>;
/*    */     }
/*    */     
/*    */     public Optional<Condition> condition() {
/* 21 */       return this.condition;
/*    */     }
/*    */     
/*    */     public int priority() {
/* 21 */       return this.priority;
/*    */     }
/*    */     
/*    */     public Selector(Condition condition, int priority) {
/* 26 */       this(Optional.of(condition), priority);
/*    */     }
/*    */     
/*    */     public Selector(int priority) {
/* 30 */       this(Optional.empty(), priority);
/*    */     }
/*    */     
/*    */     public static <Context, Condition extends PriorityProvider.SelectorCondition<Context>> Codec<Selector<Context, Condition>> codec(Codec<Condition> conditionCodec) {
/* 34 */       return RecordCodecBuilder.create(i -> i.group((App)conditionCodec.optionalFieldOf("condition").forGetter(Selector::condition), (App)Codec.INT.fieldOf("priority").forGetter(Selector::priority)).apply((Applicative)i, Selector::new));
/*    */     }
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface SelectorCondition<C> extends Predicate<C> {
/*    */     static <C> SelectorCondition<C> alwaysTrue() {
/* 44 */       return context -> true;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class UnpackedEntry<C, T> extends Record {
/*    */     private final T entry;
/*    */     
/*    */     private final int priority;
/*    */     
/*    */     private final PriorityProvider.SelectorCondition<C> condition;
/*    */     
/*    */     public UnpackedEntry(T entry, int priority, PriorityProvider.SelectorCondition<C> condition) {
/* 48 */       this.entry = entry;
/* 48 */       this.priority = priority;
/* 48 */       this.condition = condition;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry<TC;TT;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry<TC;TT;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry<TC;TT;>;
/*    */     }
/*    */     
/*    */     public T entry() {
/* 48 */       return this.entry;
/*    */     }
/*    */     
/*    */     public int priority() {
/* 48 */       return this.priority;
/*    */     }
/*    */     
/*    */     public PriorityProvider.SelectorCondition<C> condition() {
/* 48 */       return this.condition;
/*    */     }
/*    */     
/* 49 */     public static final Comparator<UnpackedEntry<?, ?>> HIGHEST_PRIORITY_FIRST = Comparator.<UnpackedEntry<?, ?>>comparingInt(UnpackedEntry::priority).reversed();
/*    */   }
/*    */   
/*    */   static <C, T> Stream<T> select(Stream<T> entries, Function<T, PriorityProvider<C, ?>> extractor, C context) {
/* 53 */     List<UnpackedEntry<C, T>> unpackedEntries = new ArrayList<>();
/* 54 */     entries.forEach(entry -> {
/*    */           PriorityProvider<C, ?> provider = extractor.apply(entry);
/*    */           for (Selector<C, ?> selector : provider.selectors())
/*    */             unpackedEntries.add(new UnpackedEntry<>(entry, selector.priority(), (SelectorCondition)DataFixUtils.orElseGet(selector.condition(), SelectorCondition::alwaysTrue))); 
/*    */         });
/* 61 */     unpackedEntries.sort(UnpackedEntry.HIGHEST_PRIORITY_FIRST);
/* 63 */     Iterator<UnpackedEntry<C, T>> iterator = unpackedEntries.iterator();
/* 64 */     int highestMatchedPriority = Integer.MIN_VALUE;
/* 65 */     while (iterator.hasNext()) {
/* 66 */       UnpackedEntry<C, T> entry = iterator.next();
/* 69 */       if (entry.priority < highestMatchedPriority) {
/* 70 */         iterator.remove();
/*    */         continue;
/*    */       } 
/* 72 */       if (entry.condition.test(context)) {
/* 73 */         highestMatchedPriority = entry.priority;
/*    */         continue;
/*    */       } 
/* 75 */       iterator.remove();
/*    */     } 
/* 80 */     return unpackedEntries.stream().map(UnpackedEntry::entry);
/*    */   }
/*    */   
/*    */   static <C, T> Optional<T> pick(Stream<T> entries, Function<T, PriorityProvider<C, ?>> extractor, RandomSource randomSource, C context) {
/* 84 */     List<T> selected = select(entries, extractor, context).toList();
/* 85 */     return Util.getRandomSafe(selected, randomSource);
/*    */   }
/*    */   
/*    */   static <Context, Condition extends SelectorCondition<Context>> List<Selector<Context, Condition>> single(Condition check, int priority) {
/* 89 */     return List.of(new Selector<>(check, priority));
/*    */   }
/*    */   
/*    */   static <Context, Condition extends SelectorCondition<Context>> List<Selector<Context, Condition>> alwaysTrue(int priority) {
/* 95 */     return List.of(new Selector<>(
/* 96 */           Optional.empty(), priority));
/*    */   }
/*    */   
/*    */   List<Selector<Context, Condition>> selectors();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\variant\PriorityProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */