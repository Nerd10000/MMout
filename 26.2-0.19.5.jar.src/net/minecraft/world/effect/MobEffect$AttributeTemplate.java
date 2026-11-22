/*     */ package net.minecraft.world.effect;
/*     */ 
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ 
/*     */ final class AttributeTemplate extends Record {
/*     */   private final Identifier id;
/*     */   
/*     */   private final double amount;
/*     */   
/*     */   private final AttributeModifier.Operation operation;
/*     */   
/*     */   private AttributeTemplate(Identifier id, double amount, AttributeModifier.Operation operation) {
/* 216 */     this.id = id;
/* 216 */     this.amount = amount;
/* 216 */     this.operation = operation;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/effect/MobEffect$AttributeTemplate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #216	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/effect/MobEffect$AttributeTemplate;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/effect/MobEffect$AttributeTemplate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #216	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/effect/MobEffect$AttributeTemplate;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/effect/MobEffect$AttributeTemplate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #216	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/effect/MobEffect$AttributeTemplate;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Identifier id() {
/* 216 */     return this.id;
/*     */   }
/*     */   
/*     */   public double amount() {
/* 216 */     return this.amount;
/*     */   }
/*     */   
/*     */   public AttributeModifier.Operation operation() {
/* 216 */     return this.operation;
/*     */   }
/*     */   
/*     */   public AttributeModifier create(int amplifier) {
/* 218 */     return new AttributeModifier(this.id, this.amount * (amplifier + 1), this.operation);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\MobEffect$AttributeTemplate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */