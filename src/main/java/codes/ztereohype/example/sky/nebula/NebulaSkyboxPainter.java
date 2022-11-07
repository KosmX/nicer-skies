package codes.ztereohype.example.sky.nebula;

import codes.ztereohype.example.core.Gradient;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;

import java.awt.Color;

public class NebulaSkyboxPainter extends SkyboxPainter {
    private static final float SCALING_FACTOR = 1f;
    private static final float BASE_NOISE_AMOUNT = 0.56f; // the amount of base noise to keep

    private final Gradient nebulaGradient;

    public NebulaSkyboxPainter(PerlinNoise noise, Gradient nebula_gradient) {
        super(noise);
        nebulaGradient = nebula_gradient;
    }

    @Override
    int getColour(float x, float y, float z) {
        // Get projection
        float[] projCoords = this.projectOnSphere(x, y, z);
        x = projCoords[0];
        y = projCoords[1];
        z = projCoords[2];

        // Get offset
        float offset = (float) noise.getValue(x * SCALING_FACTOR * 3, y * SCALING_FACTOR * 3, z * SCALING_FACTOR * 3);
        x = Mth.clamp(x + offset/5f, -1f, 1f);
        y = Mth.clamp(y + offset/5f, -1f, 1f);
        z = Mth.clamp(z + offset/5f, -1f, 1f);

        // Value of noise at coord, 0..1
        double noiseValue = Mth.clamp(noise.getValue(x * SCALING_FACTOR, y * SCALING_FACTOR, z * SCALING_FACTOR) + 0.5, 0D, 1D);

        // Value to be subtracted from noise at coord, 0..1
        double subtractionValue = Mth.clamp(noise.getOctaveNoise(1).noise(x * SCALING_FACTOR, y * SCALING_FACTOR, z * SCALING_FACTOR) + 0.5, 0D, 1D);

        double[] derivates = new double[3];
        noise.getOctaveNoise(0).noiseWithDerivative(x * SCALING_FACTOR, y * SCALING_FACTOR, z * SCALING_FACTOR, derivates);

        // Find a base background colour to use (xyz interpoaltion across sky, gamer mode)
        int blueness, greenness, redness;
        blueness = (int) ((x/2 + 0.5) * 127);
        greenness = (int) ((y/2 + 0.5) * 127);
        redness = (int) ((z/2 + 0.5) * 127);

        int alpha = (int)(Mth.clamp((noiseValue * (1D / BASE_NOISE_AMOUNT) - (1D / BASE_NOISE_AMOUNT - 1)) * 255D, 20D, 254.99D)); // otherwise death occurs

        alpha = (int) Mth.clamp(alpha - subtractionValue * 128, 50, 255);

        double colourValue = (Mth.clamp((noiseValue * (1D / BASE_NOISE_AMOUNT) - (1D / BASE_NOISE_AMOUNT - 1)), 0.01D, 0.9999D));
        Color color = nebulaGradient.getAt(colourValue);
        double bgPresence = Mth.clamp(Math.log10(-colourValue + 1) + 1, 0D, 1D);

        int red, green, blue;
        red = Mth.clamp((int) ((colourValue * color.getRed()) + redness * bgPresence) - (int)(derivates[0] * colourValue * 127), 0, 255);
        green = Mth.clamp((int) ((colourValue * color.getGreen()) + greenness * bgPresence) - (int)(derivates[1] * colourValue * 64), 0, 255);
        blue = Mth.clamp((int) ((colourValue * color.getBlue()) + blueness * bgPresence) - (int)(derivates[2] * colourValue * 127), 0, 255);

        return FastColor.ARGB32.color(alpha, blue, green, red);
    }
}
