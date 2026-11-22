/*    */ package net.minecraft.client.resources.metadata.gui;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.OptionalInt;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class NineSlice extends Record implements GuiSpriteScaling {
/*    */   private final int width;
/*    */   
/*    */   private final int height;
/*    */   
/*    */   private final Border border;
/*    */   
/*    */   private final boolean stretchInner;
/*    */   
/*    */   public static final MapCodec<NineSlice> CODEC;
/*    */   
/*    */   public NineSlice(int width, int height, Border border, boolean stretchInner) {
/* 41 */     this.width = width;
/* 41 */     this.height = height;
/* 41 */     this.border = border;
/* 41 */     this.stretchInner = stretchInner;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int width() {
/* 41 */     return this.width;
/*    */   }
/*    */   
/*    */   public int height() {
/* 41 */     return this.height;
/*    */   }
/*    */   
/*    */   public Border border() {
/* 41 */     return this.border;
/*    */   }
/*    */   
/*    */   public boolean stretchInner() {
/* 41 */     return this.stretchInner;
/*    */   }
/*    */   
/*    */   static {
/* 47 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(NineSlice::width), (App)ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(NineSlice::height), (App)Border.CODEC.fieldOf("border").forGetter(NineSlice::border), (App)Codec.BOOL.optionalFieldOf("stretch_inner", Boolean.valueOf(false)).forGetter(NineSlice::stretchInner)).apply((Applicative)i, NineSlice::new)).validate(NineSlice::validate);
/*    */   }
/*    */   
/*    */   private static DataResult<NineSlice> validate(NineSlice nineSlice) {
/* 50 */     Border border = nineSlice.border();
/* 51 */     if (border.left() + border.right() >= nineSlice.width())
/* 52 */       return DataResult.error(() -> "Nine-sliced texture has no horizontal center slice: " + border.left() + " + " + border.right() + " >= " + nineSlice.width()); 
/* 54 */     if (border.top() + border.bottom() >= nineSlice.height())
/* 55 */       return DataResult.error(() -> "Nine-sliced texture has no vertical center slice: " + border.top() + " + " + border.bottom() + " >= " + nineSlice.height()); 
/* 57 */     return DataResult.success(nineSlice);
/*    */   }
/*    */   
/*    */   public GuiSpriteScaling.Type type() {
/* 62 */     return GuiSpriteScaling.Type.NINE_SLICE;
/*    */   }
/*    */   
/*    */   public static final class Border extends Record {
/*    */     private final int left;
/*    */     
/*    */     private final int top;
/*    */     
/*    */     private final int right;
/*    */     
/*    */     private final int bottom;
/*    */     
/*    */     private static final Codec<Border> VALUE_CODEC;
/*    */     
/*    */     private static final Codec<Border> RECORD_CODEC;
/*    */     
/*    */     private static final Codec<Border> CODEC;
/*    */     
/*    */     public Border(int left, int top, int right, int bottom) {
/* 65 */       this.left = left;
/* 65 */       this.top = top;
/* 65 */       this.right = right;
/* 65 */       this.bottom = bottom;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #65	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #65	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #65	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice$Border;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public int left() {
/* 65 */       return this.left;
/*    */     }
/*    */     
/*    */     public int top() {
/* 65 */       return this.top;
/*    */     }
/*    */     
/*    */     public int right() {
/* 65 */       return this.right;
/*    */     }
/*    */     
/*    */     public int bottom() {
/* 65 */       return this.bottom;
/*    */     }
/*    */     
/*    */     static {
/* 66 */       VALUE_CODEC = ExtraCodecs.POSITIVE_INT.flatComapMap(size -> new Border(size.intValue(), size.intValue(), size.intValue(), size.intValue()), border -> {
/*    */             OptionalInt size = border.unpackValue();
/*    */             return size.isPresent() ? DataResult.success(Integer.valueOf(size.getAsInt())) : DataResult.error(());
/*    */           });
/* 77 */       RECORD_CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("left").forGetter(Border::left), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("top").forGetter(Border::top), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("right").forGetter(Border::right), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("bottom").forGetter(Border::bottom)).apply((Applicative)i, Border::new));
/* 85 */       CODEC = Codec.either(VALUE_CODEC, RECORD_CODEC).xmap(Either::unwrap, border -> border.unpackValue().isPresent() ? Either.left(border) : Either.right(border));
/*    */     }
/*    */     
/*    */     private OptionalInt unpackValue() {
/* 94 */       if (left() == top() && top() == right() && right() == bottom())
/* 95 */         return OptionalInt.of(left()); 
/* 97 */       return OptionalInt.empty();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\metadata\gui\GuiSpriteScaling$NineSlice.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */