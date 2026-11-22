/*     */ package net.minecraft.util.monitoring.jmx;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import javax.management.MBeanAttributeInfo;
/*     */ 
/*     */ final class AttributeDescription {
/*     */   private final String name;
/*     */   
/*     */   private final Supplier<Object> getter;
/*     */   
/*     */   private final String description;
/*     */   
/*     */   private final Class<?> type;
/*     */   
/*     */   private AttributeDescription(String name, Supplier<Object> getter, String description, Class<?> type) {
/* 115 */     this.name = name;
/* 116 */     this.getter = getter;
/* 117 */     this.description = description;
/* 118 */     this.type = type;
/*     */   }
/*     */   
/*     */   private MBeanAttributeInfo asMBeanAttributeInfo() {
/* 122 */     return new MBeanAttributeInfo(this.name, this.type.getSimpleName(), this.description, true, false, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\monitoring\jmx\MinecraftServerStatistics$AttributeDescription.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */