/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ 
/*    */ public final class AttributeEntry extends Record {
/*    */   private final Holder<Attribute> attribute;
/*    */   
/*    */   private final AttributeModifier modifier;
/*    */   
/*    */   public static final Codec<AttributeEntry> CODEC;
/*    */   
/*    */   public AttributeEntry(Holder<Attribute> attribute, AttributeModifier modifier) {
/* 43 */     this.attribute = attribute;
/* 43 */     this.modifier = modifier;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/SulfurCubeArchetype$AttributeEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #43	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$AttributeEntry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/SulfurCubeArchetype$AttributeEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #43	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$AttributeEntry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/SulfurCubeArchetype$AttributeEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #43	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/SulfurCubeArchetype$AttributeEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<Attribute> attribute() {
/* 43 */     return this.attribute;
/*    */   }
/*    */   
/*    */   public AttributeModifier modifier() {
/* 43 */     return this.modifier;
/*    */   }
/*    */   
/*    */   static {
/* 44 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Attribute.CODEC.fieldOf("attribute").forGetter(AttributeEntry::attribute), (App)AttributeModifier.MAP_CODEC.forGetter(AttributeEntry::modifier)).apply((Applicative)i, AttributeEntry::new));
/*    */   }
/*    */   
/*    */   public static AttributeEntry add(Holder<Attribute> attribute, double amount, ResourceKey<SulfurCubeArchetype> archetype) {
/* 50 */     return new AttributeEntry(attribute, new AttributeModifier(Identifier.withDefaultNamespace(archetype.identifier().getPath() + "_add_" + archetype.identifier().getPath()), amount, AttributeModifier.Operation.ADD_VALUE));
/*    */   }
/*    */   
/*    */   public static AttributeEntry multiply(Holder<Attribute> attribute, double amount, ResourceKey<SulfurCubeArchetype> archetype) {
/* 54 */     return new AttributeEntry(attribute, new AttributeModifier(Identifier.withDefaultNamespace(archetype.identifier().getPath() + "_mul_" + archetype.identifier().getPath()), amount - 1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\SulfurCubeArchetype$AttributeEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */