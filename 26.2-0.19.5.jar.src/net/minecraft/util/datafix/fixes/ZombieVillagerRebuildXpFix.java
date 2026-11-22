/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class ZombieVillagerRebuildXpFix extends NamedEntityFix {
/*    */   public ZombieVillagerRebuildXpFix(Schema outputSchema, boolean changesType) {
/* 11 */     super(outputSchema, changesType, "Zombie Villager XP rebuild", References.ENTITY, "minecraft:zombie_villager");
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 16 */     return entity.update(DSL.remainderFinder(), remainder -> {
/*    */           Optional<Number> xp = remainder.get("Xp").asNumber().result();
/*    */           if (xp.isEmpty()) {
/*    */             int level = remainder.get("VillagerData").get("level").asInt(1);
/*    */             return remainder.set("Xp", remainder.createInt(VillagerRebuildLevelAndXpFix.getMinXpPerLevel(level)));
/*    */           } 
/*    */           return remainder;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ZombieVillagerRebuildXpFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */