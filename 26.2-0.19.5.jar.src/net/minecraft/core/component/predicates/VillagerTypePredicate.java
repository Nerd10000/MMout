/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.predicates.SingleComponentItemPredicate;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.entity.npc.villager.VillagerType;
/*    */ 
/*    */ public final class VillagerTypePredicate extends Record implements SingleComponentItemPredicate<Holder<VillagerType>> {
/*    */   private final HolderSet<VillagerType> villagerTypes;
/*    */   
/*    */   public VillagerTypePredicate(HolderSet<VillagerType> villagerTypes) {
/* 13 */     this.villagerTypes = villagerTypes;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/VillagerTypePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/VillagerTypePredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/VillagerTypePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/VillagerTypePredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/VillagerTypePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/VillagerTypePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HolderSet<VillagerType> villagerTypes() {
/* 13 */     return this.villagerTypes;
/*    */   }
/*    */   
/* 14 */   public static final Codec<VillagerTypePredicate> CODEC = RegistryCodecs.homogeneousList(Registries.VILLAGER_TYPE).xmap(VillagerTypePredicate::new, VillagerTypePredicate::villagerTypes);
/*    */   
/*    */   public DataComponentType<Holder<VillagerType>> componentType() {
/* 18 */     return DataComponents.VILLAGER_VARIANT;
/*    */   }
/*    */   
/*    */   public boolean matches(Holder<VillagerType> villagerType) {
/* 23 */     return this.villagerTypes.contains(villagerType);
/*    */   }
/*    */   
/*    */   public static VillagerTypePredicate villagerTypes(HolderSet<VillagerType> villagerTypes) {
/* 27 */     return new VillagerTypePredicate(villagerTypes);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\VillagerTypePredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */