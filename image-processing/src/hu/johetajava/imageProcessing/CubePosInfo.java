package hu.johetajava.imageProcessing;

public class CubePosInfo {
    public double errorUnits;
    public MyColor cubeColor;
    public MyColor boxColor;

    public CubePosInfo(double errorUnits, MyColor cubeColor, MyColor boxColor) {
        this.errorUnits = errorUnits;
        this.cubeColor = cubeColor;
        this.boxColor = boxColor;
    }
}
