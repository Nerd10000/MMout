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
/*    */ import net.minecraft.world.item.component.ItemContainerContents;
/*    */ 
/*    */ public final class ContainerPredicate extends Record implements SingleComponentItemPredicate<ItemContainerContents> {
/*    */   private final Optional<CollectionPredicate<ItemInstance, ItemPredicate>> items;
/*    */   
/*    */   public static final Codec<ContainerPredicate> CODEC;
/*    */   
/*    */   public ContainerPredicate(Optional<CollectionPredicate<ItemInstance, ItemPredicate>> items) {
/* 15 */     this.items = items;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/ContainerPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/ContainerPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/ContainerPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/ContainerPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/ContainerPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/ContainerPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<CollectionPredicate<ItemInstance, ItemPredicate>> items() {
/* 15 */     return this.items;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)CollectionPredicate.codec(ItemPredicate.CODEC).optionalFieldOf("items").forGetter(ContainerPredicate::items)).apply((Applicative)i, ContainerPredicate::new));
/*    */   }
/*    */   
/*    */   public DataComponentType<ItemContainerContents> componentType() {
/* 22 */     return DataComponents.CONTAINER;
/*    */   }
/*    */   
/*    */   public boolean matches(ItemContainerContents value) {
/* 27 */     if (this.items.isPresent() && !((CollectionPredicate)this.items.get()).test(value.nonEmptyItems()))
/* 28 */       return false; 
/* 31 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\ContainerPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */