/*     */ package net.minecraft.world.scores;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.numbers.NumberFormat;
/*     */ import net.minecraft.network.chat.numbers.NumberFormatTypes;
/*     */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*     */ 
/*     */ public final class Packed extends Record {
/*     */   private final String name;
/*     */   
/*     */   private final ObjectiveCriteria criteria;
/*     */   
/*     */   private final Component displayName;
/*     */   
/*     */   private final ObjectiveCriteria.RenderType renderType;
/*     */   
/*     */   private final boolean displayAutoUpdate;
/*     */   
/*     */   private final Optional<NumberFormat> numberFormat;
/*     */   
/*     */   public static final Codec<Packed> CODEC;
/*     */   
/*     */   public Packed(String name, ObjectiveCriteria criteria, Component displayName, ObjectiveCriteria.RenderType renderType, boolean displayAutoUpdate, Optional<NumberFormat> numberFormat) {
/* 112 */     this.name = name;
/* 112 */     this.criteria = criteria;
/* 112 */     this.displayName = displayName;
/* 112 */     this.renderType = renderType;
/* 112 */     this.displayAutoUpdate = displayAutoUpdate;
/* 112 */     this.numberFormat = numberFormat;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/Objective$Packed;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/scores/Objective$Packed;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/Objective$Packed;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/scores/Objective$Packed;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/Objective$Packed;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/scores/Objective$Packed;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String name() {
/* 112 */     return this.name;
/*     */   }
/*     */   
/*     */   public ObjectiveCriteria criteria() {
/* 112 */     return this.criteria;
/*     */   }
/*     */   
/*     */   public Component displayName() {
/* 112 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public ObjectiveCriteria.RenderType renderType() {
/* 112 */     return this.renderType;
/*     */   }
/*     */   
/*     */   public boolean displayAutoUpdate() {
/* 112 */     return this.displayAutoUpdate;
/*     */   }
/*     */   
/*     */   public Optional<NumberFormat> numberFormat() {
/* 112 */     return this.numberFormat;
/*     */   }
/*     */   
/*     */   static {
/* 120 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("Name").forGetter(Packed::name), (App)ObjectiveCriteria.CODEC.optionalFieldOf("CriteriaName", ObjectiveCriteria.DUMMY).forGetter(Packed::criteria), (App)ComponentSerialization.CODEC.fieldOf("DisplayName").forGetter(Packed::displayName), (App)ObjectiveCriteria.RenderType.CODEC.optionalFieldOf("RenderType", ObjectiveCriteria.RenderType.INTEGER).forGetter(Packed::renderType), (App)Codec.BOOL.optionalFieldOf("display_auto_update", Boolean.valueOf(false)).forGetter(Packed::displayAutoUpdate), (App)NumberFormatTypes.CODEC.optionalFieldOf("format").forGetter(Packed::numberFormat)).apply((Applicative)i, Packed::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\Objective$Packed.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */