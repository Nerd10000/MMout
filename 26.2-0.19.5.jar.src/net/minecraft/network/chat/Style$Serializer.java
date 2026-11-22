/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class Serializer {
/*    */   public static final MapCodec<Style> MAP_CODEC;
/*    */   
/*    */   static {
/* 21 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TextColor.CODEC.optionalFieldOf("color").forGetter(()), (App)ExtraCodecs.ARGB_COLOR_CODEC.optionalFieldOf("shadow_color").forGetter(()), (App)Codec.BOOL.optionalFieldOf("bold").forGetter(()), (App)Codec.BOOL.optionalFieldOf("italic").forGetter(()), (App)Codec.BOOL.optionalFieldOf("underlined").forGetter(()), (App)Codec.BOOL.optionalFieldOf("strikethrough").forGetter(()), (App)Codec.BOOL.optionalFieldOf("obfuscated").forGetter(()), (App)ClickEvent.CODEC.optionalFieldOf("click_event").forGetter(()), (App)HoverEvent.CODEC.optionalFieldOf("hover_event").forGetter(()), (App)Codec.STRING.optionalFieldOf("insertion").forGetter(()), (App)FontDescription.CODEC.optionalFieldOf("font").forGetter(())).apply((Applicative)i, Style::create));
/*    */   }
/*    */   
/* 36 */   public static final Codec<Style> CODEC = MAP_CODEC.codec();
/*    */   
/* 37 */   public static final StreamCodec<RegistryFriendlyByteBuf, Style> TRUSTED_STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistriesTrusted(CODEC);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\Style$Serializer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */