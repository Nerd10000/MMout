/*     */ package net.minecraft.client.data.models.model;
/*     */ 
/*     */ import com.mojang.math.Transformation;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.color.item.Constant;
/*     */ import net.minecraft.client.color.item.ItemTintSource;
/*     */ import net.minecraft.client.renderer.item.CompositeModel;
/*     */ import net.minecraft.client.renderer.item.ConditionalItemModel;
/*     */ import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
/*     */ import net.minecraft.client.renderer.item.ItemModel;
/*     */ import net.minecraft.client.renderer.item.RangeSelectItemModel;
/*     */ import net.minecraft.client.renderer.item.SelectItemModel;
/*     */ import net.minecraft.client.renderer.item.SpecialModelWrapper;
/*     */ import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
/*     */ import net.minecraft.client.renderer.item.properties.conditional.HasComponent;
/*     */ import net.minecraft.client.renderer.item.properties.conditional.IsUsingItem;
/*     */ import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
/*     */ import net.minecraft.client.renderer.item.properties.select.ContextDimension;
/*     */ import net.minecraft.client.renderer.item.properties.select.ItemBlockState;
/*     */ import net.minecraft.client.renderer.item.properties.select.LocalTime;
/*     */ import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.SpecialDates;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class ItemModelUtils {
/*     */   public static ItemModel.Unbaked plainModel(Identifier id) {
/*  36 */     return (ItemModel.Unbaked)new CuboidItemModelWrapper.Unbaked(id, Optional.empty(), List.of());
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked plainModel(Identifier id, Transformation transformation) {
/*  40 */     return (ItemModel.Unbaked)new CuboidItemModelWrapper.Unbaked(id, Optional.of(transformation), List.of());
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked tintedModel(Identifier id, ItemTintSource... tints) {
/*  44 */     return (ItemModel.Unbaked)new CuboidItemModelWrapper.Unbaked(id, Optional.empty(), List.of(tints));
/*     */   }
/*     */   
/*     */   public static ItemTintSource constantTint(int color) {
/*  48 */     return (ItemTintSource)new Constant(color);
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked composite(ItemModel.Unbaked... models) {
/*  52 */     return (ItemModel.Unbaked)new CompositeModel.Unbaked(List.of(models), Optional.empty());
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked specialModel(Identifier base, SpecialModelRenderer.Unbaked<?> model) {
/*  56 */     return specialModel(base, Optional.empty(), model);
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked specialModel(Identifier base, Transformation transformation, SpecialModelRenderer.Unbaked<?> model) {
/*  60 */     return specialModel(base, Optional.of(transformation), model);
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked specialModel(Identifier base, Optional<Transformation> transformation, SpecialModelRenderer.Unbaked<?> model) {
/*  64 */     return (ItemModel.Unbaked)new SpecialModelWrapper.Unbaked(base, transformation, model);
/*     */   }
/*     */   
/*     */   public static RangeSelectItemModel.Entry override(ItemModel.Unbaked model, float value) {
/*  68 */     return new RangeSelectItemModel.Entry(value, model);
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked rangeSelect(RangeSelectItemModelProperty property, ItemModel.Unbaked fallback, RangeSelectItemModel.Entry... entries) {
/*  72 */     return (ItemModel.Unbaked)new RangeSelectItemModel.Unbaked(Optional.empty(), property, 1.0F, List.of(entries), Optional.of(fallback));
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked rangeSelect(RangeSelectItemModelProperty property, float scale, ItemModel.Unbaked fallback, RangeSelectItemModel.Entry... entries) {
/*  76 */     return (ItemModel.Unbaked)new RangeSelectItemModel.Unbaked(Optional.empty(), property, scale, List.of(entries), Optional.of(fallback));
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked rangeSelect(RangeSelectItemModelProperty property, ItemModel.Unbaked fallback, List<RangeSelectItemModel.Entry> entries) {
/*  80 */     return (ItemModel.Unbaked)new RangeSelectItemModel.Unbaked(Optional.empty(), property, 1.0F, entries, Optional.of(fallback));
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked rangeSelect(RangeSelectItemModelProperty property, List<RangeSelectItemModel.Entry> entries) {
/*  84 */     return (ItemModel.Unbaked)new RangeSelectItemModel.Unbaked(Optional.empty(), property, 1.0F, entries, Optional.empty());
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked rangeSelect(RangeSelectItemModelProperty property, float scale, List<RangeSelectItemModel.Entry> entries) {
/*  88 */     return (ItemModel.Unbaked)new RangeSelectItemModel.Unbaked(Optional.empty(), property, scale, entries, Optional.empty());
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked conditional(ConditionalItemModelProperty property, ItemModel.Unbaked onTrue, ItemModel.Unbaked onFalse) {
/*  92 */     return conditional(Optional.empty(), property, onTrue, onFalse);
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked conditional(Transformation transformation, ConditionalItemModelProperty property, ItemModel.Unbaked onTrue, ItemModel.Unbaked onFalse) {
/*  96 */     return conditional(Optional.of(transformation), property, onTrue, onFalse);
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked conditional(Optional<Transformation> transformation, ConditionalItemModelProperty property, ItemModel.Unbaked onTrue, ItemModel.Unbaked onFalse) {
/* 100 */     return (ItemModel.Unbaked)new ConditionalItemModel.Unbaked(transformation, property, onTrue, onFalse);
/*     */   }
/*     */   
/*     */   public static <T> SelectItemModel.SwitchCase<T> when(T value, ItemModel.Unbaked model) {
/* 109 */     return new SelectItemModel.SwitchCase(List.of(value), model);
/*     */   }
/*     */   
/*     */   public static <T> SelectItemModel.SwitchCase<T> when(List<T> values, ItemModel.Unbaked model) {
/* 113 */     return new SelectItemModel.SwitchCase(values, model);
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public static <T> ItemModel.Unbaked select(SelectItemModelProperty<T> property, ItemModel.Unbaked fallback, SelectItemModel.SwitchCase<T>... cases) {
/* 118 */     return select(property, fallback, List.of(cases));
/*     */   }
/*     */   
/*     */   public static <T> ItemModel.Unbaked select(SelectItemModelProperty<T> property, ItemModel.Unbaked fallback, List<SelectItemModel.SwitchCase<T>> cases) {
/* 122 */     return (ItemModel.Unbaked)new SelectItemModel.Unbaked(
/* 123 */         Optional.empty(), new SelectItemModel.UnbakedSwitch(property, cases), 
/*     */         
/* 125 */         Optional.of(fallback));
/*     */   }
/*     */   
/*     */   public static <T> ItemModel.Unbaked select(Transformation transformation, SelectItemModelProperty<T> property, ItemModel.Unbaked fallback, List<SelectItemModel.SwitchCase<T>> cases) {
/* 130 */     return (ItemModel.Unbaked)new SelectItemModel.Unbaked(
/* 131 */         Optional.of(transformation), new SelectItemModel.UnbakedSwitch(property, cases), 
/*     */         
/* 133 */         Optional.of(fallback));
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public static <T> ItemModel.Unbaked select(SelectItemModelProperty<T> property, SelectItemModel.SwitchCase<T>... cases) {
/* 139 */     return select(property, List.of(cases));
/*     */   }
/*     */   
/*     */   public static <T> ItemModel.Unbaked select(SelectItemModelProperty<T> property, List<SelectItemModel.SwitchCase<T>> cases) {
/* 143 */     return (ItemModel.Unbaked)new SelectItemModel.Unbaked(
/* 144 */         Optional.empty(), new SelectItemModel.UnbakedSwitch(property, cases), 
/*     */         
/* 146 */         Optional.empty());
/*     */   }
/*     */   
/*     */   public static ConditionalItemModelProperty isUsingItem() {
/* 151 */     return (ConditionalItemModelProperty)new IsUsingItem();
/*     */   }
/*     */   
/*     */   public static ConditionalItemModelProperty hasComponent(DataComponentType<?> component) {
/* 155 */     return (ConditionalItemModelProperty)new HasComponent(component, false);
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked inOverworld(ItemModel.Unbaked ifTrue, ItemModel.Unbaked ifFalse) {
/* 159 */     return select((SelectItemModelProperty<?>)new ContextDimension(), ifFalse, (SelectItemModel.SwitchCase<?>[])new SelectItemModel.SwitchCase[] { when(Level.OVERWORLD, ifTrue) });
/*     */   }
/*     */   
/*     */   private static <T extends Comparable<T>> List<SelectItemModel.SwitchCase<String>> createBlockPropertySwitchCases(Property<T> property, Map<T, ItemModel.Unbaked> cases) {
/* 167 */     return cases.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(e -> {
/*     */           String valueName = property.getName((Comparable)e.getKey());
/*     */           return new SelectItemModel.SwitchCase(List.of(valueName), (ItemModel.Unbaked)e.getValue());
/* 170 */         }).toList();
/*     */   }
/*     */   
/*     */   public static <T extends Comparable<T>> ItemModel.Unbaked selectBlockItemProperty(Property<T> property, ItemModel.Unbaked fallback, Map<T, ItemModel.Unbaked> cases) {
/* 174 */     return select((SelectItemModelProperty<String>)new ItemBlockState(property
/* 175 */           .getName()), fallback, 
/*     */         
/* 177 */         createBlockPropertySwitchCases(property, cases));
/*     */   }
/*     */   
/*     */   public static <T extends Comparable<T>> ItemModel.Unbaked selectBlockItemProperty(Transformation transformation, Property<T> property, ItemModel.Unbaked fallback, Map<T, ItemModel.Unbaked> cases) {
/* 182 */     return select(transformation, (SelectItemModelProperty<String>)new ItemBlockState(property
/*     */           
/* 184 */           .getName()), fallback, 
/*     */         
/* 186 */         createBlockPropertySwitchCases(property, cases));
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked isXmas(ItemModel.Unbaked onTrue, ItemModel.Unbaked onFalse) {
/* 192 */     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd", Locale.ROOT);
/* 193 */     Objects.requireNonNull(formatter);
/* 193 */     List<String> days = SpecialDates.CHRISTMAS_RANGE.stream().map(formatter::format).toList();
/* 195 */     return select(
/* 196 */         (SelectItemModelProperty<?>)LocalTime.create("MM-dd", "", Optional.empty()), onFalse, 
/*     */         
/* 198 */         List.of(
/* 199 */           when(days, onTrue)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\model\ItemModelUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */