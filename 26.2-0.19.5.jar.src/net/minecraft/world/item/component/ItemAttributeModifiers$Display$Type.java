/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum Type implements StringRepresentable {
/* 129 */   DEFAULT("default", 0, (MapCodec)ItemAttributeModifiers.Display.Default.CODEC, (StreamCodec)ItemAttributeModifiers.Display.Default.STREAM_CODEC),
/* 130 */   HIDDEN("hidden", 1, (MapCodec)ItemAttributeModifiers.Display.Hidden.CODEC, (StreamCodec)ItemAttributeModifiers.Display.Hidden.STREAM_CODEC),
/* 131 */   OVERRIDE("override", 2, (MapCodec)ItemAttributeModifiers.Display.OverrideText.CODEC, (StreamCodec)ItemAttributeModifiers.Display.OverrideText.STREAM_CODEC);
/*     */   
/*     */   private static final Codec<Type> CODEC;
/*     */   
/*     */   private static final IntFunction<Type> BY_ID;
/*     */   
/*     */   private static final StreamCodec<ByteBuf, Type> STREAM_CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final MapCodec<? extends ItemAttributeModifiers.Display> codec;
/*     */   
/*     */   private final StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec;
/*     */   
/*     */   static {
/* 134 */     CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/* 136 */     BY_ID = ByIdMap.continuous(Type::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 137 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Type::id);
/*     */   }
/*     */   
/*     */   Type(String name, int id, MapCodec<? extends ItemAttributeModifiers.Display> codec, StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec) {
/* 145 */     this.name = name;
/* 146 */     this.id = id;
/* 147 */     this.codec = codec;
/* 148 */     this.streamCodec = streamCodec;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 153 */     return this.name;
/*     */   }
/*     */   
/*     */   private int id() {
/* 157 */     return this.id;
/*     */   }
/*     */   
/*     */   private StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec() {
/* 161 */     return this.streamCodec;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\ItemAttributeModifiers$Display$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */