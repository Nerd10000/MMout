/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.alchemy.Potion;
/*    */ 
/*    */ public class PotionTags {
/* 11 */   public static final TagKey<Potion> TRADEABLE = create("tradeable");
/*    */   
/*    */   private static TagKey<Potion> create(String name) {
/* 14 */     return TagKey.create(Registries.POTION, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\PotionTags.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */