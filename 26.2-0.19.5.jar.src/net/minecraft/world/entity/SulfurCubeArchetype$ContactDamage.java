/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.util.valueproviders.FloatProviders;
/*    */ import net.minecraft.world.damagesource.DamageType;
/*    */ 
/*    */ public final class ContactDamage extends Record {
/*    */   private final Holder<DamageType> damageType;
/*    */   
/*    */   private final FloatProvider amount;
/*    */   
/*    */   private final boolean attributeToSource;
/*    */   
/*    */   public static final Codec<ContactDamage> CODEC;
/*    */   
/*    */   public ContactDamage(Holder<DamageType> damageType, FloatProvider amount, boolean attributeToSource) {
/* 66 */     this.damageType = damageType;
/* 66 */     this.amount = amount;
/* 66 */     this.attributeToSource = attributeToSource;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/SulfurCubeArchetype$ContactDamage;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$ContactDamage;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/SulfurCubeArchetype$ContactDamage;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$ContactDamage;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/SulfurCubeArchetype$ContactDamage;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$ContactDamage;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<DamageType> damageType() {
/* 66 */     return this.damageType;
/*    */   }
/*    */   
/*    */   public FloatProvider amount() {
/* 66 */     return this.amount;
/*    */   }
/*    */   
/*    */   public boolean attributeToSource() {
/* 66 */     return this.attributeToSource;
/*    */   }
/*    */   
/*    */   static {
/* 67 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)DamageType.CODEC.fieldOf("damage_type").forGetter(ContactDamage::damageType), (App)FloatProviders.codec(0.0F).fieldOf("amount").forGetter(ContactDamage::amount), (App)Codec.BOOL.fieldOf("attribute_to_source").forGetter(ContactDamage::attributeToSource)).apply((Applicative)i, ContactDamage::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\SulfurCubeArchetype$ContactDamage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */