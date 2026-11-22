/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.saveddata.maps.MapDecorationType;
/*    */ 
/*    */ public final class Entry extends Record {
/*    */   private final Holder<MapDecorationType> type;
/*    */   
/*    */   private final double x;
/*    */   
/*    */   private final double z;
/*    */   
/*    */   private final float rotation;
/*    */   
/*    */   public static final Codec<Entry> CODEC;
/*    */   
/*    */   public Entry(Holder<MapDecorationType> type, double x, double z, float rotation) {
/* 23 */     this.type = type;
/* 23 */     this.x = x;
/* 23 */     this.z = z;
/* 23 */     this.rotation = rotation;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/MapDecorations$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/MapDecorations$Entry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/MapDecorations$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/MapDecorations$Entry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/MapDecorations$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/MapDecorations$Entry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<MapDecorationType> type() {
/* 23 */     return this.type;
/*    */   }
/*    */   
/*    */   public double x() {
/* 23 */     return this.x;
/*    */   }
/*    */   
/*    */   public double z() {
/* 23 */     return this.z;
/*    */   }
/*    */   
/*    */   public float rotation() {
/* 23 */     return this.rotation;
/*    */   }
/*    */   
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)MapDecorationType.CODEC.fieldOf("type").forGetter(Entry::type), (App)Codec.DOUBLE.fieldOf("x").forGetter(Entry::x), (App)Codec.DOUBLE.fieldOf("z").forGetter(Entry::z), (App)Codec.FLOAT.fieldOf("rotation").forGetter(Entry::rotation)).apply((Applicative)i, Entry::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\MapDecorations$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */