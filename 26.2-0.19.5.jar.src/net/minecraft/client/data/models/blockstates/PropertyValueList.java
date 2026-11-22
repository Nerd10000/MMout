/*    */ package net.minecraft.client.data.models.blockstates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public final class PropertyValueList extends Record {
/*    */   private final List<Property.Value<?>> values;
/*    */   
/*    */   public PropertyValueList(List<Property.Value<?>> values) {
/* 11 */     this.values = values;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/data/models/blockstates/PropertyValueList;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/data/models/blockstates/PropertyValueList;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/data/models/blockstates/PropertyValueList;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/data/models/blockstates/PropertyValueList;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<Property.Value<?>> values() {
/* 11 */     return this.values;
/*    */   }
/*    */   
/* 14 */   public static final PropertyValueList EMPTY = new PropertyValueList(List.of());
/*    */   
/*    */   private static final Comparator<Property.Value<?>> COMPARE_BY_NAME;
/*    */   
/*    */   static {
/* 16 */     COMPARE_BY_NAME = Comparator.comparing(p -> p.property().getName());
/*    */   }
/*    */   
/*    */   public PropertyValueList extend(Property.Value<?> element) {
/* 19 */     return new PropertyValueList(Util.copyAndAdd(this.values, element));
/*    */   }
/*    */   
/*    */   public PropertyValueList extend(PropertyValueList other) {
/* 23 */     return new PropertyValueList((List<Property.Value<?>>)ImmutableList.builder().addAll(this.values).addAll(other.values).build());
/*    */   }
/*    */   
/*    */   public static PropertyValueList of(Property.Value<?>... values) {
/* 27 */     return new PropertyValueList(List.of(values));
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 31 */     return this.values.stream().sorted(COMPARE_BY_NAME).map(Property.Value::toString).collect(Collectors.joining(","));
/*    */   }
/*    */   
/*    */   public String toString() {
/* 36 */     return getKey();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\blockstates\PropertyValueList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */