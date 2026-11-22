/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.predicates.CollectionPredicate;
/*    */ import net.minecraft.advancements.predicates.ItemPredicate;
/*    */ import net.minecraft.advancements.predicates.SingleComponentItemPredicate;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.item.component.BundleContents;
/*    */ 
/*    */ public final class BundlePredicate extends Record implements SingleComponentItemPredicate<BundleContents> {
/*    */   private final Optional<CollectionPredicate<ItemInstance, ItemPredicate>> items;
/*    */   
/*    */   public static final Codec<BundlePredicate> CODEC;
/*    */   
/*    */   public BundlePredicate(Optional<CollectionPredicate<ItemInstance, ItemPredicate>> items) {
/* 15 */     this.items = items;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/BundlePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/BundlePredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/BundlePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/BundlePredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/BundlePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/BundlePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<CollectionPredicate<ItemInstance, ItemPredicate>> items() {
/* 15 */     return this.items;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)CollectionPredicate.codec(ItemPredicate.CODEC).optionalFieldOf("items").forGetter(BundlePredicate::items)).apply((Applicative)i, BundlePredicate::new));
/*    */   }
/*    */   
/*    */   public DataComponentType<BundleContents> componentType() {
/* 24 */     return DataComponents.BUNDLE_CONTENTS;
/*    */   }
/*    */   
/*    */   public boolean matches(BundleContents value) {
/* 29 */     if (this.items.isPresent() && !((CollectionPredicate)this.items.get()).test(value.items()))
/* 30 */       return false; 
/* 33 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\BundlePredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */