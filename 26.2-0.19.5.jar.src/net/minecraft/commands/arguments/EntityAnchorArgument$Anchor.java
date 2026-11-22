/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public enum Anchor {
/*    */   FEET, EYES;
/*    */   
/*    */   private static final Map<String, Anchor> BY_NAME;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final BiFunction<Vec3, Entity, Vec3> transform;
/*    */   
/*    */   static {
/* 60 */     FEET = new Anchor("FEET", 0, "feet", (p, e) -> p);
/* 61 */     EYES = new Anchor("EYES", 1, "eyes", (p, e) -> new Vec3(p.x, p.y + e.getEyeHeight(), p.z));
/*    */   }
/*    */   
/*    */   static {
/* 64 */     BY_NAME = (Map<String, Anchor>)Util.make(Maps.newHashMap(), map -> {
/*    */           for (Anchor anchor : values())
/*    */             map.put(anchor.name, anchor); 
/*    */         });
/*    */   }
/*    */   
/*    */   Anchor(String name, BiFunction<Vec3, Entity, Vec3> transform) {
/* 74 */     this.name = name;
/* 75 */     this.transform = transform;
/*    */   }
/*    */   
/*    */   public static Anchor getByName(String name) {
/* 79 */     return BY_NAME.get(name);
/*    */   }
/*    */   
/*    */   public Vec3 apply(Entity entity) {
/* 83 */     return this.transform.apply(entity.position(), entity);
/*    */   }
/*    */   
/*    */   public Vec3 apply(CommandSourceStack source) {
/* 87 */     Entity entity = source.getEntity();
/* 88 */     if (entity == null)
/* 89 */       return source.getPosition(); 
/* 91 */     return this.transform.apply(source.getPosition(), entity);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\EntityAnchorArgument$Anchor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */