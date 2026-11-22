/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public interface Type extends StringRepresentable {
/* 33 */   public static final Map<String, Type> TYPES = (Map<String, Type>)new Object2ObjectArrayMap();
/*    */   
/* 35 */   public static final Codec<Type> CODEC = Codec.stringResolver(StringRepresentable::getSerializedName, TYPES::get);
/*    */   
/*    */   static {
/* 35 */     Objects.requireNonNull(TYPES);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SkullBlock$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */