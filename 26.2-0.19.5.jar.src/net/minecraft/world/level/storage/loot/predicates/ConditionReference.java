/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class ConditionReference extends Record implements LootItemCondition {
/*    */   private final ResourceKey<LootItemCondition> name;
/*    */   
/*    */   public ConditionReference(ResourceKey<LootItemCondition> name) {
/* 14 */     this.name = name;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/ConditionReference;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/ConditionReference;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/ConditionReference;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/ConditionReference;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/ConditionReference;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/ConditionReference;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ResourceKey<LootItemCondition> name() {
/* 14 */     return this.name;
/*    */   }
/*    */   
/* 17 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static final MapCodec<ConditionReference> MAP_CODEC;
/*    */   
/*    */   static {
/* 19 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.PREDICATE).fieldOf("name").forGetter(ConditionReference::name)).apply((Applicative)i, ConditionReference::new));
/*    */   }
/*    */   
/*    */   public MapCodec<ConditionReference> codec() {
/* 25 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 30 */     super.validate(context);
/* 32 */     Validatable.validateReference(context, this.name);
/*    */   }
/*    */   
/*    */   public boolean test(LootContext lootContext) {
/* 37 */     LootItemCondition condition = lootContext.getResolver().get(this.name).map(Holder.Reference::value).orElse(null);
/* 38 */     if (condition == null) {
/* 39 */       LOGGER.warn("Tried using unknown condition table called {}", this.name.identifier());
/* 40 */       return false;
/*    */     } 
/* 42 */     LootContext.VisitedEntry<?> breadcrumb = LootContext.createVisitedEntry(condition);
/* 43 */     if (lootContext.pushVisitedElement(breadcrumb))
/*    */       try {
/* 45 */         return condition.test(lootContext);
/*    */       } finally {
/* 47 */         lootContext.popVisitedElement(breadcrumb);
/*    */       }  
/* 50 */     LOGGER.warn("Detected infinite loop in loot tables");
/* 51 */     return false;
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder conditionReference(ResourceKey<LootItemCondition> name) {
/* 56 */     return () -> new ConditionReference(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\ConditionReference.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */