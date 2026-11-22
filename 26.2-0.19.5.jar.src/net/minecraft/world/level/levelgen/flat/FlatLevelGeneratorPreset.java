/*    */ package net.minecraft.world.level.levelgen.flat;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ 
/*    */ public final class FlatLevelGeneratorPreset extends Record {
/*    */   private final Holder<Item> displayItem;
/*    */   
/*    */   private final FlatLevelGeneratorSettings settings;
/*    */   
/*    */   public static final Codec<FlatLevelGeneratorPreset> DIRECT_CODEC;
/*    */   
/*    */   public FlatLevelGeneratorPreset(Holder<Item> displayItem, FlatLevelGeneratorSettings settings) {
/* 10 */     this.displayItem = displayItem;
/* 10 */     this.settings = settings;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorPreset;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<Item> displayItem() {
/* 10 */     return this.displayItem;
/*    */   }
/*    */   
/*    */   public FlatLevelGeneratorSettings settings() {
/* 10 */     return this.settings;
/*    */   }
/*    */   
/*    */   static {
/* 14 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)Item.CODEC.fieldOf("display").forGetter(()), (App)FlatLevelGeneratorSettings.CODEC.fieldOf("settings").forGetter(())).apply((Applicative)i, FlatLevelGeneratorPreset::new));
/*    */   }
/*    */   
/* 19 */   public static final Codec<Holder<FlatLevelGeneratorPreset>> CODEC = (Codec<Holder<FlatLevelGeneratorPreset>>)RegistryFileCodec.create(Registries.FLAT_LEVEL_GENERATOR_PRESET, DIRECT_CODEC);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\flat\FlatLevelGeneratorPreset.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */