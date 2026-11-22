/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.advancements.predicates.SingleComponentItemPredicate;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.item.equipment.trim.ArmorTrim;
/*    */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*    */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*    */ 
/*    */ public final class TrimPredicate extends Record implements SingleComponentItemPredicate<ArmorTrim> {
/*    */   private final Optional<HolderSet<TrimMaterial>> material;
/*    */   
/*    */   private final Optional<HolderSet<TrimPattern>> pattern;
/*    */   
/*    */   public static final Codec<TrimPredicate> CODEC;
/*    */   
/*    */   public TrimPredicate(Optional<HolderSet<TrimMaterial>> material, Optional<HolderSet<TrimPattern>> pattern) {
/* 17 */     this.material = material;
/* 17 */     this.pattern = pattern;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/TrimPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/TrimPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/TrimPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/TrimPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/TrimPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/TrimPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<HolderSet<TrimMaterial>> material() {
/* 17 */     return this.material;
/*    */   }
/*    */   
/*    */   public Optional<HolderSet<TrimPattern>> pattern() {
/* 17 */     return this.pattern;
/*    */   }
/*    */   
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.TRIM_MATERIAL).optionalFieldOf("material").forGetter(TrimPredicate::material), (App)RegistryCodecs.homogeneousList(Registries.TRIM_PATTERN).optionalFieldOf("pattern").forGetter(TrimPredicate::pattern)).apply((Applicative)i, TrimPredicate::new));
/*    */   }
/*    */   
/*    */   public DataComponentType<ArmorTrim> componentType() {
/* 29 */     return DataComponents.TRIM;
/*    */   }
/*    */   
/*    */   public boolean matches(ArmorTrim value) {
/* 34 */     if (this.material.isPresent() && !((HolderSet)this.material.get()).contains(value.material()))
/* 35 */       return false; 
/* 38 */     if (this.pattern.isPresent() && !((HolderSet)this.pattern.get()).contains(value.pattern()))
/* 39 */       return false; 
/* 42 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\TrimPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */