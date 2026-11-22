/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class EntityZombieVillagerTypeFix extends NamedEntityFix {
/*    */   private static final int PROFESSION_MAX = 6;
/*    */   
/*    */   public EntityZombieVillagerTypeFix(Schema outputSchema, boolean changesType) {
/* 13 */     super(outputSchema, changesType, "EntityZombieVillagerTypeFix", References.ENTITY, "Zombie");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 17 */     if (input.get("IsVillager").asBoolean(false)) {
/* 18 */       if (input.get("ZombieType").result().isEmpty()) {
/* 19 */         int type = getVillagerProfession(input.get("VillagerProfession").asInt(-1));
/* 20 */         if (type == -1)
/* 21 */           type = getVillagerProfession(RandomSource.createThreadLocalInstance().nextInt(6)); 
/* 24 */         input = input.set("ZombieType", input.createInt(type));
/*    */       } 
/* 27 */       input = input.remove("IsVillager");
/*    */     } 
/* 29 */     return input;
/*    */   }
/*    */   
/*    */   private int getVillagerProfession(int profession) {
/* 33 */     if (profession < 0 || profession >= 6)
/* 34 */       return -1; 
/* 36 */     return profession;
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 41 */     return entity.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntityZombieVillagerTypeFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */