/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class AreaEffectCloudDurationScaleFix extends NamedEntityFix {
/*    */   public AreaEffectCloudDurationScaleFix(Schema outputSchema) {
/*  9 */     super(outputSchema, false, "AreaEffectCloudDurationScaleFix", References.ENTITY, "minecraft:area_effect_cloud");
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 14 */     return entity.update(DSL.remainderFinder(), tag -> tag.set("potion_duration_scale", tag.createFloat(0.25F)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\AreaEffectCloudDurationScaleFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */