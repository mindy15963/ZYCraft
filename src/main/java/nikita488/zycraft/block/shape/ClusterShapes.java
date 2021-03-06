package nikita488.zycraft.block.shape;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import nikita488.zycraft.block.QuartzCrystalClusterBlock;

import java.util.EnumMap;
import java.util.Map;

public class ClusterShapes {
    public static final VoxelShape[] DOWN = new VoxelShape[]
            {
                    Block.makeCuboidShape(6, 6, 6, 10, 16, 10),
                    Block.makeCuboidShape(6, 6, 6, 10, 16, 14),
                    Block.makeCuboidShape(2, 6, 6, 10, 16, 14),
                    Block.makeCuboidShape(2, 6, 6, 14, 16, 14),
                    Block.makeCuboidShape(2, 6, 2, 14, 16, 14)
            };

    public static final VoxelShape[] UP = new VoxelShape[]
            {
                    Block.makeCuboidShape(6, 0, 6, 10, 10, 10),
                    Block.makeCuboidShape(6, 0, 2, 10, 10, 10),
                    Block.makeCuboidShape(2, 0, 2, 10, 10, 10),
                    Block.makeCuboidShape(2, 0, 2, 14, 10, 10),
                    Block.makeCuboidShape(2, 0, 2, 14, 10, 14)
            };

    public static final VoxelShape[] NORTH = new VoxelShape[]
            {
                    Block.makeCuboidShape(6, 6, 6, 10, 10, 16),
                    Block.makeCuboidShape(6, 6, 6, 10, 14, 16),
                    Block.makeCuboidShape(6, 6, 6, 14, 14, 16),
                    Block.makeCuboidShape(2, 6, 6, 14, 14, 16),
                    Block.makeCuboidShape(2, 2, 6, 14, 14, 16)
            };

    public static final VoxelShape[] SOUTH = new VoxelShape[]
            {
                    Block.makeCuboidShape(6, 6, 0, 10, 10, 10),
                    Block.makeCuboidShape(6, 6, 0, 10, 14, 10),
                    Block.makeCuboidShape(2, 6, 0, 10, 14, 10),
                    Block.makeCuboidShape(2, 6, 0, 14, 14, 10),
                    Block.makeCuboidShape(2, 2, 0, 14, 14, 10)
            };

    public static final VoxelShape[] WEST = new VoxelShape[]
            {
                    Block.makeCuboidShape(6, 6, 6, 16, 10, 10),
                    Block.makeCuboidShape(6, 6, 6, 16, 14, 10),
                    Block.makeCuboidShape(6, 6, 2, 16, 14, 10),
                    Block.makeCuboidShape(6, 6, 2, 16, 14, 14),
                    Block.makeCuboidShape(6, 2, 2, 16, 14, 14)
            };

    public static final VoxelShape[] EAST = new VoxelShape[]
            {
                    Block.makeCuboidShape(0, 6, 6, 10, 10, 10),
                    Block.makeCuboidShape(0, 6, 6, 10, 14, 10),
                    Block.makeCuboidShape(0, 6, 6, 10, 14, 14),
                    Block.makeCuboidShape(0, 6, 2, 10, 14, 14),
                    Block.makeCuboidShape(0, 2, 2, 10, 14, 14)
            };

    private static final Map<Direction, VoxelShape[]> SHAPES = new EnumMap<>(Direction.class);

    static {
        SHAPES.put(Direction.DOWN, DOWN);
        SHAPES.put(Direction.UP, UP);
        SHAPES.put(Direction.NORTH, NORTH);
        SHAPES.put(Direction.SOUTH, SOUTH);
        SHAPES.put(Direction.WEST, WEST);
        SHAPES.put(Direction.EAST, EAST);
    }

    public static VoxelShape get(BlockState state)
    {
        return SHAPES.get(state.get(QuartzCrystalClusterBlock.FACING))[state.get(QuartzCrystalClusterBlock.AMOUNT) - 1];
    }
}
