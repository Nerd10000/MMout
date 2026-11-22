/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import java.util.Collection;
/*     */ 
/*     */ public interface Strategy {
/* 104 */   public static final Strategy AND = AdvancementRequirements::allOf;
/*     */   
/* 105 */   public static final Strategy OR = AdvancementRequirements::anyOf;
/*     */   
/*     */   AdvancementRequirements create(Collection<String> paramCollection);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\AdvancementRequirements$Strategy.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */