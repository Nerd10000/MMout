/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMultimap;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.Utf8String;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, PropertyMap> {
/*     */   public PropertyMap decode(ByteBuf input) {
/* 697 */     int propertyCount = ByteBufCodecs.readCount(input, 16);
/* 698 */     ImmutableMultimap.Builder<String, Property> result = ImmutableMultimap.builder();
/* 699 */     for (int i = 0; i < propertyCount; i++) {
/* 700 */       String name = Utf8String.read(input, 64);
/* 701 */       String value = Utf8String.read(input, 32767);
/* 702 */       String signature = (String)FriendlyByteBuf.readNullable(input, in -> Utf8String.read(in, 1024));
/* 703 */       Property property = new Property(name, value, signature);
/* 704 */       result.put(property.name(), property);
/*     */     } 
/* 706 */     return new PropertyMap((Multimap)result.build());
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, PropertyMap properties) {
/* 711 */     ByteBufCodecs.writeCount(output, properties.size(), 16);
/* 712 */     for (Property property : properties.values()) {
/* 713 */       Utf8String.write(output, property.name(), 64);
/* 714 */       Utf8String.write(output, property.value(), 32767);
/* 715 */       FriendlyByteBuf.writeNullable(output, property.signature(), (out, signature) -> Utf8String.write(out, signature, 1024));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$32.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */