/*    */ package net.minecraft.client.renderer.item.properties.conditional;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class ComponentMatches extends Record implements ConditionalItemModelProperty {
/*    */   private final DataComponentPredicate.Single<?> predicate;
/*    */   
/*    */   public static final MapCodec<ComponentMatches> MAP_CODEC;
/*    */   
/*    */   public ComponentMatches(DataComponentPredicate.Single<?> predicate) {
/* 12 */     this.predicate = predicate;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/conditional/ComponentMatches;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/ComponentMatches;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/conditional/ComponentMatches;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/ComponentMatches;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/conditional/ComponentMatches;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/ComponentMatches;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public DataComponentPredicate.Single<?> predicate() {
/* 12 */     return this.predicate;
/*    */   }
/*    */   
/*    */   static {
/* 13 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DataComponentPredicate.singleCodec("predicate").forGetter(ComponentMatches::predicate)).apply((Applicative)i, ComponentMatches::new));
/*    */   }
/*    */   
/*    */   public boolean get(ItemStack itemStack, ClientLevel level, LivingEntity owner, int seed, ItemDisplayContext displayContext) {
/* 19 */     return this.predicate.predicate().matches((DataComponentGetter)itemStack);
/*    */   }
/*    */   
/*    */   public MapCodec<ComponentMatches> type() {
/* 24 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\conditional\ComponentMatches.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */