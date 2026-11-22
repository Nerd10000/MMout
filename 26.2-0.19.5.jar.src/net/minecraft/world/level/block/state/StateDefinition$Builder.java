/*     */ package net.minecraft.world.level.block.state;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class Builder<O, S extends StateHolder<O, S>> {
/*     */   private final O owner;
/*     */   
/* 182 */   private final Map<String, Property<?>> properties = Maps.newHashMap();
/*     */   
/*     */   public Builder(O owner) {
/* 185 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public Builder<O, S> add(Property<?>... properties) {
/* 189 */     for (Property<?> property : properties) {
/* 190 */       validateProperty(property);
/* 191 */       this.properties.put(property.getName(), property);
/*     */     } 
/* 193 */     return this;
/*     */   }
/*     */   
/*     */   private <T extends Comparable<T>> void validateProperty(Property<T> property) {
/* 197 */     String name = property.getName();
/* 198 */     if (!StateDefinition.NAME_PATTERN.matcher(name).matches())
/* 199 */       throw new IllegalArgumentException(String.valueOf(this.owner) + " has invalidly named property: " + String.valueOf(this.owner)); 
/* 202 */     Collection<T> values = property.getPossibleValues();
/* 203 */     if (values.size() <= 1)
/* 204 */       throw new IllegalArgumentException(String.valueOf(this.owner) + " attempted use property " + String.valueOf(this.owner) + " with <= 1 possible values"); 
/* 207 */     for (Comparable comparable : values) {
/* 208 */       String valueName = property.getName(comparable);
/* 209 */       if (!StateDefinition.NAME_PATTERN.matcher(valueName).matches())
/* 210 */         throw new IllegalArgumentException(String.valueOf(this.owner) + " has property: " + String.valueOf(this.owner) + " with invalidly named value: " + name); 
/*     */     } 
/* 214 */     if (this.properties.containsKey(name))
/* 215 */       throw new IllegalArgumentException(String.valueOf(this.owner) + " has duplicate property: " + String.valueOf(this.owner)); 
/*     */   }
/*     */   
/*     */   public StateDefinition<O, S> create(Function<O, S> defaultState, StateDefinition.Factory<O, S> factory) {
/* 220 */     return new StateDefinition<>(defaultState, this.owner, factory, this.properties);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\StateDefinition$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */