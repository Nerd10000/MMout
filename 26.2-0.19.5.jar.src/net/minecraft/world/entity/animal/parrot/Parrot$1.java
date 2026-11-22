/*    */ package net.minecraft.world.entity.animal.parrot;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ class null implements Predicate<Mob> {
/*    */   public boolean test(Mob input) {
/* 82 */     return (input != null && Parrot.MOB_SOUND_MAP.containsKey(input.getType()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\parrot\Parrot$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */