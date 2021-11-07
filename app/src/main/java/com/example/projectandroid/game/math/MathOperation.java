package com.example.projectandroid.game.math;

public class MathOperation {
    private final int x;
    private final int y;
    private final int z;
    private final Operation operationXY;
    private final Operation operationYZ;

    public MathOperation(int x, int y, int z, Operation operationXY, Operation operationYZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.operationXY = operationXY;
        this.operationYZ = operationYZ;
    }

    // not repeating same operation twice in an operation
    public int getOperationResult() {
        int result;

        switch (operationXY) {
            case SUM:
                if (operationYZ == Operation.PROD) {
                    result = x + y * z;
                } else {
                    result = x + y - z;
                }
                break;
            case DIFF:
                if (operationYZ == Operation.PROD) {
                    result = x - y * z;
                } else {
                    result = x - y + z;
                }
                break;
            case PROD:
                if (operationYZ == Operation.DIFF) {
                    result = x * y - z;
                } else {
                    result = x * y + z;
                }
                break;
            default:
                result = -1;
        }
        return result;
    }

    enum Operation {
        SUM, DIFF, PROD
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Operation getOperationXY() {
        return operationXY;
    }

    public Operation getOperationYZ() {
        return operationYZ;
    }

    public String convertEnumOperationToString(Operation op) {
        switch (op) {
            case SUM:
                return "+";
            case PROD:
                return "*";
            case DIFF:
                return "-";
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return "MathOperation{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", operationXY=" + operationXY +
                ", operationYZ=" + operationYZ +
                '}';
    }
}
