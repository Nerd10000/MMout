/*     */ package net.minecraft.advancements.predicates.entity;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.advancements.CriterionProgress;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ final class AdvancementCriterionsPredicate extends Record implements PlayerPredicate.AdvancementPredicate {
/*     */   private final Object2BooleanMap<String> criterions;
/*     */   
/*     */   private AdvancementCriterionsPredicate(Object2BooleanMap<String> criterions) {
/*  94 */     this.criterions = criterions;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementCriterionsPredicate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #94	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementCriterionsPredicate;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementCriterionsPredicate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #94	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementCriterionsPredicate;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementCriterionsPredicate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #94	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$AdvancementCriterionsPredicate;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Object2BooleanMap<String> criterions() {
/*  94 */     return this.criterions;
/*     */   }
/*     */   
/*  95 */   public static final Codec<AdvancementCriterionsPredicate> CODEC = ExtraCodecs.object2BooleanMap((Codec)Codec.STRING).xmap(AdvancementCriterionsPredicate::new, AdvancementCriterionsPredicate::criterions);
/*     */   
/*     */   public boolean test(AdvancementProgress progress) {
/*  99 */     for (ObjectIterator<Object2BooleanMap.Entry<String>> objectIterator = this.criterions.object2BooleanEntrySet().iterator(); objectIterator.hasNext(); ) {
/*  99 */       Object2BooleanMap.Entry<String> e = objectIterator.next();
/* 100 */       CriterionProgress criterion = progress.getCriterion((String)e.getKey());
/* 101 */       if (criterion == null || criterion.isDone() != e.getBooleanValue())
/* 102 */         return false; 
/*     */     } 
/* 105 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\PlayerPredicate$AdvancementCriterionsPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */