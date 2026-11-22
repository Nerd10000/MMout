/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class CustomModelData extends Record {
/*    */   private final List<Float> floats;
/*    */   
/*    */   private final List<Boolean> flags;
/*    */   
/*    */   private final List<String> strings;
/*    */   
/*    */   private final List<Integer> colors;
/*    */   
/*    */   public CustomModelData(List<Float> floats, List<Boolean> flags, List<String> strings, List<Integer> colors) {
/* 13 */     this.floats = floats;
/* 13 */     this.flags = flags;
/* 13 */     this.strings = strings;
/* 13 */     this.colors = colors;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/CustomModelData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/CustomModelData;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/CustomModelData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/CustomModelData;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/CustomModelData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/CustomModelData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<Float> floats() {
/* 13 */     return this.floats;
/*    */   }
/*    */   
/*    */   public List<Boolean> flags() {
/* 13 */     return this.flags;
/*    */   }
/*    */   
/*    */   public List<String> strings() {
/* 13 */     return this.strings;
/*    */   }
/*    */   
/*    */   public List<Integer> colors() {
/* 13 */     return this.colors;
/*    */   }
/*    */   
/* 19 */   public static final CustomModelData EMPTY = new CustomModelData(
/* 20 */       List.of(), 
/* 21 */       List.of(), 
/* 22 */       List.of(), 
/* 23 */       List.of());
/*    */   
/*    */   public static final Codec<CustomModelData> CODEC;
/*    */   
/*    */   static {
/* 26 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.FLOAT.listOf().optionalFieldOf("floats", List.of()).forGetter(CustomModelData::floats), (App)Codec.BOOL.listOf().optionalFieldOf("flags", List.of()).forGetter(CustomModelData::flags), (App)Codec.STRING.listOf().optionalFieldOf("strings", List.of()).forGetter(CustomModelData::strings), (App)ExtraCodecs.RGB_COLOR_CODEC.listOf().optionalFieldOf("colors", List.of()).forGetter(CustomModelData::colors)).apply((Applicative)i, CustomModelData::new));
/*    */   }
/*    */   
/* 33 */   public static final StreamCodec<ByteBuf, CustomModelData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT
/* 34 */       .apply(ByteBufCodecs.list()), CustomModelData::floats, ByteBufCodecs.BOOL
/* 35 */       .apply(ByteBufCodecs.list()), CustomModelData::flags, ByteBufCodecs.STRING_UTF8
/* 36 */       .apply(ByteBufCodecs.list()), CustomModelData::strings, ByteBufCodecs.INT
/* 37 */       .apply(ByteBufCodecs.list()), CustomModelData::colors, CustomModelData::new);
/*    */   
/*    */   private static <T> T getSafe(List<T> values, int index) {
/* 42 */     if (index < 0 || index >= values.size())
/* 43 */       return null; 
/* 46 */     return values.get(index);
/*    */   }
/*    */   
/*    */   public Float getFloat(int index) {
/* 50 */     return getSafe(this.floats, index);
/*    */   }
/*    */   
/*    */   public Boolean getBoolean(int index) {
/* 54 */     return getSafe(this.flags, index);
/*    */   }
/*    */   
/*    */   public String getString(int index) {
/* 58 */     return getSafe(this.strings, index);
/*    */   }
/*    */   
/*    */   public Integer getColor(int index) {
/* 62 */     return getSafe(this.colors, index);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\CustomModelData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */