/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ 
/*    */ public final class TemplateEntry extends Record {
/*    */   private final Identifier template;
/*    */   
/*    */   private final List<Rotation> rotations;
/*    */   
/*    */   public static final Codec<TemplateEntry> CODEC;
/*    */   
/*    */   public TemplateEntry(Identifier template, List<Rotation> rotations) {
/* 16 */     this.template = template;
/* 16 */     this.rotations = rotations;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/TemplateFeatureConfiguration$TemplateEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/TemplateFeatureConfiguration$TemplateEntry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/TemplateFeatureConfiguration$TemplateEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/TemplateFeatureConfiguration$TemplateEntry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/TemplateFeatureConfiguration$TemplateEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/TemplateFeatureConfiguration$TemplateEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier template() {
/* 16 */     return this.template;
/*    */   }
/*    */   
/*    */   public List<Rotation> rotations() {
/* 16 */     return this.rotations;
/*    */   }
/*    */   
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("id").forGetter(TemplateEntry::template), (App)Rotation.CODEC.listOf().optionalFieldOf("rotations", List.of(Rotation.values())).forGetter(TemplateEntry::rotations)).apply((Applicative)i, TemplateEntry::new));
/*    */   }
/*    */   
/*    */   public static TemplateEntry of(Identifier template) {
/* 23 */     return new TemplateEntry(template, List.of(Rotation.values()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\TemplateFeatureConfiguration$TemplateEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */