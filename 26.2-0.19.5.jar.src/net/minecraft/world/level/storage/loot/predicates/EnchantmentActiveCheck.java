/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public final class EnchantmentActiveCheck extends Record implements LootItemCondition {
/*    */   private final boolean active;
/*    */   
/*    */   public static final MapCodec<EnchantmentActiveCheck> MAP_CODEC;
/*    */   
/*    */   public EnchantmentActiveCheck(boolean active) {
/* 12 */     this.active = active;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean active() {
/* 12 */     return this.active;
/*    */   }
/*    */   
/*    */   static {
/* 13 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.fieldOf("active").forGetter(EnchantmentActiveCheck::active)).apply((Applicative)i, EnchantmentActiveCheck::new));
/*    */   }
/*    */   
/*    */   public boolean test(LootContext lootContext) {
/* 19 */     return (((Boolean)lootContext.getParameter(LootContextParams.ENCHANTMENT_ACTIVE)).booleanValue() == this.active);
/*    */   }
/*    */   
/*    */   public MapCodec<EnchantmentActiveCheck> codec() {
/* 24 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 29 */     return Set.of(LootContextParams.ENCHANTMENT_ACTIVE);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder enchantmentActiveCheck() {
/* 33 */     return () -> new EnchantmentActiveCheck(true);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder enchantmentInactiveCheck() {
/* 37 */     return () -> new EnchantmentActiveCheck(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\EnchantmentActiveCheck.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */