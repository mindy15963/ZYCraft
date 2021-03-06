package nikita488.zycraft.init;

import com.google.common.collect.ImmutableMap;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.ExplosionDecay;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.IForgeRegistryEntry;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.block.*;
import nikita488.zycraft.client.ZYColors;
import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.enums.ZYType;

import java.util.Map;
import java.util.function.Supplier;

public class ZYBlocks
{
    private static final Registrate REGISTRATE = ZYCraft.registrate().itemGroup(() -> ZYGroups.BLOCKS, "ZYCraft Blocks");

    public static final BlockEntry<Block> ZYCHORITE = REGISTRATE.block("zychorite", Block::new)
            .initialProperties(Material.ROCK, MaterialColor.BLACK)
            .properties(properties -> properties.setRequiresTool().hardnessAndResistance(1.5F, 6))
            .item()
                .tag(ItemTags.STONE_TOOL_MATERIALS, ItemTags.STONE_CRAFTING_MATERIALS)
                .build()
            .register();

    public static final BlockEntry<Block> ZYCHORITE_BLOCK = REGISTRATE.block("zychorite_block", Block::new)
            .initialProperties(ZYCHORITE)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .simpleItem()
            .recipe((ctx, provider) -> storageBlock(provider, ZYCHORITE, ctx::getEntry))
            .register();

    public static final BlockEntry<Block> ZYCHORITE_BRICKS = REGISTRATE.block("zychorite_bricks", Block::new)
            .initialProperties(ZYCHORITE)
            .simpleItem()
            .recipe((ctx, provider) -> bricks(provider, DataIngredient.items(ZYCHORITE), ctx::getEntry))
            .register();

    public static final BlockEntry<Block> SMALL_ZYCHORITE_BRICKS = REGISTRATE.block("small_zychorite_bricks", Block::new)
            .initialProperties(ZYCHORITE)
            .simpleItem()
            .recipe((ctx, provider) -> smallBricks(provider, ZYCHORITE_BRICKS, ctx::getEntry))
            .register();

    public static final BlockEntry<Block> ALUMINIUM_ORE = REGISTRATE.block("aluminium_ore", Block::new)
            .properties(properties -> properties.setRequiresTool().hardnessAndResistance(3, 3))
            .tag(ZYTags.Blocks.ORES_ALUMINIUM, ZYTags.Blocks.ORES_ALUMINUM)
            .simpleItem()
            .recipe((ctx, provider) -> provider.smeltingAndBlasting(DataIngredient.items(ctx.getEntry()), ZYItems.ALUMINIUM, 0.1F))
            .register();

    public static final BlockEntry<Block> ALUMINIUM_BLOCK = REGISTRATE.block("aluminium_block", Block::new)
            .initialProperties(Material.ROCK, MaterialColor.QUARTZ)
            .properties(properties -> properties.setRequiresTool().hardnessAndResistance(1.5F, 6))
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .simpleItem()
            .recipe((ctx, provider) -> storageBlock(provider, ZYItems.ALUMINIUM, ctx::getEntry))
            .register();

    public static final BlockEntry<Block> ALUMINIUM_BRICKS = REGISTRATE.block("aluminium_bricks", Block::new)
            .initialProperties(ALUMINIUM_BLOCK)
            .simpleItem()
            .recipe((ctx, provider) ->
                    infused(provider, DataIngredient.items(ZYItems.ALUMINIUM), DataIngredient.tag(ItemTags.STONE_BRICKS), ctx::getEntry))
            .register();

    public static final BlockEntry<Block> SMALL_ALUMINIUM_BRICKS = REGISTRATE.block("small_aluminium_bricks", Block::new)
            .initialProperties(ALUMINIUM_BLOCK)
            .simpleItem()
            .recipe((ctx, provider) -> smallBricks(provider, ALUMINIUM_BRICKS, ctx::getEntry))
            .register();

    public static final BlockEntry<QuartzCrystalClusterBlock> QUARTZ_CRYSTAL = REGISTRATE.block("quartz_crystal", QuartzCrystalClusterBlock::new)
            .initialProperties(Material.MISCELLANEOUS, MaterialColor.DIAMOND)
            .properties(properties -> properties.hardnessAndResistance(0.3F).setLightLevel(state -> 9).sound(SoundType.GLASS).notSolid())
            .addLayer(() -> RenderType::getTranslucent)
            .loot((tables, block) -> tables.registerLootTable(block, LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .acceptCondition(SurvivesExplosion.builder())
                            .rolls(ConstantRange.of(1))
                            .addEntry(ItemLootEntry.builder(block)
                                    .acceptFunction(SetCount.builder(ConstantRange.of(2))
                                            .acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                                    .withIntProp(QuartzCrystalClusterBlock.AMOUNT, 2))))
                                    .acceptFunction(SetCount.builder(ConstantRange.of(3))
                                            .acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                                    .withIntProp(QuartzCrystalClusterBlock.AMOUNT, 3))))
                                    .acceptFunction(SetCount.builder(ConstantRange.of(4))
                                            .acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                                    .withIntProp(QuartzCrystalClusterBlock.AMOUNT, 4))))
                                    .acceptFunction(SetCount.builder(ConstantRange.of(5))
                                            .acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                                    .withIntProp(QuartzCrystalClusterBlock.AMOUNT, 5))))))))
            .blockstate((ctx, provider) -> NonNullConsumer.noop())
            .item()
                .model((ctx, provider) -> provider.blockItem(ctx::getEntry))
                .build()
            .register();

    public static final BlockEntry<QuartzCrystalBlock> QUARTZ_CRYSTAL_BLOCK = REGISTRATE.block("quartz_crystal_block", QuartzCrystalBlock::new)
            .initialProperties(Material.GLASS, MaterialColor.DIAMOND)
            .properties(properties -> properties.hardnessAndResistance(0.3F).setLightLevel(state -> 9).sound(SoundType.GLASS).notSolid())
            .addLayer(() -> RenderType::getTranslucent)
            .tag(Tags.Blocks.STORAGE_BLOCKS, BlockTags.IMPERMEABLE)
            .simpleItem()
            .recipe((ctx, provider) -> storageBlock(provider, QUARTZ_CRYSTAL, ctx::getEntry))
            .register();

    public static final BlockEntry<QuartzCrystalBlock> QUARTZ_CRYSTAL_BRICKS = REGISTRATE.block("quartz_crystal_bricks", QuartzCrystalBlock::new)
            .initialProperties(QUARTZ_CRYSTAL_BLOCK)
            .addLayer(() -> RenderType::getTranslucent)
            .tag(BlockTags.IMPERMEABLE)
            .simpleItem()
            .recipe((ctx, provider) ->
                    infused(provider, DataIngredient.items(QUARTZ_CRYSTAL), DataIngredient.tag(ItemTags.STONE_BRICKS), ctx::getEntry))
            .register();

    public static final BlockEntry<QuartzCrystalBlock> SMALL_QUARTZ_CRYSTAL_BRICKS = REGISTRATE.block("small_quartz_crystal_bricks", QuartzCrystalBlock::new)
            .initialProperties(QUARTZ_CRYSTAL_BLOCK)
            .addLayer(() -> RenderType::getTranslucent)
            .tag(BlockTags.IMPERMEABLE)
            .simpleItem()
            .recipe((ctx, provider) -> smallBricks(provider, QUARTZ_CRYSTAL_BRICKS, ctx::getEntry))
            .register();

    public static final Map<ZYType, BlockEntry<Block>> ZYCHORIUM_ORE = zyBlock("{type}_zychorium_ore", (type, block) -> block
            .properties(properties -> properties.setRequiresTool().hardnessAndResistance(3, 3))
            .tag(ZYTags.Blocks.ORES_ZYCHORIUM)
            .loot((tables, ore) -> tables.registerLootTable(ore, RegistrateBlockLootTables.droppingWithSilkTouch(ore,
                    ItemLootEntry.builder(ZYItems.ZYCHORIUM.get(type).get())
                            .acceptFunction(ExplosionDecay.builder())
                            .acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))
                            .acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE))))));

    public static final Map<ZYType, BlockEntry<Block>> ZYCHORIUM_BLOCK = zyBlock("{type}_zychorium_block", (type, block) -> block
            .tag(ZYTags.Blocks.STORAGE_BLOCKS_ZYCHORIUM)
            .recipe((ctx, provider) -> storageBlock(provider, ZYItems.ZYCHORIUM.get(type), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> ZYCHORIUM_BRICKS = zyBricks("{type}_zychorium_bricks", (type, block) -> block
            .tag(ZYTags.Blocks.BRICKS_ZYCHORIUM)
            .recipe((ctx, provider) ->
                    infused(provider, DataIngredient.items(ZYItems.ZYCHORIUM.get(type)), DataIngredient.tag(ItemTags.STONE_BRICKS), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> SMALL_ZYCHORIUM_BRICKS = zyBricks("small_{type}_zychorium_bricks", (type, block) -> block
            .tag(ZYTags.Blocks.SMALL_BRICKS_ZYCHORIUM)
            .recipe((ctx, provider) -> smallBricks(provider, ZYCHORIUM_BRICKS.get(type), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> SOLID_ZYCHORIUM_BRICKS = solidZyBricks("solid_{type}_zychorium_bricks", (type, block) -> block
            .tag(ZYTags.Blocks.BRICKS_SOLID_ZYCHORIUM)
            .recipe((ctx, provider) ->
                    infused(provider, DataIngredient.items(Blocks.STONE), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> SMALL_SOLID_ZYCHORIUM_BRICKS = solidZyBricks("small_solid_{type}_zychorium_bricks", (type, block) -> block
            .tag(ZYTags.Blocks.SMALL_BRICKS_SOLID_ZYCHORIUM)
            .recipe((ctx, provider) -> smallBricks(provider, SOLID_ZYCHORIUM_BRICKS.get(type), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> ZYCHORIZED_ZYCHORIUM_BRICKS = zyBlock("zychorized_{type}_zychorium_bricks", (type, block) -> block
            .tag(ZYTags.Blocks.BRICKS_ZYCHORIZED_ZYCHORIUM)
            .recipe((ctx, provider) ->
                    infused(provider, DataIngredient.items(ZYCHORITE), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> SMALL_ZYCHORIZED_ZYCHORIUM_BRICKS = zyBlock("small_zychorized_{type}_zychorium_bricks", (type, block) -> block
            .tag(ZYTags.Blocks.SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM)
            .recipe((ctx, provider) -> smallBricks(provider, ZYCHORIZED_ZYCHORIUM_BRICKS.get(type), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> ALUMINIZED_ZYCHORIUM_BRICKS = zyBlock("aluminized_{type}_zychorium_bricks", (type, block) -> block
            .tag(ZYTags.Blocks.BRICKS_ALUMINIZED_ZYCHORIUM)
            .recipe((ctx, provider) ->
                    infused(provider, DataIngredient.items(ZYItems.ALUMINIUM), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> SMALL_ALUMINIZED_ZYCHORIUM_BRICKS = zyBlock("small_aluminized_{type}_zychorium_bricks", (type, block) -> block
            .tag(ZYTags.Blocks.SMALL_BRICKS_ALUMINIZED_ZYCHORIUM)
            .recipe((ctx, provider) -> smallBricks(provider, ALUMINIZED_ZYCHORIUM_BRICKS.get(type), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> ZYCHORIUM_PLATE = zyBlock("{type}_zychorium_plate", (type, block) -> block
            .properties(properties -> properties.hardnessAndResistance(1.5F, 12))
            .tag(ZYTags.Blocks.ZYCHORIUM_PLATE)
            .recipe((ctx, provider) ->
                    infused(provider, DataIngredient.tag(Tags.Items.INGOTS_IRON), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> ZYCHORIUM_SHIELD = zyBlock("{type}_zychorium_shield", (type, block) -> block
            .properties(properties -> properties.hardnessAndResistance(1.5F, 1200))
            .tag(ZYTags.Blocks.ZYCHORIUM_SHIELD)
            .recipe((ctx, provider) ->
                    infused(provider, DataIngredient.tag(Tags.Items.OBSIDIAN), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final BlockEntry<ZychoriumLampBlock> ZYCHORIUM_LAMP = lamp(false);

    public static final BlockEntry<ZychoriumLampBlock> INVERTED_ZYCHORIUM_LAMP = lamp(true);

    public static final BlockEntry<ColorableBlock> IMMORTAL_BLOCK = REGISTRATE.block("immortal_block", ColorableBlock::new)
            .initialProperties(Material.ROCK, MaterialColor.SNOW)
            .properties(properties -> properties.setRequiresTool().hardnessAndResistance(1.5F, 6))
            .addLayer(() -> RenderType::getCutout)
            .color(() -> ZYColors::colorableBlockColor)
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .withExistingParent(ctx.getName(), provider.modLoc("block/zy_cube_all"))
                    .texture("all", provider.modLoc("block/zychorium_block"))))
            .tag(ZYTags.Blocks.COLORABLE)
            .simpleItem()
            .recipe((ctx, provider) ->
                    colorable(provider, DataIngredient.tag(ZYTags.Items.STORAGE_BLOCKS_ZYCHORIUM), ctx::getEntry))
            .register();

    public static final BlockEntry<ColorableBlock> THE_AUREY_BLOCK = REGISTRATE.block("the_aurey_block", ColorableBlock::new)
            .initialProperties(IMMORTAL_BLOCK)
            .addLayer(() -> RenderType::getCutout)
            .color(() -> ZYColors::colorableBlockColor)
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .withExistingParent(ctx.getName(), provider.modLoc("block/colorable_cube_all"))
                    .texture("base", provider.modLoc("block/" + ctx.getName()))
                    .texture("all", provider.modLoc("block/zychorium_block"))))
            .tag(ZYTags.Blocks.COLORABLE)
            .simpleItem()
            .recipe((ctx, provider) ->
                    colorable(provider, DataIngredient.items(ALUMINIUM_BLOCK), ctx::getEntry))
            .register();

    public static final Map<ViewerType, BlockEntry<ViewerBlock>> VIEWER = viewer(false);

    public static final Map<ViewerType, BlockEntry<ImmortalViewerBlock>> IMMORTAL_VIEWER = immortalViewer(false);

    public static final Map<ViewerType, BlockEntry<ViewerBlock>> PHANTOMIZED_VIEWER = viewer(true);

    public static final Map<ViewerType, BlockEntry<ImmortalViewerBlock>> PHANTOMIZED_IMMORTAL_VIEWER = immortalViewer(true);

    public static final Map<ZYType, BlockEntry<Block>> ZYCHORIZED_ENGINEERING_BLOCK = zyBlock("zychorized_{type}_engineering_block", (type, block) -> block
            .addLayer(() -> RenderType::getTranslucent)
            .tag(ZYTags.Blocks.ZYCHORIZED_ENGINEERING_BLOCK)
            .recipe((ctx, provider) ->
                    engineering(provider, DataIngredient.items(ZYCHORITE), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> ALUMINIZED_ENGINEERING_BLOCK = zyBlock("aluminized_{type}_engineering_block", (type, block) -> block
            .addLayer(() -> RenderType::getTranslucent)
            .tag(ZYTags.Blocks.ALUMINIZED_ENGINEERING_BLOCK)
            .recipe((ctx, provider) ->
                    engineering(provider, DataIngredient.items(ZYItems.ALUMINIUM), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final BlockEntry<BasicMachineBlock> ZYCHORIUM_WATER = basicMachine("zychorium_water", ZYType.BLUE);
    public static final BlockEntry<BasicMachineBlock> ZYCHORIUM_SOIL = basicMachine("zychorium_soil", ZYType.GREEN);
    public static final BlockEntry<BasicMachineBlock> FIRE_BASIN = basicMachine("fire_basin", ZYType.RED);
    public static final BlockEntry<BasicMachineBlock> FLUID_VOID = basicMachine("fluid_void", ZYType.DARK);
    public static final BlockEntry<BasicMachineBlock> ZYCHORIUM_ICE = basicMachine("zychorium_ice", ZYType.LIGHT);

    private static ImmutableMap<ZYType, BlockEntry<Block>> zyBlock(String pattern, NonNullBiFunction<ZYType, BlockBuilder<Block, Registrate>, BlockBuilder<Block, Registrate>> factory)
    {
        return zyBase(pattern, false, factory);
    }

    private static ImmutableMap<ZYType, BlockEntry<Block>> zyBricks(String pattern, NonNullBiFunction<ZYType, BlockBuilder<Block, Registrate>, BlockBuilder<Block, Registrate>> factory)
    {
        return zyBase(pattern, true, factory);
    }

    private static ImmutableMap<ZYType, BlockEntry<Block>> zyBase(String pattern, boolean bricks, NonNullBiFunction<ZYType, BlockBuilder<Block, Registrate>, BlockBuilder<Block, Registrate>> factory)
    {
        ImmutableMap.Builder<ZYType, BlockEntry<Block>> blocks = ImmutableMap.builder();
        String name = pattern.replace("{type}_", "");

        for (ZYType type : ZYType.VALUES)
        {
            blocks.put(type, factory.apply(type, REGISTRATE.block(pattern.replace("{type}", type.getString()), Block::new)
                    .initialProperties(Material.ROCK, type.mtlColor())
                    .properties(properties -> properties.setRequiresTool().hardnessAndResistance(1.5F, 6).setAllowsSpawn((state, world, pos, entity) -> false))
                    .addLayer(() -> RenderType::getCutout)
                    .color(() -> () -> ZYColors.zyBlockColor(type, bricks))
                    .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                            .withExistingParent(name, provider.modLoc("block/" + (bricks ? "zy_bricks" : "zy_cube_all")))
                            .texture("all", provider.modLoc("block/" + name)))))
                    .item()
                        .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + name)))
                        .color(() -> () -> ZYColors.zyItemColor(type, bricks))
                        .build()
                    .register());
        }

        return blocks.build();
    }

    private static ImmutableMap<ZYType, BlockEntry<Block>> solidZyBricks(String pattern, NonNullBiFunction<ZYType, BlockBuilder<Block, Registrate>, BlockBuilder<Block, Registrate>> factory)
    {
        ImmutableMap.Builder<ZYType, BlockEntry<Block>> blocks = ImmutableMap.builder();

        for (ZYType type : ZYType.VALUES)
        {
            blocks.put(type, factory.apply(type, REGISTRATE.block(pattern.replace("{type}", type.getString()), Block::new)
                    .initialProperties(Material.ROCK, type.mtlColor())
                    .properties(properties -> properties.setRequiresTool().hardnessAndResistance(1.5F, 6))
                    .simpleItem())
                    .register());
        }

        return blocks.build();
    }

    private static BlockEntry<ZychoriumLampBlock> lamp(boolean inverted)
    {
        String name = "zychorium_lamp";
        return REGISTRATE.block(inverted ? "inverted_" + name : name, properties -> new ZychoriumLampBlock(inverted, properties))
                .initialProperties(Material.ROCK, MaterialColor.SNOW)
                .properties(properties -> properties.hardnessAndResistance(0.3F, 6)
                        .setLightLevel(state -> state.get(BlockStateProperties.LIT) ? 15 : 0)
                        .sound(SoundType.GLASS))
                .addLayer(() -> RenderType::getCutout)
                .color(() -> ZYColors::colorableBlockColor)
                .blockstate((ctx, provider) ->
                {
                    if (!inverted)
                        provider.simpleBlock(ctx.getEntry(), provider.models()
                                .withExistingParent(ctx.getName(), provider.modLoc("block/zy_cube_all"))
                                .texture("all", provider.modLoc("block/" + ctx.getName())));
                    else
                        provider.simpleBlock(ctx.getEntry(), provider.models().getExistingFile(provider.modLoc(name)));
                })
                .tag(ZYTags.Blocks.ZYCHORIUM_LAMPS)
                .item()
                    .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + name)))
                    .color(() -> () -> ZYColors.zyLampItemColor(inverted))
                    .build()
                .recipe((ctx, provider) ->
                {
                    DataIngredient source = DataIngredient.items(Blocks.GLOWSTONE);

                    ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                            .key('S', source)
                            .key('P', Tags.Items.DUSTS_REDSTONE)
                            .key('#', inverted ? Ingredient.fromItems(Items.REDSTONE_TORCH) : Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE))
                            .key('R', ZYItems.ZYCHORIUM.get(ZYType.RED).get())
                            .key('G', ZYItems.ZYCHORIUM.get(ZYType.GREEN).get())
                            .key('B', ZYItems.ZYCHORIUM.get(ZYType.BLUE).get())
                            .key('D', ZYItems.ZYCHORIUM.get(ZYType.DARK).get())
                            .key('L', ZYItems.ZYCHORIUM.get(ZYType.LIGHT).get())
                            .patternLine("RGB")
                            .patternLine("PSP")
                            .patternLine("D#L")
                            .addCriterion("has_" + provider.safeName(source), source.getCritereon(provider))
                            .build(provider, provider.safeId(ctx.getEntry()));

                    if (!inverted)
                        colorable(ZYCraft.modLoc(name + "_from_" + provider.safeName(Blocks.REDSTONE_LAMP)), provider, DataIngredient.items(Blocks.REDSTONE_LAMP), ctx::getEntry);

                    ShapelessRecipeBuilder.shapelessRecipe(inverted ? ZYCHORIUM_LAMP.get() : INVERTED_ZYCHORIUM_LAMP.get())
                            .addIngredient(inverted ? INVERTED_ZYCHORIUM_LAMP.get() : ZYCHORIUM_LAMP.get())
                            .addIngredient(Ingredient.fromItems(Items.REDSTONE_TORCH))
                            .addCriterion("has_redstone_torch", RegistrateRecipeProvider.hasItem(Items.REDSTONE_TORCH))
                            .build(provider, ZYCraft.modLoc(inverted ? "zychorium_lamp_from_inverted_zychorium_lamp" : "inverted_zychorium_lamp_from_zychorium_lamp"));
                })
                .register();
    }

    private static ImmutableMap<ViewerType, BlockEntry<ViewerBlock>> viewer(boolean phantomized)
    {
        ImmutableMap.Builder<ViewerType, BlockEntry<ViewerBlock>> blocks = ImmutableMap.builder();

        for (ViewerType type : ViewerType.VALUES)
        {
            String name = type.getString() + "_viewer";

            blocks.put(type, REGISTRATE.block(phantomized ? "phantomized_" + name : name, properties -> new ViewerBlock(type, properties))
                    .initialProperties(() -> Blocks.GLASS)
                    .properties(properties -> type.properties(properties, phantomized))
                    .addLayer(() -> type::layer)
                    .blockstate((ctx, provider) ->
                    {
                        if (!phantomized)
                            provider.simpleBlock(ctx.getEntry());
                        else
                            provider.simpleBlock(ctx.getEntry(), provider.models().getExistingFile(provider.modLoc(name)));
                    })
                    .tag(type.tag(phantomized))
                    .item()
                        .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + name)))
                        .build()
                    .recipe((ctx, provider) ->
                    {
                        if (!phantomized)
                            if (type == ViewerType.GLASS)
                                ShapedRecipeBuilder.shapedRecipe(ctx.getEntry(), 8)
                                        .key('#', Tags.Items.GLASS)
                                        .key('X', type.ingredient())
                                        .patternLine("###")
                                        .patternLine("#X#")
                                        .patternLine("###")
                                        .addCriterion("has_" + provider.safeName(type.ingredient()), type.ingredient().getCritereon(provider))
                                        .build(provider, provider.safeId(ctx.getEntry()));
                            else
                                infused(provider, type.ingredient(), DataIngredient.items(VIEWER.get(ViewerType.GLASS)), ctx::getEntry);
                        else
                            infused(provider, DataIngredient.items(Items.PHANTOM_MEMBRANE), DataIngredient.items(VIEWER.get(type)), ctx::getEntry);
                    })
                    .register());
        }

        return blocks.build();
    }

    private static ImmutableMap<ViewerType, BlockEntry<ImmortalViewerBlock>> immortalViewer(boolean phantomized)
    {
        ImmutableMap.Builder<ViewerType, BlockEntry<ImmortalViewerBlock>> blocks = ImmutableMap.builder();
        String name = "immortal_viewer";

        for (ViewerType type : ViewerType.IMMORTAL_VALUES)
        {
            String registryName = type.getString() + "_viewer";

            blocks.put(type, REGISTRATE.block(phantomized ? "phantomized_" + registryName : registryName, properties -> new ImmortalViewerBlock(type, properties))
                    .initialProperties(() -> Blocks.GLASS)
                    .properties(properties -> type.properties(properties, phantomized))
                    .addLayer(() -> type::layer)
                    .color(() -> ZYColors::colorableBlockColor)
                    .blockstate((ctx, provider) ->
                    {
                        if (!phantomized && type == ViewerType.IMMORTAL)
                            provider.simpleBlock(ctx.getEntry(), provider.models()
                                    .withExistingParent(name, provider.modLoc("block/colorable_cube_all"))
                                    .texture("base", provider.modLoc("block/immortal_viewer_base"))
                                    .texture("all", provider.modLoc("block/immortal_viewer_overlay")));
                        else
                            provider.simpleBlock(ctx.getEntry(), provider.models().getExistingFile(provider.modLoc(name)));

                    })
                    .tag(type.tag(phantomized))
                    .item()
                        .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + name)))
                        .build()
                    .recipe((ctx, provider) ->
                    {
                        if (!phantomized)
                            if (type == ViewerType.IMMORTAL)
                                colorable(provider, DataIngredient.items(VIEWER.get(ViewerType.GLASS)), ctx::getEntry);
                            else
                                infused(provider, type.ingredient(), DataIngredient.items(IMMORTAL_VIEWER.get(ViewerType.IMMORTAL)), ctx::getEntry);
                        else
                            infused(provider, DataIngredient.items(Items.PHANTOM_MEMBRANE), DataIngredient.items(IMMORTAL_VIEWER.get(type)), ctx::getEntry);
                    })
                    .register());
        }

        return blocks.build();
    }

    private static BlockEntry<BasicMachineBlock> basicMachine(String name, ZYType type)
    {
        return REGISTRATE.block(name, properties -> new BasicMachineBlock(type, properties))
                .initialProperties(Material.ROCK, type.mtlColor())
                .properties(properties -> properties.setRequiresTool().hardnessAndResistance(1.5F, 6))
                .addLayer(() -> RenderType::getCutout)
                .color(() -> () -> ZYColors.zyBlockColor(type, false))
                .blockstate((ctx, provider) ->
                {
                    ModelFile model = null;

                    switch (type)
                    {
                        case BLUE:
                        case LIGHT:
                            model = provider.models()
                                    .withExistingParent(name, provider.modLoc("block/basic_machine"))
                                    .texture("side", provider.modLoc("block/" + name))
                                    .texture("top", provider.modLoc("block/basic_machine"));
                            break;
                        case GREEN:
                        case RED:
                            model = provider.models()
                                    .withExistingParent(name, provider.modLoc("block/basic_machine"))
                                    .texture("side", provider.modLoc("block/" + name + "_side"))
                                    .texture("top", provider.modLoc("block/" + name + "_top"));
                            break;
                        case DARK:
                            model = provider.models()
                                    .withExistingParent(name, provider.modLoc("block/zy_cube_all"))
                                    .texture("all", provider.modLoc("block/" + name));
                            break;
                    }

                    provider.simpleBlock(ctx.getEntry(), model);
                })
                .tag(ZYTags.Blocks.BASIC_MACHINES)
                .item()
                    .color(() -> () -> ZYColors.zyItemColor(type, false))
                    .tag(ZYTags.Items.BASIC_MACHINES)
                    .build()
                .recipe((ctx, provider) ->
                {
                    DataIngredient base = null;

                    switch (type)
                    {
                        case BLUE:
                            base = DataIngredient.items(Items.WATER_BUCKET);
                            break;
                        case GREEN:
                            base = DataIngredient.tag(ItemTags.SAPLINGS);
                            break;
                        case RED:
                            base = DataIngredient.tag(Tags.Items.GUNPOWDER);
                            break;
                        case DARK:
                            base = DataIngredient.items(Items.BUCKET);
                            break;
                        case LIGHT:
                            base = DataIngredient.items(Items.SNOWBALL);
                            break;
                    }

                    ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                            .key('#', DataIngredient.items(ZYCHORIZED_ENGINEERING_BLOCK.get(type)))
                            .key('T', type == ZYType.RED || type == ZYType.DARK ? DataIngredient.items(Items.IRON_BARS) : DataIngredient.items(ZYCHORITE))
                            .key('S', type == ZYType.DARK ? DataIngredient.items(Items.IRON_BARS) : DataIngredient.items(ZYCHORITE))
                            .key('X', base)
                            .patternLine("#T#")
                            .patternLine("SXS")
                            .patternLine("#S#")
                            .addCriterion("has_" + provider.safeName(base), base.getCritereon(provider))
                            .build(provider, provider.safeId(ctx.getEntry()));
                })
                .register();
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void storageBlock(RegistrateRecipeProvider provider, NonNullSupplier<? extends T> source, NonNullSupplier<? extends T> result)
    {
        storageBlock(provider, DataIngredient.items(source), result, source);
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void storageBlock(RegistrateRecipeProvider provider, DataIngredient source, NonNullSupplier<? extends T> result, NonNullSupplier<? extends T> reverseSource)
    {
        provider.square(source, result, false);
        provider.singleItemUnfinished(DataIngredient.items(result), reverseSource, 1, 9)
                .build(provider, provider.safeId(source) + "_from_" + provider.safeName(result.get()));
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void bricks(RegistrateRecipeProvider provider, DataIngredient source, NonNullSupplier<? extends T> result)
    {
        provider.square(source, result, true);
        provider.stonecutting(source, result);
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void smallBricks(RegistrateRecipeProvider provider, NonNullSupplier<? extends T> source, NonNullSupplier<? extends T> result)
    {
        smallBricks(provider, DataIngredient.items(source), result, source);
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void smallBricks(RegistrateRecipeProvider provider, DataIngredient source, NonNullSupplier<? extends T> result, NonNullSupplier<? extends T> reverseSource)
    {
        provider.square(source, result, true);
        ShapedRecipeBuilder.shapedRecipe(reverseSource.get())
                .key('X', result.get())
                .patternLine("XX")
                .patternLine("XX")
                .addCriterion("has_" + provider.safeName(result.get()), RegistrateRecipeProvider.hasItem(result.get()))
                .build(provider, provider.safeId(source) + "_from_" + provider.safeName(result.get()));
        provider.stonecutting(source, result);
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void infused(RegistrateRecipeProvider provider, DataIngredient infusionSource, DataIngredient source, Supplier<? extends T> result)
    {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 4)
                .key('I', infusionSource)
                .key('#', source)
                .patternLine("I#I")
                .patternLine("# #")
                .patternLine("I#I")
                .addCriterion("has_" + provider.safeName(infusionSource), infusionSource.getCritereon(provider))
                .build(provider, provider.safeId(result.get()));
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void engineering(RegistrateRecipeProvider provider, DataIngredient infusionSource, DataIngredient source, Supplier<? extends T> result)
    {
        DataIngredient core = DataIngredient.tag(Tags.Items.DUSTS_REDSTONE);

        ShapedRecipeBuilder.shapedRecipe(result.get(), 4)
                .key('I', infusionSource)
                .key('#', source)
                .key('X', core)
                .patternLine("I#I")
                .patternLine("#X#")
                .patternLine("I#I")
                .addCriterion("has_" + provider.safeName(core), core.getCritereon(provider))
                .build(provider, provider.safeId(result.get()));
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void colorable(RegistrateRecipeProvider provider, DataIngredient source, Supplier<? extends T> result)
    {
        colorable(provider.safeId(result.get()), provider, source, result);
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void colorable(ResourceLocation name, RegistrateRecipeProvider provider, DataIngredient source, Supplier<? extends T> result)
    {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 4)
                .key('#', source)
                .key('R', ZYItems.ZYCHORIUM.get(ZYType.RED).get())
                .key('G', ZYItems.ZYCHORIUM.get(ZYType.GREEN).get())
                .key('B', ZYItems.ZYCHORIUM.get(ZYType.BLUE).get())
                .key('D', ZYItems.ZYCHORIUM.get(ZYType.DARK).get())
                .key('L', ZYItems.ZYCHORIUM.get(ZYType.LIGHT).get())
                .patternLine("#L#")
                .patternLine("RGB")
                .patternLine("#D#")
                .addCriterion("has_" + provider.safeName(source), source.getCritereon(provider))
                .build(provider, name);
    }

    public static void init() {}
}
