/*    */ package net.minecraft.network.chat.contents.objects;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class ObjectInfos {
/*  8 */   private static final ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends ObjectInfo>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
/*    */   
/* 10 */   public static final MapCodec<ObjectInfo> CODEC = ComponentSerialization.createLegacyComponentMatcher(ID_MAPPER, ObjectInfo::codec, "object");
/*    */   
/*    */   static {
/* 17 */     ID_MAPPER.put("atlas", AtlasSprite.MAP_CODEC);
/* 18 */     ID_MAPPER.put("player", PlayerSprite.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\objects\ObjectInfos.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */