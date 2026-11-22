/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class OminousBannerRenameFix extends ItemStackTagFix {
/*    */   public OminousBannerRenameFix(Schema outputSchema) {
/* 13 */     super(outputSchema, "OminousBannerRenameFix", id -> id.equals("minecraft:white_banner"));
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fixItemStackTag(Dynamic<T> tag) {
/* 17 */     return tag.update("display", display -> display.update("Name", ()));
/*    */   }
/*    */   
/*    */   protected Typed<?> fixItemStackTag(Typed<?> tag) {
/* 28 */     return Util.writeAndReadTypedOrThrow(tag, tag.getType(), this::fixItemStackTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\OminousBannerRenameFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */